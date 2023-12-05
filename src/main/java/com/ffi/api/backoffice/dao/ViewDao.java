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

    List<Map<String, Object>> listSupplier(Map<String, String> ref);

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

    List<Map<String, Object>> listPos(Map<String, String> ref);

    List<Map<String, Object>> listTypePos(Map<String, String> ref);

    List<Map<String, Object>> listItem(Map<String, String> ref);

    /////////////////////////////done
    /////////////////new method from kevin 16-mar-2023 ////////////// 
    List<Map<String, Object>> listMenuGroups(Map<String, String> ref);

    List<Map<String, Object>> listItemMenus(Map<String, String> ref);
    ///////////////////////// done

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
    List<Map<String, Object>> listGlobal(Map<String, String> ref);

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
    List<Map<String, Object>> lastEodByOutlet(Map<String, String> ref);
    ////////////Done method for last Eod ////////////

    ////////////New method for POS yg Open berdasarkan Outlet - M. Joko 30-Nov-2023////////////
    List<Map<String, Object>> eodPosOpened(Map<String, String> ref);
    ////////////Done method for POS yg Open berdasarkan Outlet ////////////

}
