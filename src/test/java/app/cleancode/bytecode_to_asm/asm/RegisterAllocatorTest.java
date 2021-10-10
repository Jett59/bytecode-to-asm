package app.cleancode.bytecode_to_asm.asm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

public class RegisterAllocatorTest {
    @Test
    public void allocate_noFree() {
        RegisterAllocator allocator = new RegisterAllocator();
        for (int i = 0; i < Register.values().length; i++) {
            assertNotEquals(allocator.allocate(), null);
        }
        assertEquals(null, allocator.allocate());
    }
}
