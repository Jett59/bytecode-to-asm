package app.cleancode.bytecode_to_asm.asm;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class Assembler {
    public void assemble(byte[] assembly, String outputFile, Optional<String> assemblerProgram)
            throws Exception {
        String assemblerCommand = assemblerProgram.orElse("as") + " -o " + outputFile + " -c -";
        System.out.println("Assembling with " + assemblerCommand);
        Process assemblerProcess = Runtime.getRuntime().exec(assemblerCommand);
        OutputStream assemblerStdin = assemblerProcess.getOutputStream();
        assemblerStdin.write(assembly);
        assemblerStdin.flush();
        assemblerStdin.close();
        int result = assemblerProcess.waitFor();
        if (result != 0) {
            System.err.println(new String(assemblerProcess.getErrorStream().readAllBytes(),
                    StandardCharsets.UTF_8));
            Files.write(Paths.get(outputFile + ".s"), assembly);
            throw new RuntimeException(
                    "Assembling failed! written assembly code to " + outputFile + ".s");
        }
    }
}
