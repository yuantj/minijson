package com.yuantj.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * This class represents an immutable JSON element.  This class has only 6 subclasses:
 * {@link JSONObject} for an object, {@link JSONArray} for an array,
 * {@link JSONString} for a string, {@link JSONNumber} for a number,
 * {@link JSONBoolean} for a boolean and {@link JSONNull} for a null.  Instances
 * of this class are immutable, so it's safe to share them.
 *
 * No other subclasses can be created since the only constructor is package-private
 * and all subclasses are final. To represent your own storage of a JSON element,
 * implement the interface {@link JSONAccessor} instead.
 *
 * Note:  All subclasses do not provide new instance methods, they only override methods
 * from this class.
 *
 * @author yuantj
 * @version 1.0
 *
 * @see JSONObject
 * @see JSONArray
 * @see JSONString
 * @see JSONNumber
 * @see JSONBoolean
 * @see JSONNull
 * @see JSON
 * @see JSONAccessor
 */
public abstract sealed class JSONElement implements JSONAccessor
        permits JSONObject, JSONArray, JSONString, JSONNumber, JSONBoolean, JSONNull {
    final JSONType type;

    // Package-private. No other subclasses permitted.
    JSONElement(JSONType type) {
        super();
        this.type = type;
    }

    /**
     * Returns a corresponding JSON element of this object.
     *
     * If the parameter implements {@link JSONAccessor} (including
     * {@code JSONElement} instances), then this method returns the
     * result of {@link JSONAccessor#toJSONElement()}; otherwise,
     * this method converts the following type to the corresponding
     * {@code JSONElement} instances:
     *
     * <ul>
     *   <li>
     *     <strong>Object:</strong> {@link Map} that contains keys with any type (will
     *     be converted to a string using {@link String#valueOf(Object)} and
     *     values.
     *   </li>
     *   <li>
     *     <strong>Array:</strong> Primitive type array, object array or {@link Iterable}
     *     that contains elements permitted by this method.
     *   </li>
     *   <li>
     *     <strong>Number:</strong> {@link Number}.
     *   </li>
     *   <li>
     *     <strong>Boolean:</strong> {@link Boolean}.
     *   </li>
     *   <li>
     *     <strong>Null: </strong> {@code null}.
     *   </li>
     *   <li>
     *      <strong>String:</strong> Other types (converted by {@link Object#toString()}).
     *   </li>
     * </ul>
     *
     * If the type of the parameter or one of its elements does not match
     * any of the permitted type, then an {@link IllegalArgumentException} is thrown.
     *
     * @apiNote This static method is inherited by subclasses, so make sure
     * you call the correct method when calling other {@code of} methods in subclasses.
     * You can check them with type-checking:
     * <p>
     *   {@code JSONArray a = JSONArray.of(new String[]{"Hello", "World"}); // ok}<br/>
     *   {@code JSONArray b = JSONArray.of(Map.of("foo", "bar")); // ERROR: type mismatch}
     * </p>
     *
     * @param value The parameter to be converted. (nullable)
     * @return The converted result.
     * @throws IllegalArgumentException If the type of the parameter or its
     * element is not supported.
     * @throws NumberFormatException If the parameter or one of its element is a
     * floating-point number which is Infinity or NaN.
     *
     * @see JSONObject#of(Map)
     * @see JSONArray#of(Object[])
     * @see JSONArray#of(byte[])
     * @see JSONArray#of(short[])
     * @see JSONArray#of(int[])
     * @see JSONArray#of(long[])
     * @see JSONArray#of(float[])
     * @see JSONArray#of(double[])
     * @see JSONArray#of(boolean[])
     * @see JSONArray#of(char[])
     * @see JSONArray#of(Iterable)
     * @see JSONString#of(Object)
     * @see JSONBoolean#of(Boolean)
     * @see JSONNull#NULL
     * @see JSONAccessor#toJSONElement()
     */
    public static JSONElement of(Object value) {
        if (value == null) {
            return JSONNull.NULL;
        } else if (value instanceof JSONAccessor a) {
            return a.toJSONElement();
        } else if (value instanceof Map<?, ?> m) {
            return JSONObject.of(m);
        } else if (value instanceof Iterable<?> i) {
            return JSONArray.of(i);
        } else if (value instanceof Number n) {
            return JSONNumber.of(n);
        } else if (value instanceof Boolean b) {
            return JSONBoolean.of(b);
        } else if (value instanceof Object[] oa) {
            return JSONArray.of(oa);
        } else if (value.getClass().isArray()) {
            return JSONArray.ofPrimitiveArray(value);
        } else {
            return JSONString.of(value);
        }
    }

    /**
     * Returns the type of the element.
     *
     * @return The type of the element.
     */
    @Override
    public final JSONType getType() {
        return type;
    }

    /**
     * Returns the corresponding raw object of this element.  See the
     * documentation of the subclasses for details.  Note that it's
     * preferred to use accessor methods for application developers
     * to avoid casting.
     *
     * @return The corresponding raw object of this element.
     *
     * @see JSONObject#toRawObject()
     * @see JSONArray#toRawObject()
     * @see JSONString#toRawObject()
     * @see JSONBoolean#toRawObject()
     * @see JSONNumber#toRawObject()
     * @see JSONNull#toRawObject()
     */
    @Override
    public abstract Object toRawObject();

    /**
     * Returns the minimum JSON string of this object.
     *
     * @return The minimum JSON string of this object.
     */
    @Override
    public final String toJSON() {
        return toJSON(false);
    }

    @Override
    public final String toAsciiJSON() {
        return toJSON(true);
    }

    // NOTE to the developer of this library:
    //
    // The default implementation of #toJSON(boolean) and #appendJSON(Appendable, boolean)
    // depend on each other, so it's required to override at least one of these
    // methods, or infinite recursion will occur and StackOverflowError will be
    // thrown.

    // JSONNumber, JSONBoolean and JSONNull override this method.
    // Do not expose this method (with a boolean param) to public.
    String toJSON(boolean ascii) {
        try {
            return appendJSON(new StringBuilder(), ascii).toString();
        } catch (IOException e) {
            // StringBuilder.append never throws IOException.
            throw new AssertionError(e);
        }
    }

    // JSONObject, JSONArray and JSONString override this method.
    // This method is used by JSON#append(Appendable) and #toJSON(boolean)
    Appendable appendJSON(Appendable appendable, boolean ascii) throws IOException {
        return appendable.append(toJSON(ascii));
    }

    /**
     * Returns the indented JSON string of this object.
     *
     * @param indent Number of spaces of each indent.
     * @return The indented JSON string of this object.
     */
    @Override
    public final String toJSON(int indent) {
        if (indent < 0) {
            throw new IllegalArgumentException("indent < 0 (indent = " + indent + ")");
        }
        return toJSON(indent, false);
    }

    @Override
    public final String toAsciiJSON(int indent) {
        if (indent < 0) {
            throw new IllegalArgumentException("indent < 0 (indent = " + indent + ")");
        }
        return toJSON(indent, true);
    }

    String toJSON(int indent, boolean ascii) {
        assert indent >= 0; // supposed to be checked in public APIs
        try {
            return append(new StringBuilder(), indent, 0, ascii).toString();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    static final String LINE_SEPARATOR = System.lineSeparator();

    Appendable append(Appendable appendable, int indent, int prefixBlanks, boolean ascii) throws IOException {
        return appendJSON(appendable, ascii);
    }

    /**
     * Returns true if the object has the same type and elements
     * with this object.
     *
     * @param obj The object to be compared.
     * @return {@code true} if the object has the same type and elements
     * with this object, {@code false} otherwise.
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Returns the hash code of the element.
     *
     * @return The hash code of the element.
     */
    @Override
    public abstract int hashCode();

    /**
     * Returns a string representation of this object.  The returned
     * string contains JSON of this object.  The format of the string
     * is subject to change, so do not try to parse strings returned
     * by this method.  Parse the string returned by {@link #toJSON()}
     * instead.
     *
     * @return A string representation of this object.
     */
    @Override
    public final String toString() {
        return toJSON();
    }

    /**
     * Returns an immutable list of JSON elements if this element is an array.
     *
     * @return An immutable list of JSON elements if this element is an array.
     * @throws JSONTypeMismatchException if this element is not an array.
     */
    @Override
    public List<? extends JSONElement> getList() {
        throw new JSONTypeMismatchException(type, JSONType.ARRAY);
    }

    /**
     * If this element is an array, returns the element at the specified position
     * in this JSON array.
     *
     * @param index The index to access.
     * @return The element on the index.
     * @throws JSONTypeMismatchException if this element is not an array.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    @Override
    public JSONElement get(int index) {
        throw new JSONTypeMismatchException(type, JSONType.ARRAY);
    }

    /**
     * Returns an immutable map of JSON keys and values if this element
     * is an object.
     *
     * @return A map of JSON keys and values if this node is an object.
     * @throws JSONTypeMismatchException if this node is not an object.
     */
    @Override
    public Map<String, ? extends JSONElement> getMap() {
        throw new JSONTypeMismatchException(type, JSONType.OBJECT);
    }

    /**
     * If this element is an object, returns the value to which the specified key
     * is mapped, or throws a {@code NoSuchElementException} if no such mapping
     * exists.
     *
     * @apiNote If you are not sure that the mapping exists, use {@link #getOptional(String)}
     * instead.
     *
     * @param key The key whose associated value is to be returned.
     * @return The corresponding element.
     * @throws JSONTypeMismatchException if this node is not an object.
     * @throws NoSuchElementException if no such mapping exists.
     * @throws NullPointerException if {@code key} is null.
     */
    @Override
    public JSONElement get(String key) {
        throw new JSONTypeMismatchException(type, JSONType.OBJECT);
    }

    /**
     * If this element is an object, returns an optional containing the value
     * to which the specified key is mapped, or returns an empty optional
     * if no such mapping exists.
     *
     * @param key The key whose associated value is to be returned.
     * @return An optional containing the corresponding value if the mapping exists,
     * or an empty optional if no such mapping exists.
     * @throws JSONTypeMismatchException if this node is not an object.
     * @throws NullPointerException if {@code key} is null.
     */
    @Override
    public Optional<JSONElement> getOptional(String key) {
        throw new JSONTypeMismatchException(type, JSONType.OBJECT);
    }

    /**
     * Returns the size of the element if this element is an object or an array.
     *
     * @return The size of this element if this node is an object or an array.
     * @throws JSONTypeMismatchException if this element is not an object or an array.
     */
    @Override
    public int size() {
        throw new JSONTypeMismatchException(type, JSONType.OBJECT, JSONType.ARRAY);
    }

    /**
     * Returns the raw string if this element is a string.
     *
     * @return The raw string if this element is a string.
     * @throws JSONTypeMismatchException if this element is not a string.
     */
    @Override
    public String getString() {
        throw new JSONTypeMismatchException(type, JSONType.STRING);
    }

    /**
     * Returns the raw decimal number if this element is a number.
     *
     * @return the raw decimal number if this element is a number.
     * @throws JSONTypeMismatchException if this element is not a number.
     */
    @Override
    public BigDecimal getDecimal() {
        throw new JSONTypeMismatchException(type, JSONType.NUMBER);
    }

    /**
     * Returns the raw {@code int} if this element is a number.
     *
     * @return the raw {@code int} if this element is a number.
     * @throws JSONTypeMismatchException if this element is not a number.
     */
    @Override
    public int getInt() {
        throw new JSONTypeMismatchException(type, JSONType.NUMBER);
    }

    /**
     * Returns the raw {@code long} if this element is a number.
     *
     * @return the raw {@code long} if this element is a number.
     * @throws JSONTypeMismatchException if this element is not a number.
     */
    @Override
    public long getLong() {
        throw new JSONTypeMismatchException(type, JSONType.NUMBER);
    }

    /**
     * Returns the raw {@code double} if this element is a number.
     *
     * @return the raw {@code double} if this element is a number.
     * @throws JSONTypeMismatchException if this element is not a number.
     */
    @Override
    public double getDouble() {
        throw new JSONTypeMismatchException(type, JSONType.NUMBER);
    }

    /**
     * Returns the raw {@code boolean} if this element is a boolean.
     *
     * @return the raw {@code boolean} if this element is a boolean.
     * @throws JSONTypeMismatchException if this element is not a boolean.
     */
    @Override
    public boolean getBoolean() {
        throw new JSONTypeMismatchException(type, JSONType.BOOLEAN);
    }

    /**
     * Returns {@code true} if this element is a null.
     *
     * @return {@code true} if this node is a null, {@code false} otherwise.
     */
    @Override
    public boolean isNull() {
        return false;
    }

    /**
     * This method simply returns this object.
     *
     * @return {@code this}.
     */
    @Override
    public final JSONElement toJSONElement() {
        return this;
    }
}
