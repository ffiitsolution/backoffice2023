/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ffi.api.backoffice.dao;

import com.ffi.api.backoffice.model.DetailOpname;
import com.ffi.api.backoffice.model.HeaderOpname;
import com.google.gson.JsonObject;
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
    public void updateFrayer(Map<String, String> mapping);

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
    ///////////////NEW METHOD INSERT ORDER HEADER BY DONA 14 APRIL 2023////
    public void insertOrderHeader(Map<String, String> mapping);

    /////////////////////////////DONE///////////////////////////////////////////
    ///////////////NEW METHOD INSERT ORDER DETAIL BY DONA 27 APRIL 2023////
    public void insertOrderDetail(Map<String, String> mapping);

    public void updateOrderDetail(Map<String, String> mapping);

    /////////////////////////////DONE///////////////////////////////////////////
    ///////////////NEW METHOD UPDATE COUNTER BY DONA 3 MAY 2023////////////////////////////
    public void updateMasterCounter(Map<String, String> mapping);

    /////////////done
    public void updateMCounter(Map<String, String> mapping);

    public void inserOpnameHeader(HeaderOpname mapping);

    public void updateMCounterSop(String transType, String outletCode);

    public void inserOpnameDetail(DetailOpname opnameDtls);

    public void insertSoToScDtl(Map<String, String> mapping);

    public void insertScDtlToScHdr(Map<String, String> mapping);

    public void sendDataToWarehouse(Map<String, String> mapping);

    //Add Insert to Receiving Header & Detail by KP (07-06-2023)
    public void InsertRecvHeaderDetail(JsonObject mapping);

    //Add Insert to Wastage Header & Detail by KP (03-08-2023)
    public void InsertWastageHeaderDetail(JsonObject mapping);

    //Insert MPCS by Kevin (08-08-2023)
    public void InsertMPCSTemplate(JsonObject mapping);

    public void InsertUpdateMPCSProject(JsonObject mapping);

    //Insert Return Order by Pasca (15-08-2023)
    void insertReturnOrderHeaderDetail(JsonObject param);

    ///////////////NEW METHOD UPDATE TEMPLATE STOCK OPNAME BY DONA 15 AUG 2023////////////////////////////
    public void updateTemplateStockOpnameHeader(Map<String, String> mapping);

    public void updateTemplateStockOpnameDetail(Map<String, String> mapping);

    public void deleteTempLateDetail(Map<String, String> mapping);

    public void insertMasterItem(JsonObject mapping);

    public void processTransferMasters(JsonObject mapping);
    /////////////done
    ///////////////new method updateStatusOpname 6-11-2023////////////////////////////

    public void updateOpnameStatus(Map<String, String> mapping);

    /////////////done
    
      ///////////////NEW METHOD CANCEL ORDER 15 NOV 2023 ///////////////
        public void updateCancelOrder(Map<String, String> mapping);

    /////////////done
}
