package dev.omega.arcane.lexer;

import dev.omega.arcane.exception.MolangLexException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringLexingTests {

    @Test
    public void Lex_ValidString_StringToken() throws MolangLexException {
        Assertions.assertTrue(MolangLexer.lex("\"Hello, world!\"").matches(MolangTokenType.STRING));
    }

    @Test
    public void Lex_TrailingString_ThrowsException() {
        Assertions.assertThrowsExactly(MolangLexException.class, () -> MolangLexer.lex("\"Hello, world!"));
    }
}
