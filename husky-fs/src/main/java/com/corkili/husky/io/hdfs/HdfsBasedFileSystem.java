package com.corkili.husky.io.hdfs;

import java.io.File;
import java.util.List;

import com.corkili.husky.common.Result;
import com.corkili.husky.exception.AppIOException;
import com.corkili.husky.fs.AppFileSystem;
import com.corkili.husky.fs.FileType;

public class HdfsBasedFileSystem implements AppFileSystem {

    @Override
    public FileType typeOf(String path) throws AppIOException {
        return null;
    }

    @Override
    public List<File> listFiles(String path, boolean recursion) throws AppIOException {
        return null;
    }

    @Override
    public File getFile(String path) throws AppIOException {
        return null;
    }

    @Override
    public Result<Void> saveFile(String path, File file) throws AppIOException {
        return null;
    }

    @Override
    public Result<File> deleteFile(String path) throws AppIOException {
        return null;
    }

    @Override
    public Result<Void> copyFile(String srcPath, String desPath, boolean recursion) throws AppIOException {
        return null;
    }

    @Override
    public Result<Void> moveFile(String srcPath, String desPath, boolean recursion) throws AppIOException {
        return null;
    }

}
