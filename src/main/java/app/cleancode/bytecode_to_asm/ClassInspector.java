package app.cleancode.bytecode_to_asm;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassInspector extends ClassVisitor {
    public final List<MethodInfo> methods;
    public final List<FieldInfo> fields;

    public ClassInfo info = null;

    private String name;
    private int access;

    public ClassInspector() {
        super(Opcodes.ASM9);
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName,
            String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.access = access;
        this.name = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        return new MethodInspector(access, name, descriptor, this);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature,
            Object value) {
        fields.add(new FieldInfo(name, (access & Opcodes.ACC_PUBLIC) != 0,
                (access & Opcodes.ACC_STATIC) != 0, (access & Opcodes.ACC_FINAL) != 0, descriptor,
                value));
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        info = new ClassInfo(name, (access & Opcodes.ACC_PUBLIC) != 0, methods, fields);
        System.out.println(info);
    }

}
