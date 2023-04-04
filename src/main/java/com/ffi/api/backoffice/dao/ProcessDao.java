/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ffi.api.backoffice.dao;

import java.util.Map;

/**
 *
 * @author IT
 */
public interface ProcessDao {

    public void insertSupplier(Map<String, String> mapping);
    ///////////////new method from dona 28-02-2023////////////////////////////

    public void updateSupplier(Map<String, String> mapping);

    /////////////done
    ///////////////new method from dona 03-03-2023////////////////////////////
    public void insertItemSupplier(Map<String, String> mapping);

    public void updateItemSupplier(Map<String, String> mapping);

    /////////////done
    ///////////////new method from dona 06-03-2023////////////////////////////
    public void updateMpcs(Map<String, String> mapping);

    /////////////done
    ///////////////new method from asep 16-mar-2023 ////////////// 
    public void insertPos(Map<String, String> mapping);

    public void updatePos(Map<String, String> mapping);

    //done
    ///////////////Updated By Pandu 14-03-2023////////////////////////////
    // ========================================================== MODULE MASTER STAFF (M_STAFF) =========================================================================================//
    public void insertStaff(Map<String, String> mapping);

    public void updateStaff(Map<String, String> mapping);

    public void deleteStaff(Map<String, String> mapping);

    //public void updateStaff(Map<String, String> mapping);  
    // ==================================================================================================================================================================================//
    ///////////////Done////////////////////////////      
    ///////////////new method from Dona 30-03-2023////////////////////////////
    public void insertFryer(Map<String, String> mapping);
    ///////////////Done////////////////////////////      
    ///////////////NEW METHOD LIST COND AND DATA GLOBAL BY LANI 4 APRIL 2023////
    public void insertMasterGlobal(Map<String, String> mapping);
    
    public void updateMasterGlobal(Map<String, String> mapping);
    /////////////////////////////DONE///////////////////////////////////////////
}
