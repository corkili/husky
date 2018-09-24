package com.corkili.husky.fs;

import com.corkili.husky.io.file.FileBasedAppFileSystem;
import com.corkili.husky.io.hdfs.HdfsBasedFileSystem;

public enum  FileSystemType {
    FILE("file", FileBasedAppFileSystem.class),
    HDFS("hdfs", HdfsBasedFileSystem.class);

    private String protocol;

    private Class<? extends AppFileSystem> implClass;

    FileSystemType(String protocol, Class<? extends AppFileSystem> implClass) {
        this.protocol = protocol;
        this.implClass = implClass;
    }

    public String getProtocol() {
        return protocol;
    }

    public Class<? extends AppFileSystem> getImplClass() {
        return implClass;
    }
}
