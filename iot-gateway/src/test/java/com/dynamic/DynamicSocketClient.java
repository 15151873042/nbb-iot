package com.dynamic;

import cn.hutool.core.io.IoUtil;
import cn.hutool.socket.SocketUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DynamicSocketClient {
    // 发送消息到 TCP 服务器并返回响应
    public String sendMessage(String host, int port, String serialAddressCode) throws Exception {
        String sendMessage = serialAddressCode;
        Socket socket = SocketUtil.connect(host, port, 2000);

        OutputStream outputStream = socket.getOutputStream();
        IoUtil.writeUtf8(outputStream, false, sendMessage);

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024 * 1024 * 8];
        int read = inputStream.read(bytes);
        String receiveMessage = new String(bytes, 0, read);
        System.out.println("获取到服务端返回的消息: " + receiveMessage);
        outputStream.close();
        inputStream.close();
        socket.close();
        return receiveMessage;
    }
}
