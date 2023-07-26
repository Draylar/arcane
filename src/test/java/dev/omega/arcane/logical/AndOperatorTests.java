package dev.omega.arcane.logical;

import dev.omega.arcane.Molang;
import dev.omega.arcane.exception.MolangLexException;
import dev.omega.arcane.exception.MolangParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("And (&&)")
public class AndOperatorTests {

    @Test
    public void And_FalseFalse_False() {
        Assertions.assertEquals(0.0, Molang.evaluateUnchecked("0.0 && 0.0"));
        Assertions.assertEquals(0.0, Molang.evaluateUnchecked("0 && 0"));
    }

    @Test
    public void And_FalseTrue_False() {
        Assertions.assertEquals(0.0, Molang.evaluateUnchecked("0.0 && 1.0"));
        Assertions.assertEquals(0.0, Molang.evaluateUnchecked("1.0 && 0.0"));
    }

    @Test
    public void And_TrueTrue_True() {
        Assertions.assertEquals(1.0, Molang.evaluateUnchecked("1.0 && 1.0"));
    }

    @Test
    public void And_MissingAmpersand_ThrowsLexException() {
        Assertions.assertThrowsExactly(MolangLexException.class, () -> Molang.evaluate("0.0 & 0.0"));
    }

    @Test
    public void And_MissingRightOperator_ThrowsLexException() {
        Assertions.assertThrowsExactly(MolangParseException.class, () -> Molang.evaluate("0.0 &&"));
    }

    @Test
    public void And_MissingLeftOperator_ThrowsLexException() {
        Assertions.assertThrowsExactly(MolangParseException.class, () -> Molang.evaluate("&& 0.0"));
    }

    @Test
    public void And_MathConstant_ReturnAnswer() {
        // sin(1.0) is != 0.0, so it is true
        Assertions.assertEquals(1.0f, Molang.evaluateUnchecked("math.sin(1.0) && 1.0"));
    }
}
