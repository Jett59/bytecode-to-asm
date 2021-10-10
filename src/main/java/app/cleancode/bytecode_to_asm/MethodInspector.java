package app.cleancode.bytecode_to_asm;

import java.util.Arrays;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodInspector extends MethodVisitor {

    private final String name, descriptor;
    private final ClassInspector classInspector;

    public MethodInspector(String name, String descriptor, ClassInspector classInspector) {
        super(Opcodes.ASM9);
        this.name = name;
        this.descriptor = descriptor;
        this.classInspector = classInspector;
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
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
            boolean isInterface) {
        System.out.printf(
                "\nMethod insn\nOpcode %d\nOwner %s\nName %s\nDescriptor %s\nInterface %s\n",
                opcode, owner, name, descriptor, Boolean.toString(isInterface));
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start,
            Label end, int index) {
        System.out.printf("\nLocal\nName %s\nDescriptor %s\nSignature %s\nIndex %d\n", name,
                descriptor, signature, index);
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
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
    }

    @Override
    public void visitLdcInsn(Object value) {
        System.out.printf("\nLdc insn\nValue %s\n", value);
        super.visitLdcInsn(value);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        System.out.printf("\nInt insn\nOpcode %d\nOparand %d\n", opcode, operand);
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String field, String type) {
        System.out.printf("\nField insn\nOpcode %d\nOwner %s\nName %s\nType %s\n", opcode, owner,
                field, type);
        super.visitFieldInsn(opcode, owner, field, type);
    }

    @Override
    public void visitInsn(int opcode) {
        System.out.printf("\nInsn\nOpcode %d\n", opcode);
        super.visitInsn(opcode);
    }
}
