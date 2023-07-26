package dev.omega.arcane.unary;

import dev.omega.arcane.Molang;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnaryMathTests {

    @Test
    public void Unary_ConstantTrueDouble_ReturnsFalse() {
        Assertions.assertEquals(1.0, Molang.evaluateUnchecked("!m.sin(0.0)"));
    }

    @Test
    public void UnaryMinus_FunctionDouble_ReturnsNegativeValue() {
        Assertions.assertEquals(-0.0, Molang.evaluateUnchecked("-m.sin(0.0)"));
    }
}
