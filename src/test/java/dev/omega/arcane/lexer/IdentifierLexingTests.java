package dev.omega.arcane.lexer;

import dev.omega.arcane.exception.MolangLexException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdentifierLexingTests {

    @Test
    public void Lex_Identifier_IdentifierToken() throws MolangLexException {
        LexedMolang token = MolangLexer.lex("hello");
        Assertions.assertTrue(token.matches(MolangTokenType.IDENTIFIER));
        Assertions.assertEquals("hello", token.tokens().get(0).value());
    }
}
