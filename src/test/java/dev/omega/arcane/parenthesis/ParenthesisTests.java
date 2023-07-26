package dev.omega.arcane.parenthesis;

import dev.omega.arcane.Molang;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Parenthesis")
public class ParenthesisTests {

    @Test
    public void Parenthesis_ScopeLeftAddition_OperationOrderAnswer() {
        // 5 + 5 * 5.0 => 30.0
        // (5 + 5) * 5.0 => 50.0
        Assertions.assertEquals(50.0f, Molang.evaluateUnchecked("(5 + 5) * 5.0"));
    }

    @Test
    public void Parenthesis_ScopeRightMultiplication_OperationOrderAnswer() {
        // 5 + 5 * 5.0 => 30.0
        // (5 + 5) * 5.0 => 50.0
        Assertions.assertEquals(30.0f, Molang.evaluateUnchecked("5 + (5.0 * 5.0)"));
    }

    @Test
    public void Parenthesis_DoubleScopeOuter_OperationOrderAnswer() {
        // 1 + 1 * 3 * 1 + 1 = 1 + 3 + 1 = 5
        // (1 + 1) * 3 * (1 + 1) = 2 * 3 * 2 = 12
        Assertions.assertEquals(12.0f, Molang.evaluateUnchecked("(1 + 1) * 3 * (1 + 1)"));
    }

    @Test
    public void Parenthesis_DoubleUnscopedOuter_OperationOrderAnswer() {
        // 1 + 1 * 3 * 1 + 1 = 1 + 3 + 1 = 5
        // (1 + 1) * 3 * (1 + 1) = 2 * 3 * 2 = 12
        Assertions.assertEquals(4.0f, Molang.evaluateUnchecked("3 * 1 + 1"));
    }
}
