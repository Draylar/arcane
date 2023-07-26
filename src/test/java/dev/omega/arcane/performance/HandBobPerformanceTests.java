package dev.omega.arcane.performance;

import dev.omega.arcane.Molang;
import dev.omega.arcane.ast.MolangExpression;
import dev.omega.arcane.ast.ObjectAwareExpression;
import dev.omega.arcane.reference.ExpressionBindingContext;
import dev.omega.arcane.reference.ReferenceType;
import dev.omega.arcane.exception.MolangLexException;
import dev.omega.arcane.exception.MolangParseException;
import dev.omega.arcane.parser.MolangParser;
import dev.omega.arcane.util.DummyEntityObject;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class HandBobPerformanceTests {

    private final ExpressionBindingContext context = ExpressionBindingContext.create()
            .registerReferenceResolver(ReferenceType.QUERY, "life_time", DummyEntityObject.class, EntityAgeExpression::new)
            .registerReferenceResolver(ReferenceType.QUERY, "is_on_ground", DummyEntityObject.class, EntityAgeExpression::new)
            .registerReferenceResolver(ReferenceType.QUERY, "is_alive", DummyEntityObject.class, EntityAgeExpression::new)
            .registerReferenceResolver(ReferenceType.QUERY, "position_delta", DummyEntityObject.class, EntityAgeExpression::new)
            .registerReferenceResolver(ReferenceType.QUERY, "hand_bob", DummyEntityObject.class, EntityAgeExpression::new);

    @Test
    public void testHandBob() throws MolangLexException, MolangParseException {
        String input = "query.life_time < 0.01 ? 0.0 : query.hand_bob + ((query.is_on_ground && query.is_alive ? math.clamp(math.sqrt(math.pow(query.position_delta, 2.0) + math.pow(query.position_delta, 2.0)), 0.0, 0.1) : 0.0) - query.hand_bob) * 0.02";
        DummyEntityObject entity = new DummyEntityObject();

        MolangExpression expression = MolangParser.parse(input).bind(context, entity);

        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            expression.evaluate();
        }

        Molang.LOGGER.info("Arcane MoLang parser took: " + (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - TimeUnit.NANOSECONDS.toMillis(start)) + "ms");
    }

    private static class EntityAgeExpression extends ObjectAwareExpression<DummyEntityObject> {

        public EntityAgeExpression(DummyEntityObject value) {
            super(value);
        }

        @Override
        public float evaluate() {
            value.incrementAge();
            return value.getAge();
        }
    }
}
