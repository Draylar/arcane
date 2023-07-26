package dev.omega.arcane.precedence;

import dev.omega.arcane.Molang;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PEMDASTests {

    @Test
    public void PEMDAS_MultiplicationAddition_Success() {
        // 5.0 + 5.0 * 5.0 => 5.0 + (5.0 * 5.0) => 5.0 + (25.0) => 30.0
        Assertions.assertEquals(30.0, Molang.evaluateUnchecked("5.0 + 5.0 * 5.0"));
    }
}
