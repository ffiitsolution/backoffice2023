/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.services;

import com.ffi.api.backoffice.dao.ProcessDao;
import com.ffi.api.backoffice.model.DetailOpname;
import com.ffi.api.backoffice.model.HeaderOpname;
import com.ffi.paging.ResponseMessage;
import com.google.gson.JsonObject;
import java.util.List;
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
    public void updateFrayer(Map<String, String> balance) {
        dao.updateFrayer(balance);
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

    ///////////////new method updateStatusOpname 6-11-2023////////////////////////////
    public List updateOpnameStatus(Map<String, String> balance) {
        return dao.updateOpnameStatus(balance);
    }

    ///////////////done///////////////
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
    public void insertReturnOrderHeaderDetail(JsonObject param) {
        dao.insertReturnOrderHeaderDetail(param);
    }

    ///////////////NEW METHOD UPDATE TEMPLATE STOCK OPNAME 15 AUGG 2023////   
    public void updateTemplateStockOpnameHeader(Map<String, String> balance) {
        dao.updateTemplateStockOpnameHeader(balance);
    }

    public void updateTemplateStockOpnameDetail(Map<String, String> balance) {
        dao.updateTemplateStockOpnameDetail(balance);
    }

    public void deleteTempLateDetail(Map<String, String> balance) {
        dao.deleteTempLateDetail(balance);
    }

    public void insertMasterItem(JsonObject balancing) {
        dao.insertMasterItem(balancing);
    }
    ///////////////////////done

    ///////////////NEW METHOD PROCES TRANSFER MASTER  26 SEP 2023////   
    public void processTransferMasters(JsonObject balancing) {
        dao.processTransferMasters(balancing);
    }
    ///////////////////////done

    public void sendDataOutletToWarehouse(Map<String, String> balance) {
        dao.sendDataOutletToWarehouse(balance);
    }

    ///////////////NEW METHOD POS 'N' EOD - M Joko 11/12/2023////   
    public void insertEodPosN(Map<String, String> balancing) {
        dao.insertEodPosN(balancing);
    }

    ///////////////NEW METHOD insert T Stock Card EOD - M Joko 11/12/2023////   
    public void insertTStockCard(Map<String, String> balancing) {
        dao.insertTStockCard(balancing);
    }

    ///////////////NEW METHOD insert T EOD HIST - M Joko 12/12/2023////   
    public void insertTEodHist(Map<String, String> balancing) {
        dao.insertTEodHist(balancing);
    }

    ///////////////NEW METHOD increase trans_date M Outlet selesai EOD - M Joko 11/12/2023////   
    public void increaseTransDateMOutlet(Map<String, String> balancing) {
        dao.increaseTransDateMOutlet(balancing);
    }

    ///////////////NEW METHOD insert T SUMM MPCS - M Joko 14/12/2023////   
    public void insertTSummMpcs(Map<String, String> balancing) {
        dao.insertTSummMpcs(balancing);
    }

    ///////////////NEW METHOD UPDATE MPCS PLAN BY DONA 14 DEC 2023////   
    public void updateMpcsPlan(Map<String, String> balancing) {
        dao.updateMpcsPlan(balancing);
    }
    ////////////////////////DONE///////////////////////

    //////////// NEW METHOD INSERT DELIVERY ORDER BY DANI 27 DEC 2023
    public void insertUpdateDeliveryOrder(Map<String, Object> resource) throws Exception {
        dao.insertUpdateDeliveryOrder(resource);
    }

    public void kirimDeliveryOrder(Map<String, String> resource) throws Exception {
        dao.kirimDeloveryOrder(resource);
    }

    ///////////////NEW METHOD update t_order_header jika expired setelah End of Day - M Joko 27/12/2023////   
    public void updateOrderEntryExpired(Map<String, String> balancing) {
        dao.updateOrderEntryExpired(balancing);
    }

    ///////////////NEW METHOD kirim ulang return order - M Joko 27/12/2023////   
    public boolean sendReturnOrderToWH(JsonObject object) {
        return dao.sendReturnOrderToWH(object);
    }

    // Insert MPCS Production - Fathur 8 Jan 2024 //   
    public boolean insertMpcsProduction(Map<String, String> params) throws Exception {
        return dao.insertMpcsProduction(params);
    }
    // Done Insert MPCS Production //   

    // Insert MPCS Production - Fathur 11 Jan 2024 //   
    public boolean deleteMpcsProduction(Map<String, String> params) throws Exception {
        return dao.deleteMpcsProduction(params);
    }
    // Done Insert MPCS Production //   

    // Add Counter Print Receiving Dani 11 Jan 2024
    public void addCounterPrintReceiving(Map<String, Object> param) {
        dao.addCounterPrintReceiving(param);
    }
    
    // Add Counter Print Order Entry Adit 16 Jan 2024
    public void addCounterPrintOrderEntry(Map<String, Object> param) {
        dao.addCounterPrintOrderEntry(param);
    }
    
    // Add menyimpan data user absensi by id by M Joko 16 Jan 2024
    public boolean insertAbsensi(Map<String, Object> param) {
        return dao.insertAbsensi(param);
    }
    
    ///////////////new method MPCS Management Fryer from aditya 29-01-2024////////////////////////////
    public void insertMpcsManagementFryer(JsonObject param) {
        dao.insertMpcsManagementFryer(param);
    }
    ////////////////////done

    //============== New Method Copy Data From Lukas 17-10-2023 ================
    public boolean insertDataLocal(String tableName, String date) {
        return dao.insertDataLocal(tableName,date);
    }
    public boolean sendDataLocal(String tableName, String date, String outletId) {
        return dao.sendDataLocal(tableName,date, outletId);
    }
    //============== End Method Copy Data From Lukas 17-10-2023 ================

    // Delete order detail by DANI 2 Feb 2024 //
    public void deleteOrderEntryDetail(Map<String, Object> ref) {
        dao.deleteOrderEntryDetail(ref);
    }
    
    //============== New Method From M Joko 1-2-2024 ================
    public List<Map<String, Object>> listTransferData(Map<String, Object> ref) {
        return dao.listTransferData(ref);
    }
    
    //============== New Method From M Joko 5-2-2024 ================
    public ResponseMessage processBackupDb(Map<String, Object> ref) {
        return dao.processBackupDb(ref);
    }
}
