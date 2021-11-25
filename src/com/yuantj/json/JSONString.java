package com.yuantj.json;

import java.io.IOException;

/**
 * This class represents a JSON string.
 *
 * @author yuantj
 * @version 1.0
 */
public final class JSONString extends JSONElement {
    final String value;

    private JSONString(String value) {
        super(JSONType.STRING);
        assert value != null;
        this.value = value;
    }

    /**
     * Returns a JSON string represented by this object (invoking {@link Object#toString}).
     *
     * @param value A non-null object.
     * @return A JSON string containing the given character sequence.
     * @throws NullPointerException if {@code value} is {@code null}.
     */
    public static JSONString of(Object value) {
        return new JSONString(value.toString());
    }

    /**
     * Returns a JSON string containing the given single character.
     *
     * @param value The single character.
     * @return A JSON string containing the given single character.
     */
    public static JSONString of(char value) {
        return new JSONString(String.valueOf(value));
    }

    /**
     * Returns the raw string of this JSON string.
     *
     * @return The raw string.
     */
    @Override
    public String toRawObject() {
        return value;
    }

    /**
     * Returns the raw string of this JSON string.
     *
     * @return The raw string.
     */
    @Override
    public String getString() {
        return value;
    }

    @Override
    Appendable appendJSON(Appendable appendable, boolean ascii) throws IOException {
        return rawStringToJSON(value, appendable, ascii);
    }

    /**
     * Returns {@code true} if the specified object is an instance of
     * {@code JSONString} and contains the same string with this.
     *
     * @param obj The object to be compared.
     * @return {@code true} if the specified object is equal to this.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof JSONString s && s.value.equals(value);
    }

    /**
     * Returns a hash code for this object, calculated by {@link String#hashCode()}.
     *
     * @return A hash code for this object
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    static String rawStringToJSON(String value, boolean ascii) {
        try {
            return rawStringToJSON(value, new StringBuilder(), ascii).toString();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    static Appendable rawStringToJSON(String value, Appendable appendable, boolean ascii) throws IOException {
        appendable.append("\"");
        for (var i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"':
                    appendable.append("\\\"");
                    break;
                case '\\':
                    appendable.append("\\\\");
                    break;
                case '/':
                    appendable.append("\\/");
                    break;
                case '\b':
                    appendable.append("\\b");
                    break;
                case '\f':
                    appendable.append("\\f");
                    break;
                case '\n':
                    appendable.append("\\n");
                    break;
                case '\r':
                    appendable.append("\\r");
                    break;
                case '\t':
                    appendable.append("\\t");
                    break;
                default:
                    if (ascii && (c < 0x20 || c > 0x7E)) {
                        appendable.append(String.format("\\u%04x", (int)c));
                    } else {
                        appendable.append(c);
                    }
                    break;
            }
        }
        return appendable.append("\"");
    }
}
