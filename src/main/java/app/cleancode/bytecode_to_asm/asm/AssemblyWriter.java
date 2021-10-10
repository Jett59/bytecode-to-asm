package app.cleancode.bytecode_to_asm.asm;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.objectweb.asm.Opcodes;
import app.cleancode.bytecode_to_asm.ClassInfo;
import app.cleancode.bytecode_to_asm.MethodInfo;
import app.cleancode.bytecode_to_asm.instructions.FieldInstruction;
import app.cleancode.bytecode_to_asm.instructions.Instruction;

public class AssemblyWriter {
    private int operandStackOffset = 0;

    public void writeAssembly(ClassInfo classInfo, OutputStream output, String fileName) {
        RegisterAllocator registers = new RegisterAllocator();
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(output);
                BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {
            writer.append(String.format("/*Compiled from: %s*/\n\n", classInfo.name() + ".class"));
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
                        case FIELD: {
                            writeFieldInstruction((FieldInstruction) instruction, writer, registers,
                                    method);
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

    private int getSize(String type) {
        return switch (type.charAt(0)) {
            case 'J' -> 64;
            case 'L' -> 64;
            case '[' -> 64;
            case 'I' -> 32;
            case 'S' -> 16;
            case 'B' -> 8;
            case 'C' -> 8;
            case 'Z' -> 8;
            default -> throw new IllegalArgumentException("Unknown descriptor: " + type);
        };
    }

    private void writeFieldInstruction(FieldInstruction instruction, Writer writer,
            RegisterAllocator registers, MethodInfo method) throws Exception {
        switch (instruction.opcode()) {
            case Opcodes.GETSTATIC: {
                String fieldName = NameUtils.buildName(instruction.owner(), instruction.field());
                int stackElement = operandStackOffset + method.locals();
                int stackOffset = (stackElement + 1) * 8;
                Register register = registers.allocate();
                int size = getSize(instruction.type());
                switch (size) {
                    case 64: {
                        writer.append(String.format("mov %s, %%%s\n", fieldName,
                                register.displayName64()));
                        writer.append(String.format("mov %%%s, -%d(%%rbp)\n",
                                register.displayName64(), stackOffset));
                        break;
                    }
                    case 32: {
                        writer.append(String.format("mov %s, %%%s\n", fieldName,
                                register.displayName32()));
                        writer.append(String.format("mov %%%s, -%d(%%rbp)\n",
                                register.displayName32(), stackOffset));
                        break;
                    }
                    case 16: {
                        writer.append(String.format("mov %s, %%%s\n", fieldName,
                                register.displayName16()));
                        writer.append(String.format("mov %%%s, -%d(%%rbp)\n",
                                register.displayName16(), stackOffset));
                        break;
                    }
                    case 8: {
                        writer.append(String.format("mov %s, %%%s\n", fieldName,
                                register.displayName8()));
                        writer.append(String.format("mov %%%s, -%d(%%rbp)\n",
                                register.displayName8(), stackOffset));
                        break;
                    }
                }
                registers.free(register);
                operandStackOffset += 8;
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown opcode " + instruction.opcode());
        }
    }
}
