package dev.omega.arcane.ast.math;

import dev.omega.arcane.Molang;
import dev.omega.arcane.ast.MolangExpression;
import dev.omega.arcane.reference.ExpressionBindingContext;

public class MathExpression {

    public record Abs(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return Math.abs(input.evaluate());
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Abs(input.bind(context, values));
        }
    }

    public record Acos(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.acos(Math.toRadians(input.evaluate()));
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Acos(input.bind(context, values));
        }
    }

    public record Asin(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.asin(Math.toRadians(input.evaluate()));
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Asin(input.bind(context, values));
        }
    }

    public record Atan(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.atan(Math.toRadians(input.evaluate()));
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Atan(input.bind(context, values));
        }
    }

    public record Atan2(MolangExpression y, MolangExpression x) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.atan2(Math.toRadians(y.evaluate()), Math.toRadians(x.evaluate()));
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Atan2(y.bind(context, values), x.bind(context, values));
        }
    }

    public record Ceil(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.ceil(input.evaluate());
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Ceil(input.bind(context, values));
        }
    }

    public record Clamp(MolangExpression input, MolangExpression min, MolangExpression max) implements MolangExpression {

        @Override
        public float evaluate() {
            float value = input.evaluate();
            return Math.min(Math.max(value, min.evaluate()), max.evaluate());
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Clamp(input.bind(context, values), min.bind(context, values), max.bind(context, values));
        }
    }

    public record Cos(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.cos(Math.toRadians(input.evaluate()));
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Cos(input.bind(context, values));
        }
    }

    public record DieRoll(MolangExpression num, MolangExpression low, MolangExpression high) implements MolangExpression {

        @Override
        public float evaluate() {
            float sum = 0.0f;

            for(int i = 0; i < (int) num.evaluate(); i++) {
                sum += low.evaluate() + Molang.RANDOM.nextFloat() * high.evaluate();
            }

            return sum;
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new DieRoll(num.bind(context, values), low.bind(context, values), high.bind(context, values));
        }
    }

    public record DieRollInteger(MolangExpression num, MolangExpression low, MolangExpression high) implements MolangExpression {

        @Override
        public float evaluate() {
            float sum = 0.0f;

            for(int i = 0; i < (int) num.evaluate(); i++) {
                sum += (int) low.evaluate() + (int) (Molang.RANDOM.nextFloat() * high.evaluate());
            }

            return sum;
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new DieRollInteger(num.bind(context, values), low.bind(context, values), high.bind(context, values));
        }
    }

    public record Exp(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.exp(input.evaluate());
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Exp(input.bind(context, values));
        }
    }

    public record Floor(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.floor(input.evaluate());
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Floor(input.bind(context, values));
        }
    }

    public record HermiteBlend(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            float t = input.evaluate();
            return (float) (3 * Math.pow(t, 2)) - (float) (2 * Math.pow(t, 3));
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new HermiteBlend(input.bind(context, values));
        }
    }

    public record Lerp(MolangExpression input, MolangExpression end, MolangExpression zeroToOne) implements MolangExpression {

        @Override
        public float evaluate() {
            float a = input().evaluate();
            float b = end().evaluate();
            return a + (b - a) * zeroToOne.evaluate();
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Lerp(input.bind(context, values), end.bind(context, values), zeroToOne.bind(context, values));
        }
    }

    public record Min(MolangExpression a, MolangExpression b) implements MolangExpression {

        @Override
        public float evaluate() {
            return Math.min(a.evaluate(), b.evaluate());
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Min(a.bind(context, values), b.bind(context, values));
        }
    }

    public record Max(MolangExpression a, MolangExpression b) implements MolangExpression {

        @Override
        public float evaluate() {
            return Math.max(a.evaluate(), b.evaluate());
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Max(a.bind(context, values), b.bind(context, values));
        }
    }

    public record MinAngle(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            float mid = input.evaluate();
            return Math.min(180, Math.max(-180.0f, mid));
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new MinAngle(input.bind(context, values));
        }
    }

    public record Mod(MolangExpression value, MolangExpression denominator) implements MolangExpression {

        @Override
        public float evaluate() {
            return value.evaluate() % denominator.evaluate();
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Mod(value.bind(context, values), denominator.bind(context, values));
        }
    }

    public record Pi() implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.PI;
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return this;
        }
    }

    public record Pow(MolangExpression base, MolangExpression exponent) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.pow(base.evaluate(), exponent.evaluate());
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Pow(base.bind(context, values), exponent.bind(context, values));
        }
    }

    public record Random(MolangExpression low, MolangExpression high) implements MolangExpression {

        @Override
        public float evaluate() {
            return low.evaluate() + Molang.RANDOM.nextFloat() * high.evaluate();
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Random(low.bind(context, values), high.bind(context, values));
        }
    }

    public record RandomInteger(MolangExpression low, MolangExpression high) implements MolangExpression {

        @Override
        public float evaluate() {
            // high is inclusive, so we add 'close-to-1' value to give high value ane qual chance at being picked
            return (int) (low.evaluate() + Molang.RANDOM.nextFloat() * high.evaluate() + 0.999);
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new RandomInteger(low.bind(context, values), high.bind(context, values));
        }
    }

    public record Sin(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.sin(Math.toRadians(input.evaluate()));
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Sin(input.bind(context, values));
        }
    }

    public record Sqrt(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.sqrt(input.evaluate());
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Sqrt(input.bind(context, values));
        }
    }

    public record Trunc(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) (int) input.evaluate();
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Trunc(input.bind(context, values));
        }
    }

    public record Ln(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.log(input.evaluate());
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Ln(input.bind(context, values));
        }
    }

    public record Round(MolangExpression input) implements MolangExpression {

        @Override
        public float evaluate() {
            return (float) Math.round(input.evaluate());
        }

        @Override
        public MolangExpression bind(ExpressionBindingContext context, Object... values) {
            return new Round(input.bind(context, values));
        }
    }
}
