package ru.ir.visualiser.config;

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
    @Getter
    private static Config instance = new Config();

    private Config() {}

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