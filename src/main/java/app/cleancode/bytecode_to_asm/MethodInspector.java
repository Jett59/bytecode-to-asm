package app.cleancode.bytecode_to_asm;

import java.util.ArrayList;
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
import app.cleancode.bytecode_to_asm.instructions.TypeInstruction;
import app.cleancode.bytecode_to_asm.instructions.VariableInstruction;

public class MethodInspector extends MethodVisitor {

    private final int access;
    private final String name, descriptor;
    private final ClassInspector classInspector;
    private final List<Instruction> instructions;
    private final List<VariableInfo> variables;
    private int maxStackElements, locals;

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
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start);
        instructions.add(new LineInstruction(line));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
            boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        instructions.add(new MethodInstruction(opcode, owner, name, descriptor));
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start,
            Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        variables.add(new VariableInfo(name, descriptor));
    }

    @Override
    public void visitParameter(String name, int access) {
        super.visitParameter(name, access);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
        instructions.add(new VariableInstruction(opcode, var));
    }

    @Override
    public void visitLdcInsn(Object value) {
        super.visitLdcInsn(value);
        instructions.add(new LdcInstruction(Opcodes.LDC, value));
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        super.visitIntInsn(opcode, operand);
        instructions.add(new IntInstruction(opcode, operand));
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String field, String type) {
        super.visitFieldInsn(opcode, owner, field, type);
        instructions.add(new FieldInstruction(opcode, owner, field, type));
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
        maxStackElements = maxLocals + maxStack;
        locals = maxLocals;
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
        instructions.add(new ArglessInstruction(opcode));
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);
        instructions.add(new TypeInstruction(opcode, type));
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        classInspector.methods.add(new MethodInfo(name, descriptor,
                (access & Opcodes.ACC_PUBLIC) != 0, (access & Opcodes.ACC_STATIC) != 0, variables,
                instructions, maxStackElements, locals));
    }

}
