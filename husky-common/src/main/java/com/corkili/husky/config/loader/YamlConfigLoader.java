package com.corkili.husky.config.loader;

import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import com.corkili.husky.common.Result;
import com.corkili.husky.config.MessageCode;
import com.corkili.husky.util.IUtils;

public final class YamlConfigLoader extends AbstractConfigLoader {
    
    private static YamlConfigLoader instance;

    static YamlConfigLoader getInstance() {
        if (instance == null) {
            synchronized (YamlConfigLoader.class) {
                if (instance == null) {
                    instance = new YamlConfigLoader();
                }
            }
        }
        return instance;
    }

    private YamlConfigLoader() {

    }

    @Override
    protected Result<Map<String, String>> load0(String filename) {
        try {
            Yaml yaml = new Yaml(new SafeConstructor());
            Map<String, Object> yamlMap = yaml.load(this.getClass().getClassLoader().getResourceAsStream(filename));
            Map<String, String> config = new HashMap<>();
            yamlMap.forEach((key, value) -> config.put(key, value.toString()));
            return new Result<>(true, MessageCode.SUCCESS, "success", config);
        } catch (Exception e) {
            return new Result<>(false, MessageCode.SYSTEM_INTERNAL_ERROR, IUtils.stringifyError(e), null);
        }
    }
}
