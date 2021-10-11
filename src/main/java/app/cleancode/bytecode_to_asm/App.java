package app.cleancode.bytecode_to_asm;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.objectweb.asm.ClassReader;
import app.cleancode.bytecode_to_asm.asm.Assembler;
import app.cleancode.bytecode_to_asm.asm.AssemblyWriter;

public class App {
    public static void main(String[] args) {
        try {
            String classFileName = "Test.class";
            ClassReader reader = new ClassReader(Test.class.getName());
            ClassInspector inspector = new ClassInspector();
            reader.accept(inspector, 0);
            AssemblyWriter assemblyWriter = new AssemblyWriter();
            ByteArrayOutputStream assemblyOutputStream = new ByteArrayOutputStream(65536);
            assemblyWriter.writeAssembly(inspector.info, assemblyOutputStream, "Test.java");
            boolean shouldAssemble = true;
            for (String arg : args) {
                if (arg.equals("-S")) {
                    shouldAssemble = false;
                }
            }
            if (shouldAssemble) {
                Assembler assembler = new Assembler();
                assembler.assemble(assemblyOutputStream.toByteArray(),
                        classFileName.replace(".class", ".o"));
            } else {
                Files.write(Paths.get(classFileName.replace(".class", ".S")),
                        assemblyOutputStream.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
