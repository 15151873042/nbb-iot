package com.nbb.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 胡鹏
 */
@Slf4j
public class BIOServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(6666);
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        while (true) {
            Socket socket = serverSocket.accept(); // 阻塞方法，等待连接连入，有连接进来时则继续执行
            log.info("====进来一个连接====");
            threadPool.execute(() -> BIOServer.handle(socket));
        }
    }



    @SneakyThrows
    private static void handle(Socket socket) {
        try (InputStream inputStream = socket.getInputStream();OutputStream outputStream = socket.getOutputStream()){
            byte[] bytes = new byte[1024];
            while (true) {
                int read = inputStream.read(bytes);
                if (read == -1) {
                    log.info("====读取到到达流结尾，数据读完了====");
                    break; // 读取到达达流的末尾
                }
                String receiveMessage = new String(bytes, 0, read);
                log.info("====读取到client端发送的信息：{}====", receiveMessage);
                outputStream.write(("服务端已收到数据" + receiveMessage).getBytes());
            }
        } catch (Exception e) {
            log.info("====读数据出错了====");
            log.error(e.getMessage(), e);
        } finally {
            socket.close();
        }
    }

}
