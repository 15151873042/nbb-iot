import cn.hutool.core.util.HexUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Test {

    public static void main(String[] args) {
//        byte[] bytes = HexUtil.decodeHex("01 03 00 00 00 02 c4 0b");
//        String str = HexUtil.encodeHexStr(bytes);
//        System.out.println(str);

        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("192.168.1.200", 4197), 2000);

        } catch (IOException e) {
            System.out.println("服务连接");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
