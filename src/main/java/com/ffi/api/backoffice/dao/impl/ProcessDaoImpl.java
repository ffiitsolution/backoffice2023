/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffi.api.backoffice.dao.ProcessDao;
import com.ffi.api.backoffice.model.DetailOpname;
import com.ffi.api.backoffice.model.HeaderOpname;
import com.ffi.api.backoffice.model.TableAlias;
import com.ffi.api.backoffice.utils.AppUtil;
import com.ffi.api.backoffice.utils.DynamicRowMapper;
import com.ffi.api.backoffice.utils.FileLoggerUtil;
import com.ffi.api.backoffice.utils.RestApiUtil;
import com.ffi.api.backoffice.utils.TableAliasUtil;
import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IT
 */
@Repository
public class ProcessDaoImpl implements ProcessDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TableAliasUtil tableAliasUtil;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    // String LocalDateTime.now().format(timeFormatter) = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
    // String LocalDateTime.now().format(dateFormatter) = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());

    @Autowired
    public ProcessDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, TableAliasUtil tableAliasUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableAliasUtil = tableAliasUtil;
    }
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    AppUtil appUtil;

    @Value("${endpoint.warehouse}")
    private String urlWarehouse;

    @Value("${endpoint.master}")
    private String urlMaster;

    ///////////////new method from dona 27-02-2023////////////////////////////
    @Override
    public void insertSupplier(Map<String, String> balance) {
        String sql = "INSERT INTO M_SUPPLIER (CD_SUPPLIER,SUPPLIER_NAME,ADDRESS_1,ADDRESS_2,CITY,ZIP_CODE,PHONE, "
                + "FAX,HOMEPAGE,CP_NAME,CP_TITLE,CP_PHONE,CP_PHONE_EXT,CP_MOBILE,CP_EMAIL,FLAG_CANVASING, "
                + "STATUS,USER_UPD,DATE_UPD,TIME_UPD) "
                + "values(:cdSupplier,:supplierName,:address1,:address2,:city,:zipCode,:phone,:fax,:homepage, "
                + ":cpName,:cpTitle,:cpPhone,:cpPhoneExt,:cpMobile,:cpEmail,:flagCanvasing,:status,:userUpd,:dateUpd,:timeUpd) ";
        Map param = new HashMap();
        param.put("cdSupplier", balance.get("cdSupplier"));
        param.put("supplierName", balance.get("supplierName"));
        param.put("address1", balance.get("address1"));
        param.put("address2", balance.get("address2"));
        param.put("city", balance.get("city"));
        param.put("zipCode", balance.get("zipCode"));
        param.put("phone", balance.get("phone"));
        param.put("fax", balance.get("fax"));
        param.put("homepage", balance.get("homepage"));
        param.put("cpName", balance.get("cpName"));
        param.put("cpTitle", balance.get("cpTitle"));
        param.put("cpPhone", balance.get("cpPhone"));
        param.put("cpPhoneExt", balance.get("cpPhoneExt"));
        param.put("cpMobile", balance.get("cpMobile"));
        param.put("cpEmail", balance.get("cpEmail"));
        param.put("flagCanvasing", balance.get("flagCanvasing"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(sql, param);
    }
    /////////////////////////////////done
    ///////////////new method from dona 28-02-2023////////////////////////////

    @Override
    public void updateSupplier(Map<String, String> balance) {
        String qy = "update m_supplier set SUPPLIER_NAME=:supplierName, "
                + "ADDRESS_1=:address1, "
                + "ADDRESS_2=:address2, "
                + "CITY=:city, "
                + "ZIP_CODE=:zipCode, "
                + "PHONE=:phone, "
                + "FAX=:fax, "
                + "HOMEPAGE=:homepage, "
                + "CP_NAME=:cpName, "
                + "CP_TITLE=:cpTitle, "
                + "CP_PHONE=:cpPhone, "
                + "CP_PHONE_EXT=:cpPhoneExt, "
                + "CP_MOBILE=:cpMobile, "
                + "CP_EMAIL=:cpEmail, "
                + "FLAG_CANVASING=:flagCanvasing, "
                + "STATUS=:status, "
                + "USER_UPD=:userUpd, "
                + "DATE_UPD=:dateUpd, "
                + "TIME_UPD=:timeUpd "
                + "where CD_SUPPLIER =:cdSupplier";
        Map param = new HashMap();
        param.put("cdSupplier", balance.get("cdSupplier"));
        param.put("supplierName", balance.get("supplierName"));
        param.put("address1", balance.get("address1"));
        param.put("address2", balance.get("address2"));
        param.put("city", balance.get("city"));
        param.put("zipCode", balance.get("zipCode"));
        param.put("phone", balance.get("phone"));
        param.put("fax", balance.get("fax"));
        param.put("homepage", balance.get("homepage"));
        param.put("cpName", balance.get("cpName"));
        param.put("cpTitle", balance.get("cpTitle"));
        param.put("cpPhone", balance.get("cpPhone"));
        param.put("cpPhoneExt", balance.get("cpPhoneExt"));
        param.put("cpMobile", balance.get("cpMobile"));
        param.put("cpEmail", balance.get("cpEmail"));
        param.put("flagCanvasing", balance.get("flagCanvasing"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
    }

    /////////////////////////////////done
    ///////////////new method from dona 28-02-2023////////////////////////////
    @Override
    public void insertItemSupplier(Map<String, String> balance) {
        //String qy = "UPDATE BUDGET_HEADER SET STATUS=:status where trans_no=:transNo ";
        String qy = "INSERT INTO M_ITEM_SUPPLIER (ITEM_CODE,CD_SUPPLIER,STATUS,USER_UPD, DATE_UPD,TIME_UPD )VALUES ("
                + ":itemCode,:cdSupplier,:status,:userUpd,:dateUpd,:timeUpd)";
        Map param = new HashMap();
        param.put("itemCode", balance.get("itemCode"));
        param.put("cdSupplier", balance.get("cdSupplier"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
    }

    @Override
    public void updateItemSupplier(Map<String, String> balance) {
        String qy = "UPDATE M_ITEM_SUPPLIER SET STATUS=:status,USER_UPD= :userUpd,DATE_UPD = :dateUpd,TIME_UPD=:timeUpd where ITEM_CODE=:itemCode and CD_SUPPLIER=:cdSupplier";
        Map param = new HashMap();
        param.put("itemCode", balance.get("itemCode"));
        param.put("cdSupplier", balance.get("cdSupplier"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
    }
    /////////////////////////////////done

    ///////////////new method from dona 06-03-2023////////////////////////////
    @Override
    public void updateFrayer(Map<String, String> balance) {
        String qy = "UPDATE M_MPCS_DETAIL SET STATUS= :status, USER_UPD= :userUpd, DATE_UPD = :dateUpd, TIME_UPD = :timeUpd where FRYER_TYPE = :fryerType and OUTLET_CODE = :outletCode and FRYER_TYPE_SEQ = :fryerTypeSeq";
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("fryerTypeSeq", balance.get("fryerTypeSeq"));
        param.put("fryerType", balance.get("fryerType"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));

        jdbcTemplate.update(qy, param);
    }
    /////////////////////////////////done
    ///////////////new method from asep 16-mar-2023 ////////////// 

    @Override
    public void insertPos(Map<String, String> balance) {
        String sql = "Insert into M_POS "
                + "(REGION_CODE, OUTLET_CODE, POS_CODE, POS_DESCRIPTION, REF_NO, STATUS, USER_UPD, DATE_UPD, TIME_UPD, POS_TYPE) "
                + "Values "
                + "(:regionCode,:outletCode,:posCode,:posDescription,:refNo,:status,:userUpd,:dateUpd,:timeUpd,:posType)";
        Map param = new HashMap();
        param.put("regionCode", balance.get("regionCode"));
        param.put("outletCode", balance.get("outletCode"));
        param.put("posCode", balance.get("posCode"));
        param.put("posDescription", balance.get("posDescription"));
        param.put("refNo", balance.get("refNo"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        param.put("posType", balance.get("posType"));
        jdbcTemplate.update(sql, param);
    }

    public void updatePos(Map<String, String> balance) {
        String qy = "update m_pos set ref_no=:refNo,status=:status,pos_type=:posType,USER_UPD=:userUpd,DATE_UPD=:dateUpd,TIME_UPD=:timeUpd where region_code=:regionCode and outlet_code=:outletCode and pos_code=:posCode";
        Map param = new HashMap();
        param.put("regionCode", balance.get("regionCode"));
        param.put("outletCode", balance.get("outletCode"));
        param.put("posCode", balance.get("posCode"));
        param.put("posDescription", balance.get("posDescription"));
        param.put("refNo", balance.get("refNo"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        param.put("posType", balance.get("posType"));
        jdbcTemplate.update(qy, param);
    }
    ////////////done
    ///////////////Updated By Pandu 30-03-2023////////////////////////////
    // MODULE MASTER STAFF (M_STAFF) Revisi by lani 31-03-23 //   

    @Override
    public void insertStaff(Map<String, String> balancetest1) {
        String qy = "INSERT INTO M_STAFF(REGION_CODE,OUTLET_CODE,STAFF_CODE,STAFF_NAME,PASSWORD,ID_CARD,SEX,DATE_OF_BIRTH,ADDRESS_1,ADDRESS_2,CITY,"
                + "PHONE_NO,MOBILE_PHONE_NO,EMPLOY_DATE,RESIGN_DATE,POSITION,ACCESS_LEVEL,RIDER_FLAG,GROUP_ID,STATUS,USER_UPD,DATE_UPD,TIME_UPD,STAFF_FULL_NAME)"
                + "VALUES(:regionCode,:outletCode,:staffCode,:staffName,:passwordCode,:idCard,:sexType,:dateOfBirth,:address1,:address2,"
                + ":code,:phoneNumber,:mobilePhoneNumber,:employDate,:resignDate,:positionCode,:accesslevelCode,:riderFlag,"
                + ":groupidName,:status,:userUpd,:dateUpd,:timeUpd,:staffFullName)";

        String qy2 = "INSERT INTO M_POS_STAFF(REGION_CODE,OUTLET_CODE,STAFF_CODE,STAFF_POS_CODE,STAFF_NAME,PASSWORD,"
                + "ACCESS_LEVEL,RIDER_FLAG,STATUS,USER_UPD,DATE_UPD,TIME_UPD)"
                + "VALUES(:regionCode,:outletCode,:staffCode,:staffPosCode,:staffName,:passPosCode,"
                + ":accesslevelCode,:riderFlag,:statusPos,:userUpd,:dateUpd,:timeUpd)";

        Map param = new HashMap();
        param.put("regionCode", balancetest1.get("regionCode"));
        param.put("outletCode", balancetest1.get("outletCode"));
        param.put("staffCode", balancetest1.get("staffCode"));
        param.put("staffName", balancetest1.get("staffName"));
        param.put("staffFullName", balancetest1.get("staffFullName"));
        param.put("passwordCode", balancetest1.get("passwordCode"));
        param.put("idCard", balancetest1.get("idCard"));
        param.put("sexType", balancetest1.get("sexType"));
        param.put("dateOfBirth", balancetest1.get("dateOfBirth"));
        param.put("address1", balancetest1.get("address1"));
        param.put("address2", balancetest1.get("address2"));
        param.put("code", balancetest1.get("cityCode"));
        param.put("phoneNumber", balancetest1.get("phoneNumber"));
        param.put("mobilePhoneNumber", balancetest1.get("mobile"));
        param.put("employDate", balancetest1.get("employeeDate"));
        param.put("resignDate", balancetest1.get("resignDate"));
        param.put("positionCode", balancetest1.get("positionCode"));

        if (balancetest1.get("accessLevelCode").length() <= 3) {
            param.put("accesslevelCode", balancetest1.get("accessLevelCode"));
        } else {
            param.put("accesslevelCode", "");
        }

        param.put("riderFlag", balancetest1.get("riderFlag"));
        param.put("groupidName", balancetest1.get("groupCode"));
        param.put("staffPosCode", balancetest1.get("staffPosCode"));
        param.put("passPosCode", balancetest1.get("passPosCode"));
        param.put("status", balancetest1.get("status"));
        param.put("statusPos", balancetest1.get("statusPos"));
        param.put("userUpd", balancetest1.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));

        jdbcTemplate.update(qy, param);

        if (!Objects.equals(balancetest1.get("staffPosCode"), "")) {
            jdbcTemplate.update(qy2, param);
        }
    }

    @Override
    public void updateStaff(Map<String, String> balancetest1) {
        String qy = "UPDATE M_STAFF SET STAFF_NAME = :staffName, ID_CARD = :idCard, STAFF_FULL_NAME = :staffFullName, ADDRESS_2 = :address2,"
                + "PASSWORD=:passwordCode, EMPLOY_DATE=:employDate, RESIGN_DATE=:resignDate, POSITION=:positionCode, ACCESS_LEVEL=:accesslevelCode, RIDER_FLAG=:riderFlag,"
                + "SEX=:sexType, DATE_OF_BIRTH=:dateOfBirth, ADDRESS_1=:address1, CITY=:code, PHONE_NO=:phoneNumber, "
                + "MOBILE_PHONE_NO=:mobilePhoneNumber, GROUP_ID=:groupidName, STATUS=:status, USER_UPD=:userUpd, DATE_UPD=:dateUpd, TIME_UPD=:timeUpd "
                + "WHERE STAFF_CODE=:staffCode AND OUTLET_CODE =:outletCode ";    //    String qy = "UPDATE M_STAFF SET STAFF_NAME=:staffName, STAFF_FULL_NAME=:staffFullName, PASSWORD=:staffPassword, USER_UPD=:dateUpd, DATE_UPD=:timeUpd, TIME_UPD=:staffTimeUpdate where STAFF_CODE=:staffCode ";

        String qy2 = "UPDATE M_POS_STAFF SET PASSWORD=:passPosCode,STATUS=:statusPos,STAFF_POS_CODE=:staffPosCode,"
                + " ACCESS_LEVEL=:accesslevelCode,USER_UPD=:userUpd,DATE_UPD=:dateUpd,TIME_UPD=:timeUpd "
                + " WHERE STAFF_CODE=:staffCode AND OUTLET_CODE = :outletCode ";

        String qy3 = "INSERT INTO M_POS_STAFF(REGION_CODE,OUTLET_CODE,STAFF_CODE,STAFF_POS_CODE,STAFF_NAME,PASSWORD,"
                + "ACCESS_LEVEL,RIDER_FLAG,STATUS,USER_UPD,DATE_UPD,TIME_UPD)"
                + "VALUES(:regionCode,:outletCode,:staffCode,:staffPosCode,:staffName,:passPosCode,"
                + ":accesslevelCode,:riderFlag,:statusPos,:userUpd,:dateUpd,:timeUpd)";

        Map param = new HashMap();
        param.put("regionCode", balancetest1.get("regionCode"));
        param.put("outletCode", balancetest1.get("outletCode"));
        param.put("staffCode", balancetest1.get("staffCode"));
        param.put("staffName", balancetest1.get("staffName"));
        param.put("staffFullName", balancetest1.get("staffFullName"));
        param.put("passwordCode", balancetest1.get("passwordCode"));
        param.put("idCard", balancetest1.get("idCard"));
        param.put("sexType", balancetest1.get("sexType"));
        param.put("dateOfBirth", balancetest1.get("dateOfBirth"));
        param.put("address1", balancetest1.get("address1"));
        param.put("address2", balancetest1.get("address2"));
        param.put("code", balancetest1.get("cityCode"));
        param.put("phoneNumber", balancetest1.get("phoneNumber"));
        param.put("mobilePhoneNumber", balancetest1.get("mobile"));
        param.put("employDate", balancetest1.get("employeeDate"));
        param.put("resignDate", balancetest1.get("resignDate"));
        param.put("positionCode", balancetest1.get("positionCode"));

        if (balancetest1.get("accessLevelCode").length() <= 3) {
            param.put("accesslevelCode", balancetest1.get("accessLevelCode"));
        } else {
            param.put("accesslevelCode", "");
        }

        param.put("riderFlag", balancetest1.get("riderFlag"));
        param.put("groupidName", balancetest1.get("groupCode"));
        param.put("staffPosCode", balancetest1.get("staffPosCode"));
        param.put("passPosCode", balancetest1.get("passPosCode"));
        param.put("status", balancetest1.get("status"));
        param.put("statusPos", balancetest1.get("statusPos"));
        param.put("userUpd", balancetest1.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));

        jdbcTemplate.update(qy, param);

        if (!Objects.equals(balancetest1.get("staffPosCode"), "")) {
            try {
                jdbcTemplate.update(qy3, param);
            } catch (Exception exx) {
                jdbcTemplate.update(qy2, param);
            }
        }
    }

    @Override
    public void deleteStaff(Map<String, String> balancetest) {
        // DELETE DATA KE DALAM TABLE YG PERTAMA
        String qy = "DELETE FROM M_STAFF WHERE STAFF_CODE=:staffCode ";
        Map param = new HashMap();
        param.put("staffCode", balancetest.get("staffCodex"));
        jdbcTemplate.update(qy, param);
    }

    // ==================================================================================================================================================================================//
    ///////////////Done////////////////////////////
    ///////////////new method from dona 28-02-2023////////////////////////////
    @Override
    public void insertFryer(Map<String, String> balance) {
        String qy = "INSERT INTO M_MPCS_DETAIL (OUTLET_CODE,FRYER_TYPE,FRYER_TYPE_SEQ,FRYER_TYPE_CNT,FRYER_TYPE_RESET,STATUS,USER_UPD,DATE_UPD,TIME_UPD,FRYER_TYPE_SEQ_CNT )VALUES ("
                + ":outletCode,:fryerType,:fryerTypeSeq,:fryerTypeCnt,:fryerTypeReset,:status,:userUpd,:dateUpd,:timeUpd,:fryerTypeSeqCnt)";
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("fryerType", balance.get("fryerType"));
        param.put("fryerTypeSeq", balance.get("fryerTypeSeq"));
        param.put("fryerTypeCnt", balance.get("fryerTypeCnt"));
        param.put("fryerTypeReset", balance.get("fryerTypeReset"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        param.put("fryerTypeSeqCnt", balance.get("fryerTypeSeqCnt"));

        jdbcTemplate.update(qy, param);
    }

    ///////////////Done////////////////////////////
    ///////////////NEW METHOD LIST COND AND DATA GLOBAL BY LANI 4 APRIL 2023////
    @Override
    public void insertMasterGlobal(Map<String, String> balance) {
        String qy = "INSERT INTO M_GLOBAL (COND,CODE,DESCRIPTION,VALUE,STATUS,USER_UPD,DATE_UPD,TIME_UPD)"
                + "VALUES ("
                + ":cond,:code,:description,:value,:status,:userUpd,:dateUpd,:timeUpd)";
        Map param = new HashMap();
        param.put("cond", balance.get("cond"));
        param.put("code", balance.get("code"));
        param.put("description", balance.get("description"));
        param.put("value", balance.get("value"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
    }

    @Override
    public void updateMasterGlobal(Map<String, String> balance) {
        String qy = "UPDATE M_GLOBAL SET COND = :cond,CODE = :code,DESCRIPTION =:description,"
                + " VALUE =:value,STATUS =:status,USER_UPD =:userUpd,DATE_UPD =:dateUpd,TIME_UPD =:timeUpd"
                + " WHERE COND = :cond AND CODE = :code";
        Map param = new HashMap();
        param.put("cond", balance.get("cond"));
        param.put("code", balance.get("code"));
        param.put("description", balance.get("description"));
        param.put("value", balance.get("value"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 14 APRIL 2023////
    @Override
    public Map<String,Object> insertOrderHeader(Map<String, String> balance) {

        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);
        Gson gson = new Gson();

        String insertOrderHeader = "INSERT INTO T_ORDER_HEADER (OUTLET_CODE,ORDER_TYPE,ORDER_ID,ORDER_NO,ORDER_DATE,ORDER_TO,CD_SUPPLIER,DT_DUE,DT_EXPIRED,REMARK,NO_OF_PRINT,STATUS,USER_UPD,DATE_UPD,TIME_UPD)"
                + " VALUES(:outletCode,:orderType,:orderId,:orderNo,:orderDate,:orderTo,:cdSupplier,:dtDue,:dtExpired,:remark,:noOfPrint,:status,:userUpd,:dateUpd,:timeUpd)";

        String orderNo = checkOrderNo(balance.get("orderNo"));
        String orderId = checkOrderId(balance.get("orderId"));
        
        balance.put("orderId", orderId);
        balance.put("orderNo", orderNo);
        balance.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        balance.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        balance.put("year", year);
        balance.put("month", month);
               
        JsonArray details = gson.toJsonTree(balance.get("details")).getAsJsonArray();
        details.forEach(detail -> {
            Map<String, String> bal = gson.fromJson(detail, new TypeToken<Map<String, String>>() {
            }.getType());
            bal.put("outletCode", (String) balance.get("outletCode"));
            bal.put("orderId", (String) balance.get("orderId"));
            bal.put("orderNo", (String) balance.get("orderNo"));
            bal.put("orderType", (String) balance.get("orderType"));
            bal.put("userUpd", (String) balance.get("userUpd"));
            bal.put("dateUpd", (String) balance.get("dateUpd"));
            bal.put("timeUpd", (String) balance.get("timeUpd"));

            insertOrderDetail(bal);
        });

        jdbcTemplate.update(insertOrderHeader, balance);
        
        String returnDetailItem = "SELECT * FROM T_ORDER_detail toh WHERE toh.ORDER_no = :orderNo ";
        List<Map<String, Object>> list = jdbcTemplate.query(returnDetailItem, balance, new DynamicRowMapper());

        Map<String, Object> returnedItem = new HashMap();
        returnedItem.put("detail", list);
        returnedItem.put("orderId", orderId);
        returnedItem.put("orderNo", orderNo);
        
        return returnedItem;
    }
    
    public String checkOrderNo(String orderNo) {
        System.out.print("orderNo function: " + orderNo);
        String orderNoCanUse = orderNo;
        String checkOrderNoExist = "SELECT count(*) FROM T_ORDER_HEADER toh WHERE toh.ORDER_NO = '" + orderNo + "' ";
        Integer count = jdbcTemplate.queryForObject(checkOrderNoExist, new HashMap(), Integer.class);
        if (count > 0) {
            Integer newCounter = Integer.parseInt(orderNo.substring(orderNo.length() - 5)) + 1;
            orderNoCanUse = orderNo.substring(0, 9) + newCounter ;
            return checkOrderNo(orderNoCanUse);
        } 
        return orderNoCanUse;
    }
    
    public String checkOrderId(String orderId) {
        String orderIdCanUse = orderId;
        String checkOrderNoExist = "SELECT count(*) FROM T_ORDER_HEADER toh WHERE toh.ORDER_ID = '" + orderIdCanUse + "' ";
        Integer count = jdbcTemplate.queryForObject(checkOrderNoExist, new HashMap(), Integer.class);
        if (count > 0) {
            Integer counter = Integer.parseInt(orderIdCanUse) + 1;
            orderIdCanUse = String.valueOf(counter);
            return checkOrderId(String.valueOf(orderIdCanUse));
        } 
        
        return orderIdCanUse;
    }

    @Override
    public void updateMCounter(Map<String, String> balance) {

        LocalDate transDate = this.jdbcTemplate.queryForObject("SELECT TRANS_DATE FROM M_OUTLET WHERE OUTLET_CODE = :outletCode", balance, LocalDate.class);
        int month = transDate.getMonthValue();
        int year = transDate.getYear();

        String qy1 = "update m_counter  "
                + "       set COUNTER_NO = (select COUNTER_NO+1 FROM M_COUNTER  "
                + "                        where YEAR = :year  "
                + "                        AND MONTH= :month  "
                + "                        AND TRANS_TYPE = 'ID'  "
                + "                        AND OUTLET_CODE= :outletCode ) "
                + "where YEAR = :year AND MONTH= :month AND TRANS_TYPE = 'ID' AND OUTLET_CODE= :outletCode";

        String qy2 = "update m_counter  "
                + "       set COUNTER_NO = (select COUNTER_NO+1 FROM M_COUNTER  "
                + "                        where YEAR = :year  "
                + "                        AND MONTH= :month  "
                + "                        AND TRANS_TYPE = :transType  "
                + "                        AND OUTLET_CODE= :outletCode ) "
                + "where YEAR = :year AND MONTH= :month AND TRANS_TYPE = :transType AND OUTLET_CODE= :outletCode";

        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("orderType", balance.get("orderType"));
        param.put("orderId", balance.get("orderId"));
        param.put("orderNo", balance.get("orderNo"));
        param.put("orderDate", balance.get("orderDate"));
        param.put("orderTo", balance.get("orderTo"));
        param.put("cdSupplier", balance.get("cdSupplier"));
        param.put("dtDue", balance.get("dtDue"));
        param.put("dtExpired", balance.get("dtExpired"));
        param.put("remark", balance.get("remark"));
        param.put("noOfPrint", balance.get("noOfPrint"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        param.put("year", year);
        param.put("month", month);
        param.put("transType", balance.get("transType"));
        jdbcTemplate.update(qy1, param);
        jdbcTemplate.update(qy2, param);
    }
    /////////////////////////////////DONE///////////////////////////////////////

    ///////////////NEW METHOD LIST ORDER DETAIL BY DONA 27 APRIL 2023////
    @Override
    public void insertOrderDetail(Map<String, String> balance) {
        String qy = "INSERT INTO T_ORDER_DETAIL (OUTLET_CODE,ORDER_TYPE,ORDER_ID,ORDER_NO,ITEM_CODE,QTY_1,CD_UOM_1,QTY_2,CD_UOM_2,TOTAL_QTY_STOCK,UNIT_PRICE,USER_UPD,DATE_UPD,TIME_UPD)"
                + " VALUES(:outletCode,:orderType,:orderId,:orderNo,:itemCode,:qty1,:cdUom1,:qty2,:cdUom2,:totalQtyStock,:unitPrice,:userUpd,:dateUpd,:timeUpd)";
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("orderType", balance.get("orderType"));
        param.put("orderId", balance.get("orderId"));
        param.put("orderNo", balance.get("orderNo"));
        param.put("itemCode", balance.get("itemCode"));
        param.put("qty1", balance.get("jmlBesar"));
        param.put("cdUom1", balance.get("satuanBesar"));
        param.put("qty2", balance.get("jmlKecil"));
        param.put("cdUom2", balance.get("satuanKecil"));
        param.put("totalQtyStock", balance.get("totalQty"));
        param.put("unitPrice", "0");
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
    }

    @Override
    public void updateOrderDetail(Map<String, String> balance) {
        String qy = "update t_order_detail set "
                + "QTY_1 =:jmlBesar,"
                + "CD_UOM_1 =:satuanBesar,"
                + "QTY_2 =:jmlKecil,"
                + "CD_UOM_2 =:satuanKecil,"
                + "TOTAL_QTY_STOCK =:totalQty,"
                + "USER_UPD =:userUpd,"
                + "DATE_UPD =:dateUpd,"
                + "TIME_UPD =:timeUpd,"
                + "UNIT_PRICE ='0' "
                + "WHERE OUTLET_CODE =:outletCode AND "
                + "ORDER_TYPE =:orderType AND "
                + "ORDER_ID =:orderId AND "
                + "ORDER_NO =:orderNo AND "
                + "ITEM_CODE =:itemCode";
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("orderType", balance.get("orderType"));
        param.put("orderId", balance.get("orderId"));
        param.put("orderNo", balance.get("orderNo"));
        param.put("itemCode", balance.get("itemCode"));
        param.put("jmlBesar", balance.get("jmlBesar"));
        param.put("satuanBesar", balance.get("satuanBesar"));
        param.put("jmlKecil", balance.get("jmlKecil"));
        param.put("satuanKecil", balance.get("satuanKecil"));
        param.put("totalQty", balance.get("totalQty"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////new method from dona 28-02-2023////////////////////////////
    @Override
    public void updateMasterCounter(Map<String, String> balance) {

        String qy = "update m_counter set ("
                + "counter_no =:counterNo where tr";
        Map param = new HashMap();
        param.put("itemCode", balance.get("itemCode"));
        param.put("cdSupplier", balance.get("cdSupplier"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
    }
    /////////////////////////////////DONE///////////////////////////////////////

    @Override
    public void inserOpnameHeader(HeaderOpname balance) {

        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);

        String opNo = opnameNumber(year, month, balance.getTransType(), balance.getOutletCode());
        balance.setOpnameNo(opNo);

        String qy = "INSERT INTO T_OPNAME_HEADER (OUTLET_CODE,CD_TEMPLATE,OPNAME_NO,OPNAME_DATE,REMARK,STATUS,USER_UPD,DATE_UPD,TIME_UPD) "
                + "values(:outletCode,:cdTemplate,:opnameNo,:opnameDate,:remark,:status,:userUpd,:dateUpd,:timeUpd)";

        String qry = "INSERT INTO T_OPNAME_DETAIL ( "
                + "SELECT SC.OUTLET_CODE,:opnameNo,SC.ITEM_CODE, "
                + "(SC.QTY_BEGINNING+SC.QTY_IN-SC.QTY_OUT) QTY_FREEZE,0 COST_FREEZE, "
                + "0 QTY_PURCH,I.UOM_PURCHASE,0 QTY_STOCK,I.UOM_STOCK,0 TOTAL_QTY, "
                + " :userUpd USER_UPD,:dateUpd DATE_UPD,:timeUpd TIME_UPD "
                + "FROM T_STOCK_CARD SC "
                + "LEFT JOIN M_ITEM I ON I.ITEM_CODE = SC.ITEM_CODE "
                + "WHERE SC.OUTLET_CODE = :outletCode AND I.STATUS = 'A' "
                + "AND SC.TRANS_DATE = :opnameDate  "
                + ")";

        Map param = new HashMap();
        param.put("outletCode", balance.getOutletCode());
        param.put("cdTemplate", balance.getCdTemplate());
        param.put("opnameNo", opNo);
        param.put("opnameDate", balance.getOpnameDate());
        param.put("remark", balance.getRemark() == null || balance.getRemark().length() < 1 ? " " : balance.getRemark());
        param.put("status", balance.getStatus());
        param.put("userUpd", balance.getUserUpd());
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
        param.put("year", year);
        param.put("month", month);
        param.put("transType", balance.getTransType());
        jdbcTemplate.update(qry, param);
    }

    public String opnameNumber(String year, String month, String transType, String outletCode) {
        // update by M Joko 19/12/23 : cek opname number untuk handle m_counter di bulan yg belum ada, perbaikan format sesuai yg berjalan SOP09361220232 ke SOP093623120002
        String sql = "SELECT :transType||ORDER_ID||LPAD(COUNTNO, 4, '0') ORDER_ID FROM ( "
                + "SELECT A.OUTLET_CODE||SUBSTR(A.YEAR, -2)||LPAD(:month, 2, '0') AS ORDER_ID,A.COUNTER_NO+1 COUNTNO FROM M_COUNTER A "
                + "LEFT JOIN M_OUTLET B "
                + "ON B.OUTLET_CODE=A.OUTLET_CODE "
                + "WHERE A.YEAR = :year AND A.MONTH= :month AND A.TRANS_TYPE = :transType AND A.OUTLET_CODE= :outletCode)";
        Map param = new HashMap();
        param.put("year", year);
        param.put("month", month);
        param.put("transType", transType);
        param.put("outletCode", outletCode);
        System.err.println("q : " + sql);
        System.err.println("p : " + param);
        String result = "";
        try {
            result = jdbcTemplate.queryForObject(sql, param, new RowMapper() {
                @Override
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    System.err.println("rs " + i + ": " + rs);
                    return rs.getString(1) == null ? "0" : rs.getString(1);
                }
            }).toString();
        } catch (DataAccessException e) {
            System.err.println("DataAccessException : " + e);
        }

        System.err.println("result : " + result);
        if (result == null || result.equalsIgnoreCase("[]") || result.isEmpty()) {
            updateMCounterSop(transType, outletCode);
            return opnameNumber(year, month, transType, outletCode);
        } else {
            return result;
        }
    }

    @Override
    public void updateMCounterSop(String transType, String outletCode) {

        // DateFormat df = new SimpleDateFormat("MM");
        // DateFormat dfYear = new SimpleDateFormat("yyyy");
        // Date tgl = new Date();
        // String month = df.format(tgl);
        // String year = dfYear.format(tgl);
        // update query ambil no, handle jika kosong by M Joko 19-12-23
        String selectSql = "SELECT COUNT(*) FROM m_counter WHERE OUTLET_CODE = :outletCode AND TRANS_TYPE = :transType AND YEAR = :year AND MONTH = :month";
        String updateSql = "UPDATE m_counter SET COUNTER_NO = COUNTER_NO + 1 WHERE OUTLET_CODE = :outletCode AND TRANS_TYPE = :transType AND YEAR = :year AND MONTH = :month";
        String insertSql = "INSERT INTO m_counter (OUTLET_CODE, TRANS_TYPE, YEAR, MONTH, COUNTER_NO) VALUES (:outletCode, :transType, :year, :month, 0)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("outletCode", outletCode);
        params.addValue("transType", transType);

        // using transDate from outlet code by Dani 20 Dec 2023
        LocalDate transDate = LocalDate.parse(this.jdbcTemplate.queryForObject(
                "SELECT DISTINCT TO_CHAR(TRANS_DATE, 'YYYY-MM-DD') FROM M_OUTLET WHERE OUTLET_CODE = :outletCode and status = 'A'",
                params, String.class));
        params.addValue("year", transDate.getYear());
        params.addValue("month", transDate.getMonthValue());

        int count = jdbcTemplate.queryForObject(selectSql, params, Integer.class);

        if (count > 0) {
            // Record exists, perform update
            jdbcTemplate.update(updateSql, params);
        } else {
            // Record does not exist, perform insert
            jdbcTemplate.update(insertSql, params);
        }
    }

    ///////////////new method updateStatusOpname 6-11-2023////////////////////////////
    /////////////// update menambah item ke t_stock_card_detail by M Joko 18-12-2023////////////////////////////
    @Override
    public List updateOpnameStatus(Map balance) {
        Map param = new HashMap();
        Integer status = Integer.valueOf(balance.get("status").toString());
        Integer confirmZero = Integer.valueOf(balance.getOrDefault("confirmZero", 0).toString());
        List<Map<String, Object>> list = new ArrayList();
        System.out.println("updateOpnameStatus status = " + status);
        param.put("outletCode", balance.get("outletCode"));
        param.put("opnameNo", balance.get("opnameNo"));
        param.put("status", status);

        // cek jika ada nilai nol dan belum di confirmZero, kembalikan list item yg ada freeze namun total 0
        if (confirmZero < 1 && (status == 1 || status == '1')) {
            String qry = "SELECT O.OPNAME_NO, O.ITEM_CODE, MI.ITEM_DESCRIPTION, O.QTY_FREEZE, O.QTY_PURCH, O.UOM_PURCH, O.QTY_STOCK, O.UOM_STOCK, O.TOTAL_QTY FROM T_OPNAME_DETAIL O LEFT JOIN M_ITEM MI ON O.ITEM_CODE = MI.ITEM_CODE WHERE O.OUTLET_CODE = :outletCode AND O.OPNAME_NO = :opnameNo AND O.QTY_FREEZE != 0 AND (O.QTY_STOCK + O.QTY_PURCH) = 0 ORDER BY O.ITEM_CODE";
            list = jdbcTemplate.query(qry, param, new DynamicRowMapper());
            if (!list.isEmpty()) {
                System.out.println("terdapat nilai semua 0 saat opname: " + list.size());
                return list;
            }
        }

        String qy = "update t_opname_header set status =:status WHERE OPNAME_NO = :opnameNo and OUTLET_CODE= :outletCode";
        jdbcTemplate.update(qy, param);
        if (status == 1 || status == '1') {
            itemOpnameToStockCard(balance);
            // update by M Joko 20-dec-23 
            updateMCounterAfterStockOpname(balance);
        }
        return list;
    }

    public void itemOpnameToStockCard(Map<String, String> balance) {
        // update by M Joko 18-12-23
        Map param = new HashMap();
        param.put("opnameNo", balance.get("opnameNo"));
        param.put("userUpd", balance.getOrDefault("userUpd", "SYSTEM"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        // hanya yg ada nilai di in dan out yg dimasukkan ke stock card detail
        String qryListStockOpname = "SELECT * FROM ( SELECT od.OUTLET_CODE, (SELECT trans_date FROM m_outlet WHERE outlet_code = od.outlet_code) AS TRANS_DATE, od.ITEM_CODE, 'SOP' AS CD_TRANS, CASE WHEN od.qty_freeze < od.total_qty THEN od.total_qty - od.qty_freeze ELSE 0 END AS QUANTITY_IN, CASE WHEN od.qty_freeze > od.total_qty THEN od.qty_freeze - od.total_qty ELSE 0 END AS QUANTITY, :userUpd AS USER_UPD, :dateUpd AS DATE_UPD, :timeUpd AS TIME_UPD FROM T_OPNAME_DETAIL od WHERE od.opname_no = :opnameNo ) WHERE QUANTITY_IN NOT IN (0, '0') UNION ALL SELECT * FROM ( SELECT od.OUTLET_CODE, (SELECT trans_date FROM m_outlet WHERE outlet_code = od.outlet_code) AS TRANS_DATE, od.ITEM_CODE, 'SOP' AS CD_TRANS, CASE WHEN od.qty_freeze < od.total_qty THEN od.total_qty - od.qty_freeze ELSE 0 END AS QUANTITY_IN, CASE WHEN od.qty_freeze > od.total_qty THEN od.qty_freeze - od.total_qty ELSE 0 END AS QUANTITY, :userUpd AS USER_UPD, :dateUpd AS DATE_UPD, :timeUpd AS TIME_UPD FROM T_OPNAME_DETAIL od WHERE od.opname_no = :opnameNo ) WHERE QUANTITY NOT IN (0, '0')";

        String qryToStockCardDetail = "insert into t_stock_card_detail ( " + qryListStockOpname + " )";
        try {
            jdbcTemplate.update(qryToStockCardDetail, param);
        } catch (DataAccessException e) {
            System.out.println("error insert so to sc detail: " + param);
        }

        // update method update by M Joko 15 Jan 24
        List<Map<String, Object>> list = jdbcTemplate.query(qryListStockOpname, param, new DynamicRowMapper());

        if (!list.isEmpty()) {
            for (Map<String, Object> sc : list) {
                Map paramUpd = new HashMap();
                paramUpd.put("itemCode", sc.get("itemCode"));
                paramUpd.put("outletCode", sc.get("outletCode"));
                paramUpd.put("quantityIn", sc.get("quantityIn"));
                paramUpd.put("quantity", sc.get("quantity"));
                String qryUpd = """
                    UPDATE t_stock_card sc
                    SET 
                        sc.QTY_IN = NVL(sc.QTY_IN,0) + NVL(:quantityIn,0),
                        sc.QTY_OUT = NVL(sc.QTY_OUT,0) + NVL(:quantity,0)
                    WHERE sc.trans_date = (SELECT trans_date FROM m_outlet WHERE outlet_code = :outletCode) AND sc.item_code = :itemCode
                                            """;
                try {
                    System.out.println("akan update so to sc detail: " + paramUpd);
                    jdbcTemplate.update(qryUpd, paramUpd);
                } catch (DataAccessException e) {
                    System.out.println("error update sc: " + paramUpd);
                }
            }
        }
    }
///////////////done///////////////

    @Override
    public void inserOpnameDetail(DetailOpname opnameDtls) {

        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);
        String qy = "";
        for (DetailOpname dtls : opnameDtls.getDetails()) {

            String cekOsDetails = cekOsDetails(opnameDtls.getOutletCode(), opnameDtls.getOpnameNo(), dtls.getItemCode());

            if (cekOsDetails.equals("0")) {
                qy = "INSERT INTO T_OPNAME_DETAIL (OUTLET_CODE,OPNAME_NO,ITEM_CODE,QTY_FREEZE,COST_FREEZE,QTY_PURCH, "
                        + "UOM_PURCH,QTY_STOCK,UOM_STOCK,TOTAL_QTY,USER_UPD,DATE_UPD,TIME_UPD) "
                        + "values(:outletCode,:opnameNo,:itemCode,:qtyFreeze,:costFreeze,:qtyPurch,:uomPurch,:qtyStock,:uomStock,"
                        + ":totalQty,:userUpd,:dateUpd,:timeUpd)";
                Map param = new HashMap();
                param.put("outletCode", opnameDtls.getOutletCode());
                param.put("opnameNo", opnameDtls.getOpnameNo());
                param.put("userUpd", opnameDtls.getUserUpd());
                param.put("itemCode", dtls.getItemCode());
                param.put("qtyFreeze", dtls.getQtyFreeze());
                param.put("costFreeze", dtls.getCostFreeze());
                param.put("costFreeze", dtls.getCostFreeze());
                param.put("qtyPurch", dtls.getQtyPurch());
                param.put("uomPurch", dtls.getUomPurch());
                param.put("qtyStock", dtls.getQtyStock());
                param.put("uomStock", dtls.getUomStock());
                param.put("totalQty", dtls.getTotalQty());
                param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
                param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
                jdbcTemplate.update(qy, param);
            } else {
                qy = "UPDATE T_OPNAME_DETAIL  "
                        + "SET QTY_PURCH = :qtyPurch , QTY_STOCK = :qtyStock, TOTAL_QTY = :totalQty, "
                        + "USER_UPD = :userUpd, DATE_UPD = :dateUpd , TIME_UPD = :timeUpd "
                        + "WHERE OPNAME_NO = :opnameNo AND OUTLET_CODE = :outletCode AND ITEM_CODE = :itemCode";
                Map param = new HashMap();
                param.put("outletCode", opnameDtls.getOutletCode());
                param.put("opnameNo", opnameDtls.getOpnameNo());
                param.put("userUpd", opnameDtls.getUserUpd());
                param.put("itemCode", dtls.getItemCode());
                param.put("qtyFreeze", dtls.getQtyFreeze());
                param.put("costFreeze", dtls.getCostFreeze());
                param.put("costFreeze", dtls.getCostFreeze());
                param.put("qtyPurch", dtls.getQtyPurch());
                param.put("uomPurch", dtls.getUomPurch());
                param.put("qtyStock", dtls.getQtyStock());
                param.put("uomStock", dtls.getUomStock());
                param.put("totalQty", dtls.getTotalQty());
                param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
                param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
                jdbcTemplate.update(qy, param);
            }

        }
    }

    public String cekOsDetails(String outletCode, String opnameNo, String itemCode) {

        String sql = "select count(*) cek  "
                + "from T_OPNAME_DETAIL where OUTLET_CODE = :outletCode and OPNAME_NO = :opnameNo and ITEM_CODE = :itemCode";
        Map param = new HashMap();
        param.put("opnameNo", opnameNo);
        param.put("itemCode", itemCode);
        param.put("outletCode", outletCode);
        return jdbcTemplate.queryForObject(sql, param, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString(1) == null ? "0" : rs.getString(1);

            }
        }).toString();
    }

    @Override
    public void insertSoToScDtl(Map<String, String> balance) {

        String qry = "INSERT INTO T_STOCK_CARD_DETAIL ( "
                + "SELECT * FROM ( "
                + "SELECT OUTLET_CODE,TRANS_DATE,ITEM_CODE,TRANS_TYPE, "
                + "QTY QTY_IN,0 QTY_OUT, "
                + "USER_UPD, DATE_UPD, TIME_UPD "
                + "FROM ( "
                + "select OUTLET_CODE,:opnameDate TRANS_DATE,ITEM_CODE,:transType TRANS_TYPE, "
                + "TOTAL_QTY - (QTY_FREEZE) QTY,:userUpd USER_UPD,:dateUpd DATE_UPD,:timeUpd TIME_UPD "
                + "from T_OPNAME_DETAIL  "
                + "where OUTLET_CODE = :outletCode AND OPNAME_NO = :opnameNo) "
                + "WHERE QTY < = 0 "
                + "union all "
                + "SELECT OUTLET_CODE,TRANS_DATE,ITEM_CODE,TRANS_TYPE, "
                + "0 QTY_IN, QTY QTY_OUT, "
                + "USER_UPD, DATE_UPD, TIME_UPD "
                + "FROM ( "
                + "select OUTLET_CODE,:opnameDate TRANS_DATE,ITEM_CODE,:transType TRANS_TYPE, "
                + "TOTAL_QTY - (QTY_FREEZE) QTY,:userUpd USER_UPD, :dateUpd DATE_UPD, :timeUpd TIME_UPD "
                + "from T_OPNAME_DETAIL  "
                + "where OUTLET_CODE = :outletCode AND OPNAME_NO = :opnameNo) "
                + "WHERE QTY > 0) "
                + ")";

        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("opnameNo", balance.get("opnameNo"));
        param.put("opnameDate", balance.get("opnameDate"));
        param.put("transType", balance.get("transType"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qry, param);

        try {
            String qry1 = "SELECT OUTLET_CODE,TRANS_DATE,ITEM_CODE,CD_TRANS,QUANTITY_IN,QUANTITY"
                    + " FROM T_STOCK_CARD_DETAIL WHERE OUTLET_CODE = :outletCode AND CD_TRANS = 'SOP' AND TRANS_DATE = :opnameDate";
            Map prm = new HashMap();
            prm.put("outletCode", balance.get("outletCode"));
            prm.put("opnameNo", balance.get("opnameNo"));
            prm.put("opnameDate", balance.get("opnameDate"));
            prm.put("transType", balance.get("transType"));
            prm.put("userUpd", balance.get("userUpd"));
            prm.put("dateUpd", LocalDateTime.now().format(dateFormatter));
            prm.put("timeUpd", LocalDateTime.now().format(timeFormatter));
            System.err.println("q1 :" + qry1);
            List<Map<String, Object>> list = jdbcTemplate.query(qry1, prm, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rt = new HashMap<String, Object>();
                    rt.put("outletCode", rs.getString("OUTLET_CODE"));
                    rt.put("transDate", rs.getString("TRANS_DATE"));
                    rt.put("itemCode", rs.getString("ITEM_CODE"));
                    rt.put("cdTrans", rs.getString("CD_TRANS"));
                    rt.put("qtyIn", rs.getString("QUANTITY_IN"));
                    rt.put("qtyOut", rs.getString("QUANTITY"));
                    return rt;
                }
            });

            for (Map<String, Object> opn : list) {
                String itemCode = (String) opn.get("itemCode");
                String i = (String) opn.get("qtyIn");
                String o = (String) opn.get("qtyOut");

                double in = Double.valueOf(i);
                double out = Double.valueOf(o);

                String qty_in = cekQtyIn(balance.get("outletCode"), balance.get("opnameDate"), itemCode);
                String qty_out = cekQtyOut(balance.get("outletCode"), balance.get("opnameDate"), itemCode);

                double qtyIn = Double.valueOf(qty_in);
                double qtyOut = Double.valueOf(qty_out);

                double totIn = qtyIn + in;
                double totOut = qtyOut + out;

                String qry2 = "UPDATE T_STOCK_CARD  "
                        + "SET QTY_IN = :qtyIn ,QTY_OUT = :qtyOut ,REMARK = :cdTrans , USER_UPD = :userUpd ,  "
                        + "DATE_UPD = :dateUpd, TIME_UPD = :timeUpd "
                        + "WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :opnameDate AND ITEM_CODE = :itemCode ";

                Map<String, Object> param1 = new HashMap<String, Object>();
                param1.put("opnameNo", balance.get("opnameNo"));
                param1.put("outletCode", balance.get("outletCode"));
                param1.put("opnameDate", balance.get("opnameDate"));
                param1.put("userUpd", balance.get("userUpd"));
                param1.put("itemCode", opn.get("itemCode"));
                param1.put("cdTrans", balance.get("transType") + "-" + balance.get("opnameNo"));
                param1.put("qtyIn", totIn);
                param1.put("qtyOut", totOut);
                param1.put("dateUpd", LocalDateTime.now().format(dateFormatter));
                param1.put("timeUpd", LocalDateTime.now().format(timeFormatter));
                jdbcTemplate.update(qry2, param1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertScDtlToScHdr(Map<String, String> balance) {

        try {
            String qry = "SELECT OUTLET_CODE,TRANS_DATE,ITEM_CODE,CD_TRANS,QUANTITY_IN,QUANTITY"
                    + " FROM T_STOCK_CARD_DETAIL WHERE OUTLET_CODE = :outletCode AND CD_TRANS = 'SOP' AND TRANS_DATE = :opnameDate";
            Map prm = new HashMap();
            prm.put("outletCode", balance.get("outletCode"));
            prm.put("opnameNo", balance.get("opnameNo"));
            prm.put("opnameDate", balance.get("opnameDate"));
            prm.put("transType", balance.get("transType"));
            prm.put("userUpd", balance.get("userUpd"));
            prm.put("dateUpd", LocalDateTime.now().format(dateFormatter));
            prm.put("timeUpd", LocalDateTime.now().format(timeFormatter));
            System.err.println("q1 :" + qry);
            List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rt = new HashMap<String, Object>();
                    rt.put("outletCode", rs.getString("OUTLET_CODE"));
                    rt.put("transDate", rs.getString("TRANS_DATE"));
                    rt.put("itemCode", rs.getString("ITEM_CODE"));
                    rt.put("cdTrans", rs.getString("CD_TRANS"));
                    rt.put("qtyIn", rs.getString("QUANTITY_IN"));
                    rt.put("qtyOut", rs.getString("QUANTITY"));
                    return rt;
                }
            });

            for (Map<String, Object> opn : list) {
                String itemCode = (String) opn.get("itemCode");
                String i = (String) opn.get("qtyIn");
                String o = (String) opn.get("qtyOut");

                double in = Double.valueOf(i);
                double out = Double.valueOf(o);

                String qty_in = cekQtyIn(balance.get("outletCode"), balance.get("opnameDate"), itemCode);
                String qty_out = cekQtyOut(balance.get("outletCode"), balance.get("opnameDate"), itemCode);

                double qtyIn = Double.valueOf(qty_in);
                double qtyOut = Double.valueOf(qty_out);

                double totIn = qtyIn + in;
                double totOut = qtyOut + out;

                String qry2 = "UPDATE T_STOCK_CARD  "
                        + "SET QTY_IN = :qtyIn ,QTY_OUT = :qtyOut ,REMARK = :cdTrans , USER_UPD = :userUpd ,  "
                        + "DATE_UPD = :dateUpd, TIME_UPD = :timeUpd "
                        + "WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :opnameDate AND ITEM_CODE = :itemCode ";

                Map<String, Object> param = new HashMap<String, Object>();
                param.put("opnameNo", balance.get("opnameNo"));
                param.put("outletCode", balance.get("outletCode"));
                param.put("opnameDate", balance.get("opnameDate"));
                param.put("userUpd", balance.get("userUpd"));
                param.put("itemCode", opn.get("itemCode"));
                param.put("cdTrans", balance.get("transType") + balance.get("opnameNo"));
                param.put("qtyIn", totIn);
                param.put("qtyOut", totOut);
                param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
                param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
                jdbcTemplate.update(qry2, param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String cekQtyIn(String outletCode, String opnameDate, String itemCode) {

        String sql = "SELECT QTY_IN FROM T_STOCK_CARD WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :opnameDate AND ITEM_CODE = :itemCode";
        Map param = new HashMap();
        param.put("opnameDate", opnameDate);
        param.put("itemCode", itemCode);
        param.put("outletCode", outletCode);
        return jdbcTemplate.queryForObject(sql, param, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString(1) == null ? "0" : rs.getString(1);

            }
        }).toString();
    }

    public String cekQtyOut(String outletCode, String opnameDate, String itemCode) {

        String sql = "SELECT QTY_OUT FROM T_STOCK_CARD WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :opnameDate AND ITEM_CODE = :itemCode";
        Map param = new HashMap();
        param.put("opnameDate", opnameDate);
        param.put("itemCode", itemCode);
        param.put("outletCode", outletCode);
        return jdbcTemplate.queryForObject(sql, param, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString(1) == null ? "0" : rs.getString(1);

            }
        }).toString();
    }

    // Remove empty qty order entry by Fathur 15 Feb 2024 //
    @Override
    public void removeEmptyOrder(Map<String, String> balance) {
        try {   
            String removeEmptyQtyQuery = "delete T_ORDER_DETAIL tod WHERE tod.ORDER_NO = :orderNo AND qty_1 = 0 AND qty_2 = 0 ";
            jdbcTemplate.update(removeEmptyQtyQuery, balance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void sendDataToWarehouse(Map<String, String> balance) {

        String json = "";
        Gson gson = new Gson();
        Map<String, Object> map1 = new HashMap<>();
        try {
            String qry1 = "SELECT   OUTLET_CODE AS KODE_PEMESAN, "
                    + "         CD_SUPPLIER AS KODE_TUJUAN, "
                    + "         'I' TIPE_PESANAN, "
                    + "         ORDER_NO AS NOMOR_PESANAN, "
                    + "         TO_CHAR (ORDER_DATE, 'DD-MON-YY') AS TGL_PESAN, "
                    + "         TO_CHAR (DT_DUE, 'DD-MON-YY') AS TGL_BRG_DIKIRIM, "
                    + "         TO_CHAR (DT_EXPIRED, 'DD-MON-YY') AS TGL_BATAS_EXP, "
                    + "         CASE WHEN REMARK IS NULL THEN ' ' ELSE REMARK END AS KETERANGAN1, "
                    + "         ' ' AS KETERANGAN2, "
                    + "         USER_UPD AS USER_PROSES_PEMESAN, "
                    + "         TO_CHAR (ORDER_DATE, 'DD-MON-YY') AS TGL_PROSES_PEMESAN, "
                    + "         TIME_UPD AS JAM_PROSES_PEMESAN, "
                    + "         'B' as STATUS_PESANAN, "
                    + "         ' ' as NO_SJ_PENGIRIM, "
                    + "         ' ' as USER_PROSES_PENGIRIM, "
                    + "         '1 JAN 1900' as TGL_PROSES_PENGIRIM, "
                    + "         '0' as JAM_PROSES_PENGIRIM, "
                    + "         'N' as STATUSH "
                    + "  FROM   T_ORDER_HEADER "
                    + " WHERE   ORDER_NO =:orderNo ";
            Map prm = new HashMap();
            prm.put("orderNo", balance.get("orderNo"));
            System.err.println("q1 :" + qry1);
            List<Map<String, Object>> list = jdbcTemplate.query(qry1, prm, new RowMapper<Map<String, Object>>() {
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rh = new HashMap<>();
                    String qry2 = "SELECT d.ITEM_CODE as KODE_BARANG, M.CONV_WAREHOUSE AS KONVERSI, CD_UOM_2 AS SATUAN_KECIL,"
                            + " CD_UOM_1 AS SATUAN_BESAR,QTY_1 AS QTY_PESAN_BESAR, QTY_2 AS QTY_PESAN_KECIL,"
                            + " (TOTAL_QTY_STOCK / m.CONV_STOCK) AS TOTAL_QTY_PESAN,'' AS TOTAL_QTY_KIRIM,UNIT_PRICE AS HARGA_UNIT,'000000' AS TIME_COUNTER,'N' AS SEND_FLAG"
                            + " FROM T_ORDER_DETAIL d"
                            + " LEFT JOIN M_ITEM m ON d.ITEM_CODE = m.ITEM_CODE "
                            + " WHERE ORDER_NO =:orderNo ";
                    System.err.println("q2 :" + qry2);
                    List<Map<String, Object>> list2 = jdbcTemplate.query(qry2, prm, new RowMapper<Map<String, Object>>() {
                        @Override
                        public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                            Map<String, Object> rt = new HashMap<>();
                            rt.put("kodeBarang", rs.getString("KODE_BARANG"));
                            rt.put("konversi", rs.getString("KONVERSI"));
                            rt.put("satuanKecil", rs.getString("SATUAN_KECIL"));
                            rt.put("satuanBesar", rs.getString("SATUAN_BESAR"));
                            rt.put("qtyPesanBesar", rs.getString("QTY_PESAN_BESAR"));
                            rt.put("qtyPesanKecil", rs.getString("QTY_PESAN_KECIL"));
                            rt.put("totalQtyPesan", rs.getString("TOTAL_QTY_PESAN"));
                            rt.put("totalQtyKirim", rs.getString("TOTAL_QTY_KIRIM"));
                            rt.put("hargaUnit", rs.getString("HARGA_UNIT"));
                            rt.put("timeCounter", rs.getString("TIME_COUNTER"));
                            rt.put("sendFlag", rs.getString("SEND_FLAG"));
                            return rt;
                        }
                    });
                    rh.put("itemList", list2);
                    rh.put("kodePemesan", rs.getString("KODE_PEMESAN"));
                    rh.put("kodeTujuan", rs.getString("KODE_TUJUAN"));
                    rh.put("tipePesanan", rs.getString("TIPE_PESANAN"));
                    rh.put("nomorPesanan", rs.getString("NOMOR_PESANAN"));
                    rh.put("tglPesan", rs.getString("TGL_PESAN"));
                    rh.put("tglBrgDikirim", rs.getString("TGL_BRG_DIKIRIM"));
                    rh.put("tglBatasExp", rs.getString("TGL_BATAS_EXP"));
                    rh.put("keterangan1", rs.getString("KETERANGAN1"));
                    rh.put("keterangan2", rs.getString("KETERANGAN2"));
                    rh.put("userProsesPemesan", rs.getString("USER_PROSES_PEMESAN"));
                    rh.put("tglProsesPemesan", rs.getString("TGL_PROSES_PEMESAN"));
                    rh.put("jamProsesPemesan", rs.getString("JAM_PROSES_PEMESAN"));
                    rh.put("statusPesanan", rs.getString("STATUS_PESANAN"));
                    rh.put("noSjPengirim", rs.getString("NO_SJ_PENGIRIM"));
                    rh.put("userProsesPengirim", rs.getString("USER_PROSES_PENGIRIM"));
                    rh.put("tglProsesPengirim", rs.getString("TGL_PROSES_PENGIRIM"));
                    rh.put("jamProsesPengirim", rs.getString("JAM_PROSES_PENGIRIM"));
                    rh.put("statush", rs.getString("STATUSH"));
                    return rh;
                }
            });

            for (Map<String, Object> dtl : list) {
                CloseableHttpClient client = HttpClients.createDefault();
                String url = urlWarehouse + "/insert-order-headerdetail";
                HttpPost post = new HttpPost(url);

                post.setHeader("Accept", "*/*");
                post.setHeader("Content-Type", "application/json");

                Map<String, Object> param = new HashMap<>();
                param.put("kodePemesan", dtl.get("kodePemesan"));
                param.put("kodeTujuan", dtl.get("kodeTujuan"));
                param.put("tipePesanan", dtl.get("tipePesanan"));
                param.put("nomorPesanan", dtl.get("nomorPesanan"));
                param.put("tglBrgDikirim", dtl.get("tglBrgDikirim"));
                param.put("tglBatasExp", dtl.get("tglBatasExp"));
                param.put("keterangan1", dtl.get("keterangan1"));
                param.put("keterangan2", dtl.get("keterangan2"));
                param.put("userProsesPemesan", dtl.get("userProsesPemesan"));
                param.put("tglProsesPemesan", dtl.get("tglProsesPemesan"));
                param.put("statusPesanan", dtl.get("statusPesanan"));
                param.put("noSjPengirim", dtl.get("noSjPengirim"));
                param.put("userProsesPengirim", dtl.get("userProsesPengirim"));
                param.put("tglProsesPengirim", dtl.get("tglProsesPengirim"));
                param.put("jamProsesPengirim", dtl.get("jamProsesPengirim"));
                param.put("statush", dtl.get("statush"));
                //list
                param.put("itemList", dtl.get("itemList"));
                param.put("tglPesan", LocalDateTime.now().format(dateFormatter));
                param.put("jamProsesPemesan", LocalDateTime.now().format(timeFormatter));
                json = new Gson().toJson(param);
                StringEntity entity = new StringEntity(json);
                post.setEntity(entity);
                CloseableHttpResponse response = client.execute(post);
                System.out.println("json" + json);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                (response.getEntity().getContent())
                        )
                );
                StringBuilder content = new StringBuilder();
                String line;
                while (null != (line = br.readLine())) {
                    content.append(line);
                }
                String result = content.toString();
                System.out.println("trans =" + result);
                map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
                }.getType());
            }
            //Add Insert into Table T_HIST (28 Nov 2023)
            Map<String, String> histSend = new HashMap<>();
            histSend.put("orderNo", balance.get("orderNo").toString());
            insertHistSend(histSend);
            //End by Dona
            //Add Insert to HIST_KIRIM by KP (13-06-2023)
            Map<String, String> histKirim = new HashMap<>();
            histKirim.put("orderNo", balance.get("orderNo").toString());
            histKirim.put("sendUser", balance.get("userUpd").toString());
            //End added by KP

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Add Insert to Receiving Header & Detail by KP (07-06-2023)
    @Override
    public void InsertRecvHeaderDetail(JsonObject balancing) {
        Map<String, Object> balance = new HashMap();
        balance.put("outletCode", balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
        LocalDate transDate = this.jdbcTemplate.queryForObject("SELECT TRANS_DATE FROM M_OUTLET WHERE OUTLET_CODE = :outletCode", balance, LocalDate.class);
        String month = String.valueOf(transDate.getMonthValue());
        String year = String.valueOf(transDate.getYear());
        String opNo = opnameNumber(year, month, balancing.getAsJsonObject().getAsJsonPrimitive("transType").getAsString(),
                balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());

        //Header
        String sql = "insert into t_recv_header(OUTLET_CODE, RECV_NO, RECV_DATE, ORDER_NO, REMARK, NO_OF_PRINT, STATUS, USER_UPD, DATE_UPD, TIME_UPD) "
                + "select outlet_code, :recv_no, :recv_date, :ord_no, :doc_no, 0, 1, :usr, sysdate, :time_upd from t_order_header oh "
                + "where oh.order_no = :ord_no and oh.outlet_code = :outlet ";
        Map param = new HashMap();
        param.put("ord_no", balancing.getAsJsonObject().getAsJsonPrimitive("ordNo").getAsString());
        param.put("doc_no", balancing.getAsJsonObject().getAsJsonPrimitive("documentNo").getAsString());
        param.put("outlet", balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
        param.put("recv_no", opNo);
        param.put("recv_date", balancing.getAsJsonObject().getAsJsonPrimitive("recvDate").getAsString());
        param.put("usr", balancing.getAsJsonObject().getAsJsonPrimitive("user").getAsString());
        param.put("time_upd", balancing.getAsJsonObject().getAsJsonPrimitive("timeUpd").getAsString());
        jdbcTemplate.update(sql, param);

        //Detail
        JsonArray emp = balancing.getAsJsonObject().getAsJsonArray("itemList");
        for (int i = 0; i < emp.size(); i++) {
            Map<String, String> detailParam = new HashMap<String, String>();
            detailParam.put("outletCode", balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
            detailParam.put("orderNo", balancing.getAsJsonObject().getAsJsonPrimitive("ordNo").getAsString());
            detailParam.put("recvNo", opNo);
            detailParam.put("itemCode", emp.get(i).getAsJsonObject().getAsJsonPrimitive("itemCode").getAsString());
            detailParam.put("qty1", emp.get(i).getAsJsonObject().getAsJsonPrimitive("qty1").getAsString());
            detailParam.put("cdUom1", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdUom1").getAsString());
            detailParam.put("qty2", emp.get(i).getAsJsonObject().getAsJsonPrimitive("qty2").getAsString());
            detailParam.put("cdUom2", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdUom2").getAsString());
            detailParam.put("qtyBonus", emp.get(i).getAsJsonObject().getAsJsonPrimitive("qtyBonus").getAsString());
            detailParam.put("cdUomBonus", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdUomBonus").getAsString());
            detailParam.put("totalQty", emp.get(i).getAsJsonObject().getAsJsonPrimitive("totalQty").getAsString());
            detailParam.put("totalPrice", emp.get(i).getAsJsonObject().getAsJsonPrimitive("totalPrice").getAsString());
            detailParam.put("UserUpd", balancing.getAsJsonObject().getAsJsonPrimitive("user").getAsString());
            detailParam.put("userUpd", balancing.getAsJsonObject().getAsJsonPrimitive("user").getAsString()); // userUpd using 'u' kecil by Dani 18 Dec 2023
            InsertRecvDetail(detailParam);
            detailParam.clear();
        }

        // call updateMCounterSop to update counter rcv by Dani 20 Desc 2023
        updateMCounterSop(balancing.getAsJsonObject().getAsJsonPrimitive("transType").getAsString(), balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
        // update status t_order_header menjadi close by Dani 29 Dec 2023
        String qu = " UPDATE T_ORDER_HEADER SET STATUS = '1' WHERE ORDER_NO = :ord_no ";
        jdbcTemplate.update(qu, param);
    }

    public void InsertRecvDetail(Map<String, String> balance) {

        String qy = "INSERT INTO t_recv_detail (OUTLET_CODE, ORDER_NO, RECV_NO, ITEM_CODE, QTY_1, CD_UOM_1, QTY_2, CD_UOM_2, QTY_BONUS, CD_UOM_BONUS, TOTAL_QTY, TOTAL_PRICE, USER_UPD, DATE_UPD, TIME_UPD)"
                + " VALUES(:outletCode, :orderNo, :recvNo, :itemCode, :qty1, :cdUom1, :qty2, :cdUom2, :qtyBonus, :cdUomBonus, :totalQty, :totalPrice, :userUpd, :dateUpd, :timeUpd)";
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("orderNo", balance.get("orderNo"));
        param.put("recvNo", balance.get("recvNo"));
        param.put("itemCode", balance.get("itemCode"));
        param.put("qty1", balance.get("qty1"));
        param.put("cdUom1", balance.get("cdUom1"));
        param.put("qty2", balance.get("qty2"));
        param.put("cdUom2", balance.get("cdUom2"));
        param.put("qtyBonus", balance.get("qtyBonus"));
        param.put("cdUomBonus", balance.get("cdUomBonus"));
        param.put("totalQty", balance.get("totalQty"));
        param.put("totalPrice", balance.get("totalPrice"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);

        // call insert to t_stock_card_dtl by Dani 18 December 2023
        updateRecvStockCard(param);
    }

    // update receive stock card for receive stock by Dani 18 December 2023
    // this method is for update table t_stock_card and t_stock_card_detail for receive stock
    public void updateRecvStockCard(Map<String, String> balance) {
        String qf = "SELECT CASE WHEN QUANTITY_IN IS NULL THEN 0 ELSE QUANTITY_IN END AS QUANTITY_IN FROM T_STOCK_CARD_DETAIL WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :transDate AND item_code= :itemCode AND cd_trans='RCV'";
        Map<String, Object> param = new HashMap<>();
        param.put("outletCode", balance.get("outletCode"));
        param.put("itemCode", balance.get("itemCode"));
        param.put("cdTrans", "RCV");
        param.put("qtyIn", balance.get("totalQty"));
        param.put("qty", 0);
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        BigDecimal existQty = null;
        String transDate = this.jdbcTemplate.queryForObject(
                "SELECT DISTINCT TO_CHAR(TRANS_DATE, 'DD-MON-YYYY') FROM M_OUTLET WHERE OUTLET_CODE = :outletCode and status = 'A'",
                param, String.class);
        param.put("transDate", transDate);

        try {
            String qsh = "SELECT CASE WHEN QTY_IN IS NULL THEN 0 ELSE QTY_IN END AS QTY_IN FROM T_STOCK_CARD WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :transDate AND ITEM_CODE = :itemCode ";
            BigDecimal hdrQtyInDb = jdbcTemplate.queryForObject(qsh, param, BigDecimal.class);
            String quh = "UPDATE T_STOCK_CARD SET QTY_IN = :qtyIn WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :transDate AND ITEM_CODE = :itemCode ";
            hdrQtyInDb = hdrQtyInDb.add(new BigDecimal(balance.get("totalQty")));
            param.put("qtyIn", hdrQtyInDb);
            jdbcTemplate.update(quh, param);
        } catch (EmptyResultDataAccessException exx) {
            String qih = "INSERT INTO T_STOCK_CARD (OUTLET_CODE,TRANS_DATE,ITEM_CODE,ITEM_COST,QTY_BEGINNING,QTY_IN,QTY_OUT,USER_UPD,DATE_UPD,TIME_UPD) VALUES (:outletCode,:transDate,:itemCode,0,0,:qtyIn,0,:userUpd,:dateUpd,:timeUpd) ";
            param.put("qtyIn", new BigDecimal(balance.get("totalQty")));
            jdbcTemplate.update(qih, param);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            existQty = jdbcTemplate.queryForObject(qf, param, BigDecimal.class);
            BigDecimal quantityIn = new BigDecimal(balance.get("totalQty"));
            quantityIn = quantityIn.add(existQty);
            param.put("quantityIn", quantityIn);
            String qu = "UPDATE T_STOCK_CARD_DETAIL"
                    + " SET QUANTITY_IN=:quantityIn, QUANTITY=:qty, USER_UPD=:userUpd, DATE_UPD=:dateUpd, TIME_UPD=:timeUpd "
                    + " WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :transDate AND item_code= :itemCode AND cd_trans='RCV' ";
            jdbcTemplate.update(qu, param);
        } catch (EmptyResultDataAccessException exx) {
            String qi = "INSERT INTO T_STOCK_CARD_DETAIL (OUTLET_CODE,TRANS_DATE,ITEM_CODE,CD_TRANS,QUANTITY_IN,QUANTITY,USER_UPD,DATE_UPD,TIME_UPD) VALUES (:outletCode, :transDate, :itemCode, :cdTrans , :qtyIn, :qty, :userUpd, :dateUpd, :timeUpd)";
            jdbcTemplate.update(qi, param);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Add Insert to HIST_KIRIM by KP (13-06-2023)
    public void InsertHistKirim(Map<String, String> balance) {
        String qy = "INSERT INTO t_recv_detail (OUTLET_CODE, TUJUAN_KIRIM, NO_ORDER, STATUS_KIRIM, TGL_KIRIM, JAM_KIRIM, USER_KIRIM) "
                + "SELECT OUTLET_CODE, CD_SUPPLIER, ORDER_NO, 'S', :sendDate, :sendHour, :sendUser "
                + "FROM T_ORDER_HEADER "
                + "WHERE ORDER_NO = :orderNo";
        Map param = new HashMap();
        param.put("orderNo", balance.get("orderNo"));
        param.put("sendDate", LocalDateTime.now().format(dateFormatter));
        param.put("sendHour", LocalDateTime.now().format(timeFormatter));
        param.put("sendUser", balance.get("sendUser"));
        jdbcTemplate.update(qy, param);

    }
    //End added by KP

    // New Method Insert HIST KIRIM 28 NOV 2023/////////
    public void insertHistSend(Map<String, String> balance) {
        String qy = " INSERT INTO HIST_KIRIM (OUTLET_CODE,TUJUAN_KIRIM,NO_ORDER,STATUS_KIRIM,TGL_KIRIM,JAM_KIRIM,USER_KIRIM)"
                + " select outlet_code,cd_supplier as tujuan_kirim,order_no as"
                + "      No_order,'S' as status_kirim,:sendDate,:sendHour,"
                + "      user_upd as user_kirim"
                + "      from t_order_header "
                + "      where order_no=:orderNo";
        Map param = new HashMap();
        param.put("orderNo", balance.get("orderNo"));
        param.put("sendDate", LocalDateTime.now().format(dateFormatter));
        param.put("sendHour", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
        System.err.println("qy :" + qy);
    }
    //End

    //Add Insert to Wastage Header & Detail by KP (03-08-2023)
    public String wastageCounter(String year, String month, String transType, String outletCode) {
        if (transType.equalsIgnoreCase("ID")) {
            String dateMonth = month.concat("-").concat(year);
            String outletCodeQuery = outletCode;
            if (outletCodeQuery.charAt(0) == '0') {
                outletCodeQuery = outletCodeQuery.substring(1);
            }
            String sqlId = "select to_char(nvl(max(substr(wastage_id, -3)) + 1, 1), 'fm000') as no_urut "
                    + "from t_wastage_header "
                    + "where wastage_id like '" + outletCodeQuery.concat("0").concat(month) + "%' "
                    + "and to_char(wastage_date,'mm-yyyy') = :dateMonth";
            System.err.println("Query for Id :" + sqlId);
            Map paramId = new HashMap();
            paramId.put("dateMonth", dateMonth);
            return jdbcTemplate.queryForObject(sqlId, paramId, new RowMapper() {
                @Override
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    return rs.getString("no_urut") == null ? outletCode.concat("0").concat(month).concat("0001") : outletCode.concat("0").concat(month).concat(rs.getString("no_urut"));
                }
            }).toString();
        }
        String sql = "select to_char(counter, 'fm0000') as no_urut from ( "
                + "select max(counter_no) + 1 as counter from m_counter "
                + "where outlet_code = :outletCode and trans_type = :transType and year = :year and month = :month "
                + ") tbl";
        System.err.println("Query for No Urut :" + sql);
        Map param = new HashMap();
        param.put("year", year);
        param.put("month", month);
        param.put("transType", transType);
        param.put("outletCode", outletCode);
        String noUrut = jdbcTemplate.queryForObject(sql, param, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("no_urut") == null ? "0" : rs.getString("no_urut");

            }
        }).toString();
        if (noUrut.equalsIgnoreCase("0")) {
            sql = "insert into m_counter(outlet_code, trans_type, year, month, counter_no) "
                    + "values (:outletCode, :transType, :year, :month, 1)";
            System.err.println("Query for NEW No Urut :" + sql);
            Map paramIns = new HashMap();
            paramIns.put("year", year);
            paramIns.put("month", month);
            paramIns.put("transType", transType);
            paramIns.put("outletCode", outletCode);
            jdbcTemplate.update(sql, paramIns);
            noUrut = transType.concat(outletCode).concat(year.substring(2)).concat(month).concat("0001");
        } else {
            sql = "update m_counter set counter_no = counter_no + 1 "
                    + "where outlet_code = :outletCode and trans_type = :transType and year = :year and month = :month";
            System.err.println("Query for Update Counter :" + sql);
            Map paramIns = new HashMap();
            paramIns.put("year", year);
            paramIns.put("month", month);
            paramIns.put("transType", transType);
            paramIns.put("outletCode", outletCode);
            jdbcTemplate.update(sql, paramIns);
            noUrut = transType.concat(outletCode).concat(year.substring(2)).concat(month).concat(noUrut);
        }
        System.err.println("No Urut :" + noUrut);
        return noUrut;
    }

    public void InsertWastageDetail(Map<String, String> balance) {
        String qy = "INSERT INTO t_wastage_detail (OUTLET_CODE, WASTAGE_ID, WASTAGE_NO, ITEM_CODE, QUANTITY, UOM_STOCK, ITEM_TO, USER_UPD, DATE_UPD, TIME_UPD)"
                + " VALUES(:outletCode, :wastageId, :wastageNo, :itemCode, :qty, :uom, :itemTo, :userUpd, :dateUpd, :timeUpd)";
        System.err.println("q detail:" + qy);
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("wastageId", balance.get("wastageId"));
        param.put("wastageNo", balance.get("wastageNo"));
        param.put("itemCode", balance.get("itemCode"));
        param.put("qty", balance.get("qty"));
        param.put("uom", balance.get("uom"));
        param.put("itemTo", balance.get("itemTo"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
    }

    @Override
    public void InsertWastageHeaderDetail(JsonObject balancing) {
        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);

        //Getting last number for Wastage/Leftover
        String transType = balancing.getAsJsonObject().getAsJsonPrimitive("transType").getAsString();
        if (transType.contains("W")) {
            transType = "WST";
        } else {
            transType = "LOV";
        }
        String opNo = wastageCounter(year, month, transType,
                balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
        String opId = wastageCounter(year, month, "ID",
                balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());

        //Header
        String sql = "insert into t_wastage_header(OUTLET_CODE, TYPE_TRANS, WASTAGE_ID, WASTAGE_NO, WASTAGE_DATE, REMARK, STATUS, USER_UPD, DATE_UPD, TIME_UPD) "
                + "values (:outletCode, :transType, :wastageId, :wastageNo, :wastageDate, :remark, :status, :userUpd, :dateUpd, :timeUpd)";
        System.err.println("q header :" + sql);
        Map param = new HashMap();
        param.put("outletCode", balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
        param.put("transType", balancing.getAsJsonObject().getAsJsonPrimitive("transType").getAsString());
        param.put("wastageId", opId);
        param.put("wastageNo", opNo);
        param.put("wastageDate", balancing.getAsJsonObject().getAsJsonPrimitive("wastageDate").getAsString());
        param.put("remark", balancing.getAsJsonObject().getAsJsonPrimitive("remark").getAsString());
        param.put("status", balancing.getAsJsonObject().getAsJsonPrimitive("status").getAsString());
        param.put("userUpd", balancing.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString());
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(sql, param);

        //Detail
        JsonArray emp = balancing.getAsJsonObject().getAsJsonArray("itemList");
        for (int i = 0; i < emp.size(); i++) {
            Map<String, String> detailParam = new HashMap<String, String>();
            detailParam.put("outletCode", balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
            detailParam.put("wastageId", opId);
            detailParam.put("wastageNo", opNo);
            detailParam.put("itemCode", emp.get(i).getAsJsonObject().getAsJsonPrimitive("itemCode").getAsString());
            detailParam.put("qty", emp.get(i).getAsJsonObject().getAsJsonPrimitive("qty").getAsString());
            detailParam.put("uom", emp.get(i).getAsJsonObject().getAsJsonPrimitive("uom").getAsString());
            detailParam.put("itemTo", emp.get(i).getAsJsonObject().getAsJsonPrimitive("itemTo").getAsString());
            detailParam.put("userUpd", balancing.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString());
            InsertWastageDetail(detailParam);
            detailParam.clear();
        }

        itemWastageToStockCard(balancing, opNo);
    }
    //End added by KP

    public void itemWastageToStockCard(JsonObject balancing, String wastageNo) {
        Map param = new HashMap();
        param.put("wastageNo", wastageNo);
        param.put("userUpd", balancing.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString());
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));

        if (balancing.getAsJsonObject().getAsJsonPrimitive("transType").getAsString().contains("W")) {
            try {
                String queryInsetStockCardDetail = "insert into t_stock_card_detail(SELECT a.OUTLET_CODE, (SELECT TRANS_DATE FROM M_OUTLET WHERE OUTLET_CODE = a.OUTLET_CODE) AS TRANS_DATE, a.ITEM_CODE, 'WST' AS CD_TRANS, 0 AS QUANTITY_IN, a.QUANTITY AS QUANTITY, :userUpd AS USER_UPD, :dateUpd AS DATE_UPD, :timeUpd AS TIME_UPD FROM T_WASTAGE_DETAIL a WHERE a.WASTAGE_NO = :wastageNo)";
                jdbcTemplate.update(queryInsetStockCardDetail, param);
            } catch (Exception ex) {
                String queryInsetStockCardDetail = "UPDATE T_STOCK_CARD_DETAIL a SET a.QUANTITY = a.QUANTITY + NVL((SELECT b.QUANTITY FROM T_WASTAGE_DETAIL b WHERE b.WASTAGE_NO =:wastageNo AND b.ITEM_CODE = a.ITEM_CODE), 0) WHERE a.TRANS_DATE = (SELECT TRANS_DATE FROM M_OUTLET WHERE OUTLET_CODE = a.OUTLET_CODE) AND a.CD_TRANS = 'WST'";
                jdbcTemplate.update(queryInsetStockCardDetail, param);
            }

            String queryUpdateStockCard = "UPDATE T_STOCK_CARD a SET a.QTY_OUT = a.QTY_OUT + NVL((SELECT b.QUANTITY FROM T_STOCK_CARD_DETAIL b WHERE b.ITEM_CODE = a.ITEM_CODE AND a.TRANS_DATE = b.TRANS_DATE AND b.CD_TRANS = 'WST'), 0), a.REMARK = :wastageNo WHERE a.TRANS_DATE = (SELECT TRANS_DATE FROM M_OUTLET WHERE OUTLET_CODE = a.OUTLET_CODE)";
            jdbcTemplate.update(queryUpdateStockCard, param);
        } else {
            try {
                String queryInsetStockCardDetail = "insert into t_stock_card_detail(SELECT OUTLET_CODE, TRANS_DATE, ITEM_CODE, CD_TRANS, SUM(QUANTITY_IN) AS QUANTITY_IN, SUM(QUANTITY) AS QUANTITY, USER_UPD, DATE_UPD, TIME_UPD FROM (SELECT a.OUTLET_CODE, (SELECT TRANS_DATE FROM M_OUTLET WHERE OUTLET_CODE = a.OUTLET_CODE) AS TRANS_DATE, a.ITEM_CODE, 'LOV' AS CD_TRANS, 0 AS QUANTITY_IN, a.QUANTITY AS QUANTITY, :userUpd AS USER_UPD, :dateUpd AS DATE_UPD, :timeUpd AS TIME_UPD FROM T_WASTAGE_DETAIL a WHERE a.WASTAGE_NO =:wastageNo UNION ALL SELECT a.OUTLET_CODE, (SELECT TRANS_DATE FROM M_OUTLET WHERE OUTLET_CODE = a.OUTLET_CODE) AS TRANS_DATE, a.ITEM_TO AS ITEM_CODE, 'LOV' AS CD_TRANS, a.QUANTITY AS QUANTITY_IN, 0 AS QUANTITY, :userUpd AS USER_UPD, :dateUpd AS DATE_UPD, :timeUpd AS TIME_UPD FROM T_WASTAGE_DETAIL a WHERE a.WASTAGE_NO = :wastageNo) GROUP BY ITEM_CODE, OUTLET_CODE, TRANS_DATE, CD_TRANS, USER_UPD, DATE_UPD, TIME_UPD)";
                jdbcTemplate.update(queryInsetStockCardDetail, param);
            } catch (Exception ex) {
                String queryInsetStockCardDetail = "UPDATE T_STOCK_CARD_DETAIL a SET a.QUANTITY_IN = a.QUANTITY_IN + NVL((SELECT SUM(b.QUANTITY) FROM T_WASTAGE_DETAIL b WHERE b.WASTAGE_NO = :wastageNo AND b.ITEM_TO = a.ITEM_CODE), 0), a.QUANTITY = a.QUANTITY + NVL((SELECT SUM(b.QUANTITY) FROM T_WASTAGE_DETAIL b WHERE b.WASTAGE_NO = :wastageNo AND b.ITEM_CODE = a.ITEM_CODE), 0) WHERE a.TRANS_DATE = (SELECT TRANS_DATE FROM M_OUTLET WHERE OUTLET_CODE = a.OUTLET_CODE) AND a.CD_TRANS = 'LOV'";
                jdbcTemplate.update(queryInsetStockCardDetail, param);
            }
            String queryUpdateStockCard = "UPDATE T_STOCK_CARD a SET a.QTY_IN = a.QTY_IN + NVL((SELECT b.QUANTITY_IN FROM T_STOCK_CARD_DETAIL b WHERE b.ITEM_CODE = a.ITEM_CODE AND a.TRANS_DATE = b.TRANS_DATE AND b.CD_TRANS = 'LOV'), 0), a.QTY_OUT = a.QTY_OUT + NVL((SELECT b.QUANTITY FROM T_STOCK_CARD_DETAIL b WHERE b.ITEM_CODE = a.ITEM_CODE AND a.TRANS_DATE = b.TRANS_DATE AND b.CD_TRANS = 'LOV'), 0), a.REMARK = :wastageNo WHERE a.TRANS_DATE = (SELECT TRANS_DATE FROM M_OUTLET WHERE OUTLET_CODE = a.OUTLET_CODE)";
            jdbcTemplate.update(queryUpdateStockCard, param);
        }
    }

    //Insert MPCS by Kevin (08-08-2023)
    @Override
    public void InsertMPCSTemplate(JsonObject balancing) {
        //Delete existing
        String outletCode = balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString();
        String userUpd = balancing.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString();
        Integer interval = balancing.getAsJsonObject().getAsJsonPrimitive("interval").getAsInt();
        String startTime = balancing.getAsJsonObject().getAsJsonPrimitive("startTime").getAsString();
        String endTime = balancing.getAsJsonObject().getAsJsonPrimitive("endTime").getAsString();
        String sqlDel = "delete from template_mpcs where outlet_code = :outletCode ";
        Map param = new HashMap();
        param.put("outletCode", outletCode);
        jdbcTemplate.update(sqlDel, param);

        // improvement query insert by M Joko 5 Feb 2024
        Timestamp startTimestamp = Timestamp.valueOf("1970-01-01 " + startTime + ":00");
        Timestamp endTimestamp = Timestamp.valueOf("1970-01-01 " + endTime + ":00");
        long sequence = 1;
        while (startTimestamp.compareTo(endTimestamp) <= 0) {
            String sqlInsert = "INSERT INTO TEMPLATE_MPCS (OUTLET_CODE, SEQ_MPCS, TIME_MPCS, USER_UPD, DATE_UPD, TIME_UPD) "
                    + "VALUES (:outletCode, :seqMpcs, :timeMpcs, :userUpd, :dateUpd, :timeUpd)";
            Long currentTimeMillis = System.currentTimeMillis();
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("outletCode", outletCode);
            params.addValue("seqMpcs", sequence);
            params.addValue("timeMpcs", String.format("%tH%tM%tS", startTimestamp, startTimestamp,startTimestamp).substring(0,6));
            params.addValue("userUpd", userUpd);
            params.addValue("dateUpd", String.format("%td %tb %tY", currentTimeMillis, currentTimeMillis, currentTimeMillis));
            params.addValue("timeUpd", String.format("%tH%tM%S", currentTimeMillis, currentTimeMillis, currentTimeMillis).substring(0,6));
            jdbcTemplate.update(sqlInsert, params);
            sequence++;
            startTimestamp.setTime(startTimestamp.getTime() + (interval * 60 * 1000));
        }
    }

    public String mpcsExist(Map<String, String> ref) {
        String qry = "select count(1) as existRow from t_summ_mpcs "
                + "where outlet_code = :outletCode "
                + "and mpcs_group = :mpcsGrp "
                + "and date_mpcs = :dateMpcs "
                + "and time_mpcs = :timeMpcs ";
        Map prm = new HashMap();
        prm.put("outletCode", ref.get("outlet"));
        prm.put("mpcsGrp", ref.get("mpcsGrp"));
        prm.put("dateMpcs", ref.get("dateMpcs"));
        prm.put("timeMpcs", ref.get("timeMpcs"));
        System.err.println("q :" + qry);
        return jdbcTemplate.queryForObject(qry, prm, new RowMapper() {
            @Override
            public String mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                return rs.getString("existRow");
            }
        }).toString();
    }

    @Override
    public void InsertUpdateMPCSProject(JsonObject balancing) {
        String sql = "";
        Map param = new HashMap();
        param.put("outlet", balancing.getAsJsonObject().getAsJsonPrimitive("outlet").getAsString());
        param.put("mpcsGrp", balancing.getAsJsonObject().getAsJsonPrimitive("mpcsGrp").getAsString());
        param.put("dateMpcs", balancing.getAsJsonObject().getAsJsonPrimitive("dateMpcs").getAsString());
        param.put("timeMpcs", balancing.getAsJsonObject().getAsJsonPrimitive("timeMpcs").getAsString());
        String isMpcsExist = mpcsExist(param);
        param.clear();
        if (isMpcsExist.equalsIgnoreCase("0")) {
            sql = "insert into t_summ_mpcs(outlet_code, mpcs_group, date_mpcs, seq_mpcs, "
                    + "time_mpcs, qty_proj_conv,  qty_proj, user_upd, date_upd, time_upd) "
                    + "values (:outletCode, :mpcsGrp, :dateMpcs, :seqMpcs, :timeMpcs, :qtyPr, :qtyPr, :userUpd, :dateUpd, :timeUpd) ";
            System.err.println("q :" + sql);
            param.put("outletCode", balancing.getAsJsonObject().getAsJsonPrimitive("outlet").getAsString());
            param.put("mpcsGrp", balancing.getAsJsonObject().getAsJsonPrimitive("mpcsGrp").getAsString());
            param.put("dateMpcs", balancing.getAsJsonObject().getAsJsonPrimitive("dateMpcs").getAsString());
            param.put("seqMpcs", balancing.getAsJsonObject().getAsJsonPrimitive("seqMpcs").getAsString());
            param.put("timeMpcs", balancing.getAsJsonObject().getAsJsonPrimitive("timeMpcs").getAsString());
            param.put("qtyPr", balancing.getAsJsonObject().getAsJsonPrimitive("qtyPr").getAsString());
            param.put("userUpd", balancing.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString());
            param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
            param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
            jdbcTemplate.update(sql, param);
        } else {
            sql = "update t_summ_mpcs "
                    + "set qty_proj_conv = :qtyPr, qty_proj = :qtyPr "
                    + "where outlet_code = :outletCode "
                    + "and mpcs_group = :mpcsGrp "
                    + "and date_mpcs = :dateMpcs "
                    + "and time_mpcs = :timeMpcs ";
            System.err.println("q :" + sql);
            param.put("outletCode", balancing.getAsJsonObject().getAsJsonPrimitive("outlet").getAsString());
            param.put("mpcsGrp", balancing.getAsJsonObject().getAsJsonPrimitive("mpcsGrp").getAsString());
            param.put("dateMpcs", balancing.getAsJsonObject().getAsJsonPrimitive("dateMpcs").getAsString());
            param.put("timeMpcs", balancing.getAsJsonObject().getAsJsonPrimitive("timeMpcs").getAsString());
            param.put("qtyPr", balancing.getAsJsonObject().getAsJsonPrimitive("qtyPr").getAsString());
            jdbcTemplate.update(sql, param);
        }
    }
    //End of MPCS

    @Override
    public void insertReturnOrderHeaderDetail(JsonObject param) {
        String outletCode = param.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString();
        Map balance = new HashMap();
        balance.put("outletCode", outletCode);
        LocalDate transDate = this.jdbcTemplate.queryForObject("SELECT TRANS_DATE FROM M_OUTLET WHERE OUTLET_CODE = :outletCode", balance, LocalDate.class);
//        String month = String.valueOf(transDate.getMonthValue());
        String month = String.format("%02d", transDate.getMonthValue());
        System.out.println(month);
        String year = String.valueOf(transDate.getYear());

        String typeReturn = param.getAsJsonPrimitive("typeReturn").getAsString();

        //Getting last number for Return Order
        String noID = returnOrderCounter(year, month, "ID", param.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
        String noReturn = returnOrderCounter(year, month, "RTR", param.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());

        //Insert Header
        String queryHeader = "INSERT INTO T_RETURN_HEADER (OUTLET_CODE, TYPE_RETURN, RETURN_ID, RETURN_NO, RETURN_DATE,"
                + " RETURN_TO, REMARK, STATUS, USER_UPD, DATE_UPD, TIME_UPD) VALUES (:outletCode, :typeReturn, :returnId,"
                + " :returnNo, :returnDate, :returnTo, :remark, :status, :userUpd, :dateUpd, :timeUpd)";

        Map<String, Object> prm = new HashMap<>();
        prm.put("outletCode", outletCode);
        prm.put("typeReturn", param.getAsJsonObject().getAsJsonPrimitive("typeReturn").getAsString());
        prm.put("returnId", noID);
        prm.put("returnNo", noReturn);
        prm.put("returnDate", param.getAsJsonObject().getAsJsonPrimitive("returnDate").getAsString());
        prm.put("returnTo", param.getAsJsonObject().getAsJsonPrimitive("returnTo").getAsString());
        prm.put("remark", param.getAsJsonObject().getAsJsonPrimitive("remark").getAsString());
        prm.put("status", typeReturn.equalsIgnoreCase("1") ? 0 : 1);
        prm.put("userUpd", param.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString());
        prm.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        prm.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(queryHeader, prm);

        //Insert Detail
        ObjectMapper objectMapper = new ObjectMapper();
        JsonArray emp = param.getAsJsonObject().getAsJsonArray("itemList");
        StringBuilder query = new StringBuilder();
        String userUpd = param.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString();
        query.append("INSERT ALL");

        for (int i = 0; i < emp.size(); i++) {
            query.append(" INTO T_RETURN_DETAIL (OUTLET_CODE, RETURN_ID, RETURN_NO, ITEM_CODE, QTY_WAREHOUSE, "
                    + "UOM_WAREHOUSE, QTY_PURCHASE, UOM_PURCHASE, TOTAL_QTY, USER_UPD, DATE_UPD, TIME_UPD) VALUES ('")
                    .append(outletCode).append("', '").append(noID).append("', '").append(noReturn)
                    .append("', '").append(emp.get(i).getAsJsonObject().getAsJsonPrimitive("itemCode").getAsString())
                    .append("', '").append(emp.get(i).getAsJsonObject().getAsJsonPrimitive("qtyWarehouse").getAsString())
                    .append("', '").append(emp.get(i).getAsJsonObject().getAsJsonPrimitive("uomWarehouse").getAsString())
                    .append("', '").append(emp.get(i).getAsJsonObject().getAsJsonPrimitive("qtyPurchase").getAsString())
                    .append("', '").append(emp.get(i).getAsJsonObject().getAsJsonPrimitive("uomPurchase").getAsString())
                    .append("', '").append(emp.get(i).getAsJsonObject().getAsJsonPrimitive("totalQty").getAsString())
                    .append("', '").append(userUpd).append("', '").append(LocalDateTime.now().format(dateFormatter)).append("', '")
                    .append(LocalDateTime.now().format(timeFormatter)).append("')");
        }
        query.append(" SELECT * FROM dual");
        String queryDetail = query.toString();
        jdbcTemplate.update(queryDetail, new HashMap<>());
        
        if(!typeReturn.equals("1")){
            updateStockCardRO(noReturn, outletCode);
        }
    }

    // kirim return order ke api warehouse by M Joko - 22-12-23
    public void updateStockCardRO(String noReturn, String outletCode) {
        String qryRO = "SELECT h.type_return, h.return_to, h.remark, TO_CHAR(h.return_date, 'DD-MON-YY') AS return_date, d.outlet_code, d.return_id, d.return_no, d.item_code, d.qty_warehouse, d.uom_warehouse, d.qty_purchase, d.uom_purchase, d.total_qty, d.user_upd, TO_CHAR(d.date_upd, 'DD-MON-YY') AS date_upd, d.time_upd FROM t_return_detail d JOIN t_return_header h ON d.return_no = h.return_no AND d.outlet_code = h.outlet_code WHERE d.return_no = :returnNo AND d.outlet_code = :outletCode";
        Map<String, Object> prmRO = new HashMap<>();
        prmRO.put("returnNo", noReturn);
        prmRO.put("outletCode", outletCode);
        List<Map<String, Object>> listDetailRO = jdbcTemplate.query(qryRO, prmRO, new DynamicRowMapper());
        for (int i = 0; i < listDetailRO.size(); i++) {
            //insert t_stock_card_detail by M Joko - 22-12-23
            Map<String, Object> detailRO = listDetailRO.get(i);
            String qryInsert = "MERGE INTO t_stock_card_detail dst USING ( SELECT :outletCode AS outlet_code, (SELECT trans_date FROM m_outlet WHERE outlet_code = :outletCode) AS trans_date, :itemCode AS item_code, 'RTR' AS cd_trans, 0 AS quantity_in, :qtyOut AS quantity, :userUpd AS user_upd, SYSDATE AS date_upd, TO_CHAR(SYSDATE, 'HH24MISS') AS time_upd FROM dual ) src ON ( dst.outlet_code = src.outlet_code AND dst.cd_trans = src.cd_trans AND dst.trans_date = src.trans_date AND dst.item_code = src.item_code ) WHEN MATCHED THEN UPDATE SET dst.quantity = dst.quantity + src.quantity, dst.user_upd = src.user_upd, dst.date_upd = src.date_upd, dst.time_upd = src.time_upd WHEN NOT MATCHED THEN INSERT ( outlet_code, trans_date, item_code, cd_trans, quantity_in, quantity, user_upd, date_upd, time_upd ) VALUES ( src.outlet_code, src.trans_date, src.item_code, src.cd_trans, src.quantity_in, src.quantity, src.user_upd, src.date_upd, src.time_upd )";
            Map<String, Object> prm = new HashMap<>();
            prm.put("outletCode", detailRO.get("outletCode"));
            prm.put("itemCode", detailRO.get("itemCode"));
            prm.put("qtyOut", detailRO.get("totalQty"));
            prm.put("userUpd", detailRO.get("userUpd"));
            System.err.println("qryInsert: " + qryInsert);
            System.err.println("detailRO: " + detailRO);
            jdbcTemplate.update(qryInsert, prm);

            //update t_stock_card by M Joko - 22-12-23
            String qryUpdateStockCard = "MERGE INTO t_stock_card tgt USING ( SELECT :outletCode AS OUTLET_CODE, (SELECT trans_date FROM m_outlet WHERE outlet_code = :outletCode) AS TRANS_DATE, :itemCode AS ITEM_CODE, NVL(:qtyOut, 0) AS QTY_OUT FROM dual ) src ON (tgt.OUTLET_CODE = src.OUTLET_CODE AND tgt.TRANS_DATE = src.TRANS_DATE AND tgt.ITEM_CODE = src.ITEM_CODE) WHEN MATCHED THEN UPDATE SET tgt.QTY_OUT = tgt.QTY_OUT + src.QTY_OUT WHEN NOT MATCHED THEN INSERT ( OUTLET_CODE, TRANS_DATE, ITEM_CODE, QTY_OUT, ITEM_COST, QTY_BEGINNING, QTY_IN, REMARK, USER_UPD, DATE_UPD, TIME_UPD ) VALUES ( src.OUTLET_CODE, src.TRANS_DATE, src.ITEM_CODE, src.QTY_OUT, 0, 0, 0, 'RTR', :userUpd, SYSDATE , TO_CHAR(SYSDATE, 'HH24MISS') )";
            System.err.println("qryUpdateStockCard: " + qryInsert);
            jdbcTemplate.update(qryUpdateStockCard, prm);
        }
    }

    // kirim ulang return order
    @Override
    public boolean sendReturnOrderToWH(JsonObject balance) {
        String outletCode = balance.getAsJsonPrimitive("outletCode").getAsString();
        String returnNo = balance.getAsJsonPrimitive("returnNo").getAsString();
        String userUpd = balance.getAsJsonPrimitive("userUpd").getAsString();

        // ambil detail return no
        String qryRO = "SELECT h.type_return, h.return_to, h.remark, TO_CHAR(h.return_date, 'DD-MON-YY') AS return_date, d.outlet_code, d.return_id, d.return_no, d.item_code, d.qty_warehouse, d.uom_warehouse, d.qty_purchase, d.uom_purchase, d.total_qty, d.user_upd, TO_CHAR(d.date_upd, 'DD-MON-YY') AS date_upd, d.time_upd FROM t_return_detail d JOIN t_return_header h ON d.return_no = h.return_no AND d.outlet_code = h.outlet_code WHERE d.return_no = :returnNo AND d.outlet_code = :outletCode";
        Map<String, Object> prmRO = new HashMap<>();
        prmRO.put("returnNo", returnNo);
        prmRO.put("outletCode", outletCode);
        prmRO.put("userUpd", userUpd);
        List<Map<String, Object>> listDetailRO = jdbcTemplate.query(qryRO, prmRO, new DynamicRowMapper());
        System.err.println("listDetailRO to wh : " + listDetailRO);

        if (sendReturnOrderDetailToWH(listDetailRO, returnNo, outletCode)) {
            String qryUpdStatus = "update t_return_header set status = 1 where return_no = :returnNo and outlet_code = :outletCode";
            try {
                jdbcTemplate.update(qryUpdStatus, prmRO);
                return true;
            } catch (DataAccessException e) {
                System.err.println("qryUpdStatus error: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    // kirim return order ke api warehouse by M Joko - 22-12-23
    public boolean sendReturnOrderDetailToWH(List<Map<String, Object>> listDetailRO, String returnNo, String outletCode) {
        try {
            String url = urlWarehouse + "/insert-return-order-wh";
            RestApiUtil restApiUtil = new RestApiUtil();
            System.out.println("sendReturnOrderDetailToWH URL: " + url);
            Gson gson = new Gson();
            System.out.println("sendReturnOrderDetailToWH listDetailRO: " + listDetailRO);
            System.out.println("sendReturnOrderDetailToWH json RO: " + gson.toJson(listDetailRO));
            ResponseEntity<String> responseEntity = restApiUtil.post(url, listDetailRO, null);
            String responseBody = responseEntity.getBody();
            System.out.println("sendReturnOrderDetailToWH Response: " + responseBody);
            // update stock card by M Joko - 22-12-23
            updateStockCardRO(returnNo, outletCode);
            return true;
        } catch (Exception e) {
            System.out.println("sendReturnOrderDetailToWH error: " + e.getMessage());
            return false;
        }
    }

    public String returnOrderCounter(String year, String month, String transType, String outletCode) {
        if (transType.equalsIgnoreCase("ID")) {
            String dateMonth = month.concat("-").concat(year);
            String outletCodeQuery = outletCode;
            if (outletCodeQuery.charAt(0) == '0') {
                outletCodeQuery = outletCodeQuery.substring(1);
            }
            String sqlId = "select to_char(nvl(max(substr(RETURN_ID, -3)) + 1, 1), 'fm000') as no_urut "
                    + "from T_RETURN_HEADER "
                    + "where RETURN_ID like '" + outletCodeQuery.concat("0").concat(month) + "%' "
                    + "and to_char(RETURN_DATE,'mm-yyyy') = :dateMonth";
            System.err.println("Query for Id :" + sqlId);
            Map paramId = new HashMap();
            paramId.put("dateMonth", dateMonth);
            return jdbcTemplate.queryForObject(sqlId, paramId, new RowMapper() {
                @Override
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    return rs.getString("no_urut") == null ? outletCode.concat("0").concat(month).concat("0001") : outletCode.concat("0").concat(month).concat(rs.getString("no_urut"));
                }
            }).toString();

        }
        String sql = "select to_char(counter, 'fm0000') as no_urut from ( "
                + "select max(counter_no) + 1 as counter from m_counter "
                + "where outlet_code = :outletCode and trans_type = :transType and year = :year and month = to_number(:month) "
                + ") tbl";
        System.err.println("Query for No Urut :" + sql);
        Map param = new HashMap();
        param.put("year", year);
        param.put("month", month);
        param.put("transType", transType);
        param.put("outletCode", outletCode);
        String noUrut = jdbcTemplate.queryForObject(sql, param, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("no_urut") == null ? "0" : rs.getString("no_urut");

            }
        }).toString();
        if (noUrut.equalsIgnoreCase("0")) {
            sql = "insert into m_counter(outlet_code, trans_type, year, month, counter_no) "
                    + "values (:outletCode, :transType, :year, :month, 1)";
            System.err.println("Query for NEW No Urut :" + sql);
            Map paramIns = new HashMap();
            paramIns.put("year", year);
            paramIns.put("month", month);
            paramIns.put("transType", transType);
            paramIns.put("outletCode", outletCode);
            jdbcTemplate.update(sql, paramIns);
            noUrut = transType.concat(outletCode).concat(year.substring(2)).concat(month).concat("0001");
        } else {
            sql = "update m_counter set counter_no = counter_no + 1 "
                    + "where outlet_code = :outletCode and trans_type = :transType and year = :year and month = :month";
            System.err.println("Query for Update Counter :" + sql);
            Map paramIns = new HashMap();
            paramIns.put("year", year);
            paramIns.put("month", month);
            paramIns.put("transType", transType);
            paramIns.put("outletCode", outletCode);
            jdbcTemplate.update(sql, paramIns);
            noUrut = transType.concat(outletCode).concat(year.substring(2)).concat(month).concat(noUrut);
        }
        System.err.println("No Urut :" + noUrut);
        return noUrut;
    }
///////////////NEW METHOD LIST ORDER HEADER BY DONA 14 AUG 2023////

    @Override
    public void updateTemplateStockOpnameHeader(Map<String, String> balance) {
        //DELETE existing Header
        String sqlDel = "delete from M_OPNAME_TEMPL_HEADER where CD_TEMPLATE = :cdTemplate ";
        Map paramDel = new HashMap();
        paramDel.put("cdTemplate", balance.get("cdTemplate"));
        jdbcTemplate.update(sqlDel, paramDel);
        System.out.println("query del header : " + sqlDel);

        //insert head
        String qy = "INSERT INTO M_OPNAME_TEMPL_HEADER (CD_TEMPLATE,TEMPLATE_NAME,STATUS,USER_UPD,DATE_UPD,TIME_UPD)"
                + " VALUES(:cdTemplate,:templateName,:status,:userUpd,:dateUpd,:timeUpd)";
        Map param = new HashMap();
        param.put("cdTemplate", balance.get("cdTemplate"));
        param.put("templateName", balance.get("templateName"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
        deleteTempLateDetail(param);
        System.out.println("query insert header: " + qy);

    }

    @Override
    public void deleteTempLateDetail(Map<String, String> balance) {
        //DELETE existing Detail
        String sqlDel = "delete from M_OPNAME_TEMPL_DETAIL where CD_TEMPLATE = :cdTemplate ";
        Map paramDel = new HashMap();
        paramDel.put("cdTemplate", balance.get("cdTemplate"));
        jdbcTemplate.update(sqlDel, paramDel);
        System.out.println("query del detail : " + sqlDel);
    }

    @Override
    public void updateTemplateStockOpnameDetail(Map<String, String> balance) {
        //Delete existing Detail
        String qy = "INSERT INTO M_OPNAME_TEMPL_DETAIL(CD_TEMPLATE,ITEM_CODE,STATUS,USER_UPD,DATE_UPD,TIME_UPD)"
                + " VALUES(:cdTemplate,:itemCode,:stat,:userUpd,:dateUpd,:timeUpd)";
        Map param = new HashMap();
        param.put("cdTemplate", balance.get("cdTemplate"));
        param.put("itemCode", balance.get("itemCode"));
        param.put("stat", balance.get("stat"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
        System.out.println("query insert header: " + qy);
    }

////////////////////////DONE
    public String itemExist(Map<String, String> ref) {
        String qry = "select count(1) as existRow from m_item "
                + "where ITEM_CODE = :ItemCode "
                + "and status = :status ";
        Map prm = new HashMap();
        prm.put("ItemCode", ref.get("item"));
        prm.put("status", ref.get("stat"));
        System.err.println("q :" + qry);

        return jdbcTemplate.queryForObject(qry, prm, new RowMapper() {
            @Override
            public String mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                return rs.getString("existRow");
            }
        }).toString();
    }

    @Override
    public void insertMasterItem(JsonObject balancing) {
        String userU = balancing.getAsJsonPrimitive("userUpd").getAsString();
        JsonArray emp = balancing.getAsJsonObject().getAsJsonArray("itemList");
        for (int i = 0; i < emp.size(); i++) {
            Map<String, String> detailParam = new HashMap<String, String>();
            detailParam.put("itemCode", emp.get(i).getAsJsonObject().getAsJsonPrimitive("itemCode").getAsString());
            detailParam.put("cdBrand", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdBrand").getAsString());
            detailParam.put("itemDescription", emp.get(i).getAsJsonObject().getAsJsonPrimitive("itemDescription").getAsString());
            detailParam.put("cdLevel1", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdLevel1").getAsString());
            detailParam.put("cdLevel2", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdLevel2").getAsString());
            detailParam.put("cdLevel3", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdLevel3").getAsString());
            detailParam.put("cdLevel4", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdLevel4").getAsString());
            detailParam.put("amtCost", emp.get(i).getAsJsonObject().getAsJsonPrimitive("amtCost").getAsString());
            detailParam.put("uomWarehouse", emp.get(i).getAsJsonObject().getAsJsonPrimitive("uomWarehouse").getAsString());
            detailParam.put("convWarehouse", emp.get(i).getAsJsonObject().getAsJsonPrimitive("convWarehouse").getAsString());
            detailParam.put("uomPurchase", emp.get(i).getAsJsonObject().getAsJsonPrimitive("uomPurchase").getAsString());
            detailParam.put("convStock", emp.get(i).getAsJsonObject().getAsJsonPrimitive("convStock").getAsString());
            detailParam.put("uomStock", emp.get(i).getAsJsonObject().getAsJsonPrimitive("uomStock").getAsString());
            detailParam.put("cdWarehouse", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdWarehouse").getAsString());
            detailParam.put("flagOthers", emp.get(i).getAsJsonObject().getAsJsonPrimitive("flagOthers").getAsString());
            detailParam.put("flagMaterial", emp.get(i).getAsJsonObject().getAsJsonPrimitive("flagMaterial").getAsString());
            detailParam.put("flagHalffinish", emp.get(i).getAsJsonObject().getAsJsonPrimitive("flagHalffinish").getAsString());
            detailParam.put("flagFinishedgood", emp.get(i).getAsJsonObject().getAsJsonPrimitive("flagFinishedgood").getAsString());
            detailParam.put("flagOpenmarket", emp.get(i).getAsJsonObject().getAsJsonPrimitive("flagOpenmarket").getAsString());
            detailParam.put("flagTransferloc", emp.get(i).getAsJsonObject().getAsJsonPrimitive("flagTransferloc").getAsString());
            detailParam.put("flagCanvasing", emp.get(i).getAsJsonObject().getAsJsonPrimitive("flagCanvasing").getAsString());
            detailParam.put("flagStock", emp.get(i).getAsJsonObject().getAsJsonPrimitive("flagStock").getAsString());
            detailParam.put("plu", emp.get(i).getAsJsonObject().getAsJsonPrimitive("plu").getAsString());
            detailParam.put("cdSupplierDefault", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdSupplierDefault").getAsString());
            detailParam.put("minStock", emp.get(i).getAsJsonObject().getAsJsonPrimitive("minStock").getAsString());
            detailParam.put("maxStock", emp.get(i).getAsJsonObject().getAsJsonPrimitive("maxStock").getAsString());
            detailParam.put("qtyStock", emp.get(i).getAsJsonObject().getAsJsonPrimitive("qtyStock").getAsString());
            detailParam.put("cdMenuItem", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdMenuItem").getAsString());
            detailParam.put("cdLtemLeftover", emp.get(i).getAsJsonObject().getAsJsonPrimitive("cdLtemLeftover").getAsString());
            detailParam.put("status", emp.get(i).getAsJsonObject().getAsJsonPrimitive("status").getAsString());
            detailParam.put("flagPaket", emp.get(i).getAsJsonObject().getAsJsonPrimitive("flagPaket").getAsString());
            detailParam.put("userUpd", userU);
            inserUpdateMaster(detailParam);
            //  System.out.println(detailParam);
            detailParam.clear();
        }
    }

    public void inserUpdateMaster(Map<String, String> balance) {
        //Delete existing Detail

        Map paramkirim = new HashMap();
        paramkirim.put("item", balance.get("itemCode"));
        paramkirim.put("stat", balance.get("status"));
        System.out.println(paramkirim);
        String itemExisting = itemExist(paramkirim);
        if (itemExisting.equals('0')) {
            String qy = "INSERT INTO M_ITEM"
                    + "(ITEM_CODE,CD_BRAND,ITEM_DESCRIPTION,CD_LEVEL_1,CD_LEVEL_2,CD_LEVEL_3, "
                    + "      CD_LEVEL_4,AMT_COST,UOM_WAREHOUSE,CONV_WAREHOUSE,UOM_PURCHASE,CONV_STOCK "
                    + "      ,UOM_STOCK,CD_WAREHOUSE,FLAG_OTHERS,FLAG_MATERIAL,FLAG_HALF_FINISH, "
                    + "      FLAG_FINISHED_GOOD,FLAG_OPEN_MARKET,FLAG_TRANSFER_LOC,FLAG_CANVASING, "
                    + "      FLAG_STOCK,PLU,CD_SUPPLIER_DEFAULT,MIN_STOCK,MAX_STOCK,QTY_STOCK, "
                    + "      CD_MENU_ITEM,CD_ITEM_LEFTOVER,STATUS,USER_UPD,DATE_UPD,TIME_UPD, "
                    + "      FLAG_PAKET)"
                    + " VALUES(:itemCode,:cdBrand,:itemDescription,:cdLevel1,:cdLevel2,:cdLevel3,:cdLevel4,:amtCost,:uomWarehouse,:convWarehouse "
                    + ",:uomPurchase "
                    + ",:convStock "
                    + ",:uomStock "
                    + ",:cdWarehouse "
                    + ",:flagOthers "
                    + ",:flagMaterial "
                    + ",:flagHalffinish "
                    + ",:flagFinishedgood "
                    + ",:flagOpenmarket "
                    + ",:flagTransferloc "
                    + ",:flagCanvasing "
                    + ",:flagStock "
                    + ",:plu,:cdSupplierDefault "
                    + ",:minStock,:maxStock,:qtyStock,:cdMenuItem,:cdLtemLeftover,:status,:userUpd,:dateUpd,:timeUpd,:flagPaket)";
            Map param = new HashMap();
            param.put("itemCode", balance.get("itemCode"));
            param.put("cdBrand", balance.get("cdBrand"));
            param.put("itemDescription", balance.get("itemDescription"));
            param.put("cdLevel1", balance.get("cdLevel1"));
            param.put("cdLevel2", balance.get("cdLevel2"));
            param.put("cdLevel3", balance.get("cdLevel3"));
            param.put("cdLevel4", balance.get("cdLevel4"));
            param.put("amtCost", balance.get("amtCost"));
            param.put("uomWarehouse", balance.get("uomWarehouse"));
            param.put("convWarehouse", balance.get("convWarehouse"));
            param.put("uomPurchase", balance.get("uomPurchase"));
            param.put("convStock", balance.get("convStock"));
            param.put("uomStock", balance.get("uomStock"));
            param.put("cdWarehouse", balance.get("cdWarehouse"));
            param.put("flagOthers", balance.get("cpemail"));
            param.put("flagMaterial", balance.get("flagcanvasing"));
            param.put("flagHalffinish", balance.get("flagOthers"));
            param.put("flagFinishedgood", balance.get("flagFinishedgood"));
            param.put("flagOpenmarket", balance.get("flagOpenmarket"));
            param.put("flagTransferloc", balance.get("flagTransferloc"));
            param.put("flagCanvasing", balance.get("flagCanvasing"));
            param.put("flagStock", balance.get("flagStock"));
            param.put("plu", balance.get("plu"));
            param.put("cdSupplierDefault", balance.get("cdSupplierDefault"));
            param.put("minStock", balance.get("minStock"));
            param.put("maxStock", balance.get("maxStock"));
            param.put("qtyStock", balance.get("qtyStock"));
            param.put("cdMenuItem", balance.get("cdMenuItem"));
            param.put("cdLtemLeftover", balance.get("cdLtemLeftover"));
            param.put("status", balance.get("status"));
            param.put("userUpd", balance.get("userUpd"));
            param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
            param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
            param.put("flagPaket", balance.get("timeupd"));
            jdbcTemplate.update(qy, param);
            System.out.println("query insert item: " + qy);
        } else {
            String qy = "UPDATE M_ITEM SET  "
                    + "CD_BRAND:=cdBrand, "
                    + "ITEM_DESCRIPTION:=itemDescription, "
                    + "CD_LEVEL_1:=cdLevel1, "
                    + "CD_LEVEL_2:=cdLevel2, "
                    + "CD_LEVEL_3:=cdLevel3, "
                    + "CD_LEVEL_4:=cdLevel4, "
                    + "AMT_COST:=amtCost, "
                    + "UOM_WAREHOUSE:=uomWarehouse, "
                    + "CONV_WAREHOUSE:=convWarehouse, "
                    + "UOM_PURCHASE:=uomPurchase, "
                    + "CONV_STOCK:=convStock, "
                    + "UOM_STOCK:=uomStock, "
                    + "CD_WAREHOUSE:=cdWarehouse, "
                    + "FLAG_OTHERS:=flagOthers, "
                    + "FLAG_MATERIAL:=flagMaterial, "
                    + "FLAG_HALF_FINISH:=flagHalfFinish, "
                    + "FLAG_FINISHED_GOOD:=flagFinishedGood, "
                    + "FLAG_OPEN_MARKET:=flagOpenMarket, "
                    + "FLAG_TRANSFER_LOC:=flagTransferLoc, "
                    + "FLAG_CANVASING:=flagCanvasing, "
                    + "FLAG_STOCK:=flagStock, "
                    + "PLU:=plu, "
                    + "CD_SUPPLIER_DEFAULT:=cdSupplierDefault, "
                    + "MIN_STOCK:=minStock, "
                    + "MAX_STOCK:=maxStock, "
                    + "QTY_STOCK:=qtyStock, "
                    + "CD_MENU_ITEM:=cdMenuitem, "
                    + "CD_ITEM_LEFTOVER:=cdItemLeftover, "
                    + "STATUS:=status, "
                    + "USER_UPD:=userUpd, "
                    + "DATE_UPD:=dateUpd, "
                    + "TIME_UPD:=timeUpd, "
                    + "FLAG_PAKET:=flagPaketwhere ITEM_CODE=:itemCode";
            Map param = new HashMap();
            param.put("itemCode", balance.get("itemCode"));
            param.put("cdBrand", balance.get("cdBrand"));
            param.put("itemDescription", balance.get("itemDescription"));
            param.put("cdLevel1", balance.get("cdLevel1"));
            param.put("cdLevel2", balance.get("cdLevel2"));
            param.put("cdLevel3", balance.get("cdLevel3"));
            param.put("cdLevel4", balance.get("cdLevel4"));
            param.put("amtCost", balance.get("amtCost"));
            param.put("uomWarehouse", balance.get("uomWarehouse"));
            param.put("convWarehouse", balance.get("convWarehouse"));
            param.put("uomPurchase", balance.get("uomPurchase"));
            param.put("convStock", balance.get("convStock"));
            param.put("uomStock", balance.get("uomStock"));
            param.put("cdWarehouse", balance.get("cdWarehouse"));
            param.put("flagOthers", balance.get("flagOthers"));
            param.put("flagMaterial", balance.get("flagMaterial"));
            param.put("flagHalfFinish", balance.get("flagHalfFinish"));
            param.put("flagFinishedGood", balance.get("flagFinishedGood"));
            param.put("flagOpenMarket", balance.get("flagOpenMarket"));
            param.put("flagTransferLoc", balance.get("flagTransferLoc"));
            param.put("flagCanvasing", balance.get("flagCanvasing"));
            param.put("flagStock", balance.get("flagStock"));
            param.put("plu", balance.get("plu"));
            param.put("cdSupplierDefault", balance.get("cdSupplierDefault"));
            param.put("minStock", balance.get("minStock"));
            param.put("maxStock", balance.get("maxStock"));
            param.put("qtyStock", balance.get("qtyStock"));
            param.put("cdMenuitem", balance.get("cdMenuitem"));
            param.put("cdItemLeftover", balance.get("cdItemLeftover"));
            param.put("flagPaket", balance.get("flagPaket"));
            param.put("status", balance.get("status"));
            param.put("userUpd", balance.get("userUpd"));
            param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
            param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
            jdbcTemplate.update(qy, param);
            System.out.println("query update item: " + qy);
        }
    }

    public String colorExist(Map<String, String> ref) {
        String qry = "select count(1) as existRow from m_item "
                + "where ITEM_CODE = :ItemCode "
                + "and status = :status ";
        Map prm = new HashMap();
        prm.put("ItemCode", ref.get("item"));
        prm.put("status", ref.get("stat"));
        System.err.println("q :" + qry);

        return jdbcTemplate.queryForObject(qry, prm, new RowMapper() {
            @Override
            public String mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                return rs.getString("existRow");
            }
        }).toString();
    }

    @Override
    public void processTransferMasters(JsonObject balancing) {
        String userU = balancing.getAsJsonPrimitive("userUpd").getAsString();
        JsonArray emp = balancing.getAsJsonObject().getAsJsonArray("itemList");
        for (int i = 0; i < emp.size(); i++) {
            Map<String, String> detailParam = new HashMap<String, String>();
            detailParam.put("colorCode", emp.get(i).getAsJsonObject().getAsJsonPrimitive("colorCode").getAsString());
            detailParam.put("colorName", emp.get(i).getAsJsonObject().getAsJsonPrimitive("colorName").getAsString());
            detailParam.put("rValue", emp.get(i).getAsJsonObject().getAsJsonPrimitive("rValue").getAsString());
            detailParam.put("gValue", emp.get(i).getAsJsonObject().getAsJsonPrimitive("gValue").getAsString());
            detailParam.put("bValue", emp.get(i).getAsJsonObject().getAsJsonPrimitive("bValue").getAsString());
            detailParam.put("userUpd", userU);
            inserUpdateMaster(detailParam);
            //  System.out.println(detailParam);
            detailParam.clear();
        }
    }

    public void processTransferMasters(Map<String, String> balance) {
        //Delete existing Detail

        Map paramkirim = new HashMap();
        paramkirim.put("item", balance.get("itemCode"));
        paramkirim.put("stat", balance.get("status"));
        System.out.println(paramkirim);
        String colorExisting = colorExist(paramkirim);
        if (colorExisting.equals('0')) {
            String qy = "INSERT INTO M_COLOR"
                    + "(ITEM_CODE,CD_BRAND,ITEM_DESCRIPTION,CD_LEVEL_1,CD_LEVEL_2,CD_LEVEL_3, "
                    + "      CD_LEVEL_4,AMT_COST,UOM_WAREHOUSE,CONV_WAREHOUSE,UOM_PURCHASE,CONV_STOCK "
                    + "      ,UOM_STOCK,CD_WAREHOUSE,FLAG_OTHERS,FLAG_MATERIAL,FLAG_HALF_FINISH, "
                    + "      FLAG_FINISHED_GOOD,FLAG_OPEN_MARKET,FLAG_TRANSFER_LOC,FLAG_CANVASING, "
                    + "      FLAG_STOCK,PLU,CD_SUPPLIER_DEFAULT,MIN_STOCK,MAX_STOCK,QTY_STOCK, "
                    + "      CD_MENU_ITEM,CD_ITEM_LEFTOVER,STATUS,USER_UPD,DATE_UPD,TIME_UPD, "
                    + "      FLAG_PAKET)"
                    + " VALUES(:itemCode,:cdBrand,:itemDescription,:cdLevel1,:cdLevel2,:cdLevel3,:cdLevel4,:amtCost,:uomWarehouse,:convWarehouse "
                    + ",:uomPurchase "
                    + ",:convStock "
                    + ",:uomStock "
                    + ",:cdWarehouse "
                    + ",:flagOthers "
                    + ",:flagMaterial "
                    + ",:flagHalffinish "
                    + ",:flagFinishedgood "
                    + ",:flagOpenmarket "
                    + ",:flagTransferloc "
                    + ",:flagCanvasing "
                    + ",:flagStock "
                    + ",:plu,:cdSupplierDefault "
                    + ",:minStock,:maxStock,:qtyStock,:cdMenuItem,:cdLtemLeftover,:status,:userUpd,:dateUpd,:timeUpd,:flagPaket)";
            Map param = new HashMap();
            param.put("itemCode", balance.get("itemCode"));
            param.put("cdBrand", balance.get("cdBrand"));
            param.put("itemDescription", balance.get("itemDescription"));
            param.put("cdLevel1", balance.get("cdLevel1"));
            param.put("cdLevel2", balance.get("cdLevel2"));
            param.put("cdLevel3", balance.get("cdLevel3"));
            param.put("cdLevel4", balance.get("cdLevel4"));
            param.put("amtCost", balance.get("amtCost"));
            param.put("uomWarehouse", balance.get("uomWarehouse"));
            param.put("convWarehouse", balance.get("convWarehouse"));
            param.put("uomPurchase", balance.get("uomPurchase"));
            param.put("convStock", balance.get("convStock"));
            param.put("uomStock", balance.get("uomStock"));
            param.put("cdWarehouse", balance.get("cdWarehouse"));
            param.put("flagOthers", balance.get("cpemail"));
            param.put("flagMaterial", balance.get("flagcanvasing"));
            param.put("flagHalffinish", balance.get("flagOthers"));
            param.put("flagFinishedgood", balance.get("flagFinishedgood"));
            param.put("flagOpenmarket", balance.get("flagOpenmarket"));
            param.put("flagTransferloc", balance.get("flagTransferloc"));
            param.put("flagCanvasing", balance.get("flagCanvasing"));
            param.put("flagStock", balance.get("flagStock"));
            param.put("plu", balance.get("plu"));
            param.put("cdSupplierDefault", balance.get("cdSupplierDefault"));
            param.put("minStock", balance.get("minStock"));
            param.put("maxStock", balance.get("maxStock"));
            param.put("qtyStock", balance.get("qtyStock"));
            param.put("cdMenuItem", balance.get("cdMenuItem"));
            param.put("cdLtemLeftover", balance.get("cdLtemLeftover"));
            param.put("status", balance.get("status"));
            param.put("userUpd", balance.get("userUpd"));
            param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
            param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
            param.put("flagPaket", balance.get("timeupd"));
            jdbcTemplate.update(qy, param);
            System.out.println("query insert item: " + qy);
        } else {
            String qy = "UPDATE M_ITEM SET  "
                    + "CD_BRAND:=cdBrand, "
                    + "ITEM_DESCRIPTION:=itemDescription, "
                    + "CD_LEVEL_1:=cdLevel1, "
                    + "CD_LEVEL_2:=cdLevel2, "
                    + "CD_LEVEL_3:=cdLevel3, "
                    + "CD_LEVEL_4:=cdLevel4, "
                    + "AMT_COST:=amtCost, "
                    + "UOM_WAREHOUSE:=uomWarehouse, "
                    + "CONV_WAREHOUSE:=convWarehouse, "
                    + "UOM_PURCHASE:=uomPurchase, "
                    + "CONV_STOCK:=convStock, "
                    + "UOM_STOCK:=uomStock, "
                    + "CD_WAREHOUSE:=cdWarehouse, "
                    + "FLAG_OTHERS:=flagOthers, "
                    + "FLAG_MATERIAL:=flagMaterial, "
                    + "FLAG_HALF_FINISH:=flagHalfFinish, "
                    + "FLAG_FINISHED_GOOD:=flagFinishedGood, "
                    + "FLAG_OPEN_MARKET:=flagOpenMarket, "
                    + "FLAG_TRANSFER_LOC:=flagTransferLoc, "
                    + "FLAG_CANVASING:=flagCanvasing, "
                    + "FLAG_STOCK:=flagStock, "
                    + "PLU:=plu, "
                    + "CD_SUPPLIER_DEFAULT:=cdSupplierDefault, "
                    + "MIN_STOCK:=minStock, "
                    + "MAX_STOCK:=maxStock, "
                    + "QTY_STOCK:=qtyStock, "
                    + "CD_MENU_ITEM:=cdMenuitem, "
                    + "CD_ITEM_LEFTOVER:=cdItemLeftover, "
                    + "STATUS:=status, "
                    + "USER_UPD:=userUpd, "
                    + "DATE_UPD:=dateUpd, "
                    + "TIME_UPD:=timeUpd, "
                    + "FLAG_PAKET:=flagPaketwhere ITEM_CODE=:itemCode";
            Map param = new HashMap();
            param.put("itemCode", balance.get("itemCode"));
            param.put("cdBrand", balance.get("cdBrand"));
            param.put("itemDescription", balance.get("itemDescription"));
            param.put("cdLevel1", balance.get("cdLevel1"));
            param.put("cdLevel2", balance.get("cdLevel2"));
            param.put("cdLevel3", balance.get("cdLevel3"));
            param.put("cdLevel4", balance.get("cdLevel4"));
            param.put("amtCost", balance.get("amtCost"));
            param.put("uomWarehouse", balance.get("uomWarehouse"));
            param.put("convWarehouse", balance.get("convWarehouse"));
            param.put("uomPurchase", balance.get("uomPurchase"));
            param.put("convStock", balance.get("convStock"));
            param.put("uomStock", balance.get("uomStock"));
            param.put("cdWarehouse", balance.get("cdWarehouse"));
            param.put("flagOthers", balance.get("flagOthers"));
            param.put("flagMaterial", balance.get("flagMaterial"));
            param.put("flagHalfFinish", balance.get("flagHalfFinish"));
            param.put("flagFinishedGood", balance.get("flagFinishedGood"));
            param.put("flagOpenMarket", balance.get("flagOpenMarket"));
            param.put("flagTransferLoc", balance.get("flagTransferLoc"));
            param.put("flagCanvasing", balance.get("flagCanvasing"));
            param.put("flagStock", balance.get("flagStock"));
            param.put("plu", balance.get("plu"));
            param.put("cdSupplierDefault", balance.get("cdSupplierDefault"));
            param.put("minStock", balance.get("minStock"));
            param.put("maxStock", balance.get("maxStock"));
            param.put("qtyStock", balance.get("qtyStock"));
            param.put("cdMenuitem", balance.get("cdMenuitem"));
            param.put("cdItemLeftover", balance.get("cdItemLeftover"));
            param.put("flagPaket", balance.get("flagPaket"));
            param.put("status", balance.get("status"));
            param.put("userUpd", balance.get("userUpd"));
            param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
            param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
            jdbcTemplate.update(qy, param);
            System.out.println("query update item: " + qy);
        }
    }

    ///////////////New method from Fathur 11-Dec-2023////////////////////////////
    @Override
    public void sendDataOutletToWarehouse(Map<String, String> balance) {
        String json = "";
        Gson gson = new Gson();
        Map<String, Object> map1 = new HashMap<>();
        try {
            String qry1 = "SELECT OUTLET_CODE, "
                    + "ORDER_TYPE,   "
                    + "ORDER_ID,     "
                    + "ORDER_NO,     "
                    + "TO_CHAR(ORDER_DATE, 'DD-MON-YY') AS ORDER_DATE, "
                    + "ORDER_TO,     "
                    + "CD_SUPPLIER,  "
                    + "TO_CHAR(DT_DUE, 'DD-MON-YY') AS DT_DUE, "
                    + "TO_CHAR(DT_EXPIRED, 'DD-MON-YY') AS DT_EXPIRED, "
                    + "CASE WHEN REMARK IS NULL THEN ' ' ELSE REMARK END AS REMARK, "
                    + "NO_OF_PRINT,  "
                    + "STATUS,       "
                    + "USER_UPD,     "
                    + "TO_CHAR(DATE_UPD, 'DD-MON-YY') AS DATE_UPD,     "
                    + "TIME_UPD, "
                    + "'N' AS STATUSH "
                    + "FROM   T_ORDER_HEADER "
                    + "WHERE   ORDER_NO =:orderNo ";

            Map prm = new HashMap();
            prm.put("orderNo", balance.get("orderNo"));
            System.err.println("q1 :" + qry1);
            List<Map<String, Object>> list = jdbcTemplate.query(qry1, prm, new RowMapper<Map<String, Object>>() {
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rh = new HashMap<>();
                    String qry2 = "SELECT OUTLET_CODE, "
                            + "ORDER_TYPE, "
                            + "ORDER_ID, "
                            + "ORDER_NO, "
                            + "ITEM_CODE, "
                            + "QTY_1, "
                            + "CD_UOM_1, "
                            + "QTY_2, "
                            + "CD_UOM_2, "
                            + "TOTAL_QTY_STOCK, "
                            + "UNIT_PRICE, "
                            + "USER_UPD, "
                            + "TO_CHAR(DATE_UPD, 'DD-MON-YY') AS DATE_UPD, "
                            + "TIME_UPD, "
                            + "'N' AS STATUSD "
                            + "FROM T_ORDER_DETAIL WHERE ORDER_NO =:orderNo ";
                    System.err.println("q2 :" + qry2);
                    List<Map<String, Object>> list2 = jdbcTemplate.query(qry2, prm, new RowMapper<Map<String, Object>>() {
                        @Override
                        public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                            Map<String, Object> rt = new HashMap<>();
                            rt.put("orderType", rs.getString("ORDER_TYPE"));
                            rt.put("itemCode", rs.getString("ITEM_CODE"));
                            rt.put("qty1", rs.getString("QTY_1"));
                            rt.put("cdUom1", rs.getString("CD_UOM_1"));
                            rt.put("qty2", rs.getString("QTY_2"));
                            rt.put("cdUom2", rs.getString("CD_UOM_2"));
                            rt.put("totalQtyStock", rs.getString("TOTAL_QTY_STOCK"));
                            rt.put("unitPrice", rs.getString("UNIT_PRICE"));
                            rt.put("statusd", rs.getString("STATUSD"));
                            return rt;
                        }
                    });
                    rh.put("itemList", list2);
                    rh.put("outletCode", rs.getString("OUTLET_CODE"));
                    rh.put("orderType", rs.getString("ORDER_TYPE"));
                    rh.put("orderId", rs.getString("ORDER_ID"));
                    rh.put("orderNo", rs.getString("ORDER_NO"));
                    rh.put("orderDate", rs.getString("ORDER_DATE"));
                    rh.put("orderTo", rs.getString("ORDER_TO"));
                    rh.put("cdSupplier", rs.getString("CD_SUPPLIER"));
                    rh.put("dtDue", rs.getString("DT_DUE"));
                    rh.put("dtExpired", rs.getString("DT_EXPIRED"));
                    rh.put("remark", rs.getString("REMARK"));
                    rh.put("noOfPrint", rs.getString("NO_OF_PRINT"));
                    rh.put("status", rs.getString("STATUS"));
                    rh.put("userUpd", rs.getString("USER_UPD"));
                    rh.put("dateUpd", rs.getString("DATE_UPD"));
                    rh.put("timeUpd", rs.getString("TIME_UPD"));
                    rh.put("statush", rs.getString("STATUSH"));

                    return rh;
                }
            });

            for (Map<String, Object> dtl : list) {
                CloseableHttpClient client = HttpClients.createDefault();
                String url = urlWarehouse + "/insert-order-outlet-hdrdtl";
                HttpPost post = new HttpPost(url);

                post.setHeader("Accept", "*/*");
                post.setHeader("Content-Type", "application/json");

                Map<String, Object> param = new HashMap<>();
                param.put("outletCode", dtl.get("outletCode"));
                param.put("orderType", dtl.get("orderType"));
                param.put("orderId", dtl.get("orderId"));
                param.put("orderNo", dtl.get("orderNo"));
                param.put("orderDate", dtl.get("orderDate"));
                param.put("orderTo", dtl.get("orderTo"));
                param.put("cdSupplier", dtl.get("cdSupplier"));
                param.put("dtDue", dtl.get("dtDue"));
                param.put("dtExpired", dtl.get("dtExpired"));
                param.put("remark", dtl.get("remark"));
                param.put("noOfPrint", dtl.get("noOfPrint"));
                param.put("status", dtl.get("status"));
                param.put("userUpd", dtl.get("userUpd"));
                param.put("dateUpd", dtl.get("dateUpd"));
                param.put("timeUpd", dtl.get("timeUpd"));
                param.put("statush", dtl.get("statush"));
                //list
                param.put("itemList", dtl.get("itemList"));
                json = new Gson().toJson(param);
                StringEntity entity = new StringEntity(json);
                post.setEntity(entity);
                CloseableHttpResponse response = client.execute(post);
                System.out.println("json" + json);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                (response.getEntity().getContent())
                        )
                );
                StringBuilder content = new StringBuilder();
                String line;
                while (null != (line = br.readLine())) {
                    content.append(line);
                }
                String result = content.toString();
                System.out.println("trans =" + result);
                map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
                }.getType());
            }
            Map<String, String> histSend = new HashMap<>();
            histSend.put("orderNo", balance.get("orderNo").toString());
            insertHistSend(histSend);

            Map<String, String> histKirim = new HashMap<>();
            histKirim.put("orderNo", balance.get("orderNo").toString());
            histKirim.put("sendUser", balance.get("userUpd").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////New method for insert Eod Hist POS 'N' ke eod - M Joko M 11-Dec-2023////////////
    @Override
    public void insertEodPosN(Map<String, String> balance) {
        long startTime = System.currentTimeMillis();
        String query = "insert into t_eod_hist_dtl (REGION_CODE, OUTLET_CODE, TRANS_DATE, POS_CODE, PROCESS_EOD, NOTES, USER_UPD, DATE_UPD, TIME_UPD) values(:regionCode, :outletCode, (select trans_date from m_outlet where outlet_code=:outletCode), :posCode, :processEod, :notes, :userUpd, :dateUpd, :timeUpd)";
        Map param = new HashMap();
        param.put("regionCode", balance.get("regionCode"));
        param.put("outletCode", balance.get("outletCode"));
        param.put("posCode", balance.get("posCode"));
        param.put("processEod", balance.get("processEod"));
        param.put("notes", balance.get("notes"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(query, param);
        double elapsedTimeSeconds = (double) (System.currentTimeMillis() - startTime) / 1000.0;
        System.err.println("insertEodPosN process in: " + elapsedTimeSeconds + " seconds");
    }

    ////////////New method for insert T Stock Card EOD - M Joko M 11-Dec-2023////////////
    // 27/12/23 update ambil item dari m_item, bukan prev Stock Card
    @Override
    public void insertTStockCard(Map<String, String> balance) {
        long startTime = System.currentTimeMillis();
        String query = """
                INSERT INTO t_stock_card (
                SELECT
                    :outletCode AS OUTLET_CODE,
                    mi.TRANS_DATE+1 AS TRANS_DATE,
                    mi.ITEM_CODE,
                    0 AS ITEM_COST,
                    (NVL(tsc.qty_beginning, 0) + NVL(tsc.qty_in, 0) - NVL(tsc.qty_out, 0)) AS QTY_BEGINNING,
                    0 AS QTY_IN,
                    0 AS QTY_OUT,
                    'EOD' AS REMARK,
                    :userUpd AS USER_UPD,
                    TO_CHAR(SYSDATE, 'DD MON RR') AS DATE_UPD,
                    TO_CHAR(SYSDATE, 'HH24MISS') AS TIME_UPD
                FROM
                    (
                        SELECT
                            (SELECT trans_date FROM m_outlet WHERE outlet_code = :outletCode) AS TRANS_DATE,
                            ITEM_CODE
                        FROM
                            m_item
                        WHERE
                            FLAG_STOCK = 'Y'
                            AND FLAG_MATERIAL = 'Y'
                            AND STATUS = 'A'
                    ) mi
                LEFT JOIN
                    T_STOCK_CARD tsc ON tsc.trans_date = mi.TRANS_DATE AND tsc.item_code = mi.ITEM_CODE
                )
                       """;
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("userUpd", balance.getOrDefault("userUpd", "SYSTEM"));
        jdbcTemplate.update(query, param);
        double elapsedTimeSeconds = (double) (System.currentTimeMillis() - startTime) / 1000.0;
        System.err.println("insertTStockCard process in: " + elapsedTimeSeconds + " seconds");
    }

    ////////////New method for insert T EOD Hist - M Joko M 12-Dec-2023////////////
    @Override
    public void insertTEodHist(Map<String, String> balance) {
        long startTime = System.currentTimeMillis();
        String query = "insert into t_eod_hist (REGION_CODE, OUTLET_CODE, TRANS_DATE, USER_EOD, DATE_EOD, TIME_EOD, SEND_FLAG, USER_SEND, DATE_SEND, TIME_SEND) values ((select region_code from m_outlet where outlet_code=:outletCode), :outletCode, (select trans_date from m_outlet where outlet_code=:outletCode), :userEod, :dateEod, :timeEod, :sendFlag, :userSend, :dateSend, :timeSend)";
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("userEod", balance.get("userUpd"));
        param.put("dateEod", LocalDateTime.now().format(dateFormatter));
        param.put("timeEod", LocalDateTime.now().format(timeFormatter));
        param.put("sendFlag", "N");
        param.put("userSend", balance.get("userUpd"));
        param.put("dateSend", LocalDateTime.now().format(dateFormatter));
        param.put("timeSend", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(query, param);
        double elapsedTimeSeconds = (double) (System.currentTimeMillis() - startTime) / 1000.0;
        System.err.println("insertTEodHist process in: " + elapsedTimeSeconds + " seconds");
    }

    ////////////New method for increase trans_date M Outlet selesai EOD - M Joko M 11-Dec-2023////////////
    @Override
    public void increaseTransDateMOutlet(Map<String, String> balance) {
        long startTime = System.currentTimeMillis();
        String query = "update m_outlet set trans_date = (select trans_date from m_outlet where outlet_code=:outletCode) + 1, user_upd = :userUpd, time_upd = :timeUpd, date_upd = :dateUpd where outlet_code = :outletCode";
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(query, param);
        double elapsedTimeSeconds = (double) (System.currentTimeMillis() - startTime) / 1000.0;
        System.err.println("increaseTransDateMOutlet process in: " + elapsedTimeSeconds + " seconds");
    }

    ///////////////NEW METHOD insert T SUMM MPCS - M Joko 14/12/2023//// 
    @Override
    public void insertTSummMpcs(Map<String, String> balance) {
        long startTime = System.currentTimeMillis();
        // query insert from Dona
        String query = """
                INSERT INTO T_SUMM_MPCS (
                SELECT
                    A.OUTLET_CODE,
                    A.MPCS_GROUP,
                    A.DATE_MPCS + 1 AS DATE_MPCS,
                    A.SEQ_MPCS,
                    A.TIME_MPCS,
                    TOT AS QTY_PROJ_CONV,
                    A.UOM_PROJ_CONV,
                    TOT AS QTY_PROJ,
                    A.UOM_PROJ,
                    SUM(B.TOT) OVER (PARTITION BY A.MPCS_GROUP ORDER BY A.SEQ_MPCS) AS QTY_ACC_PROJ,
                    A.UOM_ACC_PROJ,
                    ' ' AS DESC_PROD,
                    0 AS QTY_PROD,
                    A.UOM_PROD,
                    0 AS QTY_ACC_PROD,
                    A.UOM_ACC_PROD,
                    ' ' AS PROD_BY,
                    0 AS QTY_SOLD,
                    A.UOM_SOLD,
                    0 AS QTY_ACC_SOLD,
                    A.UOM_ACC_SOLD,
                    0 AS QTY_REJECT,
                    A.UOM_REJECT,
                    0 AS QTY_ACC_REJECT,
                    A.UOM_ACC_REJECT,
                    0 AS QTY_WASTAGE,
                    A.UOM_WASTAGE,
                    0 AS QTY_ACC_WASTAGE,
                    A.UOM_ACC_WASTAGE,
                    0 AS QTY_ONHAND,
                    A.UOM_ONHAND,
                    0 AS QTY_ACC_ONHAND,
                    A.UOM_ACC_ONHAND,
                    TOT AS QTY_VARIANCE,
                    A.UOM_VARIANCE,
                    0 AS QTY_ACC_VARIANCE,
                    A.UOM_ACC_VARIANCE,
                    A.USER_UPD,
                    A.DATE_UPD,
                    A.TIME_UPD,
                    0 AS QTY_IN,
                    0 AS QTY_OUT
                FROM
                    t_summ_mpcs A
                LEFT JOIN (
                    SELECT
                        OUTLET_CODE,
                        MPCS_GROUP,
                        SEQ_MPCS,
                        TIME_MPCS,
                        SUM(TOT) AS TOT
                    FROM
                        (
                            SELECT
                                OUTLET_CODE,
                                mpcs_group,
                                seq_mpcs,
                                time_mpcs,
                                SUM(qty_sold) AS qty_sold,
                                SUM(qty_sold1) AS qty_sold1,
                                SUM(qty_sold2) AS qty_sold2,
                                ROUND((SUM(qty_sold) + SUM(qty_sold1) + SUM(qty_sold2)) / 3) AS tot
                            FROM
                                (
                                    SELECT
                                        OUTLET_CODE,
                                        mpcs_group,
                                        date_mpcs,
                                        seq_mpcs,
                                        time_mpcs,
                                        qty_sold,
                                        0 AS qty_sold1,
                                        0 AS qty_sold2
                                    FROM
                                        t_summ_mpcs
                                    WHERE
                                        date_mpcs IN (SELECT trans_date - 7 FROM m_outlet WHERE outlet_code = :outletCode)
                                    UNION ALL
                                    SELECT
                                        OUTLET_CODE,
                                        mpcs_group,
                                        date_mpcs,
                                        seq_mpcs,
                                        time_mpcs,
                                        0 AS qty_sold,
                                        qty_sold AS qty_sold1,
                                        0 AS qty_sold2
                                    FROM
                                        t_summ_mpcs
                                    WHERE
                                        date_mpcs IN (SELECT trans_date - 14 FROM m_outlet WHERE outlet_code = :outletCode)
                                    UNION ALL
                                    SELECT
                                        OUTLET_CODE,
                                        mpcs_group,
                                        date_mpcs,
                                        seq_mpcs,
                                        time_mpcs,
                                        0 AS qty_sold,
                                        0 AS qty_sold1,
                                        qty_sold AS qty_sold2
                                    FROM
                                        t_summ_mpcs
                                    WHERE
                                        date_mpcs IN (SELECT trans_date - 21 FROM m_outlet WHERE outlet_code = :outletCode)
                                )
                            GROUP BY
                                OUTLET_CODE,
                                mpcs_group,
                                seq_mpcs,
                                time_mpcs
                            ORDER BY
                                mpcs_group ASC,
                                seq_mpcs
                        ) B
                    GROUP BY
                        OUTLET_CODE,
                        MPCS_GROUP,
                        SEQ_MPCS,
                        TIME_MPCS
                ) B ON A.OUTLET_CODE = B.OUTLET_CODE
                    AND A.MPCS_GROUP = B.MPCS_GROUP
                    AND A.SEQ_MPCS = B.SEQ_MPCS
                    AND A.TIME_MPCS = B.TIME_MPCS
                WHERE
                    DATE_MPCS IN (SELECT trans_date FROM m_outlet WHERE outlet_code = :outletCode)
                   )
                       """;
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        jdbcTemplate.update(query, param);
        double elapsedTimeSeconds = (double) (System.currentTimeMillis() - startTime) / 1000.0;
        System.err.println("insertTSummMpcs process in: " + elapsedTimeSeconds + " seconds");
    }

    ///////////////NEW METHOD update MPCS plan by Dona 14/12/2023//// 
    @Override
    public void updateMpcsPlan(Map<String, String> balance) {
        String qy = "UPDATE T_SUMM_MPCS SET "
                + "QTY_PROJ=:qtyProj,"
                + "QTY_VARIANCE=(-1)*:qtyProj,"
                + "USER_UPD=:userUpd,"
                + "DATE_UPD=:dateUpd,"
                + "TIME_UPD=:timeUpd "
                + "WHERE MPCS_GROUP=:mpcsGroup "
                + "AND DATE_MPCS=:dateMpcs "
                + "AND SEQ_MPCS=:seqMpcs "
                + "AND OUTLET_CODE=:outletCode ";
        Map param = new HashMap();
        param.put("qtyProj", balance.get("qtyProj"));
        param.put("outletCode", balance.get("outletCode"));;
        param.put("mpcsGroup", balance.get("mpcsGroup"));
        param.put("dateMpcs", balance.get("dateMpcs"));
        param.put("seqMpcs", balance.get("seqMpcs"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));

        jdbcTemplate.update(qy, param);
        
        String updateQuantityAccQuery = "MERGE INTO T_SUMM_MPCS tsm "
                + "USING ("
                + "	SELECT SEQ_MPCS, QTY_PROJ, sum(QTY_PROJ) OVER (ORDER BY seq_mpcs) AS UPDATED_QTY_ACC_PROJ, sum(QTY_VARIANCE) OVER (ORDER BY seq_mpcs) AS UPDATED_QTY_ACC_VARIANCE "
                + "		FROM T_SUMM_MPCS tsm "
                + "		WHERE tsm.MPCS_GROUP =:mpcsGroup AND tsm.DATE_MPCS = :dateMpcs "
                + "	) up "
                + "ON (tsm.SEQ_MPCS = up.SEQ_MPCS AND tsm.DATE_MPCS = :dateMpcs AND tsm.MPCS_GROUP = :mpcsGroup) "
                + "WHEN MATCHED THEN "
                + "	UPDATE SET "
                + "		tsm.QTY_ACC_PROJ = up.UPDATED_QTY_ACC_PROJ, "
                + "		tsm.QTY_ACC_VARIANCE = up.UPDATED_QTY_ACC_VARIANCE, "
                + "		tsm.USER_UPD = :userUpd,"
                + "		tsm.DATE_UPD = :dateUpd,"
                + "		tsm.TIME_UPD = :timeUpd ";

        jdbcTemplate.update(updateQuantityAccQuery, param);
    }

    ////////////New method for insert m_counter setelah Stock Opname - M Joko M 20-Dec-2023////////////
    // jika row dengan menu_id, year, month sudah ada, tidak akan insert
    public void updateMCounterAfterStockOpname(Map<String, String> balance) {
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        String qryCounter = "INSERT INTO m_counter (OUTLET_CODE, TRANS_TYPE, YEAR, MONTH, COUNTER_NO) ( SELECT mo.OUTLET_CODE, menu_id AS TRANS_TYPE, CASE WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 THEN EXTRACT(YEAR FROM mo.TRANS_DATE) + 1 ELSE EXTRACT(YEAR FROM mo.TRANS_DATE) END AS YEAR, CASE WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 THEN 1 ELSE EXTRACT(MONTH FROM mo.TRANS_DATE) + 1 END AS MONTH, 0 AS COUNTER_NO FROM M_MENUDTL m JOIN m_outlet mo ON mo.OUTLET_CODE = :outletCode WHERE m.TYPE_ID = 'counter' AND m.APLIKASI = 'SOP' AND m.STATUS = 'A' AND NOT EXISTS ( SELECT 1 FROM m_counter mc WHERE mc.OUTLET_CODE = mo.OUTLET_CODE AND mc.TRANS_TYPE = m.menu_id AND mc.YEAR = CASE WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 THEN EXTRACT(YEAR FROM mo.TRANS_DATE) + 1 ELSE EXTRACT(YEAR FROM mo.TRANS_DATE) END AND mc.MONTH = CASE WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 THEN 1 ELSE EXTRACT(MONTH FROM mo.TRANS_DATE) + 1 END ) )";
        System.err.println("q updateMCounterAfterStockOpname: " + qryCounter);
        jdbcTemplate.update(qryCounter, param);
    }

    ////////////New method for update t_order_header jika expired setelah End of Day - M Joko M 27-Dec-2023////////////
    @Override
    public void updateOrderEntryExpired(Map<String, String> balance) {
        long startTime = System.currentTimeMillis();
        String qryList = "SELECT * FROM t_order_header WHERE outlet_code = :outletCode AND status = 0 AND dt_expired <= (select trans_date from m_outlet where outlet_code=:outletCode)";
        Map<String, Object> prmOE = new HashMap<>();
        prmOE.put("outletCode", balance.get("outletCode"));
        List<Map<String, Object>> listOE = jdbcTemplate.query(qryList, prmOE, new DynamicRowMapper());
        if (!listOE.isEmpty()) {
            for (int i = 0; i < listOE.size(); i++) {
                Map<String, Object> orderEntry = listOE.get(i);
                System.err.println("orderEntry (" + i + "): " + orderEntry);
                Map prmUpd = new HashMap();
                prmUpd.put("outletCode", orderEntry.get("outletCode"));
                prmUpd.put("orderNo", orderEntry.get("orderNo"));
                String qryUpd = "update t_order_header set status = 2 where order_no = :orderNo and outlet_code = :outletCode";
                System.err.println("updateOrderEntryExpired (" + orderEntry.get("orderNo") + "): " + qryUpd);
                jdbcTemplate.update(qryUpd, prmUpd);
            }
        }
        double elapsedTimeSeconds = (double) (System.currentTimeMillis() - startTime) / 1000.0;
        System.err.println("updateOrderEntryExpired process in: " + elapsedTimeSeconds + " seconds");
    }

    // New Method for insert or update to t_dev_header and t_dev_detail by Dani 27 Dec 2023
    @Transactional
    public void insertUpdateDeliveryOrder(Map<String, Object> resource) throws Exception {
        Gson gson = new Gson();
        String queryHeader = "SELECT count(*) FROM T_DEV_HEADER WHERE REQUEST_NO = :requestNo AND OUTLET_TO = :outletTo";
        Integer countHeader = jdbcTemplate.queryForObject(queryHeader, resource, Integer.class);
        resource.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        resource.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        if (countHeader > 0) {
            // query update header
            String queryHeaderUpdate = "UPDATE T_DEV_HEADER "
                    + " SET DELIVERY_DATE=:deliveryDate, REMARK=:remark, STATUS=:status, USER_UPD=:userUpd, DATE_UPD=:dateUpd, TIME_UPD=:timeUpd"
                    + " WHERE OUTLET_CODE=:outletCode AND OUTLET_TO=:outletTo AND REQUEST_NO=:requestNo";
            jdbcTemplate.update(queryHeaderUpdate, resource);
        } else {
            // query insert header
            LocalDate transDate = LocalDate.parse(this.jdbcTemplate.queryForObject(
                    "SELECT DISTINCT TO_CHAR(TRANS_DATE, 'YYYY-MM-DD') FROM M_OUTLET WHERE OUTLET_CODE = :outletCode and status = 'A'",
                    resource, String.class));
            String dlvNo = opnameNumber(transDate.getYear() + "", transDate.getMonthValue() + "", "DLV", (String) resource.get("outletCode"));
            updateMCounterSop("DLV", (String) resource.get("outletCode"));
            resource.put("deliveryNo", dlvNo);
            String queryHeaderInsert = "INSERT INTO T_DEV_HEADER "
                    + " (OUTLET_CODE, OUTLET_TO, REQUEST_NO, DELIVERY_NO, DELIVERY_DATE, REMARK, STATUS, USER_UPD, DATE_UPD, TIME_UPD)"
                    + " VALUES(:outletCode, :outletTo, :requestNo, :deliveryNo, :deliveryDate, :remark, :status, :userUpd, :dateUpd, :timeUpd)";
            jdbcTemplate.update(queryHeaderInsert, resource);
        }
        JsonArray details = gson.toJsonTree(resource.get("details")).getAsJsonArray();
        details.forEach(detail -> {
            JsonObject obj = detail.getAsJsonObject();
            obj.addProperty("requestNo", (String) resource.get("requestNo"));
            obj.addProperty("deliveryNo", (String) resource.get("deliveryNo"));
            obj.addProperty("outletTo", (String) resource.get("outletTo"));
            obj.addProperty("outletCode", (String) resource.get("outletCode"));
            obj.addProperty("userUpd", (String) resource.get("userUpd"));
            obj.addProperty("dateUpd", (String) resource.get("dateUpd"));
            obj.addProperty("timeUpd", (String) resource.get("timeUpd"));
            insertUpdateDeliveryOrderDetail(detail.getAsJsonObject());
        });
    }

    // NEW METHOD FOR INSERT AND UPDATE DELIVERY OURDER DETAIL BY DANI BY DEC 2023
    public void insertUpdateDeliveryOrderDetail(JsonObject details) {
        Gson gson = new Gson();
        for (int i = 0; i < details.size(); i++) {
            String queryDetail = "SELECT count(*) FROM T_DEV_DETAIL WHERE REQUEST_NO=:requestNo AND OUTLET_TO=:outletTo AND ITEM_CODE=:itemCode";
            Map<String, Object> map = gson.fromJson(details, new TypeToken<Map<String, Object>>() {
            }.getType());
            Integer countDetail = jdbcTemplate.queryForObject(queryDetail, map, Integer.class);
            if (countDetail > 0) {
                // query update detail
                String queryDetailUpdate = "UPDATE T_DEV_DETAIL"
                        + " SET DELIVERY_NO=:deliveryNo, QTY_PURCH=:qtyPurch, UOM_PURCH=:uomPurch, QTY_STOCK=:qtyStock, UOM_STOCK=:uomStock, TOTAL_QTY=:totalQty, USER_UPD=:userUpd, DATE_UPD=:dateUpd, TIME_UPD=:timeUpd"
                        + " WHERE OUTLET_CODE=:outletCode AND OUTLET_TO=:outletTo AND REQUEST_NO=:requestNo AND ITEM_CODE=:itemCode";
                jdbcTemplate.update(queryDetailUpdate, map);
            } else {
                // query insert detail
                String queryDetailInsert = " INSERT INTO T_DEV_DETAIL "
                        + " (OUTLET_CODE, OUTLET_TO, REQUEST_NO, DELIVERY_NO, ITEM_CODE, QTY_PURCH, UOM_PURCH, QTY_STOCK, UOM_STOCK, TOTAL_QTY, USER_UPD, DATE_UPD, TIME_UPD)"
                        + " VALUES(:outletCode, :outletTo, :requestNo, :deliveryNo, :itemCode, :qtyPurch, :uomPurch, :qtyStock, :uomStock, :totalQty, :userUpd, :dateUpd, :timeUpd)";
                jdbcTemplate.update(queryDetailInsert, map);
            }
        }
    }

    /////// NEW METHOD for kirim delivery Order by Dani 28 Dec 2023
    @Transactional
    public void kirimDeloveryOrder(Map<String, String> data) throws ClientProtocolException, IOException {
        data.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        data.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        String query = "UPDATE T_DEV_HEADER SET STATUS = '1' WHERE REQUEST_NO=:requestNo AND DELIVERY_NO =:deliveryNo";
        jdbcTemplate.update(query, data);
        // hit api to warehouse
        String qhs = "SELECT OUTLET_TO as OUTLET_CODE, REQUEST_NO as ORDER_NO, STATUS, DELIVERY_NO as REMARK FROM T_DEV_HEADER WHERE REQUEST_NO=:requestNo AND DELIVERY_NO = :deliveryNo";
        Map<String, Object> hdr = jdbcTemplate.queryForObject(qhs, data, new DynamicRowMapper());
        hdr.put("userUpd", data.get("userUpd"));
        hdr.put("dateUpd", data.get("dateUpd"));
        hdr.put("timeUpd", data.get("timeUpd"));

        String qds = "SELECT QTY_PURCH as QTY_1, QTY_STOCK as QTY_2, TOTAL_QTY, ITEM_CODE, REQUEST_NO as ORDER_NO, OUTLET_TO as OUTLET_CODE FROM T_DEV_DETAIL WHERE REQUEST_NO=:requestNo AND DELIVERY_NO=:deliveryNo";
        List<Map<String, Object>> dtls = jdbcTemplate.query(qds, data, new DynamicRowMapper());
        dtls.forEach(dtl -> {
            dtl.put("realOutletCode", data.get("outletCode"));
            dtl.put("userUpd", data.get("userUpd"));
            dtl.put("dateUpd", data.get("dateUpd"));
            dtl.put("timeUpd", data.get("timeUpd"));
        });

        hdr.put("details", dtls);

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        String json = "";
        String total = "";
        Gson gson = new Gson();
        CloseableHttpClient client = HttpClients.createDefault();
        String url = this.urlWarehouse + "/update-order-outlet-to-outlet";
        HttpPost post = new HttpPost(url);
        post.setHeader("Accept", "*/*");
        post.setHeader("Content-Type", "application/json");
        json = new Gson().toJson(hdr);
        StringEntity entity = new StringEntity(json);
        post.setEntity(entity);
        CloseableHttpResponse response = client.execute(post);
        System.out.println("json" + json);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        (response.getEntity().getContent())));
        StringBuilder content = new StringBuilder();
        String line;
        while (null != (line = br.readLine())) {
            content.append(line);
        }
        String result = content.toString();
        System.out.println("trans =" + result);
        map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());
        JsonObject job = gson.fromJson(result, JsonObject.class);

        // update stock card below
        dtls.forEach(dtl -> {
            updateDelvStockCard(dtl);
        });
    }

    // update receive stock card for delery order stock by Dani 28 December 2023
    // this method is for update table t_stock_card and t_stock_card_detail for delivery order stock
    public void updateDelvStockCard(Map<String, Object> balance) {
        String qf = "SELECT CASE WHEN QUANTITY IS NULL THEN 0 ELSE QUANTITY END AS QUANTITY FROM T_STOCK_CARD_DETAIL WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :transDate AND item_code= :itemCode AND cd_trans='DLV'";
        Map<String, Object> param = new HashMap<>();
        param.put("outletCode", balance.get("realOutletCode"));
        param.put("itemCode", balance.get("itemCode"));
        param.put("cdTrans", "DLV");
        param.put("qtyIn", 0);
        param.put("qty", balance.get("totalQty"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        BigDecimal existQty = null;
        String transDate = this.jdbcTemplate.queryForObject(
                "SELECT DISTINCT TO_CHAR(TRANS_DATE, 'DD-MON-YYYY') FROM M_OUTLET WHERE OUTLET_CODE = :outletCode and status = 'A'",
                param, String.class);
        param.put("transDate", transDate);

        try {
            String qsh = "SELECT CASE WHEN QTY_OUT IS NULL THEN 0 ELSE QTY_OUT END AS QTY_OUT FROM T_STOCK_CARD WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :transDate AND ITEM_CODE = :itemCode ";
            BigDecimal hdrQtyInDb = jdbcTemplate.queryForObject(qsh, param, BigDecimal.class);
            String quh = "UPDATE T_STOCK_CARD SET QTY_OUT = :qtyOut WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :transDate AND ITEM_CODE = :itemCode ";
            hdrQtyInDb = hdrQtyInDb.add((BigDecimal) balance.get("totalQty"));
            param.put("qtyOut", hdrQtyInDb);
            jdbcTemplate.update(quh, param);
        } catch (EmptyResultDataAccessException exx) {
            String qih = "INSERT INTO T_STOCK_CARD (OUTLET_CODE,TRANS_DATE,ITEM_CODE,ITEM_COST,QTY_BEGINNING,QTY_IN,QTY_OUT,USER_UPD,DATE_UPD,TIME_UPD) VALUES (:outletCode,:transDate,:itemCode,0,0,0,:qtyOut,:userUpd,:dateUpd,:timeUpd) ";
            param.put("qtyOut", (BigDecimal) balance.get("totalQty"));
            jdbcTemplate.update(qih, param);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            existQty = jdbcTemplate.queryForObject(qf, param, BigDecimal.class);
            BigDecimal quantity = (BigDecimal) balance.get("totalQty");
            quantity = quantity.add(existQty);
            param.put("qty", quantity);
            String qu = "UPDATE T_STOCK_CARD_DETAIL"
                    + " SET QUANTITY=:qty, USER_UPD=:userUpd, DATE_UPD=:dateUpd, TIME_UPD=:timeUpd "
                    + " WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :transDate AND item_code= :itemCode AND cd_trans='DLV' ";
            jdbcTemplate.update(qu, param);
        } catch (EmptyResultDataAccessException exx) {
            String qi = "INSERT INTO T_STOCK_CARD_DETAIL (OUTLET_CODE,TRANS_DATE,ITEM_CODE,CD_TRANS,QUANTITY_IN,QUANTITY,USER_UPD,DATE_UPD,TIME_UPD) VALUES (:outletCode, :transDate, :itemCode, 'DLV' , 0, :qty, :userUpd, :dateUpd, :timeUpd)";
            jdbcTemplate.update(qi, param);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Insert MPCS Production - Fathur 8 Jan 2024 //
    // Update for integration to stock card 17 Jan 2024 //
    // Update for fryer type S calculation based on oil usage 20 Feb 2024 //
    @Transactional
    @Override
    public boolean insertMpcsProduction(Map<String, String> params) {

        Map prm = new HashMap();
        prm.put("userUpd", params.get("userUpd"));
        prm.put("dateUpd", params.get("dateUpd"));
        prm.put("timeUpd", params.get("timeUpd"));

        prm.put("remark", params.get("remark"));
        prm.put("recipeCode", params.get("recipeCode"));
        prm.put("qtyMpcs", params.get("qtyMpcs"));
        prm.put("mpcsGroup", params.get("mpcsGroup"));
        prm.put("mpcsDate", params.get("mpcsDate"));
        prm.put("outletCode", params.get("outletCode"));

        prm.put("fryerType", params.getOrDefault("fryerType", " "));
        prm.put("fryerTypeSeq", params.getOrDefault("fryerTypeSeq"," "));

        if (!prm.get("fryerType").equals(" ") || !prm.get("fryerTypeSeq").equals(" ") || !prm.get("fryerType").equals(null) || !prm.get("fryerTypeSeq").equals(null)) {
            if (prm.get("fryerType").equals("S")) {
                String oilItemCode = "06-1002";
                String updateFryer = "Update M_MPCS_DETAIL "
                    + " SET FRYER_TYPE_CNT = (FRYER_TYPE_CNT + (SELECT (QTY_STOCK * :qtyMpcs) as total FROM M_RECIPE_DETAIL where RECIPE_CODE = :recipeCode AND ITEM_CODE = '"+oilItemCode+"' )) " 
                    + " WHERE OUTLET_CODE = :outletCode AND FRYER_TYPE = :fryerType and FRYER_TYPE_SEQ = :fryerTypeSeq ";
                jdbcTemplate.update(updateFryer, prm);
            } else {
                String updateFryer = "Update M_MPCS_DETAIL "
                    + " SET FRYER_TYPE_CNT = (FRYER_TYPE_CNT + (SELECT (sum(QTY_STOCK) * :qtyMpcs) FROM m_recipe_product WHERE RECIPE_CODE = :recipeCode)) " 
                    + " WHERE OUTLET_CODE = :outletCode AND FRYER_TYPE = :fryerType and FRYER_TYPE_SEQ = :fryerTypeSeq ";
                jdbcTemplate.update(updateFryer, prm);
            }
        } 

        String insertProductionQuery = "UPDATE T_SUMM_MPCS SET "
                + "QTY_PROD = (QTY_PROD + (SELECT (sum(QTY_STOCK) * :qtyMpcs) FROM m_recipe_product WHERE RECIPE_CODE = :recipeCode)), "
                + "DESC_PROD = :remark, "
                + "PROD_BY = :userUpd, "
                + "USER_UPD = :userUpd, "
                + "TIME_UPD = :timeUpd, "
                + "DATE_UPD = :dateUpd "
                + "WHERE DATE_MPCS = :mpcsDate AND MPCS_GROUP = :mpcsGroup "
                + "AND SEQ_MPCS = (SELECT tsm.SEQ_MPCS FROM T_SUMM_MPCS tsm WHERE DATE_MPCS = :mpcsDate AND MPCS_GROUP = :mpcsGroup "
                + "AND TIME_MPCS > :timeUpd AND ROWNUM = 1) ";
        jdbcTemplate.update(insertProductionQuery, prm);

        String updateQuantityAccQuery = "MERGE INTO T_SUMM_MPCS tsm "
                + "USING ("
                + "	SELECT SEQ_MPCS, QTY_ACC_PROD, sum(QTY_PROD) OVER (ORDER BY seq_mpcs) AS UPDATED_QTY_ACC_PROD "
                + "		FROM T_SUMM_MPCS tsm "
                + "		WHERE tsm.MPCS_GROUP =:mpcsGroup AND tsm.DATE_MPCS = :mpcsDate "
                + "	) up "
                + "ON (tsm.SEQ_MPCS = up.SEQ_MPCS AND tsm.DATE_MPCS = :mpcsDate AND tsm.MPCS_GROUP = :mpcsGroup) "
                + "WHEN MATCHED THEN "
                + "	UPDATE SET "
                + "		tsm.QTY_ACC_PROD = up.UPDATED_QTY_ACC_PROD, "
                + "		tsm.USER_UPD = :userUpd,"
                + "		tsm.DATE_UPD = :dateUpd,"
                + "		tsm.TIME_UPD = :timeUpd ";

        jdbcTemplate.update(updateQuantityAccQuery, prm);

        String insertHistoryQuery = "INSERT INTO T_MPCS_HIST (HIST_SEQ,HIST_TYPE,OUTLET_CODE,MPCS_DATE,MPCS_GROUP,RECIPE_CODE,FRYER_TYPE,FRYER_TYPE_SEQ,SEQ_MPCS,QUANTITY,USER_UPD,DATE_UPD,TIME_UPD) "
                + "VALUES ((SELECT (NVL(MAX(tmh.HIST_SEQ), 0) + 1)  FROM T_MPCS_HIST tmh),'C',:outletCode, :mpcsDate,:mpcsGroup,:recipeCode, :fryerType, :fryerTypeSeq, (SELECT tsm.SEQ_MPCS FROM T_SUMM_MPCS tsm WHERE DATE_MPCS = :mpcsDate AND MPCS_GROUP = :mpcsGroup "
                + "AND TIME_MPCS > :timeUpd "
                + "AND ROWNUM = 1),:qtyMpcs,:userUpd, :dateUpd, :timeUpd) ";
        jdbcTemplate.update(insertHistoryQuery, prm);

        String insertMpcsDetailQuery = "INSERT INTO T_MPCS_DETAIL (OUTLET_CODE,DATE_MPCS,TIME_MPCS,RECIPE_CODE,TYPE_MPCS,ITEM_CODE,QTY1,UOM1,QTY2,UOM2,STATUS,USER_UPD,DATE_UPD,TIME_UPD) ("
                + "	SELECT :outletCode AS OUTLET_CODE, :mpcsDate AS DATE_MPCS, :timeUpd AS TIME_MPCS, RECIPE_CODE, :mpcsGroup AS TYPE_MPCS, ITEM_CODE, -(QTY_STOCK * :qtyMpcs) AS QTY1, UOM_STOCK AS UOM1, -(QTY_STOCK * :qtyMpcs) AS QTY2, UOM_STOCK AS UOM2, '1' AS STATUS,  :userUpd AS USER_UPD, :dateUpd AS DATE_UPD, :timeUpd AS TIME_UPD FROM M_RECIPE_DETAIL where RECIPE_CODE = :recipeCode "
                + "	UNION "
                + "	SELECT :outletCode AS OUTLET_CODE, :mpcsDate  AS DATE_MPCS, :timeUpd AS TIME_MPCS, RECIPE_CODE, :mpcsGroup AS TYPE_MPCS, PRODUCT_CODE as ITEM_CODE,  (QTY_STOCK * :qtyMpcs) AS QTY1, UOM_STOCK AS UOM1, (QTY_STOCK * :qtyMpcs) AS QTY2, UOM_STOCK AS UOM2, '1' AS STATUS, :userUpd AS USER_UPD, :dateUpd AS DATE_UPD, :timeUpd AS TIME_UPD  FROM m_recipe_product where recipe_code = :recipeCode "
                + ") ";
        jdbcTemplate.update(insertMpcsDetailQuery, prm);

        String itemNeedToUpdateInStockCard = "SELECT 'Y' AS IS_RECIPE, 'N' AS IS_PRODUCT, ITEM_CODE AS PRODUCT_CODE, QTY_STOCK FROM M_RECIPE_DETAIL WHERE RECIPE_CODE = :recipeCode UNION all SELECT 'N' AS IS_RECIPE, 'Y' AS IS_PRODUCT, product_code AS PRODUCT_CODE, QTY_STOCK FROM m_recipe_product WHERE RECIPE_CODE = :recipeCode";
        List<Map<String, Object>> rowsNeedToUpdateToStockCardDetail = jdbcTemplate.query(itemNeedToUpdateInStockCard, prm, new DynamicRowMapper());

        for (Map<String, Object> row : rowsNeedToUpdateToStockCardDetail) {
            Map<String, Object> newParam = new HashMap<>();
            newParam.put("itemCode", row.get("productCode"));
            newParam.put("outletCode", prm.get("outletCode"));
            newParam.put("isRecipe", row.get("isRecipe"));
            newParam.put("isProduct", row.get("isProduct"));
            newParam.put("qtyStock", row.get("qtyStock"));
            newParam.put("qtyMpcs", prm.get("qtyMpcs"));
            newParam.put("mpcsDate", prm.get("mpcsDate"));
            newParam.put("userUpd", prm.get("userUpd"));
            newParam.put("dateUpd", prm.get("dateUpd"));
            newParam.put("timeUpd", prm.get("timeUpd"));

            BigDecimal qtyStock = (BigDecimal) newParam.get("qtyStock");
            BigDecimal qtyMpcs = new BigDecimal(newParam.get("qtyMpcs").toString());
            BigDecimal totalQty = qtyMpcs.multiply(qtyStock);
            newParam.put("totalQty", totalQty);

            if (newParam.get("isProduct").equals("Y")) {
                updateInsertStockCard_in(newParam);
            } else {
                System.out.print("newParam " + newParam);
                updateInsertStockCard_out(newParam);
            }
        }
        return true;
    }

    // Update stock card detail from mpcs production
    public void updateInsertStockCard_in(Map<String, Object> param) {
        String checkExistDtlQuery = "SELECT count(*) FROM T_STOCK_CARD_DETAIL t "
                + "WHERE t.OUTLET_CODE = :outletCode "
                + "AND t.TRANS_DATE = :mpcsDate "
                + "AND t.ITEM_CODE = :itemCode "
                + "AND t.CD_TRANS = 'PRD' ";

        Integer countExistDtl = jdbcTemplate.queryForObject(checkExistDtlQuery, param, Integer.class);
        if (countExistDtl > 0) {
            String updateStockCardDetail = "UPDATE T_STOCK_CARD_DETAIL t "
                    + "SET t.QUANTITY_IN = (t.QUANTITY_IN + :totalQty), "
                    + "TIME_UPD = :timeUpd, "
                    + "DATE_UPD = :dateUpd, "
                    + "USER_UPD = :userUpd "
                    + "WHERE t.OUTLET_CODE = :outletCode "
                    + "AND t.TRANS_DATE = :mpcsDate "
                    + "AND t.ITEM_CODE = :itemCode "
                    + "AND t.CD_TRANS = 'PRD' ";
            jdbcTemplate.update(updateStockCardDetail, param);
        } else {
            String insertStockCardDetail = "INSERT INTO T_STOCK_CARD_DETAIL "
                    + "(OUTLET_CODE,TRANS_DATE,ITEM_CODE,CD_TRANS,QUANTITY_IN,QUANTITY,USER_UPD,DATE_UPD,TIME_UPD) VALUES "
                    + "(:outletCode,:mpcsDate,:itemCode,'PRD',:totalQty,0,:userUpd ,:dateUpd,:timeUpd) ";

            jdbcTemplate.update(insertStockCardDetail, param);
        }

        // Insert Update stock card header from mpcs production
        String checkExistHeaderQuery = "SELECT COUNT(*) FROM T_STOCK_CARD WHERE TRANS_DATE = :mpcsDate AND ITEM_CODE = :itemCode ";
        Integer countExistHeader = jdbcTemplate.queryForObject(checkExistHeaderQuery, param, Integer.class);

        if (countExistHeader > 0) {
            String updateStockCardHeader = "UPDATE T_STOCK_CARD SET "
                    + "QTY_IN = (QTY_IN  + :totalQty), "
                    + "USER_UPD = :userUpd, "
                    + "DATE_UPD = :dateUpd, "
                    + "TIME_UPD = :timeUpd "
                    + "WHERE OUTLET_CODE = :outletCode AND ITEM_CODE = :itemCode and TRANS_DATE = :mpcsDate ";
            jdbcTemplate.update(updateStockCardHeader, param);

        } else {
            String insertStockCardHeader = "INSERT INTO T_STOCK_CARD "
                    + "(OUTLET_CODE,TRANS_DATE,ITEM_CODE,ITEM_COST,QTY_BEGINNING,QTY_IN,QTY_OUT,REMARK,USER_UPD,DATE_UPD,TIME_UPD) VALUES "
                    + "(:outletCode, :mpcsDate, :itemCode, 0, 0, :totalQty, 0, ' ', :userUpd, :dateUpd, :timeUpd)";
            jdbcTemplate.update(insertStockCardHeader, param);
        }
    }

    public void updateInsertStockCard_out(Map<String, Object> param) {
        // Insert Update stock card detail from mpcs production
        String checkExistDtlQuery = "SELECT count(*) FROM T_STOCK_CARD_DETAIL t "
                + "WHERE t.OUTLET_CODE = :outletCode "
                + "AND t.TRANS_DATE = :mpcsDate "
                + "AND t.ITEM_CODE = :itemCode "
                + "AND t.CD_TRANS = 'PRD' ";

        Integer countExistDtl = jdbcTemplate.queryForObject(checkExistDtlQuery, param, Integer.class);
        if (countExistDtl > 0) {
            String updateStockCardDetail = "UPDATE T_STOCK_CARD_DETAIL t "
                    + "SET t.QUANTITY = (t.QUANTITY + :totalQty), "
                    + "TIME_UPD = :timeUpd, "
                    + "DATE_UPD = :dateUpd, "
                    + "USER_UPD = :userUpd "
                    + "WHERE t.OUTLET_CODE = :outletCode "
                    + "AND t.TRANS_DATE = :mpcsDate "
                    + "AND t.ITEM_CODE = :itemCode "
                    + "AND t.CD_TRANS = 'PRD' ";
            jdbcTemplate.update(updateStockCardDetail, param);
        } else {
            String insertStockCardDetail = "INSERT INTO T_STOCK_CARD_DETAIL "
                    + "(OUTLET_CODE,TRANS_DATE,ITEM_CODE,CD_TRANS,QUANTITY_IN,QUANTITY,USER_UPD,DATE_UPD,TIME_UPD) VALUES "
                    + "(:outletCode,:mpcsDate,:itemCode,'PRD',0,:totalQty,:userUpd ,:dateUpd,:timeUpd) ";
            jdbcTemplate.update(insertStockCardDetail, param);
        }

        // Insert Update stock card header from mpcs production
        String checkExistHeaderQuery = "SELECT COUNT(*) FROM T_STOCK_CARD WHERE TRANS_DATE = :mpcsDate AND ITEM_CODE = :itemCode ";
        Integer countExistHeader = jdbcTemplate.queryForObject(checkExistHeaderQuery, param, Integer.class);

        if (countExistHeader > 0) {
            String updateStockCardHeader = "UPDATE T_STOCK_CARD SET "
                    + "QTY_OUT = (QTY_OUT + :totalQty), "
                    + "USER_UPD = :userUpd, "
                    + "DATE_UPD = :dateUpd, "
                    + "TIME_UPD = :timeUpd "
                    + "WHERE OUTLET_CODE = :outletCode AND ITEM_CODE = :itemCode and TRANS_DATE = :mpcsDate ";
            jdbcTemplate.update(updateStockCardHeader, param);

        } else {
            String insertStockCardHeader = "INSERT INTO T_STOCK_CARD "
                    + "(OUTLET_CODE,TRANS_DATE,ITEM_CODE,ITEM_COST,QTY_BEGINNING,QTY_IN,QTY_OUT,REMARK,USER_UPD,DATE_UPD,TIME_UPD) VALUES "
                    + "(:outletCode, :mpcsDate, :itemCode, 0, 0, 0, :totalQty, ' ', :userUpd, :dateUpd,  :timeUpd ) ";
            jdbcTemplate.update(insertStockCardHeader, param);
        }
    }
    // Done insert MPCS Production // 

    // Delete MPCS Production by Fathur 11 Jan 2024 //
    // Update for integration to stock card 17 Jan 2024 //
    // Update for Delete Validation 16 Feb 20024 //
    @Transactional
    @Override
    public ResponseMessage deleteMpcsProduction(Map<String, String> params) {
        ResponseMessage rm = new ResponseMessage();

        Map prm = new HashMap();
        prm.put("userUpd", params.get("userUpd"));
        prm.put("dateUpd", params.get("dateUpd"));
        prm.put("timeUpd", params.get("timeUpd"));

        prm.put("remark", params.get("remark"));
        prm.put("qtyMpcs", params.get("qtyMpcs"));
        prm.put("mpcsGroup", params.get("mpcsGroup"));
        prm.put("mpcsDate", params.get("mpcsDate"));
        prm.put("outletCode", params.get("outletCode"));
        prm.put("seqMpcs", params.get("seqMpcs"));
        prm.put("histSeq", params.get("histSeq"));
        String maxMinutesvalidation = "60";

        String selectedProductionTime = jdbcTemplate.queryForObject("SELECT time_upd FROM T_MPCS_HIST WHERE HIST_SEQ = :histSeq", prm, String.class);

        String timeValidationQuery = "SELECT CASE WHEN TO_TIMESTAMP(TO_CHAR(SYSDATE, 'YYYYMMDD') || '"+selectedProductionTime+"', 'YYYYMMDDHH24MISS') + INTERVAL '"+maxMinutesvalidation+"' MINUTE >= SYSDATE THEN 'Y' ELSE 'N' END AS ALLOW_DELETE FROM dual ";
        String allowDelete = jdbcTemplate.queryForObject(timeValidationQuery, prm, String.class);
        System.out.println("allowDelete: " +allowDelete);

        if (allowDelete.equals("N")) {
            rm.setSuccess(false);
            rm.setMessage("Tidak dapat menghapus data produksi lebih dari "+maxMinutesvalidation+" menit yang lalu");
            rm.setItem(null);
            return rm;
        }
        
        prm.put("fryerType", params.getOrDefault("fryerType", " "));
        prm.put("fryerTypeSeq", params.getOrDefault("fryerTypeSeq"," "));

        if (!prm.get("fryerType").equals(" ") || !prm.get("fryerTypeSeq").equals(" ") || !prm.get("fryerType").equals(null) || !prm.get("fryerTypeSeq").equals(null)) {
            if (prm.get("fryerType").equals("S")) {
                String oilItemCode = "06-1002";
                String updateFryer = "Update M_MPCS_DETAIL "
                    + " SET FRYER_TYPE_CNT = (FRYER_TYPE_CNT - (SELECT (QTY_STOCK * :qtyMpcs) as total FROM M_RECIPE_DETAIL where RECIPE_CODE = (SELECT RECIPE_CODE FROM M_RECIPE_HEADER mrh WHERE MPCS_GROUP = :mpcsGroup) AND ITEM_CODE = '"+oilItemCode+"' )) " 
                    + " WHERE OUTLET_CODE = :outletCode AND FRYER_TYPE = :fryerType and FRYER_TYPE_SEQ = :fryerTypeSeq ";
                jdbcTemplate.update(updateFryer, prm);
            } else {
                String updateFryer = "Update M_MPCS_DETAIL "
                    + " SET FRYER_TYPE_CNT = (FRYER_TYPE_CNT - (SELECT (sum(QTY_STOCK) * :qtyMpcs) FROM m_recipe_product WHERE RECIPE_CODE = (SELECT RECIPE_CODE FROM M_RECIPE_HEADER mrh WHERE MPCS_GROUP = :mpcsGroup))) " 
                    + " WHERE OUTLET_CODE = :outletCode AND FRYER_TYPE = :fryerType and FRYER_TYPE_SEQ = :fryerTypeSeq ";
                jdbcTemplate.update(updateFryer, prm);
            }
        } 
        
        String updateQtyQuery = "UPDATE T_SUMM_MPCS "
                + "SET QTY_PROD = (QTY_PROD - (SELECT (sum(QTY_STOCK) * :qtyMpcs) FROM m_recipe_product WHERE RECIPE_CODE = (SELECT RECIPE_CODE FROM M_RECIPE_HEADER mrh WHERE MPCS_GROUP = :mpcsGroup))), "
                + "PROD_BY = :userUpd, "
                + "USER_UPD = :userUpd, "
                + "TIME_UPD = :timeUpd, "
                + "DATE_UPD = :dateUpd "
                + "WHERE DATE_MPCS = :mpcsDate "
                + "AND MPCS_GROUP = :mpcsGroup "
                + "AND SEQ_MPCS = :seqMpcs ";
        jdbcTemplate.update(updateQtyQuery, prm);

        String updateQuantityAccQuery = "MERGE INTO T_SUMM_MPCS tsm "
                + "USING ("
                + "	SELECT SEQ_MPCS, QTY_ACC_PROD, sum(QTY_PROD) OVER (ORDER BY seq_mpcs) AS UPDATED_QTY_ACC_PROD "
                + "		FROM T_SUMM_MPCS tsm "
                + "		WHERE tsm.MPCS_GROUP =:mpcsGroup AND tsm.DATE_MPCS = :mpcsDate "
                + "	) up "
                + "ON (tsm.SEQ_MPCS = up.SEQ_MPCS AND tsm.DATE_MPCS = :mpcsDate AND tsm.MPCS_GROUP = :mpcsGroup) "
                + "WHEN MATCHED THEN "
                + "	UPDATE SET "
                + "		tsm.QTY_ACC_PROD = up.UPDATED_QTY_ACC_PROD, "
                + "		tsm.USER_UPD = :userUpd, "
                + "		tsm.DATE_UPD = :dateUpd, "
                + "		tsm.TIME_UPD = :timeUpd ";
        jdbcTemplate.update(updateQuantityAccQuery, prm);

        String updateHistoryQuery = "UPDATE T_MPCS_HIST tmh "
                + "SET FRYER_TYPE = 'D', "
                + "TIME_UPD = :timeUpd, "
                + "DATE_UPD = :dateUpd "
                + "WHERE tmh.HIST_SEQ = :histSeq AND OUTLET_CODE = :outletCode AND MPCS_GROUP = :mpcsGroup AND SEQ_MPCS = :seqMpcs AND QUANTITY = :qtyMpcs ";
        jdbcTemplate.update(updateHistoryQuery, prm);

        String insertMpcsDetailQuery = "INSERT INTO T_MPCS_DETAIL (OUTLET_CODE,DATE_MPCS,TIME_MPCS,RECIPE_CODE,TYPE_MPCS,ITEM_CODE,QTY1,UOM1,QTY2,UOM2,STATUS,USER_UPD,DATE_UPD,TIME_UPD) ( "
                + "	SELECT :outletCode AS OUTLET_CODE, :mpcsDate AS DATE_MPCS, :timeUpd AS TIME_MPCS, RECIPE_CODE, :mpcsGroup AS TYPE_MPCS, ITEM_CODE, (QTY_STOCK * :qtyMpcs) AS QTY1, UOM_STOCK AS UOM1, (QTY_STOCK * :qtyMpcs) AS QTY2, UOM_STOCK AS UOM2, '1' AS STATUS,  :userUpd AS USER_UPD, :dateUpd AS DATE_UPD, :timeUpd AS TIME_UPD "
                + "	FROM M_RECIPE_DETAIL where RECIPE_CODE = (SELECT RECIPE_CODE FROM M_RECIPE_HEADER mrh WHERE MPCS_GROUP = :mpcsGroup) "
                + "	UNION "
                + "	SELECT :outletCode AS OUTLET_CODE, :mpcsDate  AS DATE_MPCS, :timeUpd AS TIME_MPCS, RECIPE_CODE, :mpcsGroup AS TYPE_MPCS, PRODUCT_CODE as ITEM_CODE,  -(QTY_STOCK * :qtyMpcs) AS QTY1, UOM_STOCK AS UOM1, -(QTY_STOCK * :qtyMpcs) AS QTY2, UOM_STOCK AS UOM2, '1' AS STATUS, :userUpd AS USER_UPD, :dateUpd AS DATE_UPD, :timeUpd AS TIME_UPD  "
                + "	FROM m_recipe_product where recipe_code = (SELECT RECIPE_CODE FROM M_RECIPE_HEADER mrh WHERE MPCS_GROUP = :mpcsGroup)) ";
        jdbcTemplate.update(insertMpcsDetailQuery, prm);

        String itemNeedToUpdateInStockCard = "SELECT 'Y' AS IS_RECIPE, 'N' AS IS_PRODUCT, ITEM_CODE AS PRODUCT_CODE, QTY_STOCK FROM M_RECIPE_DETAIL WHERE RECIPE_CODE = (SELECT RECIPE_CODE FROM M_RECIPE_HEADER mrh WHERE MPCS_GROUP = :mpcsGroup) UNION all SELECT 'N' AS IS_RECIPE, 'Y' AS IS_PRODUCT, product_code AS PRODUCT_CODE, QTY_STOCK FROM m_recipe_product WHERE RECIPE_CODE = (SELECT RECIPE_CODE FROM M_RECIPE_HEADER mrh WHERE MPCS_GROUP = :mpcsGroup)";
        List<Map<String, Object>> rowsNeedToUpdateToStockCardDetail = jdbcTemplate.query(itemNeedToUpdateInStockCard, prm, new DynamicRowMapper());

        Integer checkTableCount =jdbcTemplate.queryForObject("SELECT count(*) FROM all_tables WHERE table_name = 'T_HIST_DEL_PRD'", prm, Integer.class);
        if (checkTableCount > 0) {
            String insertIntoMpcsDelHistoryQuery = "INSERT INTO T_HIST_DEL_PRD (MPCS_GROUP,DATE_MPCS,QTY,UOM,TIME_MPCS,ITEM_CODE) "
                + "SELECT :mpcsGroup AS mpcs_group, :mpcsDate AS mpcs_date, -(QTY_STOCK * :qtyMpcs) AS QTY, UOM_STOCK AS UOM, :timeUpd AS TIME_MPCS, PRODUCT_CODE AS ITEM_CODE FROM M_RECIPE_PRODUCT mrp WHERE mrp.RECIPE_CODE = (SELECT RECIPE_CODE FROM M_RECIPE_HEADER mrh WHERE MPCS_GROUP = :mpcsGroup) ";
            jdbcTemplate.update(insertIntoMpcsDelHistoryQuery, prm);
        }

        for (Map<String, Object> row : rowsNeedToUpdateToStockCardDetail) {
            Map<String, Object> newParam = new HashMap<>();
            newParam.put("itemCode", row.get("productCode"));
            newParam.put("outletCode", prm.get("outletCode"));
            newParam.put("isRecipe", row.get("isRecipe"));
            newParam.put("isProduct", row.get("isProduct"));
            newParam.put("qtyStock", row.get("qtyStock"));
            newParam.put("qtyMpcs", prm.get("qtyMpcs"));
            newParam.put("mpcsDate", prm.get("mpcsDate"));
            newParam.put("userUpd", prm.get("userUpd"));
            newParam.put("dateUpd", prm.get("dateUpd"));
            newParam.put("timeUpd", prm.get("timeUpd"));

            BigDecimal qtyStock = (BigDecimal) newParam.get("qtyStock");
            BigDecimal qtyMpcs = new BigDecimal(newParam.get("qtyMpcs").toString());
            BigDecimal totalQty = qtyMpcs.multiply(qtyStock);
            newParam.put("totalQty", totalQty);

            if (newParam.get("isProduct").equals("Y")) {
                updateInsertStockCard_out_delete(newParam);
            } else {
                updateInsertStockCard_in_delete(newParam);
            }
        }
        rm.setSuccess(true);
        rm.setMessage("Succesfully delete mpcs production");
        rm.setItem(null);
        return rm;
    }

    // Update stock card detail from mpcs production
    public void updateInsertStockCard_out_delete(Map<String, Object> param) {
        String deleteTransType = "DEL";
        String checkIsExist = "SELECT COUNT(*) FROM  T_STOCK_CARD_DETAIL t "
                + "WHERE t.OUTLET_CODE = :outletCode "
                + "AND t.TRANS_DATE = :mpcsDate "
                + "AND t.ITEM_CODE = :itemCode "
                + "AND t.CD_TRANS = '"+deleteTransType+"' ";

        Integer rowCount = jdbcTemplate.queryForObject(checkIsExist, param, Integer.class);
        if (rowCount > 0) {
            String updateStockCardDetail = "UPDATE T_STOCK_CARD_DETAIL t "
                + "SET t.QUANTITY = (t.QUANTITY + :totalQty), "
                + "TIME_UPD = :timeUpd, "
                + "DATE_UPD = :dateUpd, "
                + "USER_UPD = :userUpd "
                + "WHERE t.OUTLET_CODE = :outletCode "
                + "AND t.TRANS_DATE = :mpcsDate "
                + "AND t.ITEM_CODE = :itemCode "
                + "AND t.CD_TRANS = '"+deleteTransType+"' ";
            jdbcTemplate.update(updateStockCardDetail, param);
        } else {
            String insertStockCardDetail = "INSERT INTO T_STOCK_CARD_DETAIL "
                + "(OUTLET_CODE,TRANS_DATE,ITEM_CODE,CD_TRANS,QUANTITY_IN,QUANTITY,USER_UPD,DATE_UPD,TIME_UPD) "
                + "VALUES (:outletCode, :mpcsDate, :itemCode, '"+deleteTransType+"', 0, :totalQty, :userUpd, :dateUpd, :timeUpd) ";
            jdbcTemplate.update(insertStockCardDetail, param);
        }

        // Insert Update stock card header from mpcs production
        String updateStockCardHeader = "UPDATE T_STOCK_CARD SET "
                + "QTY_OUT = (QTY_OUT  + :totalQty), "
                + "USER_UPD = :userUpd, "
                + "DATE_UPD = :dateUpd, "
                + "TIME_UPD = :timeUpd "
                + "WHERE OUTLET_CODE = :outletCode AND ITEM_CODE = :itemCode and TRANS_DATE = :mpcsDate ";
        jdbcTemplate.update(updateStockCardHeader, param);
    }

    public void updateInsertStockCard_in_delete(Map<String, Object> param) {
        String deleteTransType = "DEL";
        String checkIsExist = "SELECT COUNT(*) FROM  T_STOCK_CARD_DETAIL t "
                + "WHERE t.OUTLET_CODE = :outletCode "
                + "AND t.TRANS_DATE = :mpcsDate "
                + "AND t.ITEM_CODE = :itemCode "
                + "AND t.CD_TRANS = '"+deleteTransType+"' ";

        Integer rowCount = jdbcTemplate.queryForObject(checkIsExist, param, Integer.class);

        if (rowCount > 0) {
            // Update stock card detail from mpcs production
            String updateStockCardDetail = "UPDATE T_STOCK_CARD_DETAIL t "
                + "SET t.QUANTITY_IN = (t.QUANTITY_IN - :totalQty), "
                + "TIME_UPD = :timeUpd, "
                + "DATE_UPD = :dateUpd, "
                + "USER_UPD = :userUpd "
                + "WHERE t.OUTLET_CODE = :outletCode "
                + "AND t.TRANS_DATE = :mpcsDate "
                + "AND t.ITEM_CODE = :itemCode "
                + "AND t.CD_TRANS = '"+deleteTransType+"' ";
            jdbcTemplate.update(updateStockCardDetail, param);
        } else {
            String insertStockCardDetail = "INSERT INTO T_STOCK_CARD_DETAIL "
                + "(OUTLET_CODE,TRANS_DATE,ITEM_CODE,CD_TRANS,QUANTITY_IN,QUANTITY,USER_UPD,DATE_UPD,TIME_UPD) "
                + "VALUES (:outletCode, :mpcsDate, :itemCode, '"+deleteTransType+"', :totalQty, 0, :userUpd, :dateUpd, :timeUpd) ";
            jdbcTemplate.update(insertStockCardDetail, param);
        }

        // Update stock card header from mpcs production
        String updateStockCardHeader = "UPDATE T_STOCK_CARD SET "
                + "QTY_IN = (QTY_IN + :totalQty), "
                + "USER_UPD = :userUpd, "
                + "DATE_UPD = :dateUpd, "
                + "TIME_UPD = :timeUpd "
                + "WHERE OUTLET_CODE = :outletCode AND ITEM_CODE = :itemCode and TRANS_DATE = :mpcsDate ";
        jdbcTemplate.update(updateStockCardHeader, param);
    }
    // Done delete MPCS Production //

    // Add Counter Print Receiving Dani 11 Jan 2024
    @Override
    public void addCounterPrintReceiving(Map<String, Object> param) {
        String query = "UPDATE T_RECV_HEADER a SET a.NO_OF_PRINT  = NO_OF_PRINT+1 WHERE a.ORDER_NO = :noOrder AND a.RECV_NO = :recvNo";
        jdbcTemplate.update(query, param);
    }

    // Add Counter Print Order Entry adit 16 Jan 2024
    @Override
    public void addCounterPrintOrderEntry(Map<String, Object> param) {
        String query = "UPDATE T_ORDER_HEADER a SET a.NO_OF_PRINT  = a.NO_OF_PRINT+1 WHERE a.ORDER_NO = :orderNo";
        jdbcTemplate.update(query, param);
    }

    // Add menyimpan data user absensi by id by M Joko 16 Jan 2024
    @Override
    public boolean insertAbsensi(Map<String, Object> param) {
        String query = "SELECT * FROM M_STAFF ms WHERE ms.STAFF_CODE = :staffCode AND ms.PASSWORD = :password";
        List list = jdbcTemplate.query(query, param, new DynamicRowMapper());
        if (list.isEmpty()) {
            return false;
        }

        String queryUpd = """
            INSERT INTO T_ABSENSI (
              OUTLET_CODE, DAY_SEQ, STAFF_ID, SEQ_NO, 
              DATE_ABSEN, TIME_ABSEN, STATUS
            ) 
            SELECT 
              * 
            FROM 
              (
                SELECT 
                  OUTLET_CODE, 
                  CASE WHEN NVL(SEQ_NO, 2) = 2 THEN NVL(DAY_SEQ, 0) + 1 ELSE NVL(DAY_SEQ, 1) END AS DAY_SEQ, 
                  STAFF_ID, 
                  CASE WHEN NVL(SEQ_NO, 1) = 1 THEN 2 ELSE 1 END AS SEQ_NO, 
                  TO_CHAR(SYSTIMESTAMP, 'DD-MON-YYYY') AS DATE_ABSEN, 
                  TO_CHAR(SYSTIMESTAMP, 'HH24MISS') AS TIME_ABSEN, 
                  'A' AS STATUS 
                FROM 
                  (
                    SELECT 
                      * 
                    FROM 
                      T_ABSENSI ta 
                    WHERE 
                      ta.STAFF_ID = :staffCode 
                    ORDER BY 
                      ta.DAY_SEQ DESC, ta.SEQ_NO DESC
                  ) 
                WHERE 
                  ROWNUM = 1 
                UNION ALL 
                SELECT 
                  :outletCode AS OUTLET_CODE, 
                  COALESCE(
                    MAX(DAY_SEQ) + 1, 
                    1
                  ) AS DAY_SEQ, 
                  :staffCode AS STAFF_ID, 
                  COALESCE(
                    MAX(SEQ_NO) + 1, 
                    1
                  ) AS SEQ_NO, 
                  TO_CHAR(SYSTIMESTAMP, 'DD-MON-YYYY') AS DATE_ABSEN, 
                  TO_CHAR(SYSTIMESTAMP, 'HH24MISS') AS TIME_ABSEN, 
                  'A' AS STATUS 
                FROM 
                  T_ABSENSI 
                WHERE 
                  STAFF_ID = :staffCode 
                  AND DATE_ABSEN = TO_CHAR(SYSTIMESTAMP, 'DD-MON-YYYY') 
                  AND NOT EXISTS (
                    SELECT 
                      1 
                    FROM 
                      T_ABSENSI ta 
                    WHERE 
                      ta.STAFF_ID = :staffCode 
                  )
              ) 
            WHERE 
              rownum = 1
                       """;
        try {
            jdbcTemplate.update(queryUpd, param);
        } catch (DataAccessException e) {
            System.err.println("err insertAbsensi: " + e.getMessage());
            return false;
        }
        return true;
    }

    //////// new Method Insert MPCS Management Fryer aditya 29 Jan 2024
    @Override
    public void insertMpcsManagementFryer(JsonObject param) {
        
        Map balance = new HashMap();
        balance.put("outletCode", param.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
        LocalDate transDate = this.jdbcTemplate.queryForObject("SELECT TRANS_DATE FROM M_OUTLET WHERE OUTLET_CODE = :outletCode", balance, LocalDate.class);
        String month = String.format("%02d", transDate.getMonthValue());
        String year = String.valueOf(transDate.getYear());

        String noProcess = MpcsManagementFryerCounter(year, month, "FRY", param.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());

        String queryHeader = "INSERT INTO T_MPCS_MANAGEMENT (OUTLET_CODE, PROCESS_NO, NOTES, FRYER_NO, FRYER_TYPE,"
                + " OIL_USE, PRECENTAGE, USER_UPD, DATE_UPD, TIME_UPD) VALUES (:outletCode, :processNo, :notes,"
                + " :fryerNo, :fryerType, :oilUse, :precentage, :userUpd, :dateUpd, :timeUpd)";

        Map<String, Object> prm = new HashMap<>();
        prm.put("outletCode", param.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
        prm.put("processNo", noProcess);
        prm.put("notes", param.getAsJsonObject().getAsJsonPrimitive("notes").getAsString());
        prm.put("fryerNo", param.getAsJsonObject().getAsJsonPrimitive("fryerNo").getAsString());
        prm.put("fryerType", param.getAsJsonObject().getAsJsonPrimitive("fryerType").getAsString());
        prm.put("oilUse", param.getAsJsonObject().getAsJsonPrimitive("oilUse").getAsString());
        prm.put("precentage", param.getAsJsonObject().getAsJsonPrimitive("precentage").getAsString());
        prm.put("userUpd", param.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString());
        prm.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        prm.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(queryHeader, prm);

        resetFryerCounter(param);
    }

    public void resetFryerCounter(JsonObject param) {
        String queryHeader = "UPDATE M_MPCS_DETAIL SET FRYER_TYPE_CNT=0, USER_UPD=:userUpd, DATE_UPD=TO_CHAR(SYSDATE, 'DD MON YYYY') , TIME_UPD=TO_CHAR(SYSDATE, 'HH24MMII'), FRYER_TYPE_SEQ_CNT=FRYER_TYPE_SEQ_CNT+1 WHERE FRYER_TYPE_SEQ=:fryerNo AND FRYER_TYPE=:fryerType";

        Map<String, Object> prm = new HashMap<>();
        prm.put("fryerNo", param.getAsJsonObject().getAsJsonPrimitive("fryerNo").getAsString());
        prm.put("fryerType", param.getAsJsonObject().getAsJsonPrimitive("fryerType").getAsString());
        prm.put("userUpd", param.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString());
        jdbcTemplate.update(queryHeader, prm);
    }
    //////// new Method Updated M Counter Management Fryer aditya 20 Feb 2024
     public String MpcsManagementFryerCounter(String year, String month, String transType, String outletCode) {
        String sql = "select to_char(counter, 'fm0000') as no_urut from ( "
                + "select max(counter_no) + 1 as counter from m_counter "
                + "where outlet_code = :outletCode and trans_type = :transType and year = :year and month = to_number(:month) "
                + ") tbl";
        System.err.println("Query for No Urut :" + sql);
        Map param = new HashMap();
        param.put("year", year);
        param.put("month", month);
        param.put("transType", transType);
        param.put("outletCode", outletCode);
        String noUrut = jdbcTemplate.queryForObject(sql, param, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("no_urut") == null ? "0" : rs.getString("no_urut");

            }
        }).toString();
        if (noUrut.equalsIgnoreCase("0")) {
            sql = "insert into m_counter(outlet_code, trans_type, year, month, counter_no) "
                    + "values (:outletCode, :transType, :year, :month, 1)";
            System.err.println("Query for NEW No Urut :" + sql);
            Map paramIns = new HashMap();
            paramIns.put("year", year);
            paramIns.put("month", month);
            paramIns.put("transType", transType);
            paramIns.put("outletCode", outletCode);
            jdbcTemplate.update(sql, paramIns);
            noUrut = transType.concat(outletCode).concat(year.substring(2)).concat(month).concat("0001");
        } else {
            sql = "update m_counter set counter_no = counter_no + 1 "
                    + "where outlet_code = :outletCode and trans_type = :transType and year = :year and month = :month";
            System.err.println("Query for Update Counter :" + sql);
            Map paramIns = new HashMap();
            paramIns.put("year", year);
            paramIns.put("month", month);
            paramIns.put("transType", transType);
            paramIns.put("outletCode", outletCode);
            jdbcTemplate.update(sql, paramIns);
            noUrut = transType.concat(outletCode).concat(year.substring(2)).concat(month).concat(noUrut);
        }
        System.err.println("No Urut :" + noUrut);
        return noUrut;
    }

    // =========== New Method List Transfer Data From M Joko 6 Feb 2024 ===========
    @Override
    public ResponseMessage listTransferData(Map<String, Object> mapping) {
        ResponseMessage rm = new ResponseMessage();
        rm.setSuccess(false);
        List<Map<String, Object>> list = new ArrayList();
        String date = mapping.get("date").toString();
        List<String> tables = (List<String>) mapping.getOrDefault("listTable", new ArrayList<String>());
        System.out.println("listTransferData type: " + mapping.get("type"));
        System.out.println("listTransferData tables: " + tables.size());
        if("TERIMA DATA MASTER".equals(mapping.get("type"))){
            try {
                for (String tableName : tables) {
                    Gson gson = new Gson();
                    Map<String, Object> map1;
                    CloseableHttpClient client = HttpClients.createDefault();
                    String url = urlMaster + "/get-data";
                    HttpGet getData = new HttpGet(url);
                    String outletId = mapping.get("outletCode").toString();
                    URI uri = new URIBuilder(getData.getURI()).addParameter("param", tableName).addParameter("date", date).addParameter("outletId", outletId).build();
                    getData.setURI(uri);
                    CloseableHttpResponse response = client.execute(getData);
                    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while (null != (line = br.readLine())) {
                        content.append(line);
                    }
                    String result = content.toString();
                    map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
                    }.getType());
                    List<Map<String, Object>> listItem = (List<Map<String, Object>>) map1.get("item");
                    if (listItem != null && !listItem.isEmpty()) {
                        Map<String, Object> mapq = new HashMap();
                        // Rubah nama tabel ke alias nya
                        Optional<TableAlias> tbl = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_M,"table",tableName);
                        String aliasedTableName = tableName;
                        if(tbl.isPresent()){
                            aliasedTableName = tbl.get().getAlias();
                        }
                        mapq.put(aliasedTableName, listItem);
                        list.add(mapq);
                        System.err.println("listTransferData "+aliasedTableName+":" + listItem.size());
                    }
                }
                rm.setItem(list);
                rm.setSuccess(true);
                rm.setMessage("Success get list.");
            } catch (IOException | URISyntaxException ex) {
                if(ex.getMessage().contains("Connection refused:")){
                    rm.setMessage("Failed get list: Connection to HQ refused.");
                } else {
                    rm.setMessage("Failed get list: " + ex.getMessage());
                }
                
                Logger.getLogger(ProcessDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if("KIRIM DATA TRANSAKSI".equals(mapping.get("type"))){
            for (String table : tables) {
                String conditionText = conditionTextTransfer(table, date);
                String query = "SELECT * FROM " + table + conditionText;
                Map prm = new HashMap();
                List<Map<String, Object>> listItem = jdbcTemplate.query(query, prm, (ResultSet rs, int index) -> convertObject(rs));
                if (listItem != null && !listItem.isEmpty()) {
                    Map<String, Object> mapq = new HashMap();
                    // Rubah nama tabel ke alias nya
                    Optional<TableAlias> ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_T, "table", table);
                    TableAlias tableAlias = ta.get();
                    mapq.put(tableAlias.getAlias(), listItem);
                    list.add(mapq);
                }
            }
            rm.setItem(list);
            rm.setSuccess(true);
            rm.setMessage("Success get list.");
        } else {
            rm.setMessage("Type cannot be null.");
        }
        return rm;
    }

    // =========== New Method Copy Data Server From Lukas 17-10-2023 ===========
    @Override
    public boolean insertDataLocal(Map<String, Object> param) {
        param.put("trx", 0);
        Date startApp = new Date();
        Gson gson = new Gson();
        String tableName = param.get("tableName").toString();
        String aliasName = param.get("tableName").toString();
        String dateUpd = param.get("dateUpd").toString();
        String timeUpd = param.get("timeUpd").toString();
        Optional<TableAlias> ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_M, "table", tableName);
        if(ta.isEmpty()){
            ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_M, "alias", tableName);
        }
        TableAlias tableAlias = ta.get();
        tableName = tableAlias.getTable();
        aliasName = tableAlias.getAlias();
        try {
            // Start Get Data API Server
            CloseableHttpClient client = HttpClients.createDefault();
            String url = urlMaster + "/get-data";

            System.out.println("URL Get : " + url);
            FileLoggerUtil.log("terimaDataMaster", ("URL Get : " + url), "SYSTEM");

            HttpGet getData = new HttpGet(url);

            URI uri = new URIBuilder(getData.getURI()).addParameter("param", tableName).addParameter("date", dateUpd).build();
            getData.setURI(uri);

            CloseableHttpResponse response = client.execute(getData);

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            StringBuilder content = new StringBuilder();
            String line;
            while (null != (line = br.readLine())) {
                content.append(line);
            }

            String result = content.toString();

//            System.out.println("Result: " + result);

            Map<String, Object> map1;
            map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
            }.getType());

            // End Get Data API Server
            List<Map<String, Object>> listItem = (List<Map<String, Object>>) map1.get("item");
            if (listItem == null) {
                System.err.println("Error From Server API");
                System.out.println("FAILED COPY DATA " + tableName);
                FileLoggerUtil.log("terimaDataMaster", ("FAILED COPY DATA " + tableName), "SYSTEM");
                return false;
            } else {
                compareData(listItem, tableName, param);
                System.out.println("Ended Copy Data " + tableName);
                FileLoggerUtil.log("terimaDataMaster", ("Ended Copy Data " + tableName), "SYSTEM");
                return true;
            }
        } catch (JsonSyntaxException | IOException | UnsupportedOperationException | URISyntaxException e) {
            Date failedApp = new Date();
            System.out.println("FAILED COPY DATA " + tableName + " At " + failedApp.toString());
            System.err.println(e.getMessage());
            FileLoggerUtil.log("terimaDataMaster", ("FAILED COPY DATA " + tableName), "SYSTEM");
            FileLoggerUtil.log("terimaDataMaster", ("Error: " + e.getMessage()), "SYSTEM");

            return false;
        }
    }

    // ==== Function Stand Alone ====
    public void compareData(List<Map<String, Object>> itemServer, String tableName, Map<String, Object> param) {
        List<String> primaryKey = primaryKeyTable(tableName);
        int totalUpdateRow = 0;
        int totalInsertRow = 0;

        for (Map<String, Object> item : itemServer) {
            String conditionText = "";
            int indexKey = 0;

            if (primaryKey.isEmpty()) {
                for (String key : item.keySet()) {
                    conditionText += key + "='" + item.get(key) + "'";
                    indexKey++;
                    if (indexKey < item.keySet().size()) {
                        conditionText += " AND ";
                    }
                }
            } else {
                for (String key : primaryKey) {
                    conditionText += key + "='" + item.get(key) + "'";
                    indexKey++;
                    if (indexKey < primaryKey.size()) {
                        conditionText += " AND ";
                    }
                }
            }

            String query = "SELECT * FROM " + tableName; // Need Update Condition HERE

            if (!"".equals(conditionText)) {
                query += " WHERE " + conditionText;
            }

            Map prm = new HashMap();

            List<Map<String, Object>> list = jdbcTemplate.query(query, prm, (ResultSet rs, int index) -> convertObject(rs));

            Map<String, Object> exist = list.isEmpty() ? null : list.get(0);

            if (exist == null || "".equals(conditionText)) {
                totalInsertRow += insertData(tableName, item);
            } else if (checkAllColumn(item, exist) == false) {
                totalUpdateRow += updateData(tableName, item, primaryKey);
            }
        }
        
        
        int total = totalInsertRow + totalUpdateRow;
        String status = total == itemServer.size() ? "UPDATED" : (total == 0 && !itemServer.isEmpty() ? "NOT UPDATED" : total + " OF " + itemServer.size());
        param.put("totalRow", total);
        param.put("status", status);
        saveToQueryKirimTerimaData(param);
        messagingTemplate.convertAndSend("/topic/kirim-terima-data", param.getOrDefault("aliasName", "data") + ": " + total + " Row");
    }

    public int  insertData(String tableName, Map<String, Object> data) {
        String columnName = "";
        String value = "";
        int indexKey = 0;
        Map<String, Object> params = new HashMap<>();

        for (String key : data.keySet()) {
            columnName += key;
            value += ":" + key;

            Object temp = data.get(key);
            if (temp == null) {
                params.put(key, null);
            } else {
                params.put(key, data.get(key));
            }
            indexKey++;
            if (indexKey < data.keySet().size()) {
                columnName += " ,";
                value += " ,";
            }
        }

        String query = "INSERT INTO " + tableName + " (" + columnName + ") VALUES (" + value + ")";
        return jdbcTemplate.update(query, params);
    }

    public int updateData(String tableName, Map<String, Object> data, List<String> primaryKey) {
        String columnValue = "";
        String conditionQuery = "";
        int indexKey = 0;

        Map<String, Object> params = new HashMap<>();
        int counterKey = 0;
        for (String key : data.keySet()) {
            columnValue += key + "= :" + key + "";
            if (primaryKey.contains(key)) {
                conditionQuery += key + "= :" + key;
                counterKey++;
                if (counterKey < primaryKey.size()) {
                    conditionQuery += " AND ";
                }
            }

            Object temp = data.get(key);
            if (temp == null) {
                params.put(key, null);
            } else {
                params.put(key, data.get(key));
            }
            indexKey++;
            if (indexKey < data.keySet().size()) {
                columnValue += " ,";
            }
        }
        String query = "UPDATE " + tableName + " SET " + columnValue + " WHERE " + conditionQuery;
        return jdbcTemplate.update(query, params);
    }
    
    @Transactional
    private void saveToQueryKirimTerimaData(Map<String,Object> prm){
        System.err.println("saveToQueryKirimTerimaData trx: " + prm.get("trx").getClass().getSimpleName());
        System.err.println("saveToQueryKirimTerimaData trx: " + prm.get("trx"));
        if((Integer) prm.get("trx") == 1){
            prm.put("trxCode","1");
            prm.put("dataCode","0");
            prm.put("processStatus","Y");
            prm.put("receiveStatus","N");
        } else {
            prm.put("trxCode","0");
            prm.put("dataCode","1");
            prm.put("processStatus","N");
            prm.put("receiveStatus","Y");
        }
        String qryHeader = """
                INSERT INTO M_OUTLET_FTP_HIST
                    (TRX_CODE, DATA_CODE, REGION_CODE, AREA_CODE, PARENT_OUTLET, OUTLET_CODE, TRANS_DATE, OUTLET_CHOICE, PROCESS_STATUS, RECEIVE_STATUS, USER_UPD, DATE_UPD, TIME_UPD, CITY)
                           SELECT :trxCode, :dataCode, mo.REGION_CODE, mo.AREA_CODE, mod2.PARENT_OUTLET, :outletCode, :dateUpd, 'Y', :processStatus, :receiveStatus, :userUpd, :dateUpd, :timeUpd, mo.CITY
                FROM M_OUTLET mo
                LEFT JOIN M_OUTLET_DETAIL mod2 ON mod2.CHILD_OUTLET = mo.OUTLET_CODE
                WHERE mo.OUTLET_CODE = :outletCode
                AND NOT EXISTS (
                    SELECT 1
                    FROM M_OUTLET_FTP_HIST
                    WHERE TRANS_DATE = :dateUpd
                    AND DATE_UPD = :dateUpd
                    AND TIME_UPD = :timeUpd
                    AND OUTLET_CODE = :outletCode
                    AND TRX_CODE = :trxCode
                    AND DATA_CODE = :dataCode
                )
                           """;
        String qryDetail = """
                INSERT INTO M_OUTLET_FTP_HIST_DTL
                (TIME_UPD, TRANS_DATE, USER_UPD, DESCRIPTION, REMARK, STATUS, TOTAL)
                SELECT :timeUpd, :dateUpd, :userUpd, :tableName, :remark, :status, :totalRow
                FROM dual
                WHERE NOT EXISTS (
                    SELECT 1 FROM M_OUTLET_FTP_HIST_DTL
                    WHERE TRANS_DATE = :dateUpd
                    AND TIME_UPD = :timeUpd
                    AND DESCRIPTION = :tableName
                )
                           """;
        jdbcTemplate.update(qryHeader, prm);
        jdbcTemplate.update(qryDetail, prm);
    }

    // ======= New Method Send Data From Local to Server (Table with name "T_") =========
    @Override
    public Map sendDataLocal(Map<String, Object> param) {
        param.put("trx", 1);
        Date startApp = new Date();
        Gson gson = new Gson();
        String tableName = param.get("tableName").toString();
        String aliasName = param.get("tableName").toString();
        String dateUpd = param.get("dateUpd").toString();
        String timeUpd = param.get("timeUpd").toString();
        Optional<TableAlias> ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_T, "table", tableName);
        if(ta.isEmpty()){
            ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_T, "alias", tableName);
        }
        System.out.println(tableName);
        TableAlias tableAlias = ta.get();
        aliasName = tableAlias.getAlias();
        Map map1 = new HashMap();

        try {
            System.out.println("Start Transfer Data " + tableName + " At " + startApp.toString());

            // todo: cek column name
            String conditionText = conditionTextTransfer(tableName, dateUpd);

            String query = "SELECT * FROM " + tableName + conditionText;
            List<Map<String, Object>> list = jdbcTemplate.query(query, param, (ResultSet rs, int index) -> convertObject(rs));

            // START API to Send Master
            CloseableHttpClient client = HttpClients.createDefault();
            String url = urlMaster + "/receive-data";
            HttpPost post = new HttpPost(url);

            post.setHeader("Accept", "*/*");
            post.setHeader("Content-Type", "application/json");

            param.put("tableName", tableName);
            param.put("data", list);
            
            String json = "";
            json = new Gson().toJson(param);
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);
            CloseableHttpResponse response = client.execute(post);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            (response.getEntity().getContent())
                    )
            );
            StringBuilder content = new StringBuilder();
            String line;
            while (null != (line = br.readLine())) {
                content.append(line);
            }
            String result = content.toString();
            System.err.println("result sendDataLocal:" + result);
            map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
            }.getType());
            // END API to Send Master
            List lst = (List) map1.get("item");
            if(!lst.isEmpty()){
                double total = (double) lst.get(0);
                String status = total == list.size() ? "UPDATED" : (total == 0 && !list.isEmpty() ? "NOT UPDATED" : total + " OF " + list.size());
                param.put("totalRow", lst.get(0));
                param.put("status", status);
                saveToQueryKirimTerimaData(param);
            }
        } catch (JsonSyntaxException | IOException | UnsupportedOperationException | DataAccessException e) {
            Date failedApp = new Date();
            System.out.println("FAILED SEND DATA " + tableName + " At " + failedApp.toString());
            System.err.println(e.getMessage());
        }
        return map1;
    }

    public String conditionTextTransfer(String tableName, String date) {
        // todo27
        Optional<TableAlias> ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_T, "table", tableName);
        TableAlias tableAlias = ta.get();
        return " WHERE " + tableAlias.getDateColumn() + " = '" + date + "' ";
    }

    public Map<String, Object> convertObject(ResultSet result) throws SQLException {
        Map<String, Object> resultReturn = new HashMap<>();
        ResultSetMetaData rsmd = result.getMetaData();
        int cols = rsmd.getColumnCount();
        for (int i = 0; i < cols; i++) {
            String columnName = rsmd.getColumnName(i + 1);
            Object columnValue = result.getObject(i + 1);
            switch (columnName) {
                // ---- Start Need to Discuss ----                
//                case "DATE_UPD" -> {
//                    resultReturn.put(rsmd.getColumnName(i + 1), dateNow);
//                }
//                case "TIME_UPD" -> {
//                    resultReturn.put(rsmd.getColumnName(i + 1), timeStamp);
//                }
                // ---- End Need to Discuss ----

                // todo: set all date column to format dd-MMM-yyyy
                case "ASSEMBLY_END_TIME", "ASSEMBLY_START_TIME", "BILL_DATE", "BOOK_DATE", "BUCKET_DATE", "CC_DATE", "DATE_1", "DATE_2", "DATE_3", "DATE_4", "DATE_5", "DATE_6", "DATE_ABSEN", "DATE_CREATE", "DATE_DEL", "DATE_END", "DATE_EOD", "DATE_MPCS", "DATE_OF_BIRTH", "DATE_SEND", "DATE_START", "DATE_TRANS", "DATE_UPD", "DELIVERY_DATE", "DISPATCH_END_TIME", "DISPATCH_START_TIME", "DT_DUE", "DT_EXPIRED", "EFFECTIVE_DATE", "EMPLOY_DATE", "END_DATE", "END_OF_DAY", "EVENT_DATE", "FINISH_DATE", "FINISH_TIME", "HOLIDAY_DATE", "KEY_3", "LAST_ORDER", "LAST_RECEIVE", "LAST_RETURN", "LAST_SALES", "LAST_TRANSFER_IN", "LAST_TRANSFER_OUT", "LOG_DATE", "MPCS_DATE", "OPNAME_DATE", "ORDER_DATE", "PAYMENT_DATE", "PERIODE", "PICKUP_END_TIME", "PICKUP_START_TIME", "RECV_DATE", "RESIGN_DATE", "RETURN_DATE", "START_DATE", "START_OF_DAY", "START_TIME", "SUGGEST_DATE", "SUPPLY_END_TIME", "SUPPLY_QUEUE_START_TIME", "SUPPLY_START_TIME", "TANGGAL", "TANGGAL_CREATE", "TANGGAL_MODAL", "TANGGAL_PESAN_TERAKHIR", "TANGGAL_SETOR", "TANGGAL_TARIK", "TANGGAL_TRANS", "TANGGAL_TRANSAKSI", "TD", "TGL_CREATE", "TGL_KIRIM", "TGL_MODAL", "TGL_PESAN_TERAKHIR", "TGL_RETURN", "TGL_SETOR", "TGL_TERIMA", "TGL_TRANSAKSI", "TMPFLD2", "TMPFLD9", "TMP_DATE_UPD", "TMP_TRANS_DATE", "TRANSFER_DATE", "TRANS_DATE", "VIEW_DATE_UPD", "VIEW_TRANS_DATE", "WASTAGE_DATE" -> {
                    String dateData = new SimpleDateFormat("dd-MMM-yyyy").format(columnValue);
                    resultReturn.put(columnName, dateData);
                }
                case "SEQ_MPCS", "QTY_ACC_REJECT", "QTY_ACC_VARIANCE", "ENDING_QTY", "PURCHASE_QTY", "RECEIVE_QTY", "TOT_RECORD", "TOT_ML", "TOT_MP", "ITEM_DETAIL_SEQ", "TOTAL_CHARGE", "LOG_SEQ", "AMT_TAX", "PAYMENT_USED", "CASH_BANK", "PENGAJUAN2", "NO_SEQ", "AMT_TRANS", "EI_AMOUNT", "TA_QTY", "TA_AMOUNT", "TTD", "DELTIMEM", "BEGINING_K", "ENDING_QTY_K", "BEGINING", "QTYTRANS", "TOTAL_CLAIM", "TOTAL_PENJUALAN_FNB", "COUNT_TRANS_AMOUNT", "MIN_SALES", "RND_LIMIT", "MAX_SHIFT", "ID_NO", "TAKE_AWAY", "DISC_PERCENT", "CNT", "POSTED", "QTY_BEGINING", "FML", "DLV", "JUMLAH_DETAILS", "BIAYA_ANTAR", "SETOR_TRANSAKSI", "DISC_25_NILAI", "DISC_50_TRANSAKSI", "DISC_100_TRANSAKSI", "REFUND_NILAI", "JUMLAH_CUSTOMER", "QUANTITY_EAT_EI", "AMT_ENDSHIFT", "QTY_PROD", "QTY_ACC_PROD", "PRD_QTY", "AMT_COST", "TARGET", "PERIOD_YEAR", "TOTAL_CUSTOMER", "TOTAL_PAYMENT", "TRANS_AMOUNT", "TOTAL_TAX_CHARGE", "TOTAL_DP_PAID", "TOTAL_IN", "AMT_OUT", "DEBET_AMT", "TOTAL_QTY_STOCK", "QTY1", "AMT_SALES_HDR", "QTY_SALES", "QTY_USED", "QTY_PERC", "AMT_TA", "INTIMESTOREM", "PEMBAGI", "QTY_K_IN", "RETURN_SUPP", "TOT_AMT_PAID", "PPN_CD", "AMT_CD", "TAXABLE", "DEL_LIMIT", "RND_FACT", "MAX_BILLS", "MIN_ITEMS", "FRYER_TYPE_SEQ_CNT", "MODIFIER_GROUP6_MAX_QTY", "KODE_TERMINAL", "FREE_MAGNETIC", "SALDOAWAL", "STOCKIN", "SLS2", "DEL", "PRD", "RCV", "STO", "DISCOUNT_PERSEN", "TAX_BRUTTO_AMOUNT", "KUPON_NILAI_DIGUNAKAN", "OVERALL_HARGA_SATUAN", "Total CASH", "AMT_SETOR", "WASTAGE_ID", "ITEM_COST", "RETURN_ID", "TOTAL_PRICE", "CREDIT_AMT", "ORDER_ID", "TRANSFER_ID", "AMT_SALES_DTL", "PLU_QTY", "QTY_EI", "AMT_EI", "NILAI", "SORT", "RCV_SUPPLIER", "TRANSFER_IN_OUTLET", "LEFT_OFER_OUT", "ENDING", "MULTIPLY", "QTYTA", "FLD10", "FLD6", "FLD7", "TOT_CD", "TICKET_AVG", "PPN", "TOTPAYMENTAMOUNT", "TOTPAYMENTUSE", "DISC_AMT", "DEL_CHARGE", "DP_MIN", "MODIFIER_GROUP1_MIN_QTY", "MODIFIER_GROUP2_MIN_QTY", "MODIFIER_GROUP4_MAX_QTY", "MODIFIER_GROUP5_MAX_QTY", "MODIFIER_GROUP6_MIN_QTY", "ACCESSW", "CURRENT_STOCK", "TRANSFER_OUT", "KEY_4", "MONTH", "COUNTER_NO", "G_VALUE", "NILAI_TAX", "JML_DETAIL", "NILAI_SETOR", "NILAI_REFUND", "DISKON_NILAI", "TRX", "LOV3", "PRD4", "WAS", "DIFF", "PEMBAYARAN", "DISCOUNT_PERCENT", "STATUS", "TAX_NETT_AMOUNT", "DISC_25_TRANSAKSI", "DISC_50_NILAI", "ERROR_NILAI", "JUMLAH_TRANSAKSI", "OVERALL_TOTAL_HARGA", "AMT_MODAL", "DINE_NILAI", "QTY_ACC_SOLD", "QTY_ACC_WASTAGE", "QTY_IN", "IN_QTY", "ADJUSTMENT_QTY", "NO_OF_PRINT", "QTY_1", "ITEM_QTY", "TOTAL_DISCOUNT", "TOTAL_TAX", "PAY_SEQ", "QTY_STOCK", "QTY_FREEZE", "TOTAL", "BEGINING_B", "RCV_GUDANG", "QTYEI", "TMPFLD6", "SUM_AMOUNT", "KUPON_DIGUNAKAN_Q", "DONASI", "TAX_CHARGE_BILL", "GROSS_SALES", "AMOUNT_BY_STATUS", "PROCESS", "VALUE", "COST", "CAT_ITEMS", "REF_TIME", "MAX_DISC_PERCENT", "TAX_CHARGE", "MODIFIER_GROUP1_MAX_QTY", "MODIFIER_GROUP3_MIN_QTY", "RECEIVE", "QTY_STOCK_T", "DRAWER", "MODAL", "TOTAL_HEADER", "TOTAL_DETAIL", "STA", "FML5", "SLS", "EI_TA", "MODAL_NILAI", "SETOR_NILAI", "DISC_75_TRANSAKSI", "DISC_100_NILAI", "ERROR_TRANSAKSI", "KUPON_QUANTITY", "TOTAL_HARGA_EI", "DINE_PERSEN", "TOTAL_PERSEN", "DS", "AMT_REFUND", "QTY_PROJ_CONV", "QTY_PROJ", "QTY_VARIANCE", "ITEM_SEQ", "QTY_PURCHASE", "QTY_2", "DAY_SEQ", "TOTAL_SALES", "TOTAL_CANCEL", "DONATE_AMOUNT", "PENGAJUAN1", "QTY", "AMT_CUSTOMER", "DELTIMES", "JUMLAH", "QTY_K_OUT", "REFUND", "PRODUKSI", "ADJUSTMENT", "FLD11", "FLD3", "SUM_COUNT", "TRANSAKSI", "DISCOUNT", "TOTAL_PENDAPATAN", "ROUNDING_BILL", "DAY_OF_WEEK", "ENABLED_MENU", "TRANS_CODE", "FRYER_TYPE_RESET", "MODIFIER_GROUP5_MIN_QTY", "MODIFIER_GROUP7_MAX_QTY", "ACCESSR", "CONV_WAREHOUSE", "ON_ORDER", "RETURN", "DISC_NILAI", "HARGA_SATUAN", "BIAYA_DELIVERY", "RET6", "NOMOR_POS", "JML_TRANS", "SALDO_CASH_DRAWER", "DISC_200_TRANSAKSI", "DISC_200_NILAI", "BCA_QUANTITY", "OVERALL_QUANTITY", "AWAY_PERSEN", "COUNT_BILL", "QUANTITY", "QTY_SOLD", "OUT_QTY", "BEGINNING_QTY", "QUANTITY_IN", "QTY_BONUS", "TOTAL_AMOUNT", "TOTAL_ROUNDING", "TOTAL_OUT", "QTY_PURCH", "ENDING_AMT", "UNIT_PRICE", "PENGAJUAN3", "HIST_SEQ", "PRICE", "LEVELING", "INTIMESTORES", "DOORTIMES", "GROUPES", "QTY_B_IN", "ENDING_QTY_B", "PETTY_C", "LEFT_OVER_IN", "RETURN_GUDANG", "TMPFLD4", "CUST_AVERAGE", "CHARGE_BILL", "COUNT_DISC", "MAX_CHANGE", "CANCEL_FEE", "TIME_OUT", "MIN_PULL_TRX", "FRYER_TYPE_CNT_PREV", "MODIFIER_GROUP2_MAX_QTY", "MODIFIER_GROUP3_MAX_QTY", "MODIFIER_GROUP4_MIN_QTY", "CONV_STOCK", "ORDER_FREQ", "R_VALUE", "B_VALUE", "NILAI_VOUCHER", "NILAI_BCA_DEBIT", "CUSTOMER_COUNT", "KODE_PLU", "DISKON_PERSEN", "TOTAL_KEMBALIAN", "KODE_MAP", "NOMOR", "DEL1", "WAS9", "STO11", "LOV", "DISCOUNT_NILAI", "NILAI_BCA_DEBIT_CARD", "NO_TRANS", "KUPON_NILAI_TERPAKAI", "MODAL_TRANSAKSI", "VOID_TRANSAKSI", "HARGA_SATUAN_EI", "DINE_QTY", "AMT_SALES", "TOTALSTOCK", "NOMINAL", "QTY_REJECT", "QTY_WASTAGE", "QTY_ACC_ONHAND", "QTY_OUT", "TRANSFER_OUT_QTY", "FILE_NO", "PERIOD_MONTH", "TOTAL_EXCESS", "TOTAL_REPRINT", "TOTAL_REFUND", "TOTAL_DONATION", "BEGINNING_AMT", "CD_TEMPLATE", "SEQ", "AMT_DISC", "AMT_PERC", "QTY_B_OUT", "SALES_OUT", "TRANSFER_OUT_OUTLET", "FLD2", "TOT_TRN_PAID", "TOTALBILL", "CUSTOMER", "TAX", "TOTAL_BY_STATUS", "DONE", "FLAG_CHOICE", "MAX_PULL_VALUE", "REFUND_TIME_LIMIT", "QTY_CONV", "LEVEL_MENU", "MAX_STOCK", "TRANSFER_IN", "SALES", "KEMBALI", "NILAI_PENJUALAN", "NILAI_MODAL", "SETOR_END_SHIFT", "SALES_QUERY", "REPRINT", "RCV7", "DLV8", "RET", "WFP", "CASH_I_NILAI", "TAX_AMOUNT", "TOTAL_NILAI_TRANSAKSI", "AWAY_QTY", "AMT_DP", "TOTAL_NILAI", "QTY_ACC_PROJ", "QTY_ONHAND", "TRANSFER_IN_QTY", "SEQ_NO", "QTY_BEGINNING", "FILE_SIZE", "MP", "QTY_WAREHOUSE", "TOTAL_QTY", "AMOUNT", "TOTAL_ITEM", "PERCENTAGE", "TOTAL_CHANGE", "TRANS_SEQ", "PAYMENT_AMOUNT", "DONATE_SEQ", "DP_SEQ", "TOTAL_ESTIMATE_PAYMENT", "AMT_IN", "PENGAJUAN4", "COST_FREEZE", "QTY2", "COST_OPNAME", "SERVICE_CHARGE", "EI_QTY", "QTY_TA", "TTM", "DOORTIMEM", "PRODUKSI_IN", "WASTE_OUT", "TMPFLD5", "FLD4", "FLD5", "KUPON_DIGUNAKAN_A", "KUPON_TERPAKAI", "TOTAL_PENJUALAN", "TTL_BILL_JOINT", "SUM_TRANS_AMOUNT", "CASH_BALANCE", "MAX_DISC_AMOUNT", "FRYER_TYPE_CNT", "MODIFIER_GROUP7_MIN_QTY", "MIN_STOCK", "QTY_STOCK_E", "YEAR", "SUB_TOTAL_HARGA", "TOTAL_HARGA", "PERCENT_PPN", "SETOR", "CML", "JUMLAH_PESAN", "TOTAL_NILAI_PESAN", "STOCKOUT", "SALDOAKHIR", "LOC10", "LOC", "DISC_75_NILAI", "VOID_NILAI", "REFUND_TRANSAKSI", "BCA_NILAI", "QUANTITY_EAT_TA", "TOTAL_HARGA_TA", "HARGA_SATUAN_TA", "AMT_CATERING", "TYPE" -> {

                    Object temp = result.getObject(i + 1);
                    if (temp == null) {
                         resultReturn.put(rsmd.getColumnName(i + 1), null);
                    } else if (temp instanceof Number number) {
                        resultReturn.put(rsmd.getColumnName(i + 1), number);
                    } else if (temp instanceof String string) {
                        try {
                            Number numberValue = Double.valueOf(string);
                            resultReturn.put(rsmd.getColumnName(i + 1), numberValue);
                        } catch (NumberFormatException e) {
                            resultReturn.put(rsmd.getColumnName(i + 1), 0);
                        }
                    } else if (temp instanceof Date date) {  
                        String dateData = new SimpleDateFormat("dd-MMM-yyyy").format(date);
                        resultReturn.put(columnName, dateData);
                    } else {
                        resultReturn.put(rsmd.getColumnName(i + 1), 0);
                    }
                }
                default -> {
                    Object temp = result.getObject(i + 1);
                    if (temp == null) {
                        resultReturn.put(rsmd.getColumnName(i + 1), null);
                    } else {
                        resultReturn.put(rsmd.getColumnName(i + 1), result.getObject(i + 1).toString());
                    }
                }
            }
        }
        return resultReturn;
    }

    public boolean checkAllColumn(Map<String, Object> itemServer, Map<String, Object> itemExist) {
        boolean result = true;

        for (String key : itemServer.keySet()) {
            switch (key) {
                case "QTY_STOCK_E", "QTY_STOCK_T", "QTY_EI", "QTY_TA" -> {
                    var temp = Double.valueOf(itemServer.get(key).toString());
                    if (!itemServer.get(key).equals(temp)) {
                        System.out.println("formatter " + temp);
                        System.out.println("server " + itemServer.get(key));
                        result = false;
                    }
                }
                default -> {
                    if (itemServer.get(key) != itemExist.get(key) && !itemServer.get(key).equals(itemExist.get(key))) {
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    public List<String> primaryKeyTable(String tableName) {
        Optional<TableAlias> ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_M, "table", tableName);
        if(ta.isEmpty()){
            ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_M, "alias", tableName);
        }
        TableAlias tableAlias = ta.get();
        List<String> primaryKey = tableAlias.getPrimaryKeyList();
        return primaryKey;
    }
    // =========== End Method Copy Data Server From Lukas 17-10-2023 ===========

    @Override
    public void deleteOrderEntryDetail(Map<String, Object> param) {
        String query = "DELETE T_ORDER_DETAIL WHERE OUTLET_CODE = :outletCode AND ORDER_NO = :orderNo AND ITEM_CODE = :itemCode";
        jdbcTemplate.update(query, param);
    }
    

    //============== New Method From M Joko 5-2-2024 ================
    @Override
    public ResponseMessage processBackupDb(Map<String, Object> param) {
        ResponseMessage rm = new ResponseMessage();
        rm.setItem(new ArrayList());
        rm.setSuccess(false);
        String backupDirectoryName = "BOFFI_BACKUP_DIRECTORY";
        if (param.containsKey("process") && param.get("process").equals(true)) {
            // proses backup
            long startTime = System.currentTimeMillis();
            try {
                String currentWorkingDirectory = new File(".").getCanonicalPath();
                String backupFolderPath = currentWorkingDirectory + File.separator + "backup";
                File backupFolder = new File(backupFolderPath);
                if (!backupFolder.exists()) {
                    boolean created = backupFolder.mkdirs();
                    if (!created) {
                        System.err.println("Failed to create the backup folder.");
                        rm.setMessage("Failed to create the backup folder: " + backupFolderPath);
                        return rm;
                    }
                }
                String url = appUtil.get("spring.datasource.url");
                String db = url.substring(url.lastIndexOf(':') + 1);
                String username = appUtil.get("spring.datasource.username");
                String password = appUtil.get("spring.datasource.password");
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
                String dateName = currentDateTime.format(formatter);
                String fileName = dateName + "_" + param.getOrDefault("outletCode", "") + "_" + param.getOrDefault("userUpd", "") + "_BOFFI";
                String checkIfExistsQuery = "SELECT COUNT(*) FROM all_directories WHERE directory_name = '" + backupDirectoryName + "'";
                int count = jdbcTemplate.queryForObject(checkIfExistsQuery, param, Integer.class);
                if (count > 0) {
                    String dropQuery = "DROP DIRECTORY " + backupDirectoryName;
                    jdbcTemplate.execute(dropQuery, ps -> ps.executeUpdate());
                }
                String createQuery = "CREATE DIRECTORY " + backupDirectoryName + " AS '" + backupFolderPath + "'";
                jdbcTemplate.execute(createQuery, ps -> ps.executeUpdate());
                ProcessBuilder processBuilder = new ProcessBuilder(
                        "expdp", username + "/" + password + "@" + db,
                        "dumpfile=" + fileName + ".dmp", "directory=" + backupDirectoryName,
                        "schemas=kfc", "LOGFILE=" + fileName + ".log"
                );
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // todo: hubungkan output ke websocket untuk update progress
                        System.out.println("backupDb: " + line);
//                        Map m = parseOutputBackupDb(line);
//                        if(!m.isEmpty()){
//                            System.out.println(m);
//                            TableAlias t = this.tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_M, "table", m.getOrDefault("table", "").toString()).get();
//                            String msg = t.getAlias() + ": " + m.getOrDefault("row", "0");
                            messagingTemplate.convertAndSend("/topic/backup-db", line);
//                        }
                    }
                }
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    System.out.println("Backup completed successfully.");
                    rm.setMessage("Backup completed successfully.");
                    rm.setSuccess(true);
                } else {
                    System.err.println("Backup failed. Exit code: " + exitCode);
                    rm.setMessage("Backup failed. Exit code: " + exitCode);
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error processBackupDb : " + e.getMessage());
                rm.setMessage("Error processBackupDb : " + e.getMessage());
            }
            double elapsedTimeSeconds = (double) (System.currentTimeMillis() - startTime) / 1000.0;
            System.err.println("processBackupDb process in: " + elapsedTimeSeconds + " seconds");
        } else if (param.containsKey("delete") && param.get("delete").toString().length() > 0) {
            // fungsi hapus backup dan log di parameter delete
            String fileName = param.get("delete").toString();
            String currentWorkingDirectory = System.getProperty("user.dir");
            String backupFolderPath = currentWorkingDirectory + File.separator + "backup";
            File backupFolder = new File(backupFolderPath);
            if (backupFolder.exists() && backupFolder.isDirectory()) {
                File fileToDelete = new File(backupFolder, fileName);
                File logFileToDelete = new File(backupFolder, fileName.toUpperCase().replace(".DMP", ".log"));
                if (fileToDelete.exists() && fileToDelete.isFile()) {
                    if (fileToDelete.delete()) {
                        System.out.println("Backup and log deleted successfully: " + fileName);
                        rm.setMessage("Backup and log deleted successfully: " + fileName);
                        rm.setSuccess(true);
                        if(logFileToDelete.exists() && logFileToDelete.isFile()){
                            logFileToDelete.delete();
                        }
                    } else {
                        System.err.println("Failed to delete backup: " + fileName);
                        rm.setMessage("Failed to delete backup: " + fileName);
                    }
                } else {
                    rm.setMessage("File not found: " + fileName);
                }
            } else {
                System.err.println("Backup folder does not exist.");
                rm.setMessage("Backup folder does not exist.");
            }
        } else {
            // Kembalikan list backup
            try {
                String currentWorkingDirectory = System.getProperty("user.dir");
                String backupFolderPath = currentWorkingDirectory + File.separator + "backup";
                File backupFolder = new File(backupFolderPath);
                if (backupFolder.exists() && backupFolder.isDirectory()) {
                    File[] backupFiles = backupFolder.listFiles((dir, name) -> name.toUpperCase().endsWith(".DMP"));
                    if (backupFiles != null && backupFiles.length > 0) {
                        Arrays.sort(backupFiles, Comparator.comparingLong(File::lastModified).reversed());
                        DecimalFormat df = new DecimalFormat("#.##");
                        List<Map<String, Object>> fileList = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                        for (File file : backupFiles) {
                            Map<String, Object> fileInfo = new HashMap<>();
                            fileInfo.put("fileName", file.getName());
                            fileInfo.put("lastModified", dateFormat.format(new Date(file.lastModified())));
                            double sizeInMB = ((double) file.length()) / (1024 * 1024);
                            double sizeInGB = ((double) file.length()) / (1024 * 1024 * 1024);
                            fileInfo.put("sizeInGB", Double.valueOf(df.format(sizeInGB)));
                            fileInfo.put("sizeInMB", Double.valueOf(df.format(sizeInMB)));
                            fileInfo.put("sizeInB", file.length());
                            String[] parts = file.getName().split("_");
                            if (parts.length >= 4) {
                                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
                                LocalDateTime datetime = LocalDateTime.parse(parts[0], inputFormatter);
                                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                fileInfo.put("datetime", datetime.format(outputFormatter));
                                fileInfo.put("outletCode", parts[1]);
                                fileInfo.put("userId", parts[2]);
                                fileInfo.put("app", parts[3].toUpperCase().replace(".DMP", ""));
                            }
                            fileList.add(fileInfo);
                        }
                        double freeSpace = appUtil.getDiskFreeSpace();
                        rm.setMessage(String.format("%.2f", freeSpace));
                        rm.setItem(fileList);
                        rm.setSuccess(true);
                    } else {
                        rm.setMessage("No backup files found.");
                    }
                } else {
                    rm.setMessage("Backup folder does not exist.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Error while getting the list of backup files: " + e.getMessage());
                rm.setMessage("Error while getting the list of backup files: " + e.getMessage());
            }
        }
        return rm;
    }
    
    Map parseOutputBackupDb(String line){
        Pattern pattern = Pattern.compile("exported\\s+\".+?\"\\.\"(.+?)\"\\s+(\\d+\\.\\d+\\s+(?:GB|MB|KB))\\s+(\\d+)\\s+rows");
        Matcher matcher = pattern.matcher(line);
        Map<String, Object> infoMap = new HashMap<>();
        if (matcher.find()) {
            String tableName = matcher.group(1);
            String size = matcher.group(2);
            int numRows = Integer.parseInt(matcher.group(3));
            infoMap.put("table", tableName);
            infoMap.put("size", size);
            infoMap.put("row", numRows);
        }
        return infoMap;
    }
    
    // =========== End Method Copy Data Server From M Joko 16-02-2024 ===========
    @Override
    public ResponseMessage updateRecipe(Map<String, Object> param) {
        ResponseMessage rm = new ResponseMessage();
        rm.setItem(new ArrayList());
        rm.setSuccess(false);
        rm.setMessage("Update status " + param.get("recipeCode") + ": " + param.get("status") + " success.");
        String query = "UPDATE M_RECIPE_HEADER SET STATUS = :status, USER_UPD = :userUpd, DATE_UPD = TO_CHAR(SYSDATE, 'DD MON YYYY'), TIME_UPD = TO_CHAR(SYSDATE, 'HH24MISS')  WHERE RECIPE_CODE = :recipeCode";
        try{
            jdbcTemplate.update(query, param);
            rm.setSuccess(true);
        } catch (DataAccessException e){
            rm.setMessage(e.getMessage());
        }
        return rm;
    }

    //============== New Method From Sifa 15-02-2024 -> Update CD WAREHOUSE M_ITEM ================
    @Override
    public ResponseMessage updateCdWarehouseItem(Map<String, Object> param) {
        ResponseMessage rm = new ResponseMessage();
        rm.setItem(new ArrayList());
        rm.setSuccess(false);
        rm.setMessage("Update Item " + param.get("cdWarehouse") + ": " + param.get("homePage") + " success.");
        String query = "UPDATE M_ITEM " +
                        "SET CD_WAREHOUSE  = :cdWarehouse " +
                        "WHERE ITEM_CODE IN ( " +
                        "    SELECT DISTINCT mis.ITEM_CODE " +
                        "    FROM M_ITEM_SUPPLIER mis " +
                        "    JOIN M_SUPPLIER ms ON mis.CD_SUPPLIER = ms.CD_SUPPLIER " +
                        "    WHERE ms.HOMEPAGE = :homePage " +
                        ")";
        try{
            jdbcTemplate.update(query, param);
            rm.setSuccess(true);
        } catch (DataAccessException e){
            rm.setMessage(e.getMessage());
        }
        return rm;
    }

    @Override
    public void checkInventoryAvailability() throws ClientProtocolException, IOException {
        Gson gson = new Gson();
        CloseableHttpClient client = HttpClients.createDefault();
        String url = this.urlWarehouse + "/halo";
        HttpPost post = new HttpPost(url);
        post.setHeader("Accept", "*/*");
        post.setHeader("Content-Type", "application/json");
        String json = new Gson().toJson(new HashMap<>());
        StringEntity entity = new StringEntity(json);
        post.setEntity(entity);
        CloseableHttpResponse response = client.execute(post);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        (response.getEntity().getContent())));
        System.out.println(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Gagal menghubungkan ke warehouse.");
        }
    }
    
    ///////////////NEW METHOD insert jika belum ada m_counter di bulan berikutnya, setelah End of Day - M Joko 20/2/2024
    @Override
    public void checkMCounterNextMonth(Map<String, String> balance) {
        long startTime = System.currentTimeMillis();
        String qry = """
            INSERT INTO M_COUNTER (OUTLET_CODE, TRANS_TYPE, YEAR, MONTH, COUNTER_NO)
            SELECT 
                A.*,
                CASE 
                    WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 THEN EXTRACT(YEAR FROM mo.TRANS_DATE) + 1 
                    ELSE EXTRACT(YEAR FROM mo.TRANS_DATE) 
                END AS YEAR, 
                CASE 
                    WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 THEN 1 
                    ELSE EXTRACT(MONTH FROM mo.TRANS_DATE) + 1 
                END AS MONTH, 
                0 AS COUNTER_NO 
            FROM (
                SELECT DISTINCT mcc.OUTLET_CODE, mcc.TRANS_TYPE 
                FROM M_COUNTER mcc
                WHERE mcc.OUTLET_CODE = :outletCode
            ) A 
            JOIN M_OUTLET mo ON mo.OUTLET_CODE = A.OUTLET_CODE
            LEFT JOIN M_COUNTER mc ON mc.OUTLET_CODE = A.OUTLET_CODE
                                  AND mc.TRANS_TYPE = A.TRANS_TYPE
                                  AND mc.YEAR = CASE 
                                                    WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 
                                                    THEN EXTRACT(YEAR FROM mo.TRANS_DATE) + 1 
                                                    ELSE EXTRACT(YEAR FROM mo.TRANS_DATE) 
                                                END
                                  AND mc.MONTH = CASE 
                                                    WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 
                                                    THEN 1 
                                                    ELSE EXTRACT(MONTH FROM mo.TRANS_DATE) + 1 
                                                END
            WHERE mc.OUTLET_CODE IS NULL
                     """;
        int rowsAffected = jdbcTemplate.update(qry, balance);
        System.out.println("checkMCounterNextMonth rows affected: " + rowsAffected);
        double elapsedTimeSeconds = (double) (System.currentTimeMillis() - startTime) / 1000.0;
        System.err.println("checkMCounterNextMonth process in: " + elapsedTimeSeconds + " seconds");
    }
    
    /////////////// new method update outlet adit 21 Feb 2024
    public void updateOutlet(Map<String, String> balance) {
        String qy = "UPDATE M_OUTLET SET OUTLET_NAME=:outletName, TYPE=:type, ADDRESS_1=:address1, ADDRESS_2=:address2, CITY=:city, POST_CODE=:postCode, PHONE=:phone, FAX=:fax, CASH_BALANCE=:cashBalance, DEL_LIMIT=:delLimit, DEL_CHARGE=:delCharge, RND_PRINT=:rndPrint, RND_FACT=:rndFact, RND_LIMIT=:rndLimit, TAX=:tax, DP_MIN=:dpMin, CANCEL_FEE=:cancelFee, CAT_ITEMS=:catItems, MAX_BILLS=:maxBills, MIN_ITEMS=:minItems, REF_TIME=:refTime, TIME_OUT=:timeOut, MAX_SHIFT=:maxShift, SEND_DATA=:sendData, MIN_PULL_TRX=:minPullTrx, MAX_PULL_VALUE=:maxPullValue, STATUS=:status, START_DATE=:startDate, FINISH_DATE=:finishDate, MAX_DISC_PERCENT=:maxDiscPercent, MAX_DISC_AMOUNT=:maxDiscAmount, OPEN_TIME=:openTime, CLOSE_TIME=:closeTime, REFUND_TIME_LIMIT=:refundTimeLimit, MONDAY=:monday, TUESDAY=:tuesday, WEDNESDAY=:wednesday, THURSDAY=:thursday, FRIDAY=:friday, SATURDAY=:saturday, SUNDAY=:sunday, HOLIDAY=:holiday, OUTLET_24_HOUR=:outlet24Hour, IP_OUTLET=:ipOutlet, PORT_OUTLET=:portOutlet, USER_UPD=:userUpd, DATE_UPD=:dateUpd, TIME_UPD=:timeUpd, FTP_ADDR=:ftpAddr, FTP_USER=:ftpUser, FTP_PASSWORD=:ftpPassword, INITIAL_OUTLET=:initialOutlet, AREA_CODE=:areaCode, RSC_CODE=:rscCode, TAX_CHARGE=:taxCharge WHERE OUTLET_CODE=:outletCode";
 
             balance.put("dateUpd", LocalDateTime.now().format(dateFormatter));
             balance.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        Integer success = jdbcTemplate.update(qy, balance);
    }

    @Transactional
    @Override
    public void updateItem(Map<String, String> balance) {
        String itemQry = "UPDATE M_ITEM SET "
                + "CD_LEVEL_1=:cdLevel1, "
                + "CD_LEVEL_2=:cdLevel2, "
                + "CD_LEVEL_3=:cdLevel3, "
                + "CD_LEVEL_4=:cdLevel4, "
                + "UOM_WAREHOUSE=:uomWarehouse, "
                + "CONV_WAREHOUSE=:convWarehouse, "
                + "UOM_PURCHASE=:uomPurchase, "
                + "CONV_STOCK=:convStock, "
                + "UOM_STOCK=:uomStock, "
                + "CD_WAREHOUSE=:cdWarehouse, "
                + "FLAG_STOCK=:flagStock, "
                + "FLAG_MATERIAL=:flagMaterial, "
                + "FLAG_OPEN_MARKET=:flagOpenMarket, "
                + "FLAG_HALF_FINISH=:flagHalfFinish, "
                + "FLAG_TRANSFER_LOC=:flagTransferLoc, "
                + "FLAG_FINISHED_GOOD=:flagFinishedGood, "
                + "FLAG_CANVASING=:flagCanvasing, "
                + "FLAG_PAKET=:flagPaket, "
                + "FLAG_OTHERS=:flagOthers, "
                + "PLU=:plu, "
                + "CD_SUPPLIER_DEFAULT=:cdSupplierDefault, "
                + "MIN_STOCK=:minStock, "
                + "MAX_STOCK=:maxStock, "
                + "CD_MENU_ITEM=:cdMenuItem, "
                + "CD_ITEM_LEFTOVER=:cdItemLeftOver, "
                + "STATUS=:status, "
                + "USER_UPD=:userUpd, "
                + "DATE_UPD=:dateUpd, "
                + "TIME_UPD=:timeUpd "
                + "where ITEM_CODE =:cdItem";
        String costQry = "UPDATE M_ITEM_COST SET "
                + "ITEM_COST=:itemCost, "
                + "USER_UPD=:userUpd, "
                + "DATE_UPD=:dateUpd, "
                + "TIME_UPD=:timeUpd "
                + "WHERE OUTLET_CODE=:outletCode AND ITEM_CODE=:itemCode";
        Map costParam = new HashMap();
        costParam.put("itemCost", balance.get("cogs"));
        costParam.put("outletCode", balance.get("outletCode"));
        costParam.put("itemCode", balance.get("code"));
        costParam.put("userUpd", balance.get("userUpd"));
        costParam.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        costParam.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        
        Map param = new HashMap();
        param.put("cdItem", balance.get("code"));
        param.put("cdLevel1", balance.get("cdLevel1"));
        param.put("cdLevel2", balance.get("cdLevel2"));
        param.put("cdLevel3", balance.get("cdLevel3"));
        param.put("cdLevel4", balance.get("cdLevel4"));
        param.put("uomWarehouse", balance.get("uomWarehouse"));
        param.put("convWarehouse", balance.get("convWarehouse"));
        param.put("uomPurchase", balance.get("uomPurchase"));
        param.put("convStock", balance.get("convStock"));
        param.put("uomStock", balance.get("uomStock"));
        param.put("cdWarehouse", balance.get("cdWarehouse"));
        param.put("flagStock", balance.get("flagStock"));
        param.put("flagMaterial", balance.get("flagMaterial"));
        param.put("flagOpenMarket", balance.get("flagOpenMarket"));
        param.put("flagHalfFinish", balance.get("flagHalfFinish"));
        param.put("flagTransferLoc", balance.get("flagTransferLoc"));
        param.put("flagFinishedGood", balance.get("flagFinishedGood"));
        param.put("flagCanvasing", balance.get("flagCanvasing"));
        param.put("flagPaket", balance.get("flagPaket"));
        param.put("flagOthers", balance.get("flagOthers"));
        param.put("plu", balance.get("plu"));
        param.put("cdSupplierDefault", balance.get("cdSupplierDefault"));
        param.put("minStock", balance.get("minStock"));
        param.put("maxStock", balance.get("maxStock"));
        param.put("cdMenuItem", balance.get("cdMenuItem"));
        param.put("cdItemLeftOver", balance.get("cdItemLeftOver"));
        param.put("status", balance.get("status"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(itemQry, param);
        jdbcTemplate.update(costQry, costParam);
    }
}
