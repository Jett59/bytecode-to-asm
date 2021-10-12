package app.cleancode.bytecode_to_asm.asm;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.objectweb.asm.Opcodes;
import app.cleancode.bytecode_to_asm.ClassInfo;
import app.cleancode.bytecode_to_asm.FieldInfo;
import app.cleancode.bytecode_to_asm.MethodInfo;
import app.cleancode.bytecode_to_asm.instructions.FieldInstruction;
import app.cleancode.bytecode_to_asm.instructions.Instruction;
import app.cleancode.bytecode_to_asm.instructions.LdcInstruction;
import app.cleancode.bytecode_to_asm.instructions.MethodInstruction;
import app.cleancode.bytecode_to_asm.instructions.VariableInstruction;

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
                String methodName =
                        NameUtils.buildName(classInfo.name(), method.name(), method.signature());
                if (method.isPublic() && classInfo.isPublic()) {
                    writer.append(String.format(".global %s\n", methodName));
                }
                writer.append(String.format("%s:\n", methodName));
                writer.append("pushq %rbp\n");
                writer.append("movq %rsp, %rbp\n");
                writer.append(
                        String.format("subq $%d, %%rsp\n", method.maxStackElements() * Long.BYTES));
                storeArgs(method, registers, writer);
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
                        case LDC: {
                            writeLdcInstruction((LdcInstruction) instruction, registers, writer,
                                    method);
                            break;
                        }
                        case METHOD: {
                            writeMethodInstruction((MethodInstruction) instruction, method, writer);
                            break;
                        }
                        case ARGLESS: {
                            writeArglessInstruction(instruction, method, registers, writer);
                            break;
                        }
                        case VARIABLE: {
                            writeVariableInstruction((VariableInstruction) instruction, method,
                                    registers, writer);
                            break;
                        }
                        default:
                            throw new IllegalArgumentException(
                                    "Unknown instruction type " + instruction.getType());
                    }
                }
                writer.append('\n');
            }
            writer.append(".data\n\n");
            for (FieldInfo field : classInfo.fields()) {
                writeFieldInfo(classInfo, field, writer);
            }
            writer.append(".section .rodata\n\n");
            writer.append(rodata.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeArgs(MethodInfo method, RegisterAllocator registers, Writer writer)
            throws Exception {
        String arglist = method.signature().substring(1, method.signature().length() - 2);
        String[] args = arglist.split(",");
        int firstArgRegister = 0;
        writer.append(String.format("// Takes %d arguments\n", args.length));
        Register[] argRegisters = new Register[] {Register.RDI, Register.RSI, Register.RDX,
                Register.RCX, Register.R8, Register.R9};
        if (!method.isStatic()) {
            // Hidden 'this' argument
            writer.append("// + 'this' pointer\n");
            writer.append("mov %rdi, -8(%rbp)\n");
            firstArgRegister = 1;
        }
        for (int i = 0; i < args.length; i++) {
            String arg = args[i].trim();
            if (arg.isBlank()) {
                continue;
            }
            int argSize = getSize(arg);
            switch (argSize) {
                case 64: {
                    writer.append(String.format("mov %%%s, -%d(%%rbp)\n",
                            argRegisters[i + firstArgRegister].displayName64(),
                            i * 8 + 8 + firstArgRegister * 8));
                    break;
                }
                case 32: {
                    writer.append(String.format("mov %%%s, -%d(%%rbp)\n",
                            argRegisters[i + firstArgRegister].displayName32(),
                            i * 8 + 8 + 8 * firstArgRegister));
                    break;
                }
                case 16: {
                    writer.append(String.format("mov %%%s, -%d(%%rbp)\n",
                            argRegisters[i + firstArgRegister].displayName16(),
                            i * 8 + 8 + 8 * firstArgRegister));
                    break;
                }
                case 8: {
                    writer.append(String.format("mov %%%s, -%d(%%rbp)\n",
                            argRegisters[i + firstArgRegister].displayName8(),
                            i * 8 + 8 + 8 * firstArgRegister));
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown operand size " + argSize);
            }
        }
    }

    private void spillOperandStack(Writer writer, MethodInfo method, Register... registers)
            throws Exception {
        if (operandStackOffset <= registers.length) {
            for (int i = 0; i < operandStackOffset; i++) {
                writer.append(String.format("mov -%d(%%rbp), %%%s\n", (i + method.locals() + 1) * 8,
                        registers[i].displayName64()));
            }
        } else {
            throw new IllegalArgumentException("Excess elements on operand stack");
        }
    }

    private final StringBuilder rodata = new StringBuilder();

    private String createConstString(boolean isPublic, String name, String value) {
        String id = name != null ? name : String.format("str_%s", rodata.length());
        if (isPublic) {
            rodata.append(String.format(".global %s\n", id));
        }
        rodata.append(String.format("%s:\n", id));
        rodata.append(String.format(".asciz \"%s\"\n", value));
        return id;
    }

    private void writeFieldInfo(ClassInfo classInfo, FieldInfo field, Writer writer)
            throws Exception {
        String fieldName = NameUtils.buildName(classInfo.name(), field.name(), field.type());
        StringBuilder fieldData = new StringBuilder();
        if (field.isPublic() && classInfo.isPublic()) {
            fieldData.append(String.format(".global %s\n", fieldName));
        }
        fieldData.append(String.format("%s:\n", fieldName));
        if (field.value() == null) {
            if (field.isFinal()) {
                throw new IllegalArgumentException("Uninitialised final field " + field.name());
            }
            int fieldSize = getSize(field.type());
            int fieldBytes = fieldSize / 8;
            fieldData.append(String.format(".fill %d\n", fieldBytes));
        } else {
            if (field.value() instanceof String) {
                String stringName = createConstString(false, null, (String) field.value());
                fieldData.append(String.format(".quad %s\n", stringName));
            } else if (field.value() instanceof Number) {
                fieldData.append(String.format(".quad %d\n", ((Number) field.value()).longValue()));
            } else {
                throw new IllegalArgumentException(
                        "Unknown field type " + field.value().getClass().getSimpleName());
            }
        }
        if (!field.isFinal()) {
            writer.append(fieldData);
        } else {
            rodata.append(fieldData);
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
                String fieldName = NameUtils.buildName(instruction.owner(), instruction.field(),
                        instruction.type());
                int stackElement = operandStackOffset + method.locals();
                int stackOffset = (stackElement + 1) * 8;
                Register register = registers.allocate();
                int size = getSize(instruction.type());
                switch (size) {
                    case 64: {
                        writer.append(String.format("mov %s(%%rip), %%%s\n", fieldName,
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
                operandStackOffset++;
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown opcode " + instruction.opcode());
        }
    }

    private void writeLdcInstruction(LdcInstruction instruction, RegisterAllocator registers,
            Writer writer, MethodInfo method) throws Exception {
        final Object value = instruction.constant();
        String valueString;
        if (value instanceof Number) {
            valueString = value.toString();
            writer.append(String.format("movq $%s, -%d(%%rbp)\n", valueString,
                    (operandStackOffset + method.locals() + 1) * 8));
        } else if (value instanceof String) {
            valueString = createConstString(false, null, value.toString());
            Register register = registers.allocate();
            writer.append(
                    String.format("lea %s(%%rip), %%%s\n", valueString, register.displayName64()));
            writer.append(String.format("movq %%%s, -%d(%%rbp)\n", register.displayName64(),
                    (operandStackOffset + method.locals() + 1) * 8));
            registers.free(register);
        } else {
            throw new IllegalArgumentException("Unknown constant type " + value.getClass());
        }
        operandStackOffset++;
    }

    private void writeMethodInstruction(MethodInstruction instruction, MethodInfo method,
            Writer writer) throws Exception {
        String methodName = NameUtils.buildName(instruction.owner(), instruction.method(),
                instruction.signature());
        spillOperandStack(writer, method, Register.RDI, Register.RSI, Register.RDX, Register.RCX,
                Register.R8, Register.R9);
        writer.append(String.format("call %s\n", methodName));
        operandStackOffset = 0;
    }

    private void writeArglessInstruction(Instruction instruction, MethodInfo method,
            RegisterAllocator registers, Writer writer) throws Exception {
        switch (instruction.opcode()) {
            case Opcodes.RETURN: {
                writer.append("mov %rbp, %rsp\n");
                writer.append("pop %rbp\n");
                writer.append("retq\n");
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown opcode " + instruction.opcode());
        }
    }

    private void writeVariableInstruction(VariableInstruction instruction, MethodInfo method,
            RegisterAllocator registers, Writer writer) throws Exception {
        switch (instruction.opcode()) {
            case Opcodes.ALOAD: {
                Register register = registers.allocate();
                writer.append(String.format("mov -%d(%%rbp), %%%s\n",
                        instruction.variable() * 8 + 8, register.displayName64()));
                writer.append(String.format("mov %%%s, -%d(%%rbp)\n", register.displayName64(),
                        (operandStackOffset + method.locals() + 1) * 8));
                operandStackOffset++;
                registers.free(register);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown opcode " + instruction.opcode());
        }
    }
}
