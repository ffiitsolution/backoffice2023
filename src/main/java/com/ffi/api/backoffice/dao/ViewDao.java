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
}
