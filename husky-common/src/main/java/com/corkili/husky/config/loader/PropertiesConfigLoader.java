package com.corkili.husky.config.loader;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import com.corkili.husky.common.Result;
import com.corkili.husky.config.MessageCode;
import com.corkili.husky.util.IUtils;

public final class PropertiesConfigLoader extends AbstractConfigLoader {

    private static PropertiesConfigLoader instance;

    static PropertiesConfigLoader getInstance() {
        if (instance == null) {
            synchronized (PropertiesConfigLoader.class) {
                if (instance == null) {
                    instance = new PropertiesConfigLoader();
                }
            }
        }
        return instance;
    }

    private PropertiesConfigLoader() {

    }

    @Override
    protected Result<Map<String, String>> load0(String filename) {
        try {
            FileBasedConfigurationBuilder.setDefaultEncoding(PropertiesConfiguration.class, "UTF-8");
            PropertiesConfiguration configuration = new Configurations()
                    .properties(this.getClass().getClassLoader().getResource(filename));
            Map<String, String> config = new HashMap<>();
            configuration.getKeys().forEachRemaining(key -> config.put(key, configuration.getString(key)));
            return new Result<>(true, MessageCode.SUCCESS, "success", config);
        } catch (Exception e) {
            return new Result<>(false, MessageCode.SYSTEM_INTERNAL_ERROR, IUtils.stringifyError(e), null);
        }
    }
}
