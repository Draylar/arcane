package dev.omega.arcane.lexer;

import dev.omega.arcane.exception.MolangLexException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConstantLexingTests {

    @Test
    public void Lex_Integer_NumberToken() throws MolangLexException {
        Assertions.assertTrue(MolangLexer.lex("5").matches(MolangTokenType.NUMBER));
    }

    @Test
    public void Lex_Double_NumberToken() throws MolangLexException {
        Assertions.assertTrue(MolangLexer.lex("5.0").matches(MolangTokenType.NUMBER));
    }
}
