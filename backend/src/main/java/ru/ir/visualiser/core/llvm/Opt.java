package ru.ir.visualiser.core.llvm;

import ru.ir.visualiser.model.Ir;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Opt {

    public static boolean generateDotFiles(String opt, String dotPath, String filename) throws IOException {
        File dotDir = new File(dotPath);
        ProcessBuilder processBuilder = new ProcessBuilder(
                opt,
                "-passes=dot-cfg",
                ".." + File.separator + filename);
        processBuilder.directory(dotDir);
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Interrupted while waiting for process to finish");
        }
        return process.exitValue() == 0;
    }

    public static boolean validateOpt(String opt) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(new String[]{opt, "--version"});
            boolean result = process.waitFor(1, java.util.concurrent.TimeUnit.SECONDS);
            process.destroyForcibly();
            return result;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    public static boolean optimizeOpt(String opt, Ir parent, Ir result) throws IOException {
        File dir = new File(result.getIrPath());
        ProcessBuilder processBuilder = new ProcessBuilder(
                opt,
                result.getFlags(), "-S", ".." + File.separator + parent.getFilename(), "-o",
                result.getFilename());
        processBuilder.directory(dir);
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Interrupted while waiting for process to finish");
        }
        return process.exitValue() == 0;
    }

    /**
     * Calls `opt -passes=print<analysis> /path/to/ir -disable-output` and returns output.
     */
    private static String print(String opt, Ir ir, String analysis) throws IOException {
        String pathToIr = ir.getIrPath() + File.separator + ir.getFilename();
        ProcessBuilder processBuilder = new ProcessBuilder(
                opt,
                "-passes=print<" + analysis + ">", pathToIr, "-disable-output");
        processBuilder.redirectErrorStream(true); // Перенаправляем stderr в stdout

        Process process = processBuilder.start();

        // Читаем вывод процесса
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        // Ожидаем завершения процесса
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Process was interrupted", e);
        }

        return output.toString();
    }

    public static String printScev(String opt, Ir ir) throws IOException {
        return print(opt, ir, "scalar-evolution");
    }

    public static String printMemoryssa(String opt, Ir ir) throws IOException {
        return print(opt, ir, "memoryssa");
    }

    /**
     * Prints info about loops by starting opt as a process.
     *
     * @param opt - path to opt
     *
     * @param ir - Ir, which loop info is needed
     *
     * @return Loop info from opt
     *
     * @throws IOException if couldnt start a process.
     */
    public static String printLoops(String opt, Ir ir) throws IOException {
        return print(opt, ir, "loops");
    }

    /**
     * Prints info about domtree by starting opt as a process.
     *
     * @param opt - path to opt
     *
     * @param ir - Ir, which domtree info is needed
     *
     * @return Loop info from opt
     *
     * @throws IOException if couldnt start a process.
     */
    public static String printDomtree(String opt, Ir ir) throws IOException {
        return print(opt, ir, "domtree");
    }
}
