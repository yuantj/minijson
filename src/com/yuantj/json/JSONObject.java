package com.yuantj.json;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

import static com.yuantj.json.JSONString.rawStringToJSON;

/**
 * This class represents a JSON object.
 *
 * @author yuantj
 * @version 1.0
 */
public final class JSONObject extends JSONElement {
    final Map<String, JSONElement> values;

    private JSONObject(Map<String, JSONElement> values) {
        super(JSONType.OBJECT);
        assert values != null;
        this.values = values;
    }

    /**
     * Returns a JSON object containing keys and values given by the
     * specified map.  Keys are converted by {@link String#valueOf(Object)},
     * and values are converted by {@link JSONElement#of(Object)}.  Note that
     * if there are duplicate keys, the latter (according to the order of
     * {@code values.entrySet().iterator()} will be used.
     *
     * @param values A map.
     * @return A JSON object containing keys and values given by the
     * specified map.
     * @throws IllegalArgumentException If the type of one of the values of
     * the map is not supported.
     * @throws NullPointerException If {@code values} is {@code null}.
     */
    public static JSONObject of(Map<?, ?> values) {
        var m = new LinkedHashMap<String, JSONElement>();
        values.forEach((k, v) -> m.put(String.valueOf(k), JSONElement.of(v)));
        return new JSONObject(Collections.unmodifiableMap(m));
    }

    static JSONObject ofTrustedMap(Map<String, JSONElement> values) {
        return new JSONObject(values);
    }

    /**
     * Returns an immutable map containing all raw keys and values.
     *
     * @return An immutable map containing all raw keys and values.
     */
    @Override
    public Map<String, ?> toRawObject() {
        return toRawObject(values);
    }

    static Map<String, ?> toRawObject(Map<String, ? extends JSONAccessor> values) {
        var m = new LinkedHashMap<String, Object>();
        values.forEach((k, v) -> m.put(k, v.toRawObject()));
        return Collections.unmodifiableMap(m);
    }

    @Override
    Appendable appendJSON(Appendable appendable, boolean ascii) throws IOException {
        var i = values.entrySet().iterator();
        if (!i.hasNext()) {
            return appendable.append("{}");
        }
        appendable.append('{');
        while (true) {
            var e = i.next();
            rawStringToJSON(e.getKey(), appendable, ascii);
            appendable.append(':');
            e.getValue().appendJSON(appendable, ascii);
            if (!i.hasNext()) {
                return appendable.append('}');
            }
            appendable.append(',');
        }
    }

    static String toJSON(Map<String, ? extends JSONAccessor> map, boolean ascii) {
        var sj = new StringJoiner(",", "{", "}");
        map.forEach((k, v) -> sj.add(rawStringToJSON(k, ascii) + ":" +
                (ascii ? v.toAsciiJSON() : v.toJSON())));
        return sj.toString();
    }

    @Override
    Appendable append(Appendable appendable, int indent, int prefixBlanks, boolean ascii) throws IOException {
        var i = values.entrySet().iterator();
        if (!i.hasNext()) {
            return appendable.append("{}");
        }
        String pre = " ".repeat(prefixBlanks);
        String ind = " ".repeat(indent);
        appendable.append("{").append(LINE_SEPARATOR).append(pre);
        int newPrefixBlanks = prefixBlanks + indent;
        while (true) {
            var e = i.next();
            appendable.append(ind);
            rawStringToJSON(e.getKey(), appendable, ascii);
            appendable.append(": ");
            e.getValue().append(appendable, indent, newPrefixBlanks, ascii);
            if (!i.hasNext()) {
                return appendable.append(LINE_SEPARATOR).append(pre).append("}");
            }
            appendable.append(",").append(LINE_SEPARATOR).append(pre);
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or throws a
     * {@code NoSuchElementException} if no such mapping exists.
     *
     * @apiNote If you are not sure that the mapping exists, use {@link #getOptional(String)}
     * instead.
     *
     * @param key The key whose associated value is to be returned.
     * @return The corresponding element.
     * @throws NoSuchElementException if no such mapping exists.
     * @throws NullPointerException if {@code key} is null.
     */
    @Override
    public JSONElement get(String key) {
        return getOptional(key).orElseThrow(noElementEx(key));
    }

    static Supplier<NoSuchElementException> noElementEx(String key) {
        return () -> new NoSuchElementException(
                "key " + rawStringToJSON(key, true) + " not present");
    }

    /**
     * Returns an optional containing the value to which the specified key is mapped,
     * or returns an empty optional if no such mapping exists.
     *
     * @param key The key whose associated value is to be returned.
     * @return An optional containing the corresponding value if the mapping exists,
     * or an empty optional if no such mapping exists.
     * @throws NullPointerException if {@code key} is null.
     */
    @Override
    public Optional<JSONElement> getOptional(String key) {
        return Optional.ofNullable(values.get(Objects.requireNonNull(key)));
    }

    /**
     * Returns an immutable map of JSON keys and values.
     *
     * @return A map of JSON keys and values if this node is an object.
     */
    @Override
    public Map<String, ? extends JSONElement> getMap() {
        return values;
    }

    /**
     * Returns the size of the object.
     *
     * @return The size of this object.
     */
    @Override
    public int size() {
        return values.size();
    }

    /**
     * Returns {@code true} if the specified object is also an instance
     * of {@code JSONObject} with same mapping with this object.
     *
     * @param obj The object to be compared.
     * @return {@code true} if the specified object is equal to this.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof JSONObject o && o.values.equals(values);
    }

    /**
     * Returns a hash code for the object, calculated by
     * {@link Map#hashCode()}.
     *
     * @return A hash code for the object.
     */
    @Override
    public int hashCode() {
        return values.hashCode();
    }
}
