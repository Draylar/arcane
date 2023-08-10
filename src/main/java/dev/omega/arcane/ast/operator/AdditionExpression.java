package dev.omega.arcane.ast.operator;

import dev.omega.arcane.ast.MolangExpression;
import dev.omega.arcane.reference.ExpressionBindingContext;
import org.jetbrains.annotations.Nullable;

public record AdditionExpression(MolangExpression left, MolangExpression right) implements MolangExpression {

    @Override
    public float evaluate() {
        return left.evaluate() + right.evaluate();
    }

    @Override
    public MolangExpression simplify() {
        @Nullable MolangExpression simplified = simplifyConstantExpression(left.simplify(), right.simplify());
        return simplified == null ? new AdditionExpression(left.simplify(), right.simplify()) : simplified;
    }

    @Override
    public MolangExpression bind(ExpressionBindingContext context, Object... values) {
        return new AdditionExpression(
                left.bind(context, values),
                right.bind(context, values)
        );
    }
}
