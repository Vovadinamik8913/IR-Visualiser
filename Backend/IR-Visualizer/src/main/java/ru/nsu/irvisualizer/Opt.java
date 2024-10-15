package ru.nsu.irvisualizer;

import java.io.IOException;

/**
 * Wrapper around a `String` that represents path to opt command line tool.
 * Opt class is used to call opt command line tool.
 */
public final class Opt {
    private final String path;

    /**
     * Creates a new Opt object.
     *
     * @param path Path to opt command line tool
     */
    public Opt(String path) throws IllegalArgumentException {
        if (!validateOpt(path)) {
            throw new IllegalArgumentException("Invalid path to opt command line tool");
        }
        this.path = path;
    }

    /**
     * Checks if path to opt command line tool is valid
     * and if passed string is an opt command line tool.
     *
     * @param path Path to opt command line tool
     * @return True if opt is valid, false otherwise
     */
    public static boolean validateOpt(String path) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(new String[] {path, "--version"});
            boolean result = process.waitFor(1, java.util.concurrent.TimeUnit.SECONDS);
            process.destroyForcibly();
            return result;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    /**
     * Returns path to opt command line tool.
     *
     * @return Path to opt command line tool
     */
    public String getPath() {
        return path;
    }

    /**
     * Generates dot file from LLVM IR file.
     *
     * @param from Path to LLVM IR file
     * @param outputFile Path to output dot file
     * @return True if dot file was generated successfully, false otherwise
     * @throws IOException if an I/O error occurs when executing command
     */
    public boolean generateDotFile(String from, String outputFile) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(new String[] {path, "-passes=dot-cfg", from, "-o", outputFile});
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Interrupted while waiting for process to finish");
        }
        return process.exitValue() == 0;
    }
}
