/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.utils;

import org.springframework.beans.factory.annotation.Autowired;
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
}
