package com.yuantj.json;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;

final class ReaderJSONParser extends JSONParser {
    final Reader reader;
    Character last = null;

    ReaderJSONParser(Reader reader) {
        this.reader = reader;
    }

    @Override
    char current() {
        return last;
    }

    @Override
    void advance() {
        int r;
        try {
            r = reader.read();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        last = r < 0 ? null : (char)r;
    }

    @Override
    boolean hasInput() {
        return last != null;
    }

    @Override
    JSONParseException exception(String message) {
        return new JSONParseException(message);
    }

    @Override
    void expect(String s) {
        int r;
        int len = s.length();
        char[] c = new char[len];
        try {
            r = reader.read(c);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        if (r != len) {
            throw exception("unexpected end");
        }
        for (int i = 0; i < len; ++i) {
            char a = c[i];
            char b = s.charAt(i);
            if (c[i] != s.charAt(i)) {
                throw exception("expected character \"" + a + "\", "
                        + JSONString.rawStringToJSON(String.valueOf(b), false) + " found");
            }
        }
    }
}
