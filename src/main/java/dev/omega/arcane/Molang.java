package dev.omega.arcane;

import dev.omega.arcane.ast.MolangExpression;
import dev.omega.arcane.exception.MolangException;
import dev.omega.arcane.exception.MolangLexException;
import dev.omega.arcane.exception.MolangParseException;
import dev.omega.arcane.parser.MolangParser;
import org.jetbrains.annotations.ApiStatus;

import java.util.Random;
import java.util.logging.Logger;

/**
 * MoLang is Minecraft's official expression language. This class provides utilities for evaluating raw MoLang strings into {@code float} values.
 */
public class Molang {

    public static final Random RANDOM = new Random();
    public static final Logger LOGGER = Logger.getLogger("Arcane Molang");

    /**
     * Evaluate the MoLang contained in the {@link String} into a {@link Float} value.
     *
     * <p>
     * <b>Implementation Note:</b>
     * If this method is being called frequently, consider using {@link MolangParser#parse(String)} and directly
     * call {@link MolangExpression#evaluate()} on the output.
     * This method will construct a new {@link MolangParser} each call, which is not ideal for performance critical code.
     *
     * <p>
     * To evaluate without dealing with the {@link MolangException}, visit {@link Molang#evaluateUnchecked(String)}.
     *
     * @param input the input Molang to parse and evaluate
     * @return a {@code float} evaluated from the input Molang
     * @throws MolangException if any issues occur while lexing, parsing, or evaluating the {@code input} MoLang
     */
    @ApiStatus.AvailableSince("1.0.0")
    public static float evaluate(String input) throws MolangException {
        return MolangParser.parse(input).evaluate();
    }

    @ApiStatus.AvailableSince("1.0.0")
    public static float evaluateUnchecked(String input) {
        try {
            return MolangParser.parse(input).evaluate();
        } catch (MolangLexException | MolangParseException exception) {
            throw new RuntimeException(exception);
        }
    }
}