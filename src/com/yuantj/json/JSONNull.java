package com.yuantj.json;

/**
 * This class represents a JSON null.
 *
 * @author yuantj
 * @version 1.0
 */
public final class JSONNull extends JSONElement {
    /**
     * A {@code JSONNull} object representing {@code null}.
     */
    public static final JSONNull NULL = new JSONNull();

    private JSONNull() {
        super(JSONType.NULL);
    }

    /**
     * Returns {@code null} as the raw object.
     *
     * @return {@code null}.
     */
    @Override
    public Void toRawObject() {
        return null;
    }

    /**
     * Returns {@code true} since instances of this class represents
     * JSON null.
     *
     * @return {@code true}.
     */
    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    String toJSON(boolean ascii) {
        return "null";
    }

    @Override
    String toJSON(int indent, boolean ascii) {
        return "null";
    }

    /**
     * Returns {@code true} if the specified object is an instance
     * of this class.  All instances of this class are considered equal.
     *
     * @param obj The object to be compared.
     * @return {@code true} if the specified object is an instance
     * of this class.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof JSONNull;
    }

    /**
     * Returns a hash code for this object.  This method returns {@code 0}
     * to be compatible with {@link java.util.Objects#hashCode(Object)}.
     *
     * @return {@code 0}.
     */
    @Override
    public int hashCode() {
        return 0;
    }
}
