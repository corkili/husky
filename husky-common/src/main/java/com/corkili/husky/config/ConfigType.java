package com.corkili.husky.config;

public enum ConfigType {
    XML_CONFIG(".xml"),
    YAML_CONFIG(".yaml"),
    PROPERTIES_CONFIG(".properties");

    private String suffix;

    ConfigType(String suffix) {
        this.suffix = suffix;
    }

    public String suffix() {
        return this.suffix;
    }
}
