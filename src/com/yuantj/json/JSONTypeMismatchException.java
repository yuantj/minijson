package com.yuantj.json;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Thrown when trying to invoke a method that does not match the
 * type of the node.
 *
 * @author yuantj
 * @version 1.0
 */
public class JSONTypeMismatchException extends IllegalStateException {
    private final List<JSONType> expected;
    private final JSONType found;

    /**
     * Constructs a {@code JSONTypeMismatchException} with the given expected
     * and found type.
     *
     * @param found The found type.
     * @param expected The expected types.
     */
    public JSONTypeMismatchException(JSONType found, JSONType... expected) {
        super(generateMessage(found, Arrays.asList(expected)));
        this.expected = List.of(expected);
        this.found = found;
    }

    /**
     * Constructs a {@code JSONTypeMismatchException} with the given expected
     * and found types.
     *
     * @param found The found type.
     * @param expected The expected types.
     */
    public JSONTypeMismatchException(JSONType found, Iterable<? extends JSONType> expected) {
        super(generateMessage(found, expected));
        this.expected = StreamSupport.stream(expected.spliterator(), false)
                .collect(Collectors.toUnmodifiableList());
        this.found = found;
    }

    private static String generateMessage(JSONType found, Iterable<? extends JSONType> expected) {
        var sb = new StringBuilder();
        var sj = new StringJoiner(", ");
        for (JSONType t : expected) {
            sj.add(t.toString());
        }
        return sb.append(sj).append(" expected, ")
                .append(found).append(" found").toString();
    }

    /**
     * Returns an immutable list containing the expected types.
     *
     * @return An immutable list containing the expected types.
     */
    public List<JSONType> getExpected() {
        return expected;
    }

    /**
     * Returns the found type.
     *
     * @return The found type.
     */
    public JSONType getFound() {
        return found;
    }
}
