package ru.ir.visualiser.files;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import ru.ir.visualiser.config.LocalConfig;

import java.io.*;

/** class for work with files.
 *
 */
public class FileWorker {


    /**
     * Gets next available filename by adding a number suffix if file already exists
     *
     * @param path Base directory path
     * @param filename Original filename
     * @return Modified filename with number suffix if needed
     */
    public static String getNextAvailableFilename(String path, String filename) {
        String baseName = filename.substring(0, filename.lastIndexOf('.'));
        String extension = filename.substring(filename.lastIndexOf('.'));
        File file = new File(path + File.separator + baseName);

        if (!file.exists()) {
            return filename;
        }

        int count = 1;
        while (true) {
            String newFilename = baseName + "(" + count + ")";
            file = new File(path + File.separator + newFilename);
            if (!file.exists()) {
                return newFilename + extension;
            }
            count++;
        }
    }

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
    public static String absolutePath(LocalConfig config, String path) {
        String absolutePath = config.getWorkPath();
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
     * @param stream content
     * @throws IOException error
     */
    public static void copy(String path, String fileName, InputStream stream) throws IOException {
        File file = new File(path + File.separator + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();
    }
}
