/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ffi.api.backoffice.dao;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
    ///////////////NEW METHOD REPORT receive BY PASCA 29 MEI 2023////
    public void insertLogReport(Map<String, String> mapping);
    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT receive BY PASCA 10 July 2023////
    JasperPrint jesperReportOrderEntry (Map<String, Object> param, Connection connection) throws SQLException, JRException, IOException;
    JasperPrint jasperReportReceiving (Map<String, Object> param, Connection connection) throws IOException, JRException;
    JasperPrint jasperReportReturnOrder (Map<String, Object> param, Connection connection) throws IOException, JRException;
    JasperPrint jasperReportWastage (Map<String, Object> param, Connection connection) throws IOException, JRException;
    JasperPrint jasperReportDeliveryOrder (Map<String, Object> param, Connection connection) throws IOException, JRException;
    JasperPrint jesperReportItem (Map<String, Object> param, Connection connection) throws IOException, JRException;
    JasperPrint jasperReportStock (Map<String, Object> param, Connection connection) throws IOException, JRException;
    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT receive BY PASCA 10 July 2023////
    JasperPrint jasperReportRecipe (Map<String, Object> param);
}
