package io.github.nbb.iot.gateway.framework.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(SocketConnectionPool.class);

    // 服务器地址和端口
    private final String host;
    private final int port;
    // 连接池配置
    private final int maxTotal;        // 最大连接数
    private final int maxIdle;         // 最大空闲连接数
    private final int minIdle;         // 最小空闲连接数
    private final long maxWaitMillis;  // 获取连接的最大等待时间
    private final long idleTimeout;    // 连接最大空闲时间（超过则关闭）

    // 空闲连接池（阻塞队列，线程安全）
    private final BlockingQueue<PooledSocket> idleConnections;
    // 活跃连接计数器（跟踪正在使用的连接）
    private final AtomicInteger activeCount = new AtomicInteger(0);
    // 定时任务线程池（用于健康检查和清理）
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public SocketConnectionPool(String host, int port, int maxTotal, int maxIdle, int minIdle,
                                long maxWaitMillis, long idleTimeout) {
        this.host = host;
        this.port = port;
        this.maxTotal = maxTotal;
        this.maxIdle = maxIdle;
        this.minIdle = minIdle;
        this.maxWaitMillis = maxWaitMillis;
        this.idleTimeout = idleTimeout;
        // 初始化空闲连接池（基于数组的阻塞队列，FIFO顺序）
        this.idleConnections = new ArrayBlockingQueue<>(maxIdle);

        // 初始化最小空闲连接
        initializeMinIdleConnections();
        // 启动定时清理任务（检查空闲超时连接）
        startCleanupTask();
    }

    /**
     * 初始化最小空闲连接
     */
    private void initializeMinIdleConnections() {
        for (int i = 0; i < minIdle; i++) {
            try {
                PooledSocket socket = createNewConnection();
                idleConnections.offer(socket);
            } catch (IOException e) {
                logger.error("初始化连接失败", e);
            }
        }
    }

    /**
     * 启动定时清理任务：移除空闲超时的连接
     */
    private void startCleanupTask() {
        // 每30秒执行一次清理
        scheduler.scheduleAtFixedRate(() -> {
            try {
                cleanupIdleConnections();
            } catch (Exception e) {
                logger.error("清理空闲连接失败", e);
            }
        }, 30, 30, TimeUnit.SECONDS);
    }

    /**
     * 清理空闲超时的连接
     */
    private void cleanupIdleConnections() {
        PooledSocket socket;
        while ((socket = idleConnections.poll()) != null) {
            // 检查连接是否空闲超时
            if (System.currentTimeMillis() - socket.getLastUsedTime() > idleTimeout) {
                closeConnection(socket);
                logger.info("关闭空闲超时连接: {}", socket);
            } else {
                // 未超时的连接放回队列（因poll()已移除，需重新放入）
                idleConnections.offer(socket);
                break; // 队列按时间排序，后续连接更晚超时，无需继续检查
            }
        }

        // 补充最小空闲连接
        int currentIdle = idleConnections.size();
        if (currentIdle < minIdle) {
            int need = minIdle - currentIdle;
            for (int i = 0; i < need; i++) {
                try {
                    PooledSocket newSocket = createNewConnection();
                    idleConnections.offer(newSocket);
                    logger.info("补充空闲连接，当前空闲数: {}", idleConnections.size());
                } catch (IOException e) {
                    logger.error("补充空闲连接失败", e);
                }
            }
        }
    }

    /**
     * 创建新的Socket连接
     */
    private PooledSocket createNewConnection() throws IOException {
        Socket socket = new Socket(host, port);
        // 设置Socket参数（根据需求调整）
        socket.setTcpNoDelay(true); // 禁用Nagle算法，减少延迟
        socket.setKeepAlive(true);  // 启用TCP保活机制
        return new PooledSocket(socket);
    }

    /**
     * 从连接池获取连接
     */
    public PooledSocket getConnection() throws InterruptedException, IOException, TimeoutException {
        // 1. 先从空闲池获取
        PooledSocket socket = idleConnections.poll();
        if (socket != null) {
            // 检查连接是否有效
            if (isConnectionValid(socket)) {
                activeCount.incrementAndGet();
                socket.updateLastUsedTime(); // 更新使用时间
                return socket;
            } else {
                // 无效连接直接关闭
                closeConnection(socket);
                logger.warn("移除无效连接");
            }
        }

        // 2. 空闲池无可用连接，检查是否可创建新连接
        if (activeCount.get() < maxTotal) {
            try {
                PooledSocket newSocket = createNewConnection();
                activeCount.incrementAndGet();
                newSocket.updateLastUsedTime();
                return newSocket;
            } catch (IOException e) {
                logger.error("创建新连接失败", e);
                throw e;
            }
        }

        // 3. 达到最大连接数，等待空闲连接
        socket = idleConnections.poll(maxWaitMillis, TimeUnit.MILLISECONDS);
        if (socket == null) {
            throw new TimeoutException("获取连接超时，最大等待时间: " + maxWaitMillis + "ms");
        }

        // 再次检查连接有效性
        if (!isConnectionValid(socket)) {
            closeConnection(socket);
            throw new IOException("获取到无效连接");
        }

        activeCount.incrementAndGet();
        socket.updateLastUsedTime();
        return socket;
    }

    /**
     * 归还连接到连接池
     */
    public void returnConnection(PooledSocket socket) {
        if (socket == null || socket.isClosed()) {
            activeCount.decrementAndGet();
            return;
        }

        // 检查连接是否有效且未超过最大空闲数
        if (isConnectionValid(socket) && idleConnections.size() < maxIdle) {
            socket.updateLastUsedTime();
            idleConnections.offer(socket);
        } else {
            // 连接无效或空闲池已满，直接关闭
            closeConnection(socket);
        }
        activeCount.decrementAndGet();
    }

    /**
     * 检查连接是否有效
     */
    private boolean isConnectionValid(PooledSocket socket) {
        if (socket.isClosed()) {
            return false;
        }
        Socket rawSocket = socket.getSocket();
        try {
            // 检查Socket是否还保持连接（通过发送紧急数据或读取方式，根据服务器支持调整）
            return rawSocket.isConnected() && !rawSocket.isInputShutdown() && !rawSocket.isOutputShutdown();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 关闭连接
     */
    private void closeConnection(PooledSocket socket) {
        try {
            if (!socket.isClosed()) {
                socket.getSocket().close();
            }
        } catch (IOException e) {
            logger.error("关闭连接失败", e);
        }
    }

    /**
     * 关闭连接池
     */
    public void close() {
        scheduler.shutdown();
        // 关闭所有空闲连接
        PooledSocket socket;
        while ((socket = idleConnections.poll()) != null) {
            closeConnection(socket);
        }
        logger.info("连接池已关闭");
    }

    // 连接池状态信息（用于监控）
    public String getStatus() {
        return String.format("连接池状态: 活跃=%d, 空闲=%d, 最大=%d",
                activeCount.get(), idleConnections.size(), maxTotal);
    }
}
