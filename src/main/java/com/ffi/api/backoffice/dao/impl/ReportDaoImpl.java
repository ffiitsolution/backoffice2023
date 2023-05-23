/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffi.api.backoffice.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

@Repository
public class ReportDaoImpl implements ReportDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ReportDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> reportOrderEntry(Map<String, String> param) throws IOException {
        String query = null;
        if (param.get("detail").equals("false")) {
            query = "SELECT  a.ORDER_NO, CASE WHEN a.ORDER_TYPE = '0' THEN 'Permintaan' ELSE 'Pembelian' END tipe, \n" +
                    "CONCAT(b.OUTLET_NAME, CONCAT(c.SUPPLIER_NAME, d.DESCRIPTION)) AS order_ke, a.REMARK, \n" +
                    "CASE WHEN a.status='0' then 'Open' \n" +
                    "when a.status='1' then 'Close' else 'Cancel' end as status, a.ORDER_DATE FROM T_ORDER_HEADER a \n" +
                    "LEFT JOIN M_OUTLET b ON a.CD_SUPPLIER = b.OUTLET_CODE\n" +
                    "LEFT JOIN M_SUPPLIER c ON a.CD_SUPPLIER = c.CD_SUPPLIER\n" +
                    "LEFT JOIN M_GLOBAL d ON a.CD_SUPPLIER = d.CODE AND d.COND =:city\n" +
                    "WHERE a.ORDER_TYPE IN (:tipe, :tipe2) AND a.ORDER_DATE BETWEEN :order_date_from AND :order_date_to AND \n" +
                    "a.OUTLET_CODE = :outlet_code ORDER BY ORDER_DATE";
            Map prm = new HashMap();
            prm.put("city", "X_" + param.get("city"));
            if (param.get("order_type").toUpperCase().equals("SEMUA")) {
                prm.put("tipe", "1");
                prm.put("tipe2", "0");
            } else if (param.get("order_type").toUpperCase().equals("PERMINTAAN")) {
                prm.put("tipe", "0");
                prm.put("tipe2", "0");
            } else if (param.get("order_type").toUpperCase().equals("PEMBELIAN")) {
                prm.put("tipe", "1");
                prm.put("tipe2", "1");
            }
            prm.put("order_date_from", param.get("order_date_from"));
            prm.put("order_date_to", param.get("order_date_to"));
            prm.put("outlet_code", param.get("outlet_code"));
            List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rt = new HashMap<String, Object>();
                    rt.put("no_order", rs.getString("ORDER_NO"));
                    rt.put("tipe", rs.getString("TIPE"));
                    rt.put("order_ke", rs.getString("ORDER_KE"));
                    rt.put("catatan", rs.getString("REMARK"));
                    rt.put("status", rs.getString("STATUS"));
                    rt.put("order_dete", rs.getString("ORDER_DATE"));
                    return rt;
                }
            });
            return list;
        } else if (param.get("detail").equals("true")) {
            query = "SELECT  a.ORDER_NO, CASE WHEN a.ORDER_TYPE = '0' THEN 'Permintaan' ELSE 'Pembelian' END tipe, \n" +
                    "CONCAT(b.OUTLET_NAME, CONCAT(c.SUPPLIER_NAME, d.DESCRIPTION)) AS order_ke, a.REMARK, \n" +
                    "CASE WHEN a.status='0' then 'Open' \n" +
                    "when a.status='1' then 'Close' else 'Cancel' end as status, a.ORDER_DATE, \n" +
                    "e.ITEM_CODE, f.ITEM_DESCRIPTION, e.QTY_1, e.CD_UOM_1, e.QTY_2, \n" +
                    "e.CD_UOM_2, e.TOTAL_QTY_STOCK, e.UNIT_PRICE FROM T_ORDER_HEADER a\n" +
                    "LEFT JOIN M_OUTLET b ON a.CD_SUPPLIER = b.OUTLET_CODE\n" +
                    "LEFT JOIN M_SUPPLIER c ON a.CD_SUPPLIER = c.CD_SUPPLIER\n" +
                    "LEFT JOIN M_GLOBAL d ON a.CD_SUPPLIER = d.CODE AND d.COND =:city\n" +
                    "LEFT JOIN T_ORDER_DETAIL e ON a.ORDER_NO = e.ORDER_NO \n" +
                    "LEFT JOIN M_ITEM f ON e.ITEM_CODE = f.ITEM_CODE \n" +
                    "WHERE a.ORDER_TYPE IN (:tipe, :tipe2) AND a.ORDER_DATE BETWEEN :order_date_from AND :order_date_to AND \n" +
                    "a.OUTLET_CODE = :outlet_code ORDER BY ORDER_DATE";

            Map prm = new HashMap();
            prm.put("city", "X_" + param.get("city"));
            if (param.get("order_type").toUpperCase().equals("SEMUA")) {
                prm.put("tipe", "1");
                prm.put("tipe2", "0");
            } else if (param.get("order_type").toUpperCase().equals("PERMINTAAN")) {
                prm.put("tipe", "0");
                prm.put("tipe2", "0");
            } else if (param.get("order_type").toUpperCase().equals("PEMBELIAN")) {
                prm.put("tipe", "1");
                prm.put("tipe2", "1");
            }
            prm.put("order_date_from", param.get("order_date_from"));
            prm.put("order_date_to", param.get("order_date_to"));
            prm.put("outlet_code", param.get("outlet_code"));

            Map<String, List<Map<String, String>>> detailData = new HashMap<>();

            List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rt = new HashMap<String, Object>();
                    rt.put("no_order", rs.getString("ORDER_NO"));
                    rt.put("tipe", rs.getString("TIPE"));
                    rt.put("order_ke", rs.getString("ORDER_KE"));
                    rt.put("catatan", rs.getString("REMARK"));
                    rt.put("status", rs.getString("STATUS"));
                    rt.put("order_date", rs.getString("ORDER_DATE"));
                    if (rs.getString("ITEM_CODE") != null) {
                        Map<String, String> detail = new HashMap<>();
                        double valueQty1 = Double.valueOf(rs.getString("QTY_1"));
                        double valueQty2 = Double.valueOf(rs.getString("QTY_2"));
                        double valueTotal = Double.valueOf(rs.getString("TOTAL_QTY_STOCK"));
                        double valueUnit = Double.valueOf(rs.getString("UNIT_PRICE"));
                        DecimalFormat dfUnit = new DecimalFormat("#,##0.00");
                        DecimalFormat df = new DecimalFormat("#,##0.0000");

                        detail.put("kode", rs.getString("ITEM_CODE"));
                        detail.put("nama_barang", rs.getString("ITEM_DESCRIPTION"));
                        detail.put("qty_besar", df.format(valueQty1) + " " + rs.getString("CD_UOM_1"));
                        detail.put("qty_kecil", df.format(valueQty2) + " " + rs.getString("CD_UOM_2"));
                        detail.put("total_qty", df.format(valueTotal) + " " + rs.getString("CD_UOM_2"));
                        detail.put("unit_price", dfUnit.format(valueUnit));
                        detailData.computeIfAbsent(rs.getString("ORDER_NO"), k -> new ArrayList<>()).add(detail);
                    }
                    return rt;
                }
            });
            Set<Map<String, Object>> set = new HashSet<>(list.size());
            list.removeIf(p -> !set.add(p));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(list);

            List<Map<String, Object>> result = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                Map<String, Object> addList = new HashMap<>();
                addList.put("order_date", node.get("order_date"));
                addList.put("no_order", node.get("no_order"));
                addList.put("catatan", node.get("catatan"));
                addList.put("order_ke", node.get("order_ke"));
                addList.put("tipe", node.get("tipe"));
                addList.put("status", node.get("status"));
                for (Map.Entry<String, List<Map<String, String>>> entry : detailData.entrySet()) {
                    if (entry.getKey().equals(node.get("no_order").asText())) {
                        addList.put("detail", entry.getValue());
                    }
                }
                if (!addList.containsKey("detail")){
                    List<String> detailNull = new ArrayList<>();
                    addList.put("detail", detailNull);
                }
                result.add(addList);
            }
            return result;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> reportDeliveryOrder(Map<String, String> param) {
        String query = "SELECT a.DELIVERY_NO, a.REQUEST_NO, CONCAT(b.OUTLET_NAME, \n" +
                "CONCAT(c.SUPPLIER_NAME, d.DESCRIPTION)) AS do_ke, a.REMARK, \n" +
                "CASE WHEN a.STATUS = '0' THEN 'Open' WHEN a.STATUS = '1' THEN 'Close' ELSE 'Cancel' END AS STATUS,\n" +
                "e.ITEM_CODE, f.ITEM_DESCRIPTION, e.QTY_PURCH, e.UOM_PURCH, e.QTY_STOCK, e.UOM_STOCK, e.TOTAL_QTY, a.DELIVERY_DATE \n" +
                "FROM T_DEV_HEADER a \n" +
                "LEFT JOIN M_OUTLET b ON a.OUTLET_TO = b.OUTLET_CODE  \n" +
                "LEFT JOIN M_SUPPLIER c ON a.OUTLET_TO = c.CD_SUPPLIER \n" +
                "LEFT JOIN M_GLOBAL d ON a.OUTLET_TO  = d.CODE AND d.COND =:city\n" +
                "LEFT JOIN T_DEV_DETAIL e ON a.REQUEST_NO = e.REQUEST_NO \n" +
                "LEFT JOIN M_ITEM f ON e.ITEM_CODE = f.ITEM_CODE \n" +
                "WHERE a.OUTLET_CODE =:outlet_code AND a.DELIVERY_DATE BETWEEN :date_from AND :date_to ORDER BY a.DELIVERY_DATE ASC";

        Map prm = new HashMap();
        prm.put("city", "X_" + param.get("city"));
        prm.put("outlet_code", param.get("outlet_code"));
        prm.put("date_from", param.get("date_from"));
        prm.put("date_to", param.get("date_to"));

        Map<String, List<Map<String, String>>> detailData = new HashMap<>();

        List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("no_do", rs.getString("DELIVERY_NO"));
                rt.put("no_permintaan", rs.getString("REQUEST_NO"));
                rt.put("do_ke", rs.getString("DO_KE"));
                rt.put("catatan", rs.getString("REMARK"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("delivery_date", rs.getString("DELIVERY_DATE"));
                if (rs.getString("ITEM_CODE") != null){
                    Map<String, String> detail = new HashMap<>();
                    double valueQtyBesar = Double.valueOf(rs.getString("QTY_PURCH"));
                    double valueQtyKecil = Double.valueOf(rs.getString("QTY_STOCK"));
                    double valueTotal = Double.valueOf(rs.getString("TOTAL_QTY"));
                    DecimalFormat df = new DecimalFormat("#,##0.0000");

                    detail.put("kode", rs.getString("ITEM_CODE"));
                    detail.put("nama_barang", rs.getString("ITEM_DESCRIPTION"));
                    detail.put("qty_berat", df.format(valueQtyBesar) + " " + rs.getString("UOM_PURCH"));
                    detail.put("qty_kecil", df.format(valueQtyKecil) + " " + rs.getString("UOM_STOCK"));
                    detail.put("total_qty", df.format(valueTotal) + " " + rs.getString("UOM_STOCK"));
                    detailData.computeIfAbsent(rs.getString("DELIVERY_NO"), k -> new ArrayList<>()).add(detail);
                }
                return rt;
            }
        });

        Set<Map<String, Object>> set = new HashSet<>(list.size());
        list.removeIf(p -> !set.add(p));
        if (param.get("detail").equals("false")){
            return list;
        } else if (param.get("detail").equals("true")) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(list);
            List<Map<String, Object>> result = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                Map<String, Object> addList = new HashMap<>();
                addList.put("no_do", node.get("no_do"));
                addList.put("no_permintaan", node.get("no_permintaan"));
                addList.put("do_ke", node.get("do_ke"));
                addList.put("catatan", node.get("catatan"));
                addList.put("status", node.get("status"));
                addList.put("delivery_date", node.get("delivery_date"));
                for (Map.Entry<String, List<Map<String, String>>> entry : detailData.entrySet()) {
                    if (entry.getKey().equals(node.get("no_do").asText())) {
                        addList.put("detail", entry.getValue());
                    }
                }
                if (!addList.containsKey("detail")){
                    List<String> detailNull = new ArrayList<>();
                    addList.put("detail", detailNull);
                }
                result.add(addList);
            }
            return result;
        }
        return null;
    }
}