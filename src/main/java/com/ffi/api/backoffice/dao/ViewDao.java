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
}
