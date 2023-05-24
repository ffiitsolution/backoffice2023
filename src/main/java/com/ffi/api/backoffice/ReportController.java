/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice;

import com.ffi.api.backoffice.services.ProcessServices;
import com.ffi.api.backoffice.services.ReportServices;
import com.ffi.api.backoffice.services.ViewServices;
import com.ffi.paging.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
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
        Map<String, String> listParam = gson.fromJson(param, new TypeToken<Map<String, Object>>() {
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

        Map<String, String> listParam = gson.fromJson(param, new TypeToken<Map<String, Object>>() {
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

        Map<String, String> listParam = gson.fromJson(param, new TypeToken<Map<String, Object>>() {}.getType());
        Response res = new Response();

        res.setData(reportServices.reportReceiving(listParam));
        return res;
    }
    /////////////////////////////////DONE///////////////////////////////////////
}
