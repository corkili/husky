package com.corkili.husky.config;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.corkili.husky.util.CheckUtils;


public final class Config {

    private final Map<String, String> configMap;

    Config() {
        configMap = new ConcurrentHashMap<>();
    }

    void updateConfigMap(Map<String, String> configMap) {
        if (CheckUtils.isNotNull(configMap)) {
            this.configMap.clear();
            this.configMap.putAll(configMap);
        }
    }

    public boolean containsConfig(String name) {
        return configMap.containsKey(name);
    }

    public String getString(String name, String defaultValue) {
        return configMap.getOrDefault(name, defaultValue);
    }
    
    public byte getByte(String name, byte defaultValue) {
        try {
            return Byte.parseByte(configMap.getOrDefault(name, String.valueOf(defaultValue)));
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public short getShort(String name, short defaultValue) {
        try {
            return Short.parseShort(configMap.getOrDefault(name, String.valueOf(defaultValue)));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getInteger(String name, int defaultValue) {
        try {
            return Integer.parseInt(configMap.getOrDefault(name, String.valueOf(defaultValue)));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long getLong(String name, long defaultValue) {
        try {
            return Long.parseLong(configMap.getOrDefault(name, String.valueOf(defaultValue)));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float getFloat(String name, float defaultValue) {
        try {
            return Float.parseFloat(configMap.getOrDefault(name, String.valueOf(defaultValue)));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double getDouble(String name, double defaultValue) {
        try {
            return Double.parseDouble(configMap.getOrDefault(name, String.valueOf(defaultValue)));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return Boolean.parseBoolean(configMap.getOrDefault(name, String.valueOf(defaultValue)));
    }

    public char getFloat(String name, char defaultValue) {
        String value = configMap.getOrDefault(name, String.valueOf(defaultValue));
        return value == null ? defaultValue : (value.length() == 1 ? value.charAt(0) : defaultValue);
    }

    @Override
    public String toString() {
        return "Config{" + configMap + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(configMap, config.configMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configMap);
    }
}
