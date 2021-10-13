package app.cleancode.bytecode_to_asm;

public class Test {
    public static long stringField = 127;

    public static void main(String[] args) {
        System.out.println(stringField);
    }

    public Test(String str, int i, int j) {
        System.out.println(str);
    }
}
