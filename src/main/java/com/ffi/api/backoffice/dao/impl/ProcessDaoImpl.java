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
// outlet & item (Asep)16-03-2023   
        //insert pos
     @Override
    public void insertpos(Map<String, String> balance) {
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
    //update pos
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

}
