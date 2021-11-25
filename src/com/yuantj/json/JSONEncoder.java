package com.yuantj.json;

import java.util.Objects;

public abstract class JSONEncoder<O, A, S, NM, B, N> {
    protected JSONEncoder() {
    }

    protected abstract JSONType declareType(Object value);

    protected abstract JSONObject toJSONObject(Object value);

    protected abstract JSONArray toJSONArray(Object value);

    protected abstract JSONString toJSONString(Object value);

    protected abstract JSONNumber toJSONNumber(Object value);

    protected abstract JSONBoolean toJSONBoolean(Object value);

    protected JSONNull toJSONNull(Object value) {
        return JSONNull.NULL;
    }

    public final JSONElement encode(Object value) {
        JSONType type = Objects.requireNonNull(declareType(value));
        switch (type) {
            case OBJECT:  return toJSONObject(value);
            case ARRAY:   return toJSONArray(value);
            case STRING:  return toJSONString(value);
            case NUMBER:  return toJSONNumber(value);
            case BOOLEAN: return toJSONBoolean(value);
            case NULL:    return toJSONNull(value);
            default:      throw new AssertionError(type);
        }
    }
}
