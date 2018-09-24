package com.corkili.husky.io.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import lombok.extern.slf4j.Slf4j;

import com.corkili.husky.config.Config;
import com.corkili.husky.config.ConfigManager;
import com.corkili.husky.exception.AppIOException;
import com.corkili.husky.fs.AppFileSystem;
import com.corkili.husky.fs.AppPath;
import com.corkili.husky.fs.AppPaths;
import com.corkili.husky.fs.FileType;
import com.corkili.husky.util.IUtils;

@Slf4j
public class FileBasedAppFileSystem implements AppFileSystem {

    private String rootPath;
    private String fsPathSeparator;
    private String appPathSeparator;
    private boolean isSeparatorFsEqualApp;

    private FileBasedAppFileSystem() {
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
    public FileType typeOf(AppPath path) throws AppIOException {
        normalizePath(path);
        File file = getFile(path);
        assertFileExists(file, path.getPath());
        if (file.isFile()) {
            return FileType.FILE;
        } else if (file.isDirectory()){
            return FileType.DIRECTORY;
        } else {
            throw new AppIOException(IUtils.format("file's type \"{}\" not defined", path));
        }
    }

    @Override
    public List<File> listFiles(AppPath path, boolean recursion) throws AppIOException {
        normalizePath(path);
        File file = getFile(path);
        assertFileExists(file, path.getPath());
        if (!file.isDirectory()) {
            throw new AppIOException(IUtils.format("\"{}\" is not a directory", path));
        }
        List<File> files = new ArrayList<>();
        if (recursion) {
            Set<File> fileSet = new HashSet<>();
            listFilesRecursion(file, fileSet);
            files.addAll(fileSet);
        } else {
            File[] allFile = file.listFiles();
            if (allFile != null) {
                Collections.addAll(files, allFile);
            }
        }
        return files;
    }

    @Override
    public File getFile(AppPath path) throws AppIOException {
        normalizePath(path);
        return new File(path.getPath());
    }

    @Override
    public File getExistFile(AppPath path) throws AppIOException {
        normalizePath(path);
        File file = getFile(path);
        assertFileExists(file, path.getPath());
        return file;
    }

    @Override
    public File createFile(AppPath path) throws AppIOException {
        normalizePath(path);
        File file = getFile(path);
        assertNotFileExists(file, path.getPath());
        boolean failed;
        try {
            failed = !file.createNewFile();
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("create file \"{}\" exception - ", path.getPath()), e);
        }
        if (failed) {
            throw new AppIOException(IUtils.format("create file \"{}\" failed - ", path.getPath()));
        }
        return file;
    }

    @Override
    public File createDirectory(AppPath path) throws AppIOException {
        normalizePath(path);
        File file = getFile(path);
        assertNotFileExists(file, path.getPath());
        if (!file.mkdirs()) {
            throw new AppIOException(IUtils.format("create directory \"{}\" failed - ", path.getPath()));
        }
        return file;
    }

    @Override
    public boolean saveFile(AppPath path, File file, boolean recursion) throws AppIOException {
        normalizePath(path);
        assertFileExists(file, file.getAbsolutePath());
        return copyFile(new AppPath(file.getAbsolutePath()).normalized(), path, recursion);
    }

    @Override
    public boolean deleteFile(AppPath path, boolean recursion) throws AppIOException {
        normalizePath(path);
        File file = getFile(path);
        assertFileExists(file, path.getPath());
        if (file.isDirectory() && recursion) {
            return deleteFileRecursion(file);
        } else {
            return file.delete();
        }
    }

    @Override
    public boolean copyFile(AppPath srcPath, AppPath desPath, boolean recursion) throws AppIOException {
        normalizePath(srcPath);
        normalizePath(desPath);
        File srcFile = getFile(srcPath);
        File desFile = getFile(desPath);
        assertFileExists(srcFile, srcPath.getPath());
        assertNotFileExists(desFile, desPath.getPath());
        if (srcFile.isDirectory() && recursion) {
            copyFileRecursion(srcFile, desFile);
        } else if (srcFile.isDirectory() && !recursion) {
            return false;
        } else {
            copyFile(srcFile, desFile);
        }
        return true;
    }

    @Override
    public boolean moveFile(AppPath srcPath, AppPath desPath, boolean recursion) throws AppIOException {
        normalizePath(srcPath);
        normalizePath(desPath);
        try {
            return copyFile(srcPath, desPath, recursion) && deleteFile(srcPath, recursion);
        } catch (AppIOException e) {
            throw new AppIOException(IUtils.format("move file \"{}\" to \"{}\" failed - ",
                    srcPath.getPath(), desPath.getPath()), e);
        }
    }

    private void normalizePath(AppPath path) throws AppIOException {
        AppPaths.normalizePath(path, isSeparatorFsEqualApp, rootPath, fsPathSeparator, appPathSeparator);
    }

    private boolean isFileExists(File file) {
        return !isFileNotExists(file);
    }

    private boolean isFileNotExists(File file) {
        return file == null || !file.exists();
    }

    private void assertFileExists(File file, String path) throws AppIOException {
        if (isFileNotExists(file)) {
            throw new AppIOException(IUtils.format("file \"{}\" not found", path));
        }
    }

    private void assertNotFileExists(File file, String path) throws AppIOException {
        if (isFileExists(file)) {
            throw new AppIOException(IUtils.format("file \"{}\" already exist", path));
        }
    }

    private void listFilesRecursion(File file, Set<File> fileSet) {
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
                listFilesRecursion(childFile, fileSet);
            }
        }
    }

    private void copyFile(File srcFile, File desFile) throws AppIOException {
        try {
            FileUtils.copyFile(srcFile, desFile, true);
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("copy file \"{}\" to \"{}\" failed - ",
                    srcFile.getPath(), desFile.getPath()), e);
        }
    }

    private void copyFileRecursion(File srcFile, File desFile) throws AppIOException {
        try {
            FileUtils.copyDirectory(srcFile, desFile, true);
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("copy file \"{}\" to \"{}\" failed - ",
                    srcFile.getPath(), desFile.getPath()), e);
        }
    }

    private boolean deleteFileRecursion(File file) {
        if (isFileNotExists(file)) {
            return true;
        }
        boolean success = true;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    success &= deleteFileRecursion(childFile);
                }
            }
        }
        return success & file.delete();
    }
}
