/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import com.ffi.api.backoffice.dao.ViewDao;
import com.ffi.api.backoffice.model.ParameterLogin;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dwi Prasetyo
 */
@Repository
public class ViewDoaImpl implements ViewDao{
    
    private final NamedParameterJdbcTemplate jdbcTemplate;
    
    @Autowired
    public ViewDoaImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<Map<String, Object>> loginJson(ParameterLogin ref) {
       String qry = "select OUTLET_CODE,STAFF_CODE,STAFF_NAME,STAFF_FULL_NAME,ID_CARD,POSITION,GROUP_ID from M_STAFF \n" +
                    "where STAFF_CODE = :staffCode and PASSWORD = :pass and OUTLET_CODE = :outletCode and STATUS = 'A'";
        Map prm = new HashMap();
        prm.put("staffCode", ref.getUserName());
        prm.put("pass", ref.getPassword());
        prm.put("outletCode", ref.getOutletCode());
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<>();
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("staffCode", rs.getString("STAFF_CODE"));
                rt.put("staffName", rs.getString("STAFF_NAME"));
                rt.put("staffFullName", rs.getString("STAFF_FULL_NAME"));
                rt.put("idCard", rs.getString("ID_CARD"));
                rt.put("position", rs.getString("POSITION"));
                rt.put("groupId", rs.getString("GROUP_ID"));
                return rt;
            }
        });
        return list;
    }
    
}
