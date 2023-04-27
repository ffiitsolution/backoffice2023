/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import com.ffi.api.backoffice.dao.ProcessDao;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
        //String qy = "UPDATE BUDGET_HEADER SET STATUS=:status where trans_no=:transNo ";
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
        //String qy = "UPDATE BUDGET_HEADER SET STATUS=:status where trans_no=:transNo ";
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
        jdbcTemplate.update(qy, param);
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
        String qy ="UPDATE T_ORDER_DETAIL SET QTY_1=:qty1,"
                + "CD_UOM_1=:cdUom1,QTY_2=:qty2,CD_UOM_2=:cdUom2,TOTAL_QTY_STOCK=:totalQtyStock,UNIT_PRICE=:unitPrice,USER_UPD=:userUpd, DATE_UPD=:dateUpd,TIME_UPD=:timeUpd"
                + " WHERE OUTLET_CODE=:outletCode AND ORDER_TYPE=:orderType AND ORDER_ID=:orderId AND ORDER_NO=:orderNo AND ITEM_CODE=:itemCode";
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
}
