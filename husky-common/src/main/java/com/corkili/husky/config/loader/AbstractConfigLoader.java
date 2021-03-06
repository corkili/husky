package com.corkili.husky.config.loader;

import java.util.Map;

import com.corkili.husky.common.Query;
import com.corkili.husky.common.Result;
import com.corkili.husky.common.Querys;
import com.corkili.husky.config.MessageCode;

public abstract class AbstractConfigLoader implements ConfigLoader {

    @Override
    public Result<Map<String, String>> load(Query query) {
        if (!Querys.checkQuery(ConfigLoader.class, "load", query)) {
            return new Result<>(false, MessageCode.INVALID_ARGUMENT, "invalid arguments", null);
        }
        return load0(query.getString("filename"));
    }

    protected abstract Result<Map<String, String>> load0(String filename);
}
