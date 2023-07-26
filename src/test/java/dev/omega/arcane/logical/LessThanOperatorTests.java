package dev.omega.arcane.logical;

import dev.omega.arcane.Molang;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Less Than (<)")
public class LessThanOperatorTests {

    @Test
    public void LessThan_LeftUnderRight_ReturnsTrue() {
        Assertions.assertEquals(1.0f, Molang.evaluateUnchecked("5.0 < 10.0"));
    }

    @Test
    public void LessThan_LeftAboveRightNegative_ReturnsFalse() {
        Assertions.assertEquals(0.0f, Molang.evaluateUnchecked("5.0 < -10.0"));
    }
}
