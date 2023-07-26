package dev.omega.arcane.ternary;

import dev.omega.arcane.Molang;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TernaryTests {

    @Test
    public void Ternary_BasicTrueConditional_ReturnsTrueBranch() {
        Assertions.assertEquals(5.0f, Molang.evaluateUnchecked("1.0 ? 5.0 : 3.0"));
    }

    @Test
    public void Ternary_BasicFalseConditional_ReturnsFalseBranch() {
        Assertions.assertEquals(3.0f, Molang.evaluateUnchecked("0.0 ? 5.0 : 3.0"));
    }
}
