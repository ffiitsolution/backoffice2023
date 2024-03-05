package com.ffi.api.backoffice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component()
public class FileLoggerUtil {

    public final String ACTIVITY_LOG = "ActivityLog";
    public final String SUCCESS = "SUCCESS";
    public final String FAILED = "FAILED";
    private static final String LOG_FOLDER_PATH = "logs/";
    private static String currentUserCode = "";
    private static String currentModule = "";

    private static String objectToJson(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            return "";
        }
    }

    public synchronized static void log(String module, String action, String value, String result, String note, String userCode) {
        currentUserCode = userCode;
        currentModule = module;
        try {
            String fileName = generateFileName();
            String filePath = getModuleFolderPath(module) + fileName;
            try ( FileWriter fileWriter = new FileWriter(filePath, true)) {
                String logString = createLogEntry(action, value, result, note);
                fileWriter.write(logString + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error logger: " + e.getMessage());
        }
    }

    public synchronized static void log(String module, String message, String userCode) {
        currentUserCode = userCode;
        currentModule = module;
        try {
            String fileName = generateFileName();
            String filePath = getModuleFolderPath(module) + fileName;
            try ( FileWriter fileWriter = new FileWriter(filePath, true)) {
                String logString = createSimpleLogEntry(message);
                fileWriter.write(logString + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error logger: " + e.getMessage());
        }
    }

    public synchronized void logActivity(String url, String module, String action, String userCode, String userName, Boolean success, String query, Map<String, Object> params) {
        currentUserCode = userCode;
        try {
            String fileName = generateFileName("yyyy-MM");
            String filePath = getModuleFolderPath(ACTIVITY_LOG) + fileName;
            try ( FileWriter fileWriter = new FileWriter(filePath, true)) {
                String logString = createActivityLogEntry(url, module, action, userCode, userName, success, query, params);
                fileWriter.write(logString + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error logger: " + e.getMessage());
        }
    }

    public static List<String> readAllLogs(String module) {
        List logs = new ArrayList<>();
        String folderPath = getModuleFolderPath(module);

        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".logger")) {
                    Map f = new HashMap();
                    f.put("filename", file.getName());
                    double fileSizeMB = file.length() / (1024.0 * 1024);
                    double freeSpaceGB = file.getFreeSpace() / (1024.0 * 1024 * 1024);
                    double totalSpaceGB = file.getTotalSpace() / (1024.0 * 1024 * 1024);
                    DecimalFormat df = new DecimalFormat("#.##");
                    f.put("fileSizeMB", df.format(fileSizeMB));
                    f.put("freeSpaceGB", df.format(freeSpaceGB));
                    f.put("totalSpaceGB", df.format(totalSpaceGB));
                    f.put("lastModified", formatDate(file.lastModified(),"yyyy-MM-dd HH:mm:ss"));
                    logs.add(f);
                    // logs.addAll(readLogsFromFile(file));
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

    public static List<String> readActivityLogsFromFile(String module, String fileName) {
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
        try ( BufferedReader br = new BufferedReader(new FileReader(file))) {
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
        String currentDate = getCurrentTimestamp("yyyy-MM-dd");
        return currentDate + ".logger";
    }

    private static String generateFileName(String pattern) {
        return getCurrentTimestamp(pattern) + ".logger";
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
        return "{ \"timestamp\": \"" + getCurrentTimestamp() + "\", \"action\": \"" + action
                + "\", \"value\": \"" + value + "\", \"result\": \"" + result
                + "\", \"note\": \"" + note + "\" }";
    }

    private static String createSimpleLogEntry(String message) {
        return "{ \"timestamp\": \"" + getCurrentTimestamp() + "\", \"message\": \"" + message + "\" }";
    }

    private static String createLogWithActionAndMessage(String action, String message) {
        return "{ \"time\": \"" + getCurrentTimestamp() + "\", \"action\": \"" + action
                + "\", \"message\": \"" + message + "\" }";
    }

    private static String createActivityLogEntry(String url, String module, String action, String staffCode, String staffName, Boolean success, String query, Map<String, Object> params) {
        Map map = new HashMap();
        map.put("dateUpd", getCurrentTimestamp("dd-MMM-yyyy"));
        map.put("time", getCurrentTimestamp("HHmmss"));
        map.put("url", url);
        map.put("module", module);
        map.put("action", action);
        map.put("staffCode", staffCode);
        map.put("staffName", staffName);
        map.put("success", success);
        map.put("query", query);
        map.put("params", params);
        return objectToJson(map);
    }

    private static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private static String getCurrentTimestamp(String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }
    
    private static String formatDate(long timestamp, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(timestamp);
    }
}
