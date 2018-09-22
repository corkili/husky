package com.corkili.husky.config;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.corkili.husky.config.loader.ConfigLoaderFactory;
import com.corkili.husky.common.Query;
import com.corkili.husky.common.Result;
import com.corkili.husky.util.CheckUtils;
import com.corkili.husky.util.IUtils;

public final class ConfigManager {

    private static ConfigManager instance;

    private Config config;
    private Map<String, ConfigType> fileTypeMap;
    private Map<String, Long> fileLastModifiedTimeMap;
    private ScheduledExecutorService executor;
    private int updateConfigInterval;

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    private ConfigManager() {
        initConfig();
        initTask();
    }

    public Config getConfig() {
        return this.config;
    }

    public Result<Void> registerConfigFile(String filename) {
        for (ConfigType configType : ConfigType.values()) {
            if (filename.endsWith(configType.suffix())) {
                return registerConfigFile(filename, configType);
            }
        }
        return new Result<>(false, IUtils.format("Type of File \"{}\" is not supported", filename), null);
    }

    public Result<Void> registerConfigFile(String filename, ConfigType configType) {
        if (CheckUtils.hasNull(filename, configType)) {
            return new Result<>(false, "invalid arguments: arguments is null", null);
        }
        URL url = this.getClass().getClassLoader().getResource(filename);
        if (url == null) {
            return new Result<>(false,
                    IUtils.format("File \"{}\" not found", filename), null);
        }
        fileTypeMap.put(filename, configType);
        fileLastModifiedTimeMap.put(filename, Long.MIN_VALUE);
        updateConfig();
        return new Result<>(true,
                IUtils.format("File \"{}\" register successfully", filename), null);
    }

    public void shutdown() {
        fileTypeMap.clear();
        fileLastModifiedTimeMap.clear();
        executor.shutdownNow();
    }

    private void initConfig() {
        config = new Config();
        fileTypeMap = new ConcurrentHashMap<>();
        fileLastModifiedTimeMap = new ConcurrentHashMap<>();
        String defaultPaConfFile = System.getProperty(ConfigConstant.SYSTEM_CONFIGURE_CONF_FILE,
                ConfigConstant.DEFAULT_CONFIGURE_CONF_FILE);
        registerConfigFile(defaultPaConfFile);
        updateConfigInterval = config.getInteger(ConfigConstant.CONFIG_NAME_UPDATE_CONFIG_INTERVAL_SEC,
                ConfigConstant.CONFIG_DEFAULT_UPDATE_CONFIG_INTERVAL_SEC);
    }

    private void initTask() {
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread();
            t.setName("scheduled-update-com.corkili.husky.config-thread");
            t.setDaemon(true);
            return t;
        });
        executor.scheduleAtFixedRate(this::updateConfig, updateConfigInterval, updateConfigInterval, TimeUnit.SECONDS);
    }

    private void updateConfig() {
        Map<String, String> configMap = new HashMap<>();
        fileTypeMap.forEach((filename, type) -> {
            File file = new File(filename);
            if (file.exists() && file.lastModified() > fileLastModifiedTimeMap.get(filename)) {
                fileLastModifiedTimeMap.put(filename, file.lastModified());
                configMap.putAll(loadConfigFile(filename, type));
            }
        });
        config.updateConfigMap(configMap);
    }

    private Map<String, String> loadConfigFile(String filename, ConfigType configType) {
        Query query = new Query();
        query.add("filename", filename);
        Result<Map<String, String>> result = ConfigLoaderFactory.getConfigLoader(configType).load(query);
        return result.isSuccess() ? result.getExtra() : new HashMap<>();
    }

}
