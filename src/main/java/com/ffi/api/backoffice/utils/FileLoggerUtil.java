package com.ffi.api.backoffice.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileLoggerUtil {

    private static final String LOG_FOLDER_PATH = "logs/";
    private static String currentUserCode;

    public synchronized static void log(String module, String action, String value, String result, String note, String userCode) {
        currentUserCode = userCode;
        try {
            String fileName = generateFileName();
            String filePath = getModuleFolderPath(module) + fileName;
            try (FileWriter fileWriter = new FileWriter(filePath, true)) {
                String logString = createLogEntry(action, value, result, note);
                fileWriter.write(logString + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error logger: " + e.getMessage());
        }
    }

    public synchronized static void log(String module, String message, String userCode) {
        currentUserCode = userCode;
        try {
            String fileName = generateFileName();
            String filePath = getModuleFolderPath(module) + fileName;
            try (FileWriter fileWriter = new FileWriter(filePath, true)) {
                String logString = createSimpleLogEntry(message);
                fileWriter.write(logString + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error logger: " + e.getMessage());
        }
    }

    public synchronized static void log(String module, String action, String message, String userCode) {
        currentUserCode = userCode;
        try {
            String fileName = generateFileName();
            String filePath = getModuleFolderPath(module) + fileName;
            try (FileWriter fileWriter = new FileWriter(filePath, true)) {
                String logString = createLogWithActionAndMessage(action, message);
                fileWriter.write(logString + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error logger: " + e.getMessage());
        }
    }

    public static List<String> readAllLogs(String module) {
        List<String> logs = new ArrayList<>();
        String folderPath = getModuleFolderPath(module);

        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".logger")) {
                    logs.addAll(readLogsFromFile(file));
                }
            }
        }

        return logs;
    }

    public static List<String> readLogsFromFile(String module, String fileName) {
        List<String> logs = new ArrayList<>();
        String filePath = getModuleFolderPath(module) + fileName;
        File file = new File(filePath);
        if (file.exists() && file.isFile() && file.getName().endsWith(".logger")) {
            logs.addAll(readLogsFromFile(file));
        }
        return logs;
    }

    private static List<String> readLogsFromFile(File file) {
        List<String> logs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                logs.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error logger: " + e.getMessage());
        }
        return logs;
    }

    private static String generateFileName() {
        String currentDate = getCurrentDate();
        return currentDate + "_" + currentUserCode + ".logger";
    }

    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    private static String getModuleFolderPath(String module) {
        String folderPath = LOG_FOLDER_PATH + module + "/";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folderPath;
    }

    private static String createLogEntry(String action, String value, String result, String note) {
        return "{ \"timestamp\": \"" + getCurrentTimestamp() + "\", \"action\": \"" + action +
               "\", \"value\": \"" + value + "\", \"result\": \"" + result +
               "\", \"note\": \"" + note + "\" }";
    }

    private static String createSimpleLogEntry(String message) {
        return "{ \"timestamp\": \"" + getCurrentTimestamp() + "\", \"message\": \"" + message + "\" }";
    }

    private static String createLogWithActionAndMessage(String action, String message) {
        return "{ \"timestamp\": \"" + getCurrentTimestamp() + "\", \"action\": \"" + action +
               "\", \"message\": \"" + message + "\" }";
    }

    private static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
