package com.corkili.husky.fs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.corkili.husky.common.Result;

public interface AppFileSystem {

    FileType typeOf(String path);

    List<File> listFiles(String path, boolean recursion);

    File getFile(String path);

    Result<Void> saveFile(String path, File file);

    Result<File> deleteFile(String path);

    Result<Void> copyFile(String srcPath, String desPath, boolean recursion);

    Result<Void> moveFile(String srcPath, String desPath, boolean recursion);

    // default method

    default boolean existFile(String path) {
        File file = getFile(path);
        return file != null && file.exists();
    }

    default List<File> listFiles(String path) {
        return listFiles(path, false);
    }

    default List<File> listFiles(String... paths) {
        return listFiles(false, paths);
    }

    default List<File> listFiles(boolean recursion, String... paths) {
        List<File> files = new ArrayList<>();
        Arrays.stream(paths).forEach(path -> files.addAll(listFiles(path, recursion)));
        return files;
    }

    default boolean isFile(String path) {
        return typeOf(path) == FileType.FILE;
    }

    default boolean isDirectory(String path) {
        return typeOf(path) == FileType.DIRECTORY;
    }

    default boolean isEmptyDirectory(String path) {
        return isDirectory(path) && listFiles(path, false).size() == 0;
    }
}
