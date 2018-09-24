package com.corkili.husky.io.hdfs;

import java.io.File;
import java.util.List;

import com.corkili.husky.exception.AppIOException;
import com.corkili.husky.fs.AppFileSystem;
import com.corkili.husky.fs.AppPath;
import com.corkili.husky.fs.FileType;

public class HdfsBasedFileSystem implements AppFileSystem {

    private HdfsBasedFileSystem() {

    }

    @Override
    public FileType typeOf(AppPath path) throws AppIOException {
        return null;
    }

    @Override
    public List<File> listFiles(AppPath path, boolean recursive) throws AppIOException {
        return null;
    }

    @Override
    public File getFile(AppPath path) throws AppIOException {
        return null;
    }

    @Override
    public File getExistFile(AppPath path) throws AppIOException {
        return null;
    }

    @Override
    public File createFile(AppPath path) throws AppIOException {
        return null;
    }

    @Override
    public File createDirectory(AppPath path) throws AppIOException {
        return null;
    }

    @Override
    public boolean saveFile(AppPath path, File file, boolean recursive) throws AppIOException {
        return false;
    }

    @Override
    public boolean deleteFile(AppPath path, boolean recursive) throws AppIOException {
        return false;
    }

    @Override
    public boolean copyFile(AppPath srcPath, AppPath desPath, boolean recursive) throws AppIOException {
        return false;
    }

    @Override
    public boolean moveFile(AppPath srcPath, AppPath desPath, boolean recursive) throws AppIOException {
        return false;
    }
}
