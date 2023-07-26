package dev.omega.arcane.lexer;

import dev.omega.arcane.exception.MolangLexException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OperatorLexingTests {

    @Test
    public void Lex_Operator_PlusToken() throws MolangLexException {
        Assertions.assertTrue(MolangLexer.lex("+").matches(MolangTokenType.PLUS));
    }

    @Test
    public void Lex_Operator_MinusToken() throws MolangLexException {
        Assertions.assertTrue(MolangLexer.lex("-").matches(MolangTokenType.MINUS));
    }
}
