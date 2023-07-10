/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.services;

import com.ffi.api.backoffice.dao.ReportDao;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class ReportServices {

    @Autowired
    ReportDao dao;
    ///////////////NEW METHOD REPORT BY PASCA 23 MEI 2023////

    public List<Map<String, Object>> reportOrderEntry(Map<String, Object> param) throws IOException {
        return dao.reportOrderEntry(param);
    }

    public List<Map<String, Object>> reportDeliveryOrder(Map<String, Object> param) {
        return dao.reportDeliveryOrder(param);
    }

    ///////////////////////END//////////////////////////////
    ///////////////NEW METHOD REPORT receive BY PASCA 24 MEI 2023////
    public List<Map<String, Object>> reportReceiving(Map<String, Object> param) {
        return dao.reportReceiving(param);
    }

    public List<Map<String, Object>> reportReturnOrder(Map<String, Object> param) {
        return dao.reportReturnOrder(param);
    }

    public List<Map<String, Object>> reportWastage(Map<String, Object> param) {
        return dao.reportWastage(param);
    }
    ///////////////////////END//////////////////////////////

    ///////////////NEW METHOD REPORT receive BY PASCA 29 MEI 2023////
    public void insertLogReport(Map<String, String> param) {
        dao.insertLogReport(param);
    }
    ///////////////////////END//////////////////////////////
    ///////////////NEW METHOD REPORT receive BY PASCA 10 July 2023////

    public JasperPrint jesperReportOrderEntry(Map<String, Object> param, Connection connection) throws JRException, SQLException, IOException {
        return dao.jesperReportOrderEntry(param, connection);
    }

    public JasperPrint jesperReportReceiving(Map<String, Object> param, Connection connection) throws JRException, SQLException, IOException {
        return dao.jasperReportReceiving(param, connection);
    }

    public JasperPrint jesperReportReturnOrder(Map<String, Object> param, Connection connection) throws JRException, SQLException, IOException {
        return dao.jasperReportReturnOrder(param, connection);
    }

    public JasperPrint jesperReportWastage(Map<String, Object> param, Connection connection) throws JRException, SQLException, IOException {
        return dao.jasperReportWastage(param, connection);
    }

    public JasperPrint jesperReportDeliveryOrder(Map<String, Object> param, Connection connection) throws JRException, SQLException, IOException {
        return dao.jasperReportDeliveryOrder(param, connection);
    }

    public JasperPrint jasperReportItem(Map<String, Object> param, Connection connection) {
        return dao.jesperReportItem(param, connection);
    }
}
