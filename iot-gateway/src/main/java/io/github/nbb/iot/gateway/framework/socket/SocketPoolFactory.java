package io.github.nbb.iot.gateway.framework.socket;

import io.github.nbb.iot.gateway.framework.socket.SocketConnectionPool;

public class SocketPoolFactory {
    private static SocketConnectionPool pool;

    // 初始化连接池（建议在Spring Boot启动时调用）
    public static synchronized SocketConnectionPool initPool(String host, int port) {
        if (pool == null) {
            // 连接池配置（可从配置文件读取）
            int maxTotal = 50;        // 最大连接数
            int maxIdle = 20;         // 最大空闲连接数
            int minIdle = 5;          // 最小空闲连接数
            long maxWaitMillis = 3000; // 获取连接的最大等待时间（3秒）
            long idleTimeout = 300000; // 连接最大空闲时间（5分钟）

            pool = new SocketConnectionPool(host, port, maxTotal, maxIdle, minIdle, maxWaitMillis, idleTimeout);
        }
        return pool;
    }

    // 获取连接池实例
    public static SocketConnectionPool getPool() {
        if (pool == null) {
            throw new IllegalStateException("连接池未初始化，请先调用initPool()");
        }
        return pool;
    }
}
