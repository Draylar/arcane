package dev.omega.arcane.ast;

import dev.omega.arcane.reference.ExpressionBindingContext;

/**
 * An {@link ObjectAwareExpression} is a {@link MolangExpression} which returns the result of a method call on an object.
 */
public abstract class ObjectAwareExpression<T> implements MolangExpression {

    protected final T value;

    public ObjectAwareExpression(T value) {
        this.value = value;
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
