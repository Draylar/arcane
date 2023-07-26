package dev.omega.arcane.lexer;

import dev.omega.arcane.exception.MolangLexException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static dev.omega.arcane.lexer.MolangTokenType.NUMBER;

public class NumberLexingTests {

    @Test
    public void Lex_Integer_NumberToken() throws MolangLexException {
        Assertions.assertTrue(MolangLexer.lex("5").matches(NUMBER));
    }

    @Test
    public void Lex_Double_NumberToken() throws MolangLexException {
        Assertions.assertTrue(MolangLexer.lex("5.0").matches(NUMBER));
    }
}
