package com.yuantj.json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

// This implementation is referred from openjdk.
abstract class JSONParser {
    JSONParser() {}

    JSONElement parse() {
        JSONElement e = parseElement();
        if (hasInput()) {
            throw exception("can only have one top-level JSON value");
        }
        return e;
    }

    JSONElement parseElement() {
        JSONElement result = null;
        consumeWhitespace();
        if (hasInput()) {
            char c = current();
            JSONType possibleType = possibleStartType(c);
            if (possibleType == null) {
                throw exception("not a valid start of a JSON value");
            }
            switch (possibleType) {
                case OBJECT:
                    result = parseObject();
                    break;
                case ARRAY:
                    result = parseArray();
                    break;
                case STRING:
                    result = parseString();
                    break;
                case BOOLEAN:
                    result = parseBoolean();
                    break;
                case NUMBER:
                    result = parseNumber();
                    break;
                case NULL:
                    result = parseNull();
                    break;
                default:
                    throw new AssertionError(possibleType);
            }
        }
        consumeWhitespace();
        if (result == null) {
            throw exception("no valid JSON found");
        }
        return result;
    }

    JSONObject parseObject() {
        String error = "object is not terminated with '}'";
        var map = new LinkedHashMap<String, JSONElement>();

        advance(); // step beyond opening '{'
        consumeWhitespace();
        expectMoreInput(error);

        while (current() != '}') {
            JSONElement key = parseElement();
            if (!(key instanceof JSONString)) {
                throw exception("a field must of type string");
            }

            if (!hasInput() || current() != ':') {
                throw exception("a field must be followed by ':'");
            }
            advance(); // skip ':'

            JSONElement val = parseElement();
            map.put(((JSONString)key).toRawObject(), val);

            expectMoreInput(error);
            if (current() == ',') {
                advance();
            }
            expectMoreInput(error);
        }

        advance(); // step beyond '}'
        return JSONObject.ofTrustedMap(Collections.unmodifiableMap(map));
    }

    JSONArray parseArray() {
        String error = "array is not terminated with ']'";
        var list = new ArrayList<JSONElement>();

        advance(); // step beyond opening '['
        consumeWhitespace();
        expectMoreInput(error);

        while (current() != ']') {
            JSONElement val = parseElement();
            list.add(val);

            expectMoreInput(error);
            if (current() == ',') {
                advance();
            }
            expectMoreInput(error);
        }
        advance(); // step beyond closing ']'
        return JSONArray.ofTrustedArray(list.toArray(new JSONElement[0]));
    }

    JSONString parseString() {
        String missingEndChar = "string is not terminated with '\"'";
        var sb = new StringBuilder();
        for (var c = next(missingEndChar); c != '"'; c = next(missingEndChar)) {
            if (c == '\\') {
                var n = next(missingEndChar);
                switch (n) {
                    case '"':
                        sb.append("\"");
                        break;
                    case '\\':
                        sb.append("\\");
                        break;
                    case '/':
                        sb.append("/");
                        break;
                    case 'b':
                        sb.append("\b");
                        break;
                    case 'f':
                        sb.append("\f");
                        break;
                    case 'n':
                        sb.append("\n");
                        break;
                    case 'r':
                        sb.append("\r");
                        break;
                    case 't':
                        sb.append("\t");
                        break;
                    case 'u':
                        var u1 = next(missingEndChar);
                        var u2 = next(missingEndChar);
                        var u3 = next(missingEndChar);
                        var u4 = next(missingEndChar);
                        var cp = Integer.parseInt(String.format("%c%c%c%c", u1, u2, u3, u4), 16);
                        sb.append(new String(new int[]{cp}, 0, 1));
                        break;
                    default:
                        throw exception(String.format("Unexpected escaped character '%c'", n));
                }
            } else {
                sb.append(c);
            }
        }
        advance(); // step beyond closing "
        return JSONString.of(sb);
    }

    JSONBoolean parseBoolean() {
        if (current() == 't') {
            expect("rue");
            advance();
            return JSONBoolean.TRUE;
        }
        if (current() == 'f') {
            expect("alse");
            advance();
            return JSONBoolean.FALSE;
        }
        throw exception("unexpected boolean value");
    }

    JSONNumber parseNumber() {
        var sb = new StringBuilder();
        if (current() == '-') {
            sb.append(current());
            advance();
            expectMoreInput("a number cannot consist of only '-'");
        }
        if (current() == '0') {
            sb.append(current());
            advance();
        } else {
            while (hasInput() && isLatin1Digit(current())) {
                sb.append(current());
                advance();
            }
        }
        if (hasInput() && current() == '.') {
            sb.append(current());
            advance();

            expectMoreInput("a number cannot end with '.'");

            if (!isLatin1Digit(current())) {
                throw exception("must be at least one digit after '.'");
            }

            while (hasInput() && isLatin1Digit(current())) {
                sb.append(current());
                advance();
            }
        }
        if (hasInput() && (current() == 'e' || current() == 'E')) {
            sb.append(current());
            advance();
            expectMoreInput("a number cannot end with 'e' or 'E'");

            if (current() == '+' || current() == '-') {
                sb.append(current());
                advance();
            }

            if (!isLatin1Digit(current())) {
                throw exception("a digit must follow {'e','E'}{'+','-'}");
            }

            while (hasInput() && isLatin1Digit(current())) {
                sb.append(current());
                advance();
            }
        }
        return JSONNumber.of(new BigDecimal(sb.toString()));
    }

    JSONNull parseNull() {
        expect("ull");
        advance();
        return JSONNull.NULL;
    }

    abstract char current();

    abstract void advance();

    abstract boolean hasInput();

    void consumeWhitespace() {
        while (hasInput() && isLatin1WhiteSpace(current())) {
            advance();
        }
    }

    void expect(char c) {
        char n = next();
        if (n != c) {
            throw exception("expected character \"" + c + "\", "
                    + JSONString.rawStringToJSON(String.valueOf(n), false) + " found");
        }
    }

    void expect(String s) {
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            expect(s.charAt(i));
        }
    }

    void expectMoreInput(String message) {
        if (!hasInput()) {
            throw exception(message);
        }
    }

    char next(String message) {
        advance();
        if (!hasInput()) {
            throw exception(message);
        }
        return current();
    }

    char next() {
        return next("unexpected end");
    }

    static JSONType possibleStartType(char c) {
        switch (c) {
            case 't':
            case 'f':
                return JSONType.BOOLEAN;
            case 'n':
                return JSONType.NULL;
            case '"':
                return JSONType.STRING;
            case '[':
                return JSONType.ARRAY;
            case '{':
                return JSONType.OBJECT;
            default:
                return isStartOfDigit(c) ? JSONType.NUMBER : null;
        }
    }

    abstract JSONParseException exception(String message);

    static boolean isLatin1Digit(char c) {
        return c >= '0' && c <= '9';
    }

    static boolean isStartOfDigit(char c) {
        return isLatin1Digit(c) || c == '-';
    }

    static boolean isLatin1WhiteSpace(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }
}
