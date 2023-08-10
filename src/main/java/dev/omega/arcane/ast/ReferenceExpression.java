package dev.omega.arcane.ast;

import dev.omega.arcane.Molang;
import dev.omega.arcane.reference.ExpressionBindingContext;
import dev.omega.arcane.reference.ReferenceType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ReferenceExpression(ReferenceType type, String value) implements MolangExpression {

    @Override
    public float evaluate() {
        return 0.0f;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public MolangExpression bind(ExpressionBindingContext context, Object[] values) {
        MolangExpression expression = this;

        // If the context provides a way to bind this ReferenceExpression to an Object value, try to simplify it down now~
        @Nullable List<ExpressionBindingContext.Binder<?>> evaluators = context.getEvaluators(type);
        if(evaluators != null) {
            for (ExpressionBindingContext.Binder binder : evaluators) {
                if(binder.getReferenceName().equals(value)) {
                    @Nullable Class<?> expectedClass = binder.getExpectedClass();

                    // Try to find the expected class the mapper wants from our Object[]
                    if(expectedClass != null) {
                        for (Object value : values) {
                            if(value.getClass() == expectedClass) {
                                return binder.bind(value);
                            }
                        }
                    } else {
                        return binder.bind(null);
                    }
                }
            }
        }

        Molang.LOGGER.warning("Was not able to bind %s %s to a value!".formatted(type.name().toLowerCase(), value));
        return this;
    }
}
