/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.services;

import com.ffi.api.backoffice.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ReportServices {

    @Autowired
    ReportDao dao;
    ///////////////NEW METHOD REPORT BY PASCA 23 MEI 2023////

    public List<Map<String, Object>> reportOrderEntry(Map<String, String> param) throws IOException {
        return dao.reportOrderEntry(param);
    }

    public List<Map<String, Object>> reportDeliveryOrder(Map<String, String> param) {
        return dao.reportDeliveryOrder(param);
    }
    ///////////////////////END//////////////////////////////
}
