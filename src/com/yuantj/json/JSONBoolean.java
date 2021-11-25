package com.yuantj.json;

/**
 * This class represents a JSON boolean.
 *
 * @author yuantj
 * @version 1.0
 */
public final class JSONBoolean extends JSONElement {
    /**
     * A {@code JSONBoolean} object representing {@code true}.
     */
    public static final JSONBoolean TRUE = new JSONBoolean(true);

    /**
     * A {@code JSONBoolean} object representing {@code false}.
     */
    public static final JSONBoolean FALSE = new JSONBoolean(false);

    final boolean value;

    private JSONBoolean(boolean value) {
        super(JSONType.BOOLEAN);
        this.value = value;
    }

    /**
     * Returns a JSON boolean representing the specified value.
     *
     * @param value The boolean value.
     * @return A JSON boolean representing the specified value.
     */
    public static JSONBoolean of(boolean value) {
        return value ? TRUE : FALSE;
    }

    /**
     * Returns a JSON boolean representing the specified value.
     *
     * @param value The Boolean value.
     * @return A JSON boolean representing the specified value.
     */
    public static JSONBoolean of(Boolean value) {
        return value ? TRUE : FALSE;
    }

    /**
     * Returns the raw {@link Boolean} value of this JSON boolean.
     *
     * @return The raw {@link Boolean} value.
     */
    @Override
    public Boolean toRawObject() {
        return value;
    }

    /**
     * Returns the raw {@code boolean} value of this JSON boolean.
     *
     * @return The raw {@code boolean} value.
     */
    @Override
    public boolean getBoolean() {
        return value;
    }


    @Override
    String toJSON(boolean ascii) {
        return value ? "true" : "false";
    }

    @Override
    String toJSON(int indent, boolean ascii) {
        return value ? "true" : "false";
    }

    /**
     * Returns {@code true} if the specified object is also a JSON boolean
     * and has the same value with this object.
     *
     * @param obj The object to be compared.
     * @return {@code true} if the parameter is equal to this.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof JSONBoolean b && b.value == value;
    }

    /**
     * Returns a hash code for this object.  Compatible with {@link Boolean#hashCode()}
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }
}
