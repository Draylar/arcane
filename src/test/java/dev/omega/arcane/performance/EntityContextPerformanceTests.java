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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntityContextPerformanceTests {

    private final ExpressionBindingContext context = ExpressionBindingContext.create()
            .registerReferenceResolver(
                    ReferenceType.QUERY,
                    "age", DummyEntityObject.class,
                    EntityAgeExpression::new
            );

    @Test
    public void Performance_EntityObjectAge_PrintTime() throws MolangLexException, MolangParseException {
        DummyEntityObject entity = new DummyEntityObject();
        entity.incrementAge();

        // Give the parser access to (1) context, which will let it simplify variable.age, and (2) the parent entity
        MolangExpression expression = MolangParser.parse("query.age").bind(context, entity);
        Assertions.assertInstanceOf(ObjectAwareExpression.class, expression);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100_000_000; i++) expression.evaluate();
        Molang.LOGGER.info((System.currentTimeMillis() - start) + "ms");
    }

    @Test
    public void Performance_ComplexEntityObjectAge_PrintTime() throws MolangLexException, MolangParseException {
        String input = "query.age + 5.0 + query.age * 3.0 + query.age + query.age + query.age / 3.0 - 7.0";
        DummyEntityObject entity = new DummyEntityObject();
        MolangExpression expression = MolangParser.parse(input).bind(context, entity);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1_000_000; i++) {
            expression.evaluate();
        }
        Molang.LOGGER.info("Arcane MoLang parser took: " + (System.currentTimeMillis() - start) + "ms");
    }

    private static class EntityAgeExpression extends ObjectAwareExpression<DummyEntityObject> {

        public EntityAgeExpression(DummyEntityObject value) {
            super(value);
        }

        @Override
        public float evaluate() {
            return value.getAge();
        }
    }
}
