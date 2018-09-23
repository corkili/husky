package com.corkili.husky.io.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import com.corkili.husky.common.Result;
import com.corkili.husky.config.Config;
import com.corkili.husky.config.ConfigManager;
import com.corkili.husky.exception.AppIOException;
import com.corkili.husky.fs.AppFileSystem;
import com.corkili.husky.fs.FileType;
import com.corkili.husky.util.IUtils;

@Slf4j
public class FileBasedAppFileSystem implements AppFileSystem {

    private String rootPath;
    private String fsPathSeparator;
    private String appPathSeparator;
    private boolean isSeparatorFsEqualApp;

    public FileBasedAppFileSystem() {
        Config config = ConfigManager.getInstance().getConfig();
        rootPath = config.getString(FileBasedConstants.FILE_SYSTEM_PATH_ROOT,
                FileBasedConstants.DEFAULT_SYSTEM_PATH_ROOT);
        fsPathSeparator = config.getString(FileBasedConstants.FILE_SYSTEM_PATH_SEPARATOR_FS,
                FileBasedConstants.DEFAULT_FILE_SYSTEM_PATH_SEPARATOR_FS);
        appPathSeparator = config.getString(FileBasedConstants.FILE_SYSTEM_PATH_SEPARATOR_APP,
                FileBasedConstants.DEFAULT_FILE_SYSTEM_PATH_SEPARATOR_APP);
        isSeparatorFsEqualApp = fsPathSeparator.equals(appPathSeparator);
        if (rootPath.endsWith(fsPathSeparator)) {
            rootPath = rootPath.substring(0, rootPath.length() - 1);
        }
    }

    @Override
    public FileType typeOf(String path) throws AppIOException {
        String nPath = normalizePath(path);
        File file = getFile(nPath);
        checkFileExists(file, nPath);
        if (file.isFile()) {
            return FileType.FILE;
        } else if (file.isDirectory()){
            return FileType.DIRECTORY;
        } else {
            throw new AppIOException(IUtils.format("File's type \"{}\" not defined", nPath));
        }
    }

    @Override
    public List<File> listFiles(String path, boolean recursion) throws AppIOException {
        String nPath = normalizePath(path);
        File file = getFile(path);
        checkFileExists(file, nPath);
        if (!file.isDirectory()) {
            throw new AppIOException(IUtils.format("\"{}\" is not a directory", nPath));
        }
        Set<File> fileSet = new HashSet<>();
        listFiles(file, fileSet);
        return new ArrayList<>(fileSet);
    }

    @Override
    public File getFile(String path) throws AppIOException {
        String nPath = normalizePath(path);
        File file = new File(nPath);
        checkFileExists(file, nPath);
        return file;
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

    private String normalizePath(String path) throws AppIOException {
        if (path == null) {
            throw new AppIOException("Path is null");
        }
        if (path.startsWith(rootPath)) {
            return path;
        }
        if (isSeparatorFsEqualApp) {
            return normalizePath(path, fsPathSeparator);
        } else {
            return normalizePath(path, fsPathSeparator, appPathSeparator);
        }
    }

    private String normalizePath(String path, String fsSeparator, String appSeparator) {
        return normalizePath(path.replaceAll(appSeparator, fsSeparator), fsSeparator);
    }

    private String normalizePath(String path, String separator) {
        String resPath = rootPath;
        if (!path.startsWith(separator)) {
            resPath += separator;
        }
        resPath += path;
        if (resPath.endsWith(separator)) {
            resPath = resPath.substring(0, resPath.length() - 1);
        }
        return resPath;
    }

    private boolean isFileNotExists(File file) {
        return file == null || !file.exists();
    }

    private void checkFileExists(File file, String path) throws AppIOException {
        if (isFileNotExists(file)) {
            throw new AppIOException(IUtils.format("File \"{}\" not found", path));
        }
    }

    private void listFiles(File file, Set<File> fileSet) {
        if (isFileNotExists(file)) {
            return;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        Collections.addAll(fileSet, files);
        for (File childFile : files) {
            if (childFile.isDirectory()) {
                listFiles(childFile, fileSet);
            }
        }
    }
}
