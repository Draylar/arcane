package dev.omega.arcane.reference;

import dev.omega.arcane.ast.ObjectAwareExpression;

import java.util.function.Function;

public class DynamicObjectAwareExpression<T> extends ObjectAwareExpression<T> {

    private final Function<T, Float> mapper;

    public DynamicObjectAwareExpression(T value, Function<T, Float> mapper) {
        super(value);
        this.mapper = mapper;
    }

    @Override
    public float evaluate() {
        return mapper.apply(value);
    }
}
