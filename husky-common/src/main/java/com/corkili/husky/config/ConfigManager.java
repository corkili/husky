package com.corkili.husky.config;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import com.corkili.husky.common.Query;
import com.corkili.husky.common.Result;
import com.corkili.husky.config.loader.ConfigLoaderFactory;
import com.corkili.husky.util.CheckUtils;
import com.corkili.husky.util.IUtils;

@Slf4j
public final class ConfigManager {

    private static ConfigManager instance;

    private Config config;
    private Map<String, ConfigType> fileTypeMap;
    private List<ConfigFile> configFiles;
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

    public Result<Void> registerConfigFile(ConfigFile configFile) {
        for (ConfigType configType : ConfigType.values()) {
            if (configFile.getFilename().endsWith(configType.suffix())) {
                return registerConfigFile(configFile, configType);
            }
        }
        return new Result<>(false, MessageCode.FILE_TYPE_NOT_SUPPORTED,
                IUtils.format("Type of File \"{}\" is not supported", configFile.getFilename()), null);
    }

    public Result<Void> registerConfigFile(ConfigFile configFile, ConfigType configType) {
        if (CheckUtils.hasNull(configFile, configType)) {
            return new Result<>(false, MessageCode.INVALID_ARGUMENT,
                    "invalid arguments: arguments is null", null);
        }
        Result<Void> result = checkAndAddConfigFile(configFile, configType);
        if (result.fail()) {
            return result;
        }
        updateConfig();
        return new Result<>(true, MessageCode.SUCCESS,
                IUtils.format("File \"{}\" register successfully", configFile.getFilename()), null);
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
        configFiles = new CopyOnWriteArrayList<>();
        updateConfigInterval = Integer.valueOf(System.getProperty(ConfigConstant.CONFIG_NAME_UPDATE_CONFIG_INTERVAL_SEC,
                ConfigConstant.CONFIG_DEFAULT_UPDATE_CONFIG_INTERVAL_SEC));
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

    private synchronized void updateConfig() {
        Map<String, String> configMap = new HashMap<>();
        if (shouldUpdate()) {
            configFiles.forEach(configFile -> {
                String filename = configFile.getFilename();
                configMap.putAll(loadConfigFile(filename, fileTypeMap.get(filename)));
            });
            config.updateConfigMap(configMap);
            log.debug(IUtils.format("config - {}", config));
        }
    }

    private Map<String, String> loadConfigFile(String filename, ConfigType configType) {
        Query query = new Query();
        query.add("filename", filename);
        Result<Map<String, String>> result = ConfigLoaderFactory.getConfigLoader(configType).load(query);
        return result.success() ? result.data() : new HashMap<>();
    }

    private boolean shouldUpdate() {
        boolean res = false;
        for (String filename : fileTypeMap.keySet()) {
            File file = null;
            try {
                URL url = this.getClass().getClassLoader().getResource(filename);
                if (url != null) {
                    file = new File(url.toURI());
                }
            } catch (Exception e) {
                log.error(IUtils.stringifyError(e));
            }
            if (file != null && file.exists() && file.lastModified() > fileLastModifiedTimeMap.get(filename)) {
                fileLastModifiedTimeMap.put(filename, file.lastModified());
                res = true;
            }
        }
        return res;
    }

    private Result<Void> checkAndAddConfigFile(ConfigFile configFile, ConfigType configType) {
        if (configFile.getPriority() == ConfigFile.DEFAULT_PRIORITY) {
            return addConfigFile(new ConfigFile(configFile.getFilename(), generatePriority()), configType);
        } else if (isDuplicatePriority(configFile)){
            return new Result<>(false, MessageCode.DUPLICATE_PRIORITY,
                    IUtils.format("Priority of file \"{}\" is duplicate", configFile.getFilename()), null);
        } else if (fileTypeMap.containsKey(configFile.getFilename())) {
            return new Result<>(false, MessageCode.DUPLICATE_PRIORITY,
                    IUtils.format("Filename of file \"{}\" is duplicate", configFile.getFilename()), null);
        } else {
            return addConfigFile(configFile, configType);
        }
    }

    private int generatePriority() {
        if (configFiles.isEmpty()) {
            return 1;
        } else {
            return configFiles.get(configFiles.size() - 1).getPriority() + 1;
        }
    }

    private boolean isDuplicatePriority(ConfigFile configFile) {
        for (ConfigFile file : configFiles) {
            if (file.getPriority() == configFile.getPriority()) {
                return true;
            }
        }
        return false;
    }

    private Result<Void> addConfigFile(ConfigFile configFile, ConfigType configType) {
        URL url = this.getClass().getClassLoader().getResource(configFile.getFilename());
        if (url == null) {
            return new Result<>(false, MessageCode.FILE_NOT_EXIST,
                    IUtils.format("File \"{}\" not found", configFile.getFilename()), null);
        }
        fileTypeMap.put(configFile.getFilename(), configType);
        fileLastModifiedTimeMap.put(configFile.getFilename(), Long.MIN_VALUE);
        configFiles.add(configFile);
        Collections.sort(configFiles);
        return new Result<>(true, MessageCode.SUCCESS,
                IUtils.format("add config file - {} successfully", configFile), null);
    }
    

}
