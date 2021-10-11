package app.cleancode.bytecode_to_asm.asm;

import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Assembler {
    public void assemble(byte[] assembly, String outputFile) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("as", "-o", outputFile);
        processBuilder.redirectError(Redirect.INHERIT);
        Process assemblerProcess = processBuilder.start();
        OutputStream assemblerStdin = assemblerProcess.getOutputStream();
        assemblerStdin.write(assembly);
        assemblerStdin.flush();
        assemblerStdin.close();
        int result = assemblerProcess.waitFor();
        if (result != 0) {
            Files.write(Paths.get(outputFile + ".S"), assembly);
            throw new RuntimeException(
                    "Assembling failed! written assembly code to " + outputFile + ".S");
        }
    }
}
