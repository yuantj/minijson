package com.yuantj.json;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Interface used to describe and access a JSON node.  You can implement
 * this interface to describe it.  Unlike {@code JSONElement}, this interface
 * does not guarantee to be immutable, and this interface does not redefine
 * {@code equals} and {@code hashCode}, so it's unsafe to put them in a map
 * or a set.  To get an immutable JSON node, use {@link #toJSONElement()}.
 *
 * @author yuantj
 * @version 1.0
 * @see JSONElement
 */
public interface JSONAccessor {
    /**
     * Returns the JSON type of this node.
     *
     * Note that most default implementation in this interface rely on
     * this abstract method, so it's necessary to ensure that this method
     * returns the correct type, or various problems may occur.
     *
     * @return The JSON type of this node.
     */
    JSONType getType();

    /**
     * Returns a list of JSON nodes if this node is an array.
     *
     * @implSpec This implementation always throws a {@code JSONTypeMismatchException}.
     * This method should be overridden if this node is an array.
     *
     * @return A list of JSON nodes if this node is an array.
     * @throws JSONTypeMismatchException if this node is not an array.
     */
    default List<? extends JSONAccessor> getList() {
        throw new JSONTypeMismatchException(getType(), JSONType.ARRAY);
    }

    /**
     * If this node is an array, returns the element at the specified position
     * in this JSON array.
     *
     * @implSpec This implementation returns {@code getList().get(index)}.
     *
     * @param index The index to access.
     * @return The node on the index.
     * @throws JSONTypeMismatchException if this node is not an array.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    default JSONAccessor get(int index) {
        return getList().get(index);
    }

    /**
     * Returns a map of JSON keys and values if this node is an object.
     *
     * @implSpec This implementation always throws a {@code JSONTypeMismatchException}.
     * This method should be overridden if this node is an object.
     *
     * @return A map of JSON keys and values if this node is an object.
     * @throws JSONTypeMismatchException if this node is not an object.
     */
    default Map<String, ? extends JSONAccessor> getMap() {
        throw new JSONTypeMismatchException(getType(), JSONType.OBJECT);
    }

    /**
     * If this node is an object, returns the value to which the specified key
     * is mapped, or throws a {@code NoSuchElementException} if no such mapping
     * exists.
     *
     * @apiNote If you are not sure that the mapping exists, use {@link #getOptional(String)}
     * instead.
     *
     * @implSpec This implementation first calls {@link #getOptional(String)},
     * then returns the value if the value exists or throws {@code NoSuchElementException}
     * if the value does not exist.
     *
     * @param key The key whose associated value is to be returned.
     * @return The corresponding node.
     * @throws JSONTypeMismatchException if this node is not an object.
     * @throws NoSuchElementException if no such mapping exists.
     * @throws NullPointerException if {@code key} is null.
     */
    default JSONAccessor get(String key) {
        return getOptional(requireNonNull(key)).orElseThrow(JSONObject.noElementEx(key));
    }

    /**
     * If this node is an object, returns an optional containing the value
     * to which the specified key is mapped, or returns an empty optional
     * if no such mapping exists.
     *
     * @implSpec This implementation wraps {@code getMap().get(Objects.requireNonNull(key)}
     * with {@link Optional#ofNullable(Object)}.
     *
     * @param key The key whose associated value is to be returned.
     * @return An optional containing the corresponding value if the mapping exists,
     * or an empty optional if no such mapping exists.
     * @throws JSONTypeMismatchException if this node is not an object.
     * @throws NullPointerException if {@code key} is null.
     */
    default Optional<? extends JSONAccessor> getOptional(String key) {
        return Optional.ofNullable(getMap().get(requireNonNull(key)));
    }

    /**
     * Returns the size of the node if this node is an object or an array.
     *
     * @implSpec This implementation returns {@code getMap().size()} or
     * {@code getList().size()} if the node is an object or an array.
     * If not, throws a {@code JSONTypeMismatchException}.
     *
     * @return The size of this node if this node is an object or an array.
     * @throws JSONTypeMismatchException if this node is not an object or an array.
     */
    default int size() {
        JSONType type = getType();
        return switch (type) {
            case OBJECT -> getMap().size();
            case ARRAY -> getList().size();
            default -> throw new JSONTypeMismatchException(type, JSONType.OBJECT, JSONType.ARRAY);
        };
    }

    /**
     * Returns the raw string if this node is a string.
     *
     * @implSpec This implementation always throws a {@code JSONTypeMismatchException}.
     * This method should be overridden if this node is a string.
     *
     * @return The raw string if this node is a string.
     * @throws JSONTypeMismatchException if this node is not a string.
     */
    default String getString() {
        throw new JSONTypeMismatchException(getType(), JSONType.STRING);
    }

    /**
     * Returns the raw decimal number if this node is a number.
     *
     * @implSpec This implementation always throws a {@code JSONTypeMismatchException}.
     * This method should be overridden if this node is a number.
     *
     * @return the raw decimal number if this node is a number.
     * @throws JSONTypeMismatchException if this node is not a number.
     */
    default BigDecimal getDecimal() {
        throw new JSONTypeMismatchException(getType(), JSONType.NUMBER);
    }

    /**
     * Returns the raw {@code int} if this node is a number.
     *
     * @implSpec This implementation returns {@code getDecimal().intValue()}.
     *
     * @return the raw {@code int} if this node is a number.
     * @throws JSONTypeMismatchException if this node is not a number.
     */
    default int getInt() {
        return getDecimal().intValue();
    }

    /**
     * Returns the raw {@code long} if this node is a number.
     *
     * @implSpec This implementation returns {@code getDecimal().longValue()}.
     *
     * @return the raw {@code long} if this node is a number.
     * @throws JSONTypeMismatchException if this node is not a number.
     */
    default long getLong() {
        return getDecimal().longValue();
    }

    /**
     * Returns the raw {@code double} if this node is a number.
     *
     * @implSpec This implementation returns {@code getDecimal().doubleValue()}.
     *
     * @return the raw {@code double} if this node is a number.
     * @throws JSONTypeMismatchException if this node is not a number.
     */
    default double getDouble() {
        return getDecimal().doubleValue();
    }

    /**
     * Returns the raw {@code boolean} if this node is a boolean.
     *
     * @implSpec This implementation always throws a {@code JSONTypeMismatchException}.
     * This method should be overridden if this node is a boolean.
     *
     * @return the raw {@code boolean} if this node is a boolean.
     * @throws JSONTypeMismatchException if this node is not a boolean.
     */
    default boolean getBoolean() {
        throw new JSONTypeMismatchException(getType(), JSONType.BOOLEAN);
    }

    /**
     * Returns {@code true} if this node is a null.
     *
     * @implSpec This implementations returns {@code getType() == JSONType.NULL}.
     *
     * @return {@code true} if this node is a null, {@code false} otherwise.
     */
    default boolean isNull() {
        return getType() == JSONType.NULL;
    }

    /**
     * Converts this accessor to the corresponding {@link JSONElement} instance.
     *
     * @implSpec This implementation calls the corresponding factory methods and
     * returns the corresponding {@link JSONElement} instance, or {@link JSONNull#NULL}
     * if this node is a null.
     *
     * <table>
     *   <thead>
     *     <tr><td>Element type</td><td>Method used</td></tr>
     *   </thead>
     *   <tbody>
     *     <tr><td>Object</td><td>{@link #getMap()}</td></tr>
     *     <tr><td>Array</td><td>{@link #getList()}</td></tr>
     *     <tr><td>String</td><td>{@link #getString()}</td></tr>
     *     <tr><td>Number</td><td>{@link #getDecimal()}</td></tr>
     *     <tr><td>Boolean</td><td>{@link #getBoolean()}</td></tr>
     *   </tbody>
     * </table>
     *
     * @return The corresponding {@code JSONElement} instance.
     * @see JSONElement#of(Object)
     */
    default JSONElement toJSONElement() {
        JSONType type = getType();
        return switch (type) {
            case OBJECT -> JSONObject.of(getMap());
            case ARRAY -> JSONArray.of(getList());
            case STRING -> JSONString.of(getString());
            case NUMBER -> JSONNumber.of(getDecimal());
            case BOOLEAN -> JSONBoolean.of(getBoolean());
            case NULL -> JSONNull.NULL;
        };
    }

    /**
     * Returns the raw object corresponding to the node.
     *
     * @implSpec This implementation uses the same algorithm
     * as {@code toJSONElement().toRawObject()}.
     *
     * @return The raw object corresponding to the node (nullable).
     * @see JSONElement#toRawObject()
     */
    default Object toRawObject() {
        JSONType type = getType();
        return switch (type) {
            case OBJECT -> JSONObject.toRawObject(getMap());
            case ARRAY -> JSONArray.toRawObject(getList());
            case STRING -> getString();
            case NUMBER -> getDecimal();
            case BOOLEAN -> getBoolean();
            case NULL -> null;
        };
    }

    /**
     * Returns the JSON string corresponding to the node.
     *
     * @return The JSON string corresponding to the node.
     * @see JSONElement#toJSON()
     */
    default String toJSON() {
        return toJSON(false);
    }

    default String toAsciiJSON() {
        return toJSON(true);
    }

    private String toJSON(boolean ascii) {
        JSONType type = getType();
        return switch (type) {
            case OBJECT -> JSONObject.toJSON(getMap(), ascii);
            case ARRAY -> JSONArray.toJSON(getList(), ascii);
            case STRING -> JSONString.rawStringToJSON(getString(), ascii);
            case NUMBER -> getDecimal().toString();
            case BOOLEAN -> getBoolean() ? "true" : "false";
            case NULL -> "null";
        };
    }

    /**
     * Returns the indented JSON string corresponding to the node.
     *
     * @implSpec This implementation returns {@code toJSONElement().toJSON(indent)}.
     *
     * @param indent Number of spaces of each indent.
     * @return The indented JSON string corresponding to the node.
     */
    default String toJSON(int indent) {
        if (indent < 0) {
            throw new IllegalArgumentException("indent < 0 (indent = " + indent + ")");
        }
        return toJSONElement().toJSON(indent, false);
    }

    default String toAsciiJSON(int indent) {
        if (indent < 0) {
            throw new IllegalArgumentException("indent < 0 (indent = " + indent + ")");
        }
        return toJSONElement().toJSON(indent, true);
    }
}
