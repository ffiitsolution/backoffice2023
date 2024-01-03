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
}
