/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffi.api.backoffice.dao.ReportDao;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Repository
public class ReportDaoImpl implements ReportDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
    String dateNow = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());

    @Autowired
    public ReportDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    ///////////////NEW METHOD REPORT BY PASCA 23 MEI 2023////

    public List<Map<String, Object>> reportOrderEntry(Map<String, Object> param) throws IOException {
        String query = null;
        if (param.get("detail").equals(0.0)) {
            query = "SELECT  a.ORDER_NO, CASE WHEN a.ORDER_TYPE = '0' THEN 'Permintaan' ELSE 'Pembelian' END order_type, \n"
                    + "CONCAT(b.OUTLET_NAME, CONCAT(c.SUPPLIER_NAME, d.DESCRIPTION)) AS order_ke, a.REMARK, \n"
                    + "CASE WHEN a.status='0' then 'Open' \n"
                    + "when a.status='1' then 'Close' else 'Cancel' end as status, a.ORDER_DATE FROM T_ORDER_HEADER a \n"
                    + "LEFT JOIN M_OUTLET b ON a.CD_SUPPLIER = b.OUTLET_CODE\n"
                    + "LEFT JOIN M_SUPPLIER c ON a.CD_SUPPLIER = c.CD_SUPPLIER\n"
                    + "LEFT JOIN M_GLOBAL d ON a.CD_SUPPLIER = d.CODE AND d.COND =:city\n"
                    + "WHERE a.ORDER_TYPE IN (:orderType1, :orderType2) AND a.ORDER_DATE BETWEEN :orderDateFrom AND :orderDateTo AND \n"
                    + "a.OUTLET_CODE = :outletCode ORDER BY ORDER_DATE";
            Map prm = new HashMap();
            prm.put("city", "X_" + param.get("city"));
            if (param.get("orderType").equals(0.0)) {
                prm.put("orderType1", "1");
                prm.put("orderType2", "0");
            } else if (param.get("orderType").equals(1.0)) {
                prm.put("orderType1", "0");
                prm.put("orderType2", "0");
            } else if (param.get("orderType").equals(2.0)) {
                prm.put("orderType1", "1");
                prm.put("orderType2", "1");
            }
            prm.put("orderDateFrom", param.get("orderDateFrom"));
            prm.put("orderDateTo", param.get("orderDateTo"));
            prm.put("outletCode", param.get("outletCode"));
            List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rt = new HashMap<String, Object>();
                    rt.put("orderNo", rs.getString("ORDER_NO"));
                    rt.put("orderType", rs.getString("ORDER_TYPE"));
                    rt.put("orderKe", rs.getString("ORDER_KE"));
                    rt.put("remark", rs.getString("REMARK"));
                    rt.put("status", rs.getString("STATUS"));
                    rt.put("orderDate", rs.getString("ORDER_DATE"));
                    return rt;
                }
            });
            return list;
        } else if (param.get("detail").equals(1.0)) {
            query = "SELECT  a.ORDER_NO, CASE WHEN a.ORDER_TYPE = '0' THEN 'Permintaan' ELSE 'Pembelian' END order_type, \n"
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
                    + "WHERE a.ORDER_TYPE IN (:orderType1, :orderType2) AND a.ORDER_DATE BETWEEN :orderDateFrom AND :orderDateTo AND \n"
                    + "a.OUTLET_CODE = :outletCode ORDER BY ORDER_DATE";

            Map prm = new HashMap();
            prm.put("city", "X_" + param.get("city"));
            if (param.get("orderType").equals(0.0)) {
                prm.put("orderType1", "1");
                prm.put("orderType2", "0");
            } else if (param.get("orderType").equals(1.0)) {
                prm.put("orderType1", "0");
                prm.put("orderType2", "0");
            } else if (param.get("orderType").equals(2.0)) {
                prm.put("orderType1", "1");
                prm.put("orderType2", "1");
            }
            prm.put("orderDateFrom", param.get("orderDateFrom"));
            prm.put("orderDateTo", param.get("orderDateTo"));
            prm.put("outletCode", param.get("outletCode"));

            Map<String, List<Map<String, String>>> detailData = new HashMap<>();

            List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rt = new HashMap<String, Object>();
                    rt.put("orderNo", rs.getString("ORDER_NO"));
                    rt.put("orderType", rs.getString("ORDER_TYPE"));
                    rt.put("orderKe", rs.getString("ORDER_KE"));
                    rt.put("remark", rs.getString("REMARK"));
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

                        detail.put("itemCode", rs.getString("ITEM_CODE"));
                        detail.put("ItemDescription", rs.getString("ITEM_DESCRIPTION"));
                        detail.put("cdUom1", df.format(valueQty1) + " " + rs.getString("CD_UOM_1"));
                        detail.put("cdUom2", df.format(valueQty2) + " " + rs.getString("CD_UOM_2"));
                        detail.put("totalQtyStock", df.format(valueTotal) + " " + rs.getString("CD_UOM_2"));
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
                addList.put("orderNo", node.get("orderNo"));
                addList.put("remark", node.get("remark"));
                addList.put("orderKe", node.get("orderKe"));
                addList.put("orderType", node.get("orderType"));
                addList.put("status", node.get("status"));
                for (Map.Entry<String, List<Map<String, String>>> entry : detailData.entrySet()) {
                    if (entry.getKey().equals(node.get("orderNo").asText())) {
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
                + "CONCAT(c.SUPPLIER_NAME, d.DESCRIPTION)) AS delivery_order_ke, a.REMARK, \n"
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
                rt.put("deliveryNo", rs.getString("DELIVERY_NO"));
                rt.put("requestNo", rs.getString("REQUEST_NO"));
                rt.put("deliveryOrderKe", rs.getString("DELIVERY_ORDER_KE"));
                rt.put("remark", rs.getString("REMARK"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("deliveryDate", rs.getString("DELIVERY_DATE"));
                if (rs.getString("ITEM_CODE") != null) {
                    Map<String, String> detail = new HashMap<>();
                    double valueQtyBesar = Double.valueOf(rs.getString("QTY_PURCH"));
                    double valueQtyKecil = Double.valueOf(rs.getString("QTY_STOCK"));
                    double valueTotal = Double.valueOf(rs.getString("TOTAL_QTY"));
                    DecimalFormat df = new DecimalFormat("#,##0.0000");

                    detail.put("itemCode", rs.getString("ITEM_CODE"));
                    detail.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                    detail.put("qtyPurch", df.format(valueQtyBesar) + " " + rs.getString("UOM_PURCH"));
                    detail.put("qtyStock", df.format(valueQtyKecil) + " " + rs.getString("UOM_STOCK"));
                    detail.put("totalQty", df.format(valueTotal) + " " + rs.getString("UOM_STOCK"));
                    detailData.computeIfAbsent(rs.getString("DELIVERY_NO"), k -> new ArrayList<>()).add(detail);
                }
                return rt;
            }
        });

        Set<Map<String, Object>> set = new HashSet<>(list.size());
        list.removeIf(p -> !set.add(p));
        if (param.get("detail").equals(0.0)) {
            return list;
        } else if (param.get("detail").equals(1.0)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(list);
            List<Map<String, Object>> result = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                Map<String, Object> addList = new HashMap<>();
                addList.put("deliveryNo", node.get("deliveryNo"));
                addList.put("requestNo", node.get("requestNo"));
                addList.put("deliveryOrderKe", node.get("deliveryOrderKe"));
                addList.put("remark", node.get("remark"));
                addList.put("status", node.get("status"));
                addList.put("deliveryDate", node.get("deliveryDate"));
                for (Map.Entry<String, List<Map<String, String>>> entry : detailData.entrySet()) {
                    if (entry.getKey().equals(node.get("deliveryNo").asText())) {
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
                rt.put("remark", rs.getString("REMARK"));
                rt.put("penerimaanDari", rs.getString("PENERIMAAN_DARI"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("recvDate", rs.getString("RECV_DATE"));
                if (rs.getString("ITEM_CODE") != null) {
                    Map<String, String> detail = new HashMap<>();
                    double valueQtyBesar = Double.valueOf(rs.getString("QTY_1"));
                    double valueQtyKecil = Double.valueOf(rs.getString("QTY_2"));
                    double valueTotal = Double.valueOf(rs.getString("TOTAL_QTY"));
                    DecimalFormat df = new DecimalFormat("#,##0.0000");

                    detail.put("itemCode", rs.getString("ITEM_CODE"));
                    detail.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                    detail.put("qty1", df.format(valueQtyBesar) + " " + rs.getString("CD_UOM_1"));
                    detail.put("qty2", df.format(valueQtyKecil) + " " + rs.getString("CD_UOM_2"));
                    detail.put("totalQty", df.format(valueTotal) + " " + rs.getString("CD_UOM_2"));
                    detailData.computeIfAbsent(rs.getString("RECV_NO"), k -> new ArrayList<>()).add(detail);
                }
                return rt;
            }
        });
        Set<Map<String, Object>> set = new HashSet<>(list.size());
        list.removeIf(p -> !set.add(p));
        if (param.get("detail").equals(0.0)) {
            return list;
        } else if (param.get("detail").equals(1.0)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(list);
            List<Map<String, Object>> result = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                Map<String, Object> addList = new HashMap<>();
                addList.put("recvNo", node.get("recvNo"));
                addList.put("orderNo", node.get("orderNo"));
                addList.put("remark", node.get("remark"));
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

    @Override
    public List<Map<String, Object>> reportReturnOrder(Map<String, Object> param) {
        String query = "SELECT a.RETURN_NO, CASE WHEN a.TYPE_RETURN = '0' THEN 'Supplier' ELSE 'Outlet' END AS type_return, \n" +
                "CONCAT(b.DESCRIPTION, CONCAT(c.OUTLET_NAME, d.SUPPLIER_NAME)) AS return_to , a.REMARK, \n" +
                "CASE WHEN a.STATUS = '0' THEN 'Open' WHEN a.STATUS = '1' THEN 'Close' ELSE 'Cancel' END AS status, a.RETURN_DATE,\n" +
                "e.ITEM_CODE, f.ITEM_DESCRIPTION, e.QTY_WAREHOUSE, e.UOM_WAREHOUSE, e.QTY_PURCHASE, e.UOM_PURCHASE, e.TOTAL_QTY \n" +
                "FROM T_RETURN_HEADER a \n" +
                "LEFT JOIN M_GLOBAL b ON a.RETURN_TO = b.CODE AND b.COND =:city\n" +
                "LEFT JOIN M_OUTLET c ON a.RETURN_TO  = c.OUTLET_CODE \n" +
                "LEFT JOIN M_SUPPLIER d ON a.RETURN_TO = d.CD_SUPPLIER \n" +
                "LEFT JOIN T_RETURN_DETAIL e ON a.RETURN_NO = e.RETURN_NO\n" +
                "LEFT JOIN M_ITEM f ON e.ITEM_CODE = f.ITEM_CODE \n" +
                "WHERE a.OUTLET_CODE =:outletCode AND a.TYPE_RETURN IN (:typeReturn1, :typeReturn2) AND a.RETURN_DATE BETWEEN :returnDateFrom AND :returnDateTo ";

        Map prm = new HashMap();
        prm.put("city", "X_" + param.get("city"));
        prm.put("outletCode", param.get("outletCode"));
        prm.put("returnDateFrom", param.get("returnDateFrom"));
        prm.put("returnDateTo", param.get("returnDateTo"));
        if (param.get("typeReturn").equals(0.0)) {
            prm.put("typeReturn1", "1");
            prm.put("typeReturn2", "0");
        } else if (param.get("typeReturn").equals(1.0)) {
            prm.put("typeReturn1", "0");
            prm.put("typeReturn2", "0");
        } else if (param.get("typeReturn").equals(2.0)) {
            prm.put("typeReturn1", "1");
            prm.put("typeReturn2", "1");
        }

        Map<String, List<Map<String, String>>> detailData = new HashMap<>();

        List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("returnNo", rs.getString("RETURN_NO"));
                rt.put("typeReturn", rs.getString("TYPE_RETURN"));
                rt.put("returnTo", rs.getString("RETURN_TO"));
                rt.put("remark", rs.getString("REMARK"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("returnDate", rs.getString("RETURN_DATE"));
                if (rs.getString("ITEM_CODE") != null) {
                    Map<String, String> detail = new HashMap<>();
                    double valueQtyBesar = Double.valueOf(rs.getString("QTY_WAREHOUSE"));
                    double valueQtyKecil = Double.valueOf(rs.getString("QTY_PURCHASE"));
                    double valueTotal = Double.valueOf(rs.getString("TOTAL_QTY"));
                    DecimalFormat df = new DecimalFormat("#,##0.0000");

                    detail.put("itemCode", rs.getString("ITEM_CODE"));
                    detail.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                    detail.put("qtyWarehouse", df.format(valueQtyBesar) + " " + rs.getString("UOM_WAREHOUSE"));
                    detail.put("qtyPurchase", df.format(valueQtyKecil) + " " + rs.getString("UOM_PURCHASE"));
                    detail.put("totalQty", df.format(valueTotal) + " " + rs.getString("UOM_PURCHASE"));
                    detailData.computeIfAbsent(rs.getString("RETURN_NO"), k -> new ArrayList<>()).add(detail);
                }
                return rt;
            }
        });
        Set<Map<String, Object>> set = new HashSet<>(list.size());
        list.removeIf(p -> !set.add(p));

        if (param.get("detail").equals(0.0)) {
            return list;
        } else if (param.get("detail").equals(1.0)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(list);
            List<Map<String, Object>> result = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                Map<String, Object> addList = new HashMap<>();
                addList.put("returnNo", node.get("returnNo"));
                addList.put("typeReturn", node.get("typeReturn"));
                addList.put("returnTo", node.get("returnTo"));
                addList.put("remark", node.get("remark"));
                addList.put("status", node.get("status"));
                addList.put("returnDate", node.get("returnDate"));
                for (Map.Entry<String, List<Map<String, String>>> entry : detailData.entrySet()) {
                    if (entry.getKey().equals(node.get("returnNo").asText())) {
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

    @Override
    public List<Map<String, Object>> reportWastage(Map<String, Object> param) {
        String query = "SELECT a.WASTAGE_NO, CASE WHEN a.TYPE_TRANS = 'W' THEN 'Wastage' ELSE 'Left Over' END AS type_trans,\n" +
                "a.REMARK, CASE WHEN a.STATUS = '0' THEN 'Open' WHEN a.STATUS = '1' THEN 'Close' ELSE 'Cancel' END AS status,\n" +
                "a.WASTAGE_DATE, b.ITEM_CODE, c.ITEM_DESCRIPTION, b.QUANTITY, b.UOM_STOCK \n" +
                "FROM T_WASTAGE_HEADER a \n" +
                "LEFT JOIN T_WASTAGE_DETAIL b ON a.WASTAGE_NO = b.WASTAGE_NO \n" +
                "LEFT JOIN M_ITEM c ON b.ITEM_CODE = c.ITEM_CODE \n" +
                "WHERE a.OUTLET_CODE =:outletCode AND a.TYPE_TRANS IN (:typeTrans1, :typeTrans2) AND a.WASTAGE_DATE BETWEEN :wastageDateFrom AND :wastageDateTo ORDER BY a.WASTAGE_DATE";

        Map prm = new HashMap();
        prm.put("outletCode", param.get("outletCode"));
        prm.put("wastageDateFrom", param.get("wastageDateFrom"));
        prm.put("wastageDateTo", param.get("wastageDateTo"));
        if (param.get("typeTrans").equals("All")) {
            prm.put("typeTrans1", "W");
            prm.put("typeTrans2", "L");
        } else if (param.get("typeTrans").equals("W")) {
            prm.put("typeTrans1", "W");
            prm.put("typeTrans2", "L");
        } else if (param.get("typeTrans").equals("L")) {
            prm.put("typeTrans1", "L");
            prm.put("typeTrans2", "L");
        }

        Map<String, List<Map<String, String>>> detailData = new HashMap<>();

        List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("wastageNo", rs.getString("WASTAGE_NO"));
                rt.put("typeTrans", rs.getString("TYPE_TRANS"));
                rt.put("remark", rs.getString("REMARK"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("wastageDate", rs.getString("WASTAGE_DATE"));
                if (rs.getString("ITEM_CODE") != null) {
                    Map<String, String> detail = new HashMap<>();
                    double valueQuantity = Double.valueOf(rs.getString("QUANTITY"));
                    DecimalFormat df = new DecimalFormat("#,##0.0000");

                    detail.put("itemCode", rs.getString("ITEM_CODE"));
                    detail.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                    detail.put("quantity", df.format(valueQuantity) + " " + rs.getString("UOM_STOCK"));
                    detailData.computeIfAbsent(rs.getString("WASTAGE_NO"), k -> new ArrayList<>()).add(detail);
                }
                return rt;
            }
        });
        Set<Map<String, Object>> set = new HashSet<>(list.size());
        list.removeIf(p -> !set.add(p));

        if (param.get("detail").equals(0.0)) {
            return list;
        } else if (param.get("detail").equals(1.0)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(list);
            List<Map<String, Object>> result = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                Map<String, Object> addList = new HashMap<>();
                addList.put("wastageNo", node.get("wastageNo"));
                addList.put("typeTrans", node.get("typeTrans"));
                addList.put("remark", node.get("remark"));
                addList.put("status", node.get("status"));
                addList.put("wastage", node.get("wastage"));
                for (Map.Entry<String, List<Map<String, String>>> entry : detailData.entrySet()) {
                    if (entry.getKey().equals(node.get("wastageNo").asText())) {
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
    ///////////////NEW METHOD REPORT BY PASCA 29 MEI 2023////
    @Override
    public void insertLogReport(Map<String, String> mapping) {
        String query = "INSERT INTO LOG_REPORT (TYPE_REPORT, OUTLET_CODE, DATE_START, DATE_END, \"TYPE\", DETAIL_FLAG, PARAM, USER_UPD, DATE_UPD, TIME_UPD, DESCRIPTION)\n" +
                "VALUES (:typeReport, :outletCode, :dateStart, :dateEnd, :type, :detail_flag, :param, :userUpd, :dateUpd, :timeUpd, :description)";
        Map param = new HashMap();
        param.put("typeReport", mapping.get("typeReport"));
        param.put("outletCode", mapping.get("outletCode"));
        param.put("dateStart", mapping.get("dateStart"));
        param.put("dateEnd", mapping.get("dateEnd"));
        param.put("type", mapping.get("type"));
        param.put("param", mapping.get("param"));
        param.put("detail_flag", mapping.get("detail_flag"));
        param.put("userUpd", mapping.get("userUpd"));
        param.put("dateUpd", dateNow);
        param.put("timeUpd", timeStamp);
        param.put("description", mapping.get("description"));
        jdbcTemplate.update(query, param);
    }
    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 10 July 2023////

    @Override
    public JasperPrint jesperReportOrderEntry(Map<String, Object> param, Connection connection) throws SQLException, JRException, IOException {
        HashMap hashMap = new HashMap();

        hashMap.put("city", "X_" + param.get("city"));
        if (param.get("typeOrder").equals("Semua")) {
            hashMap.put("orderType1", "0");
            hashMap.put("orderType2", "1");
            hashMap.put("typeOrder", "Semua");
        } else if (param.get("typeOrder").equals("Permintaan")) {
            hashMap.put("orderType1", "0");
            hashMap.put("orderType2", "0");
            hashMap.put("typeOrder", "Permintaan");
        } else if (param.get("typeOrder").equals("Pembelian")) {
            hashMap.put("orderType1", "1");
            hashMap.put("orderType2", "1");
            hashMap.put("typeOrder", "Pembelian");
        }
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        if (param.get("detail").equals(1.0)) {
            hashMap.put("detail", 1);
        } else {
            hashMap.put("detail", 0);
        }
        hashMap.put("user", param.get("user"));

        ClassPathResource classPathResource = new ClassPathResource("report/orderEntry.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportReceiving(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();

        hashMap.put("city", "X_" + param.get("city"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("user", param.get("user"));
        if (param.get("detail").equals(1.0)) {
            hashMap.put("detail", 1);
        } else {
            hashMap.put("detail", 0);
        }

        ClassPathResource classPathResource = new ClassPathResource("report/receiving.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportReturnOrder(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();

        hashMap.put("city", "X_" + param.get("city"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("user", param.get("user"));
        if (param.get("detail").equals(1.0)) {
            hashMap.put("detail", 1);
        } else {
            hashMap.put("detail", 0);
        }

        if (param.get("typeReturn").equals("ALL")) {
            hashMap.put("typeReturn", "ALL");
            hashMap.put("typeReturn1", "0");
            hashMap.put("typeReturn2", "1");
        } else if (param.get("typeReturn").equals("Supplier")) {
            hashMap.put("typeReturn", "Supplier");
            hashMap.put("typeReturn1", "0");
            hashMap.put("typeReturn2", "0");
        } else if (param.get("typeReturn").equals("Gudang")) {
            hashMap.put("typeReturn", "Gudang");
            hashMap.put("typeReturn1", "1");
            hashMap.put("typeReturn2", "1");
        }

        ClassPathResource classPathResource = new ClassPathResource("report/returnOrder.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportWastage(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();

        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("user", param.get("user"));
        if (param.get("detail").equals(1.0)) {
            hashMap.put("detail", 1);
        } else {
            hashMap.put("detail", 0);
        }

        if (param.get("typeTransaksi").equals("ALL")) {
            hashMap.put("typeTransaksi", "ALL");
            hashMap.put("typeTrans1", "W");
            hashMap.put("typeTrans2", "L");
        } else if (param.get("typeTransaksi").equals("Wastage")) {
            hashMap.put("typeTransaksi", "Wastage");
            hashMap.put("typeTrans1", "W");
            hashMap.put("typeTrans2", "W");
        } else if (param.get("typeTransaksi").equals("Left Offer")) {
            hashMap.put("typeTransaksi", "Left Offer");
            hashMap.put("typeTrans1", "L");
            hashMap.put("typeTrans2", "L");
        }

        ClassPathResource classPathResource = new ClassPathResource("report/wastage.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportDeliveryOrder(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();

        hashMap.put("city", "X_" + param.get("city"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("user", param.get("user"));
        if (param.get("detail").equals(1.0)) {
            hashMap.put("detail", 1);
        } else {
            hashMap.put("detail", 0);
        }

        ClassPathResource classPathResource = new ClassPathResource("report/deliveryOrder.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jesperReportItem(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();

        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("user", param.get("user"));
        hashMap.put("jenisGudang", param.get("jenisGudang"));
        if (param.get("status").equals("Semua")) {
            hashMap.put("status", "Semua");
            hashMap.put("status1", "I");
            hashMap.put("status2", "A");
        } else if (param.get("status").equals("Active")) {
            hashMap.put("status", "Active");
            hashMap.put("status1", "A");
            hashMap.put("status2", "A");
        } else if (param.get("status").equals("Non Active")) {
            hashMap.put("status", "Non Active");
            hashMap.put("status1", "I");
            hashMap.put("status2", "I");
        }

        if (param.get("type").equals("Semua")) {
            hashMap.put("typeStock", "Semua");
            hashMap.put("flagStock1", " ");
            hashMap.put("flagStock2", "N");
            hashMap.put("flagStock3", "Y");
        } else if (param.get("type").equals("Stock")) {
            hashMap.put("typeStock", "Stock");
            hashMap.put("flagStock1", "Y");
            hashMap.put("flagStock2", "Y");
            hashMap.put("flagStock3", "Y");
        } else if (param.get("type").equals("Non Stock")) {
            hashMap.put("typeStock", "Non Stock");
            hashMap.put("flagStock1", " ");
            hashMap.put("flagStock2", "N");
            hashMap.put("flagStock3", "N");
        }

        StringBuilder query = new StringBuilder();
        if (!param.get("jenisGudang").equals("Semua"))
            query.append(" AND b.DESCRIPTION = '").append(param.get("jenisGudang")).append("'");
        if (param.containsKey("bahanBaku"))
            query.append(" AND a.FLAG_MATERIAL = 'Y'");
        if (param.containsKey("itemJual"))
            query.append(" AND a.FLAG_FINISHED_GOOD = 'Y'");
        if (param.containsKey("pembelian"))
            query.append(" AND a.FLAG_OTHERS = 'Y'");
        if (param.containsKey("produksi"))
            query.append(" AND a.FLAG_HALF_FINISH = 'Y'");
        if (param.containsKey("openMarket"))
            query.append(" AND a.FLAG_OPEN_MARKET = 'Y'");
        if (param.containsKey("canvasing"))
            query.append(" AND a.FLAG_CANVASING = 'Y'");
        if (param.containsKey("transferDo"))
            query.append(" AND a.FLAG_TRANSFER_LOC = 'Y'");
        if (param.containsKey("paket"))
            query.append(" AND a.FLAG_PAKET = 'Y'");

        if (!param.get("jenisGudang").equals("Semua") || param.containsKey("bahanBaku") || param.containsKey("itemJual")
                || param.containsKey("pembelian") || param.containsKey("produksi") || param.containsKey("openMarket") ||
                param.containsKey("canvasing") || param.containsKey("transferDo") || param.containsKey("paket")) {
            hashMap.put("query", query.toString());
        }

        ClassPathResource classPathResource = new ClassPathResource("report/item.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }
    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 24 July 2023////
    @Override
    public JasperPrint jasperReportStock(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();

        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("fromDate",param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap. put("gudang", param.get("gudang"));
        hashMap.put("item", param.get("item"));
        hashMap.put("user", param.get("user"));

        StringBuilder query = new StringBuilder();

        if (param.get("typePrint").equals(1.0))
            query.append(" AND (a.QTY_IN  != 0 OR a.QTY_OUT != 0 OR a.QTY_BEGINNING != 0)");
        if (!param.get("gudang").equals("Semua"))
            query.append(" AND c.DESCRIPTION = $P{gudang}");
        if (!param.get("item").equals("Semua"))
            query.append(" AND a.ITEM_CODE = $P{item}");
        if (param.get("stockMinus").equals(1.0)){
            query.append(" AND SIGN(a.QTY_BEGINNING + a.QTY_IN - a.QTY_OUT) = -1");
            hashMap.put("title", "(Minus)");
        } else {
            hashMap.put("title", "");
        }

        if (param.get("typePrint").equals(1.0) || param.get("stockMinus").equals(1.0) ||
                !param.get("gudang").equals("Semua") || !param.get("item").equals("Semua")) {
            hashMap.put("query", query.toString());
        }
        ClassPathResource classPathResource = new ClassPathResource("report/stock.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportRecipe(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("user", param.get("user"));

        if (param.get("status").equals("Active")) {
            hashMap.put("status", "A");
            hashMap.put("labelStatus", param.get("status").toString().toUpperCase());
        } else {
            hashMap.put("status", "I");
            hashMap.put("labelStatus", param.get("status").toString().toUpperCase());
        }

        ClassPathResource classPathResource = new ClassPathResource("report/recipe.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportFreeMeal(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("user", param.get("user"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("department", param.get("department"));

        if (param.get("typeReport").equals("Rekap")) {
            hashMap.put("title", "Rekap");
            hashMap.put("detail", 0);
        } else {
            hashMap.put("title", "Detail");
            hashMap.put("detail", 1);
        }

        StringBuilder query = new StringBuilder();

        if (!param.get("department").equals("ALL")) {
            query.append(" AND a.OUTLET_TO = '").append(param.get("department")).append("'");
            hashMap.put("query", query.toString());
        }

        ClassPathResource classPathResource = new ClassPathResource("report/freeMeal.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportSalesByTime(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("address", param.get("outletName"));
        hashMap.put("user", param.get("user"));
        hashMap.put("fromTime", param.get("fromTime"));
        hashMap.put("toTime", param.get("toTime"));
        hashMap.put("fromJam", param.get("fromTime").toString().substring(0, 2));
        hashMap.put("toJam", param.get("toTime").toString().substring(0, 2));

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1){
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos){
                if (object.containsKey("posCode1")) {
                    hashMap.put("posCode1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s/d ");
                } else {
                    hashMap.put("posCode2", object.get("posCode2"));
                    posCode.append(object.get("posName2"));
                }
                hashMap.put("posCode", posCode.toString());
            }
        }

        List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");
        StringBuilder cashierCode = new StringBuilder();
        if (listCashier.size() == 1) {
            hashMap.put("cashierCode", "Semua");
            hashMap.put("cashierCode1", "000");
            hashMap.put("cashierCode2", "zzz");
        } else {
            for (Map<String, Object> object : listCashier) {
                if (object.containsKey("cashierCode1")) {
                    hashMap.put("cashierCode1", object.get("cashierCode1"));
                    cashierCode.append(object.get("cashierName1")).append(" s/d ");
                } else {
                    hashMap.put("cashierCode2", object.get("cashierCode2"));
                    cashierCode.append(object.get("cashierName2"));
                }
                hashMap.put("cashierCode", cashierCode.toString());
            }
        }

        List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");
        StringBuilder shiftCode = new StringBuilder();
        if (listShift.size() == 1) {
            hashMap.put("shiftCode", "Semua");
            hashMap.put("shiftCode1", "000");
            hashMap.put("shiftCode2", "zzz");
        } else {
            for (Map<String, Object> object : listShift) {
                if (object.containsKey("shiftCode1")) {
                    hashMap.put("shiftCode1", object.get("shiftCode1"));
                    shiftCode.append(object.get("shiftName1")).append(" s/d ");
                } else {
                    hashMap.put("shiftCode2", object.get("shiftCode2"));
                    shiftCode.append(object.get("shiftName2"));
                }
                hashMap.put("shiftCode", shiftCode.toString());
            }
        }

        ClassPathResource classPathResource = new ClassPathResource("report/salesTime.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public List<Map<String, Object>> listParamReport(Map<String, String> param) {
        String query = null;
        Map<String, Object> hashMap = new HashMap<>();
        if (param.get("typeParam").equals("Pos")){
            query = "SELECT POS_CODE, POS_DESCRIPTION FROM M_POS mp WHERE STATUS = 'A' AND POS_CODE != ' ' " +
                    "AND OUTLET_CODE =:outletCode ORDER BY POS_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
        } else if (param.get("typeParam").equals("Kasir")) {
            query = "SELECT STAFF_POS_CODE, STAFF_NAME FROM M_POS_STAFF WHERE ACCESS_level = 'KSR' AND STATUS = " +
                    "'A' AND OUTLET_CODE =:outletCode ORDER BY STAFF_POS_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
        } else if (param.get("typeParam").equals("OrderType")) {
            query = "SELECT CODE, DESCRIPTION FROM M_GLOBAL WHERE COND LIKE '%ORDER_TYPE%' AND CODE BETWEEN '000' AND 'zzz'";
        } else if (param.get("typeParam").equals("itemCode")) {
            query = "SELECT ITEM_CODE, ITEM_DESCRIPTION FROM M_ITEM WHERE STATUS = 'A' AND FLAG_PAKET = 'N' " +
                    "AND FLAG_MATERIAL = 'Y' ORDER BY ITEM_CODE asc";
        } else if (param.get("typeParam").equals("paymentType")) {
            query = "SELECT CODE, DESCRIPTION FROM M_GLOBAL WHERE COND LIKE '%PAY_TYPE%'";
        } else if (param.get("typeParam").equals("paymentMethod")) {
            query = "SELECT CODE, DESCRIPTION FROM M_GLOBAL WHERE COND LIKE '%PAY_METHOD%'";
        }

        assert query != null;
        List<Map<String, Object>> list = jdbcTemplate.query(query, hashMap, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                if (param.get("typeParam").equals("Pos")) {
                    rt.put("posCode", rs.getString("POS_CODE"));
                    rt.put("posDescription", rs.getString("POS_DESCRIPTION"));
                } else if (param.get("typeParam").equals("Kasir")) {
                    rt.put("userId", rs.getString("STAFF_POS_CODE"));
                    rt.put("name", rs.getString("STAFF_NAME"));
                } else if (param.get("typeParam").equals("OrderType")) {
                    rt.put("code", rs.getString("CODE"));
                    rt.put("description", rs.getString("DESCRIPTION"));
                } else if (param.get("typeParam").equals("itemCode")) {
                    rt.put("itemCode", rs.getString("ITEM_CODE"));
                    rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                } else if (param.get("typeParam").equals("paymentType")) {
                    rt.put("code", rs.getString("CODE"));
                    rt.put("description", rs.getString("DESCRIPTION"));
                } else if (param.get("typeParam").equals("paymentMethod")) {
                    rt.put("code", rs.getString("CODE"));
                    rt.put("description", rs.getString("DESCRIPTION"));
                }
                return rt;
            }
        });
        return list;
    }

    @Override
    public JasperPrint jasperReportSalesByDate(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("outletName", param.get("outletName"));
        hashMap.put("user", param.get("user"));

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1){
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos){
                if (object.containsKey("posCode1")) {
                    hashMap.put("posCode1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s/d ");
                } else {
                    hashMap.put("posCode2", object.get("posCode2"));
                    posCode.append(object.get("posName2"));
                }
                hashMap.put("posCode", posCode.toString());
            }
        }

        List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");
        StringBuilder cashierCode = new StringBuilder();
        if (listCashier.size() == 1) {
            hashMap.put("cashierCode", "Semua");
            hashMap.put("cashierCode1", "000");
            hashMap.put("cashierCode2", "zzz");
        } else {
            for (Map<String, Object> object : listCashier) {
                if (object.containsKey("cashierCode1")) {
                    hashMap.put("cashierCode1", object.get("cashierCode1"));
                    cashierCode.append(object.get("cashierName1")).append(" s/d ");
                } else {
                    hashMap.put("cashierCode2", object.get("cashierCode2"));
                    cashierCode.append(object.get("cashierName2"));
                }
                hashMap.put("cashierCode", cashierCode.toString());
            }
        }

        List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");
        StringBuilder shiftCode = new StringBuilder();
        if (listShift.size() == 1) {
            hashMap.put("shiftCode", "Semua");
            hashMap.put("shiftCode1", "000");
            hashMap.put("shiftCode2", "zzz");
        } else {
            for (Map<String, Object> object : listShift) {
                if (object.containsKey("shiftCode1")) {
                    hashMap.put("shiftCode1", object.get("shiftCode1"));
                    shiftCode.append(object.get("shiftName1")).append(" s/d ");
                } else {
                    hashMap.put("shiftCode2", object.get("shiftCode2"));
                    shiftCode.append(object.get("shiftName2"));
                }
                hashMap.put("shiftCode", shiftCode.toString());
            }
        }

        ClassPathResource classPathResource = new ClassPathResource("report/salesDate.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportSalesByMenu(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", param.get("user"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("fromTime", param.get("fromTime"));
        hashMap.put("toTime", param.get("toTime"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("outletName", param.get("outletName"));

        if (param.get("brand").toString().equalsIgnoreCase("SEMUA")) {
            hashMap.put("brand", "Semua");
            hashMap.put("brand1", "KFC");
            hashMap.put("brand2", "BB");
        } else {
            hashMap.put("brand", param.get("brand"));
            hashMap.put("brand1", param.get("brand").toString().toUpperCase());
            hashMap.put("brand2", param.get("brand").toString().toUpperCase());
        }

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1){
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos){
                if (object.containsKey("posCode1")) {
                    hashMap.put("posCode1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s/d ");
                } else {
                    hashMap.put("posCode2", object.get("posCode2"));
                    posCode.append(object.get("posName2"));
                }
                hashMap.put("posCode", posCode.toString());
            }
        }

        List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");
        StringBuilder cashierCode = new StringBuilder();
        if (listCashier.size() == 1) {
            hashMap.put("cashierCode", "Semua");
            hashMap.put("cashierCode1", "000");
            hashMap.put("cashierCode2", "zzz");
        } else {
            for (Map<String, Object> object : listCashier) {
                if (object.containsKey("cashierCode1")) {
                    hashMap.put("cashierCode1", object.get("cashierCode1"));
                    cashierCode.append(object.get("cashierName1")).append(" s/d ");
                } else {
                    hashMap.put("cashierCode2", object.get("cashierCode2"));
                    cashierCode.append(object.get("cashierName2"));
                }
                hashMap.put("cashierCode", cashierCode.toString());
            }
        }

        List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");
        StringBuilder shiftCode = new StringBuilder();
        if (listShift.size() == 1) {
            hashMap.put("shiftCode", "Semua");
            hashMap.put("shiftCode1", "000");
            hashMap.put("shiftCode2", "zzz");
        } else {
            for (Map<String, Object> object : listShift) {
                if (object.containsKey("shiftCode1")) {
                    hashMap.put("shiftCode1", object.get("shiftCode1"));
                    shiftCode.append(object.get("shiftName1")).append(" s/d ");
                } else {
                    hashMap.put("shiftCode2", object.get("shiftCode2"));
                    shiftCode.append(object.get("shiftName2"));
                }
                hashMap.put("shiftCode", shiftCode.toString());
            }
        }

        ClassPathResource classPathResource = new ClassPathResource("report/salesMenu.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportMenuVsDetail(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", param.get("user"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("fromTime", param.get("fromTime"));
        hashMap.put("toTime", param.get("toTime"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("outletName", param.get("outletName"));

        if (param.get("brand").toString().equalsIgnoreCase("SEMUA")) {
            hashMap.put("brand", "Semua");
            hashMap.put("brand1", "KFC");
            hashMap.put("brand2", "BB");
        } else {
            hashMap.put("brand", param.get("brand"));
            hashMap.put("brand1", param.get("brand").toString().toUpperCase());
            hashMap.put("brand2", param.get("brand").toString().toUpperCase());
        }

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1){
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos){
                if (object.containsKey("posCode1")) {
                    hashMap.put("posCode1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s/d ");
                } else {
                    hashMap.put("posCode2", object.get("posCode2"));
                    posCode.append(object.get("posName2"));
                }
                hashMap.put("posCode", posCode.toString());
            }
        }

        List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");
        StringBuilder cashierCode = new StringBuilder();
        if (listCashier.size() == 1) {
            hashMap.put("cashierCode", "Semua");
            hashMap.put("cashierCode1", "000");
            hashMap.put("cashierCode2", "zzz");
        } else {
            for (Map<String, Object> object : listCashier) {
                if (object.containsKey("cashierCode1")) {
                    hashMap.put("cashierCode1", object.get("cashierCode1"));
                    cashierCode.append(object.get("cashierName1")).append(" s/d ");
                } else {
                    hashMap.put("cashierCode2", object.get("cashierCode2"));
                    cashierCode.append(object.get("cashierName2"));
                }
                hashMap.put("cashierCode", cashierCode.toString());
            }
        }

        List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");
        StringBuilder shiftCode = new StringBuilder();
        if (listShift.size() == 1) {
            hashMap.put("shiftCode", "Semua");
            hashMap.put("shiftCode1", "000");
            hashMap.put("shiftCode2", "zzz");
        } else {
            for (Map<String, Object> object : listShift) {
                if (object.containsKey("shiftCode1")) {
                    hashMap.put("shiftCode1", object.get("shiftCode1"));
                    shiftCode.append(object.get("shiftName1")).append(" s/d ");
                } else {
                    hashMap.put("shiftCode2", object.get("shiftCode2"));
                    shiftCode.append(object.get("shiftName2"));
                }
                hashMap.put("shiftCode", shiftCode.toString());
            }
        }

        ClassPathResource classPathResource = new ClassPathResource("report/menuVsDetail.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportSummarySalesByItemCode(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", param.get("user"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("fromTime", param.get("fromTime"));
        hashMap.put("toTime", param.get("toTime"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("outletName", param.get("outletName"));

        if (param.get("brand").toString().equalsIgnoreCase("SEMUA")) {
            hashMap.put("brand", "Semua");
            hashMap.put("brand1", "KFC");
            hashMap.put("brand2", "BB");
        } else {
            hashMap.put("brand", param.get("brand"));
            hashMap.put("brand1", param.get("brand").toString().toUpperCase());
            hashMap.put("brand2", param.get("brand").toString().toUpperCase());
        }

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1){
            hashMap.put("pos", "Semua");
            hashMap.put("pos1", "000");
            hashMap.put("pos2", "zzz");
        } else {
            for (Map<String, Object> object : listPos){
                if (object.containsKey("posCode1")) {
                    hashMap.put("pos1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s/d ");
                } else {
                    hashMap.put("pos2", object.get("posCode2"));
                    posCode.append(object.get("posName2"));
                }
                hashMap.put("pos", posCode.toString());
            }
        }

        List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");
        StringBuilder cashierCode = new StringBuilder();
        if (listCashier.size() == 1) {
            hashMap.put("cashier", "Semua");
            hashMap.put("cashier1", "000");
            hashMap.put("cashier2", "zzz");
        } else {
            for (Map<String, Object> object : listCashier) {
                if (object.containsKey("cashierCode1")) {
                    hashMap.put("cashier1", object.get("cashierCode1"));
                    cashierCode.append(object.get("cashierName1")).append(" s/d ");
                } else {
                    hashMap.put("cashier2", object.get("cashierCode2"));
                    cashierCode.append(object.get("cashierName2"));
                }
                hashMap.put("cashier", cashierCode.toString());
            }
        }

        List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");
        StringBuilder shiftCode = new StringBuilder();
        if (listShift.size() == 1) {
            hashMap.put("shift", "Semua");
            hashMap.put("shift1", "000");
            hashMap.put("shift2", "zzz");
        } else {
            for (Map<String, Object> object : listShift) {
                if (object.containsKey("shiftCode1")) {
                    hashMap.put("shift1", object.get("shiftCode1"));
                    shiftCode.append(object.get("shiftName1")).append(" s/d ");
                } else {
                    hashMap.put("shift2", object.get("shiftCode2"));
                    shiftCode.append(object.get("shiftName2"));
                }
                hashMap.put("shift", shiftCode.toString());
            }
        }
        if (param.get("detail").equals(0.0)){
            ClassPathResource classPathResource = new ClassPathResource("report/ReportSummarySalesbyItemCode.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
            return JasperFillManager.fillReport(jasperReport, hashMap, connection);
        } else {
            ClassPathResource classPathResource = new ClassPathResource("report/ReportSalesDetailByItemCode.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
            return JasperFillManager.fillReport(jasperReport, hashMap, connection);
        }
    }

    @Override
    public JasperPrint jasperReportStockCard(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("user", param.get("user"));

        List<String> test = (List<String>) param.get("itemCode");
        StringBuilder item = new StringBuilder();
        item.append("'").append(test.get(0)).append("'");

        for (int i = 1; i < test.size(); i++) {
            item.append(", '").append(test.get(i)).append("'");
        }

        hashMap.put("itemCode", item.toString());


        ClassPathResource classPathResource = new ClassPathResource("report/ReportStockCard.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public Page<Map<String, Object>> getTestPagination(Pageable pageable) {
        String queryCount = "SELECT COUNT(ITEM_CODE) FROM (SELECT DISTINCT (a.ITEM_CODE)  FROM T_STOCK_CARD a " +
                "LEFT JOIN M_ITEM b ON a.ITEM_CODE = b.ITEM_CODE)";
        Map<String, Object> prm = new HashMap<>();
        int count = Integer.parseInt(Objects.requireNonNull(jdbcTemplate.queryForObject(queryCount,prm, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1) == null ? "0" : rs.getString(1);
            }
        })));

        String query = "SELECT DISTINCT a.ITEM_CODE, b.ITEM_DESCRIPTION FROM T_STOCK_CARD a LEFT JOIN M_ITEM b ON " +
                "a.ITEM_CODE = b.ITEM_CODE ORDER BY ITEM_CODE ASC OFFSET " + pageable.getOffset() + " ROWS FETCH NEXT " +
                pageable.getPageSize() + " ROWS ONLY";

        System.out.println(query);

        List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                return rt;
            }
        });
        return new PageImpl<Map<String, Object>>(list, pageable, count);
    }

    @Override
    public JasperPrint jasperReportTransaksiKasir(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("user", param.get("user"));
        hashMap.put("address", param.get("outletName"));

        List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");
        StringBuilder cashierCode = new StringBuilder();
        if (listCashier.size() == 1) {
            hashMap.put("cashierCode", "Semua");
            hashMap.put("cashierCode1", "000");
            hashMap.put("cashierCode2", "zzz");
        } else {
            for (Map<String, Object> object : listCashier) {
                if (object.containsKey("cashierCode1")) {
                    hashMap.put("cashierCode1", object.get("cashierCode1"));
                    cashierCode.append(object.get("cashierName1")).append(" s/d ");
                } else {
                    hashMap.put("cashierCode2", object.get("cashierCode2"));
                    cashierCode.append(object.get("cashierName2"));
                }
                hashMap.put("cashierCode", cashierCode.toString());
            }
        }

        List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");
        StringBuilder shiftCode = new StringBuilder();
        if (listShift.size() == 1) {
            hashMap.put("shiftCode", "Semua");
            hashMap.put("shiftCode1", "000");
            hashMap.put("shiftCode2", "zzz");
        } else {
            for (Map<String, Object> object : listShift) {
                if (object.containsKey("shiftCode1")) {
                    hashMap.put("shiftCode1", object.get("shiftCode1"));
                    shiftCode.append(object.get("shiftName1")).append(" s/d ");
                } else {
                    hashMap.put("shiftCode2", object.get("shiftCode2"));
                    shiftCode.append(object.get("shiftName2"));
                }
                hashMap.put("shiftCode", shiftCode.toString());
            }
        }

        ClassPathResource classPathResource = new ClassPathResource("report/transaksiKasir.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportReceiptMaintenance(Map<String, Object> param, Connection connection) throws JRException, IOException, ParseException {
        Date currentDate = new SimpleDateFormat("dd-MMM-yy").parse((String) param.get("periode"));
        Date yesterdayDate = new Date(currentDate.getTime() - (1000 * 60 * 60 * 24));
        String yesterdayDateString = new SimpleDateFormat("dd-MMM-yy").format(yesterdayDate);

        String queryPos = "SELECT DISTINCT POS_CODE  FROM T_POS_BILL WHERE TRANS_DATE = '" + param.get("periode")
                + "' AND OUTLET_CODE = '" + param.get("outletCode") + "' ORDER BY POS_CODE ASC ";

        List<Map<String, Object>> listPos = jdbcTemplate.query(queryPos, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("posCode", rs.getString("POS_CODE"));
                return rt;
            }
        });

        StringBuilder queryDataReceipt = new StringBuilder();
        queryDataReceipt.append("SELECT UTM.POS_CODE_NOW, JON.POS_DESCRIPTION, UTM.MIN_NOW, UTM.MAX_NOW, UTM.MIN_AGO, " +
                "UTM.MAX_AGO FROM (");
        queryDataReceipt.append("SELECT * FROM ( SELECT '").append(listPos.get(0).get("posCode")).append("'" +
                "AS POS_CODE_NOW , MIN(BILL_NO) AS MIN_NOW, MAX(BILL_NO) AS MAX_NOW FROM T_POS_BILL WHERE TRANS_DATE " +
                "= '").append(param.get("periode")).append("' AND POS_CODE = '").append(listPos.get(0).get("posCode"))
                .append("' AND OUTLET_CODE = '").append(param.get("outletCode")).append("' " +
                        ")A JOIN ( SELECT '").append(listPos.get(0).get("posCode")).append("' AS " +
                        "POS_CODE_AGO, MIN(BILL_NO) AS MIN_AGO, MAX(BILL_NO) AS MAX_AGO FROM T_POS_BILL WHERE " +
                        "TRANS_DATE = '").append(yesterdayDateString).append("' AND POS_CODE = '")
                .append(listPos.get(0).get("posCode")).append("' AND OUTLET_CODE = '").append(param.get("outletCode"))
                .append("')B ON A.POS_CODE_NOW = B.POS_CODE_AGO");

        for (int i = 1; i < listPos.size(); i++) {
            queryDataReceipt.append(" UNION ALL ");
            queryDataReceipt.append("SELECT * FROM ( SELECT '").append(listPos.get(i).get("posCode")).append("' " +
                            "AS POS_CODE_NOW , MIN(BILL_NO) AS MIN_NOW, MAX(BILL_NO) AS MAX_NOW FROM T_POS_BILL WHERE TRANS_DATE " +
                            "= '").append(param.get("periode")).append("' AND POS_CODE = '").append(listPos.get(i).get("posCode"))
                    .append("' AND OUTLET_CODE = '").append(param.get("outletCode")).append("'" +
                            ")A JOIN ( SELECT '").append(listPos.get(i).get("posCode")).append("' AS " +
                            "POS_CODE_AGO, MIN(BILL_NO) AS MIN_AGO, MAX(BILL_NO) AS MAX_AGO FROM T_POS_BILL WHERE " +
                            "TRANS_DATE = '").append(yesterdayDateString).append("' AND POS_CODE = '")
                    .append(listPos.get(i).get("posCode")).append("' AND OUTLET_CODE = '").append(param.get("outletCode"))
                    .append("')B ON A.POS_CODE_NOW = B.POS_CODE_AGO");
        }
        queryDataReceipt.append(") UTM LEFT JOIN M_POS JON ON UTM.POS_CODE_NOW = JON.POS_CODE AND JON.OUTLET_CODE = '")
                .append(param.get("outletCode")).append("' WHERE UTM.POS_CODE_NOW BETWEEN $P{posCode1} AND $P{posCode2}" +
                        "ORDER BY UTM.POS_CODE_NOW ASC");

            Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("query", queryDataReceipt.toString());
        hashMap.put("dateNow", param.get("periode"));
        hashMap.put("dateAgo", yesterdayDateString);
        hashMap.put("user", param.get("user"));
        hashMap.put("outletName", param.get("outletName"));

        List<Map<String, Object>> listParamPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listParamPos.size() == 1){
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listParamPos){
                if (object.containsKey("posCode1")) {
                    hashMap.put("posCode1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s/d ");
                } else {
                    hashMap.put("posCode2", object.get("posCode2"));
                    posCode.append(object.get("posName2"));
                }
                hashMap.put("posCode", posCode.toString());
            }
        }

        ClassPathResource classPathResource = new ClassPathResource("report/receiptMaintenance.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportSalesMixDepartment(Map<String, Object> param, Connection connection) throws JRException, IOException, ParseException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", param.get("user"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("outletName", param.get("outletName"));

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1){
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos){
                if (object.containsKey("posCode1")) {
                    hashMap.put("posCode1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s/d ");
                } else {
                    hashMap.put("posCode2", object.get("posCode2"));
                    posCode.append(object.get("posName2"));
                }
                hashMap.put("posCode", posCode.toString());
            }
        }

        List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");
        StringBuilder cashierCode = new StringBuilder();
        if (listCashier.size() == 1) {
            hashMap.put("cashierCode", "Semua");
            hashMap.put("cashierCode1", "000");
            hashMap.put("cashierCode2", "zzz");
        } else {
            for (Map<String, Object> object : listCashier) {
                if (object.containsKey("cashierCode1")) {
                    hashMap.put("cashierCode1", object.get("cashierCode1"));
                    cashierCode.append(object.get("cashierName1")).append(" s/d ");
                } else {
                    hashMap.put("cashierCode2", object.get("cashierCode2"));
                    cashierCode.append(object.get("cashierName2"));
                }
                hashMap.put("cashierCode", cashierCode.toString());
            }
        }

        List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");
        StringBuilder shiftCode = new StringBuilder();
        if (listShift.size() == 1) {
            hashMap.put("shiftCode", "Semua");
            hashMap.put("shiftCode1", "000");
            hashMap.put("shiftCode2", "zzz");
        } else {
            for (Map<String, Object> object : listShift) {
                if (object.containsKey("shiftCode1")) {
                    hashMap.put("shiftCode1", object.get("shiftCode1"));
                    shiftCode.append(object.get("shiftName1")).append(" s/d ");
                } else {
                    hashMap.put("shiftCode2", object.get("shiftCode2"));
                    shiftCode.append(object.get("shiftName2"));
                }
                hashMap.put("shiftCode", shiftCode.toString());
            }
        }

        ClassPathResource classPathResource = new ClassPathResource("report/salesMixByDepartmentReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportQueryBill(Map<String, Object> param, Connection connection) throws JRException, IOException, ParseException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("billNo", param.get("billNo"));
        hashMap.put("transDate", param.get("billDate"));
        hashMap.put("outletCode", param.get("outletCode"));

        ClassPathResource classPathResource = new ClassPathResource("report/querybill.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public List<Map<String, Object>> listQueryBill(Map<String, Object> param) {
        String query = null;
        Map<String, Object> hashMap = new HashMap<>();

        if (param.containsKey("pos") && param.containsKey("cashier")) {
            query = "SELECT BILL_NO, BILL_DATE, BILL_TIME, POS_CODE, SHIFT_CODE, ORDER_TYPE, TRANS_TYPE, CASHIER_CODE, " +
                    "DELIVERY_STATUS, TOTAL_AMOUNT, TOTAL_SALES FROM T_POS_BILL WHERE OUTLET_CODE =:outletCode AND " +
                    "TRANS_DATE BETWEEN :fromDate AND :toDate AND BILL_TIME BETWEEN :fromTime AND :toTime AND " +
                    "POS_CODE BETWEEN :posCode1 AND :posCode2 AND CASHIER_CODE BETWEEN :cashierCode1 AND :cashierCode2";

            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
            hashMap.put("fromTime", param.get("fromTime"));
            hashMap.put("toTime", param.get("toTime"));

            List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
            if (listPos.size() == 1){
                hashMap.put("posCode1", "000");
                hashMap.put("posCode2", "zzz");
            } else {
                for (Map<String, Object> object : listPos){
                    if (object.containsKey("posCode1")) {
                        hashMap.put("posCode1", object.get("posCode1"));
                    } else {
                        hashMap.put("posCode2", object.get("posCode2"));
                    }
                }
            }

            List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");
            if (listCashier.size() == 1) {
                hashMap.put("cashierCode1", "000");
                hashMap.put("cashierCode2", "zzz");
            } else {
                for (Map<String, Object> object : listCashier) {
                    if (object.containsKey("cashierCode1")) {
                        hashMap.put("cashierCode1", object.get("cashierCode1"));
                    } else {
                        hashMap.put("cashierCode2", object.get("cashierCode2"));
                    }
                }
            }

        } else if (param.containsKey("billNo")){
            if (param.get("detail").equals(0.0)) {
                query = "SELECT a.MENU_ITEM_CODE, b.ITEM_DESCRIPTION, a.ITEM_QTY, a.AMOUNT FROM T_POS_BILL_ITEM a LEFT JOIN" +
                        " M_ITEM b ON a.MENU_ITEM_CODE = b.ITEM_CODE WHERE a.OUTLET_CODE = :outletCode AND a.TRANS_DATE = " +
                        ":date AND a.BILL_NO = :billNo";
            } else {
                query = "SELECT a.MENU_ITEM_CODE, b.ITEM_DESCRIPTION, a.ITEM_QTY FROM T_POS_BILL_ITEM_DETAIL a LEFT " +
                        "JOIN M_ITEM b ON a.MENU_ITEM_CODE = b.ITEM_CODE WHERE a.OUTLET_CODE = :outletCode AND a.TRANS_DATE = " +
                        ":date AND a.BILL_NO = :billNo";
            }
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("date", param.get("date"));
            hashMap.put("billNo", param.get("billNo"));

        }

        assert query != null;
        List<Map<String, Object>> list = jdbcTemplate.query(query, hashMap, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                if (param.containsKey("pos") && param.containsKey("cashier")) {
                    try {
                        Date time = new SimpleDateFormat("HHmmss").parse(rs.getString("BILL_TIME"));
                        rt.put("billNo", rs.getString("BILL_NO"));
                        rt.put("billDate", rs.getString("BILL_DATE"));
                        rt.put("billTime", new SimpleDateFormat("HH:mm:ss").format(time));
                        rt.put("posCode", rs.getString("POS_CODE"));
                        rt.put("shiftCode", rs.getString("SHIFT_CODE"));
                        rt.put("orderType", rs.getString("ORDER_TYPE"));
                        rt.put("transType", rs.getString("TRANS_TYPE"));
                        rt.put("cashierCode", rs.getString("CASHIER_CODE"));
                        rt.put("deliveryStatus", rs.getString("DELIVERY_STATUS"));
                        rt.put("totalAmount", rs.getString("TOTAL_AMOUNT"));
                        rt.put("totalSales", rs.getString("TOTAL_SALES"));
                    } catch (ParseException e) {
                        System.out.println(e);
                        throw new RuntimeException(e);
                    }
                } else if (param.containsKey("billNo")) {
                    if (param.get("detail").equals(0.0)) {
                        rt.put("itemCode", rs.getString("MENU_ITEM_CODE"));
                        rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                        rt.put("itemQty", rs.getString("ITEM_QTY"));
                        rt.put("amount", rs.getString("AMOUNT"));
                    } else {
                        rt.put("itemCode", rs.getString("MENU_ITEM_CODE"));
                        rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                        rt.put("itemQty", rs.getString("ITEM_QTY"));
                    }
                }
                return rt;
            }
        });
        return list;
    }

    @Override
    public JasperPrint jasperReportTransactionByPaymentType(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("transDate1", param.get("fromDate"));
        hashMap.put("transDate2", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("billTime1", param.get("fromTime"));
        hashMap.put("billTime2", param.get("toTime"));

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1){
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos){
                if (object.containsKey("posCode1")) {
                    hashMap.put("posCode1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s/d ");
                } else {
                    hashMap.put("posCode2", object.get("posCode2"));
                    posCode.append(object.get("posName2"));
                }
                hashMap.put("posCode", posCode.toString());
            }
        }

        List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");
        StringBuilder cashierCode = new StringBuilder();
        if (listCashier.size() == 1) {
            hashMap.put("cashierCode", "Semua");
            hashMap.put("cashierCode1", "000");
            hashMap.put("cashierCode2", "zzz");
        } else {
            for (Map<String, Object> object : listCashier) {
                if (object.containsKey("cashierCode1")) {
                    hashMap.put("cashierCode1", object.get("cashierCode1"));
                    cashierCode.append(object.get("cashierName1")).append(" s/d ");
                } else {
                    hashMap.put("cashierCode2", object.get("cashierCode2"));
                    cashierCode.append(object.get("cashierName2"));
                }
                hashMap.put("cashierCode", cashierCode.toString());
            }
        }

        List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");
        StringBuilder shiftCode = new StringBuilder();
        if (listShift.size() == 1) {
            hashMap.put("shiftCode", "Semua");
            hashMap.put("shiftCode1", "000");
            hashMap.put("shiftCode2", "zzz");
        } else {
            for (Map<String, Object> object : listShift) {
                if (object.containsKey("shiftCode1")) {
                    hashMap.put("shiftCode1", object.get("shiftCode1"));
                    shiftCode.append(object.get("shiftName1")).append(" s/d ");
                } else {
                    hashMap.put("shiftCode2", object.get("shiftCode2"));
                    shiftCode.append(object.get("shiftName2"));
                }
                hashMap.put("shiftCode", shiftCode.toString());
            }
        }

        List<Map<String, Object>> listPaymentType = (List<Map<String, Object>>) param.get("PaymentType");
        StringBuilder paymentType = new StringBuilder();
        if (listPos.size() == 1){
            hashMap.put("paymentType", "Semua");
            hashMap.put("paymentType1", "000");
            hashMap.put("paymentType2", "zzz");
        } else {
            for (Map<String, Object> object : listPaymentType){
                if (object.containsKey("paymentType1")) {
                    hashMap.put("paymentType1", object.get("paymentType1"));
                    paymentType.append(object.get("paymentTypeName1")).append(" s/d ");
                } else {
                    hashMap.put("paymentType2", object.get("paymentType2"));
                    shiftCode.append(object.get("paymentTypeName2"));
                }
                hashMap.put("paymentType", shiftCode.toString());
            }
        }

        List<Map<String, Object>> listPaymentMethod = (List<Map<String, Object>>) param.get("paymentMethod");
        StringBuilder paymentMethod = new StringBuilder();
        if (listPos.size() == 1){
            hashMap.put("paymentMethod", "Semua");
            hashMap.put("paymentMethod1", "000");
            hashMap.put("paymentMethod2", "zzz");
        } else {
            for (Map<String, Object> object : listPaymentMethod){
                if (object.containsKey("paymentMethod1")) {
                    hashMap.put("paymentMethod1", object.get("paymentMethod1"));
                    paymentType.append(object.get("paymentMethodName1")).append(" s/d ");
                } else {
                    hashMap.put("paymentMethod2", object.get("paymentMethod2"));
                    shiftCode.append(object.get("paymentMethodName1"));
                }
                hashMap.put("paymentMethod", shiftCode.toString());
            }
        }

        ClassPathResource classPathResource = new ClassPathResource("report/reportTransactionPayment.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportPemakaianBySales(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));

        ClassPathResource classPathResource = new ClassPathResource("report/PemakaianBySalesReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }
}
