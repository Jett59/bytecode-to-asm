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
    public void visitInsn(int opcode) {
        System.out.printf("\nInsn\nOpcode %d\n", opcode);
        super.visitInsn(opcode);
    }
}
