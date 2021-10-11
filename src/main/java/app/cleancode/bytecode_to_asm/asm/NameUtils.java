package app.cleancode.bytecode_to_asm.asm;

public class NameUtils {
    public static String prepareName(String name) {
        return name.replace('/', '_').replace('<', '_').replace('>', '_').replace('(', '_')
                .replace(')', '_').replace(";", "").replace("[", "_arr_");
    }

    public static String buildName(String className, String itemName, String descriptor) {
        return prepareName(className).concat("_").concat(prepareName(itemName)).concat("_")
                .concat(prepareName(descriptor));
    }
}
