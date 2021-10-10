package app.cleancode.bytecode_to_asm;

import java.util.List;
import app.cleancode.bytecode_to_asm.instructions.Instruction;

public record MethodInfo(String name, String signature, boolean isPublic, boolean isStatic,
        List<VariableInfo> variables, List<Instruction> instructions, int maxStackElements) {

}
