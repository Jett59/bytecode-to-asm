package app.cleancode.bytecode_to_asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import app.cleancode.bytecode_to_asm.instructions.ArglessInstruction;
import app.cleancode.bytecode_to_asm.instructions.FieldInstruction;
import app.cleancode.bytecode_to_asm.instructions.Instruction;
import app.cleancode.bytecode_to_asm.instructions.IntInstruction;
import app.cleancode.bytecode_to_asm.instructions.LdcInstruction;
import app.cleancode.bytecode_to_asm.instructions.LineInstruction;
import app.cleancode.bytecode_to_asm.instructions.MethodInstruction;
import app.cleancode.bytecode_to_asm.instructions.VariableInstruction;

public class MethodInspector extends MethodVisitor {

    private final int access;
    private final String name, descriptor;
    private final ClassInspector classInspector;
    private final List<Instruction> instructions;
    private final List<VariableInfo> variables;
    private int maxStackElements;

    public MethodInspector(int access, String name, String descriptor,
            ClassInspector classInspector) {
        super(Opcodes.ASM9);
        this.access = access;
        this.name = name;
        this.descriptor = descriptor;
        this.classInspector = classInspector;
        this.instructions = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        System.out.printf("\nFrame\nType %d\nLocals %s\nStack %s\n", type,
                local == null ? "<None>" : Arrays.toString(local),
                stack == null ? "<None>" : Arrays.toString(stack));
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        System.out.printf("\nLine %d\n", line);
        super.visitLineNumber(line, start);
        instructions.add(new LineInstruction(line));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
            boolean isInterface) {
        System.out.printf(
                "\nMethod insn\nOpcode %d\nOwner %s\nName %s\nDescriptor %s\nInterface %s\n",
                opcode, owner, name, descriptor, Boolean.toString(isInterface));
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        instructions.add(new MethodInstruction(opcode, owner, name));
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start,
            Label end, int index) {
        System.out.printf("\nLocal\nName %s\nDescriptor %s\nSignature %s\nIndex %d\n", name,
                descriptor, signature, index);
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        variables.add(new VariableInfo(name, descriptor));
    }

    @Override
    public void visitParameter(String name, int access) {
        System.out.printf("\nParam\nName %s\nAccess %d\n", name, access);
        super.visitParameter(name, access);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        System.out.printf("\nVar insn\nOpcode %d\nVariable %d\n", opcode, var);
        super.visitVarInsn(opcode, var);
        instructions.add(new VariableInstruction(opcode, var));
    }

    @Override
    public void visitLdcInsn(Object value) {
        System.out.printf("\nLdc insn\nValue %s\n", value);
        super.visitLdcInsn(value);
        instructions.add(new LdcInstruction(Opcodes.LDC, value));
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        System.out.printf("\nInt insn\nOpcode %d\nOparand %d\n", opcode, operand);
        super.visitIntInsn(opcode, operand);
        instructions.add(new IntInstruction(opcode, operand));
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String field, String type) {
        System.out.printf("\nField insn\nOpcode %d\nOwner %s\nName %s\nType %s\n", opcode, owner,
                field, type);
        super.visitFieldInsn(opcode, owner, field, type);
        instructions.add(new FieldInstruction(opcode, owner, field, type));
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
        maxStackElements = maxLocals + maxStack;
    }

    @Override
    public void visitInsn(int opcode) {
        System.out.printf("\nInsn\nOpcode %d\n", opcode);
        super.visitInsn(opcode);
        instructions.add(new ArglessInstruction(opcode));
    }

    @Override
    public void visitEnd() {
        System.out.println("\nEnd");
        super.visitEnd();
        classInspector.methods.add(new MethodInfo(name, descriptor,
                (access & Opcodes.ACC_PUBLIC) != 0, (access & Opcodes.ACC_STATIC) != 0, variables,
                instructions, maxStackElements));
    }

}
