package app.cleancode.bytecode_to_asm;

import org.objectweb.asm.ClassReader;

public class App {
    public static void main(String[] args) {
        Test.class.getClassLoader()
                .getResourceAsStream(Test.class.getName().replace('.', '/').concat(".class"));
        try {
            ClassReader reader = new ClassReader(Test.class.getName());
            System.out.println(reader.getClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
