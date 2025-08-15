package com.dynamic;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.SimpleCompiler;

import java.lang.reflect.Method;

/**
 * @author 胡鹏
 */
public class DynamicTest {

    public static void main(String[] args) throws Exception {

        String code1 = "package com.dynamic;\n" +
                "\n" +
                "public class Dynamic1 {\n" +
                "    public void test() {\n" +
                "        System.out.println(\"Dynamic1\");\n" +
                "    }\n" +
                "}\n";

        String code2 = "\n" +
                "\n" +
                "public class Dynamic2 {\n" +
                "    public void test() {\n" +
                "        System.out.println(\"Dynamic2\");\n" +
                "    }\n" +
                "}\n";


        // 2. 使用 Janino 编译上述代码
        SimpleCompiler compiler = new SimpleCompiler();
        compiler.cook(code1 + code2);
        // 3. 加载编译后的类
        Class<?> dynamicClass1 = compiler.getClassLoader().loadClass("com.dynamic.Dynamic1");
        Class<?> dynamicClass2 = compiler.getClassLoader().loadClass("com.dynamic.Dynamic2");

        // 4. 实例化类并调用 sendMessage 方法
        Object instance1 = dynamicClass1.newInstance();
        Object instance2 = dynamicClass2.newInstance();
        Method method1 = dynamicClass1.getMethod("test");
        Method method2 = dynamicClass2.getMethod("test");
        Object invoke = method1.invoke(instance1);
        Object invoke2 = method2.invoke(instance2);


    }
}
