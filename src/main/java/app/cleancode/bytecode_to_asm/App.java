package app.cleancode.bytecode_to_asm;

import org.objectweb.asm.ClassReader;

public class App {
    public static void main(String[] args) {
        try {
            ClassReader reader = new ClassReader(Test.class.getName());
            reader.accept(new ClassInspector(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
