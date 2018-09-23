package com.corkili.husky.common;

import com.corkili.husky.fs.FileSystemType;

public abstract class AppFSConstants {

    public static final String APP_FILE_SYSTEM_TYPE = "filesystem.type";

    public static final String DEFAULT_APP_FILE_SYSTEM_TYPE = FileSystemType.FILE.getProtocol();

}
