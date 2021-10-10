package app.cleancode.bytecode_to_asm.instructions;

public record ArglessInstruction(int opcode) implements Instruction {

    @Override
    public InstructionType getType() {
        return InstructionType.ARGLESS;
    }

}
