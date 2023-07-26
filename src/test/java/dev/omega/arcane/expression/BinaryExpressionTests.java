package dev.omega.arcane.expression;

import dev.omega.arcane.ast.BinaryExpression;
import dev.omega.arcane.ast.ConstantExpression;
import dev.omega.arcane.lexer.MolangTokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BinaryExpressionTests {

    @Test
    public void Evaluate_BinaryAddition_CorrectEvaluation() {
        Assertions.assertEquals(10.0f, new BinaryExpression(
                new ConstantExpression(5.0f),
                new ConstantExpression(5.0f),
                MolangTokenType.PLUS
        ).evaluate());
    }

    @Test
    public void Evaluate_BinaryMultiplication_CorrectEvaluation() {
        Assertions.assertEquals(25.0f, new BinaryExpression(
                new ConstantExpression(5.0f),
                new ConstantExpression(5.0f),
                MolangTokenType.STAR
        ).evaluate());
    }
}
