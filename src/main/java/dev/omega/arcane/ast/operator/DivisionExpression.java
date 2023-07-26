package dev.omega.arcane.ast.operator;

import dev.omega.arcane.ast.MolangExpression;
import dev.omega.arcane.reference.ExpressionBindingContext;
import org.jetbrains.annotations.Nullable;

public record DivisionExpression(MolangExpression left, MolangExpression right) implements MolangExpression {

    @Override
    public float evaluate() {
        float bottom = right.evaluate();
        if(bottom == 0.0) {
            return 0.0f;
        }

        return left.evaluate() / bottom;
    }

    @Override
    public MolangExpression simplify() {
        @Nullable MolangExpression simplified = simplifyConstantExpression(left, right);
        return simplified == null ? new DivisionExpression(left.simplify(), right.simplify()) : simplified;
    }

    @Override
    public MolangExpression bind(ExpressionBindingContext context, Object... values) {
        return new DivisionExpression(
                left.bind(context, values),
                right.bind(context, values)
        );
    }
}
