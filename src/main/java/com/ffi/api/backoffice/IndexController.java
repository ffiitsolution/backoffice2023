package com.ffi.api.backoffice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffi.api.backoffice.model.DetailOpname;
import com.ffi.api.backoffice.model.HeaderOpname;
import com.ffi.api.backoffice.model.ParameterLogin;
import com.ffi.api.backoffice.model.TableAlias;
import com.ffi.api.backoffice.services.ProcessServices;
import com.ffi.api.backoffice.services.ViewServices;
import com.ffi.api.backoffice.services.ReportServices;
import com.ffi.api.backoffice.utils.AppUtil;
import com.ffi.api.backoffice.utils.FileLoggerUtil;
import com.ffi.api.backoffice.utils.TableAliasUtil;
import com.ffi.paging.Response;
import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    TableAliasUtil tableAliasUtil;
    
    @Autowired
    AppUtil appUtil;
    
    @Autowired
    private FileLoggerUtil fileLoggerUtil;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Value("${app.outletCode}")
    private String _OutletCode;
    
    @Value("${endpoint.master}")
    private String _UrlMaster;

//    @Autowired
//    TransServices transServices;
    @RequestMapping(value = "/halo")
    public @ResponseBody
    Map<String, Object> tes() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("output", "welcome");
        return map;
    }
    
    @RequestMapping(value = "/ping")
    public @ResponseBody
    Map<String, Object> ping() {
        Map<String, Object> map = new HashMap<>();
        map.put("output", "Connection is Successfully");
        return map;
    }
    
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public ResponseMessage greeting(String message) throws Exception {
        Thread.sleep(1000); // simulated delay
        ResponseMessage rm = new ResponseMessage();
        rm.setMessage("Test WS");
        rm.setSuccess(true);
        return rm;
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
        Map<String, Object> balanced = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
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
                if (user.getOrDefault("status", "I").equals("A")) {
                    rm.setSuccess(true);
                    rm.setMessage("Login Success");
                    rm.setItem(list);
                    fileLoggerUtil.logActivity("/login", "Login", "Login", balance.getUserName(), balance.getUserName(), "", Boolean.TRUE, "", balanced);
                } else {
                    rm.setSuccess(false);
                    rm.setMessage("Login failed, User is INACTIVE");
                }
            } else {
                rm.setSuccess(false);
                rm.setMessage("User and Password not match.");
                fileLoggerUtil.logActivity("/login", "Login", "Login", balance.getUserName(), balance.getUserName(), "", Boolean.FALSE, "", balanced);
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

    /////////// new Method by Dani 15-Feb-2024
    @RequestMapping(value = "/list-menu-group-tipe-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan List Group Menu Tipe Order", response = Object.class)
    public @ResponseBody
    Response listMenuGroupTipeOrder(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        Response response = new Response();
        response.setData(viewServices.listMenuGroupTipeOrder(balance));
        return response;
    }

    /////////// new Method by Dani 15-Feb-2024
    @RequestMapping(value = "/list-menu-group-outlet-limit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Menampilkan List Group Menu Outlet Limit", response = Object.class)
    public @ResponseBody
    Response listMenuGroupOutletLimit(@RequestBody String param) {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        Response response = new Response();
        response.setData(viewServices.listMenuGroupOutletLimit(balance));
        return response;
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
        if (logan.get("paket").equalsIgnoreCase("E")) {
            Map<String, Object> sddParam = new HashMap<>();
            sddParam.put("cdWarehouse", "00010");
            sddParam.put("homePage", "SDD");
            processServices.updateCdWarehouseItem(sddParam);
        }
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

    ////// new method by Dani 15-Feb-2024
    @RequestMapping(value = "/item-menus-tipe-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat menu tipe order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response listItemMenusTipeOrder(@RequestBody String param) {
        Gson gsn = new Gson();
        Response res = new Response();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        res.setData(viewServices.listItemMenusTipeOrder(balance));
        return res;
    }

    ////// new method by Dani 15-Feb-2024
    @RequestMapping(value = "/item-menus-limit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat menu limit", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response listItemMenusLimit(@RequestBody String param) {
        Gson gsn = new Gson();
        Response res = new Response();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        res.setData(viewServices.listItemMenusLimit(balance));
        return res;
    }

    ////// new method by Dani 15-Feb-2024
    @RequestMapping(value = "/item-menus-set", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk melihat menu set", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public @ResponseBody
    Response listItemMenusSet(@RequestBody String param) {
        Gson gsn = new Gson();
        Response res = new Response();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        res.setData(viewServices.listItemMenusSet(balance));
        return res;
    }

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
    Response listGlobal(@RequestBody Map<String, Object> param) throws IOException, Exception {
        Response res = new Response();
        res.setData(viewServices.listGlobal(param));
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
        List<Map<String, Object>> list = new ArrayList<>();
        ResponseMessage rm = new ResponseMessage();
        try {
            list.add(processServices.insertOrderHeader(balance));
            processServices.updateMCounter(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");
            
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed: " + e.getMessage());
            e.printStackTrace();
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
    
    @Transactional
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
        List<Map<String, Object>> list = new ArrayList<>();
        String status = "";
        ResponseMessage rm = new ResponseMessage();
        
        try {
            processServices.checkInventoryAvailability();
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
                rm.setMessage(list.size() + " item berikut terdeteksi memiliki nilai awal namun di input 0");
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
    
    @Transactional
    public @ResponseBody
    ResponseMessage sendDataOutletToWarehouse(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<>();
        ResponseMessage rm = new ResponseMessage();
        
        try {
            processServices.checkInventoryAvailability();
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
    @Transactional
    @RequestMapping(value = "/process-eod", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk process data EOD sesuai trans_date Outlet dan POS yg masih open", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Failed"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response processEod(@RequestBody String param) throws IOException, Exception {
        messagingTemplate.convertAndSend("/topic", "Mulai End Of Day");
        long startTime = System.currentTimeMillis();
        System.err.println("Start End of Day Process");
        Gson gsn = new Gson();
        Response res = new Response();
        List errors = new ArrayList();
        List posOpen = new ArrayList();
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> d = new HashMap();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> prms = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        fileLoggerUtil.logActivity("/process-eod", "End Of Day", "PROCESS", balance.getOrDefault("actUser", "SYSTEM"), balance.getOrDefault("actName", "SYSTEM"), "Start", Boolean.TRUE, "", prms);
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
            messagingTemplate.convertAndSend("/topic", "End Of Day - Cek POS: " + posCode);
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
                    errors.add("listPosOpen size: " + listPosOpen.size());
                }
            }
        }
        
        d.put("success", false);
        d.put("errors", errors);
        d.put("prevEod", prevEod);
        d.put("listMPosActive", listMPosActive);
        d.put("listPosOpen", posOpen);
        d.put("message", "Process End of Day Success");
        
        if (!errors.isEmpty()) {
            d.put("message", "Process End of Day Failed");
            data.add(d);
            res.setData(data);
            fileLoggerUtil.logActivity("/process-eod", "End Of Day", "PROCESS", balance.getOrDefault("actUser", "SYSTEM"), balance.getOrDefault("actName", "SYSTEM"), "", Boolean.FALSE, "", prms);
            return res;
        }
        
        try {
            processServices.insertTStockCard(balance);
            processServices.insertTEodHist(balance);
            processServices.insertTSummMpcs(balance);
            processServices.updateOrderEntryExpired(balance);
            processServices.checkMCounterNextMonth(balance);
            processServices.increaseTransDateMOutlet(balance);
            d.put("success", true);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.err.println("Error End of Day: " + e.getMessage());
            errors.add("Error End of Day: " + e.getMessage());
            d.put("errors", errors);
            d.put("message", "Process End of Day Failed: " + e.getMessage());
            data.add(d);
            res.setData(data);
            fileLoggerUtil.logActivity("/process-eod", "End Of Day", "PROCESS", balance.getOrDefault("actUser", "SYSTEM"), balance.getOrDefault("actName", "SYSTEM"), "", Boolean.FALSE, "", prms);
            return res;
        }
        data.add(d);
        res.setData(data);
        double elapsedTimeSeconds = (double) (System.currentTimeMillis() - startTime) / 1000.0;
        messagingTemplate.convertAndSend("/topic", "Selesai End Of Day: " + elapsedTimeSeconds + " detik");
        System.err.println("Finished End of Day Process after total: " + elapsedTimeSeconds + " seconds");
        fileLoggerUtil.logActivity("/process-eod", "End Of Day", "PROCESS", balance.getOrDefault("actUser", "SYSTEM"), balance.getOrDefault("actName", "SYSTEM"), elapsedTimeSeconds + " detik", Boolean.TRUE, "", prms);
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
    Map<String, Object> listMpcsProduction(@RequestBody String param) throws Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            List list = viewServices.listMpcsProduction(balance);
            map.put("data", list);
            map.put("success", true);
        } catch (Exception e) {
            map.put("message", e.getMessage());
            map.put("success", false);
        }
        return map;
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
    
    @RequestMapping(value = "/generate-delivery-order-freemeal", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "query list delivery order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Map<String, String> generateDeliveryOrderFreemeal(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        return this.viewServices.generateDeliveryOrderFreemeal(balance);
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
    
    @RequestMapping(value = "/delivery-order-check-exist-no-request", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert delivery order", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage deliveryOrderCheckExistNoRequest(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> data = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());
        
        var rm = new ResponseMessage();
        try {
            List<Boolean> list = new ArrayList<>();
            list.add(viewServices.deliveryOrderCheckExistNoRequest(data));
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

    // query to get Delivery order By Dani 27 Des 2023
    @RequestMapping(value = "/get-delivery-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk get delivery order", response = Object.class)
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
            processServices.checkInventoryAvailability();
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

    // Get List Daftar Menu Report  By Rafi 9 Jan 2024
    @RequestMapping(value = "/get-list-item-detail-report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan get list report selected by item detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage getListItemDetailReport(@RequestBody String params) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(params, new TypeToken<Map<String, String>>() {
        }.getType());
        var rm = new ResponseMessage();
        try {
            rm.setSuccess(true);
            rm.setMessage("Success Successfuly");
            rm.setItem(viewServices.getListItemDetailReport(balance));
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
        return processServices.deleteMpcsProduction(data);
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
        if (_UrlMaster.isBlank()) {
            System.out.println("scheduledKirimTerimaData: URL MasterHQ belum diatur di properties");
            rm.setSuccess(false);
            rm.setMessage("URL MasterHQ belum diatur di properties");
            messagingTemplate.convertAndSend("/topic", "Error: URL MasterHQ belum diatur di properties");
        } else {
            List<TableAlias> allActiveTable = tableAliasUtil.searchByColumn(TableAliasUtil.TABLE_ALIAS_M, "process", true);
            List<String> listTable = allActiveTable.stream().map(TableAlias::getTable).toList();
            String dateUpd = (String) param.get("dateUpd");
            String timeUpd = (String) param.get("timeUpd");
            System.out.println("Copy All Table Start At " + dateUpd);
            List<String> listError = new ArrayList<>();
            try {
                for (String table : listTable) {
                    Map prm = new HashMap();
                    prm.put("tableName", table);
                    prm.put("dateUpd", dateUpd);
                    prm.put("timeUpd", timeUpd);
                    if (processServices.insertDataLocal(prm) == false) {
                        Date dateError = new Date();
                        listError.add(table);
                        System.out.println("Error Insert Table " + table + " At " + dateError);
                    }
                }
                
                if (listError.isEmpty()) {
                    rm.setSuccess(true);
                    rm.setMessage("Copy " + listTable.size() + " Table for " + dateUpd + " Successfuly");
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
        }
        return rm;
    }
    
    @RequestMapping(value = "/copy-selected", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage copySelected(@RequestBody Map<String, Object> param)
            throws IOException, Exception {
        ResponseMessage rm = new ResponseMessage();
        List<String> nameTable = (List<String>) param.get("listTable");
        String dateUpd = (String) param.get("dateUpd");
        String timeUpd = (String) param.get("timeUpd");
        
        System.out.println("Copy Selected Table Start at " + dateUpd);
        List<String> listError = new ArrayList<>();
        try {
            if (nameTable.isEmpty()) {
                rm.setSuccess(false);
                rm.setMessage("Please Choose Table First");
            } else {
                for (String table : nameTable) {
                    Map prm = new HashMap();
                    TableAlias ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_M, "alias", table).get();
                    String tableName = ta.getTable();
                    prm.put("tableName", tableName);
                    prm.put("dateUpd", dateUpd);
                    prm.put("timeUpd", timeUpd);
                    if (processServices.insertDataLocal(prm) == false) {
                        Date dateError = new Date();
                        listError.add(table);
                        System.out.println("Error Insert Table " + table + " At " + dateError);
                    } else {
                        System.out.println("Done Insert Table " + table);
                    }
                }
                
                if (listError.isEmpty()) {
                    rm.setSuccess(true);
                    rm.setMessage("Copy " + nameTable.size() + " Table for " + dateUpd + " Successfuly");
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
        ResponseMessage rm = new ResponseMessage();
        if (_UrlMaster.isBlank()) {
            System.out.println("scheduledKirimTerimaData: URL MasterHQ belum diatur di properties");
            rm.setSuccess(false);
            rm.setMessage("URL MasterHQ belum diatur di properties");
            messagingTemplate.convertAndSend("/topic", "Error: URL MasterHQ belum diatur di properties");
        } else {
            String alias = (String) param.get("tableName");
            messagingTemplate.convertAndSend("/topic/kirim-terima-data", "Proses di " + alias);
            TableAlias ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_M, "alias", alias).get();
            String tableName = ta.getTable();
            String dateUpd = (String) param.get("dateUpd");
            param.put("trx", 0);
            param.put("tableName", tableName);
            param.put("aliasName", ta.getAlias());
            if (!param.containsKey("remark") || param.get("remark").toString().isBlank()) {
                param.put("remark", "SINGLE");
            }
            try {
                if (processServices.insertDataLocal(param)) {
                    rm.setSuccess(true);
                    rm.setMessage("Insert " + tableName + " for " + dateUpd + " Successfuly");
                } else {
                    rm.setSuccess(false);
                    rm.setMessage("Insert Failed. Please Check Log Insert");
                }
                System.out.println("Copy Table " + tableName + " End");
            } catch (Exception e) {
                rm.setSuccess(false);
                rm.setMessage("Insert Failed: " + e.getMessage());
                System.err.println("Copy Table " + tableName + " Failed: " + e.getMessage());
            }
        }
        return rm;
    }

    // ========= METHOD TRANSFER DATA TO MASTER =========
    @RequestMapping(value = "/transfer-data-all", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage transferDataAll(@RequestBody Map<String, Object> param) throws IOException, Exception {
        ResponseMessage rm = new ResponseMessage();
        if (_UrlMaster.isBlank()) {
            System.out.println("scheduledKirimTerimaData: URL MasterHQ belum diatur di properties");
            rm.setSuccess(false);
            rm.setMessage("URL MasterHQ belum diatur di properties");
            messagingTemplate.convertAndSend("/topic", "Error: URL MasterHQ belum diatur di properties");
        } else {
            long startTime = System.currentTimeMillis();
            List<TableAlias> allActiveTable = tableAliasUtil.searchByColumn(TableAliasUtil.TABLE_ALIAS_T, "process", true);
            String outletId = param.get("outletId") != null ? (String) param.get("outletId") : null;
            String dateUpd = (String) param.get("dateUpd");
            String timeUpd = (String) param.get("timeUpd");
            if (!param.containsKey("remark") || param.get("remark").toString().isBlank()) {
                param.put("remark", "MANUAL");
            }
            System.out.println("Send All " + allActiveTable.size() + " Table Start At " + dateUpd);
            List<String> listError = new ArrayList<>();
            try {
                for (TableAlias table : allActiveTable) {
                    String tableName = table.getTable();
                    String aliasName = table.getAlias();
                    param.put("trx", 1);
                    param.put("outletId", outletId);
                    param.put("outletCode", outletId);
                    param.put("tableName", tableName);
                    param.put("aliasName", aliasName);
                    Map map1 = processServices.sendDataLocal(param);
                    if (map1.containsKey("success")) {
                        boolean status = (boolean) map1.get("success");
                        if (!status) {
                            listError.add(aliasName + ": " + map1.get("message").toString());
                        }
                    }
                    if (map1.containsKey("error")) {
                        String error = map1.get("error").toString();
                        if (error.contains("Connection refused")) {
                            listError.add(aliasName + ": No connection to HQ.");
                            break;
                        } else {
                            listError.add(aliasName + ": " + error);
                        }
                    }
                }
                if (listError.isEmpty()) {
                    rm.setSuccess(true);
                    rm.setMessage("Send " + allActiveTable.size() + " Table for " + dateUpd + " Successfuly");
                } else {
                    rm.setSuccess(false);
                    rm.setMessage("Some Table Failed");
                    rm.setItem(listError);
                }
            } catch (MessagingException e) {
                rm.setSuccess(false);
                rm.setMessage("Insert Failed: " + e.getMessage());
            }
            double elapsedTimeSeconds = (double) (System.currentTimeMillis() - startTime) / 1000.0;
            System.err.println("transferDataAll process in: " + elapsedTimeSeconds + " seconds");
            messagingTemplate.convertAndSend("/topic", "Kirim Data Transaksi: " + elapsedTimeSeconds + " detik");
        }
        return rm;
    }
    
    @RequestMapping(value = "/transfer-data-selected", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage transferDataSelected(@RequestBody Map<String, Object> param) throws IOException, Exception {
        ResponseMessage rm = new ResponseMessage();
        if (_UrlMaster.isBlank()) {
            System.out.println("scheduledKirimTerimaData: URL MasterHQ belum diatur di properties");
            rm.setSuccess(false);
            rm.setMessage("URL MasterHQ belum diatur di properties");
            messagingTemplate.convertAndSend("/topic", "Error: URL MasterHQ belum diatur di properties");
        } else {
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
//                    if (processServices.sendDataLocal(param) == false) {
//                        Date dateError = new Date();
//                        listError.add(table);
//                        System.out.println("Error Transfer Table " + table + " At " + dateError);
//                    } else {
//                        System.out.println("Done Transfer Table " + table);
//                    }
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
        }
        return rm;
    }
    
    @RequestMapping(value = "/transfer-data-single", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage transferDataSingle(@RequestBody Map<String, Object> param) throws IOException, Exception {
        ResponseMessage rm = new ResponseMessage();
        if (_UrlMaster.isBlank()) {
            System.out.println("scheduledKirimTerimaData: URL MasterHQ belum diatur di properties");
            rm.setSuccess(false);
            rm.setMessage("URL MasterHQ belum diatur di properties");
            messagingTemplate.convertAndSend("/topic", "Error: URL MasterHQ belum diatur di properties");
        } else {
            String aliasName = (String) param.get("tableName");
            TableAlias ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_T, "alias", aliasName).get();
            String tableName = ta.getTable();
            String dateUpd = (String) param.get("dateUpd");
            param.put("trx", 1);
            param.put("outletId", param.get("outletCode").toString());
            param.put("tableName", tableName);
            param.put("aliasName", ta.getAlias());
            if (!param.containsKey("remark") || param.get("remark").toString().isBlank()) {
                param.put("remark", "SINGLE");
            }
            rm.setSuccess(false);
            try {
                Map map1 = processServices.sendDataLocal(param);
                boolean status = (boolean) map1.get("success");
                List list = (List) map1.get("item");
                if (status) {
                    messagingTemplate.convertAndSend("/topic/kirim-terima-data", "Success Kirim Data: " + aliasName);
                    rm.setSuccess(true);
                    rm.setItem(list);
                    rm.setMessage("Success Kirim Data: " + aliasName);
                } else {
                    rm.setMessage("Failed Kirim Data: " + aliasName);
                    messagingTemplate.convertAndSend("/topic/kirim-terima-data", "Failed Kirim Data: " + aliasName);
                }
            } catch (MessagingException e) {
                rm.setMessage("Transfer Failed: " + e.getMessage());
            }
        }
        return rm;
    }
    // =============== End Method From Lukas 17-10-2023 ===============

    // =============== New Method From M Joko - 1 Feb 2024 ===============
    @RequestMapping(value = "/list-log", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage listLogger(@RequestBody Map<String, Object> param) throws IOException, Exception {
        String module = (String) param.get("module");
        ResponseMessage rm = new ResponseMessage();
        try {
            List<String> listLogs = viewServices.listLogger(param);
            rm.setSuccess(true);
            rm.setMessage("Successfuly get list " + module);
            if (module.equalsIgnoreCase("ActivityLog") && param.containsKey("log")) {
                List<Map<String, Object>> listMap = new ArrayList();
                ObjectMapper objectMapper = new ObjectMapper();
                for (String json : listLogs) {
                    try {
                        Map<String, Object> map = objectMapper.readValue(json, Map.class);
                        listMap.add(map);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }
                rm.setItem(listMap);
                rm.setMessage("Successfuly get list " + module + ": " + (String) param.get("log"));
            } else {
                rm.setItem(listLogs);
            }
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage(module + " Failed: " + e.getMessage());
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

    // Delete order entry Detail by Dani 2 Feb 2024
    @RequestMapping(value = "/delete-order-entry-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete Order Entry Detail by Dani", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage deleteOrderEntryDetail(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        ResponseMessage res = new ResponseMessage();
        try {
            processServices.deleteOrderEntryDetail(balance);
            res.setMessage("Success!!");
            res.setSuccess(true);
        } catch (Exception e) {
            res.setMessage(e.getMessage());
            res.setSuccess(false);
        }
        return res;
    }

    //============== New Method From M Joko 1-2-2024 ================
    @ApiOperation(value = "List Transfer Data by M Joko", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    @RequestMapping(value = "/list-transfer-data", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseMessage listTransferData(@RequestBody Map<String, Object> param) throws IOException, Exception {
        String typeTable = param.getOrDefault("type", "ALL").toString();
        Boolean isTerimaMaster = "TERIMA DATA MASTER".equals(param.get("type"));
        List<String> listTable = new ArrayList();
        if (isTerimaMaster) {
            List<TableAlias> allActiveTable = tableAliasUtil.searchByColumn(TableAliasUtil.TABLE_ALIAS_M, "process", true);
            listTable = allActiveTable.stream().map(TableAlias::getTable).toList();
            System.err.println("testTableM :" + listTable.size());
        } else {
            List<TableAlias> allActiveTable = tableAliasUtil.searchByColumn(TableAliasUtil.TABLE_ALIAS_T, "process", true);
            listTable = allActiveTable.stream().map(TableAlias::getTable).toList();
            System.err.println("testTableT :" + listTable.size());
        }
        param.put("listTable", listTable);
        return processServices.listTransferData(param);
    }

    // =============== New Method From Sifa 05-02-2024 ===============
    @RequestMapping(value = "/list-warehouse-fsd", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list gudang FSD", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listWarehouseFSD(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        Response res = new Response();
        res.setData(viewServices.listWarehouseFSD(balance));
        return res;
    }

    //============== New Method From M Joko 5-2-2024 ================
    @ApiOperation(value = "Get List And Process Backup Database by M Joko", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    @RequestMapping(value = "/backup-database", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseMessage processBackupDb(@RequestBody Map<String, Object> param) throws IOException, Exception {
        return processServices.processBackupDb(param);
    }

    //============== New Method From M Joko 13-2-2024 ================
    @ApiOperation(value = "Update Status Master Recipe by M Joko", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    @RequestMapping(value = "/recipe-status-update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseMessage updateRecipe(@RequestBody Map<String, Object> param) throws IOException, Exception {
        return processServices.updateRecipe(param);
    }
    
    @RequestMapping(value = "/remove-empty-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk menghapus order entry yang memiliki qty besar 0 dan qty kecil 0 by Fathur 15 Feb 2024", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage removeEmptyOrder(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.removeEmptyOrder(balance);
            rm.setMessage("Success");
            rm.setSuccess(true);
            return rm;
        } catch (Exception e) {
            rm.setMessage("Failed with error: " + e.getMessage());
            rm.setSuccess(false);
        }
        return rm;
    }

    // =============== New Method From M Joko 19-02-2024 ===============
    @RequestMapping(value = "/list-transfer-data-history", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list view kirim terima data", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listTransferDataHistory(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        System.err.println("listTransferDataHistory :" + balance);
        Response res = new Response();
        res.setData(viewServices.listTransferDataHistory(balance));
        return res;
    }

    // =============== New Method From Sifa 20-02-2024 ===============
    @RequestMapping(value = "/list-menu-application-access", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk list menu akses grup", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listmenuApplicationAccess(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        Response res = new Response();
        res.setData(viewServices.listmenuApplicationAccess(balance));
        return res;
    }

    // =============== New Method From Sifa 21-02-2024 ===============
    @RequestMapping(value = "/item-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view item detail", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response itemDetail(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = viewServices.itemDetail(logan);
        Response res = new Response();
        res.setData(list);
        res.setRecordsTotal(list.size());
        return res;
        
    }
    
    @RequestMapping(value = "/order-detail-temporary-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get order detail temporary list by Fathur 23 Feb 24", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response orderDetailTemporaryList(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        Response res = new Response();
        
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            list = viewServices.orderDetailTemporaryList(logan);
            res.setData(list);
            res.setRecordsTotal(list.size());
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
        return res;
        
    }

    ///// adit list outlet detail 21 Feb 2024 
    @RequestMapping(value = "/list-outlet-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view supplier", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listOutletDetail(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        
        Response res = new Response();
        res.setData(viewServices.listOutletDetail(logan));
        return res;
        
    }

    ///// adit list outlet detail group 21 Feb 2024 
    @RequestMapping(value = "/list-outlet-detail-group", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view supplier", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listOutletDetailGroup(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        
        Response res = new Response();
        res.setData(viewServices.listOutletDetailGroup(logan));
        return res;
        
    }

    ///// adit list outlet detail Type Order group 21 Feb 2024 
    @RequestMapping(value = "/list-outlet-detail-type-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view supplier", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listOutletDetailTypeOrder(@RequestBody String param) throws JRException, IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> logan = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        
        Response res = new Response();
        res.setData(viewServices.listOutletDetailTypeOrder(logan));
        return res;
        
    }

    ///// adit Update outlet detail 21 Feb 2024 
    @RequestMapping(value = "/update-outlet", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk update mpcs", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateOutlet(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new com.google.gson.reflect.TypeToken<Map<String, Object>>() {
        }.getType());
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateOutlet(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");
            
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed Successfuly: " + e.getMessage());
        }
        
        rm.setItem(list);
        
        return rm;
    }
    
    @RequestMapping(value = "/update-item", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk update item", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage updateItem(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.updateItem(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success Successfuly");
            
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed Successfuly: " + e.getMessage());
        }
        
        rm.setItem(list);
        
        return rm;
    }

    ///////// integration from pettycash to boffi aditya 08-03-2024 
    @RequestMapping(value = "/insert-pettycash-to-boffi", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk insert header dan detail data OPM ke Stock card ", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertPettyCashToBoffi(@RequestBody String param) throws IOException, Exception {
        ResponseMessage rm = new ResponseMessage();
        rm.setSuccess(false);
        rm.setItem(new ArrayList());
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        Map<String, String> headerParam = new HashMap<String, String>();
        headerParam.put("outletCode", balance.get("outletCode").toString());
        headerParam.put("userUpd", balance.get("userUpd").toString());        
        List items = (List) balance.get("items");
        List errors = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            Map<String, Object> itemx = (Map<String, Object>) items.get(i);
            headerParam.put("itemCode", itemx.get("itemCode").toString());            
            headerParam.put("totalQty", itemx.get("totalQty").toString());            
            headerParam.put("remark", itemx.get("remark").toString());            
            try {
                processServices.insertPettyCashToBoffi(headerParam);
                System.out.println("Success Insert Detail ke-" + i);
            } catch (Exception e) {
                errors.add(e.getMessage());
                System.out.println("Exception: " + e);
            }
        }
        if (errors.isEmpty()) {
            rm.setSuccess(true);
            rm.setMessage("Success");
        } else {
            rm.setItem(errors);
            rm.setMessage("Failed");
        }
        return rm;
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
    //////// done aditya 08-03-2024

    //////////// New method to Get PDF File - M Joko 14-Feb-2024 ////////////
    @GetMapping("/get-pdf-module")
    public ResponseEntity getPdf(@RequestParam(name = "fileName", defaultValue = "Modul_Back_Office_New_For_Outlet_v2") String fileName) {
        try {
            String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            String jarParent = new File(jarPath).getParent();
            String jarDir = new File(jarParent).getPath();
            String pdfFilePath = jarDir + File.separator + fileName + ".pdf";
            byte[] pdfBytes = Files.readAllBytes(Paths.get(pdfFilePath));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.unprocessableEntity()
                    .contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }

    // schedule untuk Kirim Terima Data otomatis setiap sekian menit
    // @Scheduled(cron = "0 */30 * * * *") // dijalankan setiap 30 menit
    @Scheduled(cron = "0 0/15 0-23 * * *") // dijalankan setiap 7.00, 7.15, 7.30, 7.45, 8.00, 8.15 dst
    public void scheduledKirimTerimaData() {
        if (_OutletCode.isBlank()) {
            messagingTemplate.convertAndSend("/topic", "Kode Outlet belum diatur di properties");
        } else {
            messagingTemplate.convertAndSend("/topic", "Memulai Kirim Data Transaksi Terjadwal");
            System.out.println("Executing scheduledKirimTerimaData... " + _OutletCode);
            try {
                Map<String, Object> param = new HashMap();
                LocalDateTime currentDateTime = LocalDateTime.now();
                String dateUpd = currentDateTime.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
                String timeUpd = currentDateTime.format(DateTimeFormatter.ofPattern("HHmmss"));
                param.put("dateUpd", dateUpd);
                param.put("timeUpd", timeUpd);
                param.put("userUpd", "SYSTEM");
                param.put("outletCode", _OutletCode);
                param.put("outletId", _OutletCode);
                transferDataAll(param);
            } catch (Exception ex) {
                System.err.println("Failed Executing scheduledKirimTerimaData: " + ex.getMessage());
            }
        }
    }

    ///////////////new method from aditya 19-03-2024////////////////////////////
    @RequestMapping(value = "/list-level", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view List Level", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listLevel(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        Response res = new Response();
        res.setData(viewServices.listLevel(balance));
        return res;
    }

    /////////////////// end aditya 19 Mar 2024
    
    ///////////////new method from aditya 22-03-2024////////////////////////////
    @RequestMapping(value = "/list-mpcs-monitoring", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk view List Level", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    Response listMpcsMonitoring(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        
        Response res = new Response();
        res.setData(viewServices.listMpcsMonitoring(balance));
        return res;
    }    
    /////////////////// end aditya 22 Mar 2024
}
