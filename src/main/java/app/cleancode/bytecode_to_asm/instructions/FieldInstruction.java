package app.cleancode.bytecode_to_asm.instructions;

public record FieldInstruction(int opcode, String owner, String field, String type)
        implements Instruction {

    @Override
    public InstructionType getType() {
        return InstructionType.FIELD;
    }

}
