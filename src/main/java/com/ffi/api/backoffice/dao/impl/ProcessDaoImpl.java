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
        //String qy = "UPDATE BUDGET_HEADER SET STATUS=:status where trans_no=:transNo ";
        String qy = "UPDATE M_MPCS_DETAIL SET STATUS=:stattus,USER_UPD:userUpd,DATE_UPD=:dateUpd:,TIME_UPD:timeUpd where fryer_type=:fryerType and fryer_type_seq=:fryerTypeSeq";
        Map param = new HashMap();
        param.put("stattus", balance.get("stattus"));
        param.put("fryerType", balance.get("fryerType"));
        param.put("fryerTypeSeq", balance.get("fryerTypeSeq"));
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
        String qy = "update m_pos set ref_no=:refNo,status=:status,pos_type=:posType where region_code=:regionCode and outlet_code=:outletCode and pos_code=:posCode";        Map param = new HashMap();
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
             ///////////////Updated By Pandu 14-03-2023////////////////////////////
    // ========================================================== MODULE MASTER STAFF (M_STAFF) =========================================================================================//   
    @Override
    public void InsertStaff(Map<String, String> balancetest1) 
    {
            String qy = "INSERT INTO M_STAFF(REGION_CODE,OUTLET_CODE,STAFF_CODE,STAFF_NAME,STAFF_FULL_NAME,PASSWORD,ID_CARD,SEX,DATE_OF_BIRTH,ADDRESS_1,ADDRESS_2,CITY,"
                    + "PHONE_NO,MOBILE_PHONE_NO,EMPLOY_DATE,RESIGN_DATE,POSITION,ACCESS_LEVEL,RIDER_FLAG,GROUP_ID,STATUS)"
                    + "VALUES(:regionCode,:outletCode,:staffCode,:staffName,:staffFullName,:passwordCode,:idCard,:sexType,:dateOfBirth,:address1,:address2,"
                    + ":cityCode,:phoneNumber,:mobilePhoneNumber,:employDate,:resignDate,:positionName,:accesslevelCode,:riderFlag,"
                    + ":groupidName,:statusName)";       
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
            param.put("cityCode", balancetest1.get("cityCode"));  
            param.put("phoneNumber", balancetest1.get("phoneNumber"));  
            param.put("mobilePhoneNumber", balancetest1.get("mobilePhoneNumber"));  
            param.put("employDate", balancetest1.get("employDate"));  
            param.put("resignDate", balancetest1.get("resignDate"));  
           // param.put("staffPhoto", balancetest1.get("staffPhoto"));  
            param.put("positionName", balancetest1.get("positionName")); 
            param.put("accesslevelCode", balancetest1.get("accesslevelCode"));  
            param.put("riderFlag", balancetest1.get("riderFlag"));  
            param.put("groupidName", balancetest1.get("groupidName"));  
            param.put("statusName", balancetest1.get("statusName"));  
            
            jdbcTemplate.update(qy, param);
        
    }
    
    @Override
    public void UpdateStaff(Map<String, String> balancetest) 
    {
            String qy = "UPDATE M_STAFF SET STAFF_NAME=:staffName, STAFF_FULL_NAME=:staffFullName, PASSWORD=:passwordCode, ID_CARD=:idCard, "
                + "SEX=:sexType, DATE_OF_BIRTH=:dateOfBirth, ADDRESS_1=:address1, ADDRESS_2=:address2, CITY=:cityCode, PHONE_NO=:phoneNumber, "
                + "MOBILE_PHONE_NO=:mobilePhoneNumber, GROUP_ID=:groupidName, USER_UPD=:userUpd "
                + "WHERE STAFF_CODE=:staffCode ";    //    String qy = "UPDATE M_STAFF SET STAFF_NAME=:staffName, STAFF_FULL_NAME=:staffFullName, PASSWORD=:staffPassword, USER_UPD=:staffUserUpdate, DATE_UPD=:staffDateUpdate, TIME_UPD=:staffTimeUpdate where STAFF_CODE=:staffCode ";
     
        Map param = new HashMap();
            param.put("regionCode", balancetest.get("regionCode"));
            param.put("outletCode", balancetest.get("outletCode"));
            param.put("staffCode", balancetest.get("staffCode"));            
            param.put("staffName", balancetest.get("staffName"));            
            param.put("staffFullName", balancetest.get("staffFullName"));            
            param.put("passwordCode", balancetest.get("passwordCode"));  
            param.put("idCard", balancetest.get("idCard"));  
            param.put("sexType", balancetest.get("sexType"));  
            param.put("dateOfBirth", balancetest.get("dateOfBirth"));  
            param.put("address1", balancetest.get("address1"));  
            param.put("address2", balancetest.get("address2"));  
            param.put("cityCode", balancetest.get("cityCode"));  
            param.put("phoneNumber", balancetest.get("phoneNumber"));  
            param.put("mobilePhoneNumber", balancetest.get("mobilePhoneNumber"));  
            param.put("employDate", balancetest.get("employDate"));  
            param.put("resignDate", balancetest.get("resignDate"));  
           // param.put("staffPhoto", balancetest1.get("staffPhoto"));  
            param.put("positionName", balancetest.get("positionName")); 
            param.put("accesslevelCode", balancetest.get("accesslevelCode"));  
            param.put("riderFlag", balancetest.get("riderFlag"));  
            param.put("groupidName", balancetest.get("groupidName"));  
            param.put("statusName", balancetest.get("statusName"));
            param.put("userUpd", balancetest.get("userUpd"));
            
        jdbcTemplate.update(qy, param);
        
        // UPDATE DATA KE DALAM TABLE YG KEDUA
        String qy2 = "UPDATE M_POS_STAFF SET STAFF_NAME=:staffName2, STAFF_POS_CODE=:staffPosCode2 where STAFF_CODE=:staffCode2 ";
        Map param2 = new HashMap();
        param2.put("staffName2", balancetest.get("staffNamex2"));
        param2.put("staffPosCode2", balancetest.get("staffPosCodex2"));
        param2.put("staffCode2", balancetest.get("staffCodex2"));       
        jdbcTemplate.update(qy2, param2);
    }

    @Override
    public void DeleteStaff(Map<String, String> balancetest) 
    {
       // DELETE DATA KE DALAM TABLE YG PERTAMA
        String qy = "DELETE FROM M_STAFF WHERE STAFF_CODE=:staffCode ";
        Map param = new HashMap();
        param.put("staffCode", balancetest.get("staffCodex"));
        jdbcTemplate.update(qy, param);
    }
    // ==================================================================================================================================================================================//
    ///////////////Done////////////////////////////

}
