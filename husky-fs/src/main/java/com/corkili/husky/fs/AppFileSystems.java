package com.corkili.husky.fs;

import java.lang.reflect.Constructor;

import lombok.extern.slf4j.Slf4j;

import com.corkili.husky.config.Config;
import com.corkili.husky.config.ConfigManager;
import com.corkili.husky.exception.InitAppFileSystemException;
import com.corkili.husky.util.IUtils;

@Slf4j
public final class AppFileSystems {

    private static FileSystemType fileSystemType;

    private static AppFileSystem appFileSystem;

    static {
        init();
    }

    public static AppFileSystem getAppFileSystem() {
        if (appFileSystem == null) {
            synchronized (AppFileSystems.class) {
                if (appFileSystem == null) {
                    if (fileSystemType == null) {
                        fileSystemType = FileSystemType.FILE;
                    }
                    try {
                        Constructor constructor = fileSystemType.getImplClass().getDeclaredConstructor();
                        constructor.setAccessible(true);
                        appFileSystem = (AppFileSystem) constructor.newInstance();
                        constructor.setAccessible(false);
                    } catch (Exception e) {
                        log.error(IUtils.format("init appFileSystem failed - {}", IUtils.stringifyError(e)));
                        throw new InitAppFileSystemException("init appFileSystem failed", e);
                    }
                }
            }
        }
        return appFileSystem;
    }

    private static void init() {
        ConfigManager manager = ConfigManager.getInstance();
        Config config = manager.getConfig();
        String fsType = config.getString(AppFSConstants.APP_FILE_SYSTEM_TYPE,
                AppFSConstants.DEFAULT_APP_FILE_SYSTEM_TYPE).toLowerCase();
        FileSystemType systemType = FileSystemType.FILE;
        for (FileSystemType type : FileSystemType.values()) {
            if (type.getProtocol().equals(fsType)) {
                systemType = type;
            }
        }
        fileSystemType = systemType;
        appFileSystem = getAppFileSystem();
    }

    private AppFileSystems() {

    }

}
