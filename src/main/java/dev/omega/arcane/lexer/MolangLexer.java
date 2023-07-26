package dev.omega.arcane.lexer;

import dev.omega.arcane.exception.MolangLexException;

import java.util.ArrayList;
import java.util.List;

public class MolangLexer {

    private final String text;
    private int start = 0;
    private int cursor = 0;

    public MolangLexer(String input) {
        this.text = input;
    }

    /**
     * Lexes the {@code input} into a {@link List} of MoLang tokens based on the language grammar.
     *
     * @param input the MoLang input to be lexed into tokens
     * @throws MolangLexException if the input could not be lexed
     */
    public static LexedMolang lex(String input) throws MolangLexException {
        return new MolangLexer(input).lex();
    }

    private LexedMolang lex() throws MolangLexException {
        ArrayList<MolangTokenInstance> tokens = new ArrayList<>();

        while(cursor < text.length()) {
            MolangTokenInstance token = next(text.charAt(cursor));
            if(token.type() != MolangTokenType.SPACE) {
                tokens.add(token);
            }

            cursor++;
            start = cursor;
        }

        return new LexedMolang(tokens);
    }

    private MolangTokenInstance next(char input) throws MolangLexException {
        return switch(input) {
            case ' ' -> token(MolangTokenType.SPACE);
            case '+' -> token(MolangTokenType.PLUS);
            case '-' -> token(MolangTokenType.MINUS);
            case '*' -> token(MolangTokenType.STAR);
            case '/' -> token(MolangTokenType.SLASH);
            case '(' -> token(MolangTokenType.LEFT_PAREN);
            case ')' -> token(MolangTokenType.RIGHT_PAREN);
            case '{' -> token(MolangTokenType.LEFT_BRACE);
            case '}' -> token(MolangTokenType.RIGHT_BRACE);
            case '[' -> token(MolangTokenType.LEFT_BRACKET);
            case ']' -> token(MolangTokenType.RIGHT_BRACKET);
            case '.' -> token(MolangTokenType.DOT);
            case ',' -> token(MolangTokenType.COMMA);
            case ':' -> token(MolangTokenType.COLON);
            case ';' -> token(MolangTokenType.SEMICOLON);
            case '=' -> {
                if(tryConsume('=')) {
                    yield token(MolangTokenType.DOUBLE_EQUAL);
                } else {
                    yield token(MolangTokenType.EQUAL);
                }
            }
            case '>' -> {
                if(tryConsume('=')) {
                    yield token(MolangTokenType.GREATER_THAN_OR_EQUAL);
                } else {
                    yield token(MolangTokenType.GREATER_THAN);
                }
            }
            case '<' -> {
                if(tryConsume('=')) {
                    yield token(MolangTokenType.LESS_THAN_OR_EQUAL);
                } else {
                    yield token(MolangTokenType.LESS_THAN);
                }
            }
            case '!' -> {
                if(tryConsume('=')) {
                    yield token(MolangTokenType.BANG_EQUAL);
                } else {
                    yield token(MolangTokenType.BANG);
                }
            }
            case '&' -> {
                if(tryConsume('&')) {
                    yield token(MolangTokenType.DOUBLE_AMPERSAND);
                } else {
                    throw new MolangLexException("Found operator '&' without a second '&' afterwards (bitwise operators not supported)");
                }
            }
            case '|' -> {
                if(tryConsume('|')) {
                    yield token(MolangTokenType.DOUBLE_PIPE);
                } else {
                    throw new MolangLexException("Found operator '|' without a second '||' afterwards (bitwise operators not supported)");
                }
            }
            case '?' -> {
                if(tryConsume('?')) {
                    yield token(MolangTokenType.QUESTION_QUESTION);
                } else {
                    yield token(MolangTokenType.QUESTION);
                }
            }
            case '"' -> string();

            default -> {

                // Number
                if(Character.isDigit(input)) {
                    yield number();
                }

                // Identifier
                if(Character.isAlphabetic(input)) {
                    yield identifier();
                }

                // Unknown
                throw new MolangLexException("Failed to Lex input char: '" + input + "'");
            }
        };
    }

    private MolangTokenInstance token(MolangTokenType type) {
        return new MolangTokenInstance(type, text.substring(start, cursor));
    }

    private MolangTokenInstance string() throws MolangLexException {
        // consume string
        char last = ' ';
        do {
            if(isAtEnd()) {
                throw new MolangLexException("Failed to close string starting at index " + start);
            }

            last = consume();
        } while(last != '"');

        return new MolangTokenInstance(MolangTokenType.STRING, text.substring(start, cursor), text.substring(start + 1, cursor - 1));
    }

    private MolangTokenInstance number() {
        if(!isAtEnd()) {
            char next = peek();
            boolean hasFoundDecimal = false;
            while (!isAtEnd() && Character.isDigit(next) || (!hasFoundDecimal && next == '.')) {
                if(next == '.') {
                    hasFoundDecimal = true;
                }

                consume();

                if(!isAtEnd()) {
                    next = peek();
                }
            }
        }

        String lexeme = text.substring(start, cursor + 1);
        return new MolangTokenInstance(MolangTokenType.NUMBER, lexeme, Float.parseFloat(lexeme));
    }

    private MolangTokenInstance identifier() {
        while(!isAtEnd() && Character.isJavaIdentifierPart(peek())) {
            consume();
        }

        String lexeme = text.substring(start, cursor + 1);
        return new MolangTokenInstance(MolangTokenType.IDENTIFIER, lexeme, lexeme);
    }

    private boolean isAtEnd() {
        return cursor >= text.length() - 1;
    }

    private char peek() {
        return text.charAt(cursor + 1);
    }

    private char consume() {
        cursor++;
        return text.charAt(cursor);
    }

    private boolean tryConsume(char check) {
        if(cursor >= text.length()) {
            return false;
        }

        char next = text.charAt(cursor + 1);
        if(next == check) {
            cursor++;
            return true;
        }

        return false;
    }
}
