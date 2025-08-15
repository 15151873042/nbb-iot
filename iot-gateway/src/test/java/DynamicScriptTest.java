import org.codehaus.janino.SimpleCompiler;

import java.lang.reflect.Method;

/**
 * 执行java动态脚本
 *
 * @author 胡鹏
 */
public class DynamicScriptTest {


    public static void main(String[] args) throws Exception {
        // 1. 定义要动态编译的 Socket 客户端代码
        // 功能：连接 TCP 服务器，发送消息，接收响应并返回
        String socketCode = "package com.dynamic;\n" +
                "\n" +
                "import cn.hutool.core.io.IoUtil;\n" +
                "import cn.hutool.socket.SocketUtil;\n" +
                "\n" +
                "import java.io.InputStream;\n" +
                "import java.io.OutputStream;\n" +
                "import java.net.Socket;\n" +
                "\n" +
                "public class DynamicSocketClient {\n" +
                "    // 发送消息到 TCP 服务器并返回响应\n" +
                "    public String sendMessage(String host, int port, String serialAddressCode) throws Exception {\n" +
                "        String sendMessage = serialAddressCode;\n" +
                "        Socket socket = SocketUtil.connect(host, port, 2000);\n" +
                "\n" +
                "        OutputStream outputStream = socket.getOutputStream();\n" +
                "        IoUtil.writeUtf8(outputStream, false, sendMessage);\n" +
                "\n" +
                "        InputStream inputStream = socket.getInputStream();\n" +
                "        byte[] bytes = new byte[1024 * 1024 * 8];\n" +
                "        int read = inputStream.read(bytes);\n" +
                "        String receiveMessage = new String(bytes, 0, read);\n" +
                "        System.out.println(\"获取到服务端返回的消息: \" + receiveMessage);\n" +
                "        outputStream.close();\n" +
                "        inputStream.close();\n" +
                "        socket.close();\n" +
                "        return receiveMessage;\n" +
                "    }\n" +
                "}\n";


        // 2. 使用 Janino 编译上述代码
        SimpleCompiler compiler = new SimpleCompiler();
        compiler.cook(socketCode); // 编译代码

        // 3. 加载编译后的类
        Class<?> dynamicClass = compiler.getClassLoader().loadClass("com.dynamic.DynamicSocketClient");

        // 4. 实例化类并调用 sendMessage 方法
        Object clientInstance = dynamicClass.newInstance();
        Method sendMethod = dynamicClass.getMethod("sendMessage", String.class, int.class, String.class);

        // 5. 执行 Socket 请求（替换为实际的 TCP 服务器地址、端口和消息）
        String host = "localhost";
        int port = 6666;
        String message = "张三";
        try {
            String response = (String) sendMethod.invoke(clientInstance, host, port, message);
            System.out.println("服务器响应：" + response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
