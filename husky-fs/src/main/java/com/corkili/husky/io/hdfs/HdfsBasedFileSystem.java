package com.corkili.husky.io.hdfs;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import lombok.extern.slf4j.Slf4j;

import com.corkili.husky.common.UUIDGenerator;
import com.corkili.husky.config.Config;
import com.corkili.husky.config.ConfigManager;
import com.corkili.husky.exception.AppIOException;
import com.corkili.husky.fs.AppFileSystem;
import com.corkili.husky.fs.AppPath;
import com.corkili.husky.fs.AppPaths;
import com.corkili.husky.fs.FileType;
import com.corkili.husky.util.IUtils;

@Slf4j
public class HdfsBasedFileSystem implements AppFileSystem {

    private static final DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

    private Configuration hdfsConf;
    private String rootPath;
    private String fsPathSeparator;
    private String appPathSeparator;
    private String hdfsLocalTmpPath;
    private boolean isSeparatorFsEqualApp;
    private String currentDate;

    private HdfsBasedFileSystem() {
        Config config = ConfigManager.getInstance().getConfig();
        rootPath = config.getString(HdfsBasedConstants.HDFS_SYSTEM_PATH_ROOT,
                HdfsBasedConstants.DEFAULT_SYSTEM_PATH_ROOT);
        fsPathSeparator = config.getString(HdfsBasedConstants.HDFS_SYSTEM_PATH_SEPARATOR_FS,
                HdfsBasedConstants.DEFAULT_HDFS_SYSTEM_PATH_SEPARATOR_FS);
        appPathSeparator = config.getString(HdfsBasedConstants.HDFS_SYSTEM_PATH_SEPARATOR_APP,
                HdfsBasedConstants.DEFAULT_HDFS_SYSTEM_PATH_SEPARATOR_APP);
        isSeparatorFsEqualApp = fsPathSeparator.equals(appPathSeparator);
        hdfsLocalTmpPath = config.getString(HdfsBasedConstants.HDFS_LOCAL_TMP_PATH,
                HdfsBasedConstants.DEFAULT_HDFS_LOCAL_TMP_PATH);
        if (rootPath.endsWith(fsPathSeparator)) {
            rootPath = rootPath.substring(0, rootPath.length() - 1);
        }
        if (!hdfsLocalTmpPath.endsWith(fsPathSeparator)) {
            hdfsLocalTmpPath += fsPathSeparator;
        }
        String ip = config.getString(HdfsBasedConstants.HDFS_IP,
                HdfsBasedConstants.DEFAULT_HDFS_IP);
        String port = config.getString(HdfsBasedConstants.HDFS_PORT,
                HdfsBasedConstants.DEFAULT_HDFS_PORT);
        hdfsConf = new Configuration();
        hdfsConf.set("fs.defaultFS", "hdfs://" + ip + ":" + port);
        updateDate();
        ScheduledExecutorService updateDateTask = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setName("hdfs-app-file-system-update-date-task");
            t.setDaemon(true);
            return t;
        });
        updateDateTask.scheduleAtFixedRate(this::updateDate, 0, 1, TimeUnit.HOURS);
    }

    @Override
    public FileType typeOf(AppPath path) throws AppIOException {
        normalizePath(path);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(hdfsConf);
            Path srcPath = new Path(path.getPath());
            assertFileExists(fs, srcPath);
            if (fs.isDirectory(srcPath)) {
                return FileType.DIRECTORY;
            } else {
                return FileType.FILE;
            }
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("get type Of file \"{}\" failed - ", path.getPath()), e);
        } finally {
            close(fs);
        }
    }

    @Override
    public List<File> listFiles(AppPath path, boolean recursive) throws AppIOException {
        normalizePath(path);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(hdfsConf);
            Path srcPath = new Path(path.getPath());
            assertFileExists(fs, srcPath);
            RemoteIterator<LocatedFileStatus> fileIterator = fs.listFiles(srcPath, recursive);
            List<File> files = new ArrayList<>();
            while (fileIterator.hasNext()) {
                LocatedFileStatus fileStatus = fileIterator.next();
                Path filePath = fileStatus.getPath();
                if (fileStatus.isFile()) {
                    File file = getFile(fs, filePath);
                    if (file.exists()) {
                        files.add(file);
                    }
                }
            }
            return files;
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("list files of \"{}\" failed - ", path.getPath()), e);
        } finally {
            close(fs);
        }
    }

    @Override
    public File getFile(AppPath path) throws AppIOException {
        normalizePath(path);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(hdfsConf);
            Path srcPath = new Path(path.getPath());
            return getFile(fs, srcPath);
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("get file \"{}\" failed - ", path.getPath()), e);
        } finally {
            close(fs);
        }
    }

    @Override
    public File getExistFile(AppPath path) throws AppIOException {
        normalizePath(path);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(hdfsConf);
            Path srcPath = new Path(path.getPath());
            assertFileExists(fs, srcPath);
            File file = getFile(path);
            assertFileExists(file, file.getAbsolutePath());
            return file;
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("get exist file \"{}\" failed - ", path.getPath()), e);
        } finally {
            close(fs);
        }
    }

    @Override
    public File createFile(AppPath path) throws AppIOException {
        normalizePath(path);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(hdfsConf);
            Path srcPath = new Path(path.getPath());
            assertNotFileExists(fs, srcPath);
            if (fs.createNewFile(srcPath)) {
                return getFile(fs, srcPath);
            } else {
                throw new AppIOException(IUtils.format("create new file \"{}\" failed - ", path.getPath()));
            }
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("create new file \"{}\" failed - ", path.getPath()), e);
        } finally {
            close(fs);
        }
    }

    @Override
    public File createDirectory(AppPath path) throws AppIOException {
        normalizePath(path);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(hdfsConf);
            Path srcPath = new Path(path.getPath());
            assertNotFileExists(fs, srcPath);
            if (fs.mkdirs(srcPath)) {
                return getFile(fs, srcPath);
            } else {
                throw new AppIOException(IUtils.format("create new directory \"{}\" failed - ", path.getPath()));
            }
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("create new directory \"{}\" failed - ", path.getPath()), e);
        } finally {
            close(fs);
        }
    }

    @Override
    public boolean saveFile(AppPath path, File file, boolean recursive) throws AppIOException {
        normalizePath(path);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(hdfsConf);
            Path desPath = new Path(path.getPath());
            assertFileExists(file, file.getAbsolutePath());
            if (file.isDirectory()) {
                return false;
            } else {
                fs.copyFromLocalFile(new Path(file.toURI()), desPath);
                return true;
            }
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("save file \"{}\" to \"{}\" failed - ",
                    file.getAbsoluteFile(), path.getPath()), e);
        } finally {
            close(fs);
        }
    }

    @Override
    public boolean deleteFile(AppPath path, boolean recursive) throws AppIOException {
        normalizePath(path);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(hdfsConf);
            return fs.delete(new Path(path.getPath()), recursive);
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("delete file \"{}\" failed - ", path.getPath()), e);
        } finally {
            close(fs);
        }
    }

    @Override
    public boolean copyFile(AppPath srcPath, AppPath desPath, boolean recursive) throws AppIOException {
        normalizePath(srcPath);
        normalizePath(desPath);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(hdfsConf);
            Path src = new Path(srcPath.getPath());
            assertFileExists(fs, src);
            File file = getFile(fs, src);
            return saveFile(desPath, file);
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("copy file \"{}\" to \"{}\" failed - ",
                srcPath.getPath(), desPath.getPath()), e);
        } finally {
            close(fs);
        }
    }

    @Override
    public boolean moveFile(AppPath srcPath, AppPath desPath, boolean recursive) throws AppIOException {
        normalizePath(srcPath);
        normalizePath(desPath);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(hdfsConf);
            return fs.rename(new Path(srcPath.getPath()), new Path(desPath.getPath()));
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("move file \"{}\" to \"{}\" failed - ",
                    srcPath.getPath(), desPath.getPath()), e);
        } finally {
            close(fs);
        }
    }

    @Override
    public boolean existFile(AppPath path) throws AppIOException {
        normalizePath(path);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(hdfsConf);
            return fs.exists(new Path(path.getPath()));
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("judge exist file \"{}\" failed - ", path.getPath()), e);
        } finally {
            close(fs);
        }
    }

    private void normalizePath(AppPath path) throws AppIOException {
        AppPaths.normalizePath(path, isSeparatorFsEqualApp, rootPath, fsPathSeparator, appPathSeparator);
    }

    private boolean isFileNotExists(File file) {
        return file == null || !file.exists();
    }

    private void assertFileExists(File file, String path) throws AppIOException {
        if (isFileNotExists(file)) {
            throw new AppIOException(IUtils.format("file \"{}\" not found", path));
        }
    }

    private void assertFileExists(FileSystem fs, Path path) throws AppIOException {
        try {
            if (!fs.exists(path)) {
                throw new AppIOException(IUtils.format("file \"{}\" not found", path.toUri().getPath()));
            }
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("file \"{}\" not found", path.toUri().getPath()));
        }
    }

    private void assertNotFileExists(FileSystem fs, Path path) throws AppIOException {
        try {
            if (fs.exists(path)) {
                throw new AppIOException(IUtils.format("file \"{}\" already exist", path.toUri().getPath()));
            }
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("file \"{}\" not found", path.toUri().getPath()));
        }
    }

    private String generateRandomLocalFilePath(String srcPath) {
        return hdfsLocalTmpPath + currentDate + fsPathSeparator + UUIDGenerator.getUUID()
                + srcPath.substring(srcPath.lastIndexOf(fsPathSeparator) + 1);
    }

    private File getFile(FileSystem fs, Path path) throws AppIOException {
        try {
            Path desPath = new Path(generateRandomLocalFilePath(path.toUri().getPath()));
            fs.copyToLocalFile(path, desPath);
            return new File(desPath.toUri());
        } catch (IOException e) {
            throw new AppIOException(IUtils.format("get file \"{}\" failed - ", path.toUri().getPath()), e);
        }
    }

    private void updateDate() {
        this.currentDate = dateFormatter.format(new Date());
    }

    private void close(FileSystem fs) {
        try {
            if (fs != null) {
                fs.close();
            }
        } catch (IOException ignored) {

        }
    }
}
