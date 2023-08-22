/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffi.api.backoffice.dao.ProcessDao;
import com.ffi.api.backoffice.model.DetailOpname;
import com.ffi.api.backoffice.model.HeaderOpname;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IT
 */
@Repository
public class ProcessDaoImpl implements ProcessDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
    String dateNow = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());

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
        String sql = "INSERT INTO M_SUPPLIER (CD_SUPPLIER,SUPPLIER_NAME,ADDRESS_1,ADDRESS_2,CITY,ZIP_CODE,PHONE,\n"
                + "FAX,HOMEPAGE,CP_NAME,CP_TITLE,CP_PHONE,CP_PHONE_EXT,CP_MOBILE,CP_EMAIL,FLAG_CANVASING,\n"
                + "STATUS,USER_UPD,DATE_UPD,TIME_UPD)\n"
                + "values(:cdSupplier,:supplierName,:address1,:address2,:city,:zipCode,:phone,:fax,:homepage,\n"
                + ":cpName,:cpTitle,:cpPhone,:cpPhoneExt,:cpMobile,:cpEmail,:flagCanvasing,:status,:userUpd,:dateUpd,:timeUpd)\n";
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
        jdbcTemplate.update(sql, param);
    }
    /////////////////////////////////done
    ///////////////new method from dona 28-02-2023////////////////////////////

    @Override
    public void updateSupplier(Map<String, String> balance) {
        String qy = "update m_supplier set SUPPLIER_NAME=:supplierName,\n"
                + "ADDRESS_1=:address1,\n"
                + "ADDRESS_2=:address2,\n"
                + "CITY=:city,\n"
                + "ZIP_CODE=:zipCode,\n"
                + "PHONE=:phone,\n"
                + "FAX=:fax,\n"
                + "HOMEPAGE=:homepage,\n"
                + "CP_NAME=:cpName,\n"
                + "CP_TITLE=:cpTitle,\n"
                + "CP_PHONE=:cpPhone,\n"
                + "CP_PHONE_EXT=:cpPhoneExt,\n"
                + "CP_MOBILE=:cpMobile,\n"
                + "CP_EMAIL=:cpEmail,\n"
                + "FLAG_CANVASING=:flagCanvasing,\n"
                + "STATUS=:status,\n"
                + "USER_UPD=:userUpd,\n"
                + "DATE_UPD=:dateUpd,\n"
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
        jdbcTemplate.update(qy, param);
    }
    /////////////////////////////////done

    ///////////////new method from dona 06-03-2023////////////////////////////
    public void updateMpcs(Map<String, String> balance) {
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);

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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
        param.put("posType", balance.get("posType"));
        jdbcTemplate.update(qy, param);
    }
    ////////////done
    ///////////////Updated By Pandu 30-03-2023////////////////////////////
    // MODULE MASTER STAFF (M_STAFF) Revisi by lani 31-03-23 //   

    @Override
    public void insertStaff(Map<String, String> balancetest1) {
        String qy = "INSERT INTO M_STAFF(REGION_CODE,OUTLET_CODE,STAFF_CODE,STAFF_NAME,PASSWORD,ID_CARD,SEX,DATE_OF_BIRTH,ADDRESS_1,ADDRESS_2,CITY,"
                + "PHONE_NO,MOBILE_PHONE_NO,EMPLOY_DATE,POSITION,ACCESS_LEVEL,RIDER_FLAG,GROUP_ID,STATUS,USER_UPD,DATE_UPD,TIME_UPD,STAFF_FULL_NAME)"
                + "VALUES(:regionCode,:outletCode,:staffCode,:staffName,:passwordCode,:idCard,:sexType,:dateOfBirth,:address1,:address2,"
                + ":code,:phoneNumber,:mobilePhoneNumber,:employDate,:positionCode,:accesslevelCode,:riderFlag,"
                + ":groupidName,:status,:userUpd,:dateUpd,:timeUpd,:staffFullName)";

        String qy2 = "INSERT INTO M_POS_STAFF(REGION_CODE,OUTLET_CODE,STAFF_CODE,STAFF_POS_CODE,STAFF_NAME,PASSWORD,"
                + "ACCESS_LEVEL,RIDER_FLAG,STATUS,USER_UPD,DATE_UPD,TIME_UPD)"
                + "VALUES(:regionCode,:outletCode,:staffCode,:staffPosCode,:staffName,:passPosCode,"
                + ":accesslevelCode,:riderFlag,:status,:userUpd,:dateUpd,:timeUpd)";

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
        param.put("positionCode", balancetest1.get("positionCode"));
        param.put("accesslevelCode", balancetest1.get("accessLevelCode"));
        param.put("riderFlag", balancetest1.get("riderFlag"));
        param.put("groupidName", balancetest1.get("groupCode"));
        param.put("staffPosCode", balancetest1.get("staffPosCode"));
        param.put("passPosCode", balancetest1.get("passPosCode"));
        param.put("status", balancetest1.get("status"));
        param.put("userUpd", balancetest1.get("userUpd"));
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);

        jdbcTemplate.update(qy, param);
        jdbcTemplate.update(qy2, param);
    }

    @Override
    public void updateStaff(Map<String, String> balancetest1) {
        String qy = "UPDATE M_STAFF SET STAFF_NAME = :staffName, ID_CARD = :idCard, STAFF_FULL_NAME = :staffFullName, ADDRESS_2 = :address2,"
                + "PASSWORD=:passwordCode, EMPLOY_DATE=:employDate, POSITION=:positionCode, ACCESS_LEVEL=:accesslevelCode, RIDER_FLAG=:riderFlag,"
                + "SEX=:sexType, DATE_OF_BIRTH=:dateOfBirth, ADDRESS_1=:address1, CITY=:code, PHONE_NO=:phoneNumber, "
                + "MOBILE_PHONE_NO=:mobilePhoneNumber, GROUP_ID=:groupidName, STATUS=:status, USER_UPD=:userUpd, DATE_UPD=:dateUpd, TIME_UPD=:timeUpd "
                + "WHERE STAFF_CODE=:staffCode AND OUTLET_CODE =:outletCode ";    //    String qy = "UPDATE M_STAFF SET STAFF_NAME=:staffName, STAFF_FULL_NAME=:staffFullName, PASSWORD=:staffPassword, USER_UPD=:dateUpd, DATE_UPD=:timeUpd, TIME_UPD=:staffTimeUpdate where STAFF_CODE=:staffCode ";

        String qy2 = "UPDATE M_POS_STAFF SET PASSWORD=:passPosCode,STATUS=:status,STAFF_POS_CODE=:staffPosCode,"
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
        param.put("positionCode", balancetest1.get("positionCode"));
        param.put("accesslevelCode", balancetest1.get("accessLevelCode"));
        param.put("riderFlag", balancetest1.get("riderFlag"));
        param.put("groupidName", balancetest1.get("groupCode"));
        param.put("staffPosCode", balancetest1.get("staffPosCode"));
        param.put("passPosCode", balancetest1.get("passPosCode"));
        param.put("status", balancetest1.get("status"));
        param.put("userUpd", balancetest1.get("userUpd"));
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);

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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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

        String qy1 = "update m_counter \n"
                + "       set COUNTER_NO = (select COUNTER_NO+1 FROM M_COUNTER \n"
                + "                        where YEAR = :year \n"
                + "                        AND MONTH= :month \n"
                + "                        AND TRANS_TYPE = 'ID' \n"
                + "                        AND OUTLET_CODE= :outletCode )\n"
                + "where YEAR = :year AND MONTH= :month AND TRANS_TYPE = 'ID' AND OUTLET_CODE= :outletCode";

        String qy2 = "update m_counter \n"
                + "       set COUNTER_NO = (select COUNTER_NO+1 FROM M_COUNTER \n"
                + "                        where YEAR = :year \n"
                + "                        AND MONTH= :month \n"
                + "                        AND TRANS_TYPE = :transType \n"
                + "                        AND OUTLET_CODE= :outletCode )\n"
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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

        String qy = "INSERT INTO T_OPNAME_HEADER (OUTLET_CODE,CD_TEMPLATE,OPNAME_NO,OPNAME_DATE,REMARK,STATUS,USER_UPD,DATE_UPD,TIME_UPD)\n"
                + "values(:outletCode,:cdTemplate,:opnameNo,:opnameDate,:remark,:status,:userUpd,:dateUpd,:timeUpd)";

        String qry = "INSERT INTO T_OPNAME_DETAIL (\n"
                + "SELECT SC.OUTLET_CODE,:opnameNo,SC.ITEM_CODE,\n"
                + "(SC.QTY_BEGINNING+SC.QTY_IN-SC.QTY_OUT) QTY_FREEZE,0 COST_FREEZE,\n"
                + "0 QTY_PURCH,I.UOM_PURCHASE,0 QTY_STOCK,I.UOM_STOCK,0 TOTAL_QTY,\n"
                + " :userUpd USER_UPD,:dateUpd DATE_UPD,:timeUpd TIME_UPD\n"
                + "FROM T_STOCK_CARD SC\n"
                + "LEFT JOIN M_ITEM I ON I.ITEM_CODE = SC.ITEM_CODE\n"
                + "WHERE SC.OUTLET_CODE = :outletCode AND I.STATUS = 'A'\n"
                + "AND SC.TRANS_DATE = :opnameDate \n"
                + ")";

        Map param = new HashMap();
        param.put("outletCode", balance.getOutletCode());
        param.put("cdTemplate", balance.getCdTemplate());
        param.put("opnameNo", opNo);
        param.put("opnameDate", balance.getOpnameDate());
        param.put("remark", balance.getRemark());
        param.put("status", balance.getStatus());
        param.put("userUpd", balance.getUserUpd());
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
        param.put("year", year);
        param.put("month", month);
        param.put("transType", balance.getTransType());
        jdbcTemplate.update(qy, param);
        jdbcTemplate.update(qry, param);
        balance.setOpnameNo(opNo);
    }

    public String opnameNumber(String year, String month, String transType, String outletCode) {

        String sql = "SELECT :transType||ORDER_ID||COUNTNO ORDER_ID FROM (\n"
                + "SELECT A.OUTLET_CODE||:month||A.YEAR AS ORDER_ID,A.COUNTER_NO+1 COUNTNO FROM M_COUNTER A\n"
                + "LEFT JOIN M_OUTLET B\n"
                + "ON B.OUTLET_CODE=A.OUTLET_CODE\n"
                + "WHERE A.YEAR = :year AND A.MONTH= :month AND A.TRANS_TYPE = :transType AND A.OUTLET_CODE= :outletCode)";
        Map param = new HashMap();
        param.put("year", year);
        param.put("month", month);
        param.put("transType", transType);
        param.put("outletCode", outletCode);
        return jdbcTemplate.queryForObject(sql, param, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString(1) == null ? "0" : rs.getString(1);

            }
        }).toString();
    }

    @Override
    public void updateMCounterSop(String transType, String outletCode) {

        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);

        String qy = "update m_counter set \n"
                + "counter_no = (select counter_no+1 from M_COUNTER WHERE YEAR = :year AND MONTH= :month AND TRANS_TYPE = :transType AND OUTLET_CODE= :outletCode)\n"
                + "WHERE YEAR = :year AND MONTH= :month AND TRANS_TYPE = :transType AND OUTLET_CODE= :outletCode";
        Map param = new HashMap();
        param.put("year", year);
        param.put("month", month);
        param.put("transType", transType);
        param.put("outletCode", outletCode);
        jdbcTemplate.update(qy, param);
    }

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
                qy = "INSERT INTO T_OPNAME_DETAIL (OUTLET_CODE,OPNAME_NO,ITEM_CODE,QTY_FREEZE,COST_FREEZE,QTY_PURCH,\n"
                        + "UOM_PURCH,QTY_STOCK,UOM_STOCK,TOTAL_QTY,USER_UPD,DATE_UPD,TIME_UPD)\n"
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
                param.put("dateUpd", dateNow);
                param.put("timeUpd", timeStamp);
                jdbcTemplate.update(qy, param);
            } else {
                qy = "UPDATE T_OPNAME_DETAIL \n"
                        + "SET QTY_PURCH = :qtyPurch , QTY_STOCK = :qtyStock, TOTAL_QTY = :totalQty,\n"
                        + "USER_UPD = :userUpd, DATE_UPD = :dateUpd , TIME_UPD = :timeUpd\n"
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
                param.put("dateUpd", dateNow);
                param.put("timeUpd", timeStamp);
                jdbcTemplate.update(qy, param);
            }
        }
    }

    public String cekOsDetails(String outletCode, String opnameNo, String itemCode) {

        String sql = "select count(*) cek \n"
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

        String qry = "INSERT INTO T_STOCK_CARD_DETAIL (\n"
                + "SELECT * FROM (\n"
                + "SELECT OUTLET_CODE,TRANS_DATE,ITEM_CODE,TRANS_TYPE,\n"
                + "QTY QTY_IN,0 QTY_OUT,\n"
                + "USER_UPD, DATE_UPD, TIME_UPD\n"
                + "FROM (\n"
                + "select OUTLET_CODE,:opnameDate TRANS_DATE,ITEM_CODE,:transType TRANS_TYPE,\n"
                + "TOTAL_QTY - (QTY_FREEZE) QTY,:userUpd USER_UPD,:dateUpd DATE_UPD,:timeUpd TIME_UPD\n"
                + "from T_OPNAME_DETAIL \n"
                + "where OUTLET_CODE = :outletCode AND OPNAME_NO = :opnameNo)\n"
                + "WHERE QTY < = 0\n"
                + "union all\n"
                + "SELECT OUTLET_CODE,TRANS_DATE,ITEM_CODE,TRANS_TYPE,\n"
                + "0 QTY_IN, QTY QTY_OUT,\n"
                + "USER_UPD, DATE_UPD, TIME_UPD\n"
                + "FROM (\n"
                + "select OUTLET_CODE,:opnameDate TRANS_DATE,ITEM_CODE,:transType TRANS_TYPE,\n"
                + "TOTAL_QTY - (QTY_FREEZE) QTY,:userUpd USER_UPD, :dateUpd DATE_UPD, :timeUpd TIME_UPD\n"
                + "from T_OPNAME_DETAIL \n"
                + "where OUTLET_CODE = :outletCode AND OPNAME_NO = :opnameNo)\n"
                + "WHERE QTY > 0)\n"
                + ")";

        Map param = new HashMap();
        param.put("outletCode", balance.get("outletCode"));
        param.put("opnameNo", balance.get("opnameNo"));
        param.put("opnameDate", balance.get("opnameDate"));
        param.put("transType", balance.get("transType"));
        param.put("userUpd", balance.get("userUpd"));
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
            prm.put("dateUpd", dateNow);
            prm.put("timeUpd", timeStamp);
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

                String qry2 = "UPDATE T_STOCK_CARD \n"
                        + "SET QTY_IN = :qtyIn ,QTY_OUT = :qtyOut ,REMARK = :cdTrans , USER_UPD = :userUpd , \n"
                        + "DATE_UPD = :dateUpd, TIME_UPD = :timeUpd\n"
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
                param1.put("dateUpd", dateNow);
                param1.put("timeUpd", timeStamp);
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
            prm.put("dateUpd", dateNow);
            prm.put("timeUpd", timeStamp);
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

                String qry2 = "UPDATE T_STOCK_CARD \n"
                        + "SET QTY_IN = :qtyIn ,QTY_OUT = :qtyOut ,REMARK = :cdTrans , USER_UPD = :userUpd , \n"
                        + "DATE_UPD = :dateUpd, TIME_UPD = :timeUpd\n"
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
                param.put("dateUpd", dateNow);
                param.put("timeUpd", timeStamp);
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
            String qry1 = "SELECT OUTLET_CODE,ORDER_TYPE,ORDER_ID,ORDER_NO,TO_CHAR(ORDER_DATE,'DD-MON-YY')ORDER_DATE,ORDER_TO,\n"
                    + "CD_SUPPLIER,TO_CHAR(DT_DUE,'DD-MON-YY')DT_DUE,TO_CHAR(DT_EXPIRED,'DD-MON-YY')DT_EXPIRED,REMARK,NO_OF_PRINT,STATUS\n"
                    + "FROM T_ORDER_HEADER WHERE ORDER_NO = :orderNo ";
            Map prm = new HashMap();
            prm.put("orderNo", balance.get("orderNo"));
            System.err.println("q1 :" + qry1);
            List<Map<String, Object>> list = jdbcTemplate.query(qry1, prm, new RowMapper<Map<String, Object>>() {
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rh = new HashMap<String, Object>();
                    String qry2 = "SELECT ITEM_CODE,QTY_1,CD_UOM_1,QTY_2,CD_UOM_2,\n"
                            + "TOTAL_QTY_STOCK,UNIT_PRICE\n"
                            + "FROM T_ORDER_DETAIL WHERE ORDER_NO = :orderNo ";
                    List<Map<String, Object>> list2 = jdbcTemplate.query(qry2, prm, new RowMapper<Map<String, Object>>() {
                        @Override
                        public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                            Map<String, Object> rt = new HashMap<String, Object>();
                            rt.put("itemCode", rs.getString("ITEM_CODE"));
                            rt.put("qty1", rs.getString("QTY_1"));
                            rt.put("cdUom1", rs.getString("CD_UOM_1"));
                            rt.put("qty2", rs.getString("QTY_2"));
                            rt.put("cdUom2", rs.getString("CD_UOM_2"));
                            rt.put("totalQtyStock", rs.getString("TOTAL_QTY_STOCK"));
                            rt.put("unitPrice", rs.getString("UNIT_PRICE"));
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
                param.put("userUpd", balance.get("userUpd"));
                param.put("itemList", dtl.get("itemList"));
                param.put("dateUpd", dateNow);
                param.put("timeUpd", timeStamp);

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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
        jdbcTemplate.update(qy, param);
    }

    //Add Insert to HIST_KIRIM by KP (13-06-2023)
    public void InsertHistKirim(Map<String, String> balance) {
        String qy = "INSERT INTO t_recv_detail (OUTLET_CODE, TUJUAN_KIRIM, NO_ORDER, STATUS_KIRIM, TGL_KIRIM, JAM_KIRIM, USER_KIRIM) "
                + "SELECT OUTLET_CODE, CD_SUPPLIER, ORDER_NO, 'S', :sendDate, :sendHour, :sendUser "
                + "FROM T_ORDER_HEADER "
                + "WHERE ORDER_NO = :orderNo";
        Map param = new HashMap();
        param.put("orderNo", balance.get("orderNo"));
        param.put("sendDate", dateNow);
        param.put("sendHour", timeStamp);
        param.put("sendUser", balance.get("sendUser"));
        jdbcTemplate.update(qy, param);
    }
    //End added by KP

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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
                param.put("dateUpd", dateNow);
                param.put("timeUpd", timeStamp);
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
            param.put("dateUpd", dateNow);
            param.put("timeUpd", timeStamp);
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
        prm.put("dateUpd", dateNow);
        prm.put("timeUpd", timeStamp);
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
                    .append("', '").append(userUpd).append("', '").append(dateNow).append("', '")
                    .append(timeStamp).append("')");
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
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
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
        jdbcTemplate.update(qy, param);
        System.out.println("query insert header: " + qy);
    }

////////////////////////DONE
}
