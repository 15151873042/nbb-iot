package com.nbb.test;

import cn.hutool.core.io.IoUtil;
import cn.hutool.socket.SocketUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author 胡鹏
 */
public class SocketTest {

    public String sendMessage(String host, int port) throws IOException {
        String requestString = "001122";
        // 创建并连接Socket，使用Hutool的SocketUtil简化操作
        Socket socket = SocketUtil.connect(host, port, 2000);

        OutputStream outputStream = socket.getOutputStream();
        IoUtil.writeUtf8(outputStream, false, requestString);

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int read = inputStream.read(bytes);
        String receiveMessage = new String(bytes, 0, read);

        outputStream.close();
        inputStream.close();
        socket.close();

        System.out.println("获取到服务端返回的消息: " + receiveMessage);
        return receiveMessage;
    }

    public static void main(String[] args) throws IOException {
        new SocketTest().sendMessage("127.0.0.1", 6666);
    }
}
