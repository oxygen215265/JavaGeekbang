package jvmclassloader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            MyClassLoader classLoader = new MyClassLoader();
            Class<?> hellpClass = classLoader.loadClass("Hello");
            Method method = hellpClass.getMethod("hello");
            method.invoke(hellpClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("G:\\javaWork\\Hello.xlass"));
            for (int i = 0; i < bytes.length; i++) {
                byte temp = bytes[i];
                bytes[i] = (byte) (255-temp);
            }
            //verify result
            Files.write(Paths.get("G:\\javaWork\\Hello.class"), bytes, StandardOpenOption.CREATE);
            return defineClass(name,bytes,0,bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


}
