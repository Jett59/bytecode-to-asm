package app.cleancode.bytecode_to_asm.instructions;

public record VariableInstruction(int opcode, int variable) implements Instruction {

    @Override
    public InstructionType getType() {
        return InstructionType.VARIABLE;
    }

}
