package com.yuantj.json;

import java.io.IOException;
import java.util.*;

/**
 * This class represents a JSON array.
 *
 * @author yuantj
 * @version 1.0
 */
public final class JSONArray extends JSONElement {
    final JSONElement[] values;

    private JSONArray(JSONElement[] values) {
        super(JSONType.ARRAY);
        assert values != null;
        this.values = values;
    }

    /**
     * Returns a JSON array containing deep copies of the values.  Each elements
     * are converted with {@link JSONElement#of(Object)} method.
     *
     * @param values An array containing various elements.
     * @return A JSON array containing deep copies of the values.
     * @throws IllegalArgumentException If the type of element of
     * the array is not supported.
     * @throws NullPointerException If {@code values} is {@code null}.
     * @throws NumberFormatException If one of the elements is a floating-point
     * number which is Infinity or NaN.
     *
     * @see JSONElement#of(Object)
     */
    public static JSONArray of(Object[] values) {
        // Do not treat JSONElement[] specially, because elements may be null.
        int length = values.length;
        var elements = new JSONElement[length];
        for (int i = 0; i < length; ++i) {
            elements[i] = JSONElement.of(values[i]);
        }
        return JSONArray.ofTrustedArray(elements);
    }

    // Elements are not cloned.
    static JSONArray ofTrustedArray(JSONElement[] values) {
        return new JSONArray(values);
    }

    /**
     * Returns a JSON array of numbers in the specified array.
     *
     * @param values An array containing various numbers.
     * @return A JSON array of numbers in the specified array.
     * @throws NullPointerException If {@code values} is {@code null}.
     */
    public static JSONArray of(byte[] values) {
        int length = values.length;
        var elements = new JSONElement[length];
        for (int i = 0; i < length; ++i) {
            elements[i] = JSONNumber.of(values[i]);
        }
        return JSONArray.ofTrustedArray(elements);
    }

    /**
     * Returns a JSON array of numbers in the specified array.
     *
     * @param values An array containing various numbers.
     * @return A JSON array of numbers in the specified array.
     * @throws NullPointerException If {@code values} is {@code null}.
     */
    public static JSONArray of(short[] values) {
        int length = values.length;
        var elements = new JSONElement[length];
        for (int i = 0; i < length; ++i) {
            elements[i] = JSONNumber.of(values[i]);
        }
        return JSONArray.ofTrustedArray(elements);
    }

    /**
     * Returns a JSON array of numbers in the specified array.
     *
     * @param values An array containing various numbers.
     * @return A JSON array of numbers in the specified array.
     * @throws NullPointerException If {@code values} is {@code null}.
     */
    public static JSONArray of(int[] values) {
        int length = values.length;
        var elements = new JSONElement[length];
        for (int i = 0; i < length; ++i) {
            elements[i] = JSONNumber.of(values[i]);
        }
        return JSONArray.ofTrustedArray(elements);
    }

    /**
     * Returns a JSON array of numbers in the specified array.
     *
     * @param values An array containing various numbers.
     * @return A JSON array of numbers in the specified array.
     * @throws NullPointerException If {@code values} is {@code null}.
     */
    public static JSONArray of(long[] values) {
        int length = values.length;
        var elements = new JSONElement[length];
        for (int i = 0; i < length; ++i) {
            elements[i] = JSONNumber.of(values[i]);
        }
        return JSONArray.ofTrustedArray(elements);
    }

    /**
     * Returns a JSON array of numbers in the specified array.
     *
     * @param values An array containing various numbers.
     * @return A JSON array of numbers in the specified array.
     * @throws NullPointerException If {@code values} is {@code null}.
     * @throws NumberFormatException if one of the elements is {@code Infinity} or {@code NaN}.
     */
    public static JSONArray of(float[] values) {
        int length = values.length;
        var elements = new JSONElement[length];
        for (int i = 0; i < length; ++i) {
            elements[i] = JSONNumber.of(values[i]);
        }
        return JSONArray.ofTrustedArray(elements);
    }

    /**
     * Returns a JSON array of numbers in the specified array.
     *
     * @param values An array containing various numbers.
     * @return A JSON array of numbers in the specified array.
     * @throws NullPointerException If {@code values} is {@code null}.
     * @throws NumberFormatException if one of the elements is {@code Infinity} or {@code NaN}.
     */
    public static JSONArray of(double[] values) {
        int length = values.length;
        var elements = new JSONElement[length];
        for (int i = 0; i < length; ++i) {
            elements[i] = JSONNumber.of(values[i]);
        }
        return JSONArray.ofTrustedArray(elements);
    }

    /**
     * Returns a JSON array of booleans in the specified array.
     *
     * @param values An array containing various booleans
     * @return A JSON array of booleans in the specified array.
     * @throws NullPointerException If {@code values} is {@code null}.
     */
    public static JSONArray of(boolean[] values) {
        int length = values.length;
        var elements = new JSONElement[length];
        for (int i = 0; i < length; ++i) {
            elements[i] = JSONBoolean.of(values[i]);
        }
        return JSONArray.ofTrustedArray(elements);
    }

    /**
     * Returns a JSON array of single-character strings in the specified array.
     *
     * @param values An array containing various numbers.
     * @return A JSON array of single-character strings in the specified array.
     * @throws NullPointerException If {@code values} is {@code null}.
     */
    public static JSONArray of(char[] values) {
        int length = values.length;
        var elements = new JSONElement[length];
        for (int i = 0; i < length; ++i) {
            elements[i] = JSONString.of(values[i]);
        }
        return JSONArray.ofTrustedArray(elements);
    }

    static JSONArray ofPrimitiveArray(Object values) {
        assert values.getClass().isArray();
        if (values instanceof int[]) {
            return JSONArray.of((int[])values);
        } else if (values instanceof long[]) {
            return JSONArray.of((long[])values);
        } else if (values instanceof double[]) {
            return JSONArray.of((double[])values);
        } else if (values instanceof boolean[]) {
            return JSONArray.of((boolean[])values);
        } else if (values instanceof float[]) {
            return JSONArray.of((float[])values);
        } else if (values instanceof char[]) {
            return JSONArray.of((char[])values);
        } else if (values instanceof byte[]) {
            return JSONArray.of((byte[])values);
        } else if (values instanceof short[]) {
            return JSONArray.of((short[])values);
        } else {
            // Should not happen, throw an error to report a bug
            throw new AssertionError(values.getClass() + " is not a primitive array type");
        }
    }

    /**
     * Returns a JSON array containing deep copies of the values.  Each element
     * are converted with {@link JSONElement#of(Object)} method.
     *
     * @param values An iterable containing various elements.
     * @return A JSON array containing deep copies of the values.
     * @throws IllegalArgumentException If the type of one of the elements of
     * the array is not supported.
     * @throws NullPointerException If {@code values} is {@code null}.
     *
     * @see JSONElement#of(Object)
     */
    public static JSONArray of(Iterable<?> values) {
        var elements = new ArrayList<JSONElement>(guessCapacity(values));
        for (Object o : values) {
            elements.add(JSONElement.of(o));
        }
        return JSONArray.ofTrustedArray(elements.toArray(new JSONElement[0]));
    }

    private static int guessCapacity(Iterable<?> values) {
        if (values instanceof Collection<?> c) {
            return c.size();
        }
        long s = values.spliterator().getExactSizeIfKnown();
        if (s < 0L || s > Integer.MAX_VALUE - 32) {
            return 0;
        } else {
            return (int)s;
        }
    }

    /**
     * Returns an immutable list containing raw objects of
     * the elements in this array.
     *
     * @return An immutable list containing raw objects of
     * the elements in this array.
     */
    @Override
    public List<?> toRawObject() {
        var list = new ArrayList<>(values.length);
        for (JSONAccessor e : values) {
            list.add(e.toRawObject());
        }
        return Collections.unmodifiableList(list);
    }

    static List<?> toRawObject(List<? extends JSONAccessor> values) {
        var list = new ArrayList<>(values.size());
        for (JSONAccessor e : values) {
            list.add(e.toRawObject());
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    Appendable appendJSON(Appendable appendable, boolean ascii) throws IOException {
        int length = values.length;
        if (length == 0) {
            return appendable.append("[]");
        }
        appendable.append('[');
        int i = 0;
        while (true) {
            values[i++].appendJSON(appendable, ascii);
            if (i == length) {
                return appendable.append(']');
            }
            appendable.append(',');
        }
    }

    static String toJSON(List<? extends JSONAccessor> list, boolean ascii) {
        var sj = new StringJoiner(",", "[", "]");
        for (JSONAccessor a : list) {
            sj.add(ascii ? a.toAsciiJSON() : a.toJSON());
        }
        return sj.toString();
    }

    @Override
    Appendable append(Appendable appendable, int indent, int prefixBlanks, boolean ascii) throws IOException {
        int length = values.length;
        if (length == 0) {
            return appendable.append("[]");
        }
        String pre = " ".repeat(prefixBlanks);
        String ind = " ".repeat(indent);
        appendable.append("[").append(LINE_SEPARATOR).append(pre).append(ind);
        int newPrefixBlanks = prefixBlanks + indent;
        int i = 0;
        while (true) {
            values[i++].append(appendable, indent, newPrefixBlanks, ascii);
            if (i == length) {
                return appendable.append(LINE_SEPARATOR).append(pre).append("]");
            }
            appendable.append(",").append(LINE_SEPARATOR).append(pre).append(ind);
        }
    }

    /**
     * Returns the element at the specified position in this JSON array.
     *
     * @param index The index to access.
     * @return The element on the index.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    @Override
    public JSONElement get(int index) {
        return values[index];
    }

    /**
     * Returns an immutable list of JSON elements.
     *
     * @return An immutable list of JSON elements.
     */
    @Override
    public List<? extends JSONElement> getList() {
        return List.of(values);
    }

    /**
     * Returns the size of the array.
     *
     * @return The size of the array.
     */
    @Override
    public int size() {
        return values.length;
    }

    /**
     * Returns {@code true} if the specified object is also a JSON
     * array and contains the same elements.
     *
     * @param obj The object to be compared.
     * @return {@code true} if the specified object is equal to this.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof JSONArray a && Arrays.equals(values, a.values);
    }

    /**
     * Returns a hash code for the object.  Compatible with
     * {@link List#hashCode()}.
     *
     * @return A hash code for the object.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
