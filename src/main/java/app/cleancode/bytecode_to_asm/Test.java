package app.cleancode.bytecode_to_asm;

public class Test {
    public static long stringField = 1024;

    public static void main(String[] args) {
        System.out.println(stringField);
    }

    public Test(String str) {
        System.out.println(str);
    }
}
