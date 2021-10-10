package app.cleancode.bytecode_to_asm.instructions;

public interface Instruction {
    InstructionType getType();

    int opcode();
}
