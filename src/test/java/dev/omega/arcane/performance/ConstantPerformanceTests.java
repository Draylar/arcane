package dev.omega.arcane.performance;

import dev.omega.arcane.Molang;
import dev.omega.arcane.ast.MolangExpression;
import dev.omega.arcane.exception.MolangLexException;
import dev.omega.arcane.exception.MolangParseException;
import dev.omega.arcane.parser.MolangParser;
import org.junit.jupiter.api.Test;

public class ConstantPerformanceTests {

    @Test
    public void Performance_Complex_PrintTime() throws MolangLexException, MolangParseException {
        // This is all simplified anyways... :)
        MolangExpression expression = MolangParser.parse("5.0 + 5.0 * 3.0 / 10.0 + 7.0 - 3.0 + 1.0 + 3.0");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100_000_000; i++) {
            expression.evaluate();
        }
        Molang.LOGGER.info((System.currentTimeMillis() - start) + "ms");
    }
}
