package com.corkili.husky.config.loader;

import java.util.Map;

import com.corkili.husky.annotation.Param;
import com.corkili.husky.annotation.Params;
import com.corkili.husky.common.Query;
import com.corkili.husky.common.Result;

public interface ConfigLoader {

    @Params(params = {
            @Param(name = "filename", type = String.class)})
    Result<Map<String, String>> load(Query query);

}
