package com.yuantj.json;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * An util class containing several methods dealing with JSON
 * elements, such as stringify and parse.
 *
 * @author yuantj
 * @version 1.0
 */
public final class JSON {
    private JSON() {}

    public static String stringify(Object value) {
        return JSONElement.of(value).toJSON();
    }

    public static String stringify(Object value, int indent) {
        return JSONElement.of(value).toJSON(indent);
    }

    public static JSONElement parse(String json) {
        return new StringJSONParser(json).parse();
    }

    public static Object parseToRaw(String json) {
        return parse(json).toRawObject();
    }

    public static JSONElement load(InputStream in) throws IOException {
        return load(new InputStreamReader(in));
    }

    public static JSONElement load(Reader in) throws IOException {
        try {
            return new ReaderJSONParser(in).parse();
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    public static Appendable append(JSONAccessor element, Appendable appendable) throws IOException {
        return element.toJSONElement().appendJSON(appendable, false);
    }

    public static void dump(JSONAccessor element, Writer writer) throws IOException {
        append(element, writer);
    }

    public static void dump(JSONAccessor element, OutputStream out) throws IOException {
        var osw = new OutputStreamWriter(out);
        dump(element, osw);
        osw.flush();
    }

    public static Appendable append(JSONAccessor element, Appendable appendable, int indent) throws IOException {
        return element.toJSONElement().append(appendable, indent, 0, false);
    }

    public static void dump(JSONAccessor element, Writer writer, int indent) throws IOException {
        append(element, writer, indent);
    }

    public static void dump(JSONAccessor element, OutputStream out, int indent) throws IOException {
        var osw = new OutputStreamWriter(out);
        dump(element, osw, indent);
        osw.flush();
    }

    static final Map<Class<?>, Function<?, ? extends JSONElement>> typeConverters
            = new LinkedHashMap<>();

    public static <T> void registerConverter(Class<T> type, Function<? super T, ? extends JSONElement> converter) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(converter);
        typeConverters.put(type, converter);
    }

    static {
        registerConverter(JSONAccessor.class, JSONAccessor::toJSONElement);
        registerConverter(Map.class, JSONObject::of);
        registerConverter(Iterable.class, JSONArray::of);
        registerConverter(CharSequence.class, JSONString::of);
        registerConverter(Number.class, JSONNumber::of);
        registerConverter(Boolean.class, JSONBoolean::of);
        registerConverter(Object[].class, JSONArray::of);
        registerConverter(int[].class, JSONArray::of);
        registerConverter(long[].class, JSONArray::of);
        registerConverter(double[].class, JSONArray::of);
        registerConverter(float[].class, JSONArray::of);
        registerConverter(boolean[].class, JSONArray::of);
        registerConverter(char[].class, JSONArray::of);
        registerConverter(short[].class, JSONArray::of);
        registerConverter(byte[].class, JSONArray::of);
    }
}
