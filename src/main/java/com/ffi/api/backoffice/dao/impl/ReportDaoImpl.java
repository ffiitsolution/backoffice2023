/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffi.api.backoffice.dao.ReportDao;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
        System.out.println(query);
        System.exit(0);

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
        } else if (param.get("typeTransaksi").equals("Left Over")) {
            hashMap.put("typeTransaksi", "Left Over");
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

}
