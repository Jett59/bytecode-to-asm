package app.cleancode.bytecode_to_asm;

import java.util.List;

public record ClassInfo(String name, boolean isPublic, List<MethodInfo> methods,
        List<FieldInfo> fields) {

}
