package com.corkili.husky.config.loader;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import com.corkili.husky.common.Result;
import com.corkili.husky.util.IUtils;

public final class XmlConfigLoader extends AbstractConfigLoader {

    private static XmlConfigLoader instance;

    static XmlConfigLoader getInstance() {
        if (instance == null) {
            synchronized (XmlConfigLoader.class) {
                if (instance == null) {
                    instance = new XmlConfigLoader();
                }
            }
        }
        return instance;
    }

    private XmlConfigLoader() {

    }

    @Override
    protected Result<Map<String, String>> load0(String filename) {
        try {
            FileBasedConfigurationBuilder.setDefaultEncoding(PropertiesConfiguration.class, "UTF-8");
            XMLConfiguration configuration = new Configurations()
                    .xml(this.getClass().getClassLoader().getResource(filename));
            Map<String, String> config = new HashMap<>();
            configuration.getKeys().forEachRemaining(key -> config.put(key, configuration.getString(key)));
            return new Result<>(false, "success", config);
        } catch (Exception e) {
            return new Result<>(false, IUtils.stringifyError(e), null);
        }
    }
}
