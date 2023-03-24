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

    public List<Map<String, Object>> listPrice(Map<String, String> ref) {
        return viewDao.listPrice(ref);
    }

    public List<Map<String, Object>> listMenuGroup(Map<String, String> ref) {
        return viewDao.listMenuGroup(ref);
    }

    public List<Map<String, Object>> listItemPrice(Map<String, String> ref) {
        return viewDao.listItemPrice(ref);
    }

    public List<Map<String, Object>> listItemDetail(Map<String, String> ref) {
        return viewDao.listItemDetail(ref);
    }

    public List<Map<String, Object>> listModifier(Map<String, String> ref) {
        return viewDao.listModifier(ref);
    }

    public List<Map<String, Object>> listSpecialPrice(Map<String, String> ref) {
        return viewDao.listSpecialPrice(ref);
    }
    ///////////////done

    ///////////////new method from dona 14-03-2023//////////////////////////// 
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
     ///////////////new method from asep 16-03-2023////////////////////////////
        public List<Map<String, Object>> listOutlet(Map<String, String> ref) {
        return viewDao.listOutlet(ref);
    }
    public List<Map<String, Object>> listPos(Map<String, String> ref) {
        return viewDao.listPos(ref);
    }
    public List<Map<String, Object>> listTypePos(Map<String, String> ref) {
        return viewDao.listTypePos(ref);
    }
    public List<Map<String, Object>> listItem(Map<String, String> ref) {
        return viewDao.listItem(ref);
    }
     ///////////////done
    
    //////////////////new method from kevin 16-03-2023////////////////////////////
    public List<Map<String, Object>> listMenuGroups(Map<String, String> ref) {
        return viewDao.listMenuGroup(ref);
    }
    public List<Map<String, Object>> listItemMenus(Map<String, String> ref) {
        return viewDao.listItemMenus(ref);
    }
    ////////////////done
    
    //////////////////new method from kevin 24-03-2023////////////////////////////
    public List<Map<String, Object>> listRecipeHeader(Map<String, String> ref) {
        return viewDao.listRecipeHeader(ref);
    }
    
    public List<Map<String, Object>> listRecipeDetail(Map<String, String> ref) {
        return viewDao.listRecipeDetail(ref);
    }
    
    public List<Map<String, Object>> listRecipeProduct(Map<String, String> ref) {
        return viewDao.listRecipeProduct(ref);
    }
    ////////////////done
         ///////////////Updated By Pandu 14-03-2023////////////////////////////
    // ========================================================== MODULE MASTER STAFF (M_STAFF) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listUserStaff(Map<String, String> ref) 
    {
        return viewDao.listUserStaff(ref);
    }
    // ========================================================== MODULE MASTER REGION (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW REGION
    public List<Map<String, Object>> listUserRegion(Map<String, String> ref) 
    {
        return viewDao.listUserRegion(ref);
    }
    // ========================================================== MODULE MASTER OUTLET (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW OUTLET
    public List<Map<String, Object>> listUserOutlet(Map<String, String> ref) 
    {
        return viewDao.listUserOutlet(ref);
    }
    // ========================================================== MODULE MASTER STAFF (M_STAFF) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listUserFormStaff(Map<String, String> ref) 
    {
        return viewDao.listUserFormStaff(ref);
    }  
    // ========================================================== MODULE MASTER GLOBAL (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listCity(Map<String, String> ref) 
    {
        return viewDao.listCity(ref);
    }    
    // ========================================================== MODULE MASTER GLOBAL (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listPositionUser(Map<String, String> ref) 
    {
        return viewDao.listPositionUser(ref);
    }   
    // ========================================================== MODULE MASTER GLOBAL (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listAccessLevel(Map<String, String> ref) 
    {
        return viewDao.listAccessLevel(ref);
    }   
    
    // ========================================================== MODULE MASTER MENU GROUP (M_MENUGRP) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listGroupUser(Map<String, String> ref) 
    {
        return viewDao.listGroupUser(ref);
    }     
    
    // ==================================================================================================================================================================================//
        ///////////////done
    
    ///////////////Updated By Pandu 16-03-2023////////////////////////////
    // ========================================================== MODULE MASTER SALES RECIPE (M_SALES_RECIPE, M_RECIPE_HEADER, M_RECIPE_DETAIL) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listSalesRecipe(Map<String, String> ref) 
    {
        return viewDao.listSalesRecipe(ref);
    }
    ///////////////Done////////////////////////////  
}
