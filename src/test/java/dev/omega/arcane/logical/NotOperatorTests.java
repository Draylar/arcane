package dev.omega.arcane.logical;

import dev.omega.arcane.Molang;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Not (!)")
public class NotOperatorTests {

    @Test
    public void Not_ConstantZero_ReturnsTrue() {
        Assertions.assertEquals(1.0, Molang.evaluateUnchecked("!0.0"));
    }

    @Test
    public void Not_ConstantOne_ReturnsFalse() {
        Assertions.assertEquals(0.0, Molang.evaluateUnchecked("!1.0"));
    }
}
