package com.yuantj.json;

import java.util.Objects;

final class StringJSONParser extends JSONParser {
    private int pos = 0;
    private final String input;

    StringJSONParser(String input) {
        this.input = Objects.requireNonNull(input);
    }

    @Override
    char current() {
        return input.charAt(pos);
    }

    @Override
    void advance() {
        pos++;
    }

    @Override
    boolean hasInput() {
        return pos < input.length();
    }

    @Override
    JSONParseException exception(String message) {
        return new JSONParseException(String.format("[%d]: %s : %s", pos, message, input));
    }
}
