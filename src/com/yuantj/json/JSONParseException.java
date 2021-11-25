package com.yuantj.json;

/**
 * Thrown when the parser try to parse an invalid JSON string, for example,
 * syntax error.
 *
 * @author yuantj
 * @version 1.0
 */
public class JSONParseException extends IllegalArgumentException {
    /**
     * Constructs a {@code JSONParseException} with no detail message.
     */
    public JSONParseException() {
        super();
    }

    /**
     * Constructs a {@code JSONParseException} with the specified detail message.
     *
     * @param s The detail message.
     */
    public JSONParseException(String s) {
        super(s);
    }
}
