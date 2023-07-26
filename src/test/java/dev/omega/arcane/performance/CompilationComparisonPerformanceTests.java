package dev.omega.arcane.performance;

import dev.omega.arcane.Molang;
import dev.omega.arcane.ast.MolangExpression;
import dev.omega.arcane.exception.MolangLexException;
import dev.omega.arcane.exception.MolangParseException;
import dev.omega.arcane.parser.MolangParser;
import dev.omega.arcane.util.Timer;
import org.junit.jupiter.api.Test;

public class CompilationComparisonPerformanceTests {

    @Test
    public void Performance_CompilationComparison_Print() {
        int attempts = 10_000_000;

        long raw = Timer.time(() -> {
            for (int i = 0; i < attempts; i++) {
                Molang.evaluateUnchecked("5.0 + 10.0 + ((3.0 / 3.0) - 7.0) / 3.5 - 9.9 + (1.3 - 35.5)");
            }
        });

        long compiled = Timer.time(() -> {
            try {
                MolangExpression parsed = MolangParser.parse("5.0 + 10.0 + ((3.0 / 3.0) - 7.0) / 3.5 - 9.9 + (1.3 - 35.5)");
                for (int i = 0; i < attempts; i++) {
                    parsed.evaluate();
                }
            } catch (MolangLexException | MolangParseException exception) {
                throw new RuntimeException(exception);
            }
        });

        Molang.LOGGER.info("Raw Time (ms): " + raw);
        Molang.LOGGER.info("Compiled Time (ms): " + compiled);
    }
}
