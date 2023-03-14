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

    List<Map<String, Object>> ListPrice(Map<String, String> ref);

    List<Map<String, Object>> ListMenuGroup(Map<String, String> ref);

    List<Map<String, Object>> ListItemPrice(Map<String, String> ref);

    List<Map<String, Object>> ListItemDetail(Map<String, String> ref);

    List<Map<String, Object>> ListModifier(Map<String, String> ref);

    List<Map<String, Object>> ListSpecialPrice(Map<String, String> ref);
    /////////////////////////////done 
    ///////////////new method from budhi 14-03-2023////////////////////////////
    List<Map<String, Object>> listMasterCity(Map<String, String> ref);

    List<Map<String, Object>> listPosition(Map<String, String> ref);

    List<Map<String, Object>> listMpcsHeader(Map<String, String> ref);
    /////////////////////////////done 
}
