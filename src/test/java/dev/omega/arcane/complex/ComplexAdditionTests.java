package dev.omega.arcane.complex;

import dev.omega.arcane.ast.ConstantExpression;
import dev.omega.arcane.ast.MolangExpression;
import dev.omega.arcane.exception.MolangLexException;
import dev.omega.arcane.exception.MolangParseException;
import dev.omega.arcane.parser.MolangParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ComplexAdditionTests {

    @Test
    public void Addition_10Units_Success() throws MolangLexException, MolangParseException {
        MolangExpression expression = MolangParser.parse("1.0 + 1.0 + 1.0 + 1.0 + 1.0 + 1.0 + 1.0 + 1.0 + 1.0 + 1.0");
        Assertions.assertInstanceOf(ConstantExpression.class, expression);
        Assertions.assertEquals(10.0f, expression.evaluate());
    }
}
