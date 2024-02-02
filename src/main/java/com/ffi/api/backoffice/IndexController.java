/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffi.api.backoffice.model.DetailOpname;
import com.ffi.api.backoffice.model.HeaderOpname;
import com.ffi.api.backoffice.model.ParameterLogin;
import com.ffi.api.backoffice.services.ProcessServices;
import com.ffi.api.backoffice.services.ViewServices;
import com.ffi.api.backoffice.services.ReportServices;
import com.ffi.api.backoffice.utils.AppUtil;
import com.ffi.paging.Response;
import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @Autowired
    ProcessServices processServices;
    @Autowired
    ReportServices reportServices;
    @Autowired
    AppUtil appUtil;

//    @Autowired
//    TransServices transServices;
    @RequestMapping(value = "/halo")
    public @ResponseBody
    Map<String, Object> tes() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("output", "welcome");
        return map;
    }
////////////UPDATE 27 MAR 23 BY LANI 

    @RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk login department", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    ResponseMessage listLogin(@RequestBody String param) throws JSONException {
        Gson gsn = new Gson();
        ParameterLogin balance = gsn.fromJson(param, new TypeToken<ParameterLogin>() {
        }.getType());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<>();
        ResponseMessage rm = new ResponseMessage();
        rm.setItem(new ArrayList());

        list = viewServices.loginJson(balance);

        /// inactive tidak boleh login by M Joko - 24 Jan 2024
        try {
            if (!list.isEmpty()) {
                Map user = list.get(0);
                if(user.getOrDefault("status", "I").equals("A")){
                    rm.setSuccess(true);
                    rm.setMessage("Login Success");
                    rm.setItem(list);
                } else {
                    rm.setSuccess(false);
                    rm.setMessage("Login failed, User is INACTIVE");
                }
            } else {
                rm.setSuccess(false);
                rm.setMessage("User and Password not match.");
            }
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Failed : " + e.getMessage());
        }

        list.add(map);
        rm.setItem(list);
        return rm;
    }
//////Done
    
    ///////////////new method from dona 27-02-2023////////////////////////////
    //INSERT SUPPLIER===============================================================================================
    @RequestMapping(value = "/insert-supplier", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert supplier", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertSupplier(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.insertSupplier(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed Successfuly: " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    ///////////////done 
    ///////////////new method from dona 28-02-2023////////////////////////////
    @RequestMapping(value = "/list-supplier", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view mpcs", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listSupplier(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.listSupplier(balance));
        return res;

    }

    @RequestMapping(value = "/update-supplier", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk update supplier", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateSupplier(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateSupplier(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed Successfuly: " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }
    ///////////done

    ///////////////new method from dona 03-03-2023////////////////////////////
    @RequestMapping(value = "/list-item-supplier", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view list supplier item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listItemSupplier(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.listItemSupplier(balance));
        return res;
    }

    @RequestMapping(value = "/update-item-supplier", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view list supplier item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateItemSupplier(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateItemSupplier(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed Successfuly: " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @RequestMapping(value = "/insert-item-supplier", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view list supplier item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertItemSupplier(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.insertItemSupplier(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed Successfuly: " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @RequestMapping(value = "/list-master-item", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view master item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listMasterItem(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listMasterItem(balance));
        return res;
    }

    @RequestMapping(value = "/list-supplier-item", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view mpcs", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listDataItemSupplier(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listDataItemSupplier(balance));
        return res;

    }

    ///////////////done 
    ///////////////new method from dona 06-03-2023////////////////////////////
    @RequestMapping(value = "/list-mpcs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view mpcs", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listMpcs(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listMpcs(balance));
        return res;
    }

    @RequestMapping(value = "/update-frayer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk update mpcs", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateFrayer(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateFrayer(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed Successfuly: " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @RequestMapping(value = "/list-itemcost", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view item cost", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody

    Response listItemCost(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.listItemCost(balance));
        return res;
    }

    ///////////////new method from budhi 14-MAR-2023 ////////////// 
    @RequestMapping(value = "/list-menu-group", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan List Group Menu ", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )

    public @ResponseBody
    Response listMenuGroup(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listMenuGroup(balance));
        return res;
    }

    @RequestMapping(value = "/list-item-price", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Harga ITEM ", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )

    public @ResponseBody
    Response listItemPrice(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listItemPrice(balance));
        return res;
    }

    @RequestMapping(value = "/list-item-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Detail Item ", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )

    public @ResponseBody
    Response listItemDetail(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listItemDetail(balance));
        return res;
    }

    @RequestMapping(value = "/list-modifier", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Modifier Item ", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )

    public @ResponseBody
    Response listModifier(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listModifier(balance));
        return res;
    }

    @RequestMapping(value = "/list-special-price", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Modifier Item ", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )

    public @ResponseBody
    Response listSpecialPrice(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listSpecialPrice(balance));
        return res;
    }
    ///////////////done

    ///////////////new method from dona 14-MAR-2023 ////////////// 
    @RequestMapping(value = "/list-master-city", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view master item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listMasterCity(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.listMasterCity(balance));
        return res;
    }

    @RequestMapping(value = "/list-position", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view master item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listPosition(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.listPosition(balance));
        return res;
    }

    @RequestMapping(value = "/list-mpcs-header", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view master item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listMpcsHeader(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listMpcsHeader(balance));
        return res;
    }

    @RequestMapping(value = "/list-master-item-for-supplier", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view master item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listMasterItemSupplier(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.listMasterItemSupplier(balance));
        return res;
    }

    ///////////////done
    ///////////////new method from asep 16-mar-2023 ////////////// 
    //View Outlet================================================================================================
    @RequestMapping(value = "/list-outlet", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view supplier", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listOutlet(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.listOutlet(logan));
        return res;

    }

    @RequestMapping(value = "/list-pos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view supplier", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listPost(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.listPos(logan));
        return res;

    }

    @RequestMapping(value = "/type-pos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view supplier", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listTypePos(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.listTypePos(logan));
        return res;

    }

    @RequestMapping(value = "/item", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view supplier", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listItem(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = viewServices.listItem(logan);
        Response res = new Response();
        res.setData(list);
        res.setRecordsTotal(list.size());
        return res;

    }

    @RequestMapping(value = "/insert-pos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk update mpcs", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertPos(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.insertPos(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed Successfuly: " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @RequestMapping(value = "/update-pos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk update mpcs", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updatePos(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new com.google.gson.reflect.TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updatePos(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed Successfuly: " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    ///////////////done
    ///////////////new method from kevin 16-mar-2023 ////////////// 
    @RequestMapping(value = "/menu-group", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat list menu item beserta harganya", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response listMenuGroups(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listMenuGroups(balance);
        } catch (Exception e) {
        }
        rm.setData(list);
        return rm;
    }

    //Ambil menu
    @RequestMapping(value = "/item-menus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat list menu item beserta harganya", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response listItemMenus(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listItemMenus(balance);
            //rm.setSuccess(true);
            //rm.setMessage("List Menu KFC");
        } catch (Exception e) {
            //rm.setSuccess(false);
            //rm.setMessage("Failed to retrieve menu : " + e.getMessage());
        }
        rm.setData(list);
        return rm;
    }
    ///////////////done

    ///////////////new method from kevin 24-mar-2023 ////////////// 
    //Ambil recipe (header)
    @RequestMapping(value = "/recipe-header", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat master recipe (header)", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewRecipeHeader(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listRecipeHeader(balance);
            rm.setRecordsTotal(list.size());
            rm.setRecordsFiltered(list.size());
            //rm.setSuccess(true);
            //rm.setMessage("List Menu KFC");
        } catch (Exception e) {
            //rm.setSuccess(false);
            //rm.setMessage("Failed to retrieve menu : " + e.getMessage());
        }
        rm.setData(list);
        return rm;
    }

    //Ambil recipe (detail)
    @RequestMapping(value = "/recipe-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat detail recipe", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewRecipeDetail(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listRecipeDetail(balance);
            rm.setRecordsTotal(list.size());
            rm.setRecordsFiltered(list.size());
            //rm.setSuccess(true);
            //rm.setMessage("List Menu KFC");
        } catch (Exception e) {
            //rm.setSuccess(false);
            //rm.setMessage("Failed to retrieve menu : " + e.getMessage());
        }
        rm.setData(list);
        return rm;
    }

    //Ambil recipe (product)
    @RequestMapping(value = "/recipe-product", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat hasil product recipe", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewRecipeProduct(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listRecipeProduct(balance);
            rm.setRecordsTotal(list.size());
            rm.setRecordsFiltered(list.size());
            //rm.setSuccess(true);
            //rm.setMessage("List Menu KFC");
        } catch (Exception e) {
            //rm.setSuccess(false);
            //rm.setMessage("Failed to retrieve menu : " + e.getMessage());
        }
        rm.setData(list);
        return rm;
    }

    /////////////////done
    ///////////////Updated By Pandu 14-03-2023////////////////////////////  
    // ========================================================== MODULE MASTER STAFF (M_STAFF) ==========================================================================================================//   
    //PERCOBAAN VIEW DATA MASTER STAFF (M_STAFF)
    @RequestMapping(value = "/list-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk View Data", response = Object.class)
    @ApiResponses(value
            = {
                @ApiResponse(code = 200, message = "OK"),
                @ApiResponse(code = 404, message = "The resource not found")
            }
    )
    public @ResponseBody

    Response listStaff(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listStaff(balance));
        return res;
    }

    @RequestMapping(value = "/insert-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk Insert Data", response = Object.class)
    @ApiResponses(value
            = {
                @ApiResponse(code = 200, message = "OK"),
                @ApiResponse(code = 404, message = "The resource not found")
            }
    )
    public @ResponseBody
    ResponseMessage insertStaff(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balancetest1 = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.insertStaff(balancetest1);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : Kode User atau ID Card Telah ada");
        }
        rm.setItem(list);
        return rm;
    }

    //PERCOBAAN UPDATE TO TABLE MASTER STAFF (M_STAFF)
    @RequestMapping(value = "/update-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk Update Data", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    ResponseMessage updateStaff(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balancetest = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateStaff(balancetest);
            rm.setSuccess(true);
            rm.setMessage("Update Success");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    //PERCOBAAN DELETE TABLE MASTER STAFF (M_STAFF)
    @RequestMapping(value = "/delete-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk Delete Data", response = Object.class)
    @ApiResponses(value
            = {
                @ApiResponse(code = 200, message = "OK"),
                @ApiResponse(code = 404, message = "The resource not found")
            }
    )
    public @ResponseBody
    ResponseMessage deleteStaff(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balancetest = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.deleteStaff(balancetest);
            rm.setSuccess(true);
            rm.setMessage("Delete Success");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Delete Failed : " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }
    //==================================================================================================================================================================================================//

    //PERCOBAAN VIEW REGION CODE, REGION NAME DI TABLE MASTER GLOBAL (M_GLOBAL)
    @RequestMapping(value = "/list-region", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk View Data", response = Object.class)
    @ApiResponses(value
            = {
                @ApiResponse(code = 200, message = "OK"),
                @ApiResponse(code = 404, message = "The resource not found")
            }
    )
    public @ResponseBody
    Response listRegion(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listRegion(balance));
        return res;
    }

    //PERCOBAAN VIEW OUTLET CODE, OUTLET NAME DI TABLE MASTER GLOBAL (M_GLOBAL)
    @RequestMapping(value = "/list-outlets", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk View Data", response = Object.class)
    @ApiResponses(value
            = {
                @ApiResponse(code = 200, message = "OK"),
                @ApiResponse(code = 404, message = "The resource not found")
            }
    )

    public @ResponseBody
    Response listOutlets(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listOutlets(balance));
        return res;
    }

    //PERCOBAAN VIEW STAFF CODE, STAFF NAME DI TABLE MASTER GLOBAL (M_GLOBAL)
    @RequestMapping(value = "/list-viewformstaff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk View Data", response = Object.class)
    @ApiResponses(value
            = {
                @ApiResponse(code = 200, message = "OK"),
                @ApiResponse(code = 404, message = "The resource not found")
            }
    )
    public @ResponseBody

    Response listViewFormStaff(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listViewFormStaff(balance));
        return res;
    }

    @RequestMapping(value = "/list-viewposition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk View Data", response = Object.class)
    @ApiResponses(value
            = {
                @ApiResponse(code = 200, message = "OK"),
                @ApiResponse(code = 404, message = "The resource not found")
            }
    )
    public @ResponseBody

    Response listViewPosition(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<>();
        Response res = new Response();
        res.setData(viewServices.listViewPosition(balance));
        return res;
    }

    //PERCOBAAN VIEW ACCESS LEVEL -> CODE, DESCRIPTION DI TABLE MASTER GLOBAL (M_GLOBAL) COND = 'CITY';
    @RequestMapping(value = "/list-viewaccesslevel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk View Data", response = Object.class)
    @ApiResponses(value
            = {
                @ApiResponse(code = 200, message = "OK"),
                @ApiResponse(code = 404, message = "The resource not found")
            }
    )
    public @ResponseBody

    Response listViewAccessLevel(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<>();
        Response res = new Response();
        res.setData(viewServices.listViewAccessLevel(balance));
        return res;
    }

    //PERCOBAAN VIEW GROUP USER -> CODE, DESCRIPTION DI TABLE MASTER GLOBAL (M_GLOBAL) COND = 'CITY';
    @RequestMapping(value = "/list-viewgroupuser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk View Data", response = Object.class)
    @ApiResponses(value
            = {
                @ApiResponse(code = 200, message = "OK"),
                @ApiResponse(code = 404, message = "The resource not found")
            }
    )
    public @ResponseBody

    Response listViewGroupUser(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<>();
        Response res = new Response();
        res.setData(viewServices.listViewGroupUser(balance));
        return res;
    }
    ///////////////Done////////////////////////////  

    @RequestMapping(value = "/list-sales-recipe", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk menampilkan list rom", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response listSalesRecipe(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listSalesRecipe(balance));
        return res;
    }

    //////////////// new method sales recipe header by Lani 30-03-2023//////////
    @RequestMapping(value = "/list-sales-recipe-header", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk menampilkan list rom", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response listSalesRecipeHeader(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listSalesRecipeHeader(balance));
        return res;
    }

    /////////////////////////////////Done
    //////////Group Items by Kevin 29-03-2023
    //Ambil menu Group
    @RequestMapping(value = "/menu-items", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat menu item untuk nanti ditampilkan recipe saat order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewMenuItems(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listMenuItem(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    //Ambil menu Group
    @RequestMapping(value = "/group-items", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat recipe menu item saat order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewSalesRecipe(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listGroupItem(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    //////////DONE
    //tambahan outlet asep 29-maret-2023
    //View list area 29-maret 2023================================================================================================
    @RequestMapping(value = "/list-area", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view area code", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response PostViewArea(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.viewArea(logan));
        return res;

    }
    //View list type 29-maret 2023================================================================================================

    @RequestMapping(value = "/list-type-store", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view type store", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response PostViewTypeStore(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.viewTypeStore(logan));
        return res;

    }

    ///////////////new method from dona 29-03-2023////////////////////////////
    @RequestMapping(value = "/list-global", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list fryer global", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listGlobal(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.listGlobal(balance));
        return res;
    }

    @RequestMapping(value = "/insert-fryer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert fryer", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertFryer(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.insertFryer(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed Successfuly: " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    ///////////////done 
    ///////////////NEW METHOD LIST COND AND DATA GLOBAL BY LANI 4 APRIL 2023////
    @RequestMapping(value = "/list-cond-global", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list global cond", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listGlobalCond(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.listGlobalCond(balance));
        return res;
    }

    @RequestMapping(value = "/list-master-global", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list global cond", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listMasterGlobal(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.listMasterGlobal(balance));
        return res;
    }

    @RequestMapping(value = "/insert-master-global", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert master global", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertMasterGlobal(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.insertMasterGlobal(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed Successfuly: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    @RequestMapping(value = "/update-master-global", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk update master global", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateMasterGlobal(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateMasterGlobal(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed Successfuly: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 17 APRIL 2023////
    @RequestMapping(value = "/list-order-header", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list global cond", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listOrderHeader(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.listOrderHeader(balance));
        return res;
    }

    @RequestMapping(value = "/insert-order-header", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert master global", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage inserOrderHeader(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.insertOrderHeader(balance);
            processServices.updateMCounter(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed Successfuly: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }
    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD LIST ORDER HEADER ALL BY DONA 18 APRIL 2023////

    @RequestMapping(value = "/list-order-header-all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list order all", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listOrderHeaderAll(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.listOrderHeaderAll(balance));
        return res;
    }
    /////////////////////////////////DONE///////////////////////////////////////

    ///////////////NEW METHOD LIST ORDER HEADER ALL BY DONA 27 APRIL 2023////
    @RequestMapping(value = "/list-order-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list order detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listOrderDetail(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.listOrderDetail(balance));
        return res;
    }
    /////////////////////////////////DONE///////////////////////////////////////

    ///////////////NEW METHOD LIST ORDER HEADER ALL BY DONA 12 Juli 2023////
    @RequestMapping(value = "/list-order-detail-outlet", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list order detail outlet", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listOrderDetailOutlet(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.listOrderDetailOutlet(balance));
        return res;
    }
    /////////////////////////////////DONE///////////////////////////////////////

    ///////////////NEW METHOD INSERT ORDER DETAIL BY DONA 27 APRIL 2023////
    @RequestMapping(value = "/insert-order-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert order detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage inserOrderDetail(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.insertOrderDetail(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success Successfuly");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed Successfuly: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    @RequestMapping(value = "/update-order-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk update order detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateOrderDetail(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateOrderDetail(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed Successfuly: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD LIST ORDER HEADER ALL BY DONA 2 MEI 2023////
    @RequestMapping(value = "/list-counter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list order detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listCounter(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.listCounter(balance));
        return res;
    }
    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD UPDATE M_COUNTER BY DONA 3 MEI 2023////

    @RequestMapping(value = "/update-master-counter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk update master counter", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateMasterCounter(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateMasterCounter(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed Successfuly: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }
    /////////////////////////////////DONE///////////////////////////////////////

    @RequestMapping(value = "/view-order-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view list supplier item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response ViewOrderDetail(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Response res = new Response();
        res.setData(viewServices.ViewOrderDetail(balance));
        return res;
    }

    @RequestMapping(value = "/list-item-detail-opname", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view list item detail stock opname", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listItemDetailOpname(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listItemDetailOpname(balance));
        return res;
    }

    @RequestMapping(value = "/list-edit-item-detail-opname", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view list item detail stock opname", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listEditItemDetailOpname(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listEditItemDetailOpname(balance));
        return res;
    }

    @RequestMapping(value = "/list-header-opname", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view list item detail stock opname", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listHeaderOpname(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listHeaderOpname(balance));
        return res;
    }

    @RequestMapping(value = "/insert-opname-header", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert transaksi opname header", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage inserOpnameHeader(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        HeaderOpname balance = gsn.fromJson(param, new TypeToken<HeaderOpname>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();

        DateFormat dateFormat = new SimpleDateFormat("MMM");
        DateFormat year = new SimpleDateFormat("YYYY");
        String sDate1 = balance.getOpnameDate();
        Date date1 = new SimpleDateFormat("dd-MMM-yy").parse(sDate1);
        String toMonth = dateFormat.format(date1);
        String toYear = year.format(date1);

        String cekOpnameHdr = viewServices.cekOpname(balance.getOutletCode(), toMonth.toUpperCase(), toYear);
        try {
            if (cekOpnameHdr.equals("0")) {
                processServices.inserOpnameHeader(balance);
                rm.setSuccess(true);
                rm.setMessage("Insert Success");
                map.put("opnameNo", balance.getOpnameNo());
            } else {
                rm.setSuccess(true);
                rm.setMessage("Stock Opname bulan ini sudah dilakukan");
            }
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed: inserOpnameHeader: " + e.getMessage());
        }
        if (rm.getMessage().equals("Insert Success")) {
            processServices.updateMCounterSop(balance.getTransType(), balance.getOutletCode());

        }
        list.add(map);
        rm.setItem(list);
        return rm;
    }

    @RequestMapping(value = "/insert-opname-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert transaksi opname detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage inserOpnameDetail(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        DetailOpname opnameDtls = gsn.fromJson(param, new TypeToken<DetailOpname>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<>();
        ResponseMessage rm = new ResponseMessage();
        long maxValue = 9999999999L;
        String qtyPurch = balance.getOrDefault("qtyPurch", "0");
        String qtyStock = balance.getOrDefault("qtyStock", "0");
        String totalQty = balance.getOrDefault("totalQty", "0");
        if (Long.parseLong(qtyPurch) > maxValue || Long.parseLong(qtyStock) > maxValue || Long.parseLong(totalQty) > maxValue
                || qtyPurch.length() > 14 || qtyStock.length() > 14 || totalQty.length() > 14) {
            rm.setSuccess(false);
            rm.setMessage("Nilai maksimum terlampaui, mohon cek kembali jumlah item.");
            return rm;
        }

        try {
            processServices.inserOpnameDetail(opnameDtls);
            rm.setSuccess(true);
            rm.setMessage("Insert Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed Successfuly: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    @RequestMapping(value = "/insert-stock-card-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert transaksi opname header", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertSoToScDtl(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String status = "";
        ResponseMessage rm = new ResponseMessage();

        try {
            processServices.insertSoToScDtl(balance);
            rm.setSuccess(true);

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Stock Card Failed: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    @RequestMapping(value = "/cek-sync-item", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert transaksi opname header", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage cekSyncItem(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String status = "";
        ResponseMessage rm = new ResponseMessage();

        String totalHq = viewServices.cekItemHq();

        String cekItem = viewServices.cekItem();
        int b = Integer.valueOf(cekItem);

        try {
            if (b == 0) {
                rm.setSuccess(true);
                rm.setMessage("Template Sudah Update");
            } else {
                rm.setSuccess(false);
                rm.setMessage("Template Belum Update");
            }
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Stock Card Failed: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    @RequestMapping(value = "/send-data-to-warehouse", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert transaksi opname header", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage sendDataToWarehouse(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String status = "";
        ResponseMessage rm = new ResponseMessage();

        try {
            processServices.sendDataToWarehouse(balance);

            rm.setSuccess(true);
            rm.setMessage("Insert Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    ///////////////////////////////Add Receiving by KP (06-06-2023)///////////////////////////////
    @RequestMapping(value = "/view-rcv-header", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view list header receiving", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response ViewReceivingHeader(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listReceivingHeader(balance);
            rm.setRecordsTotal(list.size());
            rm.setRecordsFiltered(list.size());
            //rm.setSuccess(true);
            //rm.setMessage("List Menu KFC");
        } catch (Exception e) {
            rm.setRecordsTotal(0);
            rm.setRecordsFiltered(0);
            //rm.setSuccess(false);
            //rm.setMessage("Failed to retrieve menu : " + e.getMessage());
        }
        rm.setData(list);
        return rm;
    }

    @RequestMapping(value = "/view-ord-header", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view list header order (ditampilkan untuk menu Receiving)", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response ViewOrderHeader(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listUnfinishedOrderHeader(balance);
            rm.setRecordsTotal(list.size());
            rm.setRecordsFiltered(list.size());
            //rm.setSuccess(true);
            //rm.setMessage("List Menu KFC");
        } catch (Exception e) {
            rm.setRecordsTotal(0);
            rm.setRecordsFiltered(0);
            //rm.setSuccess(false);
            //rm.setMessage("Failed to retrieve menu : " + e.getMessage());
        }
        rm.setData(list);
        return rm;
    }

    @RequestMapping(value = "/view-rcv-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view list header order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response ViewReceivingDetail(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listReceivingDetail(balance);
            rm.setRecordsTotal(list.size());
            rm.setRecordsFiltered(list.size());
            //rm.setSuccess(true);
            //rm.setMessage("List Menu KFC");
        } catch (Exception e) {
            rm.setRecordsTotal(0);
            rm.setRecordsFiltered(0);
            //rm.setSuccess(false);
            //rm.setMessage("Failed to retrieve menu : " + e.getMessage());
        }
        rm.setData(list);
        return rm;
    }

    //Add Insert to Receiving Header & Detail by KP (07-06-2023)
    @RequestMapping(value = "/insert-rcv-headetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view list header order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage InsertRecvHeaderDetail(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        JsonObject result = gsn.fromJson(param, JsonObject.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.InsertRecvHeaderDetail(result);
            //prosesServices.updateMCounter(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed Successfuly: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    ///////////////NEW METHOD LIST ORDER DETAIL SUPPLIER BY DONA 13 JUL 2023////
    @RequestMapping(value = "/list-order-detail-supplier", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list order detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listOrderDetailSupplier(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.listOrderDetailSupplier(balance));
        return res;
    }
    /////////////////////////////////DONE///////////////////////////////////////

    ///////////////////////////////Add Wastage by KP (31-07-2023)///////////////////////////////
    //Ambil Wastage Header
    @RequestMapping(value = "/wastage-header", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat data wastage (header)", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewWastageHeader(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listWastageHeader(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    //Ambil Wastage Detail
    @RequestMapping(value = "/wastage-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat data wastage (detail)", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewWastageDetail(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listWastageDetail(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    //list data M_OUTLET yang tipenya HO
    @RequestMapping(value = "/list-outlet-report")
    @ApiOperation(value = "Mepampilkan list outlet untuk report", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "ok"),
        @ApiResponse(code = 400, message = "The resource not found")})
    public @ResponseBody
    Response reportOrderEntry(@RequestBody String param) throws IOException {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listOutletReport(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    //Add Insert to Receiving Header & Detail by KP (07-06-2023)
    @RequestMapping(value = "/insert-wastage-headetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert header dan detail Wastage", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage InsertWastageHeaderDetail(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        JsonObject result = gsn.fromJson(param, JsonObject.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.InsertWastageHeaderDetail(result);
            //prosesServices.updateMCounter(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Wastage Successfuly");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Wastage Failed: " + e.getMessage());
            System.err.println(e);
        }
        rm.setItem(list);
        return rm;
    }

    ///////////////NEW METHOD INSERT ORDER DETAIL BY DONA 7 AUG 2023////
    @RequestMapping(value = "/list-stock-opname", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list stock opname", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listStockOpname(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.listStockOpname(balance));
        return res;
    }

    //Add Insert to MPCS Template by KP (07-06-2023)
    @RequestMapping(value = "/mpcs-template", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert MPCS Template awal", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage InsertMPCSTemplate(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        JsonObject result = gsn.fromJson(param, JsonObject.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.InsertMPCSTemplate(result);
            //prosesServices.updateMCounter(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert MPCS Template Successfuly");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert MPCS Template Failed: " + e.getMessage());
            System.err.println(e);
        }
        rm.setItem(list);
        return rm;
    }

    @RequestMapping(value = "/view-mpcs-temp", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view MPCS Template", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response ViewMpcsTemplate(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listTemplateMpcs(balance);
            rm.setRecordsTotal(list.size());
            rm.setRecordsFiltered(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
            rm.setRecordsFiltered(0);
        }
        rm.setData(list);
        return rm;
    }

    @RequestMapping(value = "/view-mpcs-plan", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view MPCS Plan / Projection", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response ViewMpcsProject(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listProjectMpcs(balance);
            rm.setRecordsTotal(list.size());
            rm.setRecordsFiltered(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
            rm.setRecordsFiltered(0);
        }
        rm.setData(list);
        return rm;
    }

    @RequestMapping(value = "/mpcs-project", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert MPCS Plan / Project", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage InsertUpdateMPCSProject(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        JsonObject result = gsn.fromJson(param, JsonObject.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.InsertUpdateMPCSProject(result);
            //prosesServices.updateMCounter(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert MPCS Project Successfuly");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert MPCS Project Failed: " + e.getMessage());
            System.err.println(e);
        }
        rm.setItem(list);
        return rm;
    }

    ///////////////////////////////Add Return Order by Pasca (10-08-2023)///////////////////////////////
    //Ambil Return Order Header
    @RequestMapping(value = "/return-order-header", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat data return order (header)", response = Object.class)
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
            list = viewServices.listReturnOrderHeader(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    //Ambil Return Order Detail
    @RequestMapping(value = "/return-order-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat data return order (detail)", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewReturnOrderDetail(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listReturnOrderDetail(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    @RequestMapping(value = "/list-supplier-gudang-return", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat list supplier dan gudang di return order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewSupplierAndGudangReturnOrder(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listSupplierGudangReturnOrder(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    @RequestMapping(value = "/list-item-supplier-gudang-return", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat list item supplier dan gudang di return order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response viewItemSupplierAndGudangReturnOrder(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response rm = new Response();
        try {
            list = viewServices.listItemSupplierGudangReturnOrder(balance);
            rm.setData(list);
            rm.setRecordsTotal(list.size());
        } catch (Exception e) {
            rm.setRecordsTotal(0);
        }
        return rm;
    }

    //Add Insert to Return Order Header & Detail by Pasca (15-08-2023)
    @RequestMapping(value = "/insert-return-order-headetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert header dan detail Return Order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage InsertReturnOrderHeaderDetail(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        JsonObject balance = gsn.fromJson(param, JsonObject.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        if (balance.has("kirim") && balance.has("returnNo")) {
            // kirim 
            // todo:
            if (processServices.sendReturnOrderToWH(balance)) {
                rm.setSuccess(true);
                rm.setMessage("Send Return Order Successfuly");
            } else {
                rm.setSuccess(false);
                rm.setMessage("Send Return Order Failed");
            }
        } else {
            try {
                processServices.insertReturnOrderHeaderDetail(balance);
                rm.setSuccess(true);
                rm.setMessage("Insert Return Order Successfuly");
            } catch (Exception e) {
                rm.setSuccess(false);
                rm.setMessage("Insert Return Order Failed: " + e.getMessage());
                System.err.println(e);
            }
            rm.setItem(list);
        }
        return rm;
    }

    //////////////////////New Method Generate Template Stock Opname 15 AUG 2023 ////////////////////////
    @RequestMapping(value = "/update-template-stock-opname", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert order header dan detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateTemplateStockOpnameHeader(@RequestBody String param) throws IOException, Exception {
        ResponseMessage rm = new ResponseMessage();
        boolean hdr = false;
        boolean dtl = false;
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        JsonObject result = gsn.fromJson(param, JsonObject.class);

        //Header
        Map<String, String> headerParam = new HashMap<String, String>();
        String cdTempl = result.getAsJsonObject().getAsJsonPrimitive("cdTemplate").getAsString();
        String tempName = result.getAsJsonObject().getAsJsonPrimitive("templateName").getAsString();;
        String usrpd = result.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString();
        headerParam.put("cdTemplate", cdTempl);
        headerParam.put("templateName", tempName);
        headerParam.put("status", result.getAsJsonObject().getAsJsonPrimitive("status").getAsString());
        headerParam.put("userUpd", usrpd);
        try {
            processServices.updateTemplateStockOpnameHeader(headerParam);
            hdr = true;
            System.out.println("Success Insert Header!");
        } catch (Exception e) {
            hdr = false;
            System.out.println("Exception: " + e);
        }
        //Details
        JsonArray emp = result.getAsJsonObject().getAsJsonArray("itemList");
        for (int i = 0; i < emp.size(); i++) {
            Map<String, String> detailParam = new HashMap<String, String>();
            detailParam.put("cdTemplate", cdTempl);
            detailParam.put("itemCode", emp.get(i).getAsJsonObject().getAsJsonPrimitive("itemCode").getAsString());
            detailParam.put("stat", emp.get(i).getAsJsonObject().getAsJsonPrimitive("stat").getAsString());
            detailParam.put("userUpd", usrpd);
            try {
                processServices.updateTemplateStockOpnameDetail(detailParam);
                dtl = true;
                System.out.println("Success Insert Detail ke-" + i);
            } catch (Exception e) {
                dtl = false;
                System.out.println("Exception: " + e);
            }
            detailParam.clear();
        }

        if (hdr && dtl) {
            rm.setSuccess(true);
            rm.setMessage("Insert Done Successfuly");
        } else {
            rm.setSuccess(false);
            rm.setMessage("Failed to Insert");
        }

        return rm;
    }

    ///////////////new method from dona 23-08-2023////////////////////////////
    @RequestMapping(value = "/insert-master-item", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert master item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertMasterItem(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        JsonObject result = gsn.fromJson(param, JsonObject.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.insertMasterItem(result);
            //prosesServices.updateMCounter(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Master Item Successfuly");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Master Item Failed: " + e.getMessage());
            System.err.println(e);
        }
        rm.setItem(list);
        return rm;
    }

    ///////////////done 
    ///////////////new method transfer master from dona 23-08-2023////////////////////////////
    @RequestMapping(value = "/process-transfer-masters", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert master item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage processTransferMasters(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        JsonObject result = gsn.fromJson(param, JsonObject.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.processTransferMasters(result);
            //prosesServices.updateMCounter(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Master Color Successfuly");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Master Color Failed: " + e.getMessage());
            System.err.println(e);
        }
//        try {
//            processServices.insertUpdateMasterDiscountMethod(result);
//            //prosesServices.updateMCounter(balance);
//            rm.setSuccess(true);
//            rm.setMessage("Insert Master Discount Mehtod Successfuly");
//        } catch (Exception e) {
//            rm.setSuccess(false);
//            rm.setMessage("Insert Master Discount Mehtod  Failed: " + e.getMessage());
//            System.err.println(e);
//        }
//
        rm.setItem(list);
        return rm;

    }

    ///////////////done 
    ///////////////new method updateStatusOpname 6-11-2023////////////////////////////
    @RequestMapping(value = "/update-opname-status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk update status opname", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateOpnameStatus(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());
        List list = new ArrayList();
        ResponseMessage rm = new ResponseMessage();
        try {
            list = processServices.updateOpnameStatus(balance);
            if (list.isEmpty()) {
                rm.setSuccess(true);
                rm.setMessage("Update Success");
            } else {
                rm.setSuccess(false);
                rm.setMessage(list.size() + " item berikut terdeteksi memiliki nilai input total 0");
            }
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("updateOpnameStatus Failed: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }
    ///////////////done 

    ///////////////NEW METHOD LIST ORDER DETAIL SPECIFIC ORDER ///////////////
    @RequestMapping(value = "/list-order-by-orderno", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view data orderno", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody

    Response listDetailOderbyOrderno(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Response res = new Response();
        res.setData(viewServices.listDetailOderbyOrderno(balance));
        return res;
    }
    ///////////////done  

    ////////////New method for query stock card - Fathur 29-Nov-2023////////////
    @RequestMapping(value = "/stock-card", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view data query stock card", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )

    public @ResponseBody
    Response listQueryStockCard(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Response res = new Response();
        res.setData(viewServices.listQueryStockCard(balance));
        return res;
    }
    ////////////Done method for query stock card////////////

    ////////////New method for query stock card detail - Fathur 30-Nov-2023////////////
    @RequestMapping(value = "/stock-card-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view data query stock card detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )

    public @ResponseBody
    Response listQueryStockCardDetail(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        Response res = new Response();
        res.setData(viewServices.listQueryStockCardDetail(balance));
        return res;
    }
    ////////////Done method for query stock card detail////////////

    ///////////////  NEW METHOD LIST MENU GROUP CODE  BY DONA 4 DEC 2023////////////////////
    @RequestMapping(value = "/list-menu-group-code-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Data Menu Item Code ", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )

    public @ResponseBody
    Response listMenuGroupCodeDetail(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listMenuGroupCodeDetail(balance));
        return res;
    }
    /////////////////////////////DONE//////////////////////////////////// 

    ////////////New method for Last Eod by Outlet - M Joko 4-Dec-2023////////////
    @RequestMapping(value = "/last-eod", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk ambil data EOD terakhir dan POS yg belum complete di outlet", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response lastEod(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> posOpened = viewServices.listPosOpen(balance);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> lastEod = viewServices.lastEod(balance);
        Map<String, Object> eod = lastEod.get(0);
        eod.put("posOpened", posOpened);
        data.add(eod);
        Response res = new Response();
        res.setData(data);
        return res;
    }
    ////////////Done method for Last Eod////////////

    ////////////New method for Transfer master outlet - Fathur 11 Dec 2023////////////
    @RequestMapping(value = "/send-data-outlet-to-warehouse", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert transaksi opname header", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )

    public @ResponseBody
    ResponseMessage sendDataOutletToWarehouse(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String status = "";
        ResponseMessage rm = new ResponseMessage();

        try {
            processServices.sendDataOutletToWarehouse(balance);

            rm.setSuccess(true);
            rm.setMessage("Insert Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    ////////////New method for Process EOD - M Joko 8-Dec-2023////////////
    @RequestMapping(value = "/process-eod", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk process data EOD sesuai trans_date Outlet dan POS yg masih open", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Failed"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response processEod(@RequestBody String param) throws IOException, Exception {
        long startTime = System.currentTimeMillis();
        System.err.println("Start End of Day Process: " + startTime);
        Gson gsn = new Gson();
        Response res = new Response();
        List errors = new ArrayList();
        List posOpen = new ArrayList();
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> d = new HashMap();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        if (!balance.containsKey("outletCode") && !balance.containsKey("userUpd")) {
            errors.add("Missing columns: outletCode, userUpd");
            d.put("errors", errors);
            d.put("message", "Missing columns: outletCode, userUpd");
            data.add(d);
            res.setData(data);
            return res;
        }
        List<Map<String, Object>> listMPosActive = viewServices.listMPosActive(balance);
        List<Map<String, Object>> lastEod = viewServices.lastEod(balance);
        Map<String, Object> prevEod = lastEod.get(0);

        for (int i = 0; i < listMPosActive.size(); i++) {
            var mPos = listMPosActive.get(i);
            String posCode = mPos.get("posCode").toString();
            if (posCode.length() < 1) {
                errors.add("! posCode kosong ");
            } else {
                balance.put("posCode", posCode);
                List<Map<String, Object>> listPosOpen = viewServices.listPosOpen(balance);
                if (listPosOpen.size() == 1) {
                    Map<String, Object> pos = listPosOpen.get(0);
                    if (pos.get("processEod") != "Y") {
                        posOpen.add(pos);
                        errors.add("! pos Open: " + pos.get("posCode"));
                    }
                } else if (listPosOpen.size() < 1) {
                    errors.add("! listPosOpen kosong: " + posCode);
                    // insert pos ke eod 'N'
                    Map<String, String> newParams = new HashMap();
                    newParams.put("regionCode", mPos.get("regionCode").toString());
                    newParams.put("outletCode", mPos.get("outletCode").toString());
                    newParams.put("posCode", mPos.get("posCode").toString());
                    newParams.put("processEod", "N");
                    newParams.put("notes", " ");
                    newParams.put("userUpd", balance.getOrDefault("userUpd", "SYSTEM"));
                    try {
                        processServices.insertEodPosN(newParams);
                    } catch (Exception e) {
                        errors.add("insertEodPosN failed: " + e.getMessage());
                    }
                    posOpen.add(newParams);
                } else {
                    errors.add("! listPosOpen size: " + listPosOpen.size());
                }
            }
        }

        d.put("errors", errors);
        d.put("prevEod", prevEod);
        d.put("listMPosActive", listMPosActive);
        d.put("listPosOpen", posOpen);
        d.put("message", "Process End of Day Success");

        if (!errors.isEmpty()) {
            d.put("message", "Process End of Day Failed");
            data.add(d);
            res.setData(data);
            return res;
        }
        try {
            processServices.insertTStockCard(balance);
        } catch (Exception e) {
            errors.add("insertTStockCard failed: " + e.getMessage());
        }
        try {
            processServices.insertTEodHist(balance);
        } catch (Exception e) {
            errors.add("insertTEodHist failed: " + e.getMessage());
        }
        try {
            processServices.insertTSummMpcs(balance);
        } catch (Exception e) {
            errors.add("insertTSummMpcs failed: " + e.getMessage());
        }
        try {
            processServices.updateOrderEntryExpired(balance);
        } catch (Exception e) {
            errors.add("updateOrderEntryExpired failed: " + e.getMessage());
        }
        try {
            processServices.increaseTransDateMOutlet(balance);
        } catch (Exception e) {
            errors.add("increaseTransDateMOutlet failed: " + e.getMessage());
        }

        data.add(d);
        res.setData(data);
        double elapsedTimeSeconds = (double) (System.currentTimeMillis() - startTime) / 1000.0;
        System.err.println("Finished End of Day Process after: " + elapsedTimeSeconds + " seconds");
        res.setDraw((int) elapsedTimeSeconds);
        return res;
    }
    ////////////Done method for process Last Eod///////////

    ////////////New method for List Receiving all - Dani 12 Dec 2023////////////
    @RequestMapping(value = "/list-receiving-all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list receving all", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listReceivingAll(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.listReceivingAll(balance));
        return res;
    }

    ///////////////  NEW METHOD LIST MPCS PLAN  BY DONA 12 DEC 2023////////////////////
    @RequestMapping(value = "/list-mpcs-plan", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Data MPCS Plan ", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )

    public @ResponseBody
    Response listMpcsPlan(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listMpcsPlan(balance));
        return res;
    }
    /////////////////////////////DONE//////////////////////////////////// 

    ///////////////  New Method List MPCS Production By Fathur 13 Dec 2023 ////////////////////
    @RequestMapping(value = "/list-mpcs-production", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Data MPCS Production ", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )

    public @ResponseBody
    Response listMpcsProduction(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listMpcsProduction(balance));
        return res;
    }
    //////////////////Done Method List MPCS Production //////////////////////////// 

    ///////////////  New Method MPCS Production Detail By Fathur 13 Dec 2023 ////////////////////
    @RequestMapping(value = "/mpcs-production-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Data MPCS Production Detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )

    public @ResponseBody
    Response mpcsProductionDetail(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.mpcsProductionDetail(balance));
        return res;
    }
    ////////////////// Done Method MPCS Production Detail //////////////////////////// 

    ///////////////  New Method MPCS Production Recipe By Fathur 13 Dec 2023 ////////////////////
    @RequestMapping(value = "/mpcs-production-recipe", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Data MPCS Production Recipe", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )

    public @ResponseBody
    Response mpcsProductionRecipe(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.mpcsProductionRecipe(balance));
        return res;
    }
    ////////////////// Done Method MPCS Production Recipe //////////////////////////// 

    ///////////////  New Method MPCS Production Product Result By Fathur 13 Dec 2023 ////////////////////
    @RequestMapping(value = "/mpcs-production-product-result", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan Data MPCS Production Hasil Produk", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )

    public @ResponseBody
    Response mpcsProductionProductResult(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.mpcsProductionProductResult(balance));
        return res;
    }
    ////////////////// Done Method MPCS Production Recipe //////////////////////////// 

    ///////////////  NEW METHOD UPDATE MPCS PLAN  BY DONA 14 DEC 2023////////////////////
    @RequestMapping(value = "/update-mpcs-plan", produces = MediaType.APPLICATION_JSON_VALUE)

    @ApiOperation(value = "Digunakan untuk update MPCS plan", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateMpcsPlan(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateMpcsPlan(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed Successfuly: " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }
    /////////////////////////////DONE//////////////////////////////////// 

    // By Dani 18 Des 2023
    @RequestMapping(value = "/get-order-detail-inventory", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mendapatkan detail order dari database inventory gudang", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response getOrderDetailFromInventory(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response rm = new Response();
        rm.setData(this.viewServices.getOrderDetailFromInventory(balance));
        return rm;
    }

    // query list delivery order By Dani 18 Des 2023
    @RequestMapping(value = "/list-delivery-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "query list delivery order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listDeliveryOrderHdr(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response rm = new Response();
        rm.setData(this.viewServices.listDeliveryOrderHdr(balance));
        return rm;
    }

    // query list m_outlet HO By Dani 18 Des 2023
    @RequestMapping(value = "/list-outlet-ho", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view supplier", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listOutletHo(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.listOutletHo(logan));
        return res;
    }

    // query insert update Delivery order By Dani 27 Des 2023
    @RequestMapping(value = "/insert-update-delivery-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert delivery order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertUpdateDeliveryOrder(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> data = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        var rm = new ResponseMessage();
        try {
            processServices.insertUpdateDeliveryOrder(data);
            rm.setSuccess(true);
            rm.setMessage("Success Successfuly");

        } catch (Exception e) {
            e.printStackTrace();
            rm.setSuccess(false);
            rm.setMessage("Failed: " + e.getMessage());
        }
        return rm;
    }

    // query to get Delivery order By Dani 27 Des 2023
    @RequestMapping(value = "/get-delivery-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert delivery order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage getDeliveryOrder(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> data = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());

        var rm = new ResponseMessage();
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(viewServices.getDeliveryOrder(data));
            rm.setSuccess(true);
            rm.setMessage("Success Successfuly");
            rm.setItem(list);

        } catch (Exception e) {
            e.printStackTrace();
            rm.setSuccess(false);
            rm.setMessage("Failed: " + e.getMessage());
        }
        return rm;
    }

    // query to kirim Delivery order By Dani 28 Des 2023
    @RequestMapping(value = "/kirim-delivery-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert delivery order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage kirimDeliveryOrder(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> data = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());

        var rm = new ResponseMessage();
        try {
            processServices.kirimDeliveryOrder(data);
            rm.setSuccess(true);
            rm.setMessage("Success Successfuly");
        } catch (Exception e) {
            e.printStackTrace();
            rm.setSuccess(false);
            rm.setMessage("Failed: " + e.getMessage());
        }
        return rm;
    }

    // get to Delivery order outlet to outlet from warehouse By Dani 28 Des 2023
    @RequestMapping(value = "/get-list-outlet-order-warehouse", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan get list order outlet to outlet from warehouse", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage getListOrderOutletHeaderWarehouse(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> data = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());

        var rm = new ResponseMessage();
        try {
            rm.setSuccess(true);
            rm.setMessage("Success Successfuly");
            rm.setItem(viewServices.getListOrderOutletHeaderWarehouse(data));
        } catch (Exception e) {
            e.printStackTrace();
            rm.setSuccess(false);
            rm.setMessage("Failed: " + e.getMessage());
        }
        return rm;
    }

    //////// NEW METHOD to get detail outlet to outlet from warehouse  BY DANI 28 DEC 2023
    @RequestMapping(value = "/get-outlet-order-warehouse", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan get list order outlet to outlet from warehouse", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage getOrderOutletWarehouse(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> data = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());

        var rm = new ResponseMessage();
        try {
            rm.setSuccess(true);
            rm.setMessage("Success Successfuly");
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(viewServices.getOrderOutletWarehouse(data));
            rm.setItem(list);
        } catch (Exception e) {
            e.printStackTrace();
            rm.setSuccess(false);
            rm.setMessage("Failed: " + e.getMessage());
        }
        return rm;
    }
    // Done

    // Get Order Entry status from inv by Fathur 29 Dec 2023 // 
    @RequestMapping(value = "/get-order-entry-status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk get status delivery order from inv", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Map<String, Object> getOrderEntryStatusFromInv(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> data = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());
        return viewServices.getOrderEntryStatusFromInv(data);
    }
    // Done get Order Entry status from inv // 

    // Get List Daftar Menu Report  By Rafi 29 Des 2023
    @RequestMapping(value = "/get-list-daftar-menu-report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan get list report daftar menu", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage getListDaftarMenuReport() throws JRException, IOException, Exception {

        var rm = new ResponseMessage();
        try {
            rm.setSuccess(true);
            rm.setMessage("Success Successfuly");
            rm.setItem(viewServices.getListDaftarMenuReport());
        } catch (Exception e) {
            e.printStackTrace();
            rm.setSuccess(false);
            rm.setMessage("Failed: " + e.getMessage());
        }
        return rm;
    }

    @RequestMapping(value = "/get-outlet-info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk ambil data outlet di halaman login by M Joko - 4 Jan 2024", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage outletInfo(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        ResponseMessage rm = new ResponseMessage();
        if (!balance.containsKey("outletCode")) {
            rm.setSuccess(false);
            rm.setMessage("Get Failed: outletCode required");
            return rm;
        }
        try {
            rm.setItem(viewServices.outletInfo(balance.get("outletCode")));
            rm.setSuccess(true);
            rm.setMessage("Get Success");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Get Failed: " + e.getMessage());
        }
        return rm;
    }

    // Get List MPCS Group by Dani 4 Januari 2024
    @RequestMapping(value = "/list-mpcs-group", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view master mpcs group", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listMpcsGroup(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> data = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());
        List<Map<String, Object>> list = viewServices.listMpcsGroup(data);
        Response res = new Response();
        res.setData(list);
        res.setRecordsTotal(list.size());
        res.setRecordsFiltered(list.size());
        return res;
    }

    // Get List MPCS Query result by Dani 4 Januari 2024
    @RequestMapping(value = "/mpcs-query-result", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view master mpcs group", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listMpcsQueryResult(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> data = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());
        List<Object> list = new ArrayList<>();
        list.add(viewServices.listMpcsQueryResult(data));
        Response res = new Response();
        res.setData(list);
        res.setRecordsTotal(list.size());
        res.setRecordsFiltered(list.size());
        return res;
    }

    @RequestMapping(value = "/insert-mpcs-production", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk menambah mpcs produksi bedasarkan waktu, update akumulasi kuantitas dan menambah data MPCS history by Fathur 8 Jan 2024", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertMpcsProduction(@RequestBody String params) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> data = gsn.fromJson(params, new TypeToken<Map<String, String>>() {
        }.getType());
        ResponseMessage rm = new ResponseMessage();
        if (processServices.insertMpcsProduction(data)) {
            rm.setSuccess(true);
            rm.setMessage("Insert Successfuly");
        } else {
            rm.setSuccess(false);
            rm.setMessage("Insert  Failed");
        }
        return rm;
    }

    /////// NEW METHOD to get list Menu Aplikasi by M Joko 8 Januari 2024
    @RequestMapping(value = "/list-menu-application", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list Menu Aplikasi", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listMenuApplication(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> data = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());
        // List<Map<String, Object>> list = viewServices.listMenuApplication(data);
        String env = data.containsKey("env") ? data.getOrDefault("env", "prod").toString() : appUtil.getOrDefault("app.env", "development");
        Response res = new Response();
        String filepath = env.toLowerCase().contains("dev") ? "json/menuKFC-dev.json" : "json/menuKFC.json";
        System.err.println("menu filepath: " + filepath);
        if (data.containsKey("outletBrand") && data.get("outletBrand").toString().equalsIgnoreCase("TACOBELL")) {
            filepath = "json/menuTACOBELL.json";
        }
        try {
            ClassPathResource resource = new ClassPathResource(filepath);
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String jsonString = new String(bytes, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> menuList = objectMapper.readValue(jsonString, new ArrayList<LinkedHashMap<String, Object>>().getClass());
            res.setData(menuList);
        } catch (IOException e) {
            System.err.println("e: " + e.getMessage());
            List<Map<String, Object>> menuList = new ArrayList();
            data.put("success", false);
            data.put("message", e.getMessage());
            menuList.add(data);
            res.setData(menuList);
        }
        return res;
    }

    // Get List Daftar Menu Report  By Rafi 9 Jan 2024
    @RequestMapping(value = "/get-list-item-detail-report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan get list report selected by item detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage getListItemDetailReport() throws JRException, IOException, Exception {

        var rm = new ResponseMessage();
        try {
            rm.setSuccess(true);
            rm.setMessage("Success Successfuly");
            rm.setItem(viewServices.getListItemDetailReport());
        } catch (Exception e) {
            e.printStackTrace();
            rm.setSuccess(false);
            rm.setMessage("Failed: " + e.getMessage());
        }
        return rm;
    }

    /////// NEW METHOD to get list Customer Name report DP by Dani 9 Januari 2024
    @RequestMapping(value = "/list-cust-name-dp-report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list customer name di DP Report", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listCustomerNameReportDp(@RequestBody String param) throws IOException, Exception {
        Response res = new Response();
        res.setData(viewServices.listCustomerNameReportDp());
        return res;
    }

    /////// NEW METHOD to get list Customer Name report DP by Dani 9 Januari 2024
    @RequestMapping(value = "/list-ordertype-dp-report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list order type di DP Report", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listOrderTypeReportDp(@RequestBody String param) throws IOException, Exception {
        Response res = new Response();
        res.setData(viewServices.listOrderTypeReportDp());
        return res;
    }

    @RequestMapping(value = "/delete-mpcs-production", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk menghapus mpcs produksi detail by Fathur 11 Jan 2024", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage deleteMpcsProduction(@RequestBody String params) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> data = gsn.fromJson(params, new TypeToken<Map<String, String>>() {
        }.getType());
        ResponseMessage rm = new ResponseMessage();
        if (processServices.deleteMpcsProduction(data)) {
            rm.setSuccess(true);
            rm.setMessage("Delete Successfuly");
        } else {
            rm.setSuccess(false);
            rm.setMessage("Delete  Failed");
        }
        return rm;
    }

    @RequestMapping(value = "/get-id-absensi", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk mengambil data user absensi by id by M Joko 16 Jan 2024", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage getIdAbsensi(@RequestBody String params) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> data = gsn.fromJson(params, new TypeToken<Map<String, String>>() {
        }.getType());
        ResponseMessage rm = new ResponseMessage();
        List<Map<String, Object>> list = viewServices.getIdAbsensi(data);
        if (!list.isEmpty()) {
            Map first = list.get(0);
            rm.setItem(list);
            rm.setSuccess(true);
            rm.setMessage(((first.get("daySeq").toString().equalsIgnoreCase("0") && first.get("seqNo").toString().equalsIgnoreCase("0")) || list.size() % 2 == 0 ? "Masuk" : (list.size() % 2 != 0 ? "Keluar" : "User Found")));
            return rm;
        }
        rm.setSuccess(false);
        rm.setMessage("User Not Found");
        rm.setItem(new ArrayList());
        return rm;
    }

    @RequestMapping(value = "/insert-absensi", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk menyimpan data user absensi by id by M Joko 16 Jan 2024", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertAbsensi(@RequestBody String params) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> data = gsn.fromJson(params, new TypeToken<Map<String, Object>>() {
        }.getType());
        ResponseMessage rm = new ResponseMessage();
        boolean b = processServices.insertAbsensi(data);
        rm.setItem(new ArrayList());
        rm.setSuccess(b);
        rm.setMessage(b ? "Absensi berhasil." : "Periksa kembali password anda.");
        return rm;
    }
    
    ///////////////new method Management Fryer from aditya 29-01-2024////////////////////////////
    //INSERT MANAGEMENT FRYER===============================================================================================
    @RequestMapping(value = "/insert-mpcs-management-fryer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert MPCS Management Fryer", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertMpcsManagementFryer(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        JsonObject balance = gsn.fromJson(param, JsonObject.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        
        try {
            processServices.insertMpcsManagementFryer(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success Successfuly");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
            System.err.println(e);
        }

        rm.setItem(list);

        return rm;
    }
    ///////////////done 
    ///////////////new method list fryer from aditya 30-01-2024////////////////////////////
    @RequestMapping(value = "/list-fryer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view Fryer", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listFryer(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listFryer(balance));
        return res;
    }
    ///////////////done
    ///////////////new method list fryer from aditya 30-01-2024////////////////////////////
    @RequestMapping(value = "/list-management-fryer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view Fryer", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listManagementFryer(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listManagementFryer(balance));
        return res;
    }
    ///////////////done
    
    ///////////////new method list fryer from aditya 30-01-2024////////////////////////////
    @RequestMapping(value = "/list-mpcs-management-fryer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view Fryer", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listMpcsManagementFryer(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        res.setData(viewServices.listMpcsManagementFryer(balance));
        return res;
    }
    ///////////////done
    
    
    // =============== New Method From Lukas 17-10-2023 ===============
    // =============== Method Copy Data Local All / Selected / Single ===============
    @RequestMapping(value = "/copy-all", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage copyAll(@RequestBody Map<String, Object> param) throws IOException, Exception {
        ResponseMessage rm = new ResponseMessage();
        List<String> listTable = new ArrayList<>();
        listTable.add("M_COLOR");
        listTable.add("M_DISCOUNT_METHOD");
        listTable.add("M_DISCOUNT_METHOD_LIMIT");
        listTable.add("M_DONATE_METHOD");
        listTable.add("M_DONATE_METHOD_LIMIT");
        listTable.add("M_GLOBAL");
        listTable.add("M_GROUP_ITEM");
        listTable.add("M_ITEM");
        listTable.add("M_ITEM_COST");
//        listTable.add("M_LEVEL_1");
//        listTable.add("M_LEVEL_2");
//        listTable.add("M_LEVEL_3");
//        listTable.add("M_LEVEL_4");
        listTable.add("M_MENU_GROUP");
        listTable.add("M_MENU_GROUP_LIMIT");
        listTable.add("M_MENU_ITEM");
        listTable.add("M_MENU_ITEM_LIMIT");
        listTable.add("M_MENU_ITEM_SCHEDULE");
        listTable.add("M_MENU_SET");
        listTable.add("M_MODIFIER_ITEM");
        listTable.add("M_MODIFIER_PRICE");
        listTable.add("M_MPCS_HEADER");
        listTable.add("M_MPCS_DETAIL");
//        listTable.add("M_OPNAME_TEMPL_HEADER");
//        listTable.add("M_OPNAME_TEMPL_DETAIL");
        listTable.add("M_OUTLET");
        listTable.add("M_PAYMENT_METHOD");
        listTable.add("M_PAYMENT_METHOD_LIMIT");
        listTable.add("M_OUTLET_PRICE");
        listTable.add("M_PRICE");
        listTable.add("M_RECIPE_HEADER");
        listTable.add("M_RECIPE_DETAIL");
        listTable.add("M_RECIPE_PRODUCT");
        listTable.add("M_SALES_RECIPE");
        listTable.add("M_UOM_CONV");
        listTable.add("M_ITEM_SUPPLIER");
//        listTable.add("M_DELETED");
//        listTable.add("M_NPWP");
//        listTable.add("M_MENUGRP");
//        listTable.add("M_MENUDTL");

        String dateCopy = (String) param.get("date");
        if (dateCopy == null || "".equals(dateCopy)) {
            dateCopy = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        }
        System.out.println("Copy All Table Start At " + dateCopy);
        List<String> listError = new ArrayList<>();
        try {
            for (String table : listTable) {
                if (processServices.insertDataLocal(table, dateCopy) == false) {
                    Date dateError = new Date();
                    listError.add(table);
                    System.out.println("Error Insert Table " + table + " At " + dateError);
                }
            }

            if (listError.isEmpty()) {
                rm.setSuccess(true);
                rm.setMessage("Copy " + listTable.size() + " Table for " + dateCopy + " Successfuly");
            } else {
                rm.setSuccess(false);
                rm.setMessage("Some Table Failed");
                rm.setItem(listError);
            }
            System.out.println("Copy All Table End");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed: " + e.getMessage());

        }
        return rm;
    }

    @RequestMapping(value = "/copy-selected", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage copySelected(@RequestBody Map<String, Object> param)
            throws IOException, Exception {
        ResponseMessage rm = new ResponseMessage();
        List<String> nameTable = (List<String>) param.get("listTable");
        String dateCopy = (String) param.get("date");
        if (dateCopy == null || "".equals(dateCopy)) {
            dateCopy = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        }

        System.out.println("Copy Selected Table Start at " + dateCopy);
        List<String> listError = new ArrayList<>();
        try {
            if (nameTable.isEmpty()) {
                rm.setSuccess(false);
                rm.setMessage("Please Choose Table First");
            } else {
                for (String table : nameTable) {
                    if (processServices.insertDataLocal(table, dateCopy) == false) {
                        Date dateError = new Date();
                        listError.add(table);
                        System.out.println("Error Insert Table " + table + " At " + dateError);
                    } else {
                        System.out.println("Done Insert Table " + table);
                    }
                }

                if (listError.isEmpty()) {
                    rm.setSuccess(true);
                    rm.setMessage("Copy " + nameTable.size() + " Table for " + dateCopy + " Successfuly");
                } else {
                    rm.setSuccess(false);
                    rm.setMessage("Some Table Failed");
                    rm.setItem(listError);
                }
            }
            System.out.println("Copy Selected Table End");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed: " + e.getMessage());
        }
        return rm;
    }

    @RequestMapping(value = "/copy-single", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage copyPaste(@RequestBody Map<String, Object> param) throws IOException, Exception {
        String tableName = (String) param.get("tableName");
        String dateCopy = (String) param.get("date");
        if (dateCopy == null || "".equals(dateCopy)) {
            dateCopy = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        }

        System.out.println("Copy Table " + tableName + " Start at " + dateCopy);
        ResponseMessage rm = new ResponseMessage();
        try {
            if (processServices.insertDataLocal(tableName, dateCopy)) {
                rm.setSuccess(true);
                rm.setMessage("Insert " + tableName + " for " + dateCopy + " Successfuly");
            } else {
                rm.setSuccess(false);
                rm.setMessage("Insert Failed. Please Check Log Insert");
            }
            System.out.println("Copy Table " + tableName + " End");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed: " + e.getMessage());
        }
        return rm;
    }
    
    // ========= METHOD TRANSFER DATA TO MASTER =========
    @RequestMapping(value = "/transfer-data-all", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage transferDataAll(@RequestBody Map<String, Object> param) throws IOException, Exception {
        ResponseMessage rm = new ResponseMessage();
        List<String> listTable = new ArrayList<>();
        listTable.add("T_ABSENSI");
        listTable.add("T_AGENT_LOG");
        listTable.add("T_COPNAME_DETAIL");
        listTable.add("T_COPNAME_HEADER");
        listTable.add("T_COST_SCHEDULE");
        listTable.add("T_DEV_DETAIL");
        listTable.add("T_DEV_HEADER");
        listTable.add("T_EOD_HIST");
        listTable.add("T_EOD_HIST_DTL");
        listTable.add("T_HIST_DEL_PRD");
        listTable.add("T_ITEM_PRICE_SCH");
        listTable.add("T_KDS_HEADER");
        listTable.add("T_KDS_ITEM");
        listTable.add("T_KDS_ITEM_DETAIL");
        listTable.add("T_KDS_KRUSHER");
        listTable.add("T_KDS_NAME");
        listTable.add("T_LOC_DETAIL");
        listTable.add("T_LOC_HEADER");
        listTable.add("T_MPCS_DETAIL");
        listTable.add("T_MPCS_HIST");
        listTable.add("T_MPCS_LOG");
        listTable.add("T_OPNAME_DETAIL");
        listTable.add("T_OPNAME_HEADER");
        listTable.add("T_ORDER_DETAIL");
        listTable.add("T_ORDER_HEADER");
        listTable.add("T_PC_BALANCE");
        listTable.add("T_PC_CLAIM_DTL");
        listTable.add("T_PC_CLAIM_HDR");
        listTable.add("T_PC_DTL");
        listTable.add("T_PC_HDR");
        listTable.add("T_POS_BILL");
        listTable.add("T_POS_BILL_DONATE");
        listTable.add("T_POS_BILL_ITEM");
        listTable.add("T_POS_BILL_ITEM_DETAIL");
        listTable.add("T_POS_BILL_PAYMENT");
        listTable.add("T_POS_BILL_PAYMENT_DETAIL");
        listTable.add("T_POS_BOOK");
        listTable.add("T_POS_BOOK_DP_DETAIL");
        listTable.add("T_POS_BOOK_ITEM");
        listTable.add("T_POS_BOOK_ITEM_DETAIL");
        listTable.add("T_POS_BOOK_PAYMENT");
        listTable.add("T_POS_BOOK_PAYMENT_DETAIL");
        listTable.add("T_POS_CAT");
        listTable.add("T_POS_CATERING");
        listTable.add("T_POS_CAT_ITEM");
        listTable.add("T_POS_CAT_ITEM_DETAIL");
        listTable.add("T_POS_CC");
        listTable.add("T_POS_CC_ITEM");
        listTable.add("T_POS_CC_ITEM_DETAIL");
        listTable.add("T_POS_DAY");
        listTable.add("T_POS_DAY_LOG");
        listTable.add("T_POS_DAY_TRANS");
        listTable.add("T_POS_FORM");
        listTable.add("T_POS_FORM_ITEM");
        listTable.add("T_POS_FORM_ITEM_DETAIL");
        listTable.add("T_POS_ITEM_VOID");
        listTable.add("T_PROJECTION_HEADER");
        listTable.add("T_PROJECTION_TARGET");
        listTable.add("T_RECIPE_HIST");
        listTable.add("T_RECV_DETAIL");
        listTable.add("T_RECV_HEADER");
        listTable.add("T_RETURN_DETAIL");
        listTable.add("T_RETURN_HEADER");
        listTable.add("T_SCHEDULE_DETAIL");
        listTable.add("T_SCHEDULE_HEADER");
        listTable.add("T_SCHEDULE_SUBHEADER");
        listTable.add("T_SEND_RECV_D");
        listTable.add("T_SEND_RECV_H");
        listTable.add("T_STOCK_CARD");
        listTable.add("T_STOCK_CARD_DETAIL");
        listTable.add("T_STOCK_CARD_HIST");
        listTable.add("T_STOCK_CARD_HIST_TRIGGER");
        listTable.add("T_STOCK_CARD_RECAP");
        listTable.add("T_SUMM_MPCS");
        listTable.add("T_SUM_ABSENSI");
        listTable.add("T_WASTAGE_DETAIL");
        listTable.add("T_WASTAGE_HEADER");
        
        String outletId = param.get("outletId") != null ? (String) param.get("outletId") : null;
        String dateCopy = (String) param.get("date");
        if (dateCopy == null || "".equals(dateCopy)) {
            dateCopy = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        }
        System.out.println("Copy All Table Start At " + dateCopy);
        List<String> listError = new ArrayList<>();
        try {
            for (String table : listTable) {
                if (processServices.sendDataLocal(table, dateCopy, outletId) == false) {
                    Date dateError = new Date();
                    listError.add(table);
                    System.out.println("Error Insert Table " + table + " At " + dateError);
                }
            }

            if (listError.isEmpty()) {
                rm.setSuccess(true);
                rm.setMessage("Copy " + listTable.size() + " Table for " + dateCopy + " Successfuly");
            } else {
                rm.setSuccess(false);
                rm.setMessage("Some Table Failed");
                rm.setItem(listError);
            }
            System.out.println("Copy All Table End");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed: " + e.getMessage());

        }
        return rm;
    }
    
    @RequestMapping(value = "/transfer-data-selected", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage transferDataSelected(@RequestBody Map<String, Object> param) throws IOException, Exception {
        ResponseMessage rm = new ResponseMessage();
        List<String> nameTable = (List<String>) param.get("listTable");
        String outletId = param.get("outletId") != null ? (String) param.get("outletId") : null;
        String dateCopy = (String) param.get("date");
        if (dateCopy == null || "".equals(dateCopy)) {
            dateCopy = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        }

        System.out.println("Transfer Data Selected Table Start at " + dateCopy);
        List<String> listError = new ArrayList<>();
        try {
            if (nameTable.isEmpty()) {
                rm.setSuccess(false);
                rm.setMessage("Please Choose Table First");
            } else {
                for (String table : nameTable) {
                    if (processServices.sendDataLocal(table, dateCopy, outletId) == false) {
                        Date dateError = new Date();
                        listError.add(table);
                        System.out.println("Error Transfer Table " + table + " At " + dateError);
                    } else {
                        System.out.println("Done Transfer Table " + table);
                    }
                }

                if (listError.isEmpty()) {
                    rm.setSuccess(true);
                    rm.setMessage("Copy " + nameTable.size() + " Table for " + dateCopy + " Successfuly");
                } else {
                    rm.setSuccess(false);
                    rm.setMessage("Some Table Failed");
                    rm.setItem(listError);
                }
            }
            System.out.println("Transfer Selected Table End");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Transfer Failed: " + e.getMessage());
        }
        return rm;
    }
    // =============== End Method From Lukas 17-10-2023 ===============
    

    // =============== New Method From M Joko - 1 Feb 2024 ===============
    @RequestMapping(value = "/list-log", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage listLogger(@RequestBody Map<String, Object> param) throws IOException, Exception {
        String moduleName = (String) param.get("moduleName");
        ResponseMessage rm = new ResponseMessage();
        try {
            List<String> listLogs = viewServices.listLogger(param);
            rm.setSuccess(true);
            rm.setMessage("Successfuly get list " + moduleName);
            rm.setItem(listLogs);
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage(moduleName + " Failed: " + e.getMessage());
        }
        return rm;
    }
    @RequestMapping(value = "/mpcs-production-list-fryer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Fryer pada menu MPCS production tambah pemasakan by Fathur", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response mpcsProductionListFryer(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        res.setData(viewServices.mpcsProductionListFryer(balance));
        return res;
    }
}
