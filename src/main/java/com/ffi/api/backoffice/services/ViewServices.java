/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.services;

import com.ffi.api.backoffice.dao.ViewDao;
import com.ffi.api.backoffice.model.ParameterLogin;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dwi Prasetyo
 */
@Service
public class ViewServices {
    
    @Autowired
    ViewDao viewDao;
    
    public List<Map<String, Object>> loginJson(ParameterLogin ref) {
        return viewDao.loginJson(ref);
    }
    
}
