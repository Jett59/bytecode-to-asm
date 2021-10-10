package app.cleancode.bytecode_to_asm.asm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class RegisterTest {
    @Test
    public void displayName64() {
        for (Register register : Register.values()) {
            assertEquals(register.name().toLowerCase(), register.displayName64());
        }
    }

    @Test
    public void displayName32() {
        assertEquals("eax", Register.RAX.displayName32());
        assertEquals("edi", Register.RDI.displayName32());
        assertEquals("r10d", Register.R10.displayName32());
    }

    @Test
    public void displayName16() {
        assertEquals("ax", Register.RAX.displayName16());
        assertEquals("di", Register.RDI.displayName16());
        assertEquals("r10w", Register.R10.displayName16());
    }

    public void displayName8() {
        assertEquals("al", Register.RAX.displayName8());
        assertEquals("dil", Register.RDI.displayName8());
        assertEquals("r10b", Register.R10.displayName8());
    }
}
