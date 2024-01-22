/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.services;

import com.ffi.api.backoffice.dao.ReportDao;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
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
    
    /////////////////////// new method Delete MPCS produksi adit 04-01-2024
    public JasperPrint jesperReportDeleteMpcsProduksi(Map<String, Object> param, Connection connection) throws JRException, SQLException, IOException {
        return dao.jesperReportDeleteMpcsProduksi(param, connection);
    }
    /////////////////////// done adit 04-01-2024

    public JasperPrint jasperReportItem(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jesperReportItem(param, connection);
    }

    public JasperPrint jasperReportStock(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportStock(param, connection);
    }
    ///////////////////////END//////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 26 July 2023////
    public JasperPrint jasperReportRecipe(Map<String, Object> param, Connection connection) throws JRException, IOException {
       return dao.jasperReportRecipe(param, connection);
    }
    ///////////////////////END//////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 01 August 2023////
    public JasperPrint jasperReportFreeMeal(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportFreeMeal(param, connection);
    }
    ///////////////////////END//////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 23 August 2023////
    public JasperPrint jasperReportSalesByTime(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportSalesByTime(param, connection);
    }
    public List<Map<String, Object>> listParamReport(Map<String, String> param) {
        return dao.listParamReport(param);
    }
    ///////////////////////END//////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 25 August 2023////
    public JasperPrint jasperReportSalesByDate(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportSalesByDate(param, connection);
    }
    ///////////////////////END//////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 28 August 2023////
    public JasperPrint jasperReportSalesByItem(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportSalesByItem(param, connection);
    }
    public JasperPrint jasperReportMenuVsDetail(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportMenuVsDetail(param, connection);
    }
    
    ////////////// new method +++ by adit 8 Januari 2024 /////////////
    public JasperPrint jesperReportaActualStockOpname(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jesperReportaActualStockOpname(param, connection);
    }
    
    public JasperPrint jesperReportPerformanceRiderHd(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jesperReportPerformanceRiderHd(param, connection);
    }
    
    public JasperPrint jesperReportPajak(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jesperReportPajak(param, connection);
    }
    ////////////// done adit 08-01-2024

    public JasperPrint jasperReportSummarySalesByItemCode(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportSummarySalesByItemCode(param, connection);
    }

    public JasperPrint jasperReportStockCard(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportStockCard(param, connection);
    }

    public Page<Map<String, Object>> getTestPagination(Pageable pageable){
        return dao.getTestPagination(pageable);
    }

    public JasperPrint jasperReportTransaksiKasir(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportTransaksiKasir(param, connection);
    }

    public JasperPrint jasperReportReceiptMaintenance(Map<String, Object> param, Connection connection) throws JRException, IOException, ParseException {
        return dao.jasperReportReceiptMaintenance(param, connection);
    }

    public JasperPrint jasperReportSalesMixDepartment(Map<String, Object> param, Connection connection) throws JRException, IOException, ParseException {
        return dao.jasperReportSalesMixDepartment(param, connection);
    }

    public JasperPrint jasperReportQueryBill(Map<String, Object> param, Connection connection) throws JRException, IOException, ParseException {
        return dao.jasperReportQueryBill(param, connection);
    }

    public List<Map<String, Object>> listQueryBill(Map<String, Object> param) {
        return dao.listQueryBill(param);
    }

    public List<Map<String, Object>> listQuerySales(Map<String, Object> param) {
        return dao.listQuerySales(param);
    }

    public JasperPrint jasperReportTransactionByPaymentType(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportTransactionByPaymentType(param, connection);
    }

    public JasperPrint jasperReportPemakaianBySales(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportPemakaianBySales(param, connection);
    }

    public JasperPrint jasperReportProduksiAktual(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportProduksiAktual(param, connection);
    }

    public JasperPrint jasperReportInventoryMovement(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportInventoryMovement(param, connection);
    }

    public JasperPrint jasperReportStockOpname(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportStockOpname(param, connection);
    }

    public JasperPrint jasperReportOrderEntryTransactions(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportOrderEntryTransactions(param, connection);
    }

    public JasperPrint jasperReportReceivingTransactions(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportReceivingTransactions(param, connection);
    }

    public JasperPrint jasperReportWastageTransactions(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportWastageTransactions(param, connection);
    }

    public JasperPrint jasperReportReturnOrderTransactions(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportReturnOrderTransactions(param, connection);
    }

    public JasperPrint jasperReportDeliveryOrderTransactions(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportDeliveryOrderTransactions(param, connection);
    }

    public JasperPrint jasperReportItemSelectedByTime(Map<String, Object> param, Connection connection) throws  JRException, IOException {
        return dao.jasperReportItemSelectedByTime(param, connection);
    }

    public JasperPrint jasperReportDaftarMenu(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportDaftarMenu(param, connection);
    }

    public JasperPrint jasperReportSalesVoid(Map<String, Object> param, Connection connection) throws  JRException, IOException {
        return dao.jasperReportSalesVoid(param, connection);
    }

    public JasperPrint jasperReportRefund(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportRefund(param, connection);
    }

    public JasperPrint jasperReportProductEfficiency(Map<String, Object> param, Connection connection) throws JRException, IOException {
        return dao.jasperReportProductEfficiency(param, connection);
    }

    public JasperPrint jasperReportDownPayment(Map<String, Object> param, Connection connection) throws  JRException, IOException {
        return dao.jasperReportDownPayment(param, connection);
    }

    public JasperPrint jesperReportSelectedItemByDetail(Map<String, Object> param, Connection connection) throws  JRException, IOException {
        return dao.jesperReportSelectedItemByDetail(param, connection);
    }
    
    public JasperPrint jasperReportItemSelectedByProduct(Map<String, Object> param, Connection connection) throws  JRException, IOException {
        return dao.jasperReportItemSelectedByProduct(param, connection);
    }
    
    public JasperPrint jasperReportProduction(Map<String, Object> param, Connection connection) throws JRException, SQLException, IOException {
        return dao.jasperReportProduction(param, connection);
    }

    public JasperPrint jasperReportEod(Map<String, Object> param, Connection connection) throws JRException, SQLException, IOException {
        return dao.jasperReportEod(param, connection);
    }

    public JasperPrint jasperReportItemSalesAnalysis(Map<String, Object> param, Connection connection) throws JRException, SQLException, IOException {
        return dao.jasperReportItemSalesAnalysis(param, connection);
    }
    
    public JasperPrint jasperReportTimeManagement(Map<String, Object> param, Connection connection) throws JRException, SQLException, IOException {
        return dao.jasperReportTimeManagement(param, connection);
    }
}
