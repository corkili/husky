package com.corkili.husky.fs;

import java.util.Objects;

public class AppPath {

    private String path;
    private boolean normalized;

    public AppPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public AppPath setPath(String path) {
        this.path = path;
        return this;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public AppPath normalized() {
        this.normalized = true;
        return this;
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppPath appPath = (AppPath) o;
        return Objects.equals(path, appPath.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
