package app.cleancode.bytecode_to_asm;

public class Test {
    private static final String stringField = "Hello, World!";

    public static void main(String[] args) {
        System.out.println(stringField);
    }

    public Test(String str) {
        System.out.println(str);
    }
}
