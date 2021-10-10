package app.cleancode.bytecode_to_asm;

public record FieldInfo(String name, boolean isPublic, boolean isStatic, String type,
        Object value) {

}
