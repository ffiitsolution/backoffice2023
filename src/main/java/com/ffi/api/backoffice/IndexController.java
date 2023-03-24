/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice;

import com.ffi.api.backoffice.model.ParameterLogin;
import com.ffi.api.backoffice.services.ProcessServices;
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

//    @Autowired
//    TransServices transServices;
    @RequestMapping(value = "/halo")
    public @ResponseBody
    Map<String, Object> tes() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("output", "welcome");
        return map;
    }

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
    @ApiOperation(value = "Digunakan untuk update mpcs", response = Object.class)
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

    @RequestMapping(value = "/update-mpcs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk update mpcs", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateMpcs(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateMpcs(balance);
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

    Response listItemCost(@RequestBody String param)throws IOException, Exception {
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
    Response listMasterCity(@RequestBody String param)throws IOException, Exception {
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
    Response listPosition(@RequestBody String param)throws IOException, Exception {
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
    Response listMpcsHeader(@RequestBody String param)  throws IOException, Exception {
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
    Response listMasterItemSupplier(@RequestBody String param) throws  IOException, Exception {
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
        @ApiResponse(code = 200, message = "OK")
        ,
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
        @ApiResponse(code = 200, message = "OK")
        ,
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
        @ApiResponse(code = 200, message = "OK")
        ,
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
        @ApiResponse(code = 200, message = "OK")
        ,
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
        @ApiResponse(code = 200, message = "OK")
        ,
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
}
