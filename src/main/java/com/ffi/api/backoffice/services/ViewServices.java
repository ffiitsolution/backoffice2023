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
    public List<Map<String, Object>> listStaff(Map<String, String> ref) 
    {
        return viewDao.listStaff(ref);
    }
    // ========================================================== MODULE MASTER REGION (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW REGION
    public List<Map<String, Object>> listRegion(Map<String, String> ref) 
    {
        return viewDao.listRegion(ref);
    }
    // ========================================================== MODULE MASTER OUTLET (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW OUTLET
    public List<Map<String, Object>> listOutlets(Map<String, String> ref) 
    {
        return viewDao.listOutlets(ref);
    }
    // ========================================================== MODULE MASTER STAFF (M_STAFF) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listViewFormStaff(Map<String, String> ref) 
    {
        return viewDao.listViewFormStaff(ref);
    }  
    // ========================================================== MODULE MASTER GLOBAL (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listViewPosition(Map<String, String> ref) 
    {
        return viewDao.listViewPosition(ref);
    }   
    // ========================================================== MODULE MASTER GLOBAL (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listViewAccessLevel(Map<String, String> ref) 
    {
        return viewDao.listViewAccessLevel(ref);
    }   
    
    // ========================================================== MODULE MASTER MENU GROUP (M_MENUGRP) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listViewGroupUser(Map<String, String> ref) 
    {
        return viewDao.listViewGroupUser(ref);
    }     
    
    // ==================================================================================================================================================================================//
        ///////////////done
    
    //////////////////new method by Lani 29-03-2023//////////////////////////
    public List<Map<String, Object>> listSalesRecipe(Map<String, String> ref) {
        return viewDao.listSalesRecipe(ref);
    }
    
    //////////Group Items by Kevin 29-03-2023
    public List<Map<String, Object>> listMenuItem(Map<String, String> ref) {
        return viewDao.listMenuItem(ref);
    }
    
    public List<Map<String, Object>> listGroupItem(Map<String, String> ref) {
        return viewDao.listGroupItem(ref);
    }
    //////////DONE
     // update outlet 29-03-23
     public List<Map<String, Object>> viewArea(Map<String, String> ref) {
        return viewDao.viewArea(ref);
    }
     public List<Map<String, Object>> viewTypeStore(Map<String, String> ref) {
        return viewDao.viewTypeStore(ref);
    }
}
