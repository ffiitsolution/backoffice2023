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

    public List<Map<String, Object>> listSupplier(Map<String, Object> ref) {
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

    public List<Map<String, Object>> listMenuGroupTipeOrder(Map<String, String> ref) {
        return viewDao.listMenuGroupTipeOrder(ref);
    }

    public List<Map<String, Object>> listMenuGroupOutletLimit(Map<String, String> ref) {
        return viewDao.listMenuGroupOutletLimit(ref);
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
        return viewDao.listMenuGroups(ref);
    }

    public List<Map<String, Object>> listItemMenus(Map<String, String> ref) {
        return viewDao.listItemMenus(ref);
    }
    ////////////////done

    /////// new method by Dani 15-Feb-2024
    public List<Map<String, Object>> listItemMenusTipeOrder(Map<String, String> ref) {
        return viewDao.listItemMenusTipeOrder(ref);
    }

    //// new method by Dani 15-Feb-2024
    public List<Map<String, Object>> listItemMenusLimit(Map<String, String> ref) {
        return viewDao.listItemMenusLimit(ref);
    }

    //// new method by Dani 15-Feb-2024
    public List<Map<String, Object>> listItemMenusSet(Map<String, String> ref) {
        return viewDao.listItemMenusSet(ref);
    }

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
    public List<Map<String, Object>> listStaff(Map<String, String> ref) {
        return viewDao.listStaff(ref);
    }

    // ========================================================== MODULE MASTER REGION (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW REGION
    public List<Map<String, Object>> listRegion(Map<String, String> ref) {
        return viewDao.listRegion(ref);
    }

    // ========================================================== MODULE MASTER OUTLET (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW OUTLET
    public List<Map<String, Object>> listOutlets(Map<String, String> ref) {
        return viewDao.listOutlets(ref);
    }

    // ========================================================== MODULE MASTER STAFF (M_STAFF) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listViewFormStaff(Map<String, String> ref) {
        return viewDao.listViewFormStaff(ref);
    }

    // ========================================================== MODULE MASTER GLOBAL (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listViewPosition(Map<String, String> ref) {
        return viewDao.listViewPosition(ref);
    }

    // ========================================================== MODULE MASTER GLOBAL (M_GLOBAL) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listViewAccessLevel(Map<String, String> ref) {
        return viewDao.listViewAccessLevel(ref);
    }

    // ========================================================== MODULE MASTER MENU GROUP (M_MENUGRP) =========================================================================================//
    //PERCOBAAN VIEW SELECT
    public List<Map<String, Object>> listViewGroupUser(Map<String, String> ref) {
        return viewDao.listViewGroupUser(ref);
    }

    // ==================================================================================================================================================================================//
    ///////////////done
    //////////////////new method by Lani 29-03-2023//////////////////////////
    public List<Map<String, Object>> listSalesRecipe(Map<String, String> ref) {
        return viewDao.listSalesRecipe(ref);
    }

    public List<Map<String, Object>> listSalesRecipeHeader(Map<String, String> ref) {
        return viewDao.listSalesRecipeHeader(ref);
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
    ///////////////done
    //////////////////new method by Dona 30-03-2023//////////////////////////

    public List<Map<String, Object>> listGlobal(Map<String, String> ref) {
        return viewDao.listGlobal(ref);
    }

    ///////////////done 
    ///////////////NEW METHOD LIST COND AND DATA GLOBAL BY LANI 4 APRIL 2023////
    public List<Map<String, Object>> listGlobalCond(Map<String, String> ref) {
        return viewDao.listGlobalCond(ref);
    }

    public List<Map<String, Object>> listMasterGlobal(Map<String, String> ref) {
        return viewDao.listMasterGlobal(ref);
    }

    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 14 APRIL 2023////
    public List<Map<String, Object>> listOrderHeader(Map<String, String> ref) {
        return viewDao.listOrderHeader(ref);
    }

    ///////////////done 
    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 18 APRIL 2023////
    public List<Map<String, Object>> listOrderHeaderAll(Map<String, String> ref) {
        return viewDao.listOrderHeaderAll(ref);
    }

    ///////////////done 
    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 27 APRIL 2023////
    public List<Map<String, Object>> listOrderDetail(Map<String, String> ref) {
        return viewDao.listOrderDetail(ref);
    }

    ///////////////done 
    ///////////////NEW METHOD LIST COUNTER BY DONA 2 MEI 2023////
    public List<Map<String, Object>> listCounter(Map<String, String> ref) {
        return viewDao.listCounter(ref);
    }
    ///////////////done 

    public List<Map<String, Object>> ViewOrderDetail(Map<String, String> ref) {
        return viewDao.ViewOrderDetail(ref);
    }

    public List<Map<String, Object>> listItemDetailOpname(Map<String, String> ref) {
        return viewDao.listItemDetailOpname(ref);
    }

    public List<Map<String, Object>> listEditItemDetailOpname(Map<String, String> ref) {
        return viewDao.listEditItemDetailOpname(ref);
    }

    public List<Map<String, Object>> listHeaderOpname(Map<String, String> ref) {
        return viewDao.listHeaderOpname(ref);
    }

    public String cekOpname(String outletCode, String month, String year) {
        return viewDao.cekOpname(outletCode, month, year);
    }

    public String getCity(String outletCode) {
        return viewDao.getCity(outletCode);
    }

    public String cekItem() {
        return viewDao.cekItem();
    }

    public String cekItemHq() {
        return viewDao.cekItemHq();
    }

    ///////////////////////////////Add Receiving by KP (06-06-2023)///////////////////////////////
    public List<Map<String, Object>> listReceivingHeader(Map<String, String> ref) {
        return viewDao.listReceivingHeader(ref);
    }

    public List<Map<String, Object>> listUnfinishedOrderHeader(Map<String, String> ref) {
        return viewDao.listUnfinishedOrderHeader(ref);
    }

    public List<Map<String, Object>> listReceivingDetail(Map<String, String> ref) {
        return viewDao.listReceivingDetail(ref);
    }

    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 12 jul 2023////
    public List<Map<String, Object>> listOrderDetailOutlet(Map<String, String> ref) {
        return viewDao.listOrderDetailOutlet(ref);
    }
    ///////////////done 

    ///////////////NEW METHOD LIST ORDER HEADER SUPPLIER BY DONA 13 jul 2023////
    public List<Map<String, Object>> listOrderDetailSupplier(Map<String, String> ref) {
        return viewDao.listOrderDetailSupplier(ref);
    }

    ///////////////done
    ///////////////NEW METHOD CEK DATA REPORT BY PASCA 13 JUL 2023////
    public Integer cekDataReport(Map<String, Object> param, String name) {
        return viewDao.cekDataReport(param, name);
    }

    ///////////////////////////////Add Wastage by KP (31-07-2023)///////////////////////////////
    public List<Map<String, Object>> listWastageHeader(Map<String, String> ref) {
        return viewDao.listWastageHeader(ref);
    }

    public List<Map<String, Object>> listWastageDetail(Map<String, String> ref) {
        return viewDao.listWastageDetail(ref);
    }

    public List<Map<String, Object>> listOutletReport(Map<String, String> ref) {
        return viewDao.listOutletReport(ref);
    }
    ///////////////////////////////List Stock Opname 7 AUG 2023///////////////////////////////

    public List<Map<String, Object>> listStockOpname(Map<String, String> ref) {
        return viewDao.listStockOpname(ref);

    }
    /////////////////////Done

    ///////////////////////////////Add MPCS by KP (09-08-2023)///////////////////////////////
    public List<Map<String, Object>> listTemplateMpcs(Map<String, String> ref) {
        return viewDao.listTemplateMpcs(ref);
    }

    public List<Map<String, Object>> listProjectMpcs(Map<String, String> ref) {
        return viewDao.listProjectMpcs(ref);
    }

    ///////////////done
    ///////////////////////////////Add Return Order by Pasca (10-08-2023)///////////////////////////////
    public List<Map<String, Object>> listReturnOrderHeader(Map<String, String> param) {
        return viewDao.listReturnOrderHeader(param);
    }

    public List<Map<String, Object>> listReturnOrderDetail(Map<String, String> param) {
        return viewDao.listReturnOrderDetail(param);
    }

    public List<Map<String, Object>> listSupplierGudangReturnOrder(Map<String, String> param) {
        return viewDao.listSupplierGudangReturnOrder(param);
    }

    public List<Map<String, Object>> listItemSupplierGudangReturnOrder(Map<String, Object> param) {
        return viewDao.listItemSupplierGudangReturnOrder(param);
    }

    ///////////////new method from dona 9-11-2023////////////////////////////
    public List<Map<String, Object>> listDetailOderbyOrderno(Map<String, String> ref) {
        return viewDao.listDetailOderbyOrderno(ref);
    }
    ///////////////done

    ////////////New method for query stock card - Fathur 29-Nov-2023////////////
    public List<Map<String, Object>> listQueryStockCard(Map<String, String> ref) {
        return viewDao.listQueryStockCard(ref);
    }
    ////////////Done method for query stock card////////////

    ////////////New method for query stock card detail - Fathur 30-Nov-2023////////////
    public List<Map<String, Object>> listQueryStockCardDetail(Map<String, String> ref) {
        return viewDao.listQueryStockCardDetail(ref);
    }
    ////////////Done method for query stock card detail////////////

    ////////////New METHOD LIST MENU GROUP CODE BY DONA 4 DEC 2023////////////
    public List<Map<String, Object>> listMenuGroupCodeDetail(Map<String, String> ref) {
        return viewDao.listMenuGroupCodeDetail(ref);

        ////////////Done method for query stock card detail////////////
    }

    ////////////New method for query EOD terakhir Outlet - M Joko 4-Dec-2023////////////
    public List<Map<String, Object>> lastEod(Map<String, String> ref) {
        return viewDao.lastEod(ref);
    }
    ////////////Done method for query EOD terakhir Outlet ////////////

    ////////////New method for query POS yg Open - M Joko 4-Dec-2023////////////
    public List<Map<String, Object>> listPosOpen(Map<String, String> ref) {
        return viewDao.listPosOpen(ref);
    }
    ////////////Done method for query POS yg Open berdasarkan Outlet////////////

    ////////////New method for query M POS yg Aktif - M Joko 12-Dec-2023////////////
    public List<Map<String, Object>> listMPosActive(Map<String, String> ref) {
        return viewDao.listMPosActive(ref);
    }
    ////////////Done method for query M POS yg Aktif ////////////

    ///////////////NEW METHOD LIST RECEIVING BY DANI 12 DECEMBER 2023////
    public List<Map<String, Object>> listReceivingAll(Map<String, String> ref) {
        return viewDao.listReceivingAll(ref);
    }

    ////////////New LIST MPCS PLAN BY DONA 12-Dec-2023////////////
    public List<Map<String, Object>> listMpcsPlan(Map<String, String> ref) {
        return viewDao.listMpcsPlan(ref);
    }
    ////////////Done  ////////////

    // New Method List MPCS Production - Fathur 13 Dec 2023 //
    public List<Map<String, Object>> listMpcsProduction(Map<String, String> ref) {
        return viewDao.mpcsProductionList(ref);
    }
    // Done List MPCS Production //
    
    // New Method MPCS Production Detail - Fathur 13 Dec 2023 //
    public List<Map<String, Object>> mpcsProductionDetail(Map<String, String> ref) {
        return viewDao.mpcsProductionDetail(ref);
    }
    // Done MPCS Production Detail //
    
    // New Method MPCS Production Product Result - Fathur 13 Dec 2023 //
    public List<Map<String, Object>> mpcsProductionProductResult(Map<String, String> ref) {
        return viewDao.mpcsProductionProductResult(ref);
    }
    // Done MPCS Production Product Result //
    
    // New Method MPCS Production Recipe - Fathur 13 Dec 2023 //
    public List<Map<String, Object>> mpcsProductionRecipe(Map<String, String> ref) {
        return viewDao.mpcsProductionRecipe(ref);
    }
    // Done MPCS Production Recipe //

        ///////////// NEW METHOD get order Detail From Inventory - Dani 19 Des 2023
    public List<Map<String, Object>> getOrderDetailFromInventory(Map<String, String> balancing) {
        return viewDao.getOrderDetailFromInventory(balancing);
    }

    public List<Map<String, Object>> listDeliveryOrderHdr(Map<String, String> mapping) {
        return viewDao.listDeliveryOrderHdr(mapping);
    }

    ///////// NEW METHOD get HO Outlet List - Dani 22 Des 2023
    public List<Map<String, Object>> listOutletHo(Map<String, String> ref) {
        return viewDao.listOutletHo(ref);
    }

    //////// NEW METHOD get Delivery Order By Dani 27 Des 2023
    public Map<String, Object> getDeliveryOrder(Map<String, String> mapping) {
        return viewDao.getDeliveryOrder(mapping);
    }
    
    ///// NEW METHOD TO GET LIST ORDER OUTLET TO OUTLET FROM WAREHOUSE BY DANI 28 DEC 2023
    public List<Map<String, Object>> getListOrderOutletHeaderWarehouse(Map<String, String> mapping) {
        return viewDao.getListOrderOutletHeaderWarehouse(mapping);
    }

    //////// NEW METHOD to get detail outlet to outlet from warehouse  BY DANI 28 DEC 2023
    public Map<String, Object> getOrderOutletWarehouse(Map<String, String> mapping) {
        return viewDao.getOrderOutletWarehouse(mapping);
    }

    //////// NEW METHOD to get list report daftar menu  by Rafi 29 Des 2023
    public List<Map<String, Object>> getListDaftarMenuReport() {
        return viewDao.getListDaftarMenuReport();
    }
    
    public Map<String, Object> getOrderEntryStatusFromInv(Map<String, String> mapping) {
        return viewDao.getOrderEntryStatusFromInv(mapping);
    }
    
    //////// NEW METHOD Digunakan untuk ambil data outlet di halaman login by M Joko - 4 Jan 2024
    public List<Map<String, Object>> outletInfo(String outletCode) {
        return viewDao.outletInfo(outletCode);
    }

    /////// NEW METHOD to get list mpcs group by Dani 4 Januari 2024
    public List<Map<String, Object>> listMpcsGroup(Map<String, String> mapping) {
        return viewDao.listMpcsGroup(mapping);
    }

    /////// NEW METHOD to get mpcs query result by Dani 4 Januari 2024
    public Map<String, Object> listMpcsQueryResult(Map<String, String> mapping) {
        return viewDao.listMpcsQueryResult(mapping);
    }
    
    /////// NEW METHOD to get list Customer Name report DP by Dani 9 Januari 2024
    public List<Map<String, Object>> listCustomerNameReportDp() {
        return viewDao.listCustomerNameReportDp();
    }

    /////// NEW METHOD to get list order type report DP by Dani 9 Januari 2024
    public List<Map<String, Object>> listOrderTypeReportDp() {
        return viewDao.listOrderTypeReportDp();
    }

    //////// NEW METHOD to get list report daftar menu  by Rafi 9 Jan 2024
    public List<Map<String, Object>> getListItemDetailReport() {
        return viewDao.getListItemDetailReport();
    }

    /////// NEW METHOD to mengambil data user absensi by id by M Joko 16 Jan 2024
    public List<Map<String, Object>> getIdAbsensi(Map<String, String> mapping) {
        return viewDao.getIdAbsensi(mapping);
    }
    
    ///////////////new method list fryer from aditya 30-01-2024////////////////////////////
    public List<Map<String, Object>> listFryer(Map<String, String> ref) {
        return viewDao.listFryer(ref);
    }
    
    ///////////////new method list fryer from aditya 30-01-2024////////////////////////////
    public List<Map<String, Object>> listManagementFryer(Map<String, String> ref) {
        return viewDao.listManagementFryer(ref);
    }
    
    ///////////////new method list fryer from aditya 30-01-2024////////////////////////////
    public List<Map<String, Object>> listMpcsManagementFryer(Map<String, String> ref) {
        return viewDao.listMpcsManagementFryer(ref);
    }
    
    // =============== New Method From M Joko - 1 Feb 2024 ===============
    public List<String> listLogger(Map<String, Object> ref) {
        return viewDao.listLogger(ref);
    }

    // MPCS Production List Fryer 2 Feb 2024 //
    public List<Map<String, Object>> mpcsProductionListFryer(Map<String, String> ref) {
        return viewDao.mpcsProductionListFryer(ref);
    }

    // =============== New Method From Sifa 05-02-2024 ===============
    public List<Map<String, Object>> listWarehouseFSD(Map<String, String> ref) {
        return viewDao.listWarehouseFSD(ref);
    }

    // =============== New Method From M Joko 19-02-2024 ===============
    public List<Map<String, Object>> listTransferDataHistory(Map<String, String> ref) {
        return viewDao.listTransferDataHistory(ref);
    }
}
