package app.cleancode.bytecode_to_asm.instructions;

public record IntInstruction(int opcode, int operand) implements Instruction {

    @Override
    public InstructionType getType() {
        return InstructionType.INT;
    }

}
