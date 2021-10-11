package app.cleancode.bytecode_to_asm;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.objectweb.asm.ClassReader;
import app.cleancode.bytecode_to_asm.asm.AssemblyWriter;

public class App {
    public static void main(String[] args) {
        try {
            ClassReader reader = new ClassReader(Test.class.getName());
            ClassInspector inspector = new ClassInspector();
            reader.accept(inspector, 0);
            AssemblyWriter assemblyWriter = new AssemblyWriter();
            ByteArrayOutputStream assemblyOutputStream = new ByteArrayOutputStream(65536);
            assemblyWriter.writeAssembly(inspector.info, assemblyOutputStream, "Test.java");
            System.out.println(
                    new String(assemblyOutputStream.toByteArray(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
