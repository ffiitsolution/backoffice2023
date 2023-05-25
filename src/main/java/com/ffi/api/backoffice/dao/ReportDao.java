/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ffi.api.backoffice.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dwi Prasetyo
 */
public interface ReportDao {

    ///////////////NEW METHOD REPORT BY PASCA 23 MEI 2023////
    List<Map<String, Object>> reportOrderEntry(Map<String, Object> param) throws IOException;

    List<Map<String, Object>> reportDeliveryOrder(Map<String, Object> param);

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT receive BY PASCA 24 MEI 2023////
    List<Map<String, Object>> reportReceiving(Map<String, Object> param);

    List<Map<String, Object>> reportReturnOrder(Map<String, Object> param);

    List<Map<String, Object>> reportWastage(Map<String, Object> param);
    /////////////////////////////////DONE///////////////////////////////////////
}
