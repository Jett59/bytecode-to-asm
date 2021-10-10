package app.cleancode.bytecode_to_asm.asm;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import app.cleancode.bytecode_to_asm.ClassInfo;
import app.cleancode.bytecode_to_asm.MethodInfo;
import app.cleancode.bytecode_to_asm.instructions.Instruction;

public class AssemblyWriter {
    public void writeAssembly(ClassInfo classInfo, OutputStream output, String fileName) {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(output);
                BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {
            writer.append(String.format("/*Compiled from: %s*/\n\n", classInfo.name()));
            writer.append(String.format(".file \"%s\"\n\n", fileName));
            writer.append(".code64\n\n");
            writer.append(".text\n\n");
            for (MethodInfo method : classInfo.methods()) {
                String methodName = NameUtils.buildName(classInfo.name(), method.name());
                if (method.isPublic() && classInfo.isPublic()) {
                    writer.append(String.format(".global %s\n", methodName));
                }
                writer.append(String.format("%s:\n", methodName));
                writer.append("pushq %rbp\n");
                writer.append("movq %rsp, %rbp\n");
                writer.append(
                        String.format("subq $%d, %%rsp\n", method.maxStackElements() * Long.BYTES));
                for (Instruction instruction : method.instructions()) {
                    switch (instruction.getType()) {
                        case LINE: {
                            writer.append(
                                    String.format("// Source line: %d\n", instruction.opcode()));
                            break;
                        }
                        default:
                            throw new IllegalArgumentException(
                                    "Unknown instruction type " + instruction.getType());
                    }
                }
                writer.append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
