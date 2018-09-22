package com.corkili.husky.common;

import java.util.Objects;

public final class Result<Data> {

    private final boolean success;
    private final int code;
    private final String message;
    private final Data data;
    private final long createTimestamp;
    private final int hash;

    public Result(boolean success, int code, String message, Data data) {
        this.createTimestamp = System.currentTimeMillis();
        this.success = success;
        this.code = code;
        this.message = message == null ? "" : message;
        this.data = data;
        this.hash = computeHashCode();
    }

    public Result(Result<Data> result) {
        this.createTimestamp = System.currentTimeMillis();
        this.success = result.success;
        this.code = 0;
        this.message = result.message == null ? "" : result.message;
        this.data = result.data;
        this.hash = computeHashCode();
    }

    public boolean success() {
        return success;
    }

    public boolean fail() {
        return !success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public Data data() {
        return data;
    }

    public long createTimestamp() {
        return createTimestamp;
    }

    private int computeHashCode() {
        return Objects.hash(success, code, message, data, createTimestamp);
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", createTimestamp=" + createTimestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return success == result.success &&
                code == result.code &&
                createTimestamp == result.createTimestamp &&
                Objects.equals(message, result.message) &&
                Objects.equals(data, result.data);
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

}
