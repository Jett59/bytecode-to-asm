package app.cleancode.bytecode_to_asm.instructions;

public record MethodInstruction(int opcode, String owner, String method, String signature)
        implements Instruction {

    @Override
    public InstructionType getType() {
        return InstructionType.METHOD;
    }

}
