package dev.omega.arcane.ast;

import dev.omega.arcane.reference.ExpressionBindingContext;

public record TernaryExpression(MolangExpression condition, MolangExpression left, MolangExpression right) implements MolangExpression {

    @Override
    public float evaluate() {
        float conditionValue = condition.evaluate();

        // Condition is true if it isn't 0.0
        if(conditionValue != 0.0) {
            return  left.evaluate();
        }

        return right.evaluate();
    }

    @Override
    public MolangExpression bind(ExpressionBindingContext context, Object... values) {
        return new TernaryExpression(
                condition.bind(context, values),
                left.bind(context, values),
                right.bind(context, values)
        );
    }
}
