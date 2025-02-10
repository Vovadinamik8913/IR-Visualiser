package ru.ir.visualiser.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;


/** config singleton.
 * contains opts and workPath
 *
 */
public class Config {
    @Getter
    private String[] optsPath;
    @Getter
    private String workPath;
    private static Config instance;

    private Config() {}

    /** get singleton.
     *
     * @return instance
     */
    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    /** fill config.
     *
     * @param filename source file
     * @throws IOException no such file
     */
    public static void deserializeFromJson(String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        instance = objectMapper.readValue(new File(filename), Config.class);
    }

}