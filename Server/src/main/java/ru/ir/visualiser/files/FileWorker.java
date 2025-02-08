package ru.ir.visualiser.files;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.cglib.beans.FixedKeySet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileWorker {

    public static void createPath(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String getFolderName(String filename) {
        return filename.substring(0, filename.lastIndexOf('.'));
    }

    public static String absolutePath(String path) {
        String absolutePath = Config.getInstance().getWorkPath();
        if (!path.isEmpty()) {
            absolutePath += File.separator + path;
        }
        return absolutePath;
    }

    public static void addFolder(String path, String folderName) {
        File newDir = new File(path + File.separator + folderName);
        if (!newDir.exists()) {
            newDir.mkdirs();
        }
    }

    public static void cleanDirectory(String path) throws IOException {
        File dir = new File(path);
        if (dir.exists()) {
            FileUtils.cleanDirectory(dir);
        }
    }

    public static void copy(String path, String fileName, byte[] bytes) throws IOException {
        File file = new File(path + File.separator + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(file));
        stream.write(bytes);
        stream.close();
    }
}
