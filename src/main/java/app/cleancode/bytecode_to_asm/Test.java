package app.cleancode.bytecode_to_asm;

public class Test {
    public static long stringField = 127;
    public static int intField;

    public static void main(String[] args) {
        System.out.println(stringField);
        System.out.println(intField);
    }

    public Test(String str, int i, short j, byte k) {
        System.out.println(str);
    }
}
