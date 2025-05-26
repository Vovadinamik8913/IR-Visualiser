package ru.ir.visualiser.core.llvm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Svg {
    private static boolean generateSvg(Path file, File svgDir) throws IOException {
        String filename = file.getFileName().toString();
        String path = file.toAbsolutePath().toString();
        String dotPath = path.substring(path.indexOf("dot_files"), path.lastIndexOf(File.separator));
        String name = filename.substring(0, filename.lastIndexOf("."));
        ProcessBuilder processBuilder = new ProcessBuilder(
                "dot", "-Tsvg", "-o", name + ".svg",
                ".." + File.separator + dotPath + File.separator + filename
        );
        processBuilder.directory(svgDir);
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Interrupted while waiting for process to finish");
        }
        return process.exitValue() == 0;
    }

    public static boolean generateSvgFiles(String dotPath, String svgPath)
            throws IOException, RuntimeException {
        final List<Boolean> flag = new ArrayList<>();
        File dotDir = new File(dotPath);
        File svgDir = new File(svgPath);
        Files.list(dotDir.toPath())
                .filter(file -> file.toString().endsWith(".dot"))
                .forEach( file -> {
                    try {
                        flag.add(generateSvg(file, svgDir));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        return flag.stream().allMatch(val -> val);
    }
}
