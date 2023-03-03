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
    ///////////////new method from dona 28-02-2023////////////////////////////

    public List<Map<String, Object>> listSupplier(Map<String, String> ref) {
        return viewDao.listSupplier(ref);
    }
    ///////////////done
    ///////////////new method from dona 03-03-2023////////////////////////////

    public List<Map<String, Object>> listItemSupplier(Map<String, String> ref) {
        return viewDao.listItemSupplier(ref);
    }

    public List<Map<String, Object>> listMasterItem(Map<String, String> ref) {
        return viewDao.listMasterItem(ref);

    }

    public List<Map<String, Object>> listDataItemSupplier(Map<String, String> ref) {
        return viewDao.listDataItemSupplier(ref);

    }
    ///////////////done
}
