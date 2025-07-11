package io.github.nbb.iot.gateway.framework.netty;

import cn.hutool.core.util.HexUtil;
import io.github.nbb.iot.common.domain.IotSerialDO;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可重连的netty客户端
 */
@Slf4j
public class ReconnectableNettyClient {

    private final String host;
    private final int port;
    private NioEventLoopGroup group;
    private Channel channel;
    private final Bootstrap bootstrap;
    /** 心跳线程 */
    private final ScheduledExecutorService  heartbeatExecutor;
    /** 重连调度线程 */
    private final ScheduledExecutorService reconnectExecutor;
    /** 是否正在重连 */
    private final AtomicBoolean isReconnecting = new AtomicBoolean(false);
    /** 连接当前装备 */
    private final AtomicBoolean isOnline = new AtomicBoolean(false);
    /** 已重连次数 */
    private final AtomicInteger reconnectAttempts = new AtomicInteger(0);
    /** 最大重连延迟时间(秒) */
    private static final int MAX_RECONNECT_DELAY = 60; // 最大重连延迟(秒)

    private ScheduledFuture<?> reconnectFuture; // 记录重连任务句柄


    public ReconnectableNettyClient(IotSerialDO serialServerInfo) {
        this.host = serialServerInfo.getHost();
        this.port = serialServerInfo.getPort();
        this.heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
        this.reconnectExecutor = Executors.newSingleThreadScheduledExecutor();
        this.bootstrap = createBootstrap();

        this.startConnect();
        this.startHeartbeat();

    }

    private Bootstrap createBootstrap() {
        Bootstrap b = new Bootstrap();
        group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000) // 2秒超时
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
//                                new StringDecoder(),
//                                new StringEncoder(),
                                new ConnectionHandler() // 处理连接状态
                        );
                    }
                });
        return b;
    }

    public void init() {
        startConnect();
        startHeartbeat();
    }

    private void startHeartbeat() {
        heartbeatExecutor.scheduleAtFixedRate(() -> {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 2000);
                isOnline.set(true);
            } catch (IOException e) {
                isOnline.set(false);
            }
        }, 5, 5, TimeUnit.SECONDS);
    }


    // 启动客户端
    public synchronized void startConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }


        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();

            log.info("第【{}】次，连接串口服务器【{}:{}】成功", reconnectAttempts.get(), host, port);
            isOnline.set(true);
            reconnectAttempts.set(0); // 重置重连计数
        } catch (Exception e) {
            log.error("第【{}】次，连接串口服务器【{}:{}】失败，报错信息为：【{}】；准备再次连接", reconnectAttempts.get(), host, port, e.getMessage());
            isOnline.set(false);
            scheduleReconnect();
        }
    }


    // 发送消息
    public void sendMessage(String message) {
        if (channel != null && channel.isActive()) {
            byte[] bytes = HexUtil.decodeHex(message);
            channel.writeAndFlush(Unpooled.copiedBuffer(bytes)).addListener(future -> {
                if (future.isSuccess()) {
                    log.info("消息成功发送到串口服务器");
                } else {
                    log.error("消息发送到串口服务器失败");
                }
            });
            log.warn("发送消息给串口服务器【{}:{}】成功，消息内容为【{}】", host, port, message);
        } else {
            log.warn("串口服务器【{}:{}】的通道失去连接，无法发送消息", host, port);
            startConnect(); // 尝试重连
        }
    }


    // 关闭客户端
    public void shutdown() {
        isReconnecting.set(true); // 阻止重连
        if (channel != null) {
            channel.close();
            channel = null;
        }
        if (group != null) {
            group.shutdownGracefully();
        }
        reconnectExecutor.shutdown();
    }


    // 安排重连任务(带指数退避)
    private void scheduleReconnect() {
        // 取消之前已提交但未执行的重连任务
        if (reconnectFuture != null && !reconnectFuture.isDone()) {
            reconnectFuture.cancel(false); // 非强制取消（不中断线程）
        }

        int attempts = reconnectAttempts.incrementAndGet();
        int delay = Math.min((int) Math.pow(2, attempts), MAX_RECONNECT_DELAY);

        log.info("第【{}】次，准备重连串口服务器【{}:{}】，【{}】秒后执行", attempts, host, port, delay);


        reconnectFuture = reconnectExecutor.schedule(() -> {
            try {
                log.info("第【{}】次，正在重连串口服务器【{}:{}】，连接中。。。。", attempts, host, port);
                startConnect(); // 尝试重连
            } finally {
                isReconnecting.set(false);
                reconnectFuture = null; // 清空句柄
            }
        }, delay, TimeUnit.SECONDS);

        isReconnecting.set(true); // 设置为正在重连
    }



    // 连接状态处理器
    private class ConnectionHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String str = HexUtil.encodeHexStr(bytes);
            log.info("====接收到串口服务器【{}:{}】返回的消息【{}】====", host, port, str);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            log.error("串口服务器【{}:{}】连接丢失，准备重连", host, port);
            ctx.close(); // 确保Channel关闭
            scheduleReconnect();
            super.channelInactive(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            log.error("串口服务器【{}:{}】连接异常出错，错误信息为【{}】", host, port, cause.getMessage());
            ctx.close(); // 关闭Channel触发重连
        }
    }
}
