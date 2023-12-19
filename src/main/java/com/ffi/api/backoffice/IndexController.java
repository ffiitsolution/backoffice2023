/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice;

import com.ffi.api.backoffice.model.DetailOpname;
import com.ffi.api.backoffice.model.HeaderOpname;
import com.ffi.api.backoffice.model.ParameterLogin;
import com.ffi.api.backoffice.services.ProcessServices;
import com.ffi.api.backoffice.services.ViewServices;
import com.ffi.api.backoffice.services.ReportServices;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
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
    @Autowired
    ProcessServices processServices;
    @Autowired
    ReportServices reportServices;

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
        String json = "";
        ParameterLogin balance = gsn.fromJson(param, new TypeToken<ParameterLogin>() {
        }.getType());
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        Response res = new Response();
        ResponseMessage rm = new ResponseMessage();

        list = viewServices.loginJson(balance);
        //list = prosesServices.loginOutletJson(balance);

        try {
            if (list.size() > 0) {
                rm.setSuccess(true);
                rm.setMessage("Login Success");
                rm.setItem(list);
//                rm.setItem(flag);
            } else {
                rm.setSuccess(false);
                rm.setMessage("Login Failed");
                rm.setItem(null);
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
    //INSERT SUPPLIER================================================================================================

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
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
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

        Response res = new Response();
        res.setData(viewServices.listItem(logan));
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
            rm.setMessage("Insert Failed : " + e.getMessage());
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
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();

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
        JsonObject result = gsn.fromJson(param, JsonObject.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.insertReturnOrderHeaderDetail(result);
            rm.setSuccess(true);
            rm.setMessage("Insert Return Order Successfuly");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Return Order Failed: " + e.getMessage());
            System.err.println(e);
        }
        rm.setItem(list);
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
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateOpnameStatus(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");

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

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

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

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

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

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

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
        Gson gsn = new Gson();
        Response res = new Response();
        List errors = new ArrayList();
        List posOpen = new ArrayList();
        List<Map<String, Object>> prevStockCards = new ArrayList();
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
                    processServices.insertEodPosN(newParams);
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

        prevStockCards = viewServices.listPreviousTStockCard(balance);
        if (!prevStockCards.isEmpty()) {
            for (int i = 0; i < prevStockCards.size(); i++) {
                var pStockCard = prevStockCards.get(i);
                double operation = Double.parseDouble(pStockCard.getOrDefault("qtyBeginning", "0").toString()) + Double.parseDouble(pStockCard.getOrDefault("qtyIn", "0").toString()) - Double.parseDouble(pStockCard.getOrDefault("qtyOut", "0").toString());
                NumberFormat format = new DecimalFormat("#.####");
                Map<String, String> scardParams = new HashMap();
                scardParams.put("outletCode", pStockCard.get("outletCode").toString());
                scardParams.put("itemCode", pStockCard.get("itemCode").toString());
                scardParams.put("itemCost", pStockCard.get("itemCost").toString());
                scardParams.put("qtyBeginning", format.format(operation));
                scardParams.put("qtyIn", "0");
                scardParams.put("qtyOut", "0");
                scardParams.put("remark", pStockCard.getOrDefault("remark", " ").toString());
                scardParams.put("userUpd", balance.getOrDefault("userUpd", "SYSTEM"));
                processServices.insertTStockCard(scardParams);
            }
            processServices.insertTEodHist(balance);
            processServices.insertTSummMpcs(balance);
            processServices.increaseTransDateMOutlet(balance);
        } else {
            errors.add("! prevStockCards size isEmpty");
            d.put("errors", errors);
            d.put("message", "Process End of Day Failed");
        }

        data.add(d);
        res.setData(data);
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
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
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
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
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
}
