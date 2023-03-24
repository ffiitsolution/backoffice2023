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
//    public void PostTesInsert(Map<String, String> mapping);
//    public void PostTesUpdate(Map<String, String> mapping);
//    public void PostTesDelete(Map<String, String> mapping);
    public void PostInsertStaff(Map<String, String> mapping);
    public void PostUpdateStaff(Map<String, String> mapping);
    public void PostDeleteStaff(Map<String, String> mapping);    
    // ==================================================================================================================================================================================//
    ///////////////Done////////////////////////////      
}
