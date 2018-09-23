package com.corkili.husky.fs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.corkili.husky.common.Result;
import com.corkili.husky.exception.AppIOException;

public interface AppFileSystem {

    FileType typeOf(String path) throws AppIOException;

    List<File> listFiles(String path, boolean recursion) throws AppIOException;

    File getFile(String path) throws AppIOException;

    Result<Void> saveFile(String path, File file) throws AppIOException;

    Result<File> deleteFile(String path) throws AppIOException;

    Result<Void> copyFile(String srcPath, String desPath, boolean recursion) throws AppIOException;

    Result<Void> moveFile(String srcPath, String desPath, boolean recursion) throws AppIOException;

    // default method

    default boolean existFile(String path) throws AppIOException {
        File file = getFile(path);
        return file != null && file.exists();
    }

    default List<File> listFiles(String path) throws AppIOException {
        return listFiles(path, false);
    }

    default List<File> listFiles(String... paths) throws AppIOException {
        return listFiles(false, paths);
    }

    default List<File> listFiles(boolean recursion, String... paths) throws AppIOException {
        List<File> files = new ArrayList<>();
        for (String path : paths) {
            files.addAll(listFiles(path, recursion));
        }
        return files;
    }

    default boolean isFile(String path) throws AppIOException {
        return typeOf(path) == FileType.FILE;
    }

    default boolean isDirectory(String path) throws AppIOException {
        return typeOf(path) == FileType.DIRECTORY;
    }

    default boolean isEmptyDirectory(String path) throws AppIOException {
        return isDirectory(path) && listFiles(path, false).size() == 0;
    }
}
