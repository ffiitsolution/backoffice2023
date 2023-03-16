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

    ///////////////new method from dona 06-03-2023////////////////////////////
    public List<Map<String, Object>> listMpcs(Map<String, String> ref) {
        return viewDao.listMpcs(ref);

    }

    public List<Map<String, Object>> listItemCost(Map<String, String> ref) {
        return viewDao.listItemCost(ref);

    }
    ///////////////done
    ///////////////new method from budhi 14-03-2023//////////////////////////// 

    public List<Map<String, Object>> ListPrice(Map<String, String> ref) {
        return viewDao.ListPrice(ref);
    }

    public List<Map<String, Object>> ListMenuGroup(Map<String, String> ref) {
        return viewDao.ListMenuGroup(ref);
    }

    public List<Map<String, Object>> ListItemPrice(Map<String, String> ref) {
        return viewDao.ListItemPrice(ref);
    }

    public List<Map<String, Object>> ListItemDetail(Map<String, String> ref) {
        return viewDao.ListItemDetail(ref);
    }

    public List<Map<String, Object>> ListModifier(Map<String, String> ref) {
        return viewDao.ListModifier(ref);
    }

    public List<Map<String, Object>> ListSpecialPrice(Map<String, String> ref) {
        return viewDao.ListSpecialPrice(ref);
    }
    ///////////////done

    ///////////////new method from cona 14-03-2023//////////////////////////// 
    public List<Map<String, Object>> listMasterCity(Map<String, String> ref) {
        return viewDao.listMasterCity(ref);

    }

    public List<Map<String, Object>> listPosition(Map<String, String> ref) {
        return viewDao.listPosition(ref);
    }

    public List<Map<String, Object>> listMpcsHeader(Map<String, String> ref) {
        return viewDao.listMpcsHeader(ref);
    }

    public List<Map<String, Object>> listMasterItemSupplier(Map<String, String> ref) {
        return viewDao.listMasterItemSupplier(ref);

    }
    ///////////////done
     // outlet & item (Asep)16-03-2023    
        public List<Map<String, Object>> Outlet(Map<String, String> ref) {
        return viewDao.Outlet(ref);
    }
    public List<Map<String, Object>> listPos(Map<String, String> ref) {
        return viewDao.listPos(ref);
    }
    public List<Map<String, Object>> typepos(Map<String, String> ref) {
        return viewDao.typepos(ref);
    }
    public List<Map<String, Object>> item(Map<String, String> ref) {
        return viewDao.item(ref);
    }
     ///////////////done
}
