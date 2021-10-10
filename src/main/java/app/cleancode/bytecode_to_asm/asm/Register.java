package app.cleancode.bytecode_to_asm.asm;

public enum Register {
    RAX, RCX, RDX, RDI, RSI, R8, R9, R10, R11;

    public String displayName64() {
        return name().toLowerCase();
    }

    public String displayName32() {
        if (Character.isDigit(name().charAt(name().length() - 1))) {
            return displayName64() + 'd';
        } else {
            return 'e' + displayName64().substring(1);
        }
    }

    public String displayName16() {
        if (Character.isDigit(name().charAt(name().length() - 1))) {
            return displayName64() + 'w';
        } else {
            return displayName64().substring(1);
        }
    }

    public String displayName8() {
        if (Character.isDigit(name().charAt(name().length() - 1))) {
            return displayName64() + 'b';
        } else {
            if (name().endsWith("I")) {
                return displayName64().substring(1) + 'l';
            } else {
                return displayName64().charAt(1) + "l";
            }
        }
    }
}
