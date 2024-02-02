/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffi.api.backoffice.dao.ReportDao;
import com.ffi.api.backoffice.utils.DynamicRowMapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Repository
public class ReportDaoImpl implements ReportDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    // String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
    // String dateNow = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());

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
        prm.put("outletBrand", param.get("outletBrand"));
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
            prm.put("typeTrans2", "W");
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
        param.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpd", LocalDateTime.now().format(timeFormatter));
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
            hashMap.put("tipeReport", "Detail");
        } else {
            hashMap.put("detail", 0);
            hashMap.put("tipeReport", "Rekap");
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
        if (!param.get("filterBy").equals("All")) {
            hashMap.put("query", " AND b.CD_SUPPLIER = '" + param.get("filterDesc") + "'");
            hashMap.put("filterByValue", param.get("filterByValue"));
            hashMap.put("filterDesc", param.get("filterDesc"));
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
        hashMap.put("outletBrand", param.get("outletBrand"));
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
            hashMap.put("tipeReport", "Detail");
        } else {
            hashMap.put("detail", 0);
            hashMap.put("tipeReport", "Rekap");
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
            hashMap.put("tipeReport", "Detail");
        } else {
            hashMap.put("detail", 0);
            hashMap.put("tipeReport", "Rekap");
        }

        ClassPathResource classPathResource = new ClassPathResource("report/deliveryOrder.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }
    
    public String stitchItemCategoryLabel(String existing, String addedString) {
        if (existing.equals("Semua")) {
            return addedString;
        } 
        return existing + ", " + addedString;
    };

    @Override
    public JasperPrint jesperReportItem(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        String itemCategory = "Semua";

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
        if (param.containsKey("bahanBaku")) {   
            itemCategory = stitchItemCategoryLabel(itemCategory, "Bahan Baku");
            query.append(" AND a.FLAG_MATERIAL = 'Y'");
        }
        if (param.containsKey("itemJual")) {
            itemCategory = stitchItemCategoryLabel(itemCategory, "Item Jual");
            query.append(" AND a.FLAG_FINISHED_GOOD = 'Y'");
        }
        if (param.containsKey("pembelian")) {
            itemCategory = stitchItemCategoryLabel(itemCategory, "Pembelian");
            query.append(" AND a.FLAG_OTHERS = 'Y'");
        }
        if (param.containsKey("produksi")) {
            itemCategory = stitchItemCategoryLabel(itemCategory, "Produksi");
            query.append(" AND a.FLAG_HALF_FINISH = 'Y'");
        }
        if (param.containsKey("openMarket")) {
            itemCategory = stitchItemCategoryLabel(itemCategory, "Open Market");
            query.append(" AND a.FLAG_OPEN_MARKET = 'Y'");
        }
        if (param.containsKey("canvasing")) {
            itemCategory = stitchItemCategoryLabel(itemCategory, "Canvasing");
            query.append(" AND a.FLAG_CANVASING = 'Y'");
        }
        if (param.containsKey("transferDo")) {
            itemCategory = stitchItemCategoryLabel(itemCategory, "Transfer DO");
            query.append(" AND a.FLAG_TRANSFER_LOC = 'Y'");
        }
        if (param.containsKey("paket")) {
            itemCategory = stitchItemCategoryLabel(itemCategory, "Paket");
            query.append(" AND a.FLAG_PAKET = 'Y'");
        }


        if (!param.get("jenisGudang").equals("Semua") || param.containsKey("bahanBaku") || param.containsKey("itemJual")
                || param.containsKey("pembelian") || param.containsKey("produksi") || param.containsKey("openMarket") ||
                param.containsKey("canvasing") || param.containsKey("transferDo") || param.containsKey("paket")) {
            hashMap.put("query", query.toString());
        }
        hashMap.put("itemCategory", itemCategory);
        ClassPathResource classPathResource = new ClassPathResource("report/item.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    /////////////////////////////////DONE///////////////////////////////////////
    ///////////////NEW METHOD REPORT BY PASCA 24 July 2023////
    @Override
    public JasperPrint jasperReportStock(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("gudang", param.get("gudang"));
        hashMap.put("item", param.get("item"));
        hashMap.put("user", param.get("user"));
        hashMap.put("typePrint", (param.get("typePrint").equals(1.0) ? "Item yang ada mutasi stok" : "Semua item"));
        if (param.get("item").equals("Semua")){
            hashMap.put("itemName", "Semua");
        } else {
            hashMap.put("itemName", param.get("item") + " - " + param.get("itemName"));
        }

        StringBuilder query = new StringBuilder();

        if (param.get("typePrint").equals(1.0))
            query.append(" AND (a.QTY_IN  != 0 OR a.QTY_OUT != 0 OR a.QTY_BEGINNING != 0)");
        if (!param.get("gudang").equals("Semua"))
            query.append(" AND c.DESCRIPTION = $P{gudang}");
        if (!param.get("item").equals("Semua"))
            query.append(" AND a.ITEM_CODE = $P{item}");
        if (param.get("stockMinus").equals(1.0)) {
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
        String subReportPath = "report/recipeSub.jrxml";
        JasperReport subReport = null;
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(subReportPath);
            if (inputStream == null) {
                throw new RuntimeException("Subreport file not found: " + subReportPath);
            }
            subReport = JasperCompileManager.compileReport(inputStream);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("outletName", param.get("outletName"));
        hashMap.put("user", param.get("user"));
        hashMap.put("address1", param.get("address1"));
        hashMap.put("address2", param.get("address2"));
        hashMap.put("phone", param.get("phone"));


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
        hashMap.put("outletBrand", param.get("outletBrand"));
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
        
        hashMap.put("outletBrand", param.get("outletBrand"));
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
        if (listPos.size() == 1) {
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
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
        if (param.get("typeReport").equals("Query Bill") && param.get("typeParam").equals("Pos")) {
            query = "SELECT a.POS_CODE, b.POS_DESCRIPTION FROM T_POS_BILL a LEFT JOIN M_POS b ON a.POS_CODE = " +
                    "b.POS_CODE WHERE a.OUTLET_CODE =:outletCode AND a.TRANS_DATE BETWEEN :fromDate AND :toDate AND " +
                    "a.BILL_TIME BETWEEN :fromTime AND :toTime GROUP BY  a.POS_CODE, b.POS_DESCRIPTION ORDER BY a.POS_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
            hashMap.put("fromTime", param.get("fromTime"));
            hashMap.put("toTime", param.get("toTime"));
        } else if (param.get("typeReport").equals("Query Bill") && param.get("typeParam").equals("Cashier")) {
            query = "SELECT a.CASHIER_CODE, b.STAFF_NAME FROM T_POS_BILL a LEFT JOIN M_POS_STAFF b ON a.CASHIER_CODE =" +
                    " b.STAFF_POS_CODE WHERE a.OUTLET_CODE =:outletCode AND b.OUTLET_CODE =:outletCode AND a.TRANS_DATE" +
                    " BETWEEN :fromDate AND :toDate AND a.BILL_TIME BETWEEN :fromTime AND :toTime AND b.ACCESS_level =" +
                    " 'KSR' GROUP BY a.CASHIER_CODE, b.STAFF_NAME ORDER BY a.CASHIER_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
            hashMap.put("fromTime", param.get("fromTime"));
            hashMap.put("toTime", param.get("toTime"));
        } else if (param.get("typeReport").equals("Receiving")) {
            query = "SELECT b.CD_SUPPLIER, c.DESCRIPTION, d.OUTLET_NAME, e.SUPPLIER_NAME FROM T_RECV_HEADER a LEFT JOIN" +
                    " T_ORDER_HEADER b ON a.ORDER_NO = b.ORDER_NO LEFT JOIN M_GLOBAL c ON b.CD_SUPPLIER = c.CODE AND" +
                    " c.COND = :city LEFT JOIN M_OUTLET d ON  b.CD_SUPPLIER = d.OUTLET_CODE LEFT JOIN M_SUPPLIER e ON" +
                    " b.CD_SUPPLIER = e.CD_SUPPLIER WHERE b.CD_SUPPLIER IS NOT NULL AND a.OUTLET_CODE = :outletCode AND " +
                    "a.RECV_DATE BETWEEN :fromDate AND :toDate GROUP BY b.CD_SUPPLIER, c.DESCRIPTION, d.OUTLET_NAME, " +
                    "e.SUPPLIER_NAME ORDER BY b.CD_SUPPLIER ASC";
            hashMap.put("city", "X_" + param.get("city"));
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if (param.get("typeReport").equals("Cashier By Date") && param.get("typeParam").equals("Cashier")) {
            query = "SELECT a.CASHIER_CODE, b.STAFF_NAME FROM T_POS_DAY_TRANS a LEFT JOIN M_POS_STAFF b ON " +
                    "a.CASHIER_CODE = b.STAFF_POS_CODE WHERE a.OUTLET_CODE =:outletCode AND a.TRANS_DATE BETWEEN " +
                    ":fromDate AND :toDate AND b.ACCESS_LEVEL = 'KSR' AND b.STATUS = 'A' GROUP BY a.CASHIER_CODE, " +
                    "b.STAFF_NAME ORDER BY a.CASHIER_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if (param.get("typeReport").equals("Cashier By Date") && param.get("typeParam").equals("Shift")) {
            query = "SELECT a.SHIFT_CODE, CASE WHEN a.SHIFT_CODE = 'S1' THEN 'Shift 1' WHEN SHIFT_CODE = 'S2' THEN " +
                    "'Shift 2' ELSE 'Shift 3' END AS SHIFT_NAME FROM T_POS_DAY_TRANS a WHERE a.OUTLET_CODE " +
                    "=:outletCode AND a.TRANS_DATE BETWEEN :fromDate AND :toDate GROUP BY a.SHIFT_CODE ORDER BY " +
                    "a.SHIFT_CODE  ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if ((param.get("typeReport").equals("Report Menu & Detail Modifier") || param.get("typeReport").equals("Sales by Date") || param.get("typeReport").equals("Sales by Item") || param.get("typeReport").equals("Report Sales Item By Time") || param.get("typeReport").equals("Sales by Time") || param.get("typeReport").equals("Summary Sales by Item Code")) && param.get("typeParam").equals("Pos")) {
            query = "SELECT a.POS_CODE, b.POS_DESCRIPTION FROM t_pos_bill a LEFT JOIN M_POS b ON a.POS_CODE = " +
                    "b.POS_CODE WHERE a.OUTLET_CODE =:outletCode AND a.TRANS_DATE BETWEEN :fromDate AND :toDate AND " +
                    "a.delivery_status = 'CLS' GROUP BY a.POS_CODE, b.POS_DESCRIPTION ORDER BY a.POS_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if ((param.get("typeReport").equals("Report Menu & Detail Modifier") || param.get("typeReport").equals("Sales by Date") || param.get("typeReport").equals("Sales by Item") || param.get("typeReport").equals("Report Sales Item By Time") || param.get("typeReport").equals("Sales by Time") || param.get("typeReport").equals("Summary Sales by Item Code")) && param.get("typeParam").equals("Cashier")) {
            query = "SELECT a.CASHIER_CODE, b.STAFF_NAME FROM t_pos_bill a LEFT JOIN M_POS_STAFF b ON a.CASHIER_CODE =" +
                    " b.STAFF_POS_CODE WHERE a.OUTLET_CODE =:outletCode AND a.TRANS_DATE BETWEEN :fromDate AND :toDate " +
                    "AND a.delivery_status = 'CLS' AND b.ACCESS_level = 'KSR' AND b.STATUS = 'A' GROUP BY " +
                    "a.CASHIER_CODE, b.STAFF_NAME ORDER BY a.CASHIER_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if ((param.get("typeReport").equals("Report Menu & Detail Modifier") || param.get("typeReport").equals("Sales by Date") || param.get("typeReport").equals("Sales by Item") || param.get("typeReport").equals("Report Sales Item By Time") || param.get("typeReport").equals("Sales by Time") || param.get("typeReport").equals("Summary Sales by Item Code")) && param.get("typeParam").equals("Shift")) {
            query = "SELECT a.SHIFT_CODE, CASE WHEN a.SHIFT_CODE = 'S1' THEN 'Shift 1' WHEN SHIFT_CODE = 'S2' THEN " +
                    "'Shift 2' ELSE 'Shift 3' END AS SHIFT_NAME FROM t_pos_bill a WHERE a.OUTLET_CODE =:outletCode" +
                    " AND a.TRANS_DATE BETWEEN :fromDate AND :toDate AND a.delivery_status = 'CLS' GROUP BY " +
                    "a.SHIFT_CODE ORDER BY a.SHIFT_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if (param.get("typeReport").equals("Receipt Maintenance") && param.get("typeParam").equals("Pos")) {
            query = "SELECT a.POS_CODE, b.POS_DESCRIPTION FROM T_POS_BILL a LEFT JOIN M_POS b ON a.POS_CODE =" +
                    " b.POS_CODE WHERE a.OUTLET_CODE =:outletCode AND a.TRANS_DATE = :date AND a.OUTLET_CODE = " +
                    ":outletCode GROUP BY a.POS_CODE, b.POS_DESCRIPTION ORDER BY a.POS_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("date", param.get("date"));
        } else if (param.get("typeReport").equals("Sales Mix by Department") && param.get("typeParam").equals("Pos")) {
            query = "SELECT a.POS_CODE, b.POS_DESCRIPTION FROM TMP_SALES_BY_ITEM a LEFT JOIN M_POS b ON a.POS_CODE =" +
                    " b.POS_CODE WHERE a.OUTLET_CODE =:outletCode AND TRANS_DATE BETWEEN :fromDate AND :toDate GROUP" +
                    " BY a.POS_CODE, b.POS_DESCRIPTION ORDER BY a.POS_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if (param.get("typeReport").equals("Sales Mix by Department") && param.get("typeParam").equals("Cashier")) {
            query = "SELECT a.CASHIER_CODE, b.STAFF_NAME FROM TMP_SALES_BY_ITEM a LEFT JOIN M_POS_STAFF b ON " +
                    "a.CASHIER_CODE = b.STAFF_POS_CODE WHERE a.OUTLET_CODE =:outletCode AND TRANS_DATE BETWEEN " +
                    ":fromDate AND :toDate GROUP BY a.CASHIER_CODE, b.STAFF_NAME ORDER BY a.CASHIER_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if (param.get("typeReport").equals("Sales Mix by Department") && param.get("typeParam").equals("Shift")) {
            query = "SELECT a.SHIFT_CODE , CASE WHEN a.SHIFT_CODE = 'S1' THEN 'Shift 1' WHEN SHIFT_CODE = 'S2' THEN " +
                    "'Shift 2' ELSE 'Shift 3' END AS SHIFT_NAME FROM TMP_SALES_BY_ITEM a WHERE a.OUTLET_CODE " +
                    "=:outletCode AND a.TRANS_DATE BETWEEN :fromDate AND :toDate GROUP BY a.SHIFT_CODE ORDER BY " +
                    "a.SHIFT_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if (param.get("typeReport").equals("Transaction by Payment Type") && param.get("typeParam").equals("Payment Type")) {
            query = "SELECT a.PAYMENT_TYPE_CODE, b.DESCRIPTION FROM (SELECT A.PAYMENT_TYPE_CODE,A.PAYMENT_METHOD_CODE," +
                    "B.POS_CODE,B.SHIFT_CODE,B.CASHIER_CODE FROM M_PAYMENT_METHOD A, T_POS_BILL B, T_POS_BILL_PAYMENT" +
                    " C WHERE B.OUTLET_CODE = :outletCode AND B.TRANS_DATE BETWEEN :fromDate AND :toDate AND" +
                    " B.BILL_TIME BETWEEN :fromTime AND :toTime AND A.OUTLET_CODE = B.OUTLET_CODE AND B.OUTLET_CODE" +
                    " = C.OUTLET_CODE AND B.TRANS_DATE = C.TRANS_DATE AND A.PAYMENT_METHOD_CODE = " +
                    "C.PAYMENT_METHOD_CODE AND B.POS_CODE = C.POS_CODE AND B.BILL_NO = C.BILL_NO) a " +
                    "LEFT JOIN (SELECT   COND, CODE, DESCRIPTION FROM  M_GLOBAL WHERE COND LIKE '%PAY_TYPE%')" +
                    " b ON a.PAYMENT_TYPE_CODE = b.CODE GROUP BY a.PAYMENT_TYPE_CODE, b.DESCRIPTION";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
            hashMap.put("fromTime", param.get("fromTime"));
            hashMap.put("toTime", param.get("toTime"));
        } else if (param.get("typeReport").equals("Transaction by Payment Type") && param.get("typeParam").equals("Payment Method")) {
            query = "SELECT a.PAYMENT_METHOD_CODE, b.DESCRIPTION FROM (SELECT A.PAYMENT_TYPE_CODE,A.PAYMENT_METHOD_CODE," +
                    "B.POS_CODE,B.SHIFT_CODE,B.CASHIER_CODE FROM M_PAYMENT_METHOD A, T_POS_BILL B, T_POS_BILL_PAYMENT" +
                    " C WHERE B.OUTLET_CODE = :outletCode AND B.TRANS_DATE BETWEEN :fromDate AND :toDate AND " +
                    "B.BILL_TIME BETWEEN :fromTime AND :toTime AND A.OUTLET_CODE = B.OUTLET_CODE AND B.OUTLET_CODE =" +
                    " C.OUTLET_CODE AND B.TRANS_DATE = C.TRANS_DATE AND A.PAYMENT_METHOD_CODE = C.PAYMENT_METHOD_CODE" +
                    " AND B.POS_CODE = C.POS_CODE AND B.BILL_NO = C.BILL_NO) a LEFT JOIN (SELECT   COND, CODE," +
                    " DESCRIPTION FROM  M_GLOBAL WHERE COND LIKE '%PAY_METHOD%') b ON a.PAYMENT_METHOD_CODE =" +
                    " b.CODE GROUP BY a.PAYMENT_METHOD_CODE, b.DESCRIPTION";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
            hashMap.put("fromTime", param.get("fromTime"));
            hashMap.put("toTime", param.get("toTime"));
        } else if (param.get("typeReport").equals("Transaction by Payment Type") && param.get("typeParam").equals("Pos")) {
            query = "SELECT a.POS_CODE, b.POS_DESCRIPTION FROM (SELECT A.PAYMENT_TYPE_CODE,A.PAYMENT_METHOD_CODE," +
                    "B.POS_CODE,B.SHIFT_CODE,B.CASHIER_CODE FROM M_PAYMENT_METHOD A, T_POS_BILL B, " +
                    "T_POS_BILL_PAYMENT C WHERE B.OUTLET_CODE = :outletCode AND B.TRANS_DATE BETWEEN :fromDate AND " +
                    ":toDate AND B.BILL_TIME BETWEEN :fromTime AND :toTime AND A.OUTLET_CODE = B.OUTLET_CODE AND " +
                    "B.OUTLET_CODE = C.OUTLET_CODE AND B.TRANS_DATE = C.TRANS_DATE AND A.PAYMENT_METHOD_CODE = " +
                    "C.PAYMENT_METHOD_CODE AND B.POS_CODE = C.POS_CODE AND B.BILL_NO = C.BILL_NO) a LEFT JOIN M_POS " +
                    "b ON a.POS_CODE = b.POS_CODE GROUP BY a.POS_CODE, b.POS_DESCRIPTION";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
            hashMap.put("fromTime", param.get("fromTime"));
            hashMap.put("toTime", param.get("toTime"));
        } else if (param.get("typeReport").equals("Transaction by Payment Type") && param.get("typeParam").equals("Cashier")) {
            query = "SELECT a.CASHIER_CODE, b.STAFF_NAME FROM (SELECT A.PAYMENT_TYPE_CODE,A.PAYMENT_METHOD_CODE," +
                    "B.POS_CODE,B.SHIFT_CODE,B.CASHIER_CODE FROM M_PAYMENT_METHOD A, T_POS_BILL B, T_POS_BILL_PAYMENT" +
                    " C WHERE B.OUTLET_CODE = :outletCode AND B.TRANS_DATE BETWEEN :fromDate AND :toDate AND" +
                    " B.BILL_TIME BETWEEN :fromTime AND :toTime AND A.OUTLET_CODE = B.OUTLET_CODE AND B.OUTLET_CODE =" +
                    " C.OUTLET_CODE AND B.TRANS_DATE = C.TRANS_DATE AND A.PAYMENT_METHOD_CODE = C.PAYMENT_METHOD_CODE " +
                    "AND B.POS_CODE = C.POS_CODE AND B.BILL_NO = C.BILL_NO) a LEFT JOIN M_POS_STAFF b ON " +
                    "a.CASHIER_CODE = b.STAFF_POS_CODE GROUP BY a.CASHIER_CODE, b.STAFF_NAME";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
            hashMap.put("fromTime", param.get("fromTime"));
            hashMap.put("toTime", param.get("toTime"));
        } else if (param.get("typeReport").equals("Transaction by Payment Type") && param.get("typeParam").equals("Shift")) {
            query = "SELECT a.SHIFT_CODE, CASE WHEN a.SHIFT_CODE = 'S1' THEN 'Shift 1' WHEN SHIFT_CODE = 'S2' THEN " +
                    "'Shift 2' ELSE 'Shift 3' END AS SHIFT_NAME FROM (SELECT A.PAYMENT_TYPE_CODE,A.PAYMENT_METHOD_CODE," +
                    "B.POS_CODE,B.SHIFT_CODE,B.CASHIER_CODE FROM M_PAYMENT_METHOD A, T_POS_BILL B, T_POS_BILL_PAYMENT" +
                    " C WHERE B.OUTLET_CODE = :outletCode AND B.TRANS_DATE BETWEEN :fromDate AND :toDate AND" +
                    " B.BILL_TIME BETWEEN :fromTime AND :toTime AND A.OUTLET_CODE = B.OUTLET_CODE AND B.OUTLET_CODE =" +
                    " C.OUTLET_CODE AND B.TRANS_DATE = C.TRANS_DATE AND A.PAYMENT_METHOD_CODE = C.PAYMENT_METHOD_CODE" +
                    " AND B.POS_CODE = C.POS_CODE AND B.BILL_NO = C.BILL_NO) a GROUP BY a.SHIFT_CODE";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
            hashMap.put("fromTime", param.get("fromTime"));
            hashMap.put("toTime", param.get("toTime"));
        } else if (param.get("typeReport").equals("Sales by Date") && param.get("typeParam").equals("Order Type")) {
            query = "SELECT a.ORDER_TYPE, b.DESCRIPTION FROM T_POS_BILL a LEFT JOIN (SELECT CODE, DESCRIPTION FROM " +
                    "M_GLOBAL WHERE COND = 'ORDER_TYPE') b ON a.ORDER_TYPE = b.CODE WHERE (a.DELIVERY_STATUS IN " +
                    "(' ', 'CLS') OR a.DELIVERY_STATUS IS NULL) AND a.OUTLET_CODE IN (:outletCode) AND a.TRANS_DATE " +
                    "BETWEEN :fromDate AND :toDate AND a.order_type IN (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION = " +
                    "'GRPTP' AND COND BETWEEN '000' AND 'zzz') GROUP BY a.ORDER_TYPE,  b.DESCRIPTION";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if ((param.get("typeReport").equals("Report Stock Card") || param.get("typeReport").equals("Report Stock")) && param.get("typeParam").equals("Item Code")) {
            query = "SELECT a.ITEM_CODE, b.ITEM_DESCRIPTION  FROM T_STOCK_CARD a LEFT JOIN M_ITEM b ON a.ITEM_CODE " +
                    "= b.ITEM_CODE WHERE a.OUTLET_CODE = :outletCode AND a.TRANS_DATE BETWEEN :fromDate AND :toDate" +
                    " AND b.FLAG_STOCK = 'Y' AND (a.QTY_BEGINNING != 0 OR a.QTY_IN != 0 OR a.QTY_OUT != 0)" +
                    " GROUP BY a.ITEM_CODE, b.ITEM_DESCRIPTION ORDER BY a.ITEM_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if (param.get("typeReport").equals("Report Stock") && param.get("typeParam").equals("Jenis Gudang")) {
            query = "SELECT b.CD_WAREHOUSE, c.DESCRIPTION FROM T_STOCK_CARD a LEFT JOIN M_ITEM  b ON a.ITEM_CODE =" +
                    " b.ITEM_CODE LEFT JOIN M_GLOBAL c ON b.CD_WAREHOUSE = c.CODE  AND c.COND = 'WAREHOUSE' WHERE " +
                    "a.TRANS_DATE BETWEEN :fromDate AND :toDate AND a.OUTLET_CODE =:outletCode AND c.DESCRIPTION " +
                    "IS NOT NULL GROUP BY b.CD_WAREHOUSE, c.DESCRIPTION";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if (param.get("typeReport").equals("Free Meal Dept") && param.get("typeParam").equals("Department")) {
            query = "SELECT OUTLET_CODE, OUTLET_NAME FROM M_OUTLET WHERE \"TYPE\" = 'HO' ORDER BY OUTLET_CODE ASC";
        } else if (param.get("typeReport").equals("Item Barang") && param.get("typeParam").equals("Jenis Gudang")) {
            query = "SELECT CODE, DESCRIPTION FROM M_GLOBAL WHERE COND = 'WAREHOUSE' AND STATUS = 'A' ORDER BY CODE ASC";
        } else if (param.get("typeReport").equals("Item Selected by Time") && param.get("typeParam").equals("Kode Item")) {
            query = "SELECT a.MENU_ITEM_CODE, b.ITEM_DESCRIPTION FROM T_POS_BILL_ITEM a LEFT JOIN M_ITEM b ON" +
                    " a.MENU_ITEM_CODE = b.ITEM_CODE WHERE a.TRANS_DATE BETWEEN :fromDate AND :toDate AND " +
                    "a.OUTLET_CODE = :outletCode GROUP BY a.MENU_ITEM_CODE, b.ITEM_DESCRIPTION ORDER BY a.MENU_ITEM_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if (param.get("typeReport").equals("Sales Void") && param.get("typeParam").equals("Pos")) {
            query = "SELECT DISTINCT(pb.pos_code) AS POS_CODE, CASE WHEN mp.pos_description IS NULL THEN ' ' ELSE mp.pos_description END AS POS_DESCRIPTION FROM t_pos_bill pb LEFT JOIN M_POS mp ON pb.POS_CODE = mp.POS_CODE AND mp.OUTLET_CODE = pb.OUTLET_CODE LEFT JOIN M_GLOBAL mg ON mp.POS_TYPE = mg.CODE AND mg.cond = 'POS_TYPE' WHERE pb.OUTLET_CODE = :outletCode AND pb.TRANS_DATE BETWEEN :fromDate AND :toDate";
            if(param.get("canceled").equalsIgnoreCase("Order") && param.get("canceledType").equalsIgnoreCase("Cancel")) {
                query += " AND pb.DELIVERY_STATUS = 'CAN'";
            } else if(param.get("canceled").equalsIgnoreCase("Order") && param.get("canceledType").equalsIgnoreCase("Bad Order")) {
                query += " AND pb.DELIVERY_STATUS = 'BAD'";
            } else {
                query += " AND pb.DELIVERY_STATUS <> 'CLS'";
            }
            query += " ORDER BY pb.pos_code";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if (param.get("typeReport").equals("Sales Void") && param.get("typeParam").equals("Cashier")) {
            query = "SELECT distinct(pb.cashier_code), CASE WHEN ms.STAFF_NAME IS NULL THEN ' ' ELSE ms.STAFF_NAME END AS STAFF_NAME FROM t_pos_bill pb LEFT JOIN M_POS_STAFF ms ON pb.CASHIER_CODE = ms.STAFF_POS_CODE WHERE pb.OUTLET_CODE = :outletCode AND pb.TRANS_DATE BETWEEN :fromDate AND :toDate";
            if(param.get("canceled").equalsIgnoreCase("Order") && param.get("canceledType").equalsIgnoreCase("Cancel")) {
                query += " AND pb.DELIVERY_STATUS = 'CAN'";
            } else if(param.get("canceled").equalsIgnoreCase("Order") && param.get("canceledType").equalsIgnoreCase("Bad Order")) {
                query += " AND pb.DELIVERY_STATUS = 'BAD'";
            } else {
                hashMap.put("canceledType", param.get("canceledType"));
                query += " AND pb.DELIVERY_STATUS <> 'CLS'";
            }
            query += " ORDER BY cashier_code";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
            hashMap.put("fromTime", param.get("fromTime"));
            hashMap.put("toTime", param.get("toTime"));
        } else if (param.get("typeReport").equals("Sales Void") && param.get("typeParam").equals("Shift")) {
            query = "SELECT DISTINCT(pb.SHIFT_CODE), CASE WHEN SHIFT_CODE = 'S1' THEN 'Shift 1' WHEN SHIFT_CODE = 'S2' THEN 'Shift 2' ELSE 'Shift 3' END AS SHIFT_NAME FROM t_pos_bill pb WHERE pb.OUTLET_CODE = :outletCode AND pb.TRANS_DATE BETWEEN :fromDate AND :toDate";
            if(param.get("canceled").equalsIgnoreCase("Order") && param.get("canceledType").equalsIgnoreCase("Cancel")) {
                query += " AND pb.DELIVERY_STATUS = 'CAN'";
            } else if(param.get("canceled").equalsIgnoreCase("Order") && param.get("canceledType").equalsIgnoreCase("Bad Order")) {
                query += " AND pb.DELIVERY_STATUS = 'BAD'";
            } else {
                hashMap.put("canceledType", param.get("canceledType"));
                query += " AND pb.DELIVERY_STATUS <> 'CLS'";
            }
            query += " ORDER BY SHIFT_CODE";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
            hashMap.put("fromTime", param.get("fromTime"));
            hashMap.put("toTime", param.get("toTime"));
        } else if (param.get("typeReport").equals("Report Pajak") && param.get("typeParam").equals("Pos")) {
            query = "SELECT * FROM M_GLOBAL WHERE COND = 'POS_TYPE' ORDER BY code ASC";
        } else if (param.get("typeReport").equals("Laporan Item Selected By Product") && param.get("typeParam").equals("Kode Item")) {
            query = "SELECT DISTINCT(D.ITEM_CODE), mi.ITEM_DESCRIPTION FROM T_POS_BILL_ITEM_DETAIL A JOIN M_GROUP_ITEM D ON A.MENU_ITEM_CODE = D.GROUP_ITEM_CODE AND D.STATUS = 'A' JOIN M_ITEM mi ON mi.ITEM_CODE =D.ITEM_CODE WHERE A.TRANS_DATE BETWEEN :fromDate AND :toDate AND A.OUTLET_CODE = :outletCode ORDER BY D.ITEM_CODE ASC";
            hashMap.put("outletCode", param.get("outletCode"));
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        } else if (param.get("typeReport").equals("Item Sales Analysis") && param.get("typeParam").equals("Pos")) {
            query = "SELECT a.POS_CODE, b.POS_DESCRIPTION FROM TMP_SALES_BY_ITEM a LEFT JOIN M_POS b ON a.POS_CODE = b.POS_CODE WHERE a.OUTLET_CODE = :outletCode AND a.TRANS_DATE BETWEEN TO_CHAR(NEXT_DAY(TRUNC(TO_DATE(:date, 'DD-Mon-YY')-7, 'IW'),'SUNDAY'),'DD-Mon-YY') AND TO_CHAR(NEXT_DAY(TRUNC(TO_DATE(:date, 'DD-Mon-YY'), 'IW'),'SATURDAY'),'DD-Mon-YY') GROUP BY a.POS_CODE, b.POS_DESCRIPTION ORDER BY a.POS_CODE ASC";
            hashMap.put("date", param.get("date"));
            hashMap.put("outletCode", param.get("outletCode"));
        } else if (param.get("typeReport").equals("Item Sales Analysis") && param.get("typeParam").equals("Cashier")) {
            query = "SELECT a.CASHIER_CODE, b.STAFF_NAME FROM TMP_SALES_BY_ITEM a LEFT JOIN M_POS_STAFF b ON a.CASHIER_CODE = b.STAFF_POS_CODE WHERE a.OUTLET_CODE = :outletCode AND a.TRANS_DATE BETWEEN TO_CHAR(NEXT_DAY(TRUNC(TO_DATE(:date, 'DD-Mon-YY')-7, 'IW'),'SUNDAY'),'DD-Mon-YY') AND TO_CHAR(NEXT_DAY(TRUNC(TO_DATE(:date, 'DD-Mon-YY'), 'IW'),'SATURDAY'),'DD-Mon-YY') GROUP BY a.CASHIER_CODE, b.STAFF_NAME ORDER BY a.CASHIER_CODE ASC";
            hashMap.put("date", param.get("date"));
            hashMap.put("outletCode", param.get("outletCode"));
        } else if (param.get("typeReport").equals("Item Sales Analysis") && param.get("typeParam").equals("Shift")) {
            query = "SELECT a.SHIFT_CODE, CASE WHEN a.SHIFT_CODE = 'S1' THEN 'Shift 1' WHEN SHIFT_CODE = 'S2' THEN 'Shift 2' ELSE 'Shift 3' END AS SHIFT_NAME FROM TMP_SALES_BY_ITEM a WHERE a.OUTLET_CODE = :outletCode AND a.TRANS_DATE BETWEEN TO_CHAR(NEXT_DAY(TRUNC(TO_DATE(:date, 'DD-Mon-YY')-7, 'IW'),'SUNDAY'),'DD-Mon-YY') AND TO_CHAR(NEXT_DAY(TRUNC(TO_DATE(:date, 'DD-Mon-YY'), 'IW'),'SATURDAY'),'DD-Mon-YY') GROUP BY a.SHIFT_CODE ORDER BY a.SHIFT_CODE ASC";
            hashMap.put("date", param.get("date"));
            hashMap.put("outletCode", param.get("outletCode"));
        } else if (param.get("typeReport").equals("time-management") && param.get("typeParam").equals("Staff")) {
            query = "SELECT DISTINCT TA.STAFF_ID AS STAFF_CODE, ms.STAFF_FULL_NAME AS STAFF_NAME FROM T_ABSENSI ta JOIN M_STAFF ms ON TA.STAFF_ID = ms.STAFF_CODE WHERE ta.DATE_ABSEN BETWEEN :fromDate AND :toDate"; 
            hashMap.put("fromDate", param.get("fromDate"));
            hashMap.put("toDate", param.get("toDate"));
        }

        assert query != null;
        System.err.println("q prm: " + query);
        List<Map<String, Object>> list = jdbcTemplate.query(query, hashMap, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                if (param.get("typeReport").equals("Query Bill") && param.get("typeParam").equals("Pos")) {
                    rt.put("posCode", rs.getString("POS_CODE"));
                    rt.put("posDescription", rs.getString("POS_DESCRIPTION"));
                } else if (param.get("typeReport").equals("Query Bill") && param.get("typeParam").equals("Cashier")) {
                    rt.put("cashierCode", rs.getString("CASHIER_CODE"));
                    rt.put("staffName", rs.getString("STAFF_NAME"));
                } else if (param.get("typeReport").equals("Receiving") && param.get("typeParam").equals("Gudang") && rs.getString("DESCRIPTION") != null) {
                    rt.put("code", rs.getString("CD_SUPPLIER"));
                    rt.put("description", rs.getString("DESCRIPTION"));
                } else if (param.get("typeReport").equals("Receiving") && param.get("typeParam").equals("Outlet") && rs.getString("OUTLET_NAME") != null) {
                    rt.put("code", rs.getString("CD_SUPPLIER"));
                    rt.put("description", rs.getString("OUTLET_NAME"));
                } else if (param.get("typeReport").equals("Receiving") && param.get("typeParam").equals("Supplier") && rs.getString("SUPPLIER_NAME") != null) {
                    rt.put("code", rs.getString("CD_SUPPLIER"));
                    rt.put("description", rs.getString("SUPPLIER_NAME"));
                } else if (param.get("typeReport").equals("Cashier By Date") && param.get("typeParam").equals("Cashier")) {
                    rt.put("cashierCode", rs.getString("CASHIER_CODE"));
                    rt.put("staffName", rs.getString("STAFF_NAME"));
                } else if (param.get("typeReport").equals("Cashier By Date") && param.get("typeParam").equals("Shift")) {
                    rt.put("shiftCode", rs.getString("SHIFT_CODE"));
                    rt.put("shiftName", rs.getString("SHIFT_NAME"));
                } else if ((param.get("typeReport").equals("Report Menu & Detail Modifier") || param.get("typeReport").equals("Sales by Date") || param.get("typeReport").equals("Sales by Item") || param.get("typeReport").equals("Report Sales Item By Time") || param.get("typeReport").equals("Sales by Time") || param.get("typeReport").equals("Summary Sales by Item Code")) && param.get("typeParam").equals("Pos")) {
                    rt.put("posCode", rs.getString("POS_CODE"));
                    rt.put("posDescription", rs.getString("POS_DESCRIPTION"));
                } else if ((param.get("typeReport").equals("Report Menu & Detail Modifier") || param.get("typeReport").equals("Sales by Date") || param.get("typeReport").equals("Sales by Item") || param.get("typeReport").equals("Report Sales Item By Time") || param.get("typeReport").equals("Sales by Time") || param.get("typeReport").equals("Summary Sales by Item Code")) && param.get("typeParam").equals("Cashier")) {
                    rt.put("cashierCode", rs.getString("CASHIER_CODE"));
                    rt.put("staffName", rs.getString("STAFF_NAME"));
                } else if ((param.get("typeReport").equals("Report Menu & Detail Modifier") || param.get("typeReport").equals("Sales by Date") || param.get("typeReport").equals("Sales by Item") || param.get("typeReport").equals("Report Sales Item By Time") || param.get("typeReport").equals("Sales by Time") || param.get("typeReport").equals("Summary Sales by Item Code")) && param.get("typeParam").equals("Shift")) {
                    rt.put("shiftCode", rs.getString("SHIFT_CODE"));
                    rt.put("shiftName", rs.getString("SHIFT_NAME"));
                } else if (param.get("typeReport").equals("Receipt Maintenance") && param.get("typeParam").equals("Pos")) {
                    rt.put("posCode", rs.getString("POS_CODE"));
                    rt.put("posDescription", rs.getString("POS_DESCRIPTION"));
                } else if (param.get("typeReport").equals("Sales Mix by Department") && param.get("typeParam").equals("Pos")) {
                    rt.put("posCode", rs.getString("POS_CODE"));
                    rt.put("posDescription", rs.getString("POS_DESCRIPTION"));
                } else if (param.get("typeReport").equals("Sales Mix by Department") && param.get("typeParam").equals("Cashier")) {
                    rt.put("cashierCode", rs.getString("CASHIER_CODE"));
                    rt.put("staffName", rs.getString("STAFF_NAME"));
                } else if (param.get("typeReport").equals("Sales Mix by Department") && param.get("typeParam").equals("Shift")) {
                    rt.put("shiftCode", rs.getString("SHIFT_CODE"));
                    rt.put("shiftName", rs.getString("SHIFT_NAME"));
                } else if (param.get("typeReport").equals("Transaction by Payment Type") && param.get("typeParam").equals("Payment Type")) {
                    rt.put("paymentTypeCode", rs.getString("PAYMENT_TYPE_CODE"));
                    rt.put("description", rs.getString("DESCRIPTION"));
                } else if (param.get("typeReport").equals("Transaction by Payment Type") && param.get("typeParam").equals("Payment Method")) {
                    rt.put("paymentMethodCode", rs.getString("PAYMENT_METHOD_CODE"));
                    rt.put("description", rs.getString("DESCRIPTION"));
                } else if (param.get("typeReport").equals("Transaction by Payment Type") && param.get("typeParam").equals("Pos")) {
                    rt.put("posCode", rs.getString("POS_CODE"));
                    rt.put("posDescription", rs.getString("POS_DESCRIPTION"));
                } else if (param.get("typeReport").equals("Transaction by Payment Type") && param.get("typeParam").equals("Cashier")) {
                    rt.put("cashierCode", rs.getString("CASHIER_CODE"));
                    rt.put("staffName", rs.getString("STAFF_NAME"));
                } else if (param.get("typeReport").equals("Transaction by Payment Type") && param.get("typeParam").equals("Shift")) {
                    rt.put("shiftCode", rs.getString("SHIFT_CODE"));
                    rt.put("shiftName", rs.getString("SHIFT_NAME"));
                } else if (param.get("typeReport").equals("Sales by Date") && param.get("typeParam").equals("Order Type")) {
                    rt.put("orderType", rs.getString("ORDER_TYPE"));
                    rt.put("description", rs.getString("DESCRIPTION"));
                } else if ((param.get("typeReport").equals("Report Stock Card") || param.get("typeReport").equals("Report Stock")) && param.get("typeParam").equals("Item Code")) {
                    rt.put("itemCode", rs.getString("ITEM_CODE"));
                    rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                } else if (param.get("typeReport").equals("Report Stock") && param.get("typeParam").equals("Jenis Gudang")) {
                    rt.put("cdWarehouse", rs.getString("CD_WAREHOUSE"));
                    rt.put("description", rs.getString("DESCRIPTION"));
                } else if (param.get("typeReport").equals("Free Meal Dept") && param.get("typeParam").equals("Department")) {
                    rt.put("outletCode", rs.getString("OUTLET_CODE"));
                    rt.put("outletName", rs.getString("OUTLET_NAME"));
                } else if (param.get("typeReport").equals("Item Barang") && param.get("typeParam").equals("Jenis Gudang")) {
                    rt.put("code", rs.getString("CODE"));
                    rt.put("description", rs.getString("DESCRIPTION"));
                } else if (param.get("typeReport").equals("Item Selected by Time") && param.get("typeParam").equals("Kode Item")) {
                    rt.put("code", rs.getString("MENU_ITEM_CODE"));
                    rt.put("description", rs.getString("ITEM_DESCRIPTION"));
                } else if (param.get("typeReport").equals("Sales Void") && param.get("typeParam").equals("Pos")) {
                    rt.put("posCode", rs.getString("POS_CODE"));
                    rt.put("posDescription", rs.getString("POS_DESCRIPTION"));
                } else if (param.get("typeReport").equals("Sales Void") && param.get("typeParam").equals("Cashier")) {
                    rt.put("cashierCode", rs.getString("CASHIER_CODE"));
                    rt.put("staffName", rs.getString("STAFF_NAME"));
                } else if (param.get("typeReport").equals("Sales Void") && param.get("typeParam").equals("Shift")) {
                    rt.put("shiftCode", rs.getString("SHIFT_CODE"));
                    rt.put("shiftName", rs.getString("SHIFT_NAME"));
                } else if (param.get("typeReport").equals("Report Pajak") && param.get("typeParam").equals("Pos")) {
                    rt.put("posCode", rs.getString("CODE"));
                    rt.put("posDescription", rs.getString("DESCRIPTION"));
                } else if (param.get("typeReport").equals("Laporan Item Selected By Product") && param.get("typeParam").equals("Kode Item")) {
                    rt.put("code", rs.getString("ITEM_CODE"));
                    rt.put("description", rs.getString("ITEM_DESCRIPTION"));
                } else if (param.get("typeReport").equals("Item Sales Analysis") && param.get("typeParam").equals("Pos")) {
                    rt.put("posCode", rs.getString("POS_CODE"));
                    rt.put("posDescription", rs.getString("POS_DESCRIPTION"));
                } else if (param.get("typeReport").equals("Item Sales Analysis") && param.get("typeParam").equals("Cashier")) {
                    rt.put("cashierCode", rs.getString("CASHIER_CODE"));
                    rt.put("staffName", rs.getString("STAFF_NAME"));
                } else if (param.get("typeReport").equals("Item Sales Analysis") && param.get("typeParam").equals("Shift")) {
                    rt.put("shiftCode", rs.getString("SHIFT_CODE"));
                    rt.put("shiftName", rs.getString("SHIFT_NAME"));
                } else if (param.get("typeReport").equals("time-management") && param.get("typeParam").equals("Staff")) {
                    rt.put("staffCode", rs.getString("STAFF_CODE"));
                    rt.put("staffName", rs.getString("STAFF_NAME"));
                }
                return rt;
            }
        });
        list.removeIf(Map::isEmpty);
        return list;
    }

    @Override
    public JasperPrint jasperReportSalesByDate(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("outletName", param.get("outletName"));
        hashMap.put("user", param.get("user"));

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1) {
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
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

        if (param.get("orderTypeName").equals("Semua")) {
            hashMap.put("orderType", "Semua");
            hashMap.put("orderType1", "000");
            hashMap.put("orderType2", "zzz");
        } else {
            hashMap.put("orderType", param.get("orderTypeCode") + "-" + param.get("orderTypeName"));
            hashMap.put("orderType1", param.get("orderTypeCode"));
            hashMap.put("orderType2", param.get("orderType2"));
        }

        ClassPathResource classPathResource = new ClassPathResource("report/salesDate.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    // Report Sales By Date by Dani 26 Januari 2023
    @Override
    public JasperPrint jasperReportSalesByDateNew(Map<String, Object> param, Connection connection) throws IOException, JRException {
        
        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1) {
            param.put("posCode", "Semua");
            param.put("posCode1", "000");
            param.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
                if (object.containsKey("posCode1")) {
                    param.put("posCode1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s/d ");
                } else {
                    param.put("posCode2", object.get("posCode2"));
                    posCode.append(object.get("posName2"));
                }
                param.put("posCode", posCode.toString());
            }
        }

        List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");
        StringBuilder cashierCode = new StringBuilder();
        if (listCashier.size() == 1) {
            param.put("cashierCode", "Semua");
            param.put("cashierCode1", "000");
            param.put("cashierCode2", "zzz");
        } else {
            for (Map<String, Object> object : listCashier) {
                if (object.containsKey("cashierCode1")) {
                    param.put("cashierCode1", object.get("cashierCode1"));
                    cashierCode.append(object.get("cashierName1")).append(" s/d ");
                } else {
                    param.put("cashierCode2", object.get("cashierCode2"));
                    cashierCode.append(object.get("cashierName2"));
                }
                param.put("cashierCode", cashierCode.toString());
            }
        }

        List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");
        StringBuilder shiftCode = new StringBuilder();
        if (listShift.size() == 1) {
            param.put("shiftCode", "Semua");
            param.put("shiftCode1", "000");
            param.put("shiftCode2", "zzz");
        } else {
            for (Map<String, Object> object : listShift) {
                if (object.containsKey("shiftCode1")) {
                    param.put("shiftCode1", object.get("shiftCode1"));
                    shiftCode.append(object.get("shiftName1")).append(" s/d ");
                } else {
                    param.put("shiftCode2", object.get("shiftCode2"));
                    shiftCode.append(object.get("shiftName2"));
                }
                param.put("shiftCode", shiftCode.toString());
            }
        }

        if (param.get("orderTypeName") == null || param.get("orderTypeName").equals("All")) {
            param.put("orderType", "Semua");
            param.put("orderType1", "000");
            param.put("orderType2", "zzz");
        } else {
            param.put("orderType", param.get("orderTypeCode") + "-" + param.get("orderTypeName"));
            param.put("orderType1", param.get("orderTypeCode"));
            param.put("orderType2", param.get("orderTypeCode"));
        }
        
        String query1 = " SELECT D.*,  GROSS_SALES - TOTAL_CHARGE AS TOTAL_PENDAPATAN  FROM ( "
        +" SELECT  COALESCE (TAXABLE, 0) TAXABLE , COALESCE(TAX, 0) TAX, COALESCE (PEMBULATAN, 0) PEMBULATAN, COALESCE (GROSS_SALES, 0) GROSS_SALES, COALESCE (BIAYA_ANTAR, 0) BIAYA_ANTAR, COALESCE (TAX_CHARGE, 0) TAX_CHARGE, COALESCE (REFUND, 0) REFUND,  "
        +" COALESCE (CUSTOMER, 0) CUSTOMER, COALESCE (TOTAL_DP_PAID, 0) TOTAL_DP_PAID, "
        +" COALESCE (TRANSAKSI, 0) TRANSAKSI, COALESCE (TOTAL_DISCOUNT, 0) TOTAL_DISCOUNT, COALESCE (CUST_AVERAGE, 0) CUST_AVERAGE, COALESCE (TICKET_AVG, 0) TICKET_AVG, COALESCE (DONASI, 0) DONASI,  "
        +" SUM(NVL(TOTAL_CLAIM,0)) AS TOTAL_CLAIM, COALESCE (TOTAL_PAYMENT, 0) TOTAL_PAYMENT, COALESCE (TOTAL_CHARGE, 0) TOTAL_CHARGE , COALESCE (TOTAL_AMOUNT, 0) TOTAL_AMOUNT, "
        +" COALESCE (CUSTOMER_EATIN,0) AS CUSTOMER_EATIN, COALESCE (CUSTOMER_TAKEAWAY, 0) AS CUSTOMER_TAKEAWAY, COALESCE (TRANSAKSI_EATIN, 0) AS TRANSAKSI_EATIN, COALESCE (TRANSAKSI_TAKEAWAY, 0) AS TRANSAKSI_TAKEAWAY FROM( "
        +" SELECT (SUM(TOTAL_AMOUNT) - SUM(TOTAL_DISCOUNT)) TAXABLE, "
        +" 	SUM(TOTAL_AMOUNT) TOTAL_AMOUNT, "
        +"     SUM(TOTAL_TAX) TAX, "
        +"     SUM(TOTAL_PAYMENT) TOTAL_PAYMENT, "
        +"     SUM(TOTAL_ROUNDING) PEMBULATAN, "
        +"     SUM(TOTAL_SALES) GROSS_SALES, "
        +"     SUM(CASE WHEN TRANS_TYPE IN ('TA', ' ') THEN TOTAL_CUSTOMER ELSE 0 END) AS CUSTOMER_TAKEAWAY,"
        +"     SUM(CASE WHEN TRANS_TYPE = 'EI' THEN TOTAL_CUSTOMER ELSE 0 END) AS CUSTOMER_EATIN,"
        +"     SUM(CASE WHEN TRANS_TYPE IN ('TA', ' ') THEN 1 ELSE 0 END) AS TRANSAKSI_TAKEAWAY,"
        +"     SUM(CASE WHEN TRANS_TYPE = 'EI' THEN 1 ELSE 0 END) AS TRANSAKSI_EATIN,"
        +"     SUM(TOTAL_CHARGE) BIAYA_ANTAR, SUM(TOTAL_TAX_CHARGE) AS TAX_CHARGE, "
        +"     SUM(TOTAL_REFUND) REFUND, "
        +"     SUM(TOTAL_CUSTOMER) CUSTOMER, SUM(TOTAL_DP_PAID) AS TOTAL_DP_PAID, "
        +"     COUNT(0) TRANSAKSI, SUM(TOTAL_DISCOUNT) TOTAL_DISCOUNT, "
        +"     SUM(TOTAL_CHARGE) TOTAL_CHARGE, "
        +"     ROUND((SUM(TOTAL_AMOUNT) - SUM(TOTAL_DISCOUNT)) / (NVL(SUM(TOTAL_CUSTOMER), "
        +" 1))) CUST_AVERAGE, "
        +"     ROUND((SUM(TOTAL_AMOUNT) - SUM(TOTAL_DISCOUNT)) / (COUNT(1))) TICKET_AVG,  "
        +" SUM(TOTAL_DONATION) DONASI, 1 AS KODE FROM T_POS_BILL A "
        +" WHERE (DELIVERY_STATUS IN (' ','CLS') OR DELIVERY_STATUS IS NULL) "
        +" AND  OUTLET_CODE IN ( :outletCode)  AND TRANS_DATE BETWEEN :fromDate AND :toDate AND POS_CODE BETWEEN :posCode1 AND :posCode2 AND CASHIER_CODE BETWEEN :cashierCode1 AND :cashierCode2 "
        +" AND SHIFT_CODE BETWEEN :shiftCode1 AND :shiftCode2 "
        +" AND ORDER_TYPE IN (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION = 'GRPTP' AND  "
        +" COND BETWEEN :orderType1 AND  :orderType2)) A "
        +" LEFT JOIN "
        +" ( "
        +" SELECT 1 AS KODE, ORDER_TYPE, 0 AS TOTAL_CLAIM FROM "
        +" ( "
        +" SELECT CASE WHEN A.CASHBANK = 'HDL' THEN 'HMD' "
        +" WHEN A.CASHBANK = 'OPS' THEN 'ETA' "
        +" WHEN A.CASHBANK = 'CSP' THEN 'CSP' "
        +" ELSE A.CASHBANK END AS ORDER_TYPE, SUM(C.TOTAL_IN) AS TOTAL_CLAIM "
        +" FROM T_PC_CLAIM_HDR A "
        +" JOIN T_PC_HDR C ON A.OUTLET_CODE = C.OUTLET_CODE AND A.CLAIM_NO = C.TRANS_TO "
        +" WHERE C.DATE_UPD BETWEEN :fromDate AND :toDate  AND A.TYPE_PAYMENT = 'POT'GROUP BY CASE WHEN A.CASHBANK = 'HDL' THEN 'HMD' "
        +" WHEN A.CASHBANK = 'OPS' THEN 'ETA' "
        +" WHEN A.CASHBANK = 'CSP' THEN 'CSP' "
        +" ELSE A.CASHBANK END,C.TRANS_NO "
        +" ) "
        +" WHERE ORDER_TYPE IN (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION = 'GRPTP' AND  "
        +" COND BETWEEN :orderType1 AND :orderType2)GROUP BY ORDER_TYPE "
        +" ) B ON A.KODE = B.KODE "
        +" GROUP BY TAXABLE, TAX, PEMBULATAN, GROSS_SALES, BIAYA_ANTAR, TAX_CHARGE, REFUND, "
        +"  CUSTOMER, TOTAL_DP_PAID, TRANSAKSI, TOTAL_DISCOUNT, CUST_AVERAGE, TICKET_AVG,  "
        +" DONASI, TOTAL_PAYMENT, TOTAL_CHARGE, TOTAL_AMOUNT, CUSTOMER_TAKEAWAY, CUSTOMER_EATIN, TRANSAKSI_TAKEAWAY, TRANSAKSI_EATIN) D ";
        Map<String, Object> resultQuery1 = jdbcTemplate.queryForObject(query1, param, new DynamicRowMapper());
        
        String query2 = " SELECT COALESCE (SUM(TOT_CD), 0) AS TOT_CD, COALESCE (SUM(PPN_CD), 0) AS PPN_CD FROM "
        + " ( "
        + " SELECT TRANS_DATE, TOT_CD, "
        + " CASE WHEN TRANS_DATE < TANGGAL OR TANGGAL IS NULL THEN 0 ELSE  "
        + " (TOT_CD*((SELECT VALUE FROM m_global WHERE COND='PPNCD' AND STATUS ='A')/100)) END AS PPN_CD FROM "
        + " ( "
        + " SELECT "
        + " (SELECT TO_DATE(TO_CHAR( "
        + "          TO_DATE(DESCRIPTION "
        + "                 ,'DD/Month/YY') "
        + "        ,'DD/MM/YYYY'),'DD/MM/YYYY') "
        + " FROM M_GLOBAL WHERE COND = 'PPNCD') AS TANGGAL, trans_date, (SUM(Z.AMT_EI) +  "
        + " SUM(Z.AMT_TA)) TOT_CD FROM "
        + " ( "
        + " SELECT A.OUTLET_CODE,A.TRANS_DATE,B.ORDER_TYPE,A.POS_CODE,B.CASHIER_CODE,'' AS  "
        + " GROUP_CODE,A.MENU_ITEM_CODE AS ITEM_CODE,SUM(A.ITEM_QTY) AS QTY_EI,SUM(A.AMOUNT) AS AMT_EI, "
        + " 0 AS QTY_TA,0 AS QTY_PERC, 0 AS AMT_TA, 0 AS AMT_PERC, B.SHIFT_CODE, 'ALA' AS  "
        + " FLAG_MENU FROM T_POS_BILL_ITEM A "
        + " JOIN T_POS_BILL B ON A.BILL_NO = B.BILL_NO AND A.REGION_CODE = B.REGION_CODE  "
        + " AND A.OUTLET_CODE = B.OUTLET_CODE AND A.TRANS_DATE = B.TRANS_DATE "
        + " AND A.POS_CODE = B.POS_CODE AND A.TRANS_DATE BETWEEN :fromDate AND  "
        + " :toDate AND A.TRANS_TYPE = 'EI' AND B.DELIVERY_STATUS = 'CLS' "
        + " GROUP BY A.OUTLET_CODE, A.TRANS_DATE, B.ORDER_TYPE,A.POS_CODE, B.CASHIER_CODE, "
        + " A.MENU_ITEM_CODE,B.SHIFT_CODE "
        + " UNION ALL "
        + " SELECT A.OUTLET_CODE,A.TRANS_DATE,B.ORDER_TYPE,A.POS_CODE,B.CASHIER_CODE,'' AS  "
        + " GROUP_CODE,A.MENU_ITEM_CODE AS ITEM_CODE,0 AS QTY_EI,0 AS AMT_EI, "
        + " SUM(A.ITEM_QTY) AS QTY_TA,0 AS QTY_PERC, SUM(A.AMOUNT) AS AMT_TA, 0 AS AMT_PERC, "
        + "  B.SHIFT_CODE, 'ALA' AS FLAG_MENU FROM T_POS_BILL_ITEM A "
        + " JOIN T_POS_BILL B ON A.BILL_NO = B.BILL_NO AND A.REGION_CODE = B.REGION_CODE  "
        + " AND A.OUTLET_CODE = B.OUTLET_CODE AND A.TRANS_DATE = B.TRANS_DATE "
        + " AND A.POS_CODE = B.POS_CODE AND A.TRANS_DATE BETWEEN :fromDate AND  "
        + " :toDate AND A.TRANS_TYPE <> 'EI' AND B.DELIVERY_STATUS = 'CLS' "
        + " GROUP BY A.OUTLET_CODE, A.TRANS_DATE, B.ORDER_TYPE,A.POS_CODE, B.CASHIER_CODE, "
        + " A.MENU_ITEM_CODE,B.SHIFT_CODE "
        + " UNION ALL "
        + " SELECT A.OUTLET_CODE, D.TRANS_DATE, D.ORDER_TYPE, D.POS_CODE, D.CASHIER_CODE,  "
        + " '' AS GROUP_CODE, "
        + " B.MENU_ITEM_CODE AS ITEM_CODE, SUM(B.ITEM_QTY) AS QTY_EI, SUM(B.AMOUNT) AS  "
        + " AMT_EI, 0 AS QTY_TA, 0 AS QTY_PERC,0 AS AMT_TA, 0 AS AMT_PERC, D.SHIFT_CODE, 'ADD' AS FLAG_MENU "
        + "       FROM T_POS_BILL_ITEM A "
        + "       JOIN T_POS_BILL D ON A.BILL_NO = D.BILL_NO AND A.REGION_CODE =  "
        + " D.REGION_CODE AND A.OUTLET_CODE = D.OUTLET_CODE AND A.TRANS_DATE = D.TRANS_DATE  "
        + " AND A.POS_CODE = D.POS_CODE      AND D.DELIVERY_STATUS = 'CLS' "
        + "       JOIN T_POS_BILL_ITEM_DETAIL B ON A.OUTLET_CODE = B.OUTLET_CODE AND  "
        + " A.POS_CODE = B.POS_CODE AND A.TRANS_DATE = B.TRANS_DATE AND A.BILL_NO =  "
        + " B.BILL_NO AND A.ITEM_SEQ = B.ITEM_SEQ "
        + "       LEFT JOIN M_GLOBAL C ON C.CODE = B.MENU_ITEM_CODE AND C.COND = 'ITM_RPT'  "
        + " AND STATUS = 'A' "
        + "       WHERE A.TRANS_DATE BETWEEN :fromDate AND :toDate   AND  "
        + " B.MENU_ITEM_CODE <> A.MENU_ITEM_CODE AND D.TRANS_TYPE = 'EI'      AND ((B.ITEM_TYPE IN ('MDF') AND B.AMOUNT <> 0 ) OR B.ITEM_TYPE = 'ADD') "
        + "       GROUP BY A.OUTLET_CODE, D.TRANS_DATE, D.ORDER_TYPE, D.POS_CODE,  "
        + " D.CASHIER_CODE, B.MENU_ITEM_CODE, D.SHIFT_CODE "
        + " UNION ALL "
        + " SELECT A.OUTLET_CODE, D.TRANS_DATE, D.ORDER_TYPE, D.POS_CODE, D.CASHIER_CODE,  "
        + " '' AS GROUP_CODE, "
        + " B.MENU_ITEM_CODE AS ITEM_CODE, 0 AS QTY_EI, 0 AS AMT_EI, SUM(B.ITEM_QTY) AS  "
        + " QTY_TA, 0 AS QTY_PERC,SUM(B.AMOUNT) AS AMT_TA, 0 AS AMT_PERC, D.SHIFT_CODE, 'ADD' AS FLAG_MENU "
        + "       FROM T_POS_BILL_ITEM A "
        + "       JOIN T_POS_BILL D ON A.BILL_NO = D.BILL_NO AND A.REGION_CODE =  "
        + " D.REGION_CODE AND A.OUTLET_CODE = D.OUTLET_CODE AND A.TRANS_DATE = D.TRANS_DATE  "
        + " AND A.POS_CODE = D.POS_CODE      AND D.DELIVERY_STATUS = 'CLS' "
        + "       JOIN T_POS_BILL_ITEM_DETAIL B ON A.OUTLET_CODE = B.OUTLET_CODE AND  "
        + " A.POS_CODE = B.POS_CODE AND A.TRANS_DATE = B.TRANS_DATE AND A.BILL_NO =  "
        + " B.BILL_NO AND A.ITEM_SEQ = B.ITEM_SEQ "
        + "       LEFT JOIN M_GLOBAL C ON C.CODE = B.MENU_ITEM_CODE AND C.COND = 'ITM_RPT'  "
        + " AND STATUS = 'A'      WHERE A.TRANS_DATE BETWEEN :fromDate AND :toDate "
        + "       AND B.MENU_ITEM_CODE <> A.MENU_ITEM_CODE AND D.TRANS_TYPE <> 'EI' "
        + "       AND ((B.ITEM_TYPE IN ('MDF') AND B.AMOUNT <> 0 ) OR B.ITEM_TYPE = 'ADD') "
        + "       GROUP BY A.OUTLET_CODE, D.TRANS_DATE, D.ORDER_TYPE, D.POS_CODE,  "
        + " D.CASHIER_CODE, B.MENU_ITEM_CODE, D.SHIFT_CODE "
        + " UNION ALL "
        + " SELECT A.OUTLET_CODE, D.TRANS_DATE, D.ORDER_TYPE, D.POS_CODE, D.CASHIER_CODE,  "
        + " '' AS GROUP_CODE, "
        + " A.MENU_ITEM_CODE AS ITEM_CODE, 0 AS QTY_EI, (SUM(B.AMOUNT)*-1) AS AMT_EI, 0 AS  "
        + " QTY_TA, 0 AS QTY_PERC,0 AS AMT_TA, 0 AS AMT_PERC, D.SHIFT_CODE, 'ADD' AS FLAG_MENU "
        + "       FROM T_POS_BILL_ITEM A "
        + "       JOIN T_POS_BILL D ON A.BILL_NO = D.BILL_NO AND A.REGION_CODE =  "
        + " D.REGION_CODE AND A.OUTLET_CODE = D.OUTLET_CODE AND A.TRANS_DATE = D.TRANS_DATE  "
        + " AND A.POS_CODE = D.POS_CODE      AND D.DELIVERY_STATUS = 'CLS' "
        + "       JOIN T_POS_BILL_ITEM_DETAIL B ON A.OUTLET_CODE = B.OUTLET_CODE AND  "
        + " A.POS_CODE = B.POS_CODE AND A.TRANS_DATE = B.TRANS_DATE AND A.BILL_NO =  "
        + " B.BILL_NO AND A.ITEM_SEQ = B.ITEM_SEQ "
        + "       LEFT JOIN M_GLOBAL C ON C.CODE = B.MENU_ITEM_CODE AND C.COND = 'ITM_RPT'  "
        + " AND STATUS = 'A'      WHERE A.TRANS_DATE BETWEEN :fromDate AND :toDate "
        + "       AND B.MENU_ITEM_CODE <> A.MENU_ITEM_CODE AND D.TRANS_TYPE = 'EI' "
        + "       AND ((B.ITEM_TYPE IN ('MDF') AND B.AMOUNT <> 0 ) OR B.ITEM_TYPE = 'ADD') "
        + "       GROUP BY A.OUTLET_CODE, D.TRANS_DATE, D.ORDER_TYPE, D.POS_CODE,  "
        + " D.CASHIER_CODE, A.MENU_ITEM_CODE, D.SHIFT_CODE "
        + " UNION ALL "
        + " SELECT A.OUTLET_CODE, D.TRANS_DATE, D.ORDER_TYPE, D.POS_CODE, D.CASHIER_CODE,  "
        + " '' AS GROUP_CODE, "
        + " A.MENU_ITEM_CODE AS ITEM_CODE, 0 AS QTY_EI, 0 AS AMT_EI, 0 AS QTY_TA, 0 AS  "
        + " QTY_PERC,(SUM(B.AMOUNT)*-1) AS AMT_TA, 0 AS AMT_PERC, D.SHIFT_CODE, 'ADD' AS FLAG_MENU "
        + "       FROM T_POS_BILL_ITEM A "
        + "       JOIN T_POS_BILL D ON A.BILL_NO = D.BILL_NO AND A.REGION_CODE =  "
        + " D.REGION_CODE AND A.OUTLET_CODE = D.OUTLET_CODE AND A.TRANS_DATE = D.TRANS_DATE  "
        + " AND A.POS_CODE = D.POS_CODE      AND D.DELIVERY_STATUS = 'CLS' "
        + "       JOIN T_POS_BILL_ITEM_DETAIL B ON A.OUTLET_CODE = B.OUTLET_CODE AND  "
        + " A.POS_CODE = B.POS_CODE AND A.TRANS_DATE = B.TRANS_DATE AND A.BILL_NO =  "
        + " B.BILL_NO AND A.ITEM_SEQ = B.ITEM_SEQ "
        + "       LEFT JOIN M_GLOBAL C ON C.CODE = B.MENU_ITEM_CODE AND C.COND = 'ITM_RPT'  "
        + " AND STATUS = 'A'      WHERE A.TRANS_DATE BETWEEN :fromDate AND :toDate "
        + "       AND B.MENU_ITEM_CODE <> A.MENU_ITEM_CODE AND D.TRANS_TYPE <> 'EI' "
        + "       AND ((B.ITEM_TYPE IN ('MDF') AND B.AMOUNT <> 0 ) OR B.ITEM_TYPE = 'ADD') "
        + "       GROUP BY A.OUTLET_CODE, D.TRANS_DATE, D.ORDER_TYPE, D.POS_CODE,  "
        + " D.CASHIER_CODE, A.MENU_ITEM_CODE, D.SHIFT_CODE "
        + " ) Z "
        + " JOIN M_MENU_ITEM Q ON  Z.OUTLET_CODE = Q.OUTLET_CODE AND Z.ITEM_CODE =  "
        + " Q.MENU_ITEM_CODE JOIN M_ITEM R ON R.ITEM_CODE = Q.MENU_ITEM_CODE "
        + " JOIN M_SALES_RECIPE M ON Q.MENU_ITEM_CODE = M.PLU_CODE "
        + " WHERE Q.MENU_GROUP_CODE = 'G07' AND Z.POS_CODE BETWEEN :posCode1 AND :posCode2  AND  "
        + " Z.ORDER_TYPE in (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION = 'GRPTP' AND COND  "
        + " BETWEEN :orderType1 AND :orderType2 )GROUP BY TRANS_DATE "
        + " )) ";
        Map<String, Object> resultQuery2 = jdbcTemplate.queryForObject(query2, param, new DynamicRowMapper());

        String query3 = " SELECT COUNT (0) count_kupon_digunakan, COALESCE (SUM(payment_amount), 0) kupon_digunakan, COALESCE (SUM(payment_used), 0) kupon_terpakai FROM t_pos_bill_payment a"
        +" WHERE trim(a.outlet_code)||TRIM(A.POS_CODE)||TRIM(A.BILL_NO) IN ( SELECT "
        +" trim(outlet_code)||TRIM(POS_CODE)||TRIM(BILL_NO) 	FROM T_POS_BILL"
        +" 	WHERE  OUTLET_CODE = :outletCode  AND TRANS_DATE BETWEEN :fromDate AND :toDate	AND POS_CODE BETWEEN :posCode1 AND :posCode2 "
        +" 	AND CASHIER_CODE BETWEEN :cashierCode1 AND :cashierCode2 "
        +" 	AND SHIFT_CODE BETWEEN :shiftCode1 AND :shiftCode2 "
        +" 	and delivery_status='CLS'"
        +" 	and order_type in (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION = 'GRPTP' AND "
        +" COND BETWEEN :orderType1 AND :orderType2))"
        +"     AND payment_method_code = 'VCR'";
        
        Map<String, Object> resultQuery3 = jdbcTemplate.queryForObject(query3, param, new DynamicRowMapper());

        String query4 = "SELECT PAYMENT_METHOD_REPORT, COALESCE (SUM(PAYMENT_AMOUNT), 0) AS sum, COALESCE (sum(kupon_digunakan_q), 0) AS count FROM ("
        +" SELECT B.PAYMENT_METHOD_CODE, COUNT(*) KUPON_DIGUNAKAN_Q, SUM(A.PAYMENT_AMOUNT) PAYMENT_AMOUNT, "
        +" 		CASE WHEN B.PAYMENT_METHOD_CODE NOT IN ('BCA', 'VCR', 'BRI', 'BNI', 'MND') THEN 'OTH' ELSE b.PAYMENT_METHOD_CODE END AS PAYMENT_METHOD_REPORT"
        +" FROM T_POS_BILL_PAYMENT_DETAIL A , M_PAYMENT_METHOD B"
        +" WHERE A.PAYMENT_METHOD_CODE = B.PAYMENT_METHOD_CODE AND A.OUTLET_CODE = "
        +" B.OUTLET_CODE AND trim(a.outlet_code)||TRIM(A.POS_CODE)||TRIM(A.BILL_NO) IN ("
        +"      SELECT trim(outlet_code)||TRIM(POS_CODE)||TRIM(BILL_NO)"
        +"      FROM T_POS_BILL"
        +"      WHERE  OUTLET_CODE = :outletCode"
        +"      AND TRANS_DATE BETWEEN :fromDate AND :toDate"
        +"      AND POS_CODE BETWEEN :posCode1 AND :posCode2 "
        +"      AND CASHIER_CODE BETWEEN :cashierCode1 AND :cashierCode2 "
        +"      AND SHIFT_CODE BETWEEN :shiftCode1 AND :shiftCode2 "
        +" and delivery_status='CLS'"
        +"      and order_type in (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION = 'GRPTP' "
        +" AND COND BETWEEN :orderType1 AND :orderType2))GROUP BY B.PAYMENT_METHOD_CODE ) A GROUP BY A.PAYMENT_METHOD_REPORT";
        List<Map<String, Object>> resultQuery4 = jdbcTemplate.query(query4, param, new DynamicRowMapper());
        for (Map<String,Object> map : resultQuery4) {
            param.put( "countDebit"+ (String) map.get("paymentMethodReport"), map.get("count"));
            param.put( "sumDebit"+ (String) map.get("paymentMethodReport"), map.get("sum"));
        }

        String query5 = "SELECT TRANS_CODE, COUNT(0) COUNT_TRANS_AMOUNT,"
        +"        ABS(COALESCE (SUM(TRANS_AMOUNT),0)) SUM_TRANS_AMOUNT"
        +" FROM T_POS_DAY_TRANS"
        +" WHERE OUTLET_CODE in (:outletCode)  AND TRANS_CODE IN ('OPF','TLO','PRS','CTR','CAN')"
        +" AND TRANS_DATE BETWEEN :fromDate  AND :toDate "
        +" AND POS_CODE BETWEEN :posCode1 AND :posCode2"
        +" AND CASHIER_CODE BETWEEN :cashierCode1 AND :cashierCode2 AND SHIFT_CODE BETWEEN :shiftCode1 AND :shiftCode2"
        +" AND POS_CODE IN"
        +" ("
        +" SELECT POS_CODE FROM T_POS_BILL WHERE OUTLET_CODE in (:outletCode)  AND TRANS_dATE "
        +" BETWEEN :fromDate  AND :toDate AND POS_CODE BETWEEN :posCode1 AND :posCode2 "
        +" AND CASHIER_CODE BETWEEN :cashierCode1 AND :cashierCode2 AND SHIFT_CODE BETWEEN :shiftCode1 AND :shiftCode2 "
        +" AND DELIVERY_STATUS = 'CLS'"
        +" and order_type in (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION = 'GRPTP' AND "
        +" COND BETWEEN :orderType1 AND  :orderType2 )GROUP BY POS_CODE"
        +" )"
        +" GROUP BY TRANS_CODE";

        List<Map<String, Object>> resultQuery5 = jdbcTemplate.query(query5, param, new DynamicRowMapper());
        for (Map<String,Object> map : resultQuery5) {
            param.put( "count"+ (String) map.get("transCode"), map.get("countTransAmount"));
            param.put( "sum"+ (String) map.get("transCode"), map.get("sumTransAmount"));
        }

        String query6 = " SELECT COALESCE (SUM(discount_2), 0)dis1,COALESCE (SUM(discount_5), 0)dis2, COALESCE (SUM(discount_7),0)dis3,COALESCE (SUM(discount_10),0)dis4,COALESCE (SUM(discount_20),0)dis5,COALESCE (SUM(open),0)dis6, "
        +" COALESCE (SUM(count_discount_2),0)count_dis1,COALESCE (SUM(count_discount_5),0)count_dis2,COALESCE (SUM(count_discount_7),0)count_dis3,COALESCE (SUM(count_discount_10),0)count_dis4,COALESCE (SUM(count_discount_20),0)count_dis5,COALESCE (SUM(count_open),0)count_dis6 FROM( "
        +" SELECT "
        +" CASE WHEN discount_method_code='1' THEN SUM(total_discount) ELSE 0 END AS discount_2, "
        +" CASE WHEN discount_method_code='1' THEN COUNT(0) ELSE 0 END AS count_discount_2, "
        +" CASE WHEN discount_method_code='2' THEN SUM(total_discount) ELSE 0 END AS discount_5, "
        +" CASE WHEN discount_method_code='2' THEN COUNT(0) ELSE 0 END AS count_discount_5, "
        +" CASE WHEN discount_method_code='3' THEN SUM(total_discount) ELSE 0 END AS discount_7, "
        +" CASE WHEN discount_method_code='3' THEN COUNT(0) ELSE 0 END AS count_discount_7, "
        +" CASE WHEN discount_method_code='4' THEN SUM(total_discount) ELSE 0 END AS discount_10, "
        +" CASE WHEN discount_method_code='4' THEN COUNT(0) ELSE 0 END AS count_discount_10, "
        +" CASE WHEN discount_method_code='5' THEN SUM(total_discount) ELSE 0 END AS discount_20, "
        +" CASE WHEN discount_method_code='5' THEN COUNT(0) ELSE 0 END AS count_discount_20, "
        +" CASE WHEN discount_method_code='6' THEN SUM(total_discount) ELSE 0 END AS open, "
        +" CASE WHEN discount_method_code='6' THEN COUNT(0) ELSE 0 END AS count_open "
        +" FROM t_pos_bill WHERE trans_date  BETWEEN :fromDate AND :toDate   "
        +" AND POS_CODE BETWEEN :posCode1 AND :posCode2 AND CASHIER_CODE BETWEEN  "
        +" :cashierCode1 AND :cashierCode2 AND SHIFT_CODE BETWEEN :shiftCode1 AND  "
        +" :shiftCode2 AND (DELIVERY_STATUS IN (' ','CLS') OR DELIVERY_STATUS IS NULL) and  "
        +" order_type in (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION = 'GRPTP' AND COND  "
        +" BETWEEN :orderType1 AND :orderType2) "
        +" GROUP BY discount_method_code)";
        Map<String, Object> resultQuery6 = jdbcTemplate.queryForObject(query6, param, new DynamicRowMapper());

        String query7 = " SELECT a.VOID_TYPE, "
        + "        COUNT(0) SUM_COUNT, "
        + "        COALESCE (SUM(a.AMOUNT), 0) SUM_AMOUNT "
        + " FROM T_POS_ITEM_VOID a, T_POS_BILL b "
        + " WHERE A.OUTLET_CODE = :outletCode AND a.OUTLET_CODE=b.OUTLET_CODE "
        + " AND a.TRANS_DATE BETWEEN :fromDate AND :toDate AND  "
        + " a.TRANS_DATE=b.TRANS_DATE AND a.POS_CODE BETWEEN :posCode1 AND :posCode2 AND a.POS_CODE=b.POS_CODE and  "
        + " a.BILL_NO=b.BILL_NO AND a.DAY_SEQ=b.DAY_SEQ "
        + " AND a.CASHIER_CODE BETWEEN :cashierCode1 AND :cashierCode2 AND a.CASHIER_CODE=b.CASHIER_CODE  "
        + " AND b.SHIFT_CODE BETWEEN :shiftCode1 AND :shiftCode2 and B.order_type in (SELECT CODE FROM  "
        + " M_GLOBAL WHERE DESCRIPTION = 'GRPTP' AND COND BETWEEN :orderType1 AND :orderType2) GROUP BY VOID_TYPE ";

        List<Map<String, Object>> resultQuery7 = jdbcTemplate.query(query7, param, new DynamicRowMapper());
        for (Map<String,Object> map : resultQuery7) {
            param.put( "countCorrection"+ (String) map.get("voidType"), map.get("sumCount"));
            param.put( "sumCorrection"+ (String) map.get("voidType"), map.get("sumAmount"));
        }

        String query8 = " SELECT DELIVERY_STATUS, "
        +"       COUNT(0) TOTAL_BY_STATUS, "
        +"       SUM(TOTAL_AMOUNT) AMOUNT_BY_STATUS "
        +"       FROM T_POS_BILL "
        +"       WHERE OUTLET_CODE  = :outletCode "
        +"       AND DELIVERY_STATUS IN ('CAN','BAD') "
        +"       AND TRANS_DATE BETWEEN :fromDate AND :toDate "
        +"       AND POS_CODE BETWEEN :posCode1 AND :posCode2 "
        +"       AND CASHIER_CODE BETWEEN :cashierCode1 AND :cashierCode2 AND SHIFT_CODE BETWEEN :shiftCode1  "
        +" AND :shiftCode2 and order_type in (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION =  "
        +" 'GRPTP' AND COND BETWEEN :orderType1 AND :orderType2) GROUP BY DELIVERY_STATUS ";

        List<Map<String, Object>> resultQuery8 = jdbcTemplate.query(query8, param, new DynamicRowMapper());
        for (Map<String,Object> map : resultQuery8) {
            param.put( "countCorrectionTrx"+ (String) map.get("deliveryStatus"), map.get("totalByStatus"));
            param.put( "sumCorrectionTrx"+ (String) map.get("deliveryStatus"), map.get("amountByStatus"));
        }

        String query9 = "select count(0) count_correction_refund, COALESCE ( SUM(TOTAL_REFUND), 0) sum_correction_refund from t_pos_bill where outlet_code = :outletCode "
         +"and trans_date between :fromDate and :toDate and pos_code between "
        +":posCode1 and :posCode2 and cashier_code between :cashierCode1 and :cashierCode2 and total_refund <> 0 "
        +"and shift_code between :shiftCode1 and :shiftCode2 and refund_manager_code <> ' ' and "
        +"refund_manager_code is not null and order_type in (SELECT CODE FROM M_GLOBAL "
        +"WHERE DESCRIPTION = 'GRPTP' AND COND BETWEEN :orderType1 AND :orderType2)";
        Map<String, Object> resultQuery9 = jdbcTemplate.queryForObject(query9, param, new DynamicRowMapper());

        String query10 = "SELECT TAX AS CONST_TAX FROM m_outlet WHERE OUTLET_CODE =  :outletCode AND ROWNUM = 1 ";
        Map<String, Object> resultQuery10 = jdbcTemplate.queryForObject(query10, param, new DynamicRowMapper());
 
        String query11 = "SELECT PAYMENT_METHOD, SUM (PAYMENT_AMOUNT) AS PAYMENT_AMOUNT, SUM (PAYMENT_USED) AS PAYMENT_USED "
        +" FROM (select b.payment_type_code, sum(a.payment_amount) AS PAYMENT_AMOUNT, sum(a.payment_used) AS PAYMENT_USED , CASE WHEN "
        +" payment_type_code NOT IN ('CSH', 'VCR') THEN 'DBT' ELSE payment_type_code END AS PAYMENT_METHOD "
        +" from t_pos_book_payment a, m_payment_method b"
        +" where  a.outlet_code = :outletCode"
        +" and a.outlet_code = b.outlet_code"
        +" and a.payment_method_code = b.payment_method_code and a.trans_date between "
        +" :fromDate and :toDate and a.book_no in (select book_no from "
        +" t_pos_book where outlet_code = :outletCode and substr(book_no,1,3) between :posCode1 "
        +" and :posCode2 and cashier_code between :cashierCode1 and :cashierCode2 and shift_code between :shiftCode1 "
        +" and :shiftCode2 and order_type in (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION = "
        +" 'GRPTP' AND COND BETWEEN :orderType1 AND :orderType2)) group by b.payment_type_code)a group by a.PAYMENT_METHOD";

        List<Map<String, Object>> resultQuery11 = jdbcTemplate.query(query11, param, new DynamicRowMapper());
        for (Map<String,Object> map : resultQuery11) {
            param.put("dpAmountTerima" + (String) map.get("paymentMethod"), map.get("paymentAmount"));
            param.put("dpUsedTerima" + (String) map.get("paymentMethod"), map.get("paymentUsed"));
        }

        String query12 = " SELECT payment_method, sum(payment_amount) payment_amount, sum(payment_used) AS payment_used FROM ( "
        + " select b.payment_type_code, sum(a.payment_amount) AS payment_amount,  "
        + " sum(a.payment_used) AS payment_used, "
        + " CASE WHEN b.payment_type_code NOT IN ('VCR', 'CSH') THEN 'DBT' ELSE b.payment_type_code END AS payment_method "
        + " from t_pos_book_payment a, m_payment_method b "
        + " where a.outlet_code = :outletCode "
        + " and a.outlet_code = b.outlet_code "
        + " and a.payment_method_code = b.payment_method_code and a.book_no in (select  "
        + " book_no from t_pos_bill where outlet_code = :outletCode  "
        + " and trans_date between :fromDate and :toDate "
        + " and pos_code between :posCode1 and :posCode2 "
        + " and cashier_code between :cashierCode1 and :cashierCode2 and shift_code between :shiftCode1 and :shiftCode2 "
        + " and (delivery_status in (' ','CLS') or delivery_status is null) and book_no <>  "
        + " ' ' and order_type in (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION = 'GRPTP'  "
        + " AND COND BETWEEN :orderType1 AND :orderType2))group by b.payment_type_code)a GROUP BY payment_method ";

        List<Map<String, Object>> resultQuery12 = jdbcTemplate.query(query12, param, new DynamicRowMapper());
        for (Map<String,Object> map : resultQuery12) {
            param.put("dpAmountBayar" + (String) map.get("paymentMethod"), map.get("paymentAmount"));
            param.put("dpUsedBayar" + (String) map.get("paymentMethod"), map.get("paymentUsed"));
        }

        String query13 = "select count(0)count_total_refund, COALESCE( SUM(TOTAL_REFUND), 0) AS total_refund from t_pos_bill where outlet_code = :outletCode "
        +" and trans_date between :fromDate and :toDate and pos_code between "
        +" :posCode1 and :posCode2 and cashier_code between :cashierCode1 and :cashierCode2 and total_refund <> 0 "
        +" and shift_code between :shiftCode1 and :shiftCode2 and refund_manager_code <> ' ' and "
        +" refund_manager_code is not null and order_type in (SELECT CODE FROM M_GLOBAL "
        +" WHERE DESCRIPTION = 'GRPTP' AND COND BETWEEN :orderType1 AND :orderType2)";

        Map<String, Object> resultQuery13 = jdbcTemplate.queryForObject(query13, param, new DynamicRowMapper());

        String query14 = "SELECT COUNT(0) TTL_BILL_JOINT "
        +" FROM t_pos_bill"
        +" WHERE OUTLET_CODE = :outletCode AND BILL_JOINT <> ' ' "
        +" AND TRANS_DATE BETWEEN :fromDate AND :toDate "
        +" AND POS_CODE BETWEEN :posCode1 AND :posCode2"
        +" AND CASHIER_CODE BETWEEN :cashierCode1 AND :cashierCode2 AND SHIFT_CODE BETWEEN :shiftCode1 AND :shiftCode2"
        +" AND (DELIVERY_STATUS IN (' ','CLS') OR DELIVERY_STATUS IS NULL) and order_type "
        +" in (SELECT CODE FROM M_GLOBAL WHERE DESCRIPTION = 'GRPTP' AND COND BETWEEN "
        +" :orderType1 AND :orderType2)";
        Map<String, Object> resultQuery14 = jdbcTemplate.queryForObject(query14, param, new DynamicRowMapper());

        param.putAll(resultQuery1);
        param.putAll(resultQuery2);
        param.putAll(resultQuery3);
        param.putAll(resultQuery6);
        param.putAll(resultQuery9);
        param.putAll(resultQuery10);
        param.putAll(resultQuery13);
        param.putAll(resultQuery14);

        System.err.println("==============================================================================================");
        System.err.println(param);
        ClassPathResource classPathResource = new ClassPathResource("report/salesDate.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, param, connection);
    }

    @Override
    public JasperPrint jasperReportSalesByItem(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("user", param.get("user"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("fromTime", param.get("fromTime"));
        hashMap.put("toTime", param.get("toTime"));
        hashMap.put("outletCode", param.get("outletCode"));

        String queryOutlet = "SELECT mo.OUTLET_NAME, mo.ADDRESS_1, mo.ADDRESS_2, mo.PHONE FROM M_OUTLET mo WHERE mo.OUTLET_CODE = :outletCode";

        List<Map<String, Object>> list = jdbcTemplate.query(queryOutlet, param, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                hashMap.put("outletName", rs.getString("OUTLET_NAME"));
                hashMap.put("address1", rs.getString("ADDRESS_1"));
                hashMap.put("address2", rs.getString("ADDRESS_2"));
                hashMap.put("phone", rs.getString("PHONE"));
                return null;
            }
        });

        if (!param.get("outletBrand").toString().equalsIgnoreCase("TACOBELL")) {
            if (param.get("brand").toString().equalsIgnoreCase("SEMUA")) {
                hashMap.put("brand", "Semua");
                hashMap.put("brand1", "KFC");
                hashMap.put("brand2", "BB");
            } else {
                hashMap.put("brand1", param.get("brand").toString().toUpperCase());
                hashMap.put("brand2", param.get("brand").toString().toUpperCase());
            }
        }

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1) {
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
                if (object.containsKey("posCode1")) {
                    hashMap.put("posCode1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s.d. ");
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
                    cashierCode.append(object.get("cashierName1")).append(" s.d. ");
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
                    shiftCode.append(object.get("shiftName1")).append(" s.d. ");
                } else {
                    hashMap.put("shiftCode2", object.get("shiftCode2"));
                    shiftCode.append(object.get("shiftName2"));
                }
                hashMap.put("shiftCode", shiftCode.toString());
            }
        }

        ClassPathResource classPathResource = new ClassPathResource(param.get("outletBrand").toString().equalsIgnoreCase("TACOBELL") ? "report/salesByItemTaco.jrxml" : "report/salesByItemNew.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportMenuVsDetail(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("user", param.get("user"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("fromTime", param.get("fromTime"));
        hashMap.put("toTime", param.get("toTime"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("outletName", param.get("outletName"));

        if (!param.get("outletBrand").toString().equalsIgnoreCase("TACOBELL")) {
            if (param.get("brand").toString().equalsIgnoreCase("SEMUA")) {
                hashMap.put("brand", "Semua");
                hashMap.put("brand1", "KFC");
                hashMap.put("brand2", "BB");
            } else {
                hashMap.put("brand", param.get("brand"));
                hashMap.put("brand1", param.get("brand").toString().toUpperCase());
                hashMap.put("brand2", param.get("brand").toString().toUpperCase());
            }
        }

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1) {
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
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

        ClassPathResource classPathResource = new ClassPathResource(param.get("outletBrand").toString().equalsIgnoreCase("TACOBELL") ? "report/menuVsDetailTaco.jrxml" : "report/menuVsDetail.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportSummarySalesByItemCode(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("user", param.get("user"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("fromTime", param.get("fromTime"));
        hashMap.put("toTime", param.get("toTime"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("address1", param.get("address1"));
        hashMap.put("address2", param.get("address2"));
        hashMap.put("outletName", param.get("outletName"));
        hashMap.put("phone", param.get("phone"));

        if (!param.get("outletBrand").toString().equalsIgnoreCase("TACOBELL")) {
            if (param.get("brand").toString().equalsIgnoreCase("SEMUA")) {
                hashMap.put("brand", "Semua");
                hashMap.put("brand1", "KFC");
                hashMap.put("brand2", "BB");
            } else {
                hashMap.put("brand", param.get("brand"));
                hashMap.put("brand1", param.get("brand").toString().toUpperCase());
                hashMap.put("brand2", param.get("brand").toString().toUpperCase());
            }
        }

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1) {
            hashMap.put("pos", "Semua");
            hashMap.put("pos1", "000");
            hashMap.put("pos2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
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
        if (param.get("detail").equals(0.0)) {
            ClassPathResource classPathResource = new ClassPathResource(param.get("outletBrand").toString().equalsIgnoreCase("TACOBELL") ? "report/ReportSummarySalesbyItemCodeTaco.jrxml" : "report/ReportSummarySalesbyItemCode.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
            return JasperFillManager.fillReport(jasperReport, hashMap, connection);
        } else {
            ClassPathResource classPathResource = new ClassPathResource(param.get("outletBrand").toString().equalsIgnoreCase("TACOBELL") ? "report/ReportSalesDetailByItemCodeTaco.jrxml" : "report/ReportSalesDetailByItemCode.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
            return JasperFillManager.fillReport(jasperReport, hashMap, connection);
        }
    }

    @Override
    public JasperPrint jasperReportStockCard(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
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
        int count = Integer.parseInt(Objects.requireNonNull(jdbcTemplate.queryForObject(queryCount, prm, new RowMapper<String>() {
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
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("user", param.get("user"));
        hashMap.put("address1", param.get("address1"));
        hashMap.put("address2", param.get("address2"));
        hashMap.put("outletName", param.get("outletName"));
        hashMap.put("phone", param.get("phone"));

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
                "UTM.MAX_AGO, mo.OUTLET_CODE, mo.OUTLET_NAME, mo.ADDRESS_1, mo.ADDRESS_2, mo.PHONE, ms.STAFF_NAME AS PRINT_NAME FROM (");
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
                .append(param.get("outletCode")).append("' LEFT JOIN M_OUTLET mo ON mo.OUTLET_CODE = '").append(param.get("outletCode")).append("' LEFT JOIN M_STAFF ms ON ms.STAFF_CODE = '").append(param.get("user")).append("' WHERE UTM.POS_CODE_NOW BETWEEN $P{posCode1} AND $P{posCode2}" +
                        "ORDER BY UTM.POS_CODE_NOW ASC");

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("query", queryDataReceipt.toString());
        hashMap.put("dateNow", param.get("periode"));
        hashMap.put("dateAgo", yesterdayDateString);
        hashMap.put("user", param.get("user"));
        hashMap.put("outletName", param.get("outletName"));

        List<Map<String, Object>> listParamPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listParamPos.size() == 1) {
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listParamPos) {
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
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("user", param.get("user"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("address", param.get("outletName"));

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1) {
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
                if (object.containsKey("posCode1")) {
                    hashMap.put("posCode1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s.d. ");
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
                    cashierCode.append(object.get("cashierName1")).append(" s.d. ");
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
                    shiftCode.append(object.get("shiftName1")).append(" s.d. ");
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
        hashMap.put("outletBrand", param.get("outletBrand"));
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
            if (listPos.size() == 1) {
                hashMap.put("posCode1", "000");
                hashMap.put("posCode2", "zzz");
            } else {
                for (Map<String, Object> object : listPos) {
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

        } else if (param.containsKey("billNo")) {
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
    public List<Map<String, Object>> listQuerySales(Map<String, Object> param) {
        String query = null;
        String wherePos = "";
        if (param.containsKey("firstPos") && param.containsKey("lastPos")) {
            wherePos = " AND to_number(POS_CODE) >= :firstPos AND to_number(POS_CODE) <= :lastPos ";
        }

        String whereCashier = "";
        if (param.containsKey("firstCashier") && param.containsKey("lastCashier")) {
            whereCashier = " AND to_number(CASHIER_CODE) >= :firstCashier AND to_number(CASHIER_CODE) <= :lastCashier ";
        }

        String whereShift = "";
        if (param.containsKey("firstShift") && param.containsKey("lastShift")) {
            whereCashier = " AND SHIFT_CODE >= :firstShift AND SHIFT_CODE <= :lastShift ";
        }
        query = "SELECT TRANS_DATE AS TANGGAL, "
        + "    SUM(SLS) AS PENJUALAN, "
        + "    SUM(RFD) AS REFUND, "
        + "    SUM(PRS) AS SETORAN_AWAL, "
        + "    SUM(TLO) AS SETORAN_TERAKHIR, "
        + "    SUM(DNT) AS DONASI, "
        + "    SUM(OPF) AS MODAL, "
        + "    SUM(CAT) AS CATERING, "
        + "    SUM(DP) AS DOWNPAYMENT, "
        + "    SUM(SLS) + SUM(RFD) + SUM(CAT) + SUM(DP) AS TOTAL, "
        + "    SUM(SLS) + SUM(OPF) AS CASH, "
        + "    SUM(PRS) + SUM(TLO) AS SETOR, "
        + "    (SUM(SLS) + SUM(OPF)) + (SUM(PRS) + SUM(TLO)) AS SISA "
        + "FROM ( "
        + "        SELECT TRANS_DATE, "
        + "            CASE "
        + "                WHEN TRANS_CODE = 'SLS' THEN AMT "
        + "                ELSE 0 "
        + "            END AS SLS, "
        + "            CASE "
        + "                WHEN TRANS_CODE = 'RFD' THEN AMT "
        + "                ELSE 0 "
        + "            END AS RFD, "
        + "            CASE "
        + "                WHEN TRANS_CODE = 'PRS' THEN AMT "
        + "                ELSE 0 "
        + "            END AS PRS, "
        + "            CASE "
        + "                WHEN TRANS_CODE = 'TLO' THEN AMT "
        + "                ELSE 0 "
        + "            END AS TLO, "
        + "            CASE "
        + "                WHEN TRANS_CODE = 'DNT' THEN AMT "
        + "                ELSE 0 "
        + "            END AS DNT, "
        + "            CASE "
        + "                WHEN TRANS_CODE = 'OPF' THEN AMT "
        + "                ELSE 0 "
        + "            END AS OPF, "
        + "            CASE "
        + "                WHEN TRANS_CODE = 'CAT' THEN AMT "
        + "                ELSE 0 "
        + "            END AS CAT, "
        + "            CASE "
        + "                WHEN TRANS_CODE = 'DP' THEN AMT "
        + "                ELSE 0 "
        + "            END AS DP "
        + "        FROM( "
        + "                SELECT TRANS_DATE, "
        + "                    TRANS_CODE, "
        + "                    SUM(TRANS_AMOUNT) AMT "
        + "                FROM T_POS_DAY_TRANS "
        + "                WHERE OUTLET_CODE = :outletCode "
        + "                    AND TRANS_DATE >= :startTransDate "
        + "                    AND TRANS_DATE <= :endTransDate "
        + wherePos
        + whereCashier
        // + whereShift
        + "                GROUP BY TRANS_DATE, "
        + "                    TRANS_CODE "
        + "            ) "
        + "    ) "
        + "GROUP BY TRANS_DATE "
        + "ORDER BY TRANS_DATE ASC";

        return jdbcTemplate.query(query, param, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                        rt.put("tanggal", rs.getString("TANGGAL"));
                        rt.put("penjualan", rs.getString("PENJUALAN"));
                        rt.put("refund", rs.getString("REFUND"));
                        rt.put("setoran_awal", rs.getString("SETORAN_AWAL"));
                        rt.put("setoran_terakhir", rs.getString("SETORAN_TERAKHIR"));
                        rt.put("donasi", rs.getString("DONASI"));
                        rt.put("modal", rs.getString("MODAL"));
                        rt.put("catering", rs.getString("CATERING"));
                        rt.put("downpayment", rs.getString("DOWNPAYMENT"));
                        rt.put("total", rs.getString("TOTAL"));
                        rt.put("cash", rs.getString("CASH"));
                        rt.put("setor", rs.getString("SETOR"));                        
                        rt.put("sisa", rs.getString("SISA"));

                return rt;
            }
        });
    }

    @Override
    public JasperPrint jasperReportTransactionByPaymentType(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("transDate1", param.get("fromDate"));
        hashMap.put("transDate2", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("billTime1", param.get("fromTime"));
        hashMap.put("billTime2", param.get("toTime"));

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1) {
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
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
        if (listPos.size() == 1) {
            hashMap.put("paymentType", "Semua");
            hashMap.put("paymentType1", "000");
            hashMap.put("paymentType2", "zzz");
        } else {
            for (Map<String, Object> object : listPaymentType) {
                if (object.containsKey("paymentType1")) {
                    hashMap.put("paymentType1", object.get("paymentType1"));
                    paymentType.append(object.get("paymentTypeName1")).append(" s/d ");
                } else {
                    hashMap.put("paymentType2", object.get("paymentType2"));
                    paymentType.append(object.get("paymentTypeName2"));
                }
                hashMap.put("paymentType", paymentType.toString());
            }
        }

        List<Map<String, Object>> listPaymentMethod = (List<Map<String, Object>>) param.get("paymentMethod");
        StringBuilder paymentMethod = new StringBuilder();
        if (listPos.size() == 1) {
            hashMap.put("paymentMethod", "Semua");
            hashMap.put("paymentMethod1", "000");
            hashMap.put("paymentMethod2", "zzz");
        } else {
            for (Map<String, Object> object : listPaymentMethod) {
                if (object.containsKey("paymentMethod1")) {
                    hashMap.put("paymentMethod1", object.get("paymentMethod1"));
                    paymentMethod.append(object.get("paymentMethodName1")).append(" s/d ");
                } else {
                    hashMap.put("paymentMethod2", object.get("paymentMethod2"));
                    paymentMethod.append(object.get("paymentMethodName1"));
                }
                hashMap.put("paymentMethod", paymentMethod.toString());
            }
        }

        ClassPathResource classPathResource = new ClassPathResource("report/reportTransactionPayment.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportPemakaianBySales(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("user", param.get("user"));

        ClassPathResource classPathResource = new ClassPathResource("report/PemakaianBySalesReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportProduksiAktual(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("city", param.get("city"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("date", param.get("date"));
        hashMap.put("time1", param.get("startTime"));
        hashMap.put("time2", param.get("endTime"));
        hashMap.put("user", param.get("user"));

        ClassPathResource classPathResource = new ClassPathResource("report/laporanActualReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportInventoryMovement(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("user", param.get("user"));

        if (param.get("rangeData").equals(1.0)) {
            hashMap.put("query1", "QTY_BEGINNING <> 0");
            hashMap.put("query2", "(quantity_in <> 0 OR quantity <> 0)");
            hashMap.put("query3", " WHERE QTY_IN <> 0 OR QTY_OUT <> 0");
        }
        if (param.get("typeReport").equals(1.0)) {
            ClassPathResource classPathResource = new ClassPathResource("report/reportInventoryMovement.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
            return JasperFillManager.fillReport(jasperReport, hashMap, connection);
        } else {
            ClassPathResource classPathResource = new ClassPathResource("report/reportInventoryMovementDetail.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
            return JasperFillManager.fillReport(jasperReport, hashMap, connection);
        }
    }

    @Override
    public JasperPrint jasperReportStockOpname(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("user", param.get("user"));
        hashMap.put("opnameNo", param.get("opnameNo"));

        ClassPathResource classPathResource = new ClassPathResource("report/stockOpname.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportOrderEntryTransactions(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("typeOrderEntry", param.get("typeOrderEntry"));
        hashMap.put("user", param.get("user"));
        hashMap.put("orderNo", param.get("orderNo"));
        hashMap.put("outletCode", param.get("outletCode"));

        ClassPathResource classPathResource = new ClassPathResource("report/orderEntryTransactions.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportReceivingTransactions(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("user", param.get("user"));
        hashMap.put("city", "X_" + param.get("city"));
        hashMap.put("recvNo", param.get("recvNo"));       
         hashMap.put("noOrder", param.get("noOrder"));
        hashMap.put("outletCode", param.get("outletCode"));

        ClassPathResource classPathResource = new ClassPathResource("report/receivingTransactions.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportWastageTransactions(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("user", param.get("user"));
        hashMap.put("wastageNo", param.get("wastageNo"));
        hashMap.put("outletCode", param.get("outletCode"));

        ClassPathResource classPathResource = new ClassPathResource("report/wastageTransactions.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportReturnOrderTransactions(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("user", param.get("user"));
        hashMap.put("city", "X_" + param.get("city"));
        hashMap.put("returnNo", param.get("returnNo"));
        hashMap.put("outletCode", param.get("outletCode"));

        ClassPathResource classPathResource = new ClassPathResource("report/returnOrderTransactions.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportItemSelectedByTime(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("kodeItem", param.get("kodeItem"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("user", param.get("user"));

        ClassPathResource classPathResource = new ClassPathResource("report/ReportItemSelectedbyTime.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportDeliveryOrderTransactions(Map<String, Object> param, Connection connection)
            throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("user", param.get("user"));
        hashMap.put("city", "X_" + param.get("city"));
        hashMap.put("deliveryNo", param.get("deliveryNo"));
        hashMap.put("requestNo", param.get("requestNo"));
        hashMap.put("outletCode", param.get("outletCode"));

        System.out.println(hashMap);
        ClassPathResource classPathResource = new ClassPathResource("report/deliveryOrderTransactions.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
        }

    ///////////////NEW METHOD REPORT DAFTAR MENU BY RAFI 29 DECEMBER 2023////
     @Override
    public JasperPrint jasperReportDaftarMenu(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("orderType", param.get("orderType"));
        hashMap.put("status", param.get("status"));
        hashMap.put("user", param.get("user"));
        hashMap.put("address1", param.get("address1"));
        hashMap.put("address2", param.get("address2"));
        hashMap.put("outletName", param.get("outletName"));

        ClassPathResource classPathResource = new ClassPathResource("report/reportDaftarMenu.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportSalesVoid(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("userUpd", param.get("userUpd"));
        hashMap.put("detail", param.containsKey("detail") ? param.get("detail").toString() : "0");
        hashMap.put("canceled", param.containsKey("canceled") ? param.get("canceled") : "Item");
        hashMap.put("canceledType", param.containsKey("canceledType") ? param.get("canceledType") : "Semua");

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1 && listPos.contains("Semua")) {
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
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

        System.err.println("jasperReportSalesVoid prm :" + hashMap);
        System.err.println("Order :" + hashMap.get("canceled").toString().equalsIgnoreCase("Order"));
        ClassPathResource classPathResource = new ClassPathResource(hashMap.get("canceled").toString().equalsIgnoreCase("Order") ? "report/salesVoidOrder.jrxml" : "report/salesVoidItem.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }
    
    /////////////////////// new method Delete MPCS produksi adit 04-01-2024
    @Override
    public JasperPrint jesperReportDeleteMpcsProduksi(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("city", "X_" + param.get("city"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("user", param.get("user"));

        if (param.get("typeReport").equals(1.0)) {
            ClassPathResource classPathResource = new ClassPathResource("report/reportDeleteMpcsProduksi.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
            return JasperFillManager.fillReport(jasperReport, hashMap, connection);
        } else {
            ClassPathResource classPathResource = new ClassPathResource("report/reportDeleteMpcsProduksiDetail.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
            return JasperFillManager.fillReport(jasperReport, hashMap, connection);
        }     
    }
    /////////////////////// done adit 04-01-2024

    ///////////////NEW METHOD REPORT REFUND BY RAFI 4 JANUARY 2024////

    @Override
    public JasperPrint jasperReportRefund(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("user", param.get("user"));
        hashMap.put("outletBrand", param.get("outletBrand"));

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1 && listPos.contains("Semua")) {
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
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

        ClassPathResource classPathResource = new ClassPathResource("report/reportRefund.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportProductEfficiency(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("user", param.get("user"));

        ClassPathResource classPathResource = new ClassPathResource("report/reportProductEfficiency.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    /////////////////////// new method report +++ adit 08 Januari 2024
    @Override
    public JasperPrint jesperReportaActualStockOpname(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("user", param.get("user"));

        ClassPathResource classPathResource = new ClassPathResource("report/reportActualStockOpname.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }
    
        @Override
    public JasperPrint jesperReportPerformanceRiderHd(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("periode", param.get("periode"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("typeReport", param.get("typeReport"));
        hashMap.put("user", param.get("user"));

         if (param.get("typeReport").equals(1.0)) {
            ClassPathResource classPathResource = new ClassPathResource("report/reportPerformanceRiderHd.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
            return JasperFillManager.fillReport(jasperReport, hashMap, connection);
        } else {
            ClassPathResource classPathResource = new ClassPathResource("report/reportPerformanceRiderHdDetail.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
            return JasperFillManager.fillReport(jasperReport, hashMap, connection);
        }
    }
    
            @Override
    public JasperPrint jesperReportPajak(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("posType", param.get("posType"));
        hashMap.put("user", param.get("user"));

        ClassPathResource classPathResource = new ClassPathResource("report/reportPajak.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }
    /////////////////////// done adit 04-01-2024

    //////////////// New Method Report Down Payment by Dani 9 Januari 2024
    @Override
    public JasperPrint jasperReportDownPayment(Map<String, Object> param, Connection connection)
            throws JRException, IOException {
        ClassPathResource classPathResource = new ClassPathResource("report/laporanDownPayment.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, param, connection);
    }

    @Override
    public JasperPrint jesperReportSelectedItemByDetail(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("user", param.get("user"));

        List<String> test = (List<String>) param.get("menuItemCodes");
        StringBuilder item = new StringBuilder();
        item.append("'").append(test.get(0)).append("'");

        for (int i = 1; i < test.size(); i++) {
            item.append(", '").append(test.get(i)).append("'");
        }

        hashMap.put("menuItemCodes", item.toString());
        System.out.println(hashMap);

        ClassPathResource classPathResource = new ClassPathResource("report/reportSelectedItemByDetail.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }
    
    //////////////// New Method Laporan Item Selected By Product by M Joko 10 Januari 2024
    @Override
    public JasperPrint jasperReportItemSelectedByProduct(Map<String, Object> param, Connection connection)
            throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("itemCode", param.get("itemCode"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("userUpd", param.get("userUpd"));
        hashMap.put("detail", param.containsKey("detail") && param.get("detail").equals(true) );
        System.err.println("hashMap: " + hashMap);
        ClassPathResource classPathResource = new ClassPathResource("report/itemSelectedByProduct.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }
    
    ///////////////NEW METHOD REPORT PRODUCTION by Sifa 11 Januari 2024////
    @Override
    public JasperPrint jasperReportProduction(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();

        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("mpcsGroup", param.get("mpcsGroup"));
        hashMap.put("user", param.get("user"));
        hashMap.put("detail", param.get("detail"));
        
        String reportPath;
        if ("Jam Aktual".equals(param.get("detail"))) {
            reportPath = "report/productionReportActualTime.jrxml";
        } else {
            reportPath = "report/productionReport.jrxml";
        }
        ClassPathResource classPathResource = new ClassPathResource(reportPath);
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

     ///////////////NEW METHOD REPORT EOD by Dani 16 Januari 2024////
     @Override
     public JasperPrint jasperReportEod(Map<String, Object> param, Connection connection) throws IOException, JRException {
        String query1 = "SELECT D.*, COALESCE((TAXABLE + tax + PEMBULATAN) , 0) AS GROSS_SALES FROM ( SELECT COALESCE(SUM(TOTAL_AMOUNT) - SUM(TOTAL_DISCOUNT), 0) TAXABLE, "
        +" COALESCE(SUM(TOTAL_TAX), 0 ) TAX, "
        +" COALESCE(SUM(TOTAL_ROUNDING), 0 ) PEMBULATAN, "
        +" COALESCE(SUM(TOTAL_CHARGE), 0 ) BIAYA_ANTAR, "
        +" COALESCE(SUM(TOTAL_REFUND), 0 ) REFUND, "
        +" COALESCE(SUM(TOTAL_CUSTOMER), 0 ) CUSTOMER, COALESCE(SUM(TOTAL_DP_PAID), 0) TOTAL_DP_PAID, "
        +" COUNT(0) TRANSAKSI, COALESCE(SUM(TOTAL_DISCOUNT), 0) TOTAL_DISCOUNT, "
        +"    COALESCE(ROUND((SUM(TOTAL_AMOUNT) - SUM(TOTAL_DISCOUNT)) / (NVL(SUM(TOTAL_CUSTOMER), "
        +" 1))), 0 ) CUST_AVERAGE,	 COALESCE(ROUND((SUM(TOTAL_AMOUNT) - SUM(TOTAL_DISCOUNT)) / (COUNT(1))), 0) TICKET_AVG "
        +" FROM T_POS_BILL "
        +" WHERE (DELIVERY_STATUS IN (' ','CLS') OR DELIVERY_STATUS IS NULL)"
        +" AND OUTLET_CODE = :outletCode AND TRANS_DATE BETWEEN :transDate AND :transDate) D";
        Map<String, Object> resultQuery1 = jdbcTemplate.queryForObject(query1, param, new DynamicRowMapper());

        String query2 = "SELECT B.PAYMENT_TYPE_CODE as KEY,SUM(A.PAYMENT_USED) as VALUE "
        +" FROM T_POS_BILL_PAYMENT A, M_PAYMENT_METHOD B"
        +" WHERE A.OUTLET_CODE = :outletCode "
        +" AND A.TRANS_DATE BETWEEN :transDate AND :transDate "
        +" AND A.PAYMENT_METHOD_CODE = B.PAYMENT_METHOD_CODE"
        +" GROUP BY B.PAYMENT_TYPE_CODE";
        List<Map<String, Object>> resultQuery2 = jdbcTemplate.query(query2, param, new DynamicRowMapper());
        
        String query3 = "SELECT TRANS_CODE as KEY, SUM(TRANS_AMOUNT) as VALUE FROM T_POS_DAY_TRANS WHERE OUTLET_CODE = "
        +":outletCode AND TRANS_DATE BETWEEN :transDate AND :transDate GROUP BY "
        +"TRANS_CODE";
        List<Map<String, Object>> resultQuery3 = jdbcTemplate.query(query3, param, new DynamicRowMapper());

        String query5 = "SELECT COALESCE(sum(payment_used), 0) AS downPayment from (select payment_method_code, sum(payment_amount) payment_amount, sum(payment_used) payment_used from " 
        +" t_pos_book_payment where outlet_code = :outletCode and book_no in (select book_no"
        +" from t_pos_book where outlet_code = :outletCode and trans_date between :transDate "
        +" and :transDate) group by payment_method_code)";
        BigDecimal downPayment = jdbcTemplate.queryForObject(query5, param, BigDecimal.class);
        resultQuery1.put("downPayment", downPayment);

        String query6 = "SELECT SUBSTR(TRIM(VOID_TYPE),1,1) AS VOID_TYPE, COUNT(0) AS COUNTER, SUM(AMOUNT) AS AMOUNT FROM T_POS_ITEM_VOID "
        +" WHERE OUTLET_CODE = :outletCode AND TRANS_DATE BETWEEN :transDate AND "
        +" :transDate AND SUBSTR(TRIM(VOID_TYPE),1,1) IN ('V', 'C') GROUP BY SUBSTR(TRIM(VOID_TYPE),1,1)";
        List<Map<String, Object>> resultQuery6 = jdbcTemplate.query(query6, param, new DynamicRowMapper());

        String query7 = "select count(0) AS REFUND_COUNTER, COALESCE ( SUM(total_amount), 0) AS REFUND_AMOUNT from t_pos_bill where outlet_code = :outletCode and trans_date "
        +" between :transDate and :transDate and refund_manager_code <>' ' and "
        +" refund_manager_code is not NULL";
        Map<String, Object> resultQuery7 = jdbcTemplate.queryForObject(query7, param, new DynamicRowMapper());

        // combine query1 and query2 and query3 and query4
        List<Map<String, Object>> combine = new ArrayList<>();
        combine.addAll(resultQuery2);
        combine.addAll(resultQuery3);

       for (Map<String, Object> animal : combine) {
            resultQuery1.put((String) animal.get("key"), (BigDecimal) animal.get("value"));
        }

        for (Map<String,Object> resultMapQry6 : resultQuery6) {
            String voidType = (String) resultMapQry6.get("voidType");
            resultQuery1.put(voidType+"Counter", resultMapQry6.get("counter"));
            resultQuery1.put(voidType+"Amount", resultMapQry6.get("amount"));
        }

        param.putAll(resultQuery1);
        param.putAll(resultQuery7);
        // param.putAll(param);
        System.out.println(param);

        ClassPathResource classPathResource = new ClassPathResource("report/EODReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, param, connection);
     }

    @Override
    public JasperPrint jasperReportItemSalesAnalysis(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("date", param.get("date"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("user", param.get("user"));
        hashMap.put("reportBy", param.get("reportBy"));

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1) {
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
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

        ClassPathResource classPathResource = new ClassPathResource("report/reportItemSalesAnalysis.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }
    
    // Report Time Management by Fathur 22 Januari 2024 //
    @Override
    public JasperPrint jasperReportTimeManagement(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<>();
        String reportName = "report/timeManagementAllUser.jrxml";
        
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("staffCode", param.get("staffCode"));
        hashMap.put("userUpd", param.get("userUpd"));
        hashMap.put("additionalQuery", " ");
        
        if(!param.get("staffCode").equals("Semua")) {
            reportName = "report/timeManagementSelectedUser.jrxml";
            hashMap.put("additionalQuery", " AND a.STAFF_ID = '" + param.get("staffCode")+ "' ");
        }
        
        ClassPathResource classPathResource = new ClassPathResource(reportName);
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }

    @Override
    public JasperPrint jasperReportSalesItembyTime(Map<String, Object> param, Connection connection) throws JRException, IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("user", param.get("user"));
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("fromTime", param.get("fromTime"));
        hashMap.put("toTime", param.get("toTime"));
        hashMap.put("outletCode", param.get("outletCode"));

        List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");
        StringBuilder posCode = new StringBuilder();
        if (listPos.size() == 1) {
            hashMap.put("posCode", "Semua");
            hashMap.put("posCode1", "000");
            hashMap.put("posCode2", "zzz");
        } else {
            for (Map<String, Object> object : listPos) {
                if (object.containsKey("posCode1")) {
                    hashMap.put("posCode1", object.get("posCode1"));
                    posCode.append(object.get("posName1")).append(" s.d. ");
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
                    cashierCode.append(object.get("cashierName1")).append(" s.d. ");
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
                    shiftCode.append(object.get("shiftName1")).append(" s.d. ");
                } else {
                    hashMap.put("shiftCode2", object.get("shiftCode2"));
                    shiftCode.append(object.get("shiftName2"));
                }
                hashMap.put("shiftCode", shiftCode.toString());
            }
        }
        
        String queryOutlet = "SELECT mo.OUTLET_NAME, mo.ADDRESS_1, mo.ADDRESS_2, mo.PHONE FROM M_OUTLET mo WHERE mo.OUTLET_CODE = :outletCode";
        jdbcTemplate.query(queryOutlet, hashMap, (ResultSet rs, int i) -> {
            hashMap.put("outletName", rs.getString("OUTLET_NAME"));
            hashMap.put("address1", rs.getString("ADDRESS_1"));
            hashMap.put("address2", rs.getString("ADDRESS_2"));
            hashMap.put("phone", rs.getString("PHONE"));
            return null;
        });

        ClassPathResource classPathResource = new ClassPathResource("report/salesItemByTime.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }
    
    ///////////////// new report MPCS Management Fryer by aditya 30 Jan 2024
    
     @Override
    public JasperPrint jesperReportMpcsManagementFryer(Map<String, Object> param, Connection connection) throws IOException, JRException {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("fromDate", param.get("fromDate"));
        hashMap.put("toDate", param.get("toDate"));
        hashMap.put("outletCode", param.get("outletCode"));
        hashMap.put("outletBrand", param.get("outletBrand"));
        hashMap.put("fryerType", param.get("fryerType"));
        hashMap.put("user", param.get("user"));

        ClassPathResource classPathResource = new ClassPathResource("report/reportMpcsManagementFryer.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
        return JasperFillManager.fillReport(jasperReport, hashMap, connection);
    }
    /////////////////////// done adit 30-01-2024
}
