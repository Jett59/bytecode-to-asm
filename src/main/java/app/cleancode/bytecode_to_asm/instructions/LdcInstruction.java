package app.cleancode.bytecode_to_asm.instructions;

public record LdcInstruction(int opcode, Object constant) implements Instruction {

    @Override
    public InstructionType getType() {
        return InstructionType.LDC;
    }

}
