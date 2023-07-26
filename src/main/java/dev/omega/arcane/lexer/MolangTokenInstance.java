package dev.omega.arcane.lexer;

public record MolangTokenInstance(MolangTokenType type, String lexeme, Object value) {

    public MolangTokenInstance(MolangTokenType type, String lexeme) {
        this(type, lexeme, null);
    }
}
