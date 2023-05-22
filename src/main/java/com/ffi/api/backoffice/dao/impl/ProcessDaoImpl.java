/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import com.ffi.api.backoffice.dao.ProcessDao;
import com.ffi.api.backoffice.model.DetailOpname;
import com.ffi.api.backoffice.model.HeaderOpname;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public ProcessDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
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
                + "QTY_1 =:qty1 ,"
                + "CD_UOM_1 =:cdUom1 ,"
                + "QTY_2 =:qty2 ,"
                + "CD_UOM_2 =:cdUom2 ,"
                + "TOTAL_QTY_STOCK =:totalQtyStock,"
                + "UNIT_PRICE =:unitPrice ,"
                + "USER_UPD =:userUpd ,"
                + "DATE_UPD =:dateUpd ,"
                + "TIME_UPD =:timeUpd "
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

        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);

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
                + "ORDER BY ITEM_CODE\n"
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
    }

    @Override
    public void insertScDtlToScHdr(Map<String, String> balance) {

        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);
        try {
            String qry = "SELECT OUTLET_CODE,TRANS_DATE,ITEM_CODE,CD_TRANS,QUANTITY_IN,QUANTITY"
                    + " FROM T_STOCK_CARD_DETAIL WHERE OUTLET_CODE = :outletCode AND CD_TRANS = 'SOP' AND TRANS_DATE = :opnameDate";
            Map prm = new HashMap();
            prm.put("opnameNo", balance.get("opnameNo"));
            prm.put("outletCode", balance.get("outletCode"));
            prm.put("opnameDate", balance.get("opnameDate"));
            prm.put("userUpd", balance.get("userUpd"));
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

                String remark = opn.get("cdTrans")+balance.get("opnameNo");
                String qry2 = "UPDATE T_STOCK_CARD \n"
                        + "SET QTY_IN = :qtyIn ,QTY_OUT = :qtyOut ,REMARK = :cdTrans , USER_UPD = :userUpd , \n"
                        + "DATE_UPD = :dateUpd, TIME_UPD = :timeUpd\n"
                        + "WHERE OUTLET_CODE = :outletCode AND TRANS_DATE = :opnameDate ";

                Map<String, Object> param = new HashMap<String, Object>();
                param.put("opnameNo", balance.get("opnameNo"));
                param.put("outletCode", balance.get("outletCode"));
                param.put("opnameDate", balance.get("opnameDate"));
                param.put("userUpd", balance.get("userUpd"));
                param.put("itemCode", opn.get("itemCode"));
                param.put("cdTrans", opn.get("cdTrans"));
                param.put("qtyIn", opn.get("qtyIn"));
                param.put("qtyOut", opn.get("qtyOut"));
                param.put("dateUpd", dateNow);
                param.put("timeUpd", timeStamp);
                jdbcTemplate.update(qry, param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
