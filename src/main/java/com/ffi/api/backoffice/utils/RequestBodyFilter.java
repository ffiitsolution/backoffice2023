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
                // ALL REPORT CASE
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
                case "/report-report-refund-jesper"  -> {
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

                // TRANSACTION ORDER ENTRY CASE
                case "/list-order-header-all" -> {
                    module = "Transaction - Order Entry";
                    action = VIEW;
                    remark = "All Transaction Order Entry";
                }
                case "/list-order-by-orderno" -> {
                    module = "Transaction - Order Entry";
                    action = VIEW;
                    remark = "Detail Order Entry";
                }
                case "/get-order-entry-status" -> {
                    module = "Transaction - Order Entry";
                    action = VIEW;
                    remark = "Check Status Order Entry";
                }
                case "/send-data-to-warehouse" -> {
                    module = "Transaction - Order Entry";
                    action = SEND;
                    remark = "Send Order Entry Data to Warehouse";
                }
                case "/list-counter" -> {
                    module = "Transaction - Order Entry";
                    action = VIEW;
                    remark = "Get Order ID or Order Number";
                }
                case "/list-master-global" -> {
                    module = "Transaction";
                    action = VIEW;
                    remark = "Get a List of All Master Data";
                } 
                case "/list-outlet" -> {
                    module = "Transaction";
                    action = VIEW;
                    remark = "Get All Outlet List";
                }
                case "/list-supplier" -> {
                    module = "Transaction";
                    action = VIEW;
                    remark = "Get All Supplier List";
                }
                case "/list-warehouse-fsd" -> {
                    module = "Transaction - Order Entry";
                    action = VIEW;
                    remark = "Get All Warehouse FSD";
                }
                case "/order-detail-temporary-list" -> {
                    module = "Transaction - Order Entry";
                    action = CREATE;
                    remark = "Create Order Detail Temporary List Item";
                }
                case "/insert-order-header" -> {
                    module = "Transaction - Order Entry";
                    action = CREATE;
                    remark = "Insert Order Entry";
                }
                case "/remove-empty-order" -> {
                    module = "Transaction - Order Entry";
                    action = DELETE;
                    remark = "Remove Order Entry Local Storage";
                }

                // TRANSACTION RECEIVING CASE
                case "/view-rcv-header" -> {
                    module = "Transaction - Receiving";
                    action = VIEW;
                    remark = "Get All Receiving Data";
                }
                case "/view-rcv-detail" -> {
                    module = "Transaction - Receiving";
                    action = VIEW;
                    remark = "Get Detailed Receiving";
                }
                case "/list-receiving-all" -> {
                    module = "Transaction - Receiving";
                    action = VIEW;
                    remark = "Get All Receiving List Order";
                }
                case "/get-order-detail-inventory" -> {
                    module = "Transaction - Receiving";
                    action = VIEW;
                    remark = "Get Order Detail Inventory";
                }
                case "/view-order-detail" -> {
                    module = "Transaction - Receiving";
                    action = VIEW;
                    remark = "Get All Order Detail";
                }
                case "/insert-rcv-headetail" -> {
                    module = "Transaction - Receiving";
                    action = CREATE;
                    remark = "Insert Receiving Data Head Detail";
                }

                // TRANSACTION DELIVERY ORDER CASE 
                case "/list-delivery-order" -> {
                    module = "Transaction - Delivery Order";
                    action = VIEW;
                    remark = "Get All List for Delivery Order";
                }
                case "/get-delivery-order" -> {
                    module = "Transaction - Delivery Order";
                    action = VIEW;
                    remark = "View Delivery Order Detail";
                }
                case "/kirim-delivery-order" -> {
                    module = "Transaction - Delivery Order";
                    action = SEND;
                    remark = "Send Data Delivery Order";
                }
                case "/list-order-detail-outlet" -> {
                    module = "Transaction - Delivery Order";
                    action = VIEW;
                    remark = "View All Items Delivery Order";
                }
                case "/list-outlet-ho" -> {
                    module = "Transaction - Delivery Order";
                    action = VIEW;
                    remark = "View All Head Office Departement";
                }
                case "/generate-delivery-order-freemeal" -> {
                    module = "Transaction - Delivery Order";
                    action = VIEW;
                    remark = "Generate Delivery Order Free Meal Code";
                }
                case "/delivery-order-check-exist-no-request" -> {
                    module = "Transaction - Delivery Order";
                    action = SEND;
                    remark = "Check Existing Order Delivery Based on Request Number";
                }
                case "/insert-update-delivery-order" -> {
                    module = "Transaction - Delivery Order";
                    action = CREATE;
                    remark = "Insert Delivery Order Items";
                }

                // TRANSACTION WASTAGE AND LEFTOVER CASE
                case "/wastage-header" -> {
                    module = "Transaction - Wastage";
                    action = VIEW;
                    remark = "View All Wastage Data";
                }
                case "/wastage-detail" -> {
                    module = "Transaction - Wastage";
                    action = VIEW;
                    remark = "View Detail of Wastage Data";
                }
                case "/item" -> {
                    module = "Transaction - Wastage";
                    action = VIEW;
                    remark = "View All item";
                }
                case "/insert-wastage-headetail" -> {
                    module = "Transaction - Wastage";
                    action = CREATE;
                    remark = "Insert Items Wastage or Leftover";
                }

                //TRANSACTION RETURN ORDER CASE
                case "/return-order-header" -> {
                    module = "Transaction - Return Order";
                    action = VIEW;
                    remark = "View All Data of Return Order";
                }
                case "/list-supplier-gudang-return" -> {
                    module = "Transaction - Return Order";
                    action = VIEW;
                    remark = "View All List of Supplier and Warehouse";
                } 
                case "/list-item-supplier-gudang-return" -> {
                    module = "Transaction - Return Order";
                    action = VIEW;
                    remark = "View All Item Supplier or Gudang";
                }
                case "/insert-return-order-headetail" -> {
                    module = "Transaction - Return Order";
                    action = CREATE;
                    remark = "Insert Return Order Items";
                }

                // TRANSACTION MPCS CASE
                case "/view-mpcs-temp" -> {
                    module = "Transaction - MPCS";
                    action = VIEW;
                    remark = "View MPCS Template";
                }
                case "/list-mpcs-header" -> {
                    module = "Transaction - MPCS";
                    action = VIEW;
                    remark = "View All MPCS List";
                }
                case "/list-mpcs-management-fryer" -> {
                    module = "Transaction - MPCS";
                    action = VIEW;
                    remark = "View All List MPCS Management Fryer";
                }
                case "/list-mpcs-plan" -> {
                    module = "Transaction - MPCS";
                    action = VIEW;
                    remark = "View All List MPCS Plan";
                }
                case "/update-mpcs-plan" -> {
                    module = "Transaction - MPCS";
                    action = UPDATE;
                    remark = "Update List MPCS Plan";
                }
                case "/recipe-header" -> {
                    module = "Transaction - MPCS";
                    action = VIEW;
                    remark = "View All Recipe Header";
                }
                case "/mpcs-production-recipe" -> {
                    module = "Transaction - MPCS";
                    action = VIEW;
                    remark = "View All Recipe production";
                }   
                case "/mpcs-production-product-result" -> {
                    module = "Transaction - MPCS";
                    action = VIEW;
                    remark = "View All Recipe production result";
                } 
                case "/insert-mpcs-management-fryer" -> {
                    module = "Transaction - MPCS";
                    action = CREATE;
                    remark = "Insert Item Into MPCS Management Fryer";
                }

                // MASTER USER CASE
                case "/list-staff" -> {
                    module = "Master - User";
                    action = VIEW;
                    remark = "View All Staff List";
                }
                case "/list-region" -> {
                    module = "Master - User";
                    action = VIEW;
                    remark = "View All Region List";
                }
                case "/list-viewgroupuser" -> {
                    module = "Master - User";
                    action = VIEW;
                    remark = "View All List View Group User";
                }
                case "/update-staff" -> {
                    module = "Master - User";
                    action = UPDATE;
                    remark = "Update Data User";
                }
                case "/insert-staff" -> {
                    module = "Master - User";
                    action = CREATE;
                    remark = "Add New User";
                }

                // MASTER GLOBAL CASE 
                case "/list-cond-global" -> {
                    module = "Master - Global";
                    action = VIEW;
                    remark = "View All Cond Global";
                }

                // MASTER OUTLET CASE
                case "/list-type-store" -> {
                    module = "Master - Outlet";
                    action = VIEW;
                    remark = "View All Outlet Types";
                }
                case "/list-outlet-detail" -> {
                    module = "Master - Outlet";
                    action = VIEW;
                    remark = "View Outlet Detail Data";
                }
                case "/list-outlet-detail-group" -> {
                    module = "Master - Outlet";
                    action = VIEW;
                    remark = "View Outlet Detail Group";
                }
                case "/list-outlet-detail-type-order" -> {
                    module = "Master - Outlet";
                    action = VIEW;
                    remark = "View List of All Type Detail Order Outlet";
                }
                case "/update-outlet" -> {
                    module = "Master - Outlet";
                    action = UPDATE;
                    remark = "Outlet Data Update";
                }

                // MASTER MENU CASE
                case "/list-menu-group" -> {
                    module = "Master - Menu";
                    action = VIEW;
                    remark = "View All List of Menu Group";
                }
                case "/list-menu-group-tipe-order" -> {
                    module = "Master - Menu";
                    action = VIEW;
                    remark = "View All List of Menu group Type Order";
                }
                case "/item-menus" -> {
                    module = "Master - Menu";
                    action = VIEW;
                    remark = "View All Item Menu";
                }
                case "/item-menus-set" -> {
                    module = "Master - Menu";
                    action = VIEW;
                    remark = "View Item Menu Set";
                }
                case "/item-menus-tipe-order" -> {
                    module = "Master - Menu";
                    action = VIEW;
                    remark = "View All Type Order Menu";
                }
                case "/item-menus-limit" -> {
                    module = "Master - Menu";
                    action = VIEW;
                    remark = "Viewing Menu Item Limitations on Outlets";
                }

                // MASTER PRICE CASE
                case "/list-item-price" -> {
                    module = "Master - Menu";
                    action = VIEW;
                    remark = "View All Item Price Lists";
                }

                // MASTER FRYER TYPE CASE
                case "/update-frayer" -> {
                    module = "Master - Fryer Type";
                    action = UPDATE;
                    remark = "Update Fryer Type";
                }
                case "/insert-fryer" -> {
                    module = "Master - Fryer Type";
                    action = CREATE;
                    remark = "Insert New Fryer";
                }

                // MASTER GROUP ITEM CASE
                case "/menu-items" -> { 
                    module = "Master - Group Item";
                    action = VIEW;
                    remark = "View All Menu Item";
                }
                case "/group-items" -> {
                    module = "Master - Group Item";
                    action = VIEW;
                    remark = "View All Item Groupings";
                }

                // MASTER SUPPLIER CASE
                case "/list-master-city" -> {
                    module = "Master - Supplier";
                    action = VIEW;
                    remark = "View All Master City";
                }
                case "/list-position" -> {
                    module = "Master - Supplier";
                    action = VIEW;
                    remark = "View All List Position";
                }
                case "/list-master-item-for-supplier" -> {
                    module = "Master - Supplier";
                    action = VIEW;
                    remark = "View Master Item Supplier";
                }
                case "/update-supplier" -> {
                    module = "Master - Supplier";
                    action = UPDATE;
                    remark = "Update Master Supplier";
                }
                case "/insert-supplier" -> {
                    module = "Master - Supplier";
                    action = CREATE;
                    remark = "Insert New Supplier";
                }
                case "/list-supplier-item" -> {
                    module = "Master - Supplier";
                    action = VIEW;
                    remark = "View All Supplier Item Lists";
                }
                
                // MASTER ITEM CASE
                case "/item-detail" -> {
                    module = "Master - Item";
                    action = VIEW;
                    remark = "View Detail of item";
                }

                // MASTER RECIPE PRODUCTION CASE
                case "/recipe-status-update" -> {
                    module = "Master - Recipe Production";
                    action = UPDATE;
                    remark = "Update Recipe Status";
                }
                case "/recipe-detail" -> {
                    module = "Master - Recipe Production";
                    action = VIEW;
                    remark = "View Recipe Detail";
                }
                case "/recipe-product" -> {
                    module = "Master - Recipe Production";
                    action = VIEW;
                    remark = "View Recipe Product";
                }

                // MASTER MPCS
                case "/update-mpcs" -> {
                    module = "Master - MPCS";
                    action = UPDATE;
                    remark = "Update MPCS GROUP";
                }

                // EOD & QUERY SALES - QUERY BILL CASE
                case "/list-query-bill" -> {
                    module = "Eod & Query Sales - Query Bill";
                    action = VIEW;
                    remark = "View All List Query Bill And View Te Details";
                }
                case "/list-param-report" -> {
                    module = "Eod & Query Sales - Query Bill";
                    action = VIEW;
                    remark = "View All List Query Bill";
                }
                case "/list-query-sales"-> {
                    module = "Eod & Query Sales - Query Sales";
                    action = VIEW;
                    remark = "View All List Query Sales";
                }

                // EOD & QUERY SALES - END OF DAY
                case "/last-eod" -> {
                    module = "Eod & Query Sales - End of Day";
                    action = VIEW;
                    remark = "View History of Last EOD";
                }
                case "/process-eod" -> {
                    module = "Eod & Query Sales - End of Day";
                    action = SEND;
                    remark = "Send Process of EOD";
                }
                case "/list-stock-opname" -> {
                    module = "Eod & Query Sales - End of Day";
                    action = VIEW;
                    remark = "View List of All Stock Opname";
                }
                case "/backup-database" -> {
                    module = "Eod & Query Sales - End of Day";
                    action = CREATE;
                    remark = "Create Backup Database Of EOD";
                }

                // EOD & QUERY SALES - KIRIM TERIMA DATA
                case "/list-transfer-data-history"-> {
                    module = "Eod & Query Sales - Kirim Terima Data";
                    action = VIEW;
                    remark = "View All History Kirim Terima Data";
                }
                case "/list-transfer-data" -> {
                    module = "Eod & Query Sales - Kirim Terima Data";
                    action = VIEW;
                    remark = "View All Transfer Data List";
                }
                case "/copy-single" -> {
                    module = "Eod & Query Sales - Kirim Terima Data";
                    action = SEND;
                    remark = "Updating Copy Data Single";
                }
                case "/transfer-data-single" -> {
                    module = "Eod & Query Sales - Kirim Terima Data";
                    action = SEND;
                    remark = "Updating Transfer Data Single";
                }

                // TIME MANAGEMENT
                case "/get-id-absensi" -> {
                    module = "Time Management - Transaksi";
                    action = SEND;
                    remark = "Get an Attendance ID";
                }
                case "/" -> {

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
