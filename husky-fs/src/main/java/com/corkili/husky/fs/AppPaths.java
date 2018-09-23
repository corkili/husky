package com.corkili.husky.fs;

import com.corkili.husky.exception.AppIOException;

public class AppPaths {

    public static void normalizePath(AppPath path, boolean isSeparatorFsEqualApp, String rootPath,
                                       String fsSeparator, String appSeparator) throws AppIOException {
        if (path == null) {
            throw new AppIOException("Path is null");
        }
        if (path.isNormalized()) {
            return;
        }
        if (isSeparatorFsEqualApp) {
            normalizePath(path, rootPath, fsSeparator);
        } else {
            normalizePath(path, rootPath, fsSeparator, appSeparator);
        }
    }

    private static void normalizePath(AppPath path, String rootPath, String fsSeparator, String appSeparator) {
        normalizePath(path.setPath(path.getPath().replaceAll(appSeparator, fsSeparator)), rootPath, fsSeparator);
    }

    private static void normalizePath(AppPath path, String rootPath, String separator) {
        String resPath = rootPath;
        if (!path.getPath().startsWith(separator)) {
            resPath += separator;
        }
        resPath += path.getPath();
        if (resPath.endsWith(separator)) {
            resPath = resPath.substring(0, resPath.length() - 1);
        }
        path.setPath(resPath);
        path.normalized();
    }

}
