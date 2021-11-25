package com.yuantj.json;

/**
 * An enum type describes six types of a JSON node.
 *
 * @author yuantj
 * @version 1.0
 */
public enum JSONType {
    /**
     * An object.
     */
    OBJECT,

    /**
     * An array.
     */
    ARRAY,

    /**
     * A string.
     */
    STRING,

    /**
     * A number.
     */
    NUMBER,

    /**
     * A boolean.
     */
    BOOLEAN,

    /**
     * A null.
     */
    NULL;

    /**
     * Returns a string representation, same as
     * <a href="https://www.json.org/json-en.html">JSON documentation</a>.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
