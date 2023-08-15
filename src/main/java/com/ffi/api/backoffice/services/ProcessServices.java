/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.services;

import com.ffi.api.backoffice.dao.ProcessDao;
import com.ffi.api.backoffice.model.DetailOpname;
import com.ffi.api.backoffice.model.HeaderOpname;
import com.google.gson.JsonObject;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author IT
 */
@Service
public class ProcessServices {

    @Autowired
    ProcessDao dao;
    ///////////////new method from dona 27-02-2023////////////////////////////

    public void insertSupplier(Map<String, String> balance) {
        dao.insertSupplier(balance);
    }
    ////////////////////done

    ///////////////new method from dona 28-02-2023////////////////////////////
    public void updateSupplier(Map<String, String> balance) {
        dao.updateSupplier(balance);
    }

    ///////////////////////done
    ///////////////new method from dona 03-03-2023////////////////////////////
    public void insertItemSupplier(Map<String, String> balance) {
        dao.insertItemSupplier(balance);
    }

    public void updateItemSupplier(Map<String, String> balance) {
        dao.updateItemSupplier(balance);
    }

    ///////////////////////done
    ///////////////new method from dona 06-03-2023////////////////////////////
    public void updateMpcs(Map<String, String> balance) {
        dao.updateMpcs(balance);
    }
    ///////////////////////done
    ///////////////new method from asep 16-mar-2023 //////////////  

    public void insertPos(Map<String, String> balance) {
        dao.insertPos(balance);
    }

    public void updatePos(Map<String, String> balance) {
        dao.updatePos(balance);
    }
    ///////////////////////done
    ///////////////Updated By Pandu 14-03-2023////////////////////////////
// ========================================================== MODULE MASTER STAFF (M_STAFF) =============================================================================================//    
    //PERCOBAAN INSERT DATA MASTER STAFF (M_STAFF)

    public void insertStaff(Map<String, String> balancetest1) {
        dao.insertStaff(balancetest1);
    }

    //PERCOBAAN UPDATE DATA MASTER STAFF (M_STAFF)
    public void updateStaff(Map<String, String> balancetest) {
        dao.updateStaff(balancetest);

    }

    //PERCOBAAN DELETE DATA MASTER STAFF (M_STAFF)
    public void deleteStaff(Map<String, String> balancetest) {
        dao.deleteStaff(balancetest);
    }

    ///////////////new method from Dona 30-mar-2023 //////////////  
    public void insertFryer(Map<String, String> balance) {
        dao.insertFryer(balance);
    }
    ///////////////////////done

    ///////////////NEW METHOD LIST COND AND DATA GLOBAL BY LANI 4 APRIL 2023////
    public void insertMasterGlobal(Map<String, String> balance) {
        dao.insertMasterGlobal(balance);
    }

    public void updateMasterGlobal(Map<String, String> balance) {
        dao.updateMasterGlobal(balance);
    }

    ///////////////////////done
    ///////////////NEW METHOD INSERT ORDER HEADER 14 APRIL 2023////
    @Transactional
    public void insertOrderHeader(Map<String, String> balance) {
        dao.insertOrderHeader(balance);
    }

    ///////////////////////done
    ///////////////NEW METHOD INSERT ORDER HEADER 14 APRIL 2023////
    @Transactional
    public void insertOrderDetail(Map<String, String> balance) {
        dao.insertOrderDetail(balance);
    }

    @Transactional
    public void updateOrderDetail(Map<String, String> balance) {
        dao.updateOrderDetail(balance);
    }

    ///////////////////////done
    ///////////////NEW METHOD UPDATE COUNTER 3 MAY 2023////   
    public void updateMasterCounter(Map<String, String> balance) {
        dao.updateMasterCounter(balance);
    }

    ///////////////////////done
    @Transactional
    public void updateMCounter(Map<String, String> balance) {
        dao.updateMCounter(balance);
    }

    @Transactional
    public void inserOpnameHeader(HeaderOpname balance) {
        dao.inserOpnameHeader(balance);
    }

    public void updateMCounterSop(String transType, String outletCode) {
        dao.updateMCounterSop(transType, outletCode);
    }

    @Transactional
    public void inserOpnameDetail(DetailOpname opnameDtls) {
        dao.inserOpnameDetail(opnameDtls);
    }

    @Transactional
    public void insertSoToScDtl(Map<String, String> balance) {
        dao.insertSoToScDtl(balance);
    }

    @Transactional
    public void insertScDtlToScHdr(Map<String, String> balance) {
        dao.insertScDtlToScHdr(balance);
    }

    @Transactional
    public void sendDataToWarehouse(Map<String, String> balance) {
        dao.sendDataToWarehouse(balance);
    }

    //Add Insert to Receiving Header & Detail by KP (07-06-2023)
    @Transactional
    public void InsertRecvHeaderDetail(JsonObject balancing) {
        dao.InsertRecvHeaderDetail(balancing);
    }

    //Add Insert to Wastage Header & Detail by KP (07-06-2023)
    @Transactional
    public void InsertWastageHeaderDetail(JsonObject balancing) {
        dao.InsertWastageHeaderDetail(balancing);
    }

    //Insert MPCS by Kevin (08-08-2023)
    public void InsertMPCSTemplate(JsonObject balancing) {
        dao.InsertMPCSTemplate(balancing);
    }

    public void InsertUpdateMPCSProject(JsonObject balancing) {
        dao.InsertUpdateMPCSProject(balancing);
    }

    //Add Insert to returnOrder Header & Detail by Pasca (15-08-2023)
    public void insertReturnOrderHeaderDetail(Map<String, Object> param) {
        dao.insertReturnOrderHeaderDetail(param);
    }

    ///////////////NEW METHOD UPDATE TEMPLATE STOCK OPNAME 15 AUGG 2023////   
     public void updateTemplateStockOpnameHeader(Map<String, String> balance) {
        dao.updateTemplateStockOpnameHeader(balance);
    }
        public void updateTemplateStockOpnameDetail(Map<String, String> balance) {
        dao.updateTemplateStockOpnameDetail(balance);
    }

    ///////////////////////done
}
