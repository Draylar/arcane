package dev.omega.arcane.ast;

import dev.omega.arcane.Molang;
import dev.omega.arcane.reference.ExpressionBindingContext;
import org.jetbrains.annotations.Nullable;

public interface MolangExpression {

    /**
     * Evaluate this {@link MolangExpression} as a {@code float}
     *
     * @return the {@code float} value obtained when evaluating this Molang expression
     */
    float evaluate();

    /**
     * Attempt to simplify this {@link MolangExpression}.
     *
     * <p>
     * Simplification is taking a {@link MolangExpression} and boiling it into a more simple {@link MolangExpression}.
     * As an example, {@code BinaryExpression(ConstantExpression, ConstantExpression, Plus)} could be converted into a
     * single {@code ConstantExpression}, because the return result of the original {@link MolangExpression} would
     * <i>always</i> be the same.
     *
     * <p>
     * If simplification was possible, a <i>new</i> simplified {@link MolangExpression} is created and returned.
     * Otherwise, {@code this} is returned.
     *
     * @return a new simplified version of this {@link MolangExpression}, or {@code this} if simplification was not possible
     */
    default MolangExpression simplify() {
        return this;
    }

    /**
     * Rebinds {@link ReferenceExpression} ({@code variable.x}, {@code query.y}, etc.) into an {@link ObjectAwareExpression} based on the {@link ExpressionBindingContext} and provided {@code values}.
     * This will let you remap (for example) a reference for {@code variable.foo} into a call to {@link Object#hashCode()}.
     *
     * <p>
     * The {@link ExpressionBindingContext} can supply a number of binding rules for multiple types and references.
     * In the below example, {@code variable.foo} is bound to {@code World#getFoo()}, and {@code query.bar} is bound to {@code Player#getBar()}.
     *
     * <pre>{@code
     *  ExpressionBindingContext context = ExpressionBindingContext.create()
     *             .registerReferenceResolver(
     *                  ReferenceType.QUERY,
     *                  "bar",
     *                  Player.class,
     *                  PlayerBarExpression::new
     *             )
     *             .registerReferenceResolver(
     *                  ReferenceType.VARIABLE,
     *                  "foo",
     *                  World.class,
     *                  WorldFooExpression::new
     *             );
     *
     * // Re-bind expression to convert {@link ReferenceExpression} into direct method calls
     * //   -> equivalent to world.getFoo() + player.getBar()
     * var expression = MolangParser.parse("variable.foo + query.bar").bind(
     *      context,
     *      world,
     *      player
     * );
     * }</pre>
     *
     * <p>
     * <b>Binding vs. Manual Update</b>
     * References like {@code variable.x} and {@code query.y} need to be bound to values.
     * In a traditional Molang parser, you would do this by calling {@code .set("variable.foo", 5.0)} each time you wanted the reference to update.
     * This is slow. Binding through {@link ExpressionBindingContext} can map a reference directly to a method call or field on an {@link Object}, which is
     * only ran when {@link MolangExpression#simplify()} is called.
     *
     * @param context
     * @param values
     * @return
     */
    MolangExpression bind(ExpressionBindingContext context, Object... values);

    @Nullable
    default MolangExpression simplifyConstantExpression(MolangExpression... components) {
        for (MolangExpression component : components) {
            if(!(component instanceof ConstantExpression)) {
                return null;
            }
        }

        return new ConstantExpression(evaluate());
    }
}
