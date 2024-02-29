/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.utils;

import com.ffi.api.backoffice.dao.impl.ProcessDaoImpl;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 * 
 * added by M Joko - 17/01/24
 * dapat digunakan untuk mengambil nilai di application properties
 * 
 * 
 * String env = appUtil.getOrDefault("app.env", "production");
 */

@Component
public class AppUtil implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String get(String key) {
        return environment.getProperty(key);
    }
    
    public String getOrDefault(String key, String defaultValue) {
        String value = environment.getProperty(key);
        return (value != null) ? value : defaultValue;
    }
    
    public double getDiskFreeSpace() {
        double freeSpaceGB = -1;
        try {
            String location = AppUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            String jarFilePath = URLDecoder.decode(location, "UTF-8");
            if (jarFilePath.startsWith("/") && jarFilePath.contains(":/")) {
                jarFilePath = jarFilePath.substring(1);
            }
            if (jarFilePath.startsWith("file:/")) {
                int jarIndex = jarFilePath.lastIndexOf(".jar");
                int endIndex = jarFilePath.indexOf("!/", jarIndex);
                if (jarIndex != -1 && endIndex != -1) {
                    jarFilePath = jarFilePath.substring(6, endIndex);
                }
            }
            System.out.println("getDiskFreeSpace: " + jarFilePath);
            Path jarPath = Paths.get(jarFilePath).toRealPath();
            for (FileStore store : FileSystems.getDefault().getFileStores()) {
                for (Path root : FileSystems.getDefault().getRootDirectories()) {
                    if (jarPath.startsWith(root)) {
                        long freeSpace = store.getUsableSpace();
                        freeSpaceGB = freeSpace / (1024.0 * 1024.0 * 1024.0);
                        return freeSpaceGB;
                    }
                }
            }
        } catch (IOException  ex) {
            System.out.println("getDiskFreeSpace: " + ex.getMessage());
        }
        return freeSpaceGB;
    }
}
