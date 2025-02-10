package ru.ir.visualiser.files;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.cglib.beans.FixedKeySet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/** class for work with files.
 *
 */
public class FileWorker {

    /** init path.
     *
     * @param path path.
     */
    public static void createPath(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /** filename without .*.
     *
     * @param filename file
     * @return folder
     */
    public static String getFolderName(String filename) {
        return filename.substring(0, filename.lastIndexOf('.'));
    }

    /** abs path to workPath.
     *
     * @param path path
     * @return abs path in workpath
     */
    public static String absolutePath(String path) {
        String absolutePath = Config.getInstance().getWorkPath();
        if (!path.isEmpty()) {
            absolutePath += File.separator + path;
        }
        return absolutePath;
    }

    /** cleaning and deleting path.
     *
     * @param path path
     * @throws IOException error
     */
    public static void deleteDirectory(String path) throws IOException {
        File dir = new File(path);
        if (dir.exists()) {
            FileUtils.cleanDirectory(dir);
        }
        dir.delete();
    }

    /** copying context into new file.
     *
     *
     * @param path path
     * @param fileName res file
     * @param bytes context
     * @throws IOException error
     */
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
