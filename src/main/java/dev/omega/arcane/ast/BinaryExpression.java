package dev.omega.arcane.ast;

import dev.omega.arcane.reference.ExpressionBindingContext;
import dev.omega.arcane.lexer.MolangTokenType;

public record BinaryExpression(MolangExpression left, MolangExpression right, MolangTokenType operator) implements MolangExpression {

    @Override
    public float evaluate() {
        float leftValue = left.evaluate();
        float rightValue = right.evaluate();

        return switch(operator) {
            case LESS_THAN -> leftValue < rightValue ? 1.0f : 0.0f;
            case GREATER_THAN -> leftValue > rightValue ? 1.0f : 0.0f;
            case LESS_THAN_OR_EQUAL -> leftValue <= rightValue ? 1.0f : 0.0f;
            case GREATER_THAN_OR_EQUAL -> leftValue >= rightValue ? 1.0f : 0.0f;

            // "For boolean tests, a float value equivalent to 0.0 is false, and anything not equal to 0.0 is true." - Molang language documentation
            case DOUBLE_AMPERSAND -> {
                if(leftValue == 0.0 || rightValue == 0.0) {
                    yield 0.0f;
                }

                yield 1.0f;
            }

            // Not sure if this is supported by Molang natively
            case DOUBLE_PIPE -> {
                if(leftValue != 0.0 || rightValue != 0.0) {
                    yield 1.0f;
                }

                yield 0.0f;
            }

            default -> throw new RuntimeException("Operator type '" + operator + "' is not supported for Unary operations.");
        };
    }

    @Override
    public MolangExpression simplify() {
        MolangExpression leftSimplified = left.simplify();
        MolangExpression rightSimplified = right.simplify();
        BinaryExpression simplifiedExpression = new BinaryExpression(leftSimplified, rightSimplified, operator);

        // If both simplified inputs are constants, we can evaluate them now with the operand to pull out a single ConstantExpression.
        if(leftSimplified instanceof ConstantExpression && rightSimplified instanceof ConstantExpression) {
            return new ConstantExpression(simplifiedExpression.evaluate());
        }

        return simplifiedExpression;
    }

    @Override
    public MolangExpression bind(ExpressionBindingContext context, Object... values) {
        return new BinaryExpression(
                left.bind(context, values),
                right.bind(context, values),
                operator
        );
    }
}
