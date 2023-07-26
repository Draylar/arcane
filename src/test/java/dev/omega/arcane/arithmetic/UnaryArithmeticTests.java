package dev.omega.arcane.arithmetic;

import dev.omega.arcane.Molang;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnaryArithmeticTests {

    @Test
    public void Addition_Integer_Success() {
        Assertions.assertEquals(10.0, Molang.evaluateUnchecked("5 + 5"));
    }

    @Test
    public void Addition_Double_Success() {
        Assertions.assertEquals(15.0, Molang.evaluateUnchecked("5.0 + 10.0"));
    }

    @Test
    public void Subtraction_Double_Success() {
        Assertions.assertEquals(-5.0, Molang.evaluateUnchecked("5.0 - 10.0"));
    }
}
