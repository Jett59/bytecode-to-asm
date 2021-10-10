package app.cleancode.bytecode_to_asm.instructions;

public record LineInstruction(int lineNumber) implements Instruction {

    @Override
    public InstructionType getType() {
        return InstructionType.LINE;
    }

    @Override
    public int opcode() {
        return lineNumber;
    }

}
