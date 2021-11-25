package com.yuantj.json;

import java.math.BigDecimal;
import java.util.*;

public final class JSONElementBuilder implements JSONAccessor {
    private final JSONType type;
    private final Object element;

    private JSONElementBuilder(JSONType type, Object element) {
        this.type = type;
        this.element = element;
    }

    public static JSONElementBuilder of(Object source) {
        if (source instanceof JSONAccessor) {
            return copyOf((JSONAccessor)source);
        } else {
            return copyOf(JSONElement.of(source));
        }
    }

    public static JSONElementBuilder copyOf(JSONAccessor source) {
        JSONElement element = source.toJSONElement();
        JSONType type = element.type;
        switch (type) {
            case OBJECT: return copyOfObject((JSONObject)source);
            case ARRAY:  return copyOfArray((JSONArray)source);
            default:     return new JSONElementBuilder(type, element.toRawObject());
        }
    }

    private static JSONElementBuilder copyOfObject(JSONObject source) {
        var map = new LinkedHashMap<String, JSONElementBuilder>();
        source.values.forEach((k, v) -> map.put(k, copyOf(v)));
        return new JSONElementBuilder(JSONType.OBJECT, map);
    }

    private static JSONElementBuilder copyOfArray(JSONArray source) {
        var list = new ArrayList<JSONElementBuilder>(source.values.length);
        for (JSONElement e : source.values) {
            list.add(copyOf(e));
        }
        return new JSONElementBuilder(JSONType.ARRAY, list);
    }

    public static JSONElementBuilder emptyObject() {
        return new JSONElementBuilder(JSONType.OBJECT, new LinkedHashMap<>());
    }

    public static JSONElementBuilder newObjectFrom(Map<?, ?> source) {
        return copyOfObject(JSONObject.of(source));
    }

    public static JSONElementBuilder emptyArray() {
        return new JSONElementBuilder(JSONType.ARRAY, new ArrayList<>());
    }

    public static JSONElementBuilder newArrayFrom(Iterable<?> source) {
        return copyOfArray(JSONArray.of(source));
    }

    public static JSONElementBuilder newArrayFrom(Object[] source) {
        return copyOfArray(JSONArray.of(source));
    }

    public static JSONElementBuilder newArrayFrom(byte[] source) {
        return copyOfArray(JSONArray.of(source));
    }

    public static JSONElementBuilder newArrayFrom(short[] source) {
        return copyOfArray(JSONArray.of(source));
    }

    public static JSONElementBuilder newArrayFrom(int[] source) {
        return copyOfArray(JSONArray.of(source));
    }

    public static JSONElementBuilder newArrayFrom(long[] source) {
        return copyOfArray(JSONArray.of(source));
    }

    public static JSONElementBuilder newArrayFrom(float[] source) {
        return copyOfArray(JSONArray.of(source));
    }

    public static JSONElementBuilder newArrayFrom(double[] source) {
        return copyOfArray(JSONArray.of(source));
    }

    public static JSONElementBuilder newArrayFrom(boolean[] source) {
        return copyOfArray(JSONArray.of(source));
    }

    public static JSONElementBuilder newArrayFrom(char[] source) {
        return copyOfArray(JSONArray.of(source));
    }

    public static JSONElementBuilder newStringFrom(CharSequence source) {
        return new JSONElementBuilder(JSONType.STRING, source.toString());
    }

    public static JSONElementBuilder newNumberFrom(Number source) {
        return new JSONElementBuilder(JSONType.NUMBER, Objects.requireNonNull(source));
    }

    public static JSONElementBuilder newNumberFrom(long source) {
        return new JSONElementBuilder(JSONType.NUMBER, source);
    }

    public static JSONElementBuilder newNumberFrom(double source) {
        return new JSONElementBuilder(JSONType.NUMBER, source);
    }

    public static JSONElementBuilder newBooleanFrom(boolean source) {
        return new JSONElementBuilder(JSONType.BOOLEAN, source);
    }

    public static JSONElementBuilder newNull() {
        return new JSONElementBuilder(JSONType.NULL, null);
    }

    @Override
    public JSONType getType() {
        return type;
    }

    @Override
    public Map<String, JSONElementBuilder> getMap() {
        if (type != JSONType.OBJECT) {
            throw new JSONTypeMismatchException(type, JSONType.OBJECT);
        }
        @SuppressWarnings("unchecked")
        var m = (Map<String, JSONElementBuilder>)element;
        return m;
    }

    @Override
    public Optional<JSONElementBuilder> getOptional(String key) {
        return Optional.ofNullable(getMap().get(Objects.requireNonNull(key)));
    }

    @Override
    public JSONAccessor get(String key) {
        return getOptional(key).orElseThrow(JSONObject.noElementEx(key));
    }

    public Optional<JSONElementBuilder> put(String key, Object value) {
        return Optional.ofNullable(getMap().put(Objects.requireNonNull(key), of(value)));
    }

    public JSONElementBuilder putNewObject(String key) {
        JSONElementBuilder jeb = emptyObject();
        getMap().put(Objects.requireNonNull(key), jeb);
        return jeb;
    }

    public JSONElementBuilder putNewArray(String key) {
        JSONElementBuilder jeb = emptyArray();
        getMap().put(Objects.requireNonNull(key), jeb);
        return jeb;
    }

    @Override
    public List<JSONElementBuilder> getList() {
        if (type != JSONType.ARRAY) {
            throw new JSONTypeMismatchException(type, JSONType.ARRAY);
        }
        @SuppressWarnings("unchecked")
        var ls = (List<JSONElementBuilder>)element;
        return ls;
    }

    @Override
    public JSONElementBuilder get(int index) {
        return getList().get(index);
    }

    public JSONElementBuilder set(int index, Object value) {
        return getList().set(index, of(value));
    }

    public JSONElementBuilder add(Object value) {
        JSONElementBuilder jeb = of(value);
        getList().add(jeb);
        return jeb;
    }

    public JSONElementBuilder addNewObject() {
        JSONElementBuilder jeb = emptyObject();
        getList().add(jeb);
        return jeb;
    }

    public JSONElementBuilder addNewArray() {
        JSONElementBuilder jeb = emptyArray();
        getList().add(jeb);
        return jeb;
    }

    @Override
    public String getString() {
        if (type != JSONType.STRING) {
            throw new JSONTypeMismatchException(type, JSONType.STRING);
        }
        return (String)element;
    }

    private Number getNumber() {
        if (type != JSONType.NUMBER) {
            throw new JSONTypeMismatchException(type, JSONType.NUMBER);
        }
        return (Number)element;
    }

    @Override
    public BigDecimal getDecimal() {
        return JSONNumber.of(getNumber()).value;
    }

    @Override
    public int getInt() {
        return getNumber().intValue();
    }

    @Override
    public long getLong() {
        return getNumber().longValue();
    }

    @Override
    public double getDouble() {
        return getNumber().doubleValue();
    }

    @Override
    public boolean getBoolean() {
        if (type != JSONType.BOOLEAN) {
            throw new JSONTypeMismatchException(type, JSONType.BOOLEAN);
        }
        return (Boolean)element;
    }
}
