package com.yuantj.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * This class represents a JSON number.  This class uses a {@link BigDecimal}
 * to store a number.
 *
 * @author yuantj
 * @version 1.0
 */
public final class JSONNumber extends JSONElement {
    final BigDecimal value;

    private JSONNumber(BigDecimal value) {
        super(JSONType.NUMBER);
        assert value != null;
        this.value = value;
    }

    /**
     * Returns a JSON number representing the specified number.
     *
     * <ul>
     *   <li>If the number is {@link BigDecimal}, then it returns a JSON
     *   number representing the specified decimal.</li>
     *   <li>If the number is {@link Byte}, {@link Short}, {@link Integer},
     *   {@link Long} or {@link BigInteger}, then it returns a
     *   JSON number with a zero scale.</li>
     *   <li>If the number is {@link Float} or {@link Double}, then it
     *   returns a JSON number parsed by {@link BigDecimal#valueOf(double)}.  Note
     *   that it will throw an {@link NumberFormatException} if the number is {@code Infinity}
     *   or {@code NaN}.</li>
     *   <li>If the number has other type, then it tries to parse the string returned
     *   by {@code toString()} method, if it fails, then it tries to parse the number
     *   returned by {@link Number#doubleValue()}.  If it fails again, then an
     *   {@link NumberFormatException} is thrown.</li>
     * </ul>
     *
     * @param value The number.
     * @return The JSON number representing the specified number.
     * @throws NumberFormatException if the number cannot be converted to a {@link BigDecimal}.
     * @throws NullPointerException if the parameter is {@code null}.
     */
    public static JSONNumber of(Number value) {
        Objects.requireNonNull(value);
        if (value instanceof BigDecimal) {
            return new JSONNumber(safeBigDecimal((BigDecimal)value));
        } else if (value instanceof Integer || value instanceof Long ||
                value instanceof Byte || value instanceof Short) {
            return new JSONNumber(BigDecimal.valueOf(value.longValue()));
        } else if (value instanceof BigInteger) {
            return new JSONNumber(new BigDecimal((BigInteger)value));
        } else if (value instanceof Float || value instanceof Double) {
            return new JSONNumber(BigDecimal.valueOf(value.doubleValue()));
        } else {
            try {
                return new JSONNumber(new BigDecimal(value.toString()));
            } catch (NumberFormatException e) {
                return new JSONNumber(BigDecimal.valueOf(value.doubleValue()));
            }
        }
    }

    private static BigDecimal safeBigDecimal(BigDecimal value) {
        if (value.getClass() == BigDecimal.class) {
            return value;
        } else {
            return new BigDecimal(value.toString());
        }
    }

    /**
     * Returns a JSON number with a zero scale representing the specified number.
     *
     * @param value The number
     * @return The JSON number representing the specified number.
     */
    public static JSONNumber of(long value) {
        return new JSONNumber(BigDecimal.valueOf(value));
    }

    /**
     * Returns a JSON number with a zero scale representing the specified number.
     * Note that it will throw an {@link NumberFormatException} if the number is
     * {@code Infinity} or {@code NaN}.
     *
     * @param value The number
     * @return The JSON number representing the specified number.
     * @throws NumberFormatException if the number is {@code Infinity} or {@code NaN}.
     */
    public static JSONNumber of(double value) {
        return new JSONNumber(BigDecimal.valueOf(value));
    }

    /**
     * Returns the raw {@link BigDecimal} represented by this object.
     *
     * @return The raw {@link BigDecimal} represented by this object.
     */
    @Override
    public BigDecimal toRawObject() {
        return value;
    }

    /**
     * Returns the raw {@link BigDecimal} represented by this object.
     *
     * @return The raw {@link BigDecimal} represented by this object.
     */
    @Override
    public BigDecimal getDecimal() {
        return value;
    }

    /**
     * Returns the raw {@code int} represented by this object.  Converted
     * by {@link BigDecimal#intValue()}
     *
     * @return The raw {@code int} represented by this object.
     */
    @Override
    public int getInt() {
        return value.intValue();
    }

    /**
     * Returns the raw {@code long} represented by this object.  Converted
     * by {@link BigDecimal#longValue()}
     *
     * @return The raw {@code long} represented by this object.
     */
    @Override
    public long getLong() {
        return value.longValue();
    }

    /**
     * Returns the raw {@code double} represented by this object.  Converted
     * by {@link BigDecimal#doubleValue()}
     *
     * @return The raw {@code double} represented by this object.
     */
    @Override
    public double getDouble() {
        return value.doubleValue();
    }

    @Override
    String toJSON(boolean ascii) {
        return value.toString();
    }

    @Override
    String toJSON(int indent, boolean ascii) {
        return value.toString();
    }

    /**
     * Returns {@code true} if the specified object is also a JSON number
     * and has the same value with this object, compared by
     * {@link BigDecimal#equals(Object)}.
     *
     * @param obj The object to be compared.
     * @return Returns {@code true} if the specified object is equal to this one.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof JSONNumber n && n.value.equals(value);
    }

    /**
     * Returns the hash code for this object, calculated by {@link BigDecimal#hashCode()}.
     *
     * @return The hash code for this object,
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
