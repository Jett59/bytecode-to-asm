package app.cleancode.bytecode_to_asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassInspector extends ClassVisitor {

    public ClassInspector() {
        super(Opcodes.ASM9);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName,
            String[] interfaces) {
        System.out.printf("Version %d\nAccess %d\nName %s\nSignature %s\nSuper %s\nInterfaces %s\n",
                version, access, name, signature, superName, String.join(", ", interfaces));
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        System.out.printf("\nAccess %d\nName %s\nDescriptor %s\nSignature %s\nExceptions %s\n",
                access, name, descriptor, signature,
                exceptions == null ? "<None>" : String.join(", ", exceptions));
        return new MethodInspector(name, descriptor, this);
    }

}
