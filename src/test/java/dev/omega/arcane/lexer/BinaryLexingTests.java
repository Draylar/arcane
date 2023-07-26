package dev.omega.arcane.lexer;

import dev.omega.arcane.exception.MolangLexException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static dev.omega.arcane.lexer.MolangTokenType.*;

public class BinaryLexingTests {

    @Test
    public void Lex_BinaryAddition_3Tokens() throws MolangLexException {
        Assertions.assertTrue(MolangLexer.lex("5 + 5").matches(NUMBER, PLUS, NUMBER));
    }

    @Test
    public void Lex_BinarySubtraction_3Tokens() throws MolangLexException {
        Assertions.assertTrue(MolangLexer.lex("5 - 5").matches(NUMBER, MINUS, NUMBER));
    }

    @Test
    public void Lex_BinaryLessThanOrEqual_3Tokens() throws MolangLexException {
        Assertions.assertTrue(MolangLexer.lex("2 <= 3").matches(NUMBER, LESS_THAN_OR_EQUAL, NUMBER));
    }

    @Test
    public void Lex_BinaryNotEqual_3Tokens() throws MolangLexException {
        Assertions.assertTrue(MolangLexer.lex("2.0 != 3.0").matches(NUMBER, BANG_EQUAL, NUMBER));
    }
}
