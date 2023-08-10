package dev.omega.arcane.reference;

import dev.omega.arcane.ast.MolangExpression;
import dev.omega.arcane.ast.ObjectAwareExpression;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

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
        getTypeEvaluator(type).add(new FunctionBinder<>(objectClass, value, mapper));
        return this;
    }

    @ApiStatus.Experimental
    public <T> ExpressionBindingContext registerDirectReferenceResolver(ReferenceType type, String value, Class<T> objectClass, Function<T, Float> mapper) {
        getTypeEvaluator(type).add(new FunctionBinder<>(objectClass, value, bindingValue -> new DynamicObjectAwareExpression<>(bindingValue, mapper)));
        return this;
    }

    /**
     * Register a new reference resolver for this {@link ExpressionBindingContext}, which does not require a value for input.
     * For more information, visit {@link MolangExpression#bind(ExpressionBindingContext, Object...)}.
     *
     * <p>
     * Unlike {@link ExpressionBindingContext#registerReferenceResolver(ReferenceType, String, Class, Function)}, this method takes a {@link Supplier},
     * which allows you to return things like global static values, which do not depend on any single object instance.
     *
     * @param type the type (variable, query, ...) of the reference being used
     * @param value the identifier after the reference type ({@code variable.identifier}) to bind with this resolver
     * @param mapper a mapper to return values for the reference expression
     * @return this {@code ExpressionBindingContext} for building
     * @param <T> object type for reference resolving
     */
    @ApiStatus.AvailableSince("1.1.0")
    public <T> ExpressionBindingContext registerReferenceResolver(ReferenceType type, String value, Supplier<ObjectAwareExpression<Void>> mapper) {
        getTypeEvaluator(type).add(new SupplierBinder<>(value, mapper));
        return this;
    }

    @ApiStatus.Experimental
    public <T> ExpressionBindingContext registerDirectReferenceResolver(ReferenceType type, String value, Class<T> objectClass, Supplier<Float> mapper) {
        getTypeEvaluator(type).add(new FunctionBinder<>(objectClass, value, bindingValue -> new DynamicObjectAwareExpression<>(bindingValue, any -> mapper.get())));
        return this;
    }

    @ApiStatus.Internal
    @Nullable
    public List<Binder<?>> getEvaluators(ReferenceType type) {
        return evaluators.get(type);
    }

    @ApiStatus.Internal
    @NotNull
    private List<Binder<?>> getTypeEvaluator(ReferenceType type) {
        return evaluators.computeIfAbsent(type, key -> new ArrayList<>());
    }

    public interface Binder<T> {

        /**
         * Returns the identifier this binder is targeting.
         *
         * <p>
         * For example: given {@code query.anim_time}, this method will return {@code "anim_time"}.
         */
        String getReferenceName();

        @Nullable
        Class<T> getExpectedClass();

        ObjectAwareExpression<T> bind(@Nullable T value);
    }

    private record FunctionBinder<T>(Class<T> value, String expression, Function<T, ObjectAwareExpression<T>> mapper) implements Binder<T> {

        @Override
        public String getReferenceName() {
            return expression;
        }

        @Override
        public Class<T> getExpectedClass() {
            return value;
        }

        @Override
        public ObjectAwareExpression<T> bind(@Nullable T value) {
            return mapper.apply(value);
        }
    }

    private record SupplierBinder<T>(String expression, Supplier<ObjectAwareExpression<Void>> mapper) implements Binder<T> {

        @Override
        public String getReferenceName() {
            return expression;
        }

        @Nullable
        @Override
        public Class<T> getExpectedClass() {
            return null;
        }

        @Override
        public ObjectAwareExpression<T> bind(@Nullable T value) {
            //noinspection unchecked
            return (ObjectAwareExpression<T>) mapper.get();
        }
    }
}
