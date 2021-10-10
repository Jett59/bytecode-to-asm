package app.cleancode.bytecode_to_asm.asm;

import static app.cleancode.bytecode_to_asm.asm.Register.R10;
import static app.cleancode.bytecode_to_asm.asm.Register.R11;
import static app.cleancode.bytecode_to_asm.asm.Register.R8;
import static app.cleancode.bytecode_to_asm.asm.Register.R9;
import static app.cleancode.bytecode_to_asm.asm.Register.RAX;
import static app.cleancode.bytecode_to_asm.asm.Register.RCX;
import static app.cleancode.bytecode_to_asm.asm.Register.RDI;
import static app.cleancode.bytecode_to_asm.asm.Register.RDX;
import static app.cleancode.bytecode_to_asm.asm.Register.RSI;
import java.util.List;
import java.util.Stack;

public class RegisterAllocator {
    private final Stack<Register> registers;

    public RegisterAllocator() {
        registers = new Stack<>();
        registers.addAll(List.of(RAX, RCX, RDX, RDI, RSI, R8, R9, R10, R11));
    }

    public Register allocate() {
        if (registers.empty()) {
            return null;
        }
        return registers.pop();
    }

    public void free(Register register) {
        if (registers.contains(register)) {
            throw new IllegalArgumentException(
                    "Attempt to free a register already in the free register stack");
        }
        registers.push(register);
    }
}
