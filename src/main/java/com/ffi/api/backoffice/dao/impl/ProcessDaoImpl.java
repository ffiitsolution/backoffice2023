/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffi.api.backoffice.dao.ProcessDao;
import com.ffi.api.backoffice.model.DetailOpname;
import com.ffi.api.backoffice.model.HeaderOpname;
import com.ffi.api.backoffice.utils.DynamicRowMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IT
 */
@Repository
public class ProcessDaoImpl implements ProcessDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    // String LocalDateTime.now().format(timeFormatter) = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
    // String LocalDateTime.now().format(dateFormatter) = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());

    DateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy");
    DateFormat dfYear = new SimpleDateFormat("yyyy");

    @Autowired
    public ProcessDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Value("${endpoint.warehouse}")
    private String urlWarehouse;
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
    public void updateFrayer(Map<String, String> balance) {
        String qy = "UPDATE M_MPCS_DETAIL SET FRYER_TYPE_CNT= :fryerTypeCnt,FRYER_TYPE_RESET= :fryerTypeReset,"
                + "STATUS= :status,USER_UPD= :userUpd,DATE_UPD= :dateUpd,TIME_UPD= :timeUpd,FRYER_TYPE_SEQ_CNT= :fryerTypeSeqCnt "
                + "where fryer_type=:fryerType and OUTLET_CODE=:outletCode and FRYER_TYPE_SEQ=:fryerTypeSeq ";
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("fryerType", balance.get("fryerType"));
        param.put("fryerTypeSeq", balance.get("fryerTypeSeq"));
        param.put("fryerTypeCnt", balance.get("fryerTypeCnt"));
        param.put("fryerTypeReset", balance.get("fryerTypeReset"));
        param.put("fryerTypeSeqCnt", balance.get("fryerTypeSeqCnt"));
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
        jdbcTemplate.update(qy2, param);
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
        jdbcTemplate.update(qy2, param);
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
    public void insertOrderHeader(Map<String, String> balance) {

        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);

        String qy = "INSERT INTO T_ORDER_HEADER (OUTLET_CODE,ORDER_TYPE,ORDER_ID,ORDER_NO,ORDER_DATE,ORDER_TO,CD_SUPPLIER,DT_DUE,DT_EXPIRED,REMARK,NO_OF_PRINT,STATUS,USER_UPD,DATE_UPD,TIME_UPD)"
                + " VALUES(:outletCode,:orderType,:orderId,:orderNo,:orderDate,:orderTo,:cdSupplier,:dtDue,:dtExpired,:remark,:noOfPrint,:status,:userUpd,:dateUpd,:timeUpd)";
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
        jdbcTemplate.update(qy, param);
    }

    @Override
    public void updateMCounter(Map<String, String> balance) {

        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);

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
        param.put("qty1", balance.get("qty1"));
        param.put("cdUom1", balance.get("cdUom1"));
        param.put("qty2", balance.get("qty2"));
        param.put("cdUom2", balance.get("cdUom2"));
        param.put("totalQtyStock", balance.get("totalQtyStock"));
        param.put("unitPrice", balance.get("unitPrice"));
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
        param.put("remark", balance.getRemark());
        param.put("status", balance.getStatus());
        param.put("userUpd", balance.getUserUpd());
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(qy, param);
        param.put("year", year);
        param.put("month", month);
        param.put("transType", balance.getTransType());
        jdbcTemplate.update(qry, param);
        balance.setOpnameNo(opNo);
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
        if(result == null || result.equalsIgnoreCase("[]") || result.isEmpty()){
            updateMCounterSop(transType,outletCode);
            return opnameNumber(year, month, transType, outletCode);
        } else {
            return result;
        }
    }

    @Override
    public void updateMCounterSop(String transType, String outletCode) {

        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);

        // update query ambil no, handle jika kosong by M Joko 19-12-23
        String selectSql = "SELECT COUNT(*) FROM m_counter WHERE OUTLET_CODE = :outletCode AND TRANS_TYPE = :transType AND YEAR = :year AND MONTH = :month";
        String updateSql = "UPDATE m_counter SET COUNTER_NO = COUNTER_NO + 1 WHERE OUTLET_CODE = :outletCode AND TRANS_TYPE = :transType AND YEAR = :year AND MONTH = :month";
        String insertSql = "INSERT INTO m_counter (OUTLET_CODE, TRANS_TYPE, YEAR, MONTH, COUNTER_NO) VALUES (:outletCode, :transType, :year, :month, 0)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("outletCode", outletCode);
        params.addValue("transType", transType);
        params.addValue("year", year);
        params.addValue("month", month);

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
    public void updateOpnameStatus(Map balance) {
        System.out.println("updateOpnameStatus balance = " + balance);
        Map param = new HashMap();
        Integer status = Integer.valueOf(balance.get("status").toString());
        System.out.println("updateOpnameStatus status = " + status);
        param.put("outletCode", balance.get("outletCode"));
        param.put("opnameNo", balance.get("opnameNo"));
        param.put("status", status);
        String qy = "update t_opname_header set status =:status WHERE OPNAME_NO = :opnameNo and OUTLET_CODE= :outletCode";
        jdbcTemplate.update(qy, param);
        if(status == 1){
            itemOpnameToStockCard(balance);
            // update by M Joko 20-dec-23 
            updateMCounterAfterStockOpname(balance);
        }
    }
    
    public void itemOpnameToStockCard(Map<String, String> balance) {
            System.out.println("updateOpnameStatus = 1 / Close");
            // update by M Joko 18-12-23
            Map param = new HashMap();
            param.put("opnameNo", balance.get("opnameNo"));
            param.put("userUpd", balance.getOrDefault("userUpd", "SYSTEM"));
            param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
            param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
            String qryListStockOpname = "SELECT * FROM ( SELECT od.OUTLET_CODE, (SELECT trans_date FROM m_outlet WHERE outlet_code = od.outlet_code) AS TRANS_DATE, od.ITEM_CODE, 'SOP' AS CD_TRANS, CASE WHEN od.qty_freeze < od.total_qty THEN od.total_qty - od.qty_freeze ELSE 0 END AS QUANTITY_IN, CASE WHEN od.qty_freeze > od.total_qty THEN od.qty_freeze - od.total_qty ELSE 0 END AS QUANTITY, :userUpd AS USER_UPD, :dateUpd AS DATE_UPD, :timeUpd AS TIME_UPD FROM T_OPNAME_DETAIL od WHERE od.opname_no = :opnameNo ) WHERE QUANTITY_IN NOT IN (0, '0') UNION ALL SELECT * FROM ( SELECT od.OUTLET_CODE, (SELECT trans_date FROM m_outlet WHERE outlet_code = od.outlet_code) AS TRANS_DATE, od.ITEM_CODE, 'SOP' AS CD_TRANS, CASE WHEN od.qty_freeze < od.total_qty THEN od.total_qty - od.qty_freeze ELSE 0 END AS QUANTITY_IN, CASE WHEN od.qty_freeze > od.total_qty THEN od.qty_freeze - od.total_qty ELSE 0 END AS QUANTITY, :userUpd AS USER_UPD, :dateUpd AS DATE_UPD, :timeUpd AS TIME_UPD FROM T_OPNAME_DETAIL od WHERE od.opname_no = :opnameNo ) WHERE QUANTITY NOT IN (0, '0')";
        
        String qryToStockCardDetail = "insert into t_stock_card_detail ( " + qryListStockOpname + " )";
        jdbcTemplate.update(qryToStockCardDetail, param);
        
        String qryUpdateStockCard = """
                                    UPDATE t_stock_card sc
                                    SET 
                                        sc.QTY_IN = sc.QTY_IN + NVL(
                                            (SELECT tsd.QUANTITY_IN
                                             FROM t_stock_card_detail tsd
                                             WHERE tsd.ITEM_CODE = sc.ITEM_CODE
                                               AND tsd.trans_date = sc.trans_date
                                               AND tsd.CD_TRANS = 'SOP'), 
                                            0
                                        ),
                                        sc.QTY_OUT = sc.QTY_OUT + NVL(
                                            (SELECT tsd.QUANTITY
                                             FROM t_stock_card_detail tsd
                                             WHERE tsd.ITEM_CODE = sc.ITEM_CODE
                                               AND tsd.trans_date = sc.trans_date
                                               AND tsd.CD_TRANS = 'SOP'), 
                                            0
                                        )
                                    WHERE sc.trans_date = (SELECT trans_date FROM m_outlet WHERE outlet_code = sc.outlet_code)""";
        jdbcTemplate.update(qryUpdateStockCard,param);
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

    @Override
    public void sendDataToWarehouse(Map<String, String> balance) {

        String json = "";
        Gson gson = new Gson();
        Map<String, Object> map1 = new HashMap<String, Object>();
        try {
            String qry1 = "SELECT   OUTLET_CODE AS KODE_PEMESAN, "
                    + "         CD_SUPPLIER AS KODE_TUJUAN, "
                    + "         'I' TIPE_PESANAN, "
                    + "         ORDER_NO AS NOMOR_PESANAN, "
                    + "         TO_CHAR (ORDER_DATE, 'DD-MON-YY') AS TGL_PESAN, "
                    + "         TO_CHAR (DT_DUE, 'DD-MON-YY') AS TGL_BRG_DIKIRIM, "
                    + "         TO_CHAR (DT_EXPIRED, 'DD-MON-YY') AS TGL_BATAS_EXP, "
                    + "         REMARK AS KETERANGAN1, "
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
                    Map<String, Object> rh = new HashMap<String, Object>();
                    String qry2 = "SELECT ITEM_CODE as KODE_BARANG,(TOTAL_QTY_STOCK/QTY_1) AS KONVERSI,CD_UOM_2 AS SATUAN_KECIL,"
                            + " CD_UOM_1 AS SATUAN_BESAR,QTY_1 AS QTY_PESAN_BESAR,QTY_2 AS QTY_PESAN_KECIL,"
                            + "  TOTAL_QTY_STOCK AS TOTAL_QTY_PESAN,'' AS TOTAL_QTY_KIRIM,UNIT_PRICE AS HARGA_UNIT,'000000' AS TIME_COUNTER,'N' AS SEND_FLAG"
                            + "  FROM T_ORDER_DETAIL WHERE ORDER_NO =:orderNo";
                    System.err.println("q2 :" + qry2);
                    List<Map<String, Object>> list2 = jdbcTemplate.query(qry2, prm, new RowMapper<Map<String, Object>>() {
                        @Override
                        public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                            Map<String, Object> rt = new HashMap<String, Object>();
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

                Map<String, Object> param = new HashMap<String, Object>();
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
            Map<String, String> histSend = new HashMap<String, String>();
            histSend.put("orderNo", balance.get("orderNo").toString());
            insertHistSend(histSend);
            //End by Dona
            //Add Insert to HIST_KIRIM by KP (13-06-2023)
            Map<String, String> histKirim = new HashMap<String, String>();
            histKirim.put("orderNo", balance.get("orderNo").toString());
            histKirim.put("sendUser", balance.get("userUpd").toString());
            InsertHistKirim(histKirim);
            //End added by KP

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Add Insert to Receiving Header & Detail by KP (07-06-2023)
    @Override
    public void InsertRecvHeaderDetail(JsonObject balancing) {
        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);
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
                + " select outlet_code,cd_supplier as tujuan_kirim,order_no as\n"
                + "      No_order,'S' as status_kirim,:sendDate,:sendHour,B.\n"
                + "      STAFF_full_NAME as user_kirim\n"
                + "      from t_order_header A left join (\n"
                + "      select STAFF_CODE,STAFF_full_NAME\n"
                + "      FROM m_staff ) B on A.user_upd=B.staff_code\n"
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
            String sqlId = "select to_char(nvl(max(substr(wastage_id, -3)) + 1, 1), 'fm000') as no_urut "
                    + "from t_wastage_header "
                    + "where wastage_id like '" + outletCode.concat("0").concat(month) + "%' "
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
    }
    //End added by KP

    //Insert MPCS by Kevin (08-08-2023)
    @Override
    public void InsertMPCSTemplate(JsonObject balancing) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        DateFormat df2 = new SimpleDateFormat("HHmmss");
        int interval = balancing.getAsJsonObject().getAsJsonPrimitive("interval").getAsInt();
        String startTime = balancing.getAsJsonObject().getAsJsonPrimitive("startTime").getAsString();
        String endTime = balancing.getAsJsonObject().getAsJsonPrimitive("endTime").getAsString();

        //Delete existing
        String sqlDel = "delete from template_mpcs where outlet_code = :outletCode ";
        Map paramDel = new HashMap();
        paramDel.put("outletCode", balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
        jdbcTemplate.update(sqlDel, paramDel);

        String sql = "insert into template_mpcs(outlet_code, seq_mpcs, time_mpcs, user_upd, date_upd, time_upd) "
                + "values(:outletCode, :seq, :timeMpcs, :userUpd, :dateUpd, :timeUpd) ";
        System.err.println("MPCS query :" + sql);
        Map param = new HashMap();
        try {
            Date strTime = df.parse(startTime);
            Date enTime = df.parse(endTime);
            Date countTime = df.parse(startTime);
            long differ = (enTime.getTime() - strTime.getTime()) / 1000;
            int loop = (int) differ / 3600;
            //System.err.println("How many hours: " + loop);
            loop = (loop % 3600) * (60 / interval);
            //System.err.println("How many 30 minutes: " + loop);
            for (int i = 0; i <= loop; i++) {
                //System.err.println("Iteration: " + i + ", the time is: " + df.format(countTime));
                param.put("outletCode", balancing.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
                param.put("seq", (i + 1));
                param.put("timeMpcs", df2.format(countTime));
                param.put("userUpd", balancing.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString());
                param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
                param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
                jdbcTemplate.update(sql, param);
                //System.err.println(param);
                param.clear();
                countTime = new Date(countTime.getTime() + (interval * 60 * 1000));
            }
        } catch (Exception ex) {
            System.err.println("Error DateTime: " + ex);
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
        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);

        //Getting last number for Return Order
        String noID = returnOrderCounter(year, month, "ID", param.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
        String noReturn = returnOrderCounter(year, month, "RTR", param.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());

        //Insert Header
        String queryHeader = "INSERT INTO T_RETURN_HEADER (OUTLET_CODE, TYPE_RETURN, RETURN_ID, RETURN_NO, RETURN_DATE,"
                + " RETURN_TO, REMARK, STATUS, USER_UPD, DATE_UPD, TIME_UPD) VALUES (:outletCode, :typeReturn, :returnId,"
                + " :returnNo, :returnDate, :returnTo, :remark, :status, :userUpd, :dateUpd, :timeUpd)";

        Map<String, Object> prm = new HashMap<>();
        prm.put("outletCode", param.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString());
        prm.put("typeReturn", param.getAsJsonObject().getAsJsonPrimitive("typeReturn").getAsString());
        prm.put("returnId", noID);
        prm.put("returnNo", noReturn);
        prm.put("returnDate", param.getAsJsonObject().getAsJsonPrimitive("returnDate").getAsString());
        prm.put("returnTo", param.getAsJsonObject().getAsJsonPrimitive("returnTo").getAsString());
        prm.put("remark", param.getAsJsonObject().getAsJsonPrimitive("remark").getAsString());
        prm.put("status", param.getAsJsonObject().getAsJsonPrimitive("status").getAsString());
        prm.put("userUpd", param.getAsJsonObject().getAsJsonPrimitive("userUpd").getAsString());
        prm.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        prm.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        jdbcTemplate.update(queryHeader, prm);

        //Insert Detail
        ObjectMapper objectMapper = new ObjectMapper();
        JsonArray emp = param.getAsJsonObject().getAsJsonArray("itemList");
        StringBuilder query = new StringBuilder();
        String outletCode = param.getAsJsonObject().getAsJsonPrimitive("outletCode").getAsString();
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
    public void sendDataOutletToWarehouse(Map<String, String> balance) {
        String json = "";
        Gson gson = new Gson();
        Map<String, Object> map1 = new HashMap<String, Object>();
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
                    + "REMARK,        "
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
                    Map<String, Object> rh = new HashMap<String, Object>();
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
                            Map<String, Object> rt = new HashMap<String, Object>();
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

                Map<String, Object> param = new HashMap<String, Object>();
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
            Map<String, String> histSend = new HashMap<String, String>();
            histSend.put("orderNo", balance.get("orderNo").toString());
            insertHistSend(histSend);

            Map<String, String> histKirim = new HashMap<String, String>();
            histKirim.put("orderNo", balance.get("orderNo").toString());
            histKirim.put("sendUser", balance.get("userUpd").toString());
            InsertHistKirim(histKirim);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////New method for insert Eod Hist POS 'N' ke eod - M Joko M 11-Dec-2023////////////
    @Override
    public void insertEodPosN(Map<String, String> balance) {
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
        System.err.println("q_process: " + query);
        jdbcTemplate.update(query, param);
    }

    ////////////New method for insert T Stock Card EOD - M Joko M 11-Dec-2023////////////
    @Override
    public void insertTStockCard(Map<String, String> balance) {
        String query = "insert into t_stock_card (OUTLET_CODE, TRANS_DATE, ITEM_CODE, ITEM_COST, QTY_BEGINNING, QTY_IN, QTY_OUT, REMARK, USER_UPD, DATE_UPD, TIME_UPD) values (:outletCode, (select trans_date from m_outlet where outlet_code=:outletCode)+1, :itemCode, :itemCost, :qtyBeginning, :qtyIn, :qtyOut, :remark, :userUpd, :dateUpd, :timeUpd)";
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("itemCode", balance.get("itemCode"));
        param.put("itemCost", balance.get("itemCost"));
        param.put("qtyBeginning", balance.get("qtyBeginning"));
        param.put("qtyIn", balance.get("qtyIn"));
        param.put("qtyOut", balance.get("qtyOut"));
        param.put("remark", balance.get("remark"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        System.err.println("q_process: " + query);
        jdbcTemplate.update(query, param);
    }

    ////////////New method for insert T EOD Hist - M Joko M 12-Dec-2023////////////
    @Override
    public void insertTEodHist(Map<String, String> balance) {
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
        System.err.println("q_process: " + query);
        jdbcTemplate.update(query, param);
    }

    ////////////New method for increase trans_date M Outlet selesai EOD - M Joko M 11-Dec-2023////////////
    @Override
    public void increaseTransDateMOutlet(Map<String, String> balance) {
        String query = "update m_outlet set trans_date = (select trans_date from m_outlet where outlet_code=:outletCode) + 1, user_upd = :userUpd, time_upd = :timeUpd, date_upd = :dateUpd where outlet_code = :outletCode";
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        System.err.println("q_process: " + query);
        jdbcTemplate.update(query, param);
    }

    ///////////////NEW METHOD insert T SUMM MPCS - M Joko 14/12/2023//// 
    @Override
    public void insertTSummMpcs(Map<String, String> balance) {
        // query insert from Dona
        String query = "insert into t_summ_mpcs (select A.OUTLET_CODE ,A.MPCS_GROUP ,A.DATE_MPCS +1 AS DATE_MPCS ,A.SEQ_MPCS ,A.TIME_MPCS ,TOT AS QTY_PROJ_CONV ,A.UOM_PROJ_CONV ,TOT AS QTY_PROJ ,A.UOM_PROJ ,sum(B.tot)over(PARTITION BY A.MPCS_GROUP ORDER BY A.SEQ_MPCS ) as QTY_ACC_PROJ ,A.UOM_ACC_PROJ ,A.DESC_PROD ,0 as QTY_PROD ,A.UOM_PROD ,0 as QTY_ACC_PROD ,A.UOM_ACC_PROD ,A.PROD_BY ,0 as QTY_SOLD ,A.UOM_SOLD ,0 as QTY_ACC_SOLD ,A.UOM_ACC_SOLD ,0 as QTY_REJECT ,A.UOM_REJECT ,0 as QTY_ACC_REJECT ,A.UOM_ACC_REJECT ,0 as QTY_WASTAGE ,A.UOM_WASTAGE ,0 as QTY_ACC_WASTAGE ,A.UOM_ACC_WASTAGE ,0 as QTY_ONHAND ,A.UOM_ONHAND ,0 as QTY_ACC_ONHAND ,A.UOM_ACC_ONHAND, TOT as QTY_VARIANCE, A.UOM_VARIANCE ,0 as QTY_ACC_VARIANCE ,A.UOM_ACC_VARIANCE ,A.USER_UPD ,A.DATE_UPD ,A.TIME_UPD ,0 as QTY_IN ,0 as QTY_OUT from t_summ_mpcs A LEFT JOIN (SELECT OUTLET_CODE,MPCS_GROUP,SEQ_MPCS,TIME_MPCS,TOT FROM ( select OUTLET_CODE,mpcs_group,seq_mpcs,time_mpcs,sum(qty_sold),sum(qty_sold1),sum(qty_sold2),round((sum(qty_sold)+sum(qty_sold1)+sum(qty_sold2))/3) as tot from( select OUTLET_CODE,mpcs_group,date_mpcs,seq_mpcs,time_mpcs,qty_sold,0 as qty_sold1, 0 as qty_sold2 from t_summ_mpcs where date_mpcs in(select trans_date-7 from m_outlet where outlet_code=:outletCode) union all select OUTLET_CODE,mpcs_group,date_mpcs,seq_mpcs,time_mpcs,0 as qty_sold,qty_sold as qty_sold1, 0 as qty_sold2 from t_summ_mpcs where date_mpcs in(select trans_date-14 from m_outlet where outlet_code=:outletCode) union all select OUTLET_CODE,mpcs_group,date_mpcs,seq_mpcs,time_mpcs,0 as qty_sold,0 as qty_sold1,qty_sold as qty_sold2 from t_summ_mpcs where date_mpcs in(select trans_date-21 from m_outlet where outlet_code=:outletCode) ) group by OUTLET_CODE,mpcs_group,seq_mpcs,time_mpcs order by mpcs_group asc,seq_mpcs) B)B ON A.OUTLET_CODE=B.OUTLET_CODE AND A.MPCS_GROUP=B.MPCS_GROUP AND A.SEQ_MPCS=B.SEQ_MPCS AND A.TIME_MPCS=B.TIME_MPCS WHERE DATE_MPCS IN(select trans_date from m_outlet where outlet_code=:outletCode))";
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        System.err.println("q insertTSummMpcs: " + query);
        jdbcTemplate.update(query, param);
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
        System.out.println(qy);
        jdbcTemplate.update(qy, param);
    }

    ////////////New method for insert m_counter setelah Stock Opname - M Joko M 20-Dec-2023////////////
    // jika row dengan menu_id, year, month sudah ada, tidak akan insert
    public void updateMCounterAfterStockOpname(Map<String, String> balance) {
        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        String qryCounter = "INSERT INTO m_counter (OUTLET_CODE, TRANS_TYPE, YEAR, MONTH, COUNTER_NO) ( SELECT mo.OUTLET_CODE, menu_id AS TRANS_TYPE, CASE WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 THEN EXTRACT(YEAR FROM mo.TRANS_DATE) + 1 ELSE EXTRACT(YEAR FROM mo.TRANS_DATE) END AS YEAR, CASE WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 THEN 1 ELSE EXTRACT(MONTH FROM mo.TRANS_DATE) END AS MONTH, 0 AS COUNTER_NO FROM M_MENUDTL m JOIN m_outlet mo ON mo.OUTLET_CODE = :outletCode WHERE m.TYPE_ID = 'counter' AND m.APLIKASI = 'SOP' AND m.STATUS = 'A' AND NOT EXISTS ( SELECT 1 FROM m_counter mc WHERE mc.OUTLET_CODE = mo.OUTLET_CODE AND mc.TRANS_TYPE = m.menu_id AND mc.YEAR = CASE WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 THEN EXTRACT(YEAR FROM mo.TRANS_DATE) + 1 ELSE EXTRACT(YEAR FROM mo.TRANS_DATE) END AND mc.MONTH = CASE WHEN EXTRACT(MONTH FROM mo.TRANS_DATE) = 12 THEN 1 ELSE EXTRACT(MONTH FROM mo.TRANS_DATE) END ))";
        System.err.println("q updateMCounterAfterStockOpname: " + qryCounter);
        jdbcTemplate.update(qryCounter,param);
    }
}
