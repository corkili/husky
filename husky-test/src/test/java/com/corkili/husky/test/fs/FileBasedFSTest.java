package com.corkili.husky.test.fs;

import org.junit.Test;

import com.corkili.husky.config.ConfigFile;
import com.corkili.husky.config.ConfigManager;
import com.corkili.husky.exception.AppIOException;
import com.corkili.husky.fs.AppFileSystem;
import com.corkili.husky.fs.AppFileSystems;
import com.corkili.husky.fs.AppPath;

public class FileBasedFSTest {
    @Test
    public void testFileBasedFS() throws AppIOException {
        ConfigManager.getInstance().registerConfigFile(new ConfigFile("fs.properties"));
        AppFileSystem fs = AppFileSystems.getAppFileSystem();
        System.out.println("createDirectory: " + fs.createDirectory(new AppPath("/testDir/sub")).getAbsolutePath());
        System.out.println("createFile: " + fs.createFile(new AppPath("/testDir/sub/test.txt")).getAbsolutePath());

        System.out.println("getExistFile: " + fs.getExistFile(new AppPath("/testDir/sub/test.txt")).getAbsolutePath());
        System.out.println("getFile(exist): " + fs.getFile(new AppPath("/testDir/sub/test.txt")).exists());
        System.out.println("getFile(not exist): " + fs.getFile(new AppPath("/testDir/sub/test")).exists());

        System.out.println("listFiles: " + fs.listFiles(new AppPath("/testDir")));
        System.out.println("listFiles(recursion): " + fs.listFiles(new AppPath("/testDir"), true));

        System.out.println("copyFile: " + fs.copyFile(new AppPath("/testDir/sub/test.txt"), new AppPath("/testDir/copy/test.txt")));
        System.out.println("copyFile(recursion): " + fs.copyFile(new AppPath("/testDir"), new AppPath("/testDir1"), true));

        System.out.println("saveFile: " + fs.saveFile(new AppPath("/testDir/save/test.txt"), fs.getFile(new AppPath("/testDir/sub/test.txt"))));
        System.out.println("saveFile(recursion): " + fs.saveFile(new AppPath("/testDir2"), fs.getFile(new AppPath("/testDir")), true));

        System.out.println("moveFile: " + fs.moveFile(new AppPath("/testDir/sub/test.txt"), new AppPath("/testDir/move/test.txt")));
        System.out.println("moveFile(recursion): " + fs.moveFile(new AppPath("/testDir2"), new AppPath("/testDir3"), true));

        System.out.println("deleteFile: " + fs.deleteFile(new AppPath("/testDir1/sub/test.txt")));
        System.out.println("deleteFile(recursion): " + fs.deleteFile(new AppPath("/testDir"), true));
        System.out.println("deleteFile(recursion): " + fs.deleteFile(new AppPath("/testDir1"), true));
        System.out.println("deleteFile(recursion): " + fs.deleteFile(new AppPath("/testDir3"), true));
    }
}
