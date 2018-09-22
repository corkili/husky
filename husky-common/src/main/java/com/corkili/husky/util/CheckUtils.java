package com.corkili.husky.util;

public final class CheckUtils {

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static boolean allIsNull(Object... objects) {
        for (Object obj : objects) {
            if (obj != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean allIsNotNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasNotNull(Object... objects) {
        for (Object obj : objects) {
            if (obj != null) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return array != null && array.length != 0;
    }

}
