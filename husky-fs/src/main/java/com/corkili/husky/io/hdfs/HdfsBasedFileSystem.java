package com.corkili.husky.io.hdfs;

import java.io.File;
import java.util.List;

import com.corkili.husky.exception.AppIOException;
import com.corkili.husky.fs.AppFileSystem;
import com.corkili.husky.fs.AppPath;
import com.corkili.husky.fs.FileType;

public class HdfsBasedFileSystem implements AppFileSystem {


    @Override
    public FileType typeOf(AppPath path) throws AppIOException {
        return null;
    }

    @Override
    public List<File> listFiles(AppPath path, boolean recursion) throws AppIOException {
        return null;
    }

    @Override
    public File getFile(AppPath path) throws AppIOException {
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
    public boolean saveFile(AppPath path, File file) throws AppIOException {
        return false;
    }

    @Override
    public boolean deleteFile(AppPath path) throws AppIOException {
        return false;
    }

    @Override
    public boolean copyFile(AppPath srcPath, AppPath desPath, boolean recursion) throws AppIOException {
        return false;
    }

    @Override
    public boolean moveFile(AppPath srcPath, AppPath desPath, boolean recursion) throws AppIOException {
        return false;
    }
}
