package dev.omega.arcane.ast;

import dev.omega.arcane.Molang;
import dev.omega.arcane.reference.ExpressionBindingContext;
import dev.omega.arcane.reference.ReferenceType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

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
            for (ExpressionBindingContext.Binder<?> binder : evaluators) {
                if(binder.expression().equals(value)) {
                    Class<?> expectedClass = binder.value();

                    // Try to find the expected class the mapper wants from our Object[]
                    for (Object value : values) {
                        if(value.getClass() == expectedClass) {

                            // Object -> Expression Reference
                            Function mapper = binder.mapper();
                            expression = (ObjectAwareExpression) mapper.apply(value);
                            return expression;
                        }
                    }
                }
            }
        }

        Molang.LOGGER.warning("Was not able to bind %s %s to a value!".formatted(type.name().toLowerCase(), value));
        return this;
    }
}
