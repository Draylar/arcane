package dev.omega.arcane.expression;

import dev.omega.arcane.ast.ConstantExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConstantExpressionTests {

    @Test
    public void Evaluate_ConstantNumber_EvaluateMatchesInput() {
        Assertions.assertEquals(5.0f, new ConstantExpression(5.0f).evaluate());
    }
}
