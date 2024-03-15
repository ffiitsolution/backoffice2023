package com.ffi.api.backoffice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 *
 * added by M Joko - 4/3/24 digunakan untuk intercept seluruh request saat ini
 * hanya POST untuk log aktivitas user
 *
 */
@WebFilter("/*")
@Component()
public class RequestBodyFilter implements Filter {

    private final String VIEW = "VIEW";
    private final String CREATE = "CREATE";
    private final String UPDATE = "UPDATE";
    private final String DELETE = "DELETE";
    private final String SEND = "SEND";
    private final String PRINT = "PRINT";

    @Autowired
    private FileLoggerUtil fileLoggerUtil;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // sesuaikan kondisi endpoint dan param, simpan ke file logger
    private void logActivity(String ep, Map<String, Object> param) {
        if (checkValidParam(param.get("actUser")) && checkValidParam(param.get("actName")) && checkValidParam(param.get("actUrl"))) {
            param.put("actEndpoint", ep);
            String staffCode = param.getOrDefault("actUser", "").toString();
            String staffName = param.getOrDefault("actName", "").toString();
            String outletCode = param.getOrDefault("actOutlet", "").toString();
            String url = param.getOrDefault("actUrl", "").toString();
            String query = "SELECT * FROM M_LEVEL_AKSES_DETAIL WHERE URL = :actUrl";
            String module = "-";
            String action = VIEW;
            String remark = "";
            Boolean success = Boolean.TRUE;
            Map<String, Object> obj;
            List<Map<String, Object>> list = jdbcTemplate.query(query, param, new DynamicRowMapper());
            if (!list.isEmpty()) {
                obj = list.get(0);
                module = obj.getOrDefault("description", "").toString();
            }

            switch (ep) {
                case "/report-order-entry-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Order Entry";
                }
                case "/report-receiving-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Receiving";
                }
                case "/report-return-order-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Return Order";
                }
                case "/report-wastage-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Wastage";
                }
                case "/report-delivery-order-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Delivery Order";
                }
                case "/report-delivery-order-ex-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Delivery Order Ex";
                }
                case "/report-item-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Item";
                }
                case "/report-stock-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Stock";
                }
                case "/report-recipe-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Recipe";
                }
                case "/report-free-meal-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Free Meal";
                }
                case "/report-sales-by-time-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Sales By Time";
                }
                case "/report-sales-by-date-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Sales By Date";
                }
                case "/report-sales-by-date-new-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Sales By Date - New";
                }
                case "/report-sales-by-item-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Sales By Item";
                }
                case "/report-menu-vs-detail-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Menu Vs Detail";
                }
                case "/report-summary-sales-by-item-code-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Summary Sales By Item Code";
                }
                case "/report-stock-card-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Stock Card";
                }
                case "/report-transaksi-kasir-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Transaksi Kasir";
                }
                case "/report-receipt-maintenance-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Receipt Maintenance";
                }
                case "/report-sales-mix-department-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Sales Mix Department";
                }
                case "/report-query-bill-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Query Bill";
                }
                case "/report-transaction-by-payment-type-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Transaction By Payment Type";
                }
                case "/report-pemakaian-by-sales-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Pemakaian By Sales";
                }
                case "/report-produksi-aktual-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Produksi Aktual";
                }
                case "/report-inventory-movement-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Inventory Movement";
                }
                case "/report-stock-opname-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Stock Opname";
                }
                case "/report-order-entry-transactions-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Transaksi Order Entry";
                }
                case "/report-receiving-transactions-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Transaksi Receiving";
                }
                case "/report-wastage-transactions-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Transaksi Wastage";
                }
                case "/report-return-order-transactions-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Transaksi Return Order";
                }
                case "/report-delivery-order-transactions-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Transaksi Delivery Order";
                }
                case "/report-return-item-selected-by-time-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Return Item Selected By Time";
                }
                case "/report-sales-void-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Sales Void";
                }
                case "/report-item-selected-by-product-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Item Selected By Product";
                }
                case "/report-daftar-menu-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Daftar Menu";
                }
                case "/report-delete-mpcs-produksi-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Delete MPCS Produksi";
                }
                case "/report-report-refund-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Refund";
                }
                case "/report-actual-stock-opname-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Actual Stock Opname";
                }
                case "/report-performance-rider-hd-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Performance Rider HD";
                }
                case "/report-pajak-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Pajak";
                }
                case "/report-product-efficiency-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Product Efficiency";
                }
                case "/report-down-payment-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Down Payment";
                }
                case "/report-selected-item-by-detail-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Selected Item By Detail";
                }
                case "/report-production-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Production";
                }
                case "/report-eod-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "End Of Day";
                }
                case "/report-item-sales-analysis-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Item Sales Analysis";
                }
                case "/report-time-management-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Time Management";
                }
                case "/report-sales-item-by-time-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Sales Item By Time";
                }
                case "/report-mpcs-management-fryer-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "MPCS Management Fryer";
                }
                case "/report-transaksi-hd-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Transaksi HD";
                }
                case "/report-cash-pull-jesper" -> {
                    module = "Report";
                    action = PRINT;
                    remark = "Cash Pull";
                }
                default -> {
                    // todo: mapping semua endpoint 
                }
            }
            fileLoggerUtil.logActivity(url, module, PRINT, staffCode, staffName, remark, success, ep, param);

        }
    }

    private boolean checkValidParam(Object object) {
        return object != null && !object.toString().isBlank();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String method = request.getMethod();
        StringBuffer urlSB = request.getRequestURL();

        if ("POST".equalsIgnoreCase(method)) {
            CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
            String requestBody = new String(cachedRequest.getInputStream().readAllBytes());
            String BOFFI = "/boffi";
            String endpoint = "";
            int index = urlSB.indexOf(BOFFI) + BOFFI.length();
            if (index >= 0 && index < urlSB.length()) {
                endpoint = urlSB.substring(index);
            }
            if (!endpoint.isBlank() && requestBody.length() > 2) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Map<String, Object> map = objectMapper.readValue(requestBody, Map.class);
                    logActivity(endpoint, map);
                } catch (IOException e) {
                    e.getMessage();
                }
            }
            request = cachedRequest;
        }

        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic
    }

    @Override
    public void destroy() {
        // Cleanup logic
    }
}
