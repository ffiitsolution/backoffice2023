/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

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
    ///////////////NEW METHOD REPORT BY PASCA 23 MEI 2023////

    public List<Map<String, Object>> reportOrderEntry(Map<String, Object> param) throws IOException {
        String query = null;
        if (param.get("detail").equals(1.0)) {
            query = "SELECT  a.ORDER_NO, CASE WHEN a.ORDER_TYPE = '0' THEN 'Permintaan' ELSE 'Pembelian' END tipe, \n"
                    + "CONCAT(b.OUTLET_NAME, CONCAT(c.SUPPLIER_NAME, d.DESCRIPTION)) AS order_ke, a.REMARK, \n"
                    + "CASE WHEN a.status='0' then 'Open' \n"
                    + "when a.status='1' then 'Close' else 'Cancel' end as status, a.ORDER_DATE FROM T_ORDER_HEADER a \n"
                    + "LEFT JOIN M_OUTLET b ON a.CD_SUPPLIER = b.OUTLET_CODE\n"
                    + "LEFT JOIN M_SUPPLIER c ON a.CD_SUPPLIER = c.CD_SUPPLIER\n"
                    + "LEFT JOIN M_GLOBAL d ON a.CD_SUPPLIER = d.CODE AND d.COND =:city\n"
                    + "WHERE a.ORDER_TYPE IN (:tipe, :tipe2) AND a.ORDER_DATE BETWEEN :orderDateFrom AND :orderDateTo AND \n"
                    + "a.OUTLET_CODE = :outletCode ORDER BY ORDER_DATE";
            Map prm = new HashMap();
            prm.put("city", "X_" + param.get("city"));
            if (param.get("orderType").equals(0.0)) {
                prm.put("tipe", "1");
                prm.put("tipe2", "0");
            } else if (param.get("orderType").equals(1.0)) {
                prm.put("tipe", "0");
                prm.put("tipe2", "0");
            } else if (param.get("orderType").equals(2.0)) {
                prm.put("tipe", "1");
                prm.put("tipe2", "1");
            }
            prm.put("orderDateFrom", param.get("orderDateFrom"));
            prm.put("orderDateTo", param.get("orderDateTo"));
            prm.put("outletCode", param.get("outletCode"));
            List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rt = new HashMap<String, Object>();
                    rt.put("noOrder", rs.getString("ORDER_NO"));
                    rt.put("tipe", rs.getString("TIPE"));
                    rt.put("orderKe", rs.getString("ORDER_KE"));
                    rt.put("catatan", rs.getString("REMARK"));
                    rt.put("status", rs.getString("STATUS"));
                    rt.put("orderDete", rs.getString("ORDER_DATE"));
                    return rt;
                }
            });
            return list;
        } else if (param.get("detail").equals(0.0)) {
            query = "SELECT  a.ORDER_NO, CASE WHEN a.ORDER_TYPE = '0' THEN 'Permintaan' ELSE 'Pembelian' END tipe, \n"
                    + "CONCAT(b.OUTLET_NAME, CONCAT(c.SUPPLIER_NAME, d.DESCRIPTION)) AS order_ke, a.REMARK, \n"
                    + "CASE WHEN a.status='0' then 'Open' \n"
                    + "when a.status='1' then 'Close' else 'Cancel' end as status, a.ORDER_DATE, \n"
                    + "e.ITEM_CODE, f.ITEM_DESCRIPTION, e.QTY_1, e.CD_UOM_1, e.QTY_2, \n"
                    + "e.CD_UOM_2, e.TOTAL_QTY_STOCK, e.UNIT_PRICE FROM T_ORDER_HEADER a\n"
                    + "LEFT JOIN M_OUTLET b ON a.CD_SUPPLIER = b.OUTLET_CODE\n"
                    + "LEFT JOIN M_SUPPLIER c ON a.CD_SUPPLIER = c.CD_SUPPLIER\n"
                    + "LEFT JOIN M_GLOBAL d ON a.CD_SUPPLIER = d.CODE AND d.COND =:city\n"
                    + "LEFT JOIN T_ORDER_DETAIL e ON a.ORDER_NO = e.ORDER_NO \n"
                    + "LEFT JOIN M_ITEM f ON e.ITEM_CODE = f.ITEM_CODE \n"
                    + "WHERE a.ORDER_TYPE IN (:tipe, :tipe2) AND a.ORDER_DATE BETWEEN :orderDateFrom AND :orderDateTo AND \n"
                    + "a.OUTLET_CODE = :outletCode ORDER BY ORDER_DATE";

            Map prm = new HashMap();
            prm.put("city", "X_" + param.get("city"));
            if (param.get("orderType").equals(0.0)) {
                prm.put("tipe", "1");
                prm.put("tipe2", "0");
            } else if (param.get("orderType").equals(1.0)) {
                prm.put("tipe", "0");
                prm.put("tipe2", "0");
            } else if (param.get("orderType").equals(2.0)) {
                prm.put("tipe", "1");
                prm.put("tipe2", "1");
            }
            prm.put("orderDateFrom", param.get("orderDateFrom"));
            prm.put("orderDateTo", param.get("orderDateTo"));
            prm.put("outletCode", param.get("outletCode"));

            Map<String, List<Map<String, String>>> detailData = new HashMap<>();

            List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rt = new HashMap<String, Object>();
                    rt.put("noOrder", rs.getString("ORDER_NO"));
                    rt.put("tipe", rs.getString("TIPE"));
                    rt.put("orderKe", rs.getString("ORDER_KE"));
                    rt.put("catatan", rs.getString("REMARK"));
                    rt.put("status", rs.getString("STATUS"));
                    rt.put("orderDate", rs.getString("ORDER_DATE"));
                    if (rs.getString("ITEM_CODE") != null) {
                        Map<String, String> detail = new HashMap<>();
                        double valueQty1 = Double.valueOf(rs.getString("QTY_1"));
                        double valueQty2 = Double.valueOf(rs.getString("QTY_2"));
                        double valueTotal = Double.valueOf(rs.getString("TOTAL_QTY_STOCK"));
                        double valueUnit = Double.valueOf(rs.getString("UNIT_PRICE"));
                        DecimalFormat dfUnit = new DecimalFormat("#,##0.00");
                        DecimalFormat df = new DecimalFormat("#,##0.0000");

                        detail.put("kode", rs.getString("ITEM_CODE"));
                        detail.put("namaBarang", rs.getString("ITEM_DESCRIPTION"));
                        detail.put("qtyBesar", df.format(valueQty1) + " " + rs.getString("CD_UOM_1"));
                        detail.put("qtyKecil", df.format(valueQty2) + " " + rs.getString("CD_UOM_2"));
                        detail.put("totalQty", df.format(valueTotal) + " " + rs.getString("CD_UOM_2"));
                        detail.put("unitPrice", dfUnit.format(valueUnit));
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
                addList.put("orderDate", node.get("orderDate"));
                addList.put("noOrder", node.get("noOrder"));
                addList.put("catatan", node.get("catatan"));
                addList.put("orderKe", node.get("orderKe"));
                addList.put("tipe", node.get("tipe"));
                addList.put("status", node.get("status"));
                for (Map.Entry<String, List<Map<String, String>>> entry : detailData.entrySet()) {
                    if (entry.getKey().equals(node.get("noOrder").asText())) {
                        addList.put("detail", entry.getValue());
                    }
                }
                if (!addList.containsKey("detail")) {
                    List<String> detailNull = new ArrayList<>();
                    addList.put("detail", detailNull);
                }
                result.add(addList);
            }
            return result;
        }
        return null;
    }

    public List<Map<String, Object>> reportDeliveryOrder(Map<String, Object> param) {
        String query = "SELECT a.DELIVERY_NO, a.REQUEST_NO, CONCAT(b.OUTLET_NAME, \n"
                + "CONCAT(c.SUPPLIER_NAME, d.DESCRIPTION)) AS do_ke, a.REMARK, \n"
                + "CASE WHEN a.STATUS = '0' THEN 'Open' WHEN a.STATUS = '1' THEN 'Close' ELSE 'Cancel' END AS STATUS,\n"
                + "e.ITEM_CODE, f.ITEM_DESCRIPTION, e.QTY_PURCH, e.UOM_PURCH, e.QTY_STOCK, e.UOM_STOCK, e.TOTAL_QTY, a.DELIVERY_DATE \n"
                + "FROM T_DEV_HEADER a \n"
                + "LEFT JOIN M_OUTLET b ON a.OUTLET_TO = b.OUTLET_CODE  \n"
                + "LEFT JOIN M_SUPPLIER c ON a.OUTLET_TO = c.CD_SUPPLIER \n"
                + "LEFT JOIN M_GLOBAL d ON a.OUTLET_TO  = d.CODE AND d.COND =:city\n"
                + "LEFT JOIN T_DEV_DETAIL e ON a.REQUEST_NO = e.REQUEST_NO \n"
                + "LEFT JOIN M_ITEM f ON e.ITEM_CODE = f.ITEM_CODE \n"
                + "WHERE a.OUTLET_CODE =:outletCode AND a.DELIVERY_DATE BETWEEN :dateFrom AND :dateTo ORDER BY a.DELIVERY_DATE ASC";

        Map prm = new HashMap();
        prm.put("city", "X_" + param.get("city"));
        prm.put("outletCode", param.get("outletCode"));
        prm.put("dateFrom", param.get("dateFrom"));
        prm.put("dateTo", param.get("dateTo"));

        Map<String, List<Map<String, String>>> detailData = new HashMap<>();

        List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("noDo", rs.getString("DELIVERY_NO"));
                rt.put("noPermintaan", rs.getString("REQUEST_NO"));
                rt.put("doKe", rs.getString("DO_KE"));
                rt.put("catatan", rs.getString("REMARK"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("deliveryDate", rs.getString("DELIVERY_DATE"));
                if (rs.getString("ITEM_CODE") != null) {
                    Map<String, String> detail = new HashMap<>();
                    double valueQtyBesar = Double.valueOf(rs.getString("QTY_PURCH"));
                    double valueQtyKecil = Double.valueOf(rs.getString("QTY_STOCK"));
                    double valueTotal = Double.valueOf(rs.getString("TOTAL_QTY"));
                    DecimalFormat df = new DecimalFormat("#,##0.0000");

                    detail.put("kode", rs.getString("ITEM_CODE"));
                    detail.put("namaBarang", rs.getString("ITEM_DESCRIPTION"));
                    detail.put("qtyBerat", df.format(valueQtyBesar) + " " + rs.getString("UOM_PURCH"));
                    detail.put("qtyKecil", df.format(valueQtyKecil) + " " + rs.getString("UOM_STOCK"));
                    detail.put("totalQty", df.format(valueTotal) + " " + rs.getString("UOM_STOCK"));
                    detailData.computeIfAbsent(rs.getString("DELIVERY_NO"), k -> new ArrayList<>()).add(detail);
                }
                return rt;
            }
        });

        Set<Map<String, Object>> set = new HashSet<>(list.size());
        list.removeIf(p -> !set.add(p));
        if (param.get("detail").equals(1.0)) {
            return list;
        } else if (param.get("detail").equals(0.0)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(list);
            List<Map<String, Object>> result = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                Map<String, Object> addList = new HashMap<>();
                addList.put("noDo", node.get("noDo"));
                addList.put("noPermintaan", node.get("noPermintaan"));
                addList.put("doKe", node.get("doKe"));
                addList.put("catatan", node.get("catatan"));
                addList.put("status", node.get("status"));
                addList.put("deliveryDate", node.get("deliveryDate"));
                for (Map.Entry<String, List<Map<String, String>>> entry : detailData.entrySet()) {
                    if (entry.getKey().equals(node.get("noDo").asText())) {
                        addList.put("detail", entry.getValue());
                    }
                }
                if (!addList.containsKey("detail")) {
                    List<String> detailNull = new ArrayList<>();
                    addList.put("detail", detailNull);
                }
                result.add(addList);
            }
            return result;
        }
        return null;
    }
    /////////////////////////////////DONE///////////////////////////////////////

    ///////////////NEW METHOD REPORT receive BY PASCA 24 MEI 2023////
    @Override
    public List<Map<String, Object>> reportReceiving(Map<String, Object> param) {
        String query = "SELECT a.RECV_NO, a.ORDER_NO, a.REMARK, CONCAT(c.OUTLET_NAME, CONCAT(d.SUPPLIER_NAME, e.DESCRIPTION)) AS penerimaan_dari , \n"
                + "CASE WHEN a.STATUS = '0' THEN 'Open' WHEN a.STATUS = '1' THEN 'Close' ELSE 'Cancel' END AS STATUS,\n"
                + "f.ITEM_CODE, g.ITEM_DESCRIPTION, f.QTY_1, f.CD_UOM_1, f.QTY_2, f.CD_UOM_2, f.TOTAL_QTY, a.RECV_DATE \n"
                + "FROM T_RECV_HEADER a\n"
                + "LEFT JOIN T_ORDER_HEADER b ON a.ORDER_NO = b.ORDER_NO \n"
                + "LEFT JOIN M_OUTLET c ON b.CD_SUPPLIER = c.OUTLET_CODE \n"
                + "LEFT JOIN M_SUPPLIER d ON b.CD_SUPPLIER = d.CD_SUPPLIER \n"
                + "LEFT JOIN M_GLOBAL e ON b.CD_SUPPLIER = e.CODE AND e.COND =:city\n"
                + "LEFT JOIN T_RECV_DETAIL f ON a.RECV_NO = f.RECV_NO \n"
                + "LEFT JOIN M_ITEM g ON f.ITEM_CODE = g.ITEM_CODE \n"
                + "WHERE a.OUTLET_CODE =:outletCode AND a.RECV_DATE BETWEEN :dateFrom AND :dateTo ORDER BY a.RECV_DATE";

        Map prm = new HashMap();
        prm.put("city", "X_" + param.get("city"));
        prm.put("outletCode", param.get("outletCode"));
        prm.put("dateFrom", param.get("dateFrom"));
        prm.put("dateTo", param.get("dateTo"));

        Map<String, List<Map<String, String>>> detailData = new HashMap<>();

        List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("recvNo", rs.getString("RECV_NO"));
                rt.put("orderNo", rs.getString("ORDER_NO"));
                rt.put("catatan", rs.getString("REMARK"));
                rt.put("penerimaanDari", rs.getString("PENERIMAAN_DARI"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("recvDate", rs.getString("RECV_DATE"));
                if (rs.getString("ITEM_CODE") != null) {
                    Map<String, String> detail = new HashMap<>();
                    double valueQtyBesar = Double.valueOf(rs.getString("QTY_1"));
                    double valueQtyKecil = Double.valueOf(rs.getString("QTY_2"));
                    double valueTotal = Double.valueOf(rs.getString("TOTAL_QTY"));
                    DecimalFormat df = new DecimalFormat("#,##0.0000");

                    detail.put("kode", rs.getString("ITEM_CODE"));
                    detail.put("namaBarang", rs.getString("ITEM_DESCRIPTION"));
                    detail.put("qtyBesar", df.format(valueQtyBesar) + " " + rs.getString("CD_UOM_1"));
                    detail.put("qtyKecil", df.format(valueQtyKecil) + " " + rs.getString("CD_UOM_2"));
                    detail.put("totalQty", df.format(valueTotal) + " " + rs.getString("CD_UOM_2"));
                    detailData.computeIfAbsent(rs.getString("RECV_NO"), k -> new ArrayList<>()).add(detail);
                }
                return rt;
            }
        });
        Set<Map<String, Object>> set = new HashSet<>(list.size());
        list.removeIf(p -> !set.add(p));
        if (param.get("detail").equals(1.0)) {
            return list;
        } else if (param.get("detail").equals(0.0)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(list);
            List<Map<String, Object>> result = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                Map<String, Object> addList = new HashMap<>();
                addList.put("recvNo", node.get("recvNo"));
                addList.put("orderNo", node.get("orderNo"));
                addList.put("catatan", node.get("catatan"));
                addList.put("penerimaanDari", node.get("penerimaanDari"));
                addList.put("status", node.get("status"));
                addList.put("recvDate", node.get("recvDate"));
                for (Map.Entry<String, List<Map<String, String>>> entry : detailData.entrySet()) {
                    if (entry.getKey().equals(node.get("recvNo").asText())) {
                        addList.put("detail", entry.getValue());
                    }
                }
                if (!addList.containsKey("detail")) {
                    List<String> detailNull = new ArrayList<>();
                    addList.put("detail", detailNull);
                }
                result.add(addList);
            }
            return result;
        }

        return null;
    }
    /////////////////////////////////DONE///////////////////////////////////////

}
