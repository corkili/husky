package com.corkili.husky.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.corkili.husky.util.CheckUtils;

public final class Query {

    private final Map<String, Object> params;

    public Query() {
        params = new HashMap<>();
    }

    public Query(Query query) {
        this.params = new HashMap<>();
        if (CheckUtils.isNotNull(query)) {
            this.params.putAll(query.params);
        }
    }

    public boolean add(String name, Object param) {
        if (CheckUtils.hasNull(name, param)) {
            return false;
        }
        params.put(name, param);
        return true;
    }

    public boolean add(Pair<?, ?> nameAndParam) {
        if (CheckUtils.isNull(nameAndParam) ||
                CheckUtils.isNull(nameAndParam.getKey()) ||
                CheckUtils.isNull(nameAndParam.getValue())) {
            return false;
        }
        params.put(nameAndParam.getKey().toString(), nameAndParam.getValue());
        return true;
    }

    public int add(Pair<?, ?>... nameAndParams) {
        if (CheckUtils.isEmpty(nameAndParams)) {
            return 0;
        }
        int count = 0;
        for (Pair<?, ?> nameAndParam : nameAndParams) {
            if (add(nameAndParam)) {
                count++;
            }
        }
        return count;
    }

    public <T> T get(String name, Class<T> clazz) {
        if (CheckUtils.hasNull(name, clazz)) {
            return null;
        }
        Object param = params.get(name);
        if (clazz.isInstance(param)) {
            return clazz.cast(param);
        } else {
            return null;
        }
    }

    public Object get(String name) {
        return params.get(name);
    }

    public Byte getByte(String name) {
        return get(name, Byte.class);
    }

    public Short getShort(String name) {
        return get(name, Short.class);
    }

    public Integer getInt(String name) {
        return get(name, Integer.class);
    }

    public Long getLong(String name) {
        return get(name, Long.class);
    }

    public Float getFloat(String name) {
        return get(name, Float.class);
    }

    public Double getDouble(String name) {
        return get(name, Double.class);
    }

    public Boolean getBoolean(String name) {
        return get(name, Boolean.class);
    }

    public Character getChar(String name) {
        return get(name, Character.class);
    }

    public String getString(String name) {
        return get(name, String.class);
    }

    public Class<?> getClass(String name) {
        return get(name, Class.class);
    }

    public byte getByte(String name, byte defaultValue) {
        Byte param = get(name, Byte.class);
        return param == null ? defaultValue : param;
    }

    public short getShort(String name, short defaultValue) {
        Short param = get(name, Short.class);
        return param == null ? defaultValue : param;
    }

    public int getInt(String name, int defaultValue) {
        Integer param = get(name, Integer.class);
        return param == null ? defaultValue : param;
    }

    public long getLong(String name, long defaultValue) {
        Long param = get(name, Long.class);
        return param == null ? defaultValue : param;
    }

    public float getFloat(String name, float defaultValue) {
        Float param = get(name, Float.class);
        return param == null ? defaultValue : param;
    }

    public double getDouble(String name, double defaultValue) {
        Double param = get(name, Double.class);
        return param == null ? defaultValue : param;
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        Boolean param = get(name, Boolean.class);
        return param == null ? defaultValue : param;
    }

    public char getChar(String name, char defaultValue) {
        Character param = get(name, Character.class);
        return param == null ? defaultValue : param;
    }

    public String getString(String name, String defaultValue) {
        String param = get(name, String.class);
        return param == null ? defaultValue : param;
    }

    public Object[] getArray(String name) {
        return get(name, Object[].class);
    }

    public Byte[] getByteArray(String name) {
        return get(name, Byte[].class);
    }

    public Short[] getShortArray(String name) {
        return get(name, Short[].class);
    }

    public Integer[] getIntArray(String name) {
        return get(name, Integer[].class);
    }

    public Long[] getLongArray(String name) {
        return get(name, Long[].class);
    }

    public Float[] getFloatArray(String name) {
        return get(name, Float[].class);
    }

    public Double[] getDoubleArray(String name) {
        return get(name, Double[].class);
    }

    public Boolean[] getBooleanArray(String name) {
        return get(name, Boolean[].class);
    }

    public Character[] getCharArray(String name) {
        return get(name, Character[].class);
    }

    public String[] getStringArray(String name) {
        return get(name, String[].class);
    }

    public Class<?>[] getClassArray(String name) {
        return get(name, Class[].class);
    }

    @Override
    public String toString() {
        return "Query{" +
                "params=" + params +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return Objects.equals(params, query.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params);
    }
}
