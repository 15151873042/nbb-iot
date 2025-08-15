package io.github.nbb.iot.gateway.framework.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 包装Socket，增加连接池所需的元数据（如最后使用时间）
 */
public class PooledSocket {
    private final Socket socket;
    private long lastUsedTime; // 最后使用时间（毫秒）

    public PooledSocket(Socket socket) {
        this.socket = socket;
        this.lastUsedTime = System.currentTimeMillis();
    }

    // 更新最后使用时间
    public void updateLastUsedTime() {
        this.lastUsedTime = System.currentTimeMillis();
    }

    // 获取输出流（委托给原始Socket）
    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    // 获取输入流（委托给原始Socket）
    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    // 检查连接是否关闭
    public boolean isClosed() {
        return socket.isClosed();
    }

    // 获取原始Socket（谨慎使用）
    public Socket getSocket() {
        return socket;
    }

    // 获取最后使用时间
    public long getLastUsedTime() {
        return lastUsedTime;
    }

    // 禁止直接关闭（需通过连接池管理）
    public void close() throws IOException {
        throw new UnsupportedOperationException("请通过连接池归还连接，而非直接关闭");
    }
}
