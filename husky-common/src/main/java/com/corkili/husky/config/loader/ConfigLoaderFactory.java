package com.corkili.husky.config.loader;

import com.corkili.husky.config.ConfigType;
import com.corkili.husky.config.exception.ConfigLoaderNotFoundException;

public final class ConfigLoaderFactory {
    
    public static ConfigLoader getConfigLoader(ConfigType configType) {
        switch (configType) {
            case XML_CONFIG:
                return XmlConfigLoader.getInstance();
            case YAML_CONFIG:
                return YamlConfigLoader.getInstance();
            case PROPERTIES_CONFIG:
                return PropertiesConfigLoader.getInstance();
            default:
                throw new ConfigLoaderNotFoundException("com.corkili.husky.config loader of " + configType.name() + " not found");
        }
    }
    
}
