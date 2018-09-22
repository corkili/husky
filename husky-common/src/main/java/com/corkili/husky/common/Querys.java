package com.corkili.husky.common;

import java.lang.reflect.Executable;

import com.corkili.husky.annotation.Param;
import com.corkili.husky.annotation.Params;
import com.corkili.husky.util.CheckUtils;

public final class Querys {

    public static boolean checkQuery(Class<?> clazz, String methodName, Query query) {
        try {
            Executable method;
            if (methodName.equals(clazz.getSimpleName())) {
                method = clazz.getDeclaredConstructor(Query.class);
            } else {
                method = clazz.getDeclaredMethod(methodName, Query.class);
            }
            if (CheckUtils.isNull(method) || !method.isAnnotationPresent(Params.class)) {
                return false;
            }
            Param[] params = method.getAnnotation(Params.class).params();
            if (CheckUtils.isEmpty(params)) {
                return true;
            }
            for (Param param : params) {
                if (param.required() &&
                        CheckUtils.isNull(query.get(param.name(), param.type()))) {
                    return false;
                }
            }
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

}
