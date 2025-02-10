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

    public static void deleteDirectory(String path) throws IOException {
        File dir = new File(path);
        if (dir.exists()) {
            FileUtils.cleanDirectory(dir);
        }
        dir.delete();
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
