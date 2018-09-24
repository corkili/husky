package com.corkili.husky.fs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.corkili.husky.exception.AppIOException;

public interface AppFileSystem {

    FileType typeOf(AppPath path) throws AppIOException;

    List<File> listFiles(AppPath path, boolean recursion) throws AppIOException;

    File getFile(AppPath path) throws AppIOException;

    File getExistFile(AppPath path) throws AppIOException;

    File createFile(AppPath path) throws AppIOException;

    File createDirectory(AppPath path) throws AppIOException;

    boolean saveFile(AppPath path, File file, boolean recursion) throws AppIOException;

    boolean deleteFile(AppPath path, boolean recursion) throws AppIOException;

    boolean copyFile(AppPath srcPath, AppPath desPath, boolean recursion) throws AppIOException;

    boolean moveFile(AppPath srcPath, AppPath desPath, boolean recursion) throws AppIOException;

    // default method

    default boolean existFile(AppPath path) throws AppIOException {
        File file = getFile(path);
        return file != null && file.exists();
    }

    default List<File> listFiles(AppPath path) throws AppIOException {
        return listFiles(path, false);
    }

    default List<File> listFiles(AppPath... paths) throws AppIOException {
        return listFiles(false, paths);
    }

    default List<File> listFiles(boolean recursion, AppPath... paths) throws AppIOException {
        List<File> files = new ArrayList<>();
        for (AppPath path : paths) {
            files.addAll(listFiles(path, recursion));
        }
        return files;
    }

    default boolean isFile(AppPath path) throws AppIOException {
        return typeOf(path) == FileType.FILE;
    }

    default boolean isDirectory(AppPath path) throws AppIOException {
        return typeOf(path) == FileType.DIRECTORY;
    }

    default boolean isEmptyDirectory(AppPath path) throws AppIOException {
        return isDirectory(path) && listFiles(path, false).size() == 0;
    }

    default boolean saveFile(AppPath path, File file) throws AppIOException {
        return saveFile(path, file, false);
    }

    default boolean deleteFile(AppPath path) throws AppIOException {
        return deleteFile(path, false);
    }

    default boolean copyFile(AppPath srcPath, AppPath desPath) throws AppIOException {
        return copyFile(srcPath, desPath, false);
    }

    default boolean moveFile(AppPath srcPath, AppPath desPath) throws AppIOException {
        return moveFile(srcPath, desPath, false);
    }
}
