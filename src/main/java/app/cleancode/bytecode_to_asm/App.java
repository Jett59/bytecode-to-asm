package app.cleancode.bytecode_to_asm;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.objectweb.asm.ClassReader;
import app.cleancode.bytecode_to_asm.asm.Assembler;
import app.cleancode.bytecode_to_asm.asm.AssemblyWriter;

public class App {
    public static void main(String[] args) {
        try {
            boolean shouldAssemble = true;
            Optional<String> assemblerProgram = Optional.empty();
            List<String> inputFiles = new ArrayList<>();
            for (String arg : args) {
                if (arg.equals("-S")) {
                    shouldAssemble = false;
                }
                if (arg.startsWith("-massembler=")) {
                    assemblerProgram = Optional.of(arg.substring("-massemblr=".length() + 1));
                }
                if (Files.exists(Paths.get(arg))) {
                    inputFiles.add(arg);
                }
            }
            if (inputFiles.size() < 1) {
                System.err.println("Error: No input files");
                System.exit(-1);
            }
            for (String classFileName : inputFiles) {
                ClassReader reader = new ClassReader(Files.readAllBytes(Paths.get(classFileName)));
                ClassInspector inspector = new ClassInspector();
                reader.accept(inspector, 0);
                AssemblyWriter assemblyWriter = new AssemblyWriter();
                ByteArrayOutputStream assemblyOutputStream = new ByteArrayOutputStream(65536);
                assemblyWriter.writeAssembly(inspector.info, assemblyOutputStream, "Test.java");
                if (shouldAssemble) {
                    Assembler assembler = new Assembler();
                    assembler.assemble(assemblyOutputStream.toByteArray(),
                            classFileName.replace(".class", ".o"), assemblerProgram);
                } else {
                    Files.write(Paths.get(classFileName.replace(".class", ".S")),
                            assemblyOutputStream.toByteArray());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
