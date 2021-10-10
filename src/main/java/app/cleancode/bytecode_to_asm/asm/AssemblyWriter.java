package app.cleancode.bytecode_to_asm.asm;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import app.cleancode.bytecode_to_asm.ClassInfo;

public class AssemblyWriter {
    public void writeAssembly(ClassInfo classInfo, OutputStream output, String fileName) {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(output);
                BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {
            writer.append(String.format("/*Compiled from: %s*/\n\n", classInfo.name()));
            writer.append(String.format(".file \"%s\"\n\n", fileName));
            writer.append(".code64\n\n");
            writer.append(".text\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
