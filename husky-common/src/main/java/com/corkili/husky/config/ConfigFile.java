package com.corkili.husky.config;

import java.util.Objects;

public class ConfigFile implements Comparable<ConfigFile> {

    public static final int DEFAULT_PRIORITY = -1;

    private final String filename;
    private final int priority;
    private final int hash;

    public ConfigFile(String filename, int priority) {
        this.filename = filename;
        this.priority = priority;
        this.hash = Objects.hash(filename, priority);
    }

    public ConfigFile(String filename) {
        this(filename, DEFAULT_PRIORITY);
    }

    public String getFilename() {
        return filename;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigFile that = (ConfigFile) o;
        return priority == that.priority &&
                Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    @Override
    public String toString() {
        return "ConfigFile{" +
                "filename='" + filename + '\'' +
                ", priority=" + priority +
                '}';
    }

    @Override
    public int compareTo(ConfigFile other) {
        return priority - other.priority;
    }
}
