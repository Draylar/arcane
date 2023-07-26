package dev.omega.arcane.ast;

import dev.omega.arcane.reference.ExpressionBindingContext;

public record ConstantExpression(float value) implements MolangExpression {

    @Override
    public float evaluate() {
        return value;
    }

    @Override
    public MolangExpression simplify() {
        return this;
    }

    @Override
    public MolangExpression bind(ExpressionBindingContext context, Object[] values) {
        return this;
    }
}
