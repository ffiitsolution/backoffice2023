/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.utils;

import com.ffi.api.backoffice.dao.impl.ProcessDaoImpl;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
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
    
    public double getDiskFreeSpace(){
        double freeSpaceGB = 0;
        try {
            String jarFilePath = AppUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            Path jarPath = new File(jarFilePath).toPath();
            FileStore fileStore = Files.getFileStore(jarPath);
            long freeSpace = fileStore.getUsableSpace();
            freeSpaceGB = freeSpace / (1024.0 * 1024.0 * 1024.0);
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(ProcessDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return freeSpaceGB;
    }
}
