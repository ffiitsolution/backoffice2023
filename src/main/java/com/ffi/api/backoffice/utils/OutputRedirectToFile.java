package com.ffi.api.backoffice.utils;

/**
 *
 * @author USER
 *
 * added by M Joko - 7/3/24 digunakan untuk menulis error yg tampil di console
 * ke file untuk mempermudah debugging. File log akan kosong kembali setelah
 * TRUNCATE_AFTER_DAYS hari
 *
 *
 */
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class OutputRedirectToFile {

    private static final int TRUNCATE_AFTER_DAYS = 2;
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;
    private static PrintStream fileOut;
    private static PrintStream fileErr;

    public static void redirectOutputToFile(String filePath) {
        try {
            File file = new File(filePath);
            boolean fileExists = file.exists();
            fileOut = new PrintStream(new FileOutputStream(file, true));
            fileErr = new PrintStream(new FileOutputStream(file, true));

            if (!fileExists || needsTruncation(filePath)) {
                truncateLogFile(filePath);
            }

            PrintStream customOut = new PrintStream(new CombinedOutputStream(originalOut, fileOut));
            PrintStream customErr = new PrintStream(new CombinedOutputStream(originalErr, fileErr));

            System.setOut(customOut);
            System.setErr(customErr);
        } catch (FileNotFoundException e) {
            System.err.println("Error occurred while redirecting output to file: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Security Exception occurred: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public static void restoreOutput() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        if (fileOut != null) {
            fileOut.close();
        }
        if (fileErr != null) {
            fileErr.close();
        }
    }

    private static class CombinedOutputStream extends OutputStream {

        private OutputStream out1;
        private OutputStream out2;

        CombinedOutputStream(OutputStream out1, OutputStream out2) {
            this.out1 = out1;
            this.out2 = out2;
        }

        @Override
        public void write(int b) throws IOException {
            out1.write(b);
            out2.write(b);
        }

        @Override
        public void flush() throws IOException {
            out1.flush();
            out2.flush();
        }

        @Override
        public void close() throws IOException {
            out1.close();
            out2.close();
        }
    }

    private static boolean needsTruncation(String filePath) {
        try {
            Instant creationTime = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class).creationTime().toInstant();
            LocalDateTime fileCreationDateTime = LocalDateTime.ofInstant(creationTime, ZoneId.systemDefault());

            Duration duration = Duration.between(fileCreationDateTime, LocalDateTime.now());
            return duration.toDays() >= TRUNCATE_AFTER_DAYS;
        } catch (IOException e) {
            System.err.println("Error occurred while retrieving file creation time: " + e.getMessage());
            return false;
        }
    }

    private static void truncateLogFile(String filePath) {
        try {
            if (fileOut != null) {
                fileOut.close();
            }
            if (fileErr != null) {
                fileErr.close();
            }

            Files.deleteIfExists(Paths.get(filePath));
            Files.createFile(Paths.get(filePath));
            System.out.println("Log file truncated.");

            fileOut = new PrintStream(new FileOutputStream(filePath, true));
            fileErr = new PrintStream(new FileOutputStream(filePath, true));
        } catch (IOException e) {
            System.err.println("Error occurred while truncating log file: " + e.getMessage());
        }
    }
}
