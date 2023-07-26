package dev.omega.arcane.parser;

import dev.omega.arcane.ast.BinaryExpression;
import dev.omega.arcane.ast.ConstantExpression;
import dev.omega.arcane.ast.UnaryExpression;
import dev.omega.arcane.exception.MolangLexException;
import dev.omega.arcane.exception.MolangParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTests {

    @Test
    public void Parse_Constant_ReturnsBinaryExpression() throws MolangLexException, MolangParseException {
        Assertions.assertInstanceOf(ConstantExpression.class, MolangParser.parse("5.0", MolangParser.FLAG_NONE));
    }

    @Test
    public void Parse_Unary_ReturnsUnaryExpression() throws MolangLexException, MolangParseException {
        Assertions.assertInstanceOf(UnaryExpression.class, MolangParser.parse("-5", MolangParser.FLAG_NONE));
    }

    @Test
    public void Parse_BinaryAddition_ReturnsBinaryExpression() throws MolangLexException, MolangParseException {
        Assertions.assertInstanceOf(BinaryExpression.class, MolangParser.parse("5 + 5", MolangParser.FLAG_NONE));
    }

    @Test
    public void Parse_BinarySubtraction_ReturnsBinaryExpression() throws MolangLexException, MolangParseException {
        Assertions.assertInstanceOf(BinaryExpression.class, MolangParser.parse("5 - 5", MolangParser.FLAG_NONE));
    }

    @Test
    public void Parse_BinaryMultiplication_ReturnsBinaryExpression() throws MolangLexException, MolangParseException {
        Assertions.assertInstanceOf(BinaryExpression.class, MolangParser.parse("5 * 5", MolangParser.FLAG_NONE));
    }

    @Test
    public void Parse_BinaryDivision_ReturnsBinaryExpression() throws MolangLexException, MolangParseException {
        Assertions.assertInstanceOf(BinaryExpression.class, MolangParser.parse("5 / 5", MolangParser.FLAG_NONE));
    }

    @Test
    public void Parse_BinaryAnd_ReturnsBinaryExpression() throws MolangLexException, MolangParseException {
        Assertions.assertInstanceOf(BinaryExpression.class, MolangParser.parse("5 && 5", MolangParser.FLAG_NONE));
    }

    // todo: not actually sure if "OR" is part of the Molang spec?
    @Test
    public void Parse_BinaryOr_ReturnsBinaryExpression() throws MolangLexException, MolangParseException {
        Assertions.assertInstanceOf(BinaryExpression.class, MolangParser.parse("5 || 5", MolangParser.FLAG_NONE));
    }

    @Test
    public void Parse_NestedBinaryDivision_ReturnsBinaryExpression() throws MolangLexException, MolangParseException {
        Assertions.assertInstanceOf(BinaryExpression.class, MolangParser.parse("5 / 5 / 5", MolangParser.FLAG_NONE));
    }
}
