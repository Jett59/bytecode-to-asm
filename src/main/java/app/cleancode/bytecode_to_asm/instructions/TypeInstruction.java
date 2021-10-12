package app.cleancode.bytecode_to_asm.instructions;

public record TypeInstruction(int opcode, String type) implements Instruction {

    @Override
    public InstructionType getType() {
        return InstructionType.TYPE;
    }

}
