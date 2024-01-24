/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ffi.api.backoffice.dao;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
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
    ///////////////NEW METHOD REPORT receive BY RAFI 29 Desember 2023////
    JasperPrint jasperReportDaftarMenu (Map<String, Object> param, Connection connection) throws IOException, JRException;

    /////////////////////////////////DONE///////////////////////////////////////
    
    /////////////////////// new method Delete MPCS produksi adit 04-01-2024
    JasperPrint jesperReportDeleteMpcsProduksi (Map<String, Object> param, Connection connection) throws IOException, JRException;
    /////////////////////// done adit 04-01-2024
    
    ///////////////NEW METHOD REPORT receive BY PASCA 10 July 2023////
    JasperPrint jasperReportRecipe (Map<String, Object> param, Connection connection) throws IOException, JRException;
    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT receive BY PASCA 01 August 2023////
    JasperPrint jasperReportFreeMeal (Map<String, Object> param, Connection connection) throws IOException, JRException;
    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT receive BY PASCA 23 August 2023////
    JasperPrint jasperReportSalesByTime (Map<String, Object> param, Connection connection) throws IOException, JRException;
    List<Map<String, Object>> listParamReport(Map<String, String> param);
    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT receive BY PASCA 25 August 2023////
    JasperPrint jasperReportSalesByDate (Map<String, Object> param, Connection connection) throws IOException, JRException;
    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT receive BY DANI 23 Jan 2024////
    JasperPrint jasperReportSalesByDateNew (Map<String, Object> param, Connection connection) throws IOException, JRException;
    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT receive BY PASCA 28 August 2023////
    JasperPrint jasperReportSalesByItem (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jasperReportMenuVsDetail (Map<String, Object> param, Connection connection) throws JRException, IOException;
    ///////////// new method report +++ by adit 08 Januari 2024
    JasperPrint jesperReportaActualStockOpname (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jesperReportPerformanceRiderHd (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jesperReportPajak (Map<String, Object> param, Connection connection) throws JRException, IOException;
    /////////// done adit 08-01-2024
    JasperPrint jasperReportSummarySalesByItemCode (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jasperReportStockCard (Map<String, Object> param, Connection connection) throws JRException, IOException;
    Page<Map<String, Object>> getTestPagination(Pageable pageable);
    JasperPrint jasperReportTransaksiKasir (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jasperReportReceiptMaintenance(Map<String, Object> param, Connection connection) throws JRException, IOException, ParseException;
    JasperPrint jasperReportSalesMixDepartment(Map<String, Object> param, Connection connection) throws JRException, IOException, ParseException;
    JasperPrint jasperReportQueryBill(Map<String, Object> param, Connection connection) throws JRException, IOException, ParseException;
    List<Map<String, Object>> listQueryBill(Map<String, Object> param);
    List<Map<String, Object>> listQuerySales(Map<String, Object> param);
    JasperPrint jasperReportTransactionByPaymentType (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jasperReportPemakaianBySales (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jasperReportProduksiAktual (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jasperReportInventoryMovement (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jasperReportStockOpname (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jasperReportOrderEntryTransactions (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jasperReportReceivingTransactions (Map<String, Object> param, Connection connection) throws JRException, IOException;
    JasperPrint jasperReportWastageTransactions (Map<String, Object> param, Connection connection) throws  JRException,IOException;
    JasperPrint jasperReportReturnOrderTransactions (Map<String, Object> param, Connection connection) throws  JRException,IOException;
    JasperPrint jasperReportDeliveryOrderTransactions (Map<String, Object> param, Connection connection) throws  JRException,IOException;
    JasperPrint jasperReportItemSelectedByTime (Map<String, Object> param, Connection connection) throws  JRException,IOException;
    JasperPrint jasperReportSalesVoid (Map<String, Object> param, Connection connection) throws  JRException,IOException;    
    JasperPrint jasperReportRefund (Map<String, Object> param, Connection connection) throws  JRException,IOException;
    JasperPrint jasperReportProductEfficiency (Map<String, Object> param, Connection connection) throws  JRException,IOException;
    JasperPrint jasperReportDownPayment (Map<String, Object> param, Connection connection) throws  JRException,IOException;    
    JasperPrint jesperReportSelectedItemByDetail (Map<String, Object> param, Connection connection) throws  JRException,IOException;    
    JasperPrint jasperReportItemSelectedByProduct (Map<String, Object> param, Connection connection) throws  JRException,IOException;
    JasperPrint jasperReportProduction (Map<String, Object> param, Connection connection) throws IOException, JRException;
    JasperPrint jasperReportEod (Map<String, Object> param, Connection connection) throws IOException, JRException;
    JasperPrint jasperReportItemSalesAnalysis (Map<String, Object> param, Connection connection) throws  JRException,IOException;
    JasperPrint jasperReportTimeManagement (Map<String, Object> param, Connection connection) throws  JRException,IOException;
}
