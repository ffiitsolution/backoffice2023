/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice;

import com.ffi.api.backoffice.services.ProcessServices;
import com.ffi.api.backoffice.services.ReportServices;
import com.ffi.api.backoffice.services.ViewServices;
import com.ffi.paging.Response;
import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Dwi Prasetyo
 */
@RestController
public class ReportController {

    @Autowired
    ViewServices viewServices;
    @Autowired
    ProcessServices processServices;
    @Autowired
    ReportServices reportServices;

    @Value("${spring.datasource.url}")
    private String getOracleUrl;
    @Value("${spring.datasource.username}")
    private String getOracleUsername;
    @Value("${spring.datasource.password}")
    private String getOraclePass;

    ///////////////NEW METHOD REPORT BY PASCA 23 MEI 2023////
    @RequestMapping(value = "/report-order-entry")
    @ApiOperation(value = "Mepampilkan report order entry", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 400, message = "The resource not found")})
    public @ResponseBody Response reportOrderEntry(@RequestBody String param) throws IOException {
        Gson gson = new Gson();
        Map<String, Object> listParam = gson.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(reportServices.reportOrderEntry(listParam));
        return res;
    }

    @RequestMapping(value = "/report-delivery-order")
    @ApiOperation(value = "Mepampilkan report delivery order", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 400, message = "The resource not found")})
    public @ResponseBody Response reportDeliveryOrder(@RequestBody String param) {
        Gson gson = new Gson();

        Map<String, Object> listParam = gson.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();

        res.setData(reportServices.reportDeliveryOrder(listParam));
        return res;
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT recive BY PASCA 24 MEI 2023////
    @RequestMapping(value = "/report-receiving")
    @ApiOperation(value = "Mepampilkan report receiving", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 400, message = "The resource not found")})
    public @ResponseBody Response reportReceiving(@RequestBody String param) {
        Gson gson = new Gson();

        Map<String, Object> listParam = gson.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();

        res.setData(reportServices.reportReceiving(listParam));
        return res;
    }

    @RequestMapping(value = "/report-return-order")
    @ApiOperation(value = "Mepampilkan report return order", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 400, message = "The resource not found")})
    public @ResponseBody Response reportReturnOrder(@RequestBody String param) {
        Gson gson = new Gson();

        Map<String, Object> listParam = gson.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();

        res.setData(reportServices.reportReturnOrder(listParam));
        return res;
    }

    @RequestMapping(value = "/report-wastage")
    @ApiOperation(value = "Mepampilkan report wastage", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 400, message = "The resource not found")})
    public @ResponseBody Response reportWastage(@RequestBody String param) {
        Gson gson = new Gson();

        Map<String, Object> listParam = gson.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();

        res.setData(reportServices.reportWastage(listParam));
        return res;
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT recive BY PASCA 29 MEI 2023////
    @RequestMapping(value = "/insert-log-report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk Insert data ke tabel log-report", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "The resource not found")})
    public @ResponseBody ResponseMessage insertStaff(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        ResponseMessage rm = new ResponseMessage();
        try {
            reportServices.insertLogReport(prm);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }
        return rm;
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT recive BY PASCA 06 June 2023////
    @CrossOrigin
    @RequestMapping(value = "/report-order-entry-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report order entry", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportOrderEntry(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportOrderEntry(prm, conn);
        conn.close();
        byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=OrderEntryReport.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }
    @RequestMapping(value = "/report-order-entry-jesper-html", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report order entry", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "The resource not found")})
    public void jesperReportOrderEntryHtml(@RequestBody String param, HttpServletResponse response) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportOrderEntry(prm, conn);
        response.setContentType("text/html");
        conn.close();
        HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
        exporter.exportReport();

    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT recive BY PASCA 10 July 2023////
    @CrossOrigin
    @RequestMapping(value = "/report-receiving-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report receiving", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportReceiving(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Integer cekDataReport = viewServices.cekDataReport(prm);
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jesperReportReceiving(prm, conn);
            conn.close();
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=ReceivingReport.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error Message".getBytes());
    }

    @CrossOrigin
    @RequestMapping(value = "/report-return-order-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report return order", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportReturnOrder(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportReturnOrder(prm, conn);
        conn.close();
        byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=ReturnOrderReport.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }

    @CrossOrigin
    @RequestMapping(value = "/report-wastage-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report wastage", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportWastage(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportWastage(prm, conn);
        conn.close();
        byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Wastage.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }

    @CrossOrigin
    @RequestMapping(value = "/report-delivery-order-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report delivery order", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportDeliveryOrder(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportDeliveryOrder(prm, conn);
        conn.close();
        byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=DeliveryOrder.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }

    @RequestMapping(value = "/report-item-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report item", response = Object.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReport(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportItem(prm, conn);
        conn.close();
        byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=item.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }

}
