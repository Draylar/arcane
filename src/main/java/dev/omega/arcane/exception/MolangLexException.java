package dev.omega.arcane.exception;

import dev.omega.arcane.lexer.MolangLexer;

/**
 * Thrown when a lexing issue occurs in {@link MolangLexer}.
 */
public class MolangLexException extends MolangException {

    public MolangLexException(String message) {
        super(message);
    }
}
