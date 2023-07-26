package dev.omega.arcane.ast;

import dev.omega.arcane.reference.ExpressionBindingContext;
import dev.omega.arcane.lexer.MolangTokenType;

public record UnaryExpression(MolangExpression expression, MolangTokenType operator) implements MolangExpression {

    @Override
    public float evaluate() {
        float value = expression.evaluate();

        return switch(operator) {
            case BANG -> {
                if(value == 0.0f) {
                    yield 1.0f;
                } else if (value == 1.0f) {
                    yield 0.0f;
                }

                yield 0.0f;
            }

            case MINUS -> -value;
            case PLUS -> value;
            default -> throw new IllegalStateException("UnaryExpression found invalid MoLangTokenType " + operator.toString());
        };
    }

    @Override
    public MolangExpression simplify() {
        MolangExpression simplifiedInput = expression.simplify();
        UnaryExpression simplifiedUnary = new UnaryExpression(simplifiedInput, operator);

        if(simplifiedInput instanceof ConstantExpression) {
            return new ConstantExpression(simplifiedUnary.evaluate());
        }

        return simplifiedUnary;
    }

    @Override
    public MolangExpression bind(ExpressionBindingContext context, Object[] values) {
        return new UnaryExpression(expression.bind(context, values), operator);
    }
}
