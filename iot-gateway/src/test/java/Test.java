import cn.hutool.core.util.HexUtil;

public class Test {

    public static void main(String[] args) {
        byte[] bytes = HexUtil.decodeHex("01 03 00 00 00 02 c4 0b");
        String str = HexUtil.encodeHexStr(bytes);

        System.out.println(str);
    }
}
