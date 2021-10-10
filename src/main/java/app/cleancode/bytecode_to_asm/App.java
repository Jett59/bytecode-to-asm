package app.cleancode.bytecode_to_asm;

import org.objectweb.asm.ClassReader;
import app.cleancode.bytecode_to_asm.asm.AssemblyWriter;

public class App {
    public static void main(String[] args) {
        try {
            ClassReader reader = new ClassReader(Test.class.getName());
            ClassInspector inspector = new ClassInspector();
            reader.accept(inspector, 0);
            AssemblyWriter assemblyWriter = new AssemblyWriter();
            assemblyWriter.writeAssembly(inspector.info, System.err, "Test.java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
