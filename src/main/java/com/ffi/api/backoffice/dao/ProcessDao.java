/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ffi.api.backoffice.dao;

import com.ffi.api.backoffice.model.DetailOpname;
import com.ffi.api.backoffice.model.HeaderOpname;
import com.ffi.paging.ResponseMessage;
import com.google.gson.JsonObject;
import java.util.List;
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
    public Map<String,Object> insertOrderHeader(Map<String, String> mapping);

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

    public List updateOpnameStatus(Map<String, String> mapping);

    /////////////done
    ////////////New method for Transfer master outlet - Fathur 11 Dec 2023////////////
    public void sendDataOutletToWarehouse(Map<String, String> mapping);

    ////////////Done method for Transfer master outlet////////////
    ///////////////NEW METHOD insert POS 'N' EOD - M Joko 11/12/2023////////////////////////////
    public void insertEodPosN(Map<String, String> mapping);

    ///////////////NEW METHOD insert T Stock Card EOD - M Joko 11/12/2023////////////////////////////
    public void insertTStockCard(Map<String, String> mapping);

    ///////////////NEW METHOD insert T EOD HIST - M Joko 12/12/2023////////////////////////////
    public void insertTEodHist(Map<String, String> mapping);

    ///////////////NEW METHOD increase trans_date M Outlet selesai EOD - M Joko 11/12/2023////////////////////////////
    public void increaseTransDateMOutlet(Map<String, String> mapping);

    ///////////////NEW METHOD insert T SUMM MPCS - M Joko 14/12/2023//// 
    public void insertTSummMpcs(Map<String, String> mapping);

    ///////////////NEW METHOD UPDATE MPCS PLAN 12 DECEMBER 2023////
    public void updateMpcsPlan(Map<String, String> mapping);

    //////////////////////////DONE////////////////////////////

    ///////////////NEW METHOD insert T SUMM MPCS - M Joko 27/12/2023//// 
    public void updateOrderEntryExpired(Map<String, String> mapping);
    
    ///////////////NEW METHOD kirim ulang return order - M Joko 27/12/2023//// 
    public boolean sendReturnOrderToWH(JsonObject object);
    
    ///////////// NEW METHOD INSERT OR UPDATE DELIVERY ORDER by Dani 27 Dec 2023
    public void insertUpdateDeliveryOrder(Map<String, Object> mapping) throws Exception;

    public void kirimDeloveryOrder(Map<String, String> mapping) throws Exception;

    // Insert MPCS Production - Fathur 8 Jan 2024 // 
    public boolean insertMpcsProduction(Map<String, String> params) throws Exception;
    
    // Delete MPCS Production - Fathur 11 Jan 2024 // 
    public boolean deleteMpcsProduction(Map<String, String> params) throws Exception;

    // Add Counter Print Receiving Dani 11 Jan 2024
    public void addCounterPrintReceiving(Map<String, Object> params);
    
    // Add Counter Print Order Entry adit 16 Jan 2024
    public void addCounterPrintOrderEntry(Map<String, Object> params);
    
    // Add menyimpan data user absensi by id by M Joko 16 Jan 2024
    public boolean insertAbsensi(Map<String, Object> params);
    
    // Add MPCS Management Fryer by Aditya 29 Jan 2024   
    void insertMpcsManagementFryer(JsonObject param);
    ///////////////Done aditya 29-01-2024////////////////////////////
    
    //============== New Method From Lukas 17-10-2023 ================
    public boolean insertDataLocal(String tableName, String date);
    public boolean sendDataLocal(String tableName, String date, String outletId);
    //============== End Method From Lukas 17-10-2023 ================

    //============== Delete order entry detail by Dani 2 Feb 2024
    public void deleteOrderEntryDetail(Map<String, Object> param);
    
    //============== New Method From M Joko 1-2-2024 ================
    ResponseMessage listTransferData(Map<String, Object> ref);
    
    //============== New Method From M Joko 5-2-2024 ================
    ResponseMessage processBackupDb(Map<String, Object> ref);
    
    //============== New Method From M Joko 13-2-2024 ================
    ResponseMessage updateRecipe(Map<String, Object> ref);
    
    // Remove empty qty order entry by Fathur 15 Feb 2024 //
    public void removeEmptyOrder(Map<String, String> mapping);
}
