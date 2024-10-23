package ru.ir.visualiser;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Svg {
    private final File dotDir;
    private final File svgDir;

    public Svg() throws IOException {
        dotDir = new ClassPathResource("dot_files").getFile();
        svgDir = new ClassPathResource("svg_files").getFile();
    }


    private void cleanDirectory() throws IOException {
        if (svgDir.exists()) {
            FileUtils.cleanDirectory(svgDir);
        }
    }

    private boolean generateSvg(Path file) throws IOException {
        String filename = file.getFileName().toString();
        String name = filename.substring(0, filename.lastIndexOf("."));
        ProcessBuilder processBuilder = new ProcessBuilder(
                "dot", "-Tsvg", "-o", name + ".svg", "../dot_files/" + filename
        );
        processBuilder.directory(svgDir);
        processBuilder.inheritIO();
        Process process = processBuilder.start();try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Interrupted while waiting for process to finish");
        }
        return process.exitValue() == 0;
    }

    public boolean generateSvgFiles() throws IOException, RuntimeException {
        cleanDirectory();
        final List<Boolean> flag = new ArrayList<>();
        Files.list(dotDir.toPath())
                .filter(file -> file.toString().endsWith(".dot"))
                .forEach( file -> {
                    try {
                        flag.add(generateSvg(file));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        return flag.stream().allMatch(val -> val);
    }
}
