package com.corkili.husky.io.file;

import java.io.File;
import java.util.List;

import com.corkili.husky.common.Result;
import com.corkili.husky.fs.AppFileSystem;
import com.corkili.husky.fs.FileType;

public class FileBasedAppFileSystem implements AppFileSystem {

    @Override
    public FileType typeOf(String path) {
        return null;
    }

    @Override
    public List<File> listFiles(String path, boolean recursion) {
        return null;
    }

    @Override
    public File getFile(String path) {
        return null;
    }

    @Override
    public Result<Void> saveFile(String path, File file) {
        return null;
    }

    @Override
    public Result<File> deleteFile(String path) {
        return null;
    }

    @Override
    public Result<Void> copyFile(String srcPath, String desPath, boolean recursion) {
        return null;
    }

    @Override
    public Result<Void> moveFile(String srcPath, String desPath, boolean recursion) {
        return null;
    }
}
