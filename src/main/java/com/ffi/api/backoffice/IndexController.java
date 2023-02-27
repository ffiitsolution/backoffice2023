/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice;

import com.ffi.api.backoffice.model.ParameterLogin;
import com.ffi.api.backoffice.services.ViewServices;
import com.ffi.paging.Response;
import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Dwi Prasetyo
 */
@RestController
public class IndexController {

    @Autowired
    ViewServices viewServices;

//    @Autowired
//    TransServices transServices;
    @RequestMapping(value = "/halo")
    public @ResponseBody
    Map<String, Object> tes() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("output", "welcome");
        return map;
    }

//    @RequestMapping(value = "/login.action", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Digunakan untuk login department", response = Object.class)
//    @ApiResponses(value = {
//        @ApiResponse(code = 200, message = "OK"),
//        @ApiResponse(code = 404, message = "The resource not found")
//    }
//    )
//    public @ResponseBody
//    ResponseMessage listLoginJson(@RequestBody String param) throws JSONException {
//        Gson gsn = new Gson();
//        String json = "";
//        ParameterLogin balance = gsn.fromJson(param, new TypeToken<ParameterLogin>() {
//        }.getType());
//        Map<String, Object> map1 = new HashMap<String, Object>();
//        Map<String, Object> map = new LinkedHashMap<String, Object>();
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
//        Response res = new Response();
//        ResponseMessage rm = new ResponseMessage();
//
//        list = viewServices.loginJson(balance);
//        //list = prosesServices.loginOutletJson(balance);
//
//        try {
//            if (list.size() > 0) {
//                rm.setSuccess(true);
//                rm.setMessage("Login Success");
//                rm.setItem(list);
////                rm.setItem(flag);
//            } else {
//                rm.setSuccess(false);
//                rm.setMessage("Login Failed");
//                rm.setItem(null);
//            }
//        } catch (Exception e) {
//            rm.setSuccess(false);
//            rm.setMessage("Failed : " + e.getMessage());
//        }
//
//        list.add(map);
//        rm.setItem(list);
//        return rm;
//    }
//    

}
