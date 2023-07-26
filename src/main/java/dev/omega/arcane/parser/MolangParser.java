package dev.omega.arcane.parser;

import dev.omega.arcane.ast.*;
import dev.omega.arcane.ast.math.MathExpression;
import dev.omega.arcane.ast.operator.AdditionExpression;
import dev.omega.arcane.ast.operator.DivisionExpression;
import dev.omega.arcane.ast.operator.MultiplicationExpression;
import dev.omega.arcane.ast.operator.SubtractionExpression;
import dev.omega.arcane.reference.ReferenceType;
import dev.omega.arcane.exception.MolangLexException;
import dev.omega.arcane.exception.MolangParseException;
import dev.omega.arcane.lexer.LexedMolang;
import dev.omega.arcane.lexer.MolangLexer;
import dev.omega.arcane.lexer.MolangTokenInstance;
import dev.omega.arcane.lexer.MolangTokenType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static dev.omega.arcane.lexer.MolangTokenType.*;

/**
 * Recursive descent parser for the Molang language. Visit {@link MolangParser#parse(String)} to get started.
 */
public class MolangParser {

    public static final int FLAG_NONE = 0x00000000;
    public static final int FLAG_SIMPLIFY = 0x00000001;
    public static final int FLAG_CACHE = 0x00000100;

    private static final Map<String, MolangExpression> AST_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, MolangExpression> SIMPLIFIED_AST_CACHE = new ConcurrentHashMap<>();

    private final LexedMolang input;
    private int cursor = 0;

    private MolangParser(LexedMolang input) {
        this.input = input;
    }

    /**
     * Parses the {@link String} {@code input} into a simplified {@link MolangExpression} with simplification ({@link MolangParser#FLAG_SIMPLIFY}) and expression caching ({@link MolangParser#FLAG_CACHE}) enabled.
     *
     * <p>
     * If the input contains invalid tokens or non-Molang syntax, a {@link MolangLexException} or {@link MolangParseException} will be thrown.
     *
     * @param input the input to parse into a {@link MolangExpression}
     * @return the parsed and simplified {@link MolangExpression}
     * @throws MolangLexException   if an invalid token was found in the {@code input}
     * @throws MolangParseException if an expression could not be parsed from the tokenized {@code input}
     */
    @ApiStatus.AvailableSince("1.0.0")
    public static MolangExpression parse(String input) throws MolangLexException, MolangParseException {
        return parse(input, FLAG_CACHE | FLAG_SIMPLIFY);
    }

    /**
     * Parses the {@link String} {@code input} into a simplified {@link MolangExpression}. {@code flags} can be used to control
     * properties about how the expression is calculated:
     *
     * <ul>
     *     <li>{@link MolangParser#FLAG_NONE} for no special options</li>
     *     <li>{@link MolangParser#FLAG_SIMPLIFY} to simplify the returned {@link MolangExpression}</li>
     *     <li>{@link MolangParser#FLAG_CACHE} to check cache for (or parse & cache) the expression input</li>
     * </ul>
     *
     * <p>
     * If the input contains invalid tokens or non-Molang syntax, a {@link MolangLexException} or {@link MolangParseException} will be thrown.
     *
     * @param input the input to parse into a {@link MolangExpression}
     * @param flags bitwise flags for control over parsing and caching behavior
     * @return the parsed and simplified {@link MolangExpression}
     * @throws MolangLexException   if an invalid token was found in the {@code input}
     * @throws MolangParseException if an expression could not be parsed from the tokenized {@code input}
     */
    @ApiStatus.AvailableSince("1.0.0")
    public static MolangExpression parse(String input, int flags) throws MolangLexException, MolangParseException {
        boolean simplify = (flags & FLAG_SIMPLIFY) == FLAG_SIMPLIFY;
        boolean cache = (flags & FLAG_CACHE) == FLAG_CACHE;

        // If the caller does not want to use cache, parse and return early:
        if(!cache) {
            return parse(MolangLexer.lex(input), simplify);
        }

        // Check if we can avoid lexing & parsing by falling back to the cache.
        // Pick cache based on whether simplify is set - we don't want non-simplified AST getting
        // returned to users asking for simplified cache.
        var cacheMap = simplify ? SIMPLIFIED_AST_CACHE : AST_CACHE;
        @Nullable MolangExpression cached = cacheMap.get(input);
        if(cached == null) {
            cached = parse(MolangLexer.lex(input), true);
            cacheMap.put(input, cached);
        }

        return cached;
    }

    /**
     * Parse the {@link LexedMolang} into a {@link MolangExpression}.
     *
     * @param input the {@link LexedMolang} input to parse, obtained from {@link MolangLexer}
     * @param simplify whether the returned {@link MolangExpression} should be simplified for faster evaluation
     * @return a new {@link MolangExpression} parsed from the {@link LexedMolang}
     * @throws MolangParseException if a grammar syntax issue occurs while parsing
     */
    @ApiStatus.AvailableSince("1.0.0")
    public static MolangExpression parse(LexedMolang input, boolean simplify) throws MolangParseException {
        MolangExpression expression = new MolangParser(input).expression();
        if(simplify) {
            expression = expression.simplify();
        }

        return expression;
    }

    private MolangExpression expressionArgument(boolean required) throws MolangParseException {
        MolangExpression expression = expression();

        if(!match(COMMA)) {
            if(required) {
                throw new MolangParseException("Expected to find a comma after argument input!");
            }
        }

        return expression;
    }

    private MolangExpression expression() throws MolangParseException {
        return ternary();
    }

    private MolangExpression ternary() throws MolangParseException {
        MolangExpression left = or();

        // Left = the condition for the ternary operator.
        while (match(QUESTION)) {
            MolangExpression trueBranch = or();

            if(!match(COLON)) {
                throw new MolangParseException("Expected to find ':' after the true branch in conditional ternary expression!");
            }

            MolangExpression falseBranch = or();
            left = new TernaryExpression(left, trueBranch, falseBranch);
        }

        return left;
    }

    private MolangExpression or() throws MolangParseException {
        MolangExpression left = and();
        while (match(DOUBLE_PIPE)) {
            MolangTokenInstance operator = previous();
            left = new BinaryExpression(left, and(), operator.type());
        }

        return left;
    }

    private MolangExpression and() throws MolangParseException {
        MolangExpression left = comparison();

        while (match(DOUBLE_AMPERSAND)) {
            MolangTokenInstance operator = previous();

            // Read the right-hand of the && expression.
            try {
                MolangExpression right = comparison();
                left = new BinaryExpression(left, right, operator.type());
            } catch (MolangParseException parseException) {
                throw new MolangParseException("Expected to find an expression after '&&' (AND) operator. Did you forget the second value?");
            }
        }

        return left;
    }

    private MolangExpression comparison() throws MolangParseException {
        MolangExpression left = term();

        while (match(LESS_THAN, GREATER_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN_OR_EQUAL)) {
            MolangTokenInstance operator = previous();
            left = new BinaryExpression(left, term(), operator.type());
        }

        return left;
    }

    private MolangExpression term() throws MolangParseException {
        MolangExpression left = factor();

        // +
        while (match(PLUS)) {
            left = new AdditionExpression(left, factor());
        }

        // -
        while (match(MINUS)) {
            left = new SubtractionExpression(left, factor());
        }

        return left;
    }

    private MolangExpression factor() throws MolangParseException {
        MolangExpression left = unary();

        // *
        while (match(STAR)) {
            left = new MultiplicationExpression(left, unary());
        }

        // /
        while (match(SLASH)) {
            left = new DivisionExpression(left, unary());
        }

        return left;
    }

    private MolangExpression unary() throws MolangParseException {
        if(match(MINUS, BANG)) {
            MolangTokenInstance operator = previous();
            MolangExpression right = parenthesis();
            return new UnaryExpression(right, operator.type());
        }

        return parenthesis();
    }

    private MolangExpression parenthesis() throws MolangParseException {
        if(match(LEFT_PAREN)) {
            MolangExpression interior = expression();
            if(!match(RIGHT_PAREN)) {
                throw new MolangParseException("Expected to find closing ')' to end opening '('!");
            }

            return interior;
        }

        return unit();
    }

    private MolangExpression unit() throws MolangParseException {
        if(match(NUMBER)) {
            return new ConstantExpression((float) previous().value());
        }

        if(match(IDENTIFIER)) {

            // variable.<x>
            MolangTokenInstance token = previous();
            if(token.value() instanceof String string) {
                if(!match(DOT)) {
                    throw new MolangParseException("Expected to find reference . after '%s'!".formatted(string));
                }

                return switch (string) {
                    case "query", "q" -> reference(ReferenceType.QUERY);
                    case "variable", "v" -> reference(ReferenceType.VARIABLE);
                    case "math", "m" -> math();
                    default -> throw new IllegalStateException("Unexpected value: " + string);
                };
            }
        }

        throw new MolangParseException("Failed to parse next token: " + (cursor == 0 ? peek() : previous()));
    }

    private MolangExpression math() throws MolangParseException {
        if(match(IDENTIFIER)) {
            String function = previous().lexeme();

            // only math.pi doesn't have a method call
            if(!function.equals("pi")) {
                if(!match(LEFT_PAREN)) {
                    throw new MolangParseException("Expected to find opening parenthesis '(' when starting math call!");
                }
            }

            MolangExpression mathExpression = switch(function) {
                case "abs" -> new MathExpression.Abs(expression());
                case "acos" -> new MathExpression.Acos(expression());
                case "asin" -> new MathExpression.Asin(expression());
                case "atan" -> new MathExpression.Atan(expression());
                case "atan2" -> new MathExpression.Atan2(expressionArgument(true), expression());
                case "ceil" -> new MathExpression.Ceil(expression());
                case "clamp" -> new MathExpression.Clamp(expressionArgument(true), expressionArgument(true), expression());
                case "cos" -> new MathExpression.Cos(expression());
                case "die_roll" -> new MathExpression.DieRoll(expressionArgument(true), expressionArgument(true), expression());
                case "die_roll_integer" -> new MathExpression.DieRollInteger(expressionArgument(true), expressionArgument(true), expression());
                case "exp" -> new MathExpression.Exp(expression());
                case "floor" -> new MathExpression.Floor(expression());
                case "hermite_blend" -> new MathExpression.HermiteBlend(expression());
                case "lerp" -> new MathExpression.Lerp(expressionArgument(true), expressionArgument(true), expression());
                // case "lerprotate" -> new MathExpression.LerpRotate(expression());
                case "ln" -> new MathExpression.Ln(expression());
                case "max" -> new MathExpression.Max(expressionArgument(true), expression());
                case "min" -> new MathExpression.Min(expressionArgument(true), expression());
                case "min_angle" -> new MathExpression.MinAngle(expression());
                case "mod" -> new MathExpression.Mod(expressionArgument(true), expression());
                case "pi" -> new MathExpression.Pi();
                case "pow" -> new MathExpression.Pow(expressionArgument(true), expression());
                case "random" -> new MathExpression.Random(expressionArgument(true), expression());
                case "random_integer" -> new MathExpression.RandomInteger(expressionArgument(true), expression());
                case "round" -> new MathExpression.Round(expression());
                case "sin" -> new MathExpression.Sin(expression());
                case "sqrt" -> new MathExpression.Sqrt(expression());
                case "trunc" -> new MathExpression.Trunc(expression());
                default -> throw new IllegalStateException("Unexpected math function: " + function);
            };

            if(!function.equals("pi")) {
                if(!match(RIGHT_PAREN)) {
                    throw new MolangParseException("Expected to find closing parenthesis ')' when ending math call!");
                }
            }

            return mathExpression;
        }

        throw new MolangParseException("Expected to find math function name after 'math.'");
    }

    private MolangExpression reference(ReferenceType type) throws MolangParseException {
        if(match(IDENTIFIER)) {
            return new ReferenceExpression(type, previous().lexeme());
        }

        throw new MolangParseException("Expected to find name after .");
    }

    private MolangTokenInstance peek() {
        return input.tokens().get(cursor);
    }

    private MolangTokenInstance previous() {
        return input.tokens().get(cursor - 1);
    }

    /**
     * Returns {@code true} if the next available {@link MolangTokenInstance} is one of the specified {@link MolangTokenType}, otherwise {@code false} if it does not match.
     *
     * <p>
     * If this parser is at the end of its token input, {@code false} is returned.
     *
     * @param type any {@link MolangTokenType} to try to match the next {@link MolangTokenInstance} against
     * @return {@code true} if the next token instance matches the given type(s), otherwise {@code false}
     */
    private boolean match(MolangTokenType... type) {
        if(cursor >= input.tokens().size()) {
            return false;
        }

        MolangTokenType next = input.tokens().get(cursor).type();
        for (MolangTokenType check : type) {
            if(next == check) {
                cursor++;
                return true;
            }
        }

        return false;
    }
}
