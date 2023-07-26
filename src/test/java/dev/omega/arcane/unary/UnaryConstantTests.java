package dev.omega.arcane.unary;

import dev.omega.arcane.Molang;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnaryConstantTests {

    @Test
    public void UnaryBang_ConstantTrueDouble_ReturnsFalse() {
        Assertions.assertEquals(0.0, Molang.evaluateUnchecked("!1.0"));
    }

    @Test
    public void UnaryBang_ConstantFalseDouble_ReturnsTrue() {
        Assertions.assertEquals(1.0, Molang.evaluateUnchecked("!0.0"));
    }

    @Test
    public void UnaryMinus_ConstantDouble_ReturnsNegativeValue() {
        Assertions.assertEquals(-0.0, Molang.evaluateUnchecked("-0.0"));
    }
}
