package app.cleancode.bytecode_to_asm.asm;

public class NameUtils {
    public static String prepareName(String name) {
        return name.replace('/', '_').replace('<', '_').replace('>', '_');
    }

    public static String buildName(String className, String itemName) {
        return prepareName(className).concat("_").concat(prepareName(itemName));
    }
}
