package dev.omega.arcane.simplification;

import dev.omega.arcane.ast.BinaryExpression;
import dev.omega.arcane.ast.ConstantExpression;
import dev.omega.arcane.ast.MolangExpression;
import dev.omega.arcane.ast.operator.AdditionExpression;
import dev.omega.arcane.exception.MolangLexException;
import dev.omega.arcane.exception.MolangParseException;
import dev.omega.arcane.parser.MolangParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimplificationTests {

    @Test
    public void Simplify_BinaryAdditionExpression_ReturnsConstantExpression() throws MolangLexException, MolangParseException {
        MolangExpression expression = MolangParser.parse("5.0 + 5.0", MolangParser.FLAG_NONE);
        Assertions.assertInstanceOf(AdditionExpression.class, expression);

        // Simplifying the Expression will result in a Constant expression with the value 10.0.
        MolangExpression simplified = expression.simplify();
        Assertions.assertInstanceOf(ConstantExpression.class, simplified, "Binary Addition Expression should turn into a Constant Expression after simplification.");
        Assertions.assertEquals(10.0f, ((ConstantExpression) simplified).value());
    }
}
