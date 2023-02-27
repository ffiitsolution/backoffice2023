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
public class ProcessDaoImpl implements ProcessDao{
    
    private final NamedParameterJdbcTemplate jdbcTemplate;
    String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
    String dateNow = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());

    @Autowired
    public ProcessDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public void insertSupplier(Map<String, String> balancing) {
        String sql = "INSERT INTO M_SUPPLIER (CD_SUPPLIER,SUPPLIER_NAME,ADDRESS_1,ADDRESS_2,CITY,ZIP_CODE,PHONE,\n"
                + "FAX,HOMEPAGE,CP_NAME,CP_TITLE,CP_PHONE,CP_PHONE_EXT,CP_MOBILE,CP_EMAIL,FLAG_CANVASING,\n"
                + "STATUS,USER_UPD,DATE_UPD,TIME_UPD)\n"
                + "values(:cdSupplier,:supplierName,:address1,:address2,:city,:zipCode,:phone,:fax,:homepage,\n"
                + ":cpName,:cpTitle,:cpPhone,:cpPhoneExt,:cpMobile,:cpEmail,:flagCanvasing,:status,:userUpd,:dateUpd,:timeUpd)\n";
        Map param = new HashMap();
        param.put("cdSupplier", balancing.get("cdSupplier"));
        param.put("supplierName", balancing.get("supplierName"));
        param.put("address1", balancing.get("address1"));
        param.put("address2", balancing.get("address2"));
        param.put("city", balancing.get("city"));
        param.put("zipCode", balancing.get("zipCode"));
        param.put("phone", balancing.get("phone"));
        param.put("fax", balancing.get("fax"));
        param.put("homepage", balancing.get("homepage"));
        param.put("cpName", balancing.get("cpName"));
        param.put("cpTitle", balancing.get("cpTitle"));
        param.put("cpPhone", balancing.get("cpPhone"));
        param.put("cpPhoneExt", balancing.get("cpPhoneExt"));
        param.put("cpMobile", balancing.get("cpMobile"));
        param.put("cpEmail", balancing.get("cpEmail"));
        param.put("flagCanvasing", balancing.get("flagCanvasing"));
        param.put("status", balancing.get("status"));
        param.put("userUpd", balancing.get("userUpd"));
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
        jdbcTemplate.update(sql, param);
    }
    
    
}
