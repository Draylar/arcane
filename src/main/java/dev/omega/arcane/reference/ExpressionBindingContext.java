package dev.omega.arcane.reference;

import dev.omega.arcane.ast.MolangExpression;
import dev.omega.arcane.ast.ObjectAwareExpression;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ExpressionBindingContext {

    private final Map<ReferenceType, List<Binder<?>>> evaluators = new HashMap<>();

    private ExpressionBindingContext() {

    }

    @ApiStatus.AvailableSince("1.0.0")
    public static ExpressionBindingContext create() {
        return new ExpressionBindingContext();
    }

    /**
     * Register a new reference resolver for this {@link ExpressionBindingContext}. For more information, visit {@link MolangExpression#bind(ExpressionBindingContext, Object...)}.
     *
     * @param type the type (variable, query, ...) of the reference being used
     * @param value the identifier after the reference type ({@code variable.identifier}) to bind with this resolver
     * @param objectClass the type of the object being bound to the reference
     * @param mapper a mapper to extract values from an object instance for the reference expression
     * @return this {@code ExpressionBindingContext} for building
     * @param <T> object type for reference resolving
     */
    @ApiStatus.AvailableSince("1.0.0")
    public <T> ExpressionBindingContext registerReferenceResolver(ReferenceType type, String value, Class<T> objectClass, Function<T, ObjectAwareExpression<T>> mapper) {
        evaluators.computeIfAbsent(type, key -> new ArrayList<>()).add(new Binder<>(objectClass, value, mapper));
        return this;
    }

    @ApiStatus.Internal
    @Nullable
    public List<Binder<?>> getEvaluators(ReferenceType type) {
        return evaluators.get(type);
    }

    public record Binder<T>(Class<T> value, String expression, Function<T, ObjectAwareExpression<T>> mapper) {

    }
}
