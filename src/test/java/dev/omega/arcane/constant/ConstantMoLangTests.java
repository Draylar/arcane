package dev.omega.arcane.constant;

import dev.omega.arcane.Molang;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConstantMoLangTests {

    @Test
    public void Constant_Integer_ExpressionMatchesInput() {
        Assertions.assertEquals(5.0f, Molang.evaluateUnchecked("5"));
    }

    @Test
    public void Constant_Float_ExpressionMatchesInput() {
        Assertions.assertEquals(5.0f, Molang.evaluateUnchecked("5.0"));
    }

    @Test
    public void Constant_NegativeInteger_ExpressionMatchesInput() {
        Assertions.assertEquals(-5.0f, Molang.evaluateUnchecked("-5"));
    }

    @Test
    public void Constant_NegativeFloat_ExpressionMatchesInput() {
        Assertions.assertEquals(-5.0f, Molang.evaluateUnchecked("-5.0"));
    }
}
