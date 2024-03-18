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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

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
        @ApiResponse(code = 400, message = "The resource not found")})
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
        @ApiResponse(code = 400, message = "The resource not found")})
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
        @ApiResponse(code = 400, message = "The resource not found")})
    public @ResponseBody
    Response reportReceiving(@RequestBody String param) {
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
        @ApiResponse(code = 400, message = "The resource not found")})
    public @ResponseBody
    Response reportReturnOrder(@RequestBody String param) {
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
        @ApiResponse(code = 400, message = "The resource not found")})
    public @ResponseBody
    Response reportWastage(@RequestBody String param) {
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
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
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
    @CrossOrigin
    @RequestMapping(value = "/report-order-entry-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report order entry", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportOrderEntry(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        JasperPrint jasperPrint = reportServices.jesperReportOrderEntry(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "OrderEntryReport");  
    }
    
    private ResponseEntity<byte[]> generatePdfCsvReport (JasperPrint jasperPrint, Map prm, String fileName) throws JRException, IOException {
        boolean isDownloadCsv = prm.get("isDownloadCsv").equals(true);
        if (!jasperPrint.getPages().isEmpty()) {
            if (isDownloadCsv) {
                byte[] result = generateCsvReport(jasperPrint);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "inline; filename="+fileName+".csv");
                return ResponseEntity.ok().headers(headers).contentType(MediaType.TEXT_PLAIN).body(result);
            } else {
                byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "inline; filename="+fileName+".pdf");
                return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error Message".getBytes());
        }
    }
    
    private byte[] generateCsvReport(JasperPrint jasperPrint) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // Create CSV exporter
            JRCsvExporter exporter = new JRCsvExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
            SimpleCsvExporterConfiguration configuration = new SimpleCsvExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();

            return outputStream.toByteArray();
        } catch (JRException e) {
            e.printStackTrace();
            // Handle exception
            return new byte[0];
        }
    }

    @RequestMapping(value = "/report-order-entry-jesper-html", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report order entry", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
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
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportReceiving(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportReceiving(prm, conn);
        conn.close();

        return generatePdfCsvReport(jasperPrint, prm, "ReceivingReport");
    }

    @CrossOrigin
    @RequestMapping(value = "/report-return-order-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report return order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportReturnOrder(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportReturnOrder(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "ReturnOrderReport");
    }

    @CrossOrigin
    @RequestMapping(value = "/report-wastage-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report wastage", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportWastage(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportWastage(prm, conn);
        conn.close();
       
        return generatePdfCsvReport(jasperPrint, prm, "Wastage");
    }

    @CrossOrigin
    @RequestMapping(value = "/report-delivery-order-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report delivery order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportDeliveryOrder(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        JasperPrint jasperPrint = reportServices.jesperReportDeliveryOrder(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "DeliveryOrder");
    }

    @CrossOrigin
    @RequestMapping(value = "/report-delivery-order-ex-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report delivery order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<?> jesperReportDeliveryOrderTest(@RequestBody String param, HttpServletResponse response) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Integer cekDataReport = viewServices.cekDataReport(prm, "deliveryOrder");
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jesperReportDeliveryOrder(prm, conn);
            conn.close();
            JRXlsxExporter exporter = new JRXlsxExporter();
            SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
            reportConfigXLS.setSheetNames(new String[]{"test"});
            reportConfigXLS.setDetectCellType(true);
            reportConfigXLS.setCollapseRowSpan(false);
            exporter.setConfiguration(reportConfigXLS);
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
            response.setHeader(
                    HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=ex.xlsx;");
            response.setContentType("application/octet-stream");
            exporter.exportReport();
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data");
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-item-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportItem(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportItem(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "Item");
    }

    @CrossOrigin
    @RequestMapping(value = "/report-stock-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report stock", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportStock(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportStock(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "Stock");
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 26 July 2023////
    @CrossOrigin
    @RequestMapping(value = "/report-recipe-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report recipe", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportRecipe(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportRecipe(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "Recipe");
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 01 August 2023////
    @CrossOrigin
    @RequestMapping(value = "/report-free-meal-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report free meal departemen", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportFreeMealDepartemen(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportFreeMeal(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "FreeMeal");

    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 23 August 2023////
    @CrossOrigin
    @RequestMapping(value = "/report-sales-by-time-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report sales by time", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportSalesTime(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportSalesByTime(prm, conn);
        conn.close();
        byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=salesByTime.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }

    @RequestMapping(value = "/list-param-report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk mengambil list param report", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewReturnOrderHeader(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = reportServices.listParamReport(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 25 August 2023////
    @CrossOrigin
    @RequestMapping(value = "/report-sales-by-date-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report sales by date", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportSalesDate(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportSalesByDate(prm, conn);
        conn.close();
        byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=salesByDate.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }

    /////////////////////////////////DONE///////////////////////////////////////
    /////////////// NEW METHOD REPORT BY DATE DANI 23 JAN 2024 ////
    @CrossOrigin
    @RequestMapping(value = "/report-sales-by-date-new-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report sales by date", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportSalesDateNew(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportSalesByDateNew(prm, conn);
        conn.close();
        byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=salesDate.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }
    /////////////////////////////////DONE///////////////////////////////////////

    ///////////////NEW METHOD REPORT BY PASCA 26 August 2023////
    @CrossOrigin
    @RequestMapping(value = "/report-sales-by-item-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report sales by item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportSalesItem(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        JasperPrint jasperPrint = reportServices.jasperReportSalesByItem(prm, conn);
        conn.close();

        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=salesByItem.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-menu-vs-detail-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report menu vs detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportMenuVsDetail(@RequestBody String param) throws SQLException, JRException, IOException {
        JasperPrint jasperPrint;
        try (Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass)) {
            Gson gsn = new Gson();
            Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
            }.getType());
            jasperPrint = reportServices.jasperReportMenuVsDetail(prm, conn);
        }
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=salesByMenuDetail.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 29 August 2023////
    @CrossOrigin
    @RequestMapping(value = "/report-summary-sales-by-item-code-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report summary sales by item code", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportSalesDetailByItemCode(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportSummarySalesByItemCode(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=summarySalesByItemCode.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 07 September 2023////
    @CrossOrigin
    @RequestMapping(value = "/report-stock-card-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report stock cart", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportStockCard(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportStockCard(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "StockCard");
    }

    @PostMapping("/test/{size}/{page}")
    public Page<Map<String, Object>> getTestPagination(@PathVariable(value = "size") int size,
            @PathVariable(value = "page") int page) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<Map<String, Object>> result = reportServices.getTestPagination(pageable);
        System.out.println(result);

        return result;
    }

    @CrossOrigin
    @RequestMapping(value = "/report-transaksi-kasir-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report transakasi kasir", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportTransaksiKasir(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

//        Integer cekDataReport = viewServices.cekDataReport(prm, "transaksiKasir");
        JasperPrint jasperPrint = reportServices.jasperReportTransaksiKasir(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=transaksiKasir.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-receipt-maintenance-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report receipt maintenance", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportReceiptMaintenance(@RequestBody String param) throws SQLException, JRException, IOException, ParseException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Integer cekDataReport = viewServices.cekDataReport(prm, "ReceiptMaintenance");
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jasperReportReceiptMaintenance(prm, conn);
            conn.close();
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=ReceiptMaintenance.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-sales-mix-department-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report sales mix by department", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportSalesMixDepartment(@RequestBody String param) throws SQLException, JRException, IOException, ParseException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Integer cekDataReport = viewServices.cekDataReport(prm, "salesMixDepartment");
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jasperReportSalesMixDepartment(prm, conn);
            conn.close();
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=ReportSalesMixByDepartment.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-query-bill-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report sales mix by department", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportQueryBill(@RequestBody String param) throws SQLException, JRException, IOException, ParseException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportQueryBill(prm, conn);
        conn.close();
        byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=QueryBill.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }

    @RequestMapping(value = "/list-query-bill", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk mengambil list query bill", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewListQueryBill(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = reportServices.listQueryBill(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    @RequestMapping(value = "/list-query-sales", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk mengambil query sales", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    })
    public @ResponseBody
    Response viewListQuerySales(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = reportServices.listQuerySales(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    @CrossOrigin
    @RequestMapping(value = "/report-transaction-by-payment-type-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report transaction by payment type", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportTransactionByPaymentType(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

//        Integer cekDataReport = viewServices.cekDataReport(prm, "TransactionByPaymentType");
        JasperPrint jasperPrint = reportServices.jasperReportTransactionByPaymentType(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=TransactionByPaymentType.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-pemakaian-by-sales-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report pemakaian by sales", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportPemakaianBySalesReport(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

//        Integer cekDataReport = viewServices.cekDataReport(prm, "pemakaianBySales");
        JasperPrint jasperPrint = reportServices.jasperReportPemakaianBySales(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=PemakaianBySales.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-produksi-aktual-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report produksi aktual", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportProduksiAktual(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportProduksiAktual(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "ProduksiAktual");

    }

    @CrossOrigin
    @RequestMapping(value = "/report-inventory-movement-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report inventory movement", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportInventoryMovement(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportInventoryMovement(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "InventoryMovement");
    }

    @CrossOrigin
    @RequestMapping(value = "/report-stock-opname-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report stock opname", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportStockOpname(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Integer cekDataReport = viewServices.cekDataReport(prm, "stockOpname");
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jasperReportStockOpname(prm, conn);
            conn.close();
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=StockOpname.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-order-entry-transactions-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report order entry transactions", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<?> jesperReportOrderEntryTransactions(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Integer cekDataReport = viewServices.cekDataReport(prm, "orderEntryTransactions");
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jasperReportOrderEntryTransactions(prm, conn);
            conn.close();
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            String isCetak = (String) prm.get("action");
            if (isCetak != null && isCetak.equals("Download")) {
                processServices.addCounterPrintOrderEntry(prm);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=orderEntryTransactions.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Detail item tidak tersedia");
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-receiving-transactions-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report receiving transactions", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportReceivingTransactions(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Integer cekDataReport = viewServices.cekDataReport(prm, "receivingTransactions");
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jasperReportReceivingTransactions(prm, conn);
            conn.close();
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            String isCetak = (String) prm.get("action");
            if (isCetak != null && isCetak.equals("Download")) {
                processServices.addCounterPrintReceiving(prm);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=receivingTransactions.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-wastage-transactions-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report wastage transactions", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportWastageTransactions(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Integer cekDataReport = viewServices.cekDataReport(prm, "wastageTransactions");
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jasperReportWastageTransactions(prm, conn);
            conn.close();
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=wastageTransactions.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-return-order-transactions-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report return order transactions", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportReturnOrderTransactions(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Integer cekDataReport = viewServices.cekDataReport(prm, "returnOrderTransactions");
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jasperReportReturnOrderTransactions(prm, conn);
            conn.close();
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=returnOrderTransactions.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-delivery-order-transactions-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report return order transactions", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportDeliveryOrderTransactions(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Integer cekDataReport = viewServices.cekDataReport(prm, "deliveryOrderTransactions");
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jasperReportDeliveryOrderTransactions(prm, conn);
            conn.close();
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=returnOrderTransactions.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-return-item-selected-by-time-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report item selected by time", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportItemSelectedByTime(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

//        Integer cekDataReport = viewServices.cekDataReport(prm, "itemSelectedByTime");
        JasperPrint jasperPrint = reportServices.jasperReportItemSelectedByTime(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=itemSelectedByTime.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    /// report sales void by M Joko 29/12/23
    @CrossOrigin
    @RequestMapping(value = "/report-sales-void-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report sales void", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportSalesVoid(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportSalesVoid(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=salesVoid.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    //////////////// New Method Report Item Selected By Product by M Joko 10 Januari 2024
    @CrossOrigin
    @RequestMapping(value = "/report-item-selected-by-product-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report item selected by product", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportItemSelectedByProduct(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportItemSelectedByProduct(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=itemSelectedByProduct.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    ///////////////NEW METHOD REPORT daftar menu by Rafi 29 Desember 2023////
    @CrossOrigin
    @RequestMapping(value = "/report-daftar-menu-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report daftar menu", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportDaftarMenu(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportDaftarMenu(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "DaftarMenuReport");

    }

    ///////////////NEW METHOD REPORT delete mpcs produksi by adit 3 Januari 2024////
    @CrossOrigin
    @RequestMapping(value = "/report-delete-mpcs-produksi-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report DELETE MPCS Produksi", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportDeleteMpcsProduksi(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        JasperPrint jasperPrint = reportServices.jesperReportDeleteMpcsProduksi(prm, conn);
        conn.close();

        return generatePdfCsvReport(jasperPrint, prm, "DeleteMpcsProduksi");
    }
    ///////////////////////////////// done adit 04-01-2024 ///////////////////////////////////////

    ///////////////NEW METHOD REPORT REFUND by Rafi 4 Januari 2024////
    @CrossOrigin
    @RequestMapping(value = "/report-report-refund-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report refund", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportRefund(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Integer cekDataReport = viewServices.cekDataReport(prm, "reportRefund");
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jasperReportRefund(prm, conn);
            conn.close();
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=RefundReport.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error Message".getBytes());
        }
    }

    ///////////////NEW METHOD REPORT +++ by adit 8 Januari 2024////
    ////////////////// Actual Stock Opname
    @CrossOrigin
    @RequestMapping(value = "/report-actual-stock-opname-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report actual stock opname", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportaActualStockOpname(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportaActualStockOpname(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=ActualStockOpname.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }

    }

    ////////////////// performance rider HD
    @CrossOrigin
    @RequestMapping(value = "/report-performance-rider-hd-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report performance rider hd", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportPerformanceRiderHd(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportPerformanceRiderHd(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=PerformanceRiderHd.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }

    }

    ////////////////// pajak
    @CrossOrigin
    @RequestMapping(value = "/report-pajak-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report pajak", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportPajak(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportPajak(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=Pajak.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }

    }
    ///////////////////////////////// done adit 04-01-2024 ///////////////////////////////////////

    ///////////////NEW METHOD Report Product Efficiency by Pasca 9 Januari 2024////
    @CrossOrigin
    @RequestMapping(value = "/report-product-efficiency-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report product efficiency", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportProductEfficiency(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportProductEfficiency(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=productEfficiency.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    ///////////////NEW METHOD REPORT down payment by Dani 9 Januari 2024////
    @CrossOrigin
    @RequestMapping(value = "/report-down-payment-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report Down Payment", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportDownPayment(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        prm.put("startDate", prm.get("fromDate"));
        prm.put("endDate", prm.get("toDate"));
        String customerName = (String) prm.get("customerName");
        String orderType = (String) prm.get("orderType");
        String bookStatus = (String) prm.get("bookStatus");
        prm.put("customerName", "%" + (customerName != null && customerName.equalsIgnoreCase("All") ? "" : customerName) + "%");
        prm.put("orderType", "%" + (orderType != null && orderType.equalsIgnoreCase("All") ? "" : orderType) + "%");
        prm.put("bookStatus", "%" + (bookStatus != null && bookStatus.equalsIgnoreCase("All") ? "" : bookStatus) + "%");
        Integer cekDataReport = viewServices.cekDataReport(prm, "DownPayment");
        if (cekDataReport > 0) {
            JasperPrint jasperPrint = reportServices.jasperReportDownPayment(prm, conn);
            conn.close();
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=DownPayment.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error Message".getBytes());
        }
    }

    ///////////////NEW METHOD REPORT SELECTED ITEM BY DETAIL by Rafi 9 Januari 2024////
    @CrossOrigin
    @RequestMapping(value = "/report-selected-item-by-detail-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report selected item by detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportSelectedItemByDetail(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportSelectedItemByDetail(prm, conn);
        conn.close();

        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=ReportSelectedItemByDetail.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    ///////////////NEW METHOD REPORT PRODUCTION by Sifa 11 Januari 2024////
    @CrossOrigin
    @RequestMapping(value = "/report-production-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report production", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportProduction(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportProduction(prm, conn);
        conn.close();
        
        return generatePdfCsvReport(jasperPrint, prm, "ReportProduction");
    }

    ///////////////NEW METHOD REPORT EOD by Dani 16 Januari 2024////
    @CrossOrigin
    @RequestMapping(value = "/report-eod-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report production", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportEod(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportEod(prm, conn);
        conn.close();
        byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=ReportEOD.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
    }

    ///////////////NEW METHOD Report Product Efficiency by Pasca 9 Januari 2024////
    @CrossOrigin
    @RequestMapping(value = "/report-item-sales-analysis-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mepampilkan report item sales analysis", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportItemSalesAnalysis(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportItemSalesAnalysis(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=itemSalesAnalysis.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-time-management-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report Time Management by fathur 22 Jan 2024", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportTimeManagement(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportTimeManagement(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=reportTimeManagement.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/report-sales-item-by-time-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Report Sales Item by Time - Fathur 26 Jan 2024", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportSalesItembyTime(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        JasperPrint jasperPrint = reportServices.jasperReportSalesItembyTime(prm, conn);
        conn.close();

        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=salesByItem.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    ////////////////// new Report MPCS Management Fryer by Aditya 30 Jan 2024
    @CrossOrigin
    @RequestMapping(value = "/report-mpcs-management-fryer-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report MPCS Management Fryer", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportMpcsManagementFryer(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportMpcsManagementFryer(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=mpcsManagementFryer.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }

    }
    ///////////////////////////////// done adit 30-01-2024 ///////////////////////////////////////

    ////////////////// new Report MPCS Management Fryer by Pasca 16 Feb 2024
    @CrossOrigin
    @RequestMapping(value = "/report-transaksi-hd-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report transaksi hd", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportTransaksiHd(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jesperReportTransaksiHd(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=transaksiHd.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }

    }
    ///////////////////////////////// done Pasca 16-02-2024 ///////////////////////////////////////

    ///////////////NEW METHOD REPORT BY M Joko - 3 Maret 2024////
    @CrossOrigin
    @RequestMapping(value = "/report-cash-pull-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report Cash Pull", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportCashPull(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        JasperPrint jasperPrint = reportServices.jasperReportCashPull(prm, conn);
        conn.close();

        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=cashPull.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }


    ////////////////// new Report Usage by Dani 14 Mar 2024
    @CrossOrigin
    @RequestMapping(value = "/report-usage-food-beverage-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Report Usage Food & Beverage", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportUsageFoodBeverage(@RequestBody String param) throws SQLException, JRException, IOException {
        JasperPrint jasperPrint;
        try (Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass)) {
            Gson gsn = new Gson();
            Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
            }.getType());
            jasperPrint = reportServices.jesperReportUsageFoodBeverage(prm, conn);
        }
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=salesByMenuDetail.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    ////////////////// new Report Usage by Dani 14 Mar 2024
    @CrossOrigin
    @RequestMapping(value = "/report-usage-cd-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Report Usage CD", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jasperReportUsageCD(@RequestBody String param) throws SQLException, JRException, IOException {
        JasperPrint jasperPrint;
        try (Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass)) {
            Gson gsn = new Gson();
            Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
            }.getType());
            jasperPrint = reportServices.jesperReportUsageCD(prm, conn);
        }
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=salesByMenuDetail.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }

    /// report POS action by M Joko 18/3/24
    @CrossOrigin
    @RequestMapping(value = "/report-pos-action-jesper", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan report pos action", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")})
    public ResponseEntity<byte[]> jesperReportPosAction(@RequestBody String param) throws SQLException, JRException, IOException {
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        Gson gsn = new Gson();
        Map<String, Object> prm = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        JasperPrint jasperPrint = reportServices.jasperReportposAction(prm, conn);
        conn.close();
        if (!jasperPrint.getPages().isEmpty()) {
            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=posAction.pdf");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Data".getBytes());
        }
    }
}
