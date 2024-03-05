/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ffi.api.backoffice.dao;

import com.ffi.api.backoffice.model.ParameterLogin;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Dwi Prasetyo
 */
public interface ViewDao {

    List<Map<String, Object>> loginJson(ParameterLogin ref);
    ///////////////new method from dona 28-02-2023////////////////////////////

    List<Map<String, Object>> listSupplier(Map<String, Object> ref);

    /////////////////////////////done
    ///////////////new method from dona 03-03-2023////////////////////////////
    List<Map<String, Object>> listItemSupplier(Map<String, String> ref);

    List<Map<String, Object>> listMasterItem(Map<String, String> ref);

    List<Map<String, Object>> listDataItemSupplier(Map<String, String> ref);
    /////////////////////////////done

    ///////////////new method from dona 06-03-2023////////////////////////////
    List<Map<String, Object>> listMpcs(Map<String, String> ref);

    List<Map<String, Object>> listItemCost(Map<String, String> ref);
    /////////////////////////////done
    ///////////////new method from budhi 14-03-2023////////////////////////////

    List<Map<String, Object>> listPrice(Map<String, String> ref);

    List<Map<String, Object>> listMenuGroup(Map<String, String> ref);

    List<Map<String, Object>> listMenuGroupTipeOrder(Map<String, String> ref);
    
    List<Map<String, Object>> listMenuGroupOutletLimit(Map<String, String> ref);

    List<Map<String, Object>> listItemPrice(Map<String, String> ref);

    List<Map<String, Object>> listItemDetail(Map<String, String> ref);

    List<Map<String, Object>> listModifier(Map<String, String> ref);

    List<Map<String, Object>> listSpecialPrice(Map<String, String> ref);

    /////////////////////////////done 
    ///////////////new method from budhi 14-03-2023////////////////////////////
    List<Map<String, Object>> listMasterCity(Map<String, String> ref);

    List<Map<String, Object>> listPosition(Map<String, String> ref);

    List<Map<String, Object>> listMpcsHeader(Map<String, String> ref);

    List<Map<String, Object>> listMasterItemSupplier(Map<String, String> ref);

    /////////////////////////////done 
    ///////////////new method from asep 16-mar-2023 //////////////   
    List<Map<String, Object>> listOutlet(Map<String, String> ref);
    
    ///////////////new method for List Detail from adit 21-02-2024////////////////////////////
    List<Map<String, Object>> listOutletDetail(Map<String, String> ref);
    
    List<Map<String, Object>> listOutletDetailGroup(Map<String, String> ref);
    
    List<Map<String, Object>> listOutletDetailTypeOrder(Map<String, String> ref);

    //////// done adit 21-02-2024
    
    List<Map<String, Object>> listPos(Map<String, String> ref);

    List<Map<String, Object>> listTypePos(Map<String, String> ref);

    List<Map<String, Object>> listItem(Map<String, String> ref);

    /////////////////////////////done
    /////////////////new method from kevin 16-mar-2023 ////////////// 
    List<Map<String, Object>> listMenuGroups(Map<String, String> ref);

    List<Map<String, Object>> listItemMenus(Map<String, String> ref);
    ///////////////////////// done

    ////// new method by Dani 15-Feb-2024
    public List<Map<String, Object>> listItemMenusTipeOrder(Map<String, String> ref);
    ////// new method by Dani 15-Feb-2024
    public List<Map<String, Object>> listItemMenusLimit(Map<String, String> ref);
    ////// new method by Dani 15-Feb-2024
    public List<Map<String, Object>> listItemMenusSet(Map<String, String> ref);


    /////////////////new method from kevin 24-mar-2023 ////////////// 
    List<Map<String, Object>> listRecipeHeader(Map<String, String> ref);

    List<Map<String, Object>> listRecipeDetail(Map<String, String> ref);

    List<Map<String, Object>> listRecipeProduct(Map<String, String> ref);

    ///////////////////////// done
    ///////////////Updated By Pandu 14-03-2023////////////////////////////
    List<Map<String, Object>> listStaff(Map<String, String> ref);

    List<Map<String, Object>> listRegion(Map<String, String> ref);

    List<Map<String, Object>> listOutlets(Map<String, String> ref);

    List<Map<String, Object>> listViewFormStaff(Map<String, String> ref);

    List<Map<String, Object>> listViewPosition(Map<String, String> ref);

    List<Map<String, Object>> listViewAccessLevel(Map<String, String> ref);

    List<Map<String, Object>> listViewGroupUser(Map<String, String> ref);

    /////////////////////////////done
    //////////////////new method by Lani 29-03-2023////////////////////////// 
    List<Map<String, Object>> listSalesRecipe(Map<String, String> ref);

    List<Map<String, Object>> listSalesRecipeHeader(Map<String, String> ref);

    //////////Group Items by Kevin 29-03-2023
    List<Map<String, Object>> listMenuItem(Map<String, String> ref);

    List<Map<String, Object>> listGroupItem(Map<String, String> ref);

    //////////DONE
    // update outlet 29-03-23
    List<Map<String, Object>> viewArea(Map<String, String> ref);

    List<Map<String, Object>> viewTypeStore(Map<String, String> ref);

    //////////DONE
    // new method Dona outlet 30-03-23////////
    List<Map<String, Object>> listGlobal(Map<String, Object> ref);

    //////////DONE
    ///////////////NEW METHOD LIST COND AND DATA GLOBAL BY LANI 4 APRIL 2023////
    List<Map<String, Object>> listGlobalCond(Map<String, String> ref);

    List<Map<String, Object>> listMasterGlobal(Map<String, String> ref);

    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 14 APRIL 2023////
    List<Map<String, Object>> listOrderHeader(Map<String, String> ref);
    //////////DONE

    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 18 APRIL 2023////
    List<Map<String, Object>> listOrderHeaderAll(Map<String, String> ref);

    //////////DONE
    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 27 APRIL 2023////
    List<Map<String, Object>> listOrderDetail(Map<String, String> ref);

    //////////DONE
    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 2 MEI 2023////
    List<Map<String, Object>> listCounter(Map<String, String> ref);
    //////////DONE

    List<Map<String, Object>> ViewOrderDetail(Map<String, String> ref);

    List<Map<String, Object>> listItemDetailOpname(Map<String, String> ref);

    List<Map<String, Object>> listEditItemDetailOpname(Map<String, String> ref);

    List<Map<String, Object>> listHeaderOpname(Map<String, String> ref);

    public String cekOpname(String outletCode, String month, String year);

    public String cekItem();

    public String cekItemHq();

    public String getCity(String outletCode);

    public String getTransDate(String outletCode);

    ///////////////////////////////Add Receiving by KP (06-06-2023)///////////////////////////////
    List<Map<String, Object>> listReceivingHeader(Map<String, String> ref);

    List<Map<String, Object>> listUnfinishedOrderHeader(Map<String, String> ref);

    List<Map<String, Object>> listReceivingDetail(Map<String, String> ref);

    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 12 JUL 2023////
    List<Map<String, Object>> listOrderDetailOutlet(Map<String, String> ref);
    //////////DONE

    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 12 JUL 2023////
    List<Map<String, Object>> listOrderDetailSupplier(Map<String, String> ref);

    //////////DONE
    ///////////////NEW METHOD CEK DATA REPORT BY PASCA 13 JUL 2023////
    Integer cekDataReport(Map<String, Object> param, String name);

    ///////////////////////////////Add Wastage by KP (31-07-2023)///////////////////////////////
    List<Map<String, Object>> listWastageHeader(Map<String, String> ref);

    List<Map<String, Object>> listWastageDetail(Map<String, String> ref);

    List<Map<String, Object>> listOutletReport(Map<String, String> ref);
    //////////////////////////////List Stock Opname 8 AGUSTUS 2023///////////////////////////////

    List<Map<String, Object>> listStockOpname(Map<String, String> ref);

    ///////////////////////////////Add MPCS by KP (09-08-2023)///////////////////////////////
    List<Map<String, Object>> listTemplateMpcs(Map<String, String> ref);

    List<Map<String, Object>> listProjectMpcs(Map<String, String> ref);

    ///////////////////////////////Add Return Order by Pasca (10-08-2023)///////////////////////////////
    List<Map<String, Object>> listReturnOrderHeader(Map<String, String> param);

    List<Map<String, Object>> listReturnOrderDetail(Map<String, String> param);

    List<Map<String, Object>> listSupplierGudangReturnOrder(Map<String, String> param);

    List<Map<String, Object>> listItemSupplierGudangReturnOrder(Map<String, Object> param);

    ///////////////new method from dona 9-11-2023////////////////////////////
    List<Map<String, Object>> listDetailOderbyOrderno(Map<String, String> ref);

    /////////////////////////////done
    ////////////New method for query stock card - Fathur 29-Nov-2023////////////
    List<Map<String, Object>> listQueryStockCard(Map<String, String> ref);
    ////////////Done method for query stock card////////////

    ////////New method for query stock card detail - Fathur 30-Nov-2023////////////
    List<Map<String, Object>> listQueryStockCardDetail(Map<String, String> ref);
    ////////////Done method for query stock card detail////////////

    ////////////New METHODE DETAIL MENU GROUP - Dona 4 DEC 2023////////////
    List<Map<String, Object>> listMenuGroupCodeDetail(Map<String, String> ref);
    ////////////Done////////////

    ////////////New method for Last Eod - M. Joko 30-Nov-2023////////////
    List<Map<String, Object>> lastEod(Map<String, String> ref);
    ////////////Done method for last Eod ////////////

    ////////////New method for POS yg Open berdasarkan Outlet - M. Joko 30-Nov-2023////////////
    List<Map<String, Object>> listPosOpen(Map<String, String> ref);
    ////////////Done method for POS yg Open berdasarkan Outlet ////////////

    ////////////New method for M POS yg Aktif - M. Joko 30-Nov-2023////////////
    List<Map<String, Object>> listMPosActive(Map<String, String> ref);
    ////////////Done method for M POS yg Aktif ////////////

    ///////////////NEW METHOD LIST RECEIVING ALL HEADER BY DANI 12 DECEMBER 2023////
    List<Map<String, Object>> listReceivingAll(Map<String, String> ref);
    ///////////////NEW METHOD LIST MPCS PLAN 12 DECEMBER 2023////
    List<Map<String, Object>> listMpcsPlan(Map<String, String> ref);
    //////////////////////////DONE////////////////////////////

    // New Method MPCS Production List By Fathur 13 Dec 2023 //
    List<Map<String, Object>> mpcsProductionList(Map<String, String> ref) throws Exception;
    // Done Method MPCS Production List //

    // New Method MPCS Production Detail By Fathur 13 Dec 2023 //
    List<Map<String, Object>> mpcsProductionDetail(Map<String, String> ref);
    // Done Method MPCS Production Detail //

    // New Method MPCS Production Product Result By Fathur 13 Dec 2023 //
    List<Map<String, Object>> mpcsProductionProductResult(Map<String, String> ref);
    // Done Method MPCS Production ProductResult //

    // New Method MPCS Production Recipe By Fathur 13 Dec 2023 //
    List<Map<String, Object>> mpcsProductionRecipe(Map<String, String> ref);
    // Done Method MPCS Production Recipe //

    ///////////// NEW METHOD get order Detail From Inventory - Dani 19 Des 2023
    public List<Map<String, Object>> getOrderDetailFromInventory(Map<String, String> mapping);

    //////////// NEW METHOD list delivery Order header 20 Des 2023
    public List<Map<String, Object>> listDeliveryOrderHdr(Map<String, String> mapping);

    ///////// NEW METHOD get HO Outlet List - Dani 22 Des 2023
    public List<Map<String, Object>> listOutletHo(Map<String, String> mapping);

    ///////// NEW METHOD  check exists no request DO - Dani 19 Feb 2023
    public Boolean deliveryOrderCheckExistNoRequest(Map<String, String> ref);

    //////// NEW METHOD get Delivery Order By Dani 27 Des 2023
    public Map<String, Object> getDeliveryOrder(Map<String, String> mapping);

    ///// NEW METHOD TO GET LIST ORDER OUTLET TO OUTLET FROM WAREHOUSE BY DANI 28 DEC 2023
    public List<Map<String, Object>> getListOrderOutletHeaderWarehouse(Map<String, String> mapping);

    //////// NEW METHOD to get detail outlet to outlet from warehouse  BY DANI 28 DEC 2023
    public Map<String, Object> getOrderOutletWarehouse(Map<String, String> mapping);
    
    // Get Order Entry status from inv by Fathur 29 Dec 2023 // 
    public Map<String, Object> getOrderEntryStatusFromInv (Map<String, String> mapping);
    
    //////// NEW METHOD to get list daftar menu report by Rafi 29 Des 2023
    public List<Map<String, Object>> getListDaftarMenuReport();
    
    //////// NEW METHOD Digunakan untuk ambil data outlet di halaman login by M Joko - 4 Jan 2024
    public List<Map<String, Object>> outletInfo(String outletCode);

    /////// NEW METHOD to get list mpcs group by Dani 4 Januari 2024
    public List<Map<String, Object>> listMpcsGroup(Map<String, String> mapping);

    /////// NEW METHOD to get list mpcs query result by Dani 4 Januari 2024
    public  Map<String, Object>  listMpcsQueryResult(Map<String, String> mapping);

    /////// NEW METHOD to get list Menu Aplikasi by M Joko 8 Januari 2024
    public  List<Map<String, Object>>  listMenuApplication(Map<String, String> mapping);

    /////// NEW METHOD to get list Customer Name report by Dani 9 Januari 2024
    public List<Map<String, Object>> listCustomerNameReportDp();

    /////// NEW METHOD to get list order type report DP by Dani 9 Januari 2024
    public List<Map<String, Object>> listOrderTypeReportDp();

     //////// NEW METHOD to get list daftar menu report by Rafi 9 Jan 2024
    public List<Map<String, Object>> getListItemDetailReport(Map<String, String> balance);

    /////// NEW METHOD to mengambil data user absensi by id by M Joko 16 Jan 2024
    public  List<Map<String, Object>>  getIdAbsensi(Map<String, String> mapping);
    
    ///////////////new method list fryer from aditya 30-01-2024////////////////////////////
    List<Map<String, Object>> listFryer(Map<String, String> ref);
    ////// done 
    
    ///////////////new method list fryer from aditya 30-01-2024////////////////////////////
    List<Map<String, Object>> listManagementFryer(Map<String, String> ref);
    ////// done 
    
    ///////////////new method list fryer from aditya 30-01-2024////////////////////////////
    List<Map<String, Object>> listMpcsManagementFryer(Map<String, String> ref);
    ////// done 
    
    // =============== New Method From M Joko - 1 Feb 2024 ===============
    List<String> listLogger(Map<String, Object> ref);
    ////// done 
    
    // MPCS Production List Fryer 2 Feb 2024 //
    List<Map<String, Object>> mpcsProductionListFryer(Map<String, String> ref); 

    // =============== New Method From Sifa 05-02-2024 ===============
    List<Map<String, Object>> listWarehouseFSD(Map<String, String> ref);

    // =============== New Method From M Joko 19-02-2024 ===============
    List<Map<String, Object>> listTransferDataHistory(Map<String, String> ref);

    // =============== New Method From Sifa 20-02-2024 ===============
    List<Map<String, Object>> listmenuApplicationAccess(Map<String, String> ref);

    // =============== New Method From Sifa 21-02-2024 ===============
    List<Map<String, Object>> itemDetail(Map<String, String> ref);
    
    // Get order detail temporary list by Fathur 23 Feb 24
    List<Map<String, Object>> orderDetailTemporaryList(Map<String, String> ref);
    
}
