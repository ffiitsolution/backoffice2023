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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 400, message = "The resource not found")
    })
    public @ResponseBody
    Response reportOrderEntry(@RequestBody String param) throws IOException {
        Gson gson = new Gson();
        Map<String, Object> listParam = gson.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(reportServices.reportOrderEntry(listParam));
        return res;
    }

    @RequestMapping(value = "/report-delivery-order")
    @ApiOperation(value = "Mepampilkan report delivery order", response = Object.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 400, message = "The resource not found")
    })
    public @ResponseBody
    Response reportDeliveryOrder(@RequestBody String param) {
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 400, message = "The resource not found")
    })
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 400, message = "The resource not found")
    })
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 400, message = "The resource not found")
    })
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
    @ApiResponses(value
            = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    ResponseMessage insertStaff(@RequestBody String param) throws JRException, IOException, Exception {
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
    @RequestMapping(value = "/report-order-entry-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report order entry", response = Object.class)
    @ApiResponses(value
            = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public ResponseEntity<byte[]> jesperReportOrderEntry(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        HashMap hashMap = new HashMap();
        hashMap.put("city", "X_" + prm.get("city"));
        if (prm.get("typeOrder").equals("Semua")){
            hashMap.put("orderType1", "0");
            hashMap.put("orderType2", "1");
            hashMap.put("typeOrder", "Semua");
        } else if (prm.get("typeOrder").equals("Permintaan")) {
            hashMap.put("orderType1", "0");
            hashMap.put("orderType2", "0");
            hashMap.put("typeOrder", "Permintaan");
        } else if (prm.get("typeOrder").equals("Pembelian")) {
            hashMap.put("orderType1", "1");
            hashMap.put("orderType2", "1");
            hashMap.put("typeOrder", "Pembelian");
        }
        hashMap.put("fromDate", prm.get("fromDate"));
        hashMap.put("toDate", prm.get("toDate"));
        hashMap.put("outletCode", prm.get("outletCode"));
        if (prm.get("detail").equals(1.0)){
            hashMap.put("detail", 1);
        } else {
            hashMap.put("detail", 0);
        }
        hashMap.put("user", prm.get("user"));

        ClassPathResource classPathResource = new ClassPathResource("report/orderEntry.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, conn);
        byte result[] = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=OrderEntryReport.pdf");
        conn.close();
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }
    /////////////////////////////////DONE///////////////////////////////////////

    @RequestMapping(value = "/report-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report order entry", response = Object.class)
    @ApiResponses(value
            = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public ResponseEntity<byte[]> jesperReport(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        ClassPathResource classAll = new ClassPathResource("report/sub/item_all.jrxml");
        ClassPathResource calassJenisGudang = new ClassPathResource("report/sub/item_jenis_gudang.jrxml");
        JasperReport jasperSubAll = JasperCompileManager.compileReport(classAll.getInputStream());
        JasperReport jasperSubGudang = JasperCompileManager.compileReport(calassJenisGudang.getInputStream());

        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("outletCode", "0401");
        hashMap.put("user", "ZTO");
        hashMap.put("status", "Semua");
        hashMap.put("jenisGudang", "Dry Good");
        hashMap.put("typeStock", "Semua");
        hashMap.put("status1", "I");
        hashMap.put("status2", "A");
        hashMap.put("flagStock1", " ");
        hashMap.put("flagStock2", "N");
        hashMap.put("flagStock3", "Y");
        hashMap.put("subreportJenisGudang", jasperSubGudang);
        hashMap.put("subreportAll", jasperSubAll);

        ClassPathResource classPathResource = new ClassPathResource("report/test.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, conn);
        byte result[] = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Report.pdf");
        conn.close();
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }

}
