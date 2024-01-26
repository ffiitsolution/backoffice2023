/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import com.ffi.api.backoffice.dao.ViewDao;
import com.ffi.api.backoffice.model.ParameterLogin;
import com.ffi.api.backoffice.utils.AppUtil;
import com.ffi.api.backoffice.utils.DynamicRowMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dwi Prasetyo
 */
@Repository
public class ViewDoaImpl implements ViewDao {

    @Value("${endpoint.warehouse}")
    private String warehouseEndpoint;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AppUtil appUtil;

    @Autowired
    public ViewDoaImpl(NamedParameterJdbcTemplate jdbcTemplate, AppUtil appUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.appUtil = appUtil;
    }

    @Override
    public List<Map<String, Object>> loginJson(ParameterLogin ref) {
        String qry = "SELECT S.REGION_CODE, O.TRANS_DATE, O.ADDRESS_1, O.PHONE, G.DESCRIPTION AS REG_NAME, S.OUTLET_CODE, O.OUTLET_NAME, S.STAFF_CODE, S.STAFF_NAME, S.STAFF_FULL_NAME, S.ID_CARD, S.POSITION, S.GROUP_ID, O.CITY, S.STATUS FROM M_STAFF S JOIN M_OUTLET O ON O.OUTLET_CODE = S.OUTLET_CODE JOIN M_GLOBAL G ON G.CODE = S.REGION_CODE AND G.COND = 'REG_OUTLET' WHERE S.STAFF_CODE = :staffCode AND S.PASSWORD = :pass AND S.OUTLET_CODE = :outletCode";
        Map prm = new HashMap();
        prm.put("staffCode", ref.getUserName());
        prm.put("pass", ref.getPassword());
        prm.put("outletCode", ref.getOutletCode());
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<>();
                rt.put("regCode", rs.getString("REGION_CODE"));
                rt.put("regName", rs.getString("REG_NAME"));
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("outletName", rs.getString("OUTLET_NAME"));
                rt.put("staffCode", rs.getString("STAFF_CODE"));
                rt.put("staffName", rs.getString("STAFF_NAME"));
                rt.put("staffFullName", rs.getString("STAFF_FULL_NAME"));
                rt.put("idCard", rs.getString("ID_CARD"));
                rt.put("position", rs.getString("POSITION"));
                rt.put("groupId", rs.getString("GROUP_ID"));
                rt.put("cityCode", rs.getString("CITY"));
                rt.put("transDate", rs.getString("TRANS_DATE"));
                rt.put("address1", rs.getString("ADDRESS_1"));
                rt.put("phone", rs.getString("PHONE"));
                rt.put("status", rs.getString("STATUS"));
                return rt;
            }
        });
        return list;
    }
    ///////////////new method from dona 28-02-2023////////////////////////////

    @Override
    public List<Map<String, Object>> listSupplier(Map<String, String> balance) {
        String qry = "SELECT  CD_SUPPLIER, SUPPLIER_NAME, CP_NAME, FLAG_CANVASING, STATUS, ADDRESS_1, "
                + "ADDRESS_2, CITY, ZIP_CODE, PHONE, FAX, HOMEPAGE, CP_TITLE, CP_MOBILE, CP_PHONE, "
                + "CP_PHONE_EXT, CP_EMAIL, USER_UPD, DATE_UPD, TIME_UPD FROM m_supplier"
                + " where status like :status"
                + " and city LIKE :city"
                + " and FLAG_CANVASING Like :flagCanvasing"
                + " order by cd_Supplier desc";
        Map prm = new HashMap();
        prm.put("status", "%" + balance.get("status") + "%");
        prm.put("city", "%" + balance.get("city") + "%");
        prm.put("flagCanvasing", "%" + balance.get("flagCanvasing") + "%");
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap< String, Object>();
                rt.put("cdSupplier", rs.getString("CD_SUPPLIER"));
                rt.put("supplierName", rs.getString("SUPPLIER_NAME"));
                rt.put("cpName", rs.getString("CP_NAME"));
                rt.put("flagCanvasing", rs.getString("FLAG_CANVASING"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("address1", rs.getString("ADDRESS_1"));
                rt.put("address2", rs.getString("ADDRESS_2"));
                rt.put("city", rs.getString("CITY"));
                rt.put("zipCode", rs.getString("ZIP_CODE"));
                rt.put("phone", rs.getString("PHONE"));
                rt.put("fax", rs.getString("FAX"));
                rt.put("homepage", rs.getString("HOMEPAGE"));
                rt.put("cpTitle", rs.getString("CP_TITLE"));
                rt.put("cpMobile", rs.getString("CP_MOBILE"));
                rt.put("cpPhone", rs.getString("CP_PHONE"));
                rt.put("cpPhoneext", rs.getString("CP_PHONE_EXT"));
                rt.put("cpEmail", rs.getString("CP_EMAIL"));
                rt.put("userUpd", rs.getString("USER_UPD"));
                rt.put("dateUpd", rs.getString("DATE_UPD"));
                rt.put("timeUpd", rs.getString("TIME_UPD"));

                return rt;
            }
        });
        return list;
    }
    ///////////////////done
    ///////////////new method from dona 28-02-2023////////////////////////////

    @Override
    public List<Map<String, Object>> listDataItemSupplier(Map<String, String> balance) {
        String qry = "SELECT  a.CD_SUPPLIER, a.ITEM_CODE,b.item_description,a.STATUS, a.USER_UPD, a.DATE_UPD,a.TIME_UPD FROM M_ITEM_SUPPLIER A  "
                + "left join m_item B  "
                + "on a.item_code=b.item_code WHERE CD_SUPPLIER=:cdSupplier";
        Map prm = new HashMap();
        prm.put("cdSupplier", balance.get("cdSupplier"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("cdSupplier", rs.getString("CD_SUPPLIER"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("userUpd", rs.getString("USER_UPD"));
                rt.put("dateUpd", rs.getString("DATE_UPD"));
                rt.put("timeUpd", rs.getString("TIME_UPD"));

                return rt;
            }
        });
        return list;
    }

    ///////////////new update sql from dona 12-07-2023////////////////////////////
    @Override
    public List<Map<String, Object>> listMasterItem(Map<String, String> balance) {
        String qry = "select ITEM_CODE||' -'||ITEM_DESCRIPTION as name,item_description, item_code from m_item where Status='A' and Flag_Finished_Good='N' order by item_code asc";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("name", rs.getString("name"));
                return rt;
            }
        });
        return list;
    }
    /////////////////done////////////////

    @Override
    public List<Map<String, Object>> listItemSupplier(Map<String, String> balance) {

        String qry = "select  a.CD_TEMPLATE,a.item_code,b.item_description,"
                + "a.STATUS, a.USER_UPD, a.DATE_UPD, a.TIME_UPD from M_ITEM_SUPPLIER a left join "
                + "m_item b "
                + "on a.item_code=B.ITEM_CODE "
                + "WHERE a.CD_SUPPLIER=:cdSupplier ";
        Map prm = new HashMap();
        prm.put("cdSupplier", balance.get("cdSupplier"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("cdSupplier", rs.getString("CD_SUPPLIER"));
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("userUpd", rs.getString("USER_UPD"));
                rt.put("dateUpd", rs.getString("DATE_UPD"));
                rt.put("timeUpd", rs.getString("TIME_UPD"));
                return rt;
            }
        });
        return list;
    }
    ///////////////////done
    ///////////////new method from dona 06-03-2023////////////////////////////

    @Override
    public List<Map<String, Object>> listMpcs(Map<String, String> balance) {
        String qry = "SELECT A.OUTLET_CODE, B.OUTLET_NAME, A.FRYER_TYPE, A.FRYER_TYPE_SEQ, A.STATUS, A.FRYER_TYPE_RESET, A.FRYER_TYPE_SEQ_CNT, A.FRYER_TYPE_CNT, A.USER_UPD, A.DATE_UPD, A.TIME_UPD FROM M_MPCS_DETAIL A LEFT JOIN M_OUTLET B ON A.OUTLET_CODE = B.OUTLET_CODE WHERE A.OUTLET_CODE =:outletCode AND (A.FRYER_TYPE = :fryerType OR :fryerType IS NULL) ORDER BY A.OUTLET_CODE ASC, A.FRYER_TYPE ASC, A.FRYER_TYPE_SEQ ASC";
        Map prm = new HashMap();
        prm.put("outletCode", balance.get("outletCode"));
        prm.put("fryerType", balance.get("fryerType"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("fryerType", rs.getString("FRYER_TYPE"));
                rt.put("fryerTypeSeq", rs.getString("FRYER_TYPE_SEQ"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("fryerTypeReset", rs.getString("FRYER_TYPE_RESET"));
                rt.put("fryerTypeSeqCnt", rs.getString("FRYER_TYPE_SEQ_CNT"));
                rt.put("fryerTypeCnt", rs.getString("FRYER_TYPE_CNT"));
                rt.put("userUpd", rs.getString("USER_UPD"));
                rt.put("dateUpd", rs.getString("DATE_UPD"));
                rt.put("timeUpd", rs.getString("TIME_UPD"));

                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listItemCost(Map<String, String> balance) {
        String qry = "SELECT A.ITEM_CODE,A.ITEM_DESCRIPTION,B.ITEM_COST,A.STATUS FROM M_ITEM A LEFT JOIN M_ITEM_COST B ON A.ITEM_CODE=B.ITEM_CODE "
                + "WHERE A.ITEM_CODE IS NOT NULL AND A.ITEM_CODE <>' ' "
                + "and a.flag_material =:flagMaterial  AND A.STATUS='A' ORDER  BY A.ITEM_CODE ASC ";
        if (balance.get("flagMaterial").equalsIgnoreCase("A")) {
            qry = "SELECT A.ITEM_CODE,A.ITEM_DESCRIPTION,B.ITEM_COST,A.STATUS FROM M_ITEM A LEFT JOIN M_ITEM_COST B ON A.ITEM_CODE=B.ITEM_CODE "
                    + "WHERE A.ITEM_CODE IS NOT NULL AND A.ITEM_CODE <>' ' "
                    + "and a.FLAG_HALF_FINISH ='Y' AND A.STATUS='A' ORDER  BY A.ITEM_CODE ASC ";
        }
        if (balance.get("flagMaterial").equalsIgnoreCase("B")) {
            qry = "SELECT A.ITEM_CODE,A.ITEM_DESCRIPTION,B.ITEM_COST,A.STATUS FROM M_ITEM A LEFT JOIN M_ITEM_COST B ON A.ITEM_CODE=B.ITEM_CODE "
                    + "WHERE A.ITEM_CODE IS NOT NULL AND A.ITEM_CODE <>' ' "
                    + "and a.FLAG_FINISHED_GOOD ='Y' AND A.STATUS='A' ORDER  BY A.ITEM_CODE ASC ";
        }
        Map prm = new HashMap();
        prm.put("flagMaterial", balance.get("flagMaterial"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                //rt.put("flagMaterial", rs.getString("FLAG_MATERIAL"));
                rt.put("itemCost", rs.getString("ITEM_COST"));
                //rt.put("flagFinishedGood", rs.getString("FLAG_FINISHED_GOOD"));
                rt.put("status", rs.getString("STATUS"));

                return rt;
            }
        });
        return list;
    }
    ///////////////////done

    ///////////////new method from budi 14-03-2023////////////////////////////
    /////edited by dona 4 des 2023///////////////////
    /////Update where clause by Fathur 6 Des 2023///////////////////
    @Override
    public List<Map<String, Object>> listMenuGroup(Map<String, String> ref) {
        if(ref.containsKey("menuGroupCode") && (ref.get("menuGroupCode").equalsIgnoreCase("G90") || ref.get("menuGroupCode").equalsIgnoreCase("G91"))){
            return new ArrayList();
        }
        String qry = "SELECT   "
                + "    M.MENU_GROUP_CODE,M.STATUS,M.SEQ, "
                + "    G.DESCRIPTION AS MENU_GROUP "
                + "FROM M_MENU_GROUP M  "
                + "JOIN M_GLOBAL G  "
                + "ON M.MENU_GROUP_CODE = G.CODE  "
                + "WHERE G.COND = 'GROUP' AND M.OUTLET_CODE LIKE :outletCode ";
        Map prm = new HashMap();
        prm.put("outletCode", "%" + ref.get("outletCode") + "%");
        var allStatusParamValue = ref.getOrDefault("allStatus", "N");

        if ("N".equals(allStatusParamValue)) {
            qry += "AND M.STATUS = 'A' ORDER BY MENU_GROUP_CODE";
        } else {
            qry += "AND M.STATUS IN ('A', 'I' ) ORDER BY MENU_GROUP_CODE";
        }
        
        if(ref.containsKey("type") && ref.get("type").equalsIgnoreCase("Price")){
            qry = "SELECT g.CODE AS menu_group_code, g.description AS menu_group, g.value AS seq, g.status FROM M_GLOBAL g WHERE g.COND = 'GROUP' AND g.CODE LIKE 'G%' AND g.STATUS = 'A' ORDER BY g.code";
        } else if(ref.containsKey("type") && ref.get("type").equalsIgnoreCase("PriceSub")){
            qry = """
                    SELECT 
                            mg3.CODE AS MENU_GROUP_CODE,
                            mi.ITEM_DESCRIPTION AS SEQ,
                            mg3.DESCRIPTION AS MENU_GROUP,
                            mg3.STATUS AS STATUS
                    FROM M_MENU_ITEM mmi 
                    LEFT JOIN M_ITEM mi ON mi.ITEM_CODE = mmi.MENU_ITEM_CODE
                    LEFT JOIN M_PRICE mp ON mp.MENU_ITEM_CODE = mmi.MENU_ITEM_CODE
                    LEFT JOIN M_OUTLET_PRICE mop ON mop.PRICE_TYPE_CODE = mp.PRICE_TYPE_CODE  
                    LEFT JOIN M_GLOBAL mg ON mg.COND = 'GROUP' AND mg.CODE = mmi.MENU_GROUP_CODE
                    LEFT JOIN M_GLOBAL mg3 ON mg3.DESCRIPTION LIKE '%' || mg.DESCRIPTION || '%' || mi.ITEM_DESCRIPTION || '%'
                    LEFT JOIN M_MENU_GROUP_LIMIT mmgl ON mmgl.MENU_GROUP_CODE = :menuGroupCode AND mmgl.ORDER_TYPE = 'ETA'
                    WHERE mmi.MENU_GROUP_CODE = :menuGroupCode
                    AND mmi.OUTLET_CODE LIKE :outletCode  
                    AND mop.OUTLET_CODE LIKE :outletCode  
                    AND mop.ORDER_TYPE = 'ETA' 
                    AND mmi.status = 'A'
                    ORDER BY mg3.CODE
                  """;
            prm.put("menuGroupCode", ref.get("menuGroupCode"));
        }

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("seq", rs.getString("seq"));
                rt.put("status", rs.getString("status"));
                rt.put("menuGroupCode", rs.getString("menu_group_code"));
                rt.put("menuGroup", rs.getString("menu_group"));
                return rt;
            }
        });
        return list;

    }

    @Override
    public List<Map<String, Object>> listPrice(Map<String, String> ref) {
        String qry = "SELECT  "
                + "    G1.CODE AS ITEM_CODE, "
                + "    G1.DESCRIPTION AS ITEM_NAME, "
                + "    MP.PRICE AS PRICE, "
                + "    MP.PRICE_TYPE_CODE AS PRICE_TYPE_CODE, "
                + "    G2.DESCRIPTION AS ORDER_DESCRIPTION "
                + "FROM M_GLOBAL G1 "
                + "LEFT JOIN M_PRICE MP "
                + "ON G1.CODE = MP.MENU_ITEM_CODE "
                + "LEFT JOIN M_OUTLET_PRICE OP "
                + "ON OP.PRICE_TYPE_CODE = MP.PRICE_TYPE_CODE AND OP.OUTLET_CODE LIKE '%%' "
                + "LEFT JOIN M_GLOBAL G2 "
                + "ON OP.ORDER_TYPE = G2.CODE AND G2.COND = 'ORDER_TYPE' "
                + "WHERE G1.COND = 'ITEM' AND G1.STATUS = 'A' AND MP.PRICE_TYPE_CODE IN (SELECT PRICE_TYPE_CODE FROM M_OUTLET_PRICE) "
                + "ORDER BY G1.CODE";
        Map prm = new HashMap();
        prm.put("item_code", ref.get("item_code"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemcode", rs.getString("item_code"));
                rt.put("itemname", rs.getString("item_name"));
                rt.put("price", rs.getString("price"));
                rt.put("pricetypecode", rs.getString("price_type_code"));
                rt.put("oderdescription", rs.getString("order_description"));
                return rt;
            }
        });
        return list;

    }

    @Override
    public List<Map<String, Object>> listItemPrice(Map<String, String> ref) {
        List listCode = new ArrayList();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("menuGroupCode", ref.get("menuGroupCode"));
        params.addValue("outletCode", ref.get("outletCode"));
        String qry = """
                        SELECT 
                                mmi.MENU_ITEM_CODE,
                                mmi.PLU,
                                mi.ITEM_DESCRIPTION,
                                mmi.MENU_GROUP_CODE,
                                mg.DESCRIPTION AS MENU_GROUP_NAME,
                                mmi.TAXABLE,
                                mmi.STATUS AS menu_group_status,
                                mp.PRICE,
                                mp.PRICE_TYPE_CODE
                        FROM M_MENU_ITEM mmi 
                        LEFT JOIN M_ITEM mi ON mi.ITEM_CODE = mmi.MENU_ITEM_CODE
                        LEFT JOIN M_PRICE mp ON mp.MENU_ITEM_CODE = mmi.MENU_ITEM_CODE
                        LEFT JOIN M_OUTLET_PRICE mop ON mop.PRICE_TYPE_CODE = mp.PRICE_TYPE_CODE  
                        LEFT JOIN M_GLOBAL mg ON mg.COND = 'GROUP' AND mg.CODE = mmi.MENU_GROUP_CODE
                        WHERE mmi.MENU_ITEM_CODE IS NOT NULL 
                     """;
        if(ref.get("menuGroupCode") == null || ref.get("menuGroupCode").isEmpty()){
            System.err.println("allItems");
            String querySub = "SELECT code from m_global WHERE cond ='GROUP' AND code LIKE 'D%'";
            listCode = jdbcTemplate.query(querySub, params, new DynamicRowMapper());
            if (listCode != null && !listCode.isEmpty()) {
                StringJoiner joiner = new StringJoiner(",", " (", ") ");
                for (Object code : listCode) {
                    joiner.add("'" + code.toString().replaceAll("\\{code=([A-Z0-9]+)\\}", "$1") + "'");
                }
                qry += " AND mmi.MENU_GROUP_CODE IN " + joiner.toString();
            }
        } else if (ref.containsKey("menuGroupCode") && (ref.get("menuGroupCode").equalsIgnoreCase("G90") || ref.get("menuGroupCode").equalsIgnoreCase("G91"))){
            qry += " AND mmi.MENU_GROUP_CODE = :menuGroupCode ";
        } else if (ref.get("menuGroupCode").startsWith("G")){
            System.err.println("by Group G");
            String querySub = """
                                    SELECT mg3.CODE
                                    FROM M_MENU_ITEM mmi 
                                    LEFT JOIN M_ITEM mi ON mi.ITEM_CODE = mmi.MENU_ITEM_CODE
                                    LEFT JOIN M_PRICE mp ON mp.MENU_ITEM_CODE = mmi.MENU_ITEM_CODE
                                    LEFT JOIN M_OUTLET_PRICE mop ON mop.PRICE_TYPE_CODE = mp.PRICE_TYPE_CODE  
                                    LEFT JOIN M_GLOBAL mg ON mg.COND = 'GROUP' AND mg.CODE = mmi.MENU_GROUP_CODE
                                    LEFT JOIN M_GLOBAL mg3 ON mg3.DESCRIPTION LIKE '%' || mg.DESCRIPTION || '%' || mi.ITEM_DESCRIPTION || '%'
                                    LEFT JOIN M_MENU_GROUP_LIMIT mmgl ON mmgl.MENU_GROUP_CODE = :menuGroupCode AND mmgl.ORDER_TYPE = 'ETA'
                                    WHERE mmi.MENU_GROUP_CODE = :menuGroupCode
                                    AND mmi.OUTLET_CODE = :outletCode  
                                    AND mop.OUTLET_CODE = :outletCode  
                                    AND mop.ORDER_TYPE = 'ETA' 
                                    AND mmi.STATUS = 'A'
                              """;
            listCode = jdbcTemplate.query(querySub, params, new DynamicRowMapper());
            if (listCode != null && !listCode.isEmpty()) {
                StringJoiner joiner = new StringJoiner(",", " (", ") ");
                for (Object code : listCode) {
                    joiner.add("'" + code.toString().replaceAll("\\{code=([A-Z0-9]+)\\}", "$1") + "'");
                }
                qry += " AND mmi.MENU_GROUP_CODE IN " + joiner.toString();
            }
        } else {
            System.err.println("by Group D");
            qry += " AND mmi.MENU_GROUP_CODE = :menuGroupCode ";
        }
        qry += """
                AND mmi.OUTLET_CODE = :outletCode 
                AND mop.OUTLET_CODE = :outletCode 
                AND mop.ORDER_TYPE = 'ETA' 
                AND mmi.STATUS = 'A'
                ORDER BY mmi.MENU_ITEM_CODE
            """;

        System.err.println("prm :" + params);
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, params, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("menuItemCode", rs.getString("menu_item_code"));
                rt.put("itemDescription", rs.getString("item_description"));
                rt.put("menuGroupName", rs.getString("menu_group_name"));
                rt.put("menuGroupStatus", rs.getString("menu_group_status"));
                rt.put("price", rs.getString("price"));
                rt.put("priceTypeCode", rs.getString("price_type_code"));
                rt.put("taxable", rs.getString("taxable"));
                rt.put("menuGroupCode", rs.getString("menu_group_code"));
                rt.put("plu", rs.getString("plu"));
                return rt;
            }
        });
        return list;

    }

    @Override
    public List<Map<String, Object>> listItemDetail(Map<String, String> balance) {
        String qry = "  SELECT   MMI.MENU_ITEM_CODE, "
                + "           MI.ITEM_DESCRIPTION, "
                + "           MMI.MENU_GROUP_CODE,MG2.DESCRIPTION AS MENU_GROUP_NAME, "
                + "           MP.PRICE, "
                + "           MP.PRICE_TYPE_CODE AS PRICE_TYPE_CODE, "
                + "           MG.DESCRIPTION AS ORDER_DESCRIPTION, "
                + "           CASE "
                + "              WHEN     MENU_SET = 'N' "
                + "                   AND MODIFIER_GROUP1_CODE = ' ' "
                + "                   AND MODIFIER_GROUP2_CODE = ' ' "
                + "                   AND MODIFIER_GROUP3_CODE = ' ' "
                + "                   AND MODIFIER_GROUP4_CODE = ' ' "
                + "              THEN "
                + "                 'N' "
                + "              WHEN     MENU_SET = 'Y' "
                + "                   AND MODIFIER_GROUP1_CODE = ' ' "
                + "                   AND MODIFIER_GROUP2_CODE = ' ' "
                + "                   AND MODIFIER_GROUP3_CODE = ' ' "
                + "                   AND MODIFIER_GROUP4_CODE = ' ' "
                + "              THEN "
                + "                 'N' "
                + "              ELSE "
                + "                 'Y' "
                + "           END "
                + "              AS MODIFIER_STATUS "
                + "    FROM                  M_MENU_ITEM MMI "
                + "                       LEFT JOIN "
                + "                          M_ITEM MI "
                + "                       ON MMI.MENU_ITEM_CODE = MI.ITEM_CODE "
                + "                    LEFT JOIN "
                + "                       M_PRICE MP "
                + "                    ON MMI.MENU_ITEM_CODE = MP.MENU_ITEM_CODE "
                + "                 LEFT JOIN "
                + "                    M_OUTLET_PRICE MOP "
                + "                 ON MOP.PRICE_TYPE_CODE = MP.PRICE_TYPE_CODE "
                + "              LEFT JOIN "
                + "                 M_GLOBAL MG "
                + "              ON MOP.ORDER_TYPE = MG.CODE AND MG.COND = 'ORDER_TYPE' "
                + "           LEFT JOIN "
                + "              M_GLOBAL MG2 "
                + "           ON MMI.MENU_GROUP_CODE = MG2.CODE AND MG2.COND = 'GROUP' "
                + "   WHERE       MMI.MENU_GROUP_CODE = :menuGroupCode "
                + "           AND MMI.MENU_ITEM_CODE = :menuItemCode "
                + "   AND MMI.STATUS = 'A' "
                + "           AND MI.STATUS = 'A' "
                + "           AND MMI.OUTLET_CODE = :outletCode "
                + "           AND MOP.OUTLET_CODE = :outletCode "
                + "ORDER BY   MMI.MENU_ITEM_CODE";
        Map prm = new HashMap();
        prm.put("outletCode", balance.get("outletCode"));
        prm.put("menuGroupCode", balance.get("menuGroupCode"));
        prm.put("menuItemCode", balance.get("menuItemCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("menuItemCode", rs.getString("MENU_ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("menuGroupCode", rs.getString("MENU_GROUP_CODE"));
                rt.put("menuGroupName", rs.getString("MENU_GROUP_NAME"));
                rt.put("price", rs.getString("PRICE"));
                rt.put("priceTypeCode", rs.getString("PRICE_TYPE_CODE"));
                rt.put("orderDescription", rs.getString("ORDER_DESCRIPTION"));
                rt.put("modifierStatus", rs.getString("MODIFIER_STATUS"));
                return rt;
            }
        });
        return list;

    }

    @Override
    public List<Map<String, Object>> listModifier(Map<String, String> ref) {
        String qry = "SELECT  "
                + "    MMI.MENU_ITEM_CODE,  "
                + "    MI.ITEM_DESCRIPTION,  "
                + "    MOD.MODIFIER_ITEM_CODE,  "
                + "    MI2.ITEM_DESCRIPTION AS MODIFIER_ITEM_NAME  "
                + "FROM M_MENU_ITEM MMI  "
                + "LEFT JOIN M_ITEM MI  "
                + "ON MMI.MENU_ITEM_CODE = MI.ITEM_CODE  "
                + "LEFT JOIN M_MODIFIER_ITEM MOD  "
                + "ON MMI.MODIFIER_GROUP1_CODE = MOD.MODIFIER_GROUP_CODE  "
                + "LEFT JOIN M_ITEM MI2  "
                + "ON MOD.MODIFIER_ITEM_CODE = MI2.ITEM_CODE  "
                + "WHERE MMI.MENU_GROUP_CODE LIKE :menuGroupCode  "
                + "AND MMI.MENU_ITEM_CODE = :menuItemCode  "
                + "AND MMI.STATUS = 'A'  "
                + "AND MI.STATUS = 'A'  "
                + "AND MMI.OUTLET_CODE LIKE :outletCode  "
                + "ORDER BY MMI.MENU_ITEM_CODE";
        Map prm = new HashMap();
        prm.put("outletCode", "%" + ref.get("outletCode") + "%");
        prm.put("menuGroupCode", "%" + ref.get("menuGroupCode") + "%");
        prm.put("menuItemCode", ref.get("menuItemCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("menuItemCode", rs.getString("menu_item_code"));
                rt.put("itemDescription", rs.getString("item_description"));
                rt.put("modifierItemCode", rs.getString("modifier_item_code"));
                rt.put("modifierItemName", rs.getString("modifier_item_name"));

                return rt;
            }
        });
        return list;

    }

    @Override
    public List<Map<String, Object>> listSpecialPrice(Map<String, String> ref) {
        String qry = "SELECT   "
                + "    MSP.MENU_ITEM_CODE,  "
                + "    MG.DESCRIPTION,  "
                + "    MSP.DATE_START,  "
                + "    MSP.DATE_END,  "
                + "    MSP.TIME_START,  "
                + "    MSP.TIME_END,  "
                + "    MSP.OUTLET_CODE  "
                + "FROM M_OUTLET_SPECIAL_PRICE MSP  "
                + "JOIN M_GLOBAL MG  "
                + "ON MSP.MENU_ITEM_CODE = MG.CODE AND MG.COND = 'ITEM'  "
                + "WHERE DATE_START BETWEEN TRUNC(SYSDATE, 'MM') AND LAST_DAY(SYSDATE)  "
                + "AND DATE_END BETWEEN TRUNC(SYSDATE, 'MM') AND LAST_DAY(SYSDATE)  "
                + "AND MSP.OUTLET_CODE IN (SELECT OUTLET_CODE FROM M_OUTLET_PROFILE WHERE DEFAULT_SITE = 'YES')";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("menuitemcode", rs.getString("menu_item_code"));
                rt.put("description", rs.getString("description"));
                rt.put("datestart", rs.getString("date_start"));
                rt.put("dateend", rs.getString("date_end"));
                rt.put("outletcode", rs.getString("outlet_code"));

                return rt;
            }
        });
        return list;

    }
//////////////done
///////////////new method from dona 14-03-2023////////////////////////////

    @Override
    public List<Map<String, Object>> listMasterCity(Map<String, String> logan) {
        String qry = "select code,description from m_global where cond='CITY' AND STATUS='A'";
        Map prm = new HashMap();
        System.err.println("q :" + qry);

        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("code", rs.getString("CODE"));
                rt.put("description", rs.getString("DESCRIPTION"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listPosition(Map<String, String> logan) {
        String qry = "select code,description from m_global where cond='POSITION' AND STATUS='A'";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("code", rs.getString("CODE"));
                rt.put("description", rs.getString("DESCRIPTION"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listMpcsHeader(Map<String, String> param) {
        String qry = "select * from m_mpcs_header WHERE OUTLET_CODE= :outlet";
        Map prm = new HashMap();
        prm.put("outlet", param.get("outlet"));
        if (!param.get("date").isEmpty()) {
            prm.put("date", param.get("date"));
            qry += " AND MPCS_GROUP IN ( SELECT DISTINCT(MPCS_GROUP) FROM T_SUMM_MPCS WHERE OUTLET_CODE = :outlet AND DATE_MPCS = :date)";
        }
        var statusParam = param.get("status");
        if ("A".equals(statusParam)) {
            qry += " AND STATUS = 'A' ";
        }
        if ("I".equals(statusParam)) {
            qry += " AND STATUS = 'I' ";
        } else {
            qry += " AND STATUS IN ('A', 'I' )";
        }

        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("outletCode", rs.getString("Outlet_Code"));
                rt.put("description", rs.getString("DESCRIPTION"));
                rt.put("mpcsGroup", rs.getString("MPCS_GROUP"));
                rt.put("fryerType", rs.getString("FRYER_TYPE"));
                rt.put("qtyConv", rs.getString("QTY_CONV"));
                rt.put("status", rs.getString("STATUS"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listMasterItemSupplier(Map<String, String> logan) {
        String qry = "select ITEM_CODE||'-'||ITEM_DESCRIPTION as name,item_description,"
                + " item_code from m_item where Status='A' and FLAG_MATERIAL='Y' AND FLAG_STOCK='Y'";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("name", rs.getString("name"));
                return rt;
            }
        });
        return list;
    }

    //////////////done
    ///////////////new method from asep 29-mar-2023 //////////////      
    @Override
    public List<Map<String, Object>> listOutlet(Map<String, String> balance) {
        String qry = "SELECT a.region_code,b.description region_name,a.outlet_code,a.area_code,c.description area_name, a.initial_outlet, a.outlet_name, a.type,d.description type_store, a.status "
                + "FROM M_OUTLET a "
                + "join m_global b on a.region_code=b.code and b.cond='REG_OUTLET'"
                + "join m_global c on a.area_code=c.code  and c.cond='AREACODE'"
                + "join m_global d on a.type=d.code  and d.cond='OUTLET_TP'"
                + "where a.type <>'HO' and a.status='A' and  a.REGION_CODE LIKE :region AND a.AREA_CODE LIKE :area AND a.TYPE LIKE :type";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        prm.put("region", "%" + balance.get("region") + "%");
        prm.put("area", "%" + balance.get("area") + "%");
        prm.put("type", "%" + balance.get("type") + "%");
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("region", rs.getString("region_code"));
                rt.put("regionname", rs.getString("region_name"));
                rt.put("area", rs.getString("area_code"));
                rt.put("areaname", rs.getString("area_name"));
                rt.put("outlet", rs.getString("outlet_code"));
                rt.put("Initial", rs.getString("initial_outlet"));
                rt.put("Name", rs.getString("outlet_name"));
                rt.put("Type", rs.getString("type"));
                rt.put("typename", rs.getString("type_store"));
                rt.put("Status", rs.getString("status"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listPos(Map<String, String> Logan) {
        String qry = "SELECT region_code,outlet_code,pos_code,pos_description,ref_no,a.status,pos_type,description FROM M_pos a join m_global b on b.code=a.pos_type  where outlet_code= :outletcode and cond='POS_TYPE'";
        Map prm = new HashMap();
        prm.put("outletcode", Logan.get("OutletCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("Region", rs.getString("region_code"));
                rt.put("Outlet", rs.getString("outlet_code"));
                rt.put("Pos", rs.getString("pos_code"));
                rt.put("Posdesc", rs.getString("pos_description"));
                rt.put("Ref", rs.getString("ref_no"));
                rt.put("Status", rs.getString("status"));
                rt.put("PosType", rs.getString("pos_type"));
                rt.put("Description", rs.getString("description"));
                return rt;
            }
        });
        return list;
    }

    // list type pos
    @Override
    public List<Map<String, Object>> listTypePos(Map<String, String> Logan) {
        String qry = "select * from m_global where cond='POS_TYPE' and status= :Status";
        Map prm = new HashMap();
        prm.put("Status", Logan.get("status"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("code", rs.getString("code"));
                rt.put("description", rs.getString("description"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listItem(Map<String, String> Logan) {
        String qry = "select * from m_item where status='A' and flag_paket= :FlagPaket  and item_code not like'88-%' order by item_code asc";
        if (Logan.get("paket").equalsIgnoreCase("C")) {
            qry = "select * from m_item where status='A' and flag_material= 'Y'  and item_code not like'88-%' order by item_code asc";
        }
        if (Logan.get("paket").equalsIgnoreCase("D")) {
            qry = "select * from m_item where status='A' and item_code like'88-%' order by item_code asc";
        }
        ///////////////////////// untuk report stock/////////////////////////
        if (Logan.get("paket").equalsIgnoreCase("A")) {
            qry = "SELECT * FROM M_ITEM WHERE FLAG_STOCK = 'Y' ORDER BY ITEM_CODE ASC";
        }
        //W for Wastage
        if (Logan.get("paket").equalsIgnoreCase("W")) {
            qry = "SELECT * FROM M_ITEM WHERE FLAG_STOCK = 'Y' AND FLAG_MATERIAL = 'Y' AND STATUS = 'A' ORDER BY ITEM_CODE ASC";
        }
        /////////////// Revised query for Leftover - Fathur 21-nov-2023 ////////////// 
        if (Logan.get("paket").equalsIgnoreCase("L")) {
            /*qry = "SELECT * FROM M_ITEM "
                    + "WHERE (STATUS = 'A' AND FLAG_STOCK = 'Y' AND FLAG_PAKET = 'N') "
                    + "and CD_ITEM_LEFTOVER in (SELECT CODE FROM M_GLOBAL WHERE COND = 'LEFTOVER') "
                    + "order by ITEM_CODE ASC";*/
            qry = "SELECT * FROM M_ITEM WHERE STATUS = 'A' AND FLAG_STOCK = 'Y' AND FLAG_PAKET = 'N'"
                    + " AND CD_ITEM_LEFTOVER IS NOT NULL AND CD_ITEM_LEFTOVER != ' '";
        }
        /////////////// End revised query for Leftover//////////////
        //Condition when Non-Paket
        if (Logan.get("paket").equalsIgnoreCase("N")) {
            qry = "select * from m_item where status='A' and flag_paket='N' and flag_finished_good = 'Y' order by item_code asc";
        } 
        Map prm = new HashMap();
        prm.put("FlagPaket", Logan.get("paket"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("code", rs.getString("item_code"));
                rt.put("description", rs.getString("item_description"));
                rt.put("besar", rs.getString("uom_warehouse"));
                rt.put("kecil", rs.getString("uom_stock"));
                rt.put("status", rs.getString("status"));
                rt.put("leftover", rs.getString("cd_item_leftover"));
                return rt;
            }
        });
        return list;
    }
//done

    ///////////////new method from kevin 16-mar-2023 ////////////// 
    @Override
    public List<Map<String, Object>> listItemMenus(Map<String, String> ref) {
        String qry = "select distinct mg.description, mmi.plu, "
                + "mmi.seq, "
                + "mmi.status "
                + "from m_menu_item mmi join m_global mg on mmi.menu_item_code = mg.code "
                + "join m_color mc on mmi.color_code = mc.color_code "
                + "join m_outlet_price mop on mmi.outlet_code = mop.outlet_code "
                + "join m_price mp on mmi.menu_item_code = mp.menu_item_code and mop.price_type_code = mp.price_type_code "
                + "join m_menu_item_limit mmil on mmi.outlet_code = mmil.outlet_code and mmi.menu_item_code = mmil.menu_item_code "
                + "left join m_item_coffee mic on mic.menu_item_code = mmi.menu_item_code "
                + "where mg.cond = 'ITEM' "
                + "and mmi.menu_group_code = :groupCode "
                + "order by mmi.seq, mmi.plu";
        Map prm = new HashMap();
        prm.put("groupCode", ref.get("groupCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("menuPlu", rs.getString("plu"));
                rt.put("menuName", rs.getString("description"));
                rt.put("seq", rs.getInt("seq"));
                rt.put("status", rs.getString("status"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listMenuGroups(Map<String, String> ref) {
        String qry = "select distinct mmg.menu_group_code, mg.description, mmg.seq, mc.r_value, mc.g_value, mc.b_value, mmg.status "
                + "from m_menu_group mmg "
                + "join m_global mg on mmg.menu_group_code = mg.code "
                + "join m_color mc on mmg.color_code = mc.color_code "
                + "left join m_menu_group_limit mmgl on mmg.outlet_code = mmgl.outlet_code and mmg.menu_group_code = mmgl.menu_group_code "
                + "where mg.cond = 'GROUP' "
                + "order by mmg.seq, mmg.menu_group_code";
        Map prm = new HashMap();
//        prm.put("status", ref.get("status"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("groupCode", rs.getString("menu_group_code"));
                rt.put("seq", rs.getInt("seq"));
                rt.put("groupName", rs.getString("description"));
                rt.put("status", rs.getString("status"));
                return rt;
            }
        });
        return list;
    }
    ///////////done

    ///////////////new method from kevin 24-mar-2023 //////////////
    @Override
    public List<Map<String, Object>> listRecipeHeader(Map<String, String> ref) {
        String qry = "select distinct rh.recipe_code, rh.recipe_remark, rh.mpcs_group, mh.description, rh.status "
                + "from m_recipe_header rh "
                + "join m_mpcs_header mh on mh.mpcs_group = rh.mpcs_group "
                + "order by rh.status, rh.recipe_code";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("reCode", rs.getString("recipe_code"));
                rt.put("reMark", rs.getString("recipe_remark"));
                rt.put("mpGrp", rs.getString("mpcs_group"));
                rt.put("description", rs.getString("description"));
                rt.put("status", rs.getString("status"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listRecipeDetail(Map<String, String> ref) {
        String qry = "select rd.recipe_code, rd.item_code, i.item_description, rd.qty_stock, rd.uom_stock "
                + "from m_recipe_detail rd "
                + "join m_item i on i.item_code = rd.item_code "
                + "where rd.recipe_code = :reCode ";
        Map prm = new HashMap();
        prm.put("reCode", ref.get("reCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("reCode", rs.getString("recipe_code"));
                rt.put("itemCode", rs.getString("item_code"));
                rt.put("description", rs.getString("item_description"));
                rt.put("qty", rs.getString("qty_stock"));
                rt.put("uom", rs.getString("uom_stock"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listRecipeProduct(Map<String, String> ref) {
        String qry = "SELECT rp.RECIPE_CODE, rp.PRODUCT_CODE, CASE WHEN rp.PRODUCT_REMARK IS NOT NULL AND rp.PRODUCT_REMARK <> ' ' THEN rp.PRODUCT_REMARK ELSE mi.ITEM_DESCRIPTION END AS PRODUCT_REMARK, rp.QTY_STOCK, rp.UOM_STOCK FROM M_RECIPE_PRODUCT rp LEFT JOIN M_ITEM mi ON mi.ITEM_CODE = rp.PRODUCT_CODE WHERE rp.RECIPE_CODE = :reCode";
        Map prm = new HashMap();
        prm.put("reCode", ref.get("reCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("reCode", rs.getString("recipe_code"));
                rt.put("proCode", rs.getString("product_code"));
                rt.put("description", rs.getString("product_remark"));
                rt.put("qty", rs.getString("qty_stock"));
                rt.put("uom", rs.getString("uom_stock"));
                return rt;
            }
        });
        return list;
    }
    ///////////done
    ///////////////Updated By Pandu 14-03-2023-- Update By Lani 31 March 2023
    // ========================================================== MODULE MASTER STAFF (M_STAFF) ==========================================================================================//  
    //VIEW USER STAFF DATA MASTER STAFF (M_STAFF)

    @Override
    public List<Map<String, Object>> listStaff(Map<String, String> ref) {
        String qry = "SELECT DISTINCT VS.REGIONAL_DESC,VS.OUTLET_NAME,VS.AREA_DESC,VS.AREA_CODE,G.DESCRIPTION CITY_STAFF, "
                + "G1.DESCRIPTION POSITION_NAME,G2.DESCRIPTION ACCESS_NAME, "
                + "S.*,PS.STAFF_POS_CODE,PS.PASSWORD AS PASS_POS_CODE, PS.STATUS AS STATUS_POS,s.access_level ACCESS_LEVEL FROM M_STAFF S "
                + "JOIN V_STRUCTURE_STORE VS ON VS.OUTLET_CODE = S.OUTLET_CODE  "
                + "LEFT JOIN M_POS_STAFF PS ON PS.STAFF_CODE = S.STAFF_CODE  "
                + "LEFT JOIN M_GLOBAL G ON G.CODE = S.CITY AND G.COND = 'CITY' "
                + "LEFT JOIN M_GLOBAL G1 ON G1.CODE = S.POSITION AND G1.COND = 'POSITION' "
                + "LEFT JOIN M_GLOBAL G2 ON G2.CODE = S.ACCESS_LEVEL AND G2.COND = 'ACCESS' "
                + "WHERE S.OUTLET_CODE = :outletCode AND S.STATUS LIKE :status AND S.POSITION LIKE :position";
        Map prm = new HashMap();

        // PARAMETER YG DIGUNAKAN SETELAH WHERE DIDALAM QUERY //
        prm.put("outletCode", ref.get("outletCode"));
        prm.put("status", "%" + ref.get("status") + "%");
        prm.put("position", "%" + ref.get("position") + "%");

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();

                // PARAMETER YG DIGUNAKAN UNTUK MENAMPILKAN VALUE YANG ADA DIDALAM FIELD(KOLOM) SAAT MENGGUNAKAN QUERY //
                rt.put("regionCode", rs.getString("REGION_CODE"));
                rt.put("regionName", rs.getString("REGIONAL_DESC"));
                rt.put("outletName", rs.getString("OUTLET_NAME"));
                rt.put("areaName", rs.getString("AREA_DESC"));
                rt.put("areaCode", rs.getString("AREA_CODE"));
                rt.put("cityStaff", rs.getString("CITY_STAFF"));
                rt.put("positionName", rs.getString("POSITION_NAME"));
                rt.put("accessName", rs.getString("ACCESS_NAME"));
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("staffCode", rs.getString("STAFF_CODE"));
                rt.put("staffName", rs.getString("STAFF_NAME"));
                rt.put("staffFullName", rs.getString("STAFF_FULL_NAME"));
                rt.put("passwordCode", rs.getString("PASSWORD"));
                rt.put("idCard", rs.getString("ID_CARD"));
                rt.put("sexType", rs.getString("SEX"));
                rt.put("dateOfBirth", rs.getString("DATE_OF_BIRTH"));
                rt.put("address1", rs.getString("ADDRESS_1"));
                rt.put("address2", rs.getString("ADDRESS_2"));
                rt.put("cityCode", rs.getString("CITY"));
                rt.put("phoneNumber", rs.getString("PHONE_NO"));
                rt.put("mobilePhoneNumber", rs.getString("MOBILE_PHONE_NO"));
                rt.put("employDate", rs.getString("EMPLOY_DATE"));
                rt.put("resignDate", rs.getString("RESIGN_DATE"));
                rt.put("positionCode", rs.getString("POSITION"));
                rt.put("accessLevel", rs.getString("ACCESS_LEVEL"));
                rt.put("riderFlag", rs.getString("RIDER_FLAG"));
                rt.put("groupId", rs.getString("GROUP_ID"));
                rt.put("statusName", rs.getString("STATUS"));
                rt.put("statusPos", rs.getString("STATUS_POS"));
                rt.put("staffPosCode", rs.getString("STAFF_POS_CODE"));
                rt.put("passPosCode", rs.getString("PASS_POS_CODE"));

                //    rt.put("staffName", rs.getString("STAFF_NAME"));               
                return rt;
            }
        });
        return list;
    }
    // ========================================================== MODULE MASTER GLOBAL (M_GLOBAL) ==========================================================================================//  
    //VIEW REGION DATA MASTER GLOBAL (M_GLOBAL)

    @Override
    public List<Map<String, Object>> listRegion(Map<String, String> ref) {
        String qry = "SELECT COND,CODE,DESCRIPTION,STATUS FROM M_GLOBAL WHERE COND ='REG_OUTLET' ORDER BY CODE ASC";
        Map prm = new HashMap();

        // PARAMETER YG DIGUNAKAN SETELAH WHERE DIDALAM QUERY //
        prm.put("condCode", ref.get("condCode"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();

                // PARAMETER YG DIGUNAKAN UNTUK MENAMPILKAN VALUE YANG ADA DIDALAM FIELD(KOLOM) SAAT MENGGUNAKAN QUERY //
                rt.put("regionCode", rs.getString("CODE"));
                rt.put("regionName", rs.getString("DESCRIPTION"));
                rt.put("statusName", rs.getString("STATUS"));
                rt.put("condCode", rs.getString("COND"));
                return rt;
            }
        });
        return list;
    }

    // ========================================================== MODULE MASTER GLOBAL (M_GLOBAL) ==========================================================================================//  
    //VIEW USER OUTLET DATA MASTER GLOBAL (M_GLOBAL)
    @Override
    public List<Map<String, Object>> listOutlets(Map<String, String> ref) {
        // String qry = "SELECT DISTINCT REGION_CODE FROM M_OUTLET WHERE type =:typeCode order by region_code asc"; 
        //String qry = "SELECT COND,CODE,DESCRIPTION,STATUS FROM M_GLOBAL WHERE COND =:condCode ORDER BY CODE ASC"; 
        String qry = "SELECT OUTLET_CODE, OUTLET_NAME, STATUS, TYPE FROM M_OUTLET WHERE TYPE = 'MS' ORDER BY OUTLET_NAME ASC";
        Map prm = new HashMap();

        // PARAMETER YG DIGUNAKAN SETELAH WHERE DIDALAM QUERY //
        prm.put("typeCode", ref.get("typeCode"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();

                // PARAMETER YG DIGUNAKAN UNTUK MENAMPILKAN VALUE YANG ADA DIDALAM FIELD(KOLOM) SAAT MENGGUNAKAN QUERY //
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("outletName", rs.getString("OUTLET_NAME"));
                rt.put("statusName", rs.getString("STATUS"));
                rt.put("typeCode", rs.getString("TYPE"));
                return rt;
            }
        });
        return list;
    }

    // ========================================================== MODULE MASTER GLOBAL (M_GLOBAL) ==========================================================================================//  
    //VIEW USER staff DATA MASTER GLOBAL (M_GLOBAL)
    public List<Map<String, Object>> listViewFormStaff(Map<String, String> ref) {
        String qry = "SELECT DISTINCT * FROM M_STAFF WHERE OUTLET_CODE =:outletCode AND STAFF_CODE =:staffCode";
        Map prm = new HashMap();

        // PARAMETER YG DIGUNAKAN SETELAH WHERE DIDALAM QUERY //
        prm.put("outletCode", ref.get("outletCode"));
        prm.put("staffCode", ref.get("staffCode"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();

                // PARAMETER YG DIGUNAKAN UNTUK MENAMPILKAN VALUE YANG ADA DIDALAM FIELD(KOLOM) SAAT MENGGUNAKAN QUERY //
                rt.put("regionCode", rs.getString("REGION_CODE"));
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("staffCode", rs.getString("STAFF_CODE"));
                rt.put("staffName", rs.getString("STAFF_NAME"));
                rt.put("staffFullName", rs.getString("STAFF_FULL_NAME"));
                return rt;
            }
        });
        return list;
    }

    //VIEW ACCESS LEVEL DATA MASTER GLOBAL (M_GLOBAL)
    public List<Map<String, Object>> listViewAccessLevel(Map<String, String> ref) {
        String qry = "SELECT DISTINCT COND,CODE,DESCRIPTION,STATUS FROM M_GLOBAL WHERE COND =:condName AND CODE NOT IN('SRG')";
        Map prm = new HashMap();

        // PARAMETER YG DIGUNAKAN SETELAH WHERE DIDALAM QUERY //
        prm.put("condName", ref.get("condName"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();

                // PARAMETER YG DIGUNAKAN UNTUK MENAMPILKAN VALUE YANG ADA DIDALAM FIELD(KOLOM) SAAT MENGGUNAKAN QUERY //
                rt.put("condName", rs.getString("COND"));
                rt.put("accesslevelCode", rs.getString("CODE"));
                rt.put("accesslevelName", rs.getString("DESCRIPTION"));
                rt.put("statusName", rs.getString("STATUS"));
                return rt;
            }
        });
        return list;
    }

    //VIEW POSITION DATA MASTER GLOBAL (M_GLOBAL)
    public List<Map<String, Object>> listViewPosition(Map<String, String> ref) {
        String qry = "SELECT DISTINCT COND,CODE,DESCRIPTION,STATUS FROM M_GLOBAL WHERE COND =:condName AND CODE NOT IN('SRG','3591','3590')";
        Map prm = new HashMap();

        // PARAMETER YG DIGUNAKAN SETELAH WHERE DIDALAM QUERY //
        prm.put("condName", ref.get("condName"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();

                // PARAMETER YG DIGUNAKAN UNTUK MENAMPILKAN VALUE YANG ADA DIDALAM FIELD(KOLOM) SAAT MENGGUNAKAN QUERY //
                rt.put("condName", rs.getString("COND"));
                rt.put("accesslevelCode", rs.getString("CODE"));
                rt.put("accesslevelName", rs.getString("DESCRIPTION"));
                rt.put("statusName", rs.getString("STATUS"));
                return rt;
            }
        });
        return list;
    }
    //VIEW GROUP USER DATA MASTER MENU GROUP (M_MENUGRP)

    public List<Map<String, Object>> listViewGroupUser(Map<String, String> ref) {
        String qry = "SELECT DISTINCT GROUP_ID,TYPE_MENU FROM M_MENUGRP WHERE TYPE_MENU = 'MENU'";
        Map prm = new HashMap();

        // PARAMETER YG DIGUNAKAN SETELAH WHERE DIDALAM QUERY //
        prm.put("typemenuName", ref.get("typemenuName"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();

                // PARAMETER YG DIGUNAKAN UNTUK MENAMPILKAN VALUE YANG ADA DIDALAM FIELD(KOLOM) SAAT MENGGUNAKAN QUERY //
                rt.put("groupidName", rs.getString("GROUP_ID"));
                rt.put("typemenuName", rs.getString("TYPE_MENU"));
                return rt;
            }
        });
        return list;
    }

    ///////////////////done    
    //////////////////new method by Lani 29-03-2023//////////////////////////
    @Override
    public List<Map<String, Object>> listSalesRecipe(Map<String, String> ref) {
        String qry = "SELECT A.PLU_CODE,d.description,A.ITEM_CODE,B.ITEM_DESCRIPTION,A.QTY_EI,A.QTY_TA,A.UOM_STOCK, A.user_upd, A.date_upd, A.time_upd  "
                + "FROM M_SALES_RECIPE A "
                + "JOIN M_ITEM B ON a.item_code=b.item_code "
                + "join m_menu_item c on a.plu_code=c.menu_Item_code "
                + "join m_global d on a.plu_code=d.code "
                + "WHERE c.status='A' and d.cond='ITEM' AND d.status='A' and c.outlet_code=:outletCode and A.PLU_CODE = :pluCode  "
                + "GROUP BY c.menu_group_code,A.PLU_CODE,d.description,A.ITEM_CODE,B.ITEM_DESCRIPTION,A.QTY_EI,A.QTY_TA,A.UOM_STOCK, A.user_upd, A.date_upd, A.time_upd "
                + "ORDER BY c.menu_group_code,A.PLU_CODE,d.description,A.ITEM_CODE,B.ITEM_DESCRIPTION,A.QTY_EI,A.QTY_TA,A.UOM_STOCK, A.user_upd, A.date_upd, A.time_upd ";
        Map prm = new HashMap();
        prm.put("outletCode", ref.get("outletCode"));
        prm.put("pluCode", ref.get("pluCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("pluCode", rs.getString("PLU_CODE"));
                rt.put("description", rs.getString("description"));
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("qtyEi", rs.getString("QTY_EI"));
                rt.put("qtyTa", rs.getString("QTY_TA"));
                rt.put("uomStock", rs.getString("UOM_STOCK"));
                rt.put("userUpd", rs.getString("user_upd"));
                rt.put("dateUpd", rs.getString("date_upd"));
                rt.put("timeUpd", rs.getString("time_upd"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listSalesRecipeHeader(Map<String, String> ref) {
        String qry = "SELECT A.PLU_CODE,d.description "
                + "FROM M_SALES_RECIPE A "
                + "JOIN M_ITEM B ON a.item_code=b.item_code "
                + "join m_menu_item c on a.plu_code=c.menu_Item_code "
                + "join m_global d on a.plu_code=d.code "
                + "WHERE c.status='A' and d.cond='ITEM' AND d.status='A' and c.outlet_code= :outletCode  "
                + "GROUP BY c.menu_group_code,A.PLU_CODE,d.description "
                + "ORDER BY c.menu_group_code,A.PLU_CODE,d.description ";
        Map prm = new HashMap();
        prm.put("outletCode", ref.get("outletCode"));
        //prm.put("pluCode", ref.get("pluCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("pluCode", rs.getString("PLU_CODE"));
                rt.put("description", rs.getString("description"));
                return rt;
            }
        });
        return list;
    }

    //////////Group Items by Kevin 29-03-2023
    @Override
    public List<Map<String, Object>> listMenuItem(Map<String, String> ref) {
        String qry = "select i.menu_item_code, g.description, i.status "
                + "from m_menu_item i "
                + "join m_global g on g.code = i.menu_item_code "
                + "where g.cond = 'ITEM' "
                + "and g.status = 'A' ";
        if (ref.get("status").equalsIgnoreCase("A")) {
            qry = qry + "and i.status = 'A' ";
        } else if (ref.get("status").equalsIgnoreCase("I")) {
            qry = qry + "and i.status = 'I' ";
        }
        qry = qry
                + "order by i.menu_item_code";
        Map prm = new HashMap();
        //prm.put("status", ref.get("status"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("menuCode", rs.getString("menu_item_code"));
                rt.put("menuName", rs.getString("description"));
                rt.put("status", rs.getString("status"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listGroupItem(Map<String, String> ref) {
        String qry = "SELECT A.ITEM_CODE,B.ITEM_DESCRIPTION, "
                + "A.QTY_stock,A.QTY_stock_e,A.QTY_stock_t,A.UOM_STOCK, A.status "
                + "FROM M_group_item A "
                + "JOIN M_ITEM B ON a.item_code=b.item_code "
                + "join m_menu_item c on a.group_item_code=c.menu_Item_code "
                + "join m_global d on a.group_item_code=d.code "
                + "where a.group_item_code = :groupCode "
                + "and a.status='A' and c.status='A' and d.cond='ITEM' AND d.status='A' "
                + "GROUP BY c.menu_group_code,A.group_item_code,d.description,A.ITEM_CODE,B.ITEM_DESCRIPTION,A.QTY_stock,A.QTY_stock_e,A.QTY_stock_T,A.UOM_STOCK,A.Status "
                + "union all "
                + "select distinct m.menu_set_item_code, g.description, m.qty, "
                + "0 qty_stock_e, 0 qty_stock_t, 'PCS' uom_stock, m.status "
                + "from m_menu_set m "
                + "join m_global g on g.code = m.menu_set_item_code "
                + "where g.cond = 'ITEM' and menu_set_code = :groupCode "
                //+ "ORDER BY c.menu_group_code,A.group_item_code,d.description,A.ITEM_CODE,B.ITEM_DESCRIPTION,A.QTY_stock,A.QTY_stock_e,A.QTY_stock_T,A.UOM_STOCK,A.Status";
                + "ORDER BY 1, 2, 3, 4, 5, 6, 7";
        Map prm = new HashMap();
        prm.put("groupCode", ref.get("groupCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDesc", rs.getString("ITEM_DESCRIPTION"));
                rt.put("qtyStock", rs.getString("QTY_stock"));
                rt.put("qtyStockE", rs.getString("QTY_stock_e"));
                rt.put("qtyStockT", rs.getString("QTY_stock_t"));
                rt.put("uom", rs.getString("UOM_STOCK"));
                rt.put("status", rs.getString("status"));
                return rt;
            }
        });
        return list;
    }

    //////////DONE
    //update outlet 12-04-23
    //list area    
    @Override
    public List<Map<String, Object>> viewArea(Map<String, String> balance) {
        String qry = "SELECT DISTINCT a.AREA_CODE as area_code,c.DESCRIPTION as area_desc, a.REGION_CODE as region_code,b.DESCRIPTION as region_desc FROM M_OUTLET a "
                + "JOIN M_GLOBAL b ON a.REGION_CODE = b.CODE AND b.COND = 'REG_OUTLET' "
                + "JOIN M_GLOBAL c ON a.AREA_CODE = c.CODE AND c.COND = 'AREACODE' "
                + "WHERE region_code like :region_code order by region_code,area_code asc";
        Map prm = new HashMap();
//         prm.put("region_code", Logan.get("region_code"));
        prm.put("region_code", "%" + balance.get("region_code") + "%");
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("area_code", rs.getString("area_code"));
                rt.put("area_desc", rs.getString("area_desc"));
                return rt;
            }
        });
        return list;
    }

    //type store 
    @Override
    public List<Map<String, Object>> viewTypeStore(Map<String, String> Logan) {
        String qry = "SELECT DISTINCT a.type, d.description as type_store FROM m_outlet a JOIN M_GLOBAL b ON a.REGION_CODE = b.CODE and b.cond='REG_OUTLET' JOIN M_GLOBAL c ON a.AREA_CODE = c.CODE and c.cond='AREACODE' JOIN M_GLOBAL d ON a.type = d.CODE and d.cond='OUTLET_TP' where a.status='A' and a.type not in ('HO','REG')";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("type", rs.getString("type"));
                rt.put("type_store", rs.getString("type_store"));

                return rt;
            }
        });
        return list;
    }
///////new methode from Dona 30-03-23//////////////////

    @Override
    public List<Map<String, Object>> listGlobal(Map<String, String> balance) {
        String qry = "SELECT DESCRIPTION, TYPE_MENU AS CODE FROM m_menudtl WHERE TYPE_MENU = :cond AND STATUS = :status AND APLIKASI = :aplikasi ORDER BY ID_NO ASC";
        if (balance.containsKey("outletBrand") && balance.get("outletBrand").equalsIgnoreCase("TACOBELL")) {
            qry += " AND DESCRIPTION IN ('Report Menu & Detail Modifier', 'Sales by Date', 'Sales by Item', 'Sales by Time', 'Summary Sales by Item Code', 'Report Stock Card')";
        }
        Map prm = new HashMap();
        prm.put("cond", balance.get("cond"));
        prm.put("status", balance.get("status"));
        prm.put("aplikasi", balance.get("aplikasi"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("code", rs.getString("CODE"));
                rt.put("description", rs.getString("DESCRIPTION"));
                return rt;
            }
        });
        // jika kosong/belum ada, insert baru
        if(list.isEmpty()){
            String qryInsert = """
                INSERT INTO M_MENUDTL
                (TYPE_ID, MENU_ID, DESCRIPTION, ID_NO, APLIKASI, DBASE, STATUS, TYPE_MENU)
                SELECT 'PROGRAM', 'POS0001', 'Cashier By Date', 1, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0001')
                UNION ALL
                SELECT 'PROGRAM', 'POS0002', 'Laporan Pemakaian Sales', 2, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0002')
                UNION ALL
                SELECT 'PROGRAM', 'POS0003', 'Report Menu & Detail Modifier', 3, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0003')
                UNION ALL
                SELECT 'PROGRAM', 'POS0004', 'Item Sales Analisis', 4, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0004')
                UNION ALL
                SELECT 'PROGRAM', 'POS0005', 'Sales by Date', 5, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0005')
                UNION ALL
                SELECT 'PROGRAM', 'POS0006', 'Sales by Item', 6, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0006')
                UNION ALL
                SELECT 'PROGRAM', 'POS0007', 'Sales by Time', 7, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0007')
                UNION ALL
                SELECT 'PROGRAM', 'POS0008', 'Sales Mix by Department', 8, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0008')
                UNION ALL
                SELECT 'PROGRAM', 'POS0009', 'Receipt Maintenance', 9, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0009')
                UNION ALL
                SELECT 'PROGRAM', 'POS0010', 'Summary Sales by Item Code', 10, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0010')
                UNION ALL
                SELECT 'PROGRAM', 'POS0011', 'Transaction by Payment Type', 11, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0011')
                UNION ALL
                SELECT 'PROGRAM', 'POS0012', 'Sales Void', 12, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0012')
                UNION ALL
                SELECT 'PROGRAM', 'POS0013', 'Laporan Item Selected By Time', 13, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0013')
                UNION ALL
                SELECT 'PROGRAM', 'POS0014', 'Refund Report', 14, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0014')
                UNION ALL
                SELECT 'PROGRAM', 'POS0015', 'Laporan Down Payment (DP)', 15, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0015')
                UNION ALL
                SELECT 'PROGRAM', 'POS0016', 'Actual Stock Opname', 16, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0016')
                UNION ALL
                SELECT 'PROGRAM', 'POS0017', 'End of Day', 17, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0017')
                UNION ALL
                SELECT 'PROGRAM', 'POS0018', 'Laporan Selected by Item Detail', 18, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0018')
                UNION ALL
                SELECT 'PROGRAM', 'POS0019', 'Laporan Product Efficiency', 19, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0019')
                UNION ALL
                SELECT 'PROGRAM', 'POS0020', 'Laporan Item Selected By Product', 20, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0020')
                UNION ALL
                SELECT 'PROGRAM', 'POS0021', 'Report Pajak', 21, 'POS', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'POS0021')
                UNION ALL
                SELECT 'PROGRAM', 'INV0001', 'Report Stock Card', 1, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV0001')
                UNION ALL
                SELECT 'PROGRAM', 'INV0002', 'Report Stock', 2, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV0002')
                UNION ALL
                SELECT 'PROGRAM', 'INV0003', 'Free Meal Dept', 3, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV0003')
                UNION ALL
                SELECT 'PROGRAM', 'INV0004', 'Listing Recipe', 4, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV0004')
                UNION ALL
                SELECT 'PROGRAM', 'INV0005', 'Item Barang', 5, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV0005')
                UNION ALL
                SELECT 'PROGRAM', 'INV0006', 'Delivery Order', 6, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV0006')
                UNION ALL
                SELECT 'PROGRAM', 'INV0007', 'Wastage', 7, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV0007')
                UNION ALL
                SELECT 'PROGRAM', 'INV0008', 'Return Order', 8, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV0008')
                UNION ALL
                SELECT 'PROGRAM', 'INV0009', 'Receiving', 9, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV0009')
                UNION ALL
                SELECT 'PROGRAM', 'INV00010', 'Order Entry', 10, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV00010')
                UNION ALL
                SELECT 'PROGRAM', 'INV00011', 'Inventory Movement', 11, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV00011')
                UNION ALL
                SELECT 'PROGRAM', 'INV00012', 'Laporan Produksi Aktual', 12, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV00012')
                UNION ALL
                SELECT 'PROGRAM', 'INV00013', 'Laporan Delete MPCS Produksi', 13, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV00013')
                UNION ALL
                SELECT 'PROGRAM', 'INV00014', 'Daftar Menu', 14, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV00014')
                UNION ALL
                SELECT 'PROGRAM', 'INV00015', 'Laporan Produksi', 15, 'INV', 'R', 'A', 'REPORT' FROM DUAL
                WHERE NOT EXISTS (SELECT 1 FROM M_MENUDTL WHERE MENU_ID = 'INV00015')
                """;
            System.err.println("insert New Menu report");
            try{
                jdbcTemplate.update(qryInsert, new HashMap());
                return listGlobal(balance);
            } catch (DataAccessException e) {
                System.err.println("Error insert New Menu report: " + e.getMessage());
            }
        }
        return list;
    }
    ///////////////////done
    
    ///////////////NEW METHOD LIST COND AND DATA GLOBAL BY LANI 4 APRIL 2023////
    @Override
    public List<Map<String, Object>> listGlobalCond(Map<String, String> balance) {
        String qry = "select distinct cond from m_global order by cond asc ";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("cond", rs.getString("cond"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listMasterGlobal(Map<String, String> balance) {
        String qry = "select COND,CODE,DESCRIPTION,VALUE,STATUS from m_global WHERE COND = :cond and status='A' ORDER BY VALUE ASC";
        Map prm = new HashMap();
        prm.put("cond", balance.get("cond"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("cond", rs.getString("COND"));
                rt.put("code", rs.getString("CODE"));
                rt.put("description", rs.getString("DESCRIPTION"));
                rt.put("value", rs.getString("VALUE"));
                rt.put("status", rs.getString("STATUS"));
                return rt;
            }
        });
        return list;
    }

    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 14 APRIL 2023////
    @Override
    public List<Map<String, Object>> listOrderHeader(Map<String, String> balance) {
        String qry = "SELECT * FROM T_ORDER_HEADER WHERE STATUS= :status AND ORDER_TO= :orderTo AND OUTLET_CODE=:outletCode";
        Map prm = new HashMap();
        prm.put("status", balance.get("status"));
        prm.put("orderTo", balance.get("orderTo"));
        prm.put("outletCode", balance.get("outletCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("orderType", rs.getString("ORDER_TYPE"));
                rt.put("orderId", rs.getString("ORDER_ID"));
                rt.put("orderNo", rs.getString("ORDER_NO"));
                rt.put("orderDate", rs.getString("ORDER_DATE"));
                rt.put("orderTo", rs.getString("ORDER_TO"));
                rt.put("cdTemplate", rs.getString("CD_TEMPLATE"));
                rt.put("dtDue", rs.getString("DT_DUE"));
                rt.put("dtExpired", rs.getString("DT_EXPIRED"));
                rt.put("remark", rs.getString("REMARK"));
                rt.put("noOfPrint", rs.getString("NO_OF_PRINT"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("userUpd", rs.getString("USER_UPD"));
                rt.put("dateUpd", rs.getString("DATE_UPD"));
                rt.put("timeUpd", rs.getString("TIME_UPD"));
                return rt;
            }
        });
        return list;
    }

    ///////////////////done
    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 18 APRIL 2023////
    ////////////// WHERE CLOUSE USING BETWEEN TRANS_DATE AND ONE MONTH BEFORE TRANSDATE UPDATE BY DANI 8 DEC 2023
    @Override
    public List<Map<String, Object>> listOrderHeaderAll(Map<String, String> balance) {
        String getCity = getCity(balance.get("outletCode"));
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String transDate = getTransDate(balance.get("outletCode")).toLowerCase();
        LocalDate localDate = LocalDate.parse(transDate, format);
        LocalDate beforeTransDate = localDate.minusDays(30);
        String where = "";
        if (!balance.get("orderDate").equals("")) {
            where = "AND ORDER_DATE =:orderDate";
        } else {
            // where = "and ORDER_DATE between TO_CHAR(CURRENT_DATE-7,'dd-MON-yy') and TO_CHAR(CURRENT_DATE,'dd-MON-yy')";
            // WHERE CLOUSE USING BETWEEN TRANS_DATE AND ONE MONTH BEFORE TRANSDATE by Dani
            where = " and ORDER_DATE >= TO_DATE('" + beforeTransDate.format(format) + "', 'YYYY-MM-DD') AND ORDER_DATE <= TO_DATE('" + localDate.format(format) + "', 'YYYY-MM-DD') ";
        }
        String qry = "SELECT H.*, case when G.DESCRIPTION is null and  m.outlet_name is null then s.supplier_name  "
                + "                when G.DESCRIPTION is null and s.supplier_name  is null then m.outlet_name else "
                + "               g.description end as NAMA_GUDANG, "
                + "               case when K.status_kirim = 'S' then 'Sudah' else 'Belum' end as STATUS_KIRIM"
                + " FROM T_ORDER_HEADER H "
                + " LEFT JOIN M_GLOBAL G ON G.CODE = H.CD_SUPPLIER AND G.COND = 'X_" + getCity + "' AND G.STATUS = 'A' "
                + " left join m_outlet M "
                + "               on H.cd_supplier=m.outlet_code "
                + "               left join m_supplier S "
                + "               on h.cd_supplier=s.cd_supplier "
                + " LEFT JOIN HIST_KIRIM K ON K.NO_ORDER = H.ORDER_NO "
                + "WHERE H.STATUS LIKE :status  "
                + "AND H.ORDER_TYPE LIKE :orderType  "
                + "AND H.OUTLET_CODE = :outletCode  "
                + "AND H.Order_to LIKE :orderTo  "
                + "" + where + ""
                + "AND CASE WHEN G.DESCRIPTION is null and  m.outlet_name is null then s.supplier_name " 
                + "WHEN G.DESCRIPTION is null and s.supplier_name  is null then m.outlet_name ELSE g.description end LIKE :delivery"
                + " ORDER BY H.DATE_UPD DESC, H.TIME_UPD DESC ";
        Map prm = new HashMap();
        prm.put("status", "%" + balance.get("status") + "%");
        prm.put("orderType", "%" + balance.get("orderType") + "%");
        prm.put("orderTo", "%" + balance.get("orderTo") + "%");
        prm.put("outletCode", balance.get("outletCode"));
        prm.put("orderDate", balance.get("orderDate"));
        prm.put("delivery", "%" + balance.get("delivery") + "%");

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("orderType", rs.getString("ORDER_TYPE"));
                rt.put("orderId", rs.getString("ORDER_ID"));
                rt.put("orderNo", rs.getString("ORDER_NO"));
                rt.put("orderDate", rs.getString("ORDER_DATE"));
                rt.put("orderTo", rs.getString("ORDER_TO"));
                rt.put("cdSupplier", rs.getString("CD_SUPPLIER"));
                rt.put("dtDue", rs.getString("DT_DUE"));
                rt.put("dtExpired", rs.getString("DT_EXPIRED"));
                rt.put("remark", rs.getString("REMARK"));
                rt.put("noOfPrint", rs.getString("NO_OF_PRINT"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("userUpd", rs.getString("USER_UPD"));
                rt.put("dateUpd", rs.getString("DATE_UPD"));
                rt.put("timeUpd", rs.getString("TIME_UPD"));
                rt.put("gudangName", rs.getString("NAMA_GUDANG"));
                rt.put("statusKirim", rs.getString("STATUS_KIRIM"));

                return rt;
            }
        });
        return list;
    }

//    @Override
//    public String getCity(String outletCode) {
//        
//        String qry = "SELECT DISTINCT CITY FROM M_OUTLET WHERE OUTLET_CODE = :outletCode and status = 'A'";
//        Map prm = new HashMap();
//        prm.put("outletCode", outletCode);
//        System.err.println("q :" + qry);
//        String list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
//                Map<String, Object> rt = new HashMap<String, Object>();
//                rt.put("city", rs.getString("CITY"));
//                return rt;
//            }
//        });
//        return list;
//    }
    @Override
    public String getCity(String outletCode) {
        String qry = "SELECT DISTINCT CITY FROM M_OUTLET WHERE OUTLET_CODE = :outletCode and status = 'A'";
        Map prm = new HashMap();
        prm.put("outletCode", outletCode);
        return jdbcTemplate.queryForObject(qry, prm, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString(1) == null ? "0" : rs.getString(1);
            }
        }).toString();
    }

    // DONE
    ////////// NEW METHOD GET TRANS DATE FROM M_OUTLET BY OUTLET_CODE BY DANI 8 DESEMBER 2023
    @Override
    public String getTransDate(String outletCode) {
        String qry = "SELECT DISTINCT TO_CHAR(TRANS_DATE, 'YYYY-MM-DD') FROM M_OUTLET WHERE OUTLET_CODE = :outletCode and status = 'A'";
        Map prm = new HashMap();
        prm.put("outletCode", outletCode);
        return jdbcTemplate.queryForObject(qry, prm, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString(1);
            }
        }).toString();
    }

    ///////////////////done
    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 27 APRIL 2023////
    @Override
    public List<Map<String, Object>> listOrderDetail(Map<String, String> balance) {
        String qry = "SELECT  "
                + "    ITEM_CODE, "
                + "    ITEM_DESCRIPTION, "
                + "    JUMLAH_SATUAN_BESAR, "
                + "    SATUAN_BESAR, "
                + "    JUMLAH_SATUAN_KECIL, "
                + "    UOM_PURCHASE, "
                + "    (CONV_WAREHOUSE * CONV_STOCK) CONV_WAREHOUSE, "
                + "    CONV_STOCK, "
                + "    (JUMLAH_SATUAN_BESAR * CONV_WAREHOUSE) + JUMLAH_SATUAN_KECIL TOTAL_JUMLAH, "
                + "    UOM_STOCK AS TOTAL "
                + "FROM ( "
                + "SELECT  "
                + "    ITEM_CODE, "
                + "    ITEM_DESCRIPTION, "
                + "    0 AS JUMLAH_SATUAN_BESAR, "
                + "    UOM_WAREHOUSE AS SATUAN_BESAR, "
                + "    0 AS JUMLAH_SATUAN_KECIL, "
                + "    UOM_PURCHASE,UOM_STOCK, "
                + "    CONV_WAREHOUSE,CONV_STOCK, "
                + "    0 TOTAL_JUMLAH, "
                + "    UOM_PURCHASE AS TOTAL "
                + "FROM M_ITEM WHERE CD_WAREHOUSE like :cdWarehouse AND STATUS = 'A' ORDER BY ITEM_CODE ASC)";
        Map prm = new HashMap();
        prm.put("cdWarehouse", "%" + balance.get("cdWarehouse") + "%");
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("jumlahSatuanBesar", rs.getString("JUMLAH_SATUAN_BESAR"));
                rt.put("satuanBesar", rs.getString("SATUAN_BESAR"));
                rt.put("jumlahSatuanKecil", rs.getString("JUMLAH_SATUAN_KECIL"));
                rt.put("uomPurchase", rs.getString("UOM_PURCHASE"));
                rt.put("convWarehouse", rs.getString("CONV_WAREHOUSE"));
                rt.put("convStock", rs.getString("CONV_STOCK"));
                rt.put("totalJumlah", rs.getString("TOTAL_JUMLAH"));
                rt.put("total", rs.getString("TOTAL"));

                return rt;
            }
        });
        return list;
    }

    ///////////////////done
    ///////new methode from Dona 2-Mei-23//////////////////
    @Override
    public List<Map<String, Object>> listCounter(Map<String, String> balance) {

        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        Date tgl = new Date();
        String month = df.format(tgl);
        String year = dfYear.format(tgl);

        String qry = "SELECT ORDER_ID||COUNTNO ORDER_ID FROM ( "
                + "SELECT A.OUTLET_CODE||:month||A.YEAR AS ORDER_ID,A.COUNTER_NO+1 COUNTNO FROM M_COUNTER A "
                + "LEFT JOIN M_OUTLET B "
                + "ON B.OUTLET_CODE=A.OUTLET_CODE "
                + "WHERE A.YEAR = :year AND A.MONTH= :month AND A.TRANS_TYPE = :transType AND A.OUTLET_CODE= :outletCode)";
        Map prm = new HashMap();
        prm.put("transType", balance.get("transType"));
        prm.put("outletCode", balance.get("outletCode"));
        prm.put("year", year);
        prm.put("month", month);
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                if (balance.get("transType").equals("ID")) {
                    rt.put("orderId", rs.getString("ORDER_ID"));
                } else {
                    rt.put("orderNo", balance.get("transType") + rs.getString("ORDER_ID"));
                }
                return rt;
            }
        });
        return list;
    }

    ///////////////////done
    @Override
    public List<Map<String, Object>> ViewOrderDetail(Map<String, String> balance) {
        String qry = "select oh.OUTLET_CODE,oh.ORDER_NO,od.ITEM_CODE,i.ITEM_DESCRIPTION, "
                + "od.QTY_1 jumlah_besar,od.CD_UOM_1 satuan_besar, "
                + "od.QTY_2 jumlah_kecil,od.CD_UOM_2 satuan_kecil,od.TOTAL_QTY_STOCK total_qty,i.UOM_STOCK, "
                + "(i.CONV_WAREHOUSE*i.CONV_STOCK) UOM_WAREHOUSE, CONV_STOCK "
                + "from T_ORDER_HEADER oh "
                + "left join T_ORDER_DETAIL od on od.ORDER_NO = oh.ORDER_NO and od.ORDER_ID = oh.ORDER_ID "
                + "left join M_ITEM i on i.ITEM_CODE = od.ITEM_CODE "
                + "where oh.ORDER_NO = :orderNo and oh.outlet_code = :outletCode";
        Map prm = new HashMap();
        prm.put("orderNo", balance.get("orderNo"));
        prm.put("outletCode", balance.get("outletCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("orderNo", rs.getString("ORDER_NO"));
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDesc", rs.getString("ITEM_DESCRIPTION"));
                rt.put("jmlBesar", rs.getString("jumlah_besar"));
                rt.put("satuanBesar", rs.getString("satuan_besar"));
                rt.put("jmlKecil", rs.getString("jumlah_kecil"));
                rt.put("satuanKecil", rs.getString("satuan_kecil"));
                rt.put("totalQty", rs.getString("total_qty"));
                rt.put("uomTotal", rs.getString("UOM_STOCK"));
                rt.put("convStock", rs.getString("CONV_STOCK"));
                rt.put("uomWarehouse", rs.getString("UOM_WAREHOUSE"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listItemDetailOpname(Map<String, String> balance) {
        String qry = "select ITEM_CODE,ITEM_DESCRIPTION,QTY_WAREHOUSE,CONV_WAREHOUSE,UOM_WAREHOUSE, "
                + "QTY_PURCHASE,CONV_STOCK,UOM_PURCHASE,0 TOTAL_STOCK,UOM_STOCK "
                + "from ( "
                + "select i.ITEM_CODE,i.ITEM_DESCRIPTION, 0 QTY_WAREHOUSE, i.UOM_WAREHOUSE, "
                + "0 QTY_PURCHASE,i.CONV_WAREHOUSE,i.UOM_PURCHASE,i.CONV_STOCK,i.UOM_STOCK "
                + "from m_item i "
                + "where i.status = 'A' AND FLAG_MATERIAL = 'Y' AND FLAG_STOCK = 'Y' "
                + "order by i.ITEM_CODE asc)";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("qtyWarehouse", rs.getString("QTY_WAREHOUSE"));
                rt.put("convWarehouse", rs.getString("CONV_WAREHOUSE"));
                rt.put("uomWareHouse", rs.getString("UOM_WAREHOUSE"));
                rt.put("qtyPurchase", rs.getString("QTY_PURCHASE"));
                rt.put("convStock", rs.getString("CONV_STOCK"));
                rt.put("upmPurchase", rs.getString("UOM_PURCHASE"));
                rt.put("totalStock", rs.getString("TOTAL_STOCK"));
                rt.put("uomStock", rs.getString("UOM_STOCK"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listEditItemDetailOpname(Map<String, String> balance) {
        String qry = "SELECT ITEM_CODE,ITEM_DESCRIPTION, "
                + "SUM(QTY_WAREHOUSE) QTY_WAREHOUSE, MAX(UOM_WAREHOUSE) UOM_WAREHOUSE , "
                + "SUM(QTY_PURCHASE) QTY_PURCHASE, MAX(CONV_WAREHOUSE) CONV_WAREHOUSE, "
                + "MAX(UOM_PURCHASE) UOM_PURCHASE, SUM(TOTAL_QTY) TOTAL_QTY, "
                + "MAX(CONV_STOCK) CONV_STOCK,MAX(UOM_STOCK) UOM_STOCK "
                + "FROM ( "
                + "select i.ITEM_CODE,i.ITEM_DESCRIPTION, 0 QTY_WAREHOUSE, i.UOM_WAREHOUSE, "
                + "0 QTY_PURCHASE,i.CONV_WAREHOUSE,i.UOM_PURCHASE,0 TOTAL_QTY,i.CONV_STOCK,i.UOM_STOCK "
                + "from m_item i "
                + "where i.status = 'A' AND i.FLAG_MATERIAL = 'Y' AND i.FLAG_STOCK = 'Y' "
                + "UNION ALL "
                + "select OD.ITEM_CODE,I.ITEM_DESCRIPTION, "
                + "OD.QTY_PURCH AS QTY_WAREHOUSE,I.UOM_WAREHOUSE, "
                + "OD.QTY_STOCK AS QTY_PURCHASE,i.CONV_WAREHOUSE,I.UOM_PURCHASE, "
                + "OD.TOTAL_QTY,i.CONV_STOCK,i.UOM_STOCK "
                + "from T_OPNAME_DETAIL OD "
                + "LEFT JOIN M_ITEM I ON I.ITEM_CODE = OD.ITEM_CODE "
                + "WHERE OD.OPNAME_NO = :opnameNo AND OD.OUTLET_CODE = :outletCode) "
                + "GROUP BY ITEM_CODE,ITEM_DESCRIPTION "
                + "ORDER BY ITEM_CODE,ITEM_DESCRIPTION ASC";
        Map prm = new HashMap();
        prm.put("outletCode", balance.get("outletCode"));
        prm.put("opnameNo", balance.get("opnameNo"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("qtyWarehouse", rs.getString("QTY_WAREHOUSE"));
                rt.put("convWarehouse", rs.getString("CONV_WAREHOUSE"));
                rt.put("uomWareHouse", rs.getString("UOM_WAREHOUSE"));
                rt.put("qtyPurchase", rs.getString("QTY_PURCHASE"));
                rt.put("convStock", rs.getString("CONV_STOCK"));
                rt.put("upmPurchase", rs.getString("UOM_PURCHASE"));
                rt.put("totalStock", rs.getString("TOTAL_QTY"));
                rt.put("uomStock", rs.getString("UOM_STOCK"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listHeaderOpname(Map<String, String> balance) {
        String qry = "SELECT H.OPNAME_NO,H.OPNAME_DATE, "
                + "CASE WHEN H.CD_TEMPLATE = '1' THEN TH.TEMPLATE_NAME "
                + "     WHEN H.CD_TEMPLATE = '0' THEN 'STOCK OPNAME BARANG '|| H.OPNAME_DATE  "
                + "END OPNAME_NAME, "
                + "case when H.CD_TEMPLATE = '1' THEN 'TEMPLATE'  "
                + "     WHEN H.CD_TEMPLATE = '0' THEN 'PER BARANG'  "
                + "END TYPE,H.STATUS "
                + "FROM T_OPNAME_HEADER H  "
                + "LEFT JOIN M_OPNAME_TEMPL_HEADER TH ON TH.CD_TEMPLATE = H.CD_TEMPLATE order by H.OPNAME_DATE DESC";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("opnameNo", rs.getString("OPNAME_NO"));
                rt.put("opnameDate", rs.getString("OPNAME_DATE"));
                rt.put("opnameName", rs.getString("OPNAME_NAME"));
                rt.put("type", rs.getString("TYPE"));
                rt.put("status", rs.getString("STATUS"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public String cekOpname(String outletCode, String month, String year) {
        String qry = "SELECT COUNT(*) count FROM T_OPNAME_HEADER WHERE TO_CHAR(OPNAME_DATE,'MON') = :month AND OUTLET_CODE = :outletCode AND TO_CHAR(OPNAME_DATE,'YYYY') = :year and STATUS IN ('0', '1')";
        Map prm = new HashMap();
        prm.put("outletCode", outletCode);
        prm.put("month", month);
        prm.put("year", year);
        return jdbcTemplate.queryForObject(qry, prm, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString(1) == null ? "0" : rs.getString(1);

            }
        }).toString();
    }

    @Override
    public String cekItem() {
        String qry = "SELECT COUNT(*) FROM M_ITEM  "
                + "WHERE status = 'A' AND FLAG_MATERIAL = 'Y' AND FLAG_STOCK = 'Y'";
        Map prm = new HashMap();
        return jdbcTemplate.queryForObject(qry, prm, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString(1) == null ? "0" : rs.getString(1);

            }
        }).toString();
    }

    @Override
    public String cekItemHq() {

        String json = "";
        String total = "";
        Gson gson = new Gson();
        Map<String, Object> map1 = new HashMap<String, Object>();
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            String url = "http://192.168.10.28:7009/warehouse/list-count-item";
            HttpPost post = new HttpPost(url);

            post.setHeader("Accept", "*/*");
            post.setHeader("Content-Type", "application/json");

            Map<String, Object> param = new HashMap<String, Object>();

            json = new Gson().toJson(param);
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);
            CloseableHttpResponse response = client.execute(post);
            System.out.println("json" + json);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            (response.getEntity().getContent())
                    )
            );
            StringBuilder content = new StringBuilder();
            String line;
            while (null != (line = br.readLine())) {
                content.append(line);
            }
            String result = content.toString();
            System.out.println("trans =" + result);
            map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
            }.getType());

            JsonObject job = gson.fromJson(result, JsonObject.class);
            JsonElement elem = job.get("data");
            //JsonArray  a =  new JsonArray(elem.getAsInt());

            //JSONArray arr = (JSONArray) job.get("data");
            total = elem.getAsJsonArray().getAsJsonObject().getAsJsonPrimitive("total").getAsString();
            //total = elem.getAsJsonObject().getAsJsonPrimitive("total").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    ///////////////////////////////Add Receiving by KP (06-06-2023)///////////////////////////////
    @Override
    public List<Map<String, Object>> listReceivingHeader(Map<String, String> ref) {
        ref.put("city", "X_" + ref.get("city"));
        String qry = "select "
                + "rc.recv_no, "
                + "rc.recv_date, "
                + "rc.order_no, "
                + "CASE WHEN ord.ORDER_TO = '3' THEN 'Gudang' WHEN ord.ORDER_TO = '2' THEN 'Outlet' WHEN ord.ORDER_TO = '1' THEN 'Canvasing' ELSE 'Supplier' END as ORDER_TO,"
                + "CASE "
                + "WHEN MG.DESCRIPTION IS NOT NULL THEN MG.DESCRIPTION "
                + "WHEN mo.OUTLET_NAME IS NOT NULL THEN mo.OUTLET_NAME "
                + "WHEN sp.supplier_name IS NOT NULL THEN sp.SUPPLIER_NAME "
                + "END AS supplier_name, "
                + "CASE "
                + "WHEN rc.order_no LIKE '%P%' AND sp.supplier_name IS NOT NULL THEN 'Pembelian' || ' ' || 'Supplier' || ' ' || sp.SUPPLIER_NAME "
                + "WHEN rc.order_no LIKE '%P%' AND MG.DESCRIPTION IS NOT NULL THEN 'Pembelian' || ' ' || mg.DESCRIPTION "
                + "WHEN rc.order_no LIKE '%P%' AND mo.OUTLET_NAME IS NOT NULL THEN 'Pembelian' || ' ' || 'Outlet' || ' ' || mo.OUTLET_NAME "
                + "WHEN rc.order_no LIKE '%R%' AND sp.supplier_name IS NOT NULL THEN 'Permintaan' || ' ' || 'Supplier' || ' ' || sp.SUPPLIER_NAME "
                + "WHEN rc.order_no LIKE '%R%' AND MG.DESCRIPTION IS NOT NULL THEN 'Permintaan' || ' ' || mg.DESCRIPTION "
                + "WHEN rc.order_no LIKE '%R%' AND mo.OUTLET_NAME IS NOT NULL THEN 'Permintaan' || ' ' || 'Outlet' || ' ' || mo.OUTLET_NAME "
                + "END AS tipeOrder, "
                //                + "'Pembelian ' || ("
                //                + "    case when length(ord.cd_supplier) = 4 then 'Outlet ' || mo.outlet_name"
                //                + "    when length(ord.cd_supplier) = 5 then mg.description"
                //                + "    else 'Supplier ' || sp.supplier_name end"
                //                + ") remark,"
                + "rc.remark, "
                + "case when hk.status_kirim = 'S' and hk.status_terima = 'R' then 'Sudah' else 'Manual' end as upd_online, "
                + "rc.no_of_print, "
                + "case when rc.status = '1' then 'CLOSE' when rc.status = '0' then 'OPEN' else 'UNKNOWN' end as status "
                + "from t_recv_header rc "
                + "left join t_order_header ord on ord.order_no = rc.order_no "
                + "left join m_supplier sp on sp.cd_supplier = ord.cd_supplier "
                + "left join m_global mg on mg.cond = :city and mg.code = ord.cd_supplier "
                + "left join m_outlet mo on mo.outlet_code = ord.cd_supplier "
                + "left join hist_kirim hk on hk.no_order = ord.order_no "
                + "where rc.recv_date between to_date(:dateStart, 'dd-mm-yyyy') and to_date(:dateEnd, 'dd-mm-yyyy') "
                + "AND (sp.supplier_name IS NOT NULL OR MG.DESCRIPTION IS NOT NULL OR mo.OUTLET_NAME IS NOT NULL) ";
        //"and length(ord.cd_supplier) = 5 " +
        //"order by rc.recv_date ";
        Map prm = new HashMap();
        prm.put("city", ref.get("city"));
        prm.put("dateStart", ref.get("dateStart"));
        prm.put("dateEnd", ref.get("dateEnd"));
        String filter = ref.get("filter");
        if (filter.equalsIgnoreCase("1")) { //Outlet
            qry += "and length(ord.cd_supplier) = 4 ";
        } else if (filter.equalsIgnoreCase("2")) { //Gudang
            qry += "and length(ord.cd_supplier) = 5 ";
        } else if (filter.equalsIgnoreCase("3")) { //Supplier
            qry += "and length(ord.cd_supplier) = 10 ";
        }
        qry += "order by rc.ORDER_NO ";
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("noTerima", rs.getString("recv_no"));
                rt.put("tanggal", rs.getString("recv_date"));
                rt.put("orderTo", rs.getString("order_to"));
                rt.put("noOrder", rs.getString("order_no"));
                rt.put("suppName", rs.getString("supplier_name"));
                rt.put("remark", rs.getString("remark"));
                rt.put("tipeOrder", rs.getString("tipeOrder"));
                rt.put("updOnline", rs.getString("upd_online"));
                rt.put("print", rs.getString("no_of_print"));
                rt.put("status", rs.getString("status"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listUnfinishedOrderHeader(Map<String, String> ref) {
        String qry = "SELECT ord.order_no, ord.order_date, "
                + "(CASE WHEN ord.order_no like 'PO%' then 'Pembelian ' else 'Permintaan ' end) || "
                + "(CASE WHEN length(ord.cd_supplier) = 4 THEN 'Outlet ' || mo.outlet_name WHEN length(ord.cd_supplier) = 5 THEN mg.description ELSE 'Supplier ' || sp.supplier_name END) remark, "
                + "CASE WHEN ord.status = '1' THEN 'Closed' WHEN ord.status = '0' THEN 'On Order' "
                + "WHEN ord.status = '2' THEN 'CANCEL' "
                + "ELSE 'UNKNOWN' "
                + "END AS status "
                + "FROM t_order_header ord "
                + "LEFT JOIN m_supplier       sp ON sp.cd_supplier = ord.cd_supplier "
                + "LEFT JOIN m_global         mg ON mg.cond = 'X_JKT' AND mg.code = ord.cd_supplier "
                + "LEFT JOIN m_outlet         mo ON mo.outlet_code = ord.cd_supplier "
                + "LEFT JOIN hist_kirim       hk ON hk.no_order = ord.order_no "
                + "WHERE ord.status = '0' "
                + "ORDER BY ord.order_date, ord.order_no";
        Map prm = new HashMap();
        //prm.put("dateStart", ref.get("dateStart"));
        //prm.put("dateEnd", ref.get("dateEnd"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("noOrder", rs.getString("order_no"));
                rt.put("tglOrder", rs.getString("order_date"));
                rt.put("tipeOrder", rs.getString("remark"));
                rt.put("status", rs.getString("status"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listReceivingDetail(Map<String, String> ref) {
        String qry = "select rd.total_qty, rd.recv_no, rd.order_no, rd.item_code, mi.uom_stock, mi.item_description, od.qty_1 ord_qty_1, rd.qty_1 rcv_qty_1, "
                + "rd.cd_uom_1, od.qty_2 ord_qty_2, rd.qty_2 rcv_qty_2, rd.cd_uom_2, (rd.qty_1 + rd.qty_2 + rd.qty_bonus) jml_total, total_price "
                + "from t_recv_detail rd "
                + "left join t_order_detail od on od.order_no = rd.order_no and od.item_code = rd.item_code "
                + "left join m_item mi on mi.item_code = rd.item_code "
                + "where rd.recv_no = :recv_no AND rd.order_no = :noOrder ";
        Map prm = new HashMap();
        prm.put("recv_no", ref.get("recv_no"));
        prm.put("noOrder", ref.get("noOrder"));

        //prm.put("dateEnd", ref.get("dateEnd"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("noTerima", rs.getString("recv_no"));
                rt.put("noOrder", rs.getString("order_no"));
                rt.put("kode", rs.getString("item_code"));
                rt.put("namaBarang", rs.getString("item_description"));
                rt.put("order1", rs.getString("ord_qty_1"));
                rt.put("terima1", rs.getString("rcv_qty_1"));
                rt.put("satuan1", rs.getString("cd_uom_1"));
                rt.put("order2", rs.getString("ord_qty_2"));
                rt.put("terima2", rs.getString("rcv_qty_2"));
                rt.put("satuan2", rs.getString("cd_uom_2"));
                rt.put("jmlTotal", rs.getString("jml_total"));
                rt.put("totalKg", rs.getString("total_price"));
                rt.put("totalQty", rs.getString("total_qty"));
                rt.put("total", rs.getString("uom_stock"));
                return rt;
            }
        });
        return list;
    }

    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 12 JUL 2023////
    @Override
    public List<Map<String, Object>> listOrderDetailOutlet(Map<String, String> balance) {
        String qry = "                SELECT ITEM_CODE, "
                + "                    ITEM_DESCRIPTION, "
                + "                    JUMLAH_SATUAN_BESAR, "
                + "                    SATUAN_BESAR, "
                + "                    JUMLAH_SATUAN_KECIL, "
                + "                    UOM_PURCHASE, "
                + "                    (CONV_WAREHOUSE * CONV_STOCK) CONV_WAREHOUSE, "
                + "                    CONV_STOCK, "
                + "                    (JUMLAH_SATUAN_BESAR * CONV_WAREHOUSE) + JUMLAH_SATUAN_KECIL TOTAL_JUMLAH, "
                + "                    UOM_STOCK AS TOTAL "
                + "                FROM ( "
                + "                SELECT  "
                + "                    ITEM_CODE, "
                + "                    ITEM_DESCRIPTION, "
                + "                    0 AS JUMLAH_SATUAN_BESAR, "
                + "                    UOM_WAREHOUSE AS SATUAN_BESAR, "
                + "                    0 AS JUMLAH_SATUAN_KECIL, "
                + "                    UOM_PURCHASE,UOM_STOCK, "
                + "                    CONV_WAREHOUSE,CONV_STOCK, "
                + "                    0 TOTAL_JUMLAH, "
                + "                    UOM_PURCHASE AS TOTAL "
                + "                FROM M_ITEM WHERE "
                + "                SUBSTR(ITEM_CODE,1,1) != 'X' AND STATUS = 'A' "
                + "                ORDER BY item_code asc)";
        Map prm = new HashMap();
        prm.put("cdWarehouse", balance.get("cdWarehouse"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("jumlahSatuanBesar", rs.getString("JUMLAH_SATUAN_BESAR"));
                rt.put("satuanBesar", rs.getString("SATUAN_BESAR"));
                rt.put("jumlahSatuanKecil", rs.getString("JUMLAH_SATUAN_KECIL"));
                rt.put("uomPurchase", rs.getString("UOM_PURCHASE"));
                rt.put("convWarehouse", rs.getString("CONV_WAREHOUSE"));
                rt.put("totalJumlah", rs.getString("TOTAL_JUMLAH"));
                rt.put("total", rs.getString("TOTAL"));
                rt.put("convStock", rs.getString("CONV_STOCK"));

                return rt;
            }
        });
        return list;
    }

    ///////////////////done
    ///////////////NEW METHOD LIST ORDER HEADER TEMPLATE BY DONA 13 JUL 2023////
    @Override
    public List<Map<String, Object>> listOrderDetailSupplier(Map<String, String> balance) {
        String qry = "SELECT * FROM ( "
                + "SELECT  "
                + "           ITEM_CODE, "
                + "           ITEM_DESCRIPTION, "
                + "           JUMLAH_SATUAN_BESAR, "
                + "           SATUAN_BESAR, "
                + "           JUMLAH_SATUAN_KECIL, "
                + "           UOM_PURCHASE, "
                + "           CONV_STOCK, "
                + "           (CONV_WAREHOUSE * CONV_STOCK) CONV_WAREHOUSE, "
                + "           (JUMLAH_SATUAN_BESAR * CONV_WAREHOUSE) + JUMLAH_SATUAN_KECIL TOTAL_JUMLAH, "
                + "           UOM_STOCK AS TOTAL "
                + "       FROM ( "
                + "     SELECT  "
                + "           ITEM_CODE, "
                + "           ITEM_DESCRIPTION, "
                + "           0 AS JUMLAH_SATUAN_BESAR, "
                + "           UOM_WAREHOUSE AS SATUAN_BESAR, "
                + "           0 AS JUMLAH_SATUAN_KECIL, "
                + "           UOM_PURCHASE,UOM_STOCK, "
                + "           CONV_WAREHOUSE,CONV_STOCK, "
                + "           0 TOTAL_JUMLAH, "
                + "           UOM_PURCHASE AS TOTAL "
                + "        FROM M_ITEM WHERE STATUS = 'A' )) A "
                + "                   LEFT JOIN M_ITEM_SUPPLIER S "
                + "                   ON A.ITEM_CODE=S.ITEM_CODE "
                + "                   WHERE S.CD_SUPPLIER=:cdSupplier";
        Map prm = new HashMap();
        prm.put("cdSupplier", balance.get("cdSupplier"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("jumlahSatuanBesar", rs.getString("JUMLAH_SATUAN_BESAR"));
                rt.put("satuanBesar", rs.getString("SATUAN_BESAR"));
                rt.put("jumlahSatuanKecil", rs.getString("JUMLAH_SATUAN_KECIL"));
                rt.put("uomPurchase", rs.getString("UOM_PURCHASE"));
                rt.put("convWarehouse", rs.getString("CONV_WAREHOUSE"));
                rt.put("totalJumlah", rs.getString("TOTAL_JUMLAH"));
                rt.put("total", rs.getString("TOTAL"));
                rt.put("convStock", rs.getString("CONV_STOCK"));
                return rt;
            }
        });
        return list;
    }

    ///////////////////done
    ///////////////NEW METHOD CEK DATA REPORT BY PASCA 13 JUL 2023////
    @Override
    public Integer cekDataReport(Map<String, Object> param, String name) {
        String query = null;
        Map<String, Object> prm = new HashMap<>();
        switch (name) {
            case "receiving" -> {
                if (param.get("filterBy").equals("All")) {
                    query = "SELECT COUNT(*) FROM T_RECV_HEADER a WHERE a.OUTLET_CODE = :outletCode AND a.RECV_DATE BETWEEN "
                            + ":fromDate AND :toDate";
                    prm.put("outletCode", param.get("outletCode"));
                    prm.put("fromDate", param.get("fromDate"));
                    prm.put("toDate", param.get("toDate"));
                } else {
                    query = "SELECT COUNT(*) FROM T_RECV_HEADER a LEFT JOIN T_ORDER_HEADER b ON a.ORDER_NO = b.ORDER_NO"
                            + " WHERE a.OUTLET_CODE = :outletCode AND a.RECV_DATE BETWEEN :fromDate AND :toDate AND "
                            + "b.CD_SUPPLIER =:cdSupplier";
                    prm.put("outletCode", param.get("outletCode"));
                    prm.put("fromDate", param.get("fromDate"));
                    prm.put("toDate", param.get("toDate"));
                    prm.put("cdSupplier", param.get("filterDesc"));
                }
            }
            case "orderEntry" -> {
                query = "SELECT COUNT(*) FROM T_ORDER_HEADER a WHERE a.ORDER_TYPE IN (:orderType1, :orderType2) "
                        + "AND a.ORDER_DATE BETWEEN :fromDate AND :toDate AND a.OUTLET_CODE = :outletCode";
                if (param.get("typeOrder").equals("Semua")) {
                    prm.put("orderType1", "0");
                    prm.put("orderType2", "1");
                } else if (param.get("typeOrder").equals("Permintaan")) {
                    prm.put("orderType1", "0");
                    prm.put("orderType2", "0");
                } else if (param.get("typeOrder").equals("Pembelian")) {
                    prm.put("orderType1", "1");
                    prm.put("orderType2", "1");
                }
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
                prm.put("outletCode", param.get("outletCode"));
            }
            case "returnOrder" -> {
                query = "SELECT COUNT(*) FROM T_RETURN_HEADER a WHERE a.OUTLET_CODE =:outletCode AND a.TYPE_RETURN IN "
                        + "(:typeReturn1, :typeReturn2) AND a.RETURN_DATE BETWEEN :fromDate AND :toDate";
                if (param.get("typeReturn").equals("ALL")) {
                    prm.put("typeReturn1", "0");
                    prm.put("typeReturn2", "1");
                } else if (param.get("typeReturn").equals("Supplier")) {
                    prm.put("typeReturn1", "0");
                    prm.put("typeReturn2", "0");
                } else if (param.get("typeReturn").equals("Gudang")) {
                    prm.put("typeReturn1", "1");
                    prm.put("typeReturn2", "1");
                }
                prm.put("outletCode", param.get("outletCode"));
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
            }
            case "wastage" -> {
                query = "SELECT COUNT(*) FROM T_WASTAGE_HEADER a WHERE a.OUTLET_CODE =:outletCode AND a.TYPE_TRANS IN "
                        + "(:typeTrans1, :typeTrans2) AND a.WASTAGE_DATE BETWEEN :fromDate AND :toDate";
                if (param.get("typeTransaksi").equals("ALL")) {
                    prm.put("typeTrans1", "W");
                    prm.put("typeTrans2", "L");
                } else if (param.get("typeTransaksi").equals("Wastage")) {
                    prm.put("typeTrans1", "W");
                    prm.put("typeTrans2", "W");
                } else if (param.get("typeTransaksi").equals("Left Over")) {
                    prm.put("typeTrans1", "L");
                    prm.put("typeTrans2", "L");
                }
                prm.put("outletCode", param.get("outletCode"));
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
            }
            case "deliveryOrder" -> {
                query = "SELECT COUNT(*) FROM T_DEV_HEADER a WHERE a.OUTLET_CODE =:outletCode AND a.DELIVERY_DATE BETWEEN "
                        + ":fromDate AND :toDate";
                prm.put("outletCode", param.get("outletCode"));
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
            }
            //////////////////////// new method case Delete MPCS Produksi adit 04-01-2024
            case "deleteMpcsProduksi" -> {
                query = "SELECT COUNT(*) FROM t_mpcs_hist a WHERE a.OUTLET_CODE =:outletCode AND a.MPCS_DATE BETWEEN "
                        + ":fromDate AND :toDate";
                prm.put("outletCode", param.get("outletCode"));
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
            }
            
             case "pajak" -> {
                query = "SELECT COUNT(*) FROM t_pos_bill a WHERE a.OUTLET_CODE =:outletCode AND a.trans_date BETWEEN "
                        + ":fromDate AND :toDate";
                prm.put("outletCode", param.get("outletCode"));
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
            }
            /////////////////////// done adit 04-01-2024
            case "item" -> {
                StringBuilder queryBuilder = new StringBuilder();

                if (param.get("status").equals("Semua")) {
                    prm.put("status1", "I");
                    prm.put("status2", "A");
                } else if (param.get("status").equals("Active")) {
                    prm.put("status1", "A");
                    prm.put("status2", "A");
                } else if (param.get("status").equals("Non Active")) {
                    prm.put("status1", "I");
                    prm.put("status2", "I");
                }

                if (param.get("type").equals("Semua")) {
                    prm.put("flagStock1", " ");
                    prm.put("flagStock2", "N");
                    prm.put("flagStock3", "Y");
                } else if (param.get("type").equals("Stock")) {
                    prm.put("flagStock1", "Y");
                    prm.put("flagStock2", "Y");
                    prm.put("flagStock3", "Y");
                } else if (param.get("type").equals("Non Stock")) {
                    prm.put("flagStock1", " ");
                    prm.put("flagStock2", "N");
                    prm.put("flagStock3", "N");
                }

                queryBuilder.append("SELECT COUNT(*) FROM M_ITEM a LEFT JOIN M_GLOBAL b ON a.CD_WAREHOUSE = "
                        + "b.CODE AND b.COND ='WAREHOUSE' JOIN M_OUTLET c ON c.OUTLET_CODE = :outletCode WHERE a.STATUS IN"
                        + " (:status1, :status2) AND a.FLAG_STOCK IN (:flagStock1, :flagStock2, :flagStock3)");

                if (!param.get("jenisGudang").equals("Semua")) {
                    queryBuilder.append(" AND b.DESCRIPTION = '").append(param.get("jenisGudang")).append("'");
                }
                if (param.containsKey("bahanBaku")) {
                    queryBuilder.append(" AND a.FLAG_MATERIAL = 'Y'");
                }
                if (param.containsKey("itemJual")) {
                    queryBuilder.append(" AND a.FLAG_FINISHED_GOOD = 'Y'");
                }
                if (param.containsKey("pembelian")) {
                    queryBuilder.append(" AND a.FLAG_OTHERS = 'Y'");
                }
                if (param.containsKey("produksi")) {
                    queryBuilder.append(" AND a.FLAG_HALF_FINISH = 'Y'");
                }
                if (param.containsKey("openMarket")) {
                    queryBuilder.append(" AND a.FLAG_OPEN_MARKET = 'Y'");
                }
                if (param.containsKey("canvasing")) {
                    queryBuilder.append(" AND a.FLAG_CANVASING = 'Y'");
                }
                if (param.containsKey("transferDo")) {
                    queryBuilder.append(" AND a.FLAG_TRANSFER_LOC = 'Y'");
                }
                if (param.containsKey("paket")) {
                    queryBuilder.append(" AND a.FLAG_PAKET = 'Y'");
                }

                query = queryBuilder.toString();
                prm.put("outletCode", param.get("outletCode"));
            }
            case "stock" -> {
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("SELECT COUNT(*) FROM T_STOCK_CARD a LEFT JOIN M_ITEM  b ON a.ITEM_CODE = b.ITEM_CODE "
                        + "LEFT JOIN M_GLOBAL c ON b.CD_WAREHOUSE = c.CODE AND c.COND = 'WAREHOUSE' WHERE a.OUTLET_CODE = "
                        + ":outletCode AND TRANS_DATE BETWEEN :fromDate AND :toDate");

                prm.put("outletCode", param.get("outletCode"));
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));

                if (param.get("typePrint").equals(1.0)) {
                    queryBuilder.append(" AND (a.QTY_IN  != 0 OR a.QTY_OUT != 0 OR a.QTY_BEGINNING != 0)");
                }
                if (param.get("stockMinus").equals(1.0)) {
                    queryBuilder.append(" AND SIGN(a.QTY_BEGINNING + a.QTY_IN - a.QTY_OUT) = -1");
                }
                if (!param.get("gudang").equals("Semua")) {
                    queryBuilder.append(" AND c.DESCRIPTION = :gudang");
                    prm.put("gudang", param.get("gudang"));
                }
                if (!param.get("item").equals("Semua")) {
                    queryBuilder.append(" AND a.ITEM_CODE = :item");
                    prm.put("item", param.get("item"));
                }
                query = queryBuilder.toString();
            }
            case "recipe" -> {
                query = "SELECT COUNT(*) FROM M_RECIPE_HEADER a WHERE a.STATUS = :status";
                if (param.get("status").equals("Active")) {
                    prm.put("status", "A");
                } else {
                    prm.put("status", "I");
                }
            }
            case "freeMeal" -> {
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("SELECT COUNT(*) FROM T_DEV_HEADER a WHERE a.REMARK LIKE '%FREEMEAL%' AND a.OUTLET_CODE"
                        + " = :outletCode AND a.DELIVERY_DATE BETWEEN :fromDate AND :toDate");

                prm.put("outletCode", param.get("outletCode"));
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));

                if (!param.get("department").equals("ALL")) {
                    queryBuilder.append(" AND a.OUTLET_TO = :outletTo");
                    prm.put("outletTo", param.get("department"));
                }
                query = queryBuilder.toString();
            }
            case "transaksiKasir" -> {
                query = "SELECT COUNT(*) FROM T_POS_DAY_TRANS WHERE TRANS_DATE BETWEEN :fromDate AND :toDate "
                        + "AND CASHIER_CODE BETWEEN :cashierCode1 AND :cashierCode2 AND SHIFT_CODE BETWEEN :shiftCode1 AND "
                        + ":shiftCode2 AND TRANS_CODE != 'DNT' AND OUTLET_CODE = :outletCode";

                prm.put("outletCode", param.get("outletCode"));
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));

                List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");

                if (listCashier.size() == 1) {
                    prm.put("cashierCode1", "000");
                    prm.put("cashierCode2", "zzz");
                } else {
                    for (Map<String, Object> object : listCashier) {
                        if (object.containsKey("cashierCode1")) {
                            prm.put("cashierCode1", object.get("cashierCode1"));
                        } else {
                            prm.put("cashierCode2", object.get("cashierCode2"));
                        }
                    }
                }

                List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");

                if (listShift.size() == 1) {
                    prm.put("shiftCode1", "000");
                    prm.put("shiftCode2", "zzz");
                } else {
                    for (Map<String, Object> object : listShift) {
                        if (object.containsKey("shiftCode1")) {
                            prm.put("shiftCode1", object.get("shiftCode1"));
                        } else {
                            prm.put("shiftCode2", object.get("shiftCode2"));
                        }
                    }
                }
            }
            case "ReceiptMaintenance" -> {
                query = "SELECT COUNT(*) FROM T_POS_BILL WHERE TRANS_DATE = :date AND OUTLET_CODE = :outletCode";
                prm.put("date", param.get("periode"));
                prm.put("outletCode", param.get("outletCode"));
            }
            case "salesMixDepartment" -> {
                query = "SELECT COUNT(*) FROM TMP_SALES_BY_ITEM WHERE OUTLET_CODE =:outletCode AND TRANS_DATE BETWEEN "
                        + ":fromDate AND :toDate AND POS_CODE BETWEEN :posCode1 AND :posCode2 AND CASHIER_CODE BETWEEN "
                        + ":cashierCode1 AND :cashierCode2 AND SHIFT_CODE BETWEEN :shiftCode1 AND :shiftCode2";

                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
                prm.put("outletCode", param.get("outletCode"));

                List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");

                if (listPos.size() == 1) {
                    prm.put("posCode1", "000");
                    prm.put("posCode2", "zzz");
                } else {
                    for (Map<String, Object> object : listPos) {
                        if (object.containsKey("posCode1")) {
                            prm.put("posCode1", object.get("posCode1"));
                        } else {
                            prm.put("posCode2", object.get("posCode2"));
                        }
                    }
                }

                List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");

                if (listCashier.size() == 1) {
                    prm.put("cashierCode1", "000");
                    prm.put("cashierCode2", "zzz");
                } else {
                    for (Map<String, Object> object : listCashier) {
                        if (object.containsKey("cashierCode1")) {
                            prm.put("cashierCode1", object.get("cashierCode1"));
                        } else {
                            prm.put("cashierCode2", object.get("cashierCode2"));
                        }
                    }
                }

                List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");

                if (listShift.size() == 1) {
                    prm.put("shiftCode1", "000");
                    prm.put("shiftCode2", "zzz");
                } else {
                    for (Map<String, Object> object : listShift) {
                        if (object.containsKey("shiftCode1")) {
                            prm.put("shiftCode1", object.get("shiftCode1"));
                        } else {
                            prm.put("shiftCode2", object.get("shiftCode2"));
                        }
                    }
                }
            }
            case "TransactionByPaymentType" -> {
                query = "SELECT COUNT(*) FROM M_PAYMENT_METHOD A, T_POS_BILL B, T_POS_BILL_PAYMENT C WHERE B.OUTLET_CODE = "
                        + ":outletCode AND B.TRANS_DATE BETWEEN :transDate1 AND :transDate2 AND B.POS_CODE BETWEEN :posCode1 "
                        + "AND :posCode2 AND B.CASHIER_CODE BETWEEN :cashierCode1 AND :cashierCode2 AND B.BILL_TIME BETWEEN"
                        + " :billTime1 AND :billTime2 AND A.PAYMENT_TYPE_CODE BETWEEN :paymentType1 AND :paymentType2 AND "
                        + "A.PAYMENT_METHOD_CODE BETWEEN :paymentMethod1 AND :paymentMethod2 AND B.SHIFT_CODE BETWEEN "
                        + ":shiftCode1 AND :shiftCode2 AND A.OUTLET_CODE = B.OUTLET_CODE AND B.OUTLET_CODE = C.OUTLET_CODE"
                        + " AND B.TRANS_DATE = C.TRANS_DATE AND A.PAYMENT_METHOD_CODE = C.PAYMENT_METHOD_CODE AND B.POS_CODE"
                        + " = C.POS_CODE AND B.BILL_NO = C.BILL_NO";

                prm.put("transDate1", param.get("fromDate"));
                prm.put("transDate2", param.get("toDate"));
                prm.put("outletCode", param.get("outletCode"));
                prm.put("billTime1", param.get("fromTime"));
                prm.put("billTime2", param.get("toTime"));

                List<Map<String, Object>> listPos = (List<Map<String, Object>>) param.get("pos");

                if (listPos.size() == 1) {
                    prm.put("posCode1", "000");
                    prm.put("posCode2", "zzz");
                } else {
                    for (Map<String, Object> object : listPos) {
                        if (object.containsKey("posCode1")) {
                            prm.put("posCode1", object.get("posCode1"));
                        } else {
                            prm.put("posCode2", object.get("posCode2"));
                        }
                    }
                }

                List<Map<String, Object>> listCashier = (List<Map<String, Object>>) param.get("cashier");

                if (listCashier.size() == 1) {
                    prm.put("cashierCode1", "000");
                    prm.put("cashierCode2", "zzz");
                } else {
                    for (Map<String, Object> object : listCashier) {
                        if (object.containsKey("cashierCode1")) {
                            prm.put("cashierCode1", object.get("cashierCode1"));
                        } else {
                            prm.put("cashierCode2", object.get("cashierCode2"));
                        }
                    }
                }

                List<Map<String, Object>> listShift = (List<Map<String, Object>>) param.get("shift");

                if (listShift.size() == 1) {
                    prm.put("shiftCode1", "000");
                    prm.put("shiftCode2", "zzz");
                } else {
                    for (Map<String, Object> object : listShift) {
                        if (object.containsKey("shiftCode1")) {
                            prm.put("shiftCode1", object.get("shiftCode1"));
                        } else {
                            prm.put("shiftCode2", object.get("shiftCode2"));
                        }
                    }
                }

                List<Map<String, Object>> listPaymentType = (List<Map<String, Object>>) param.get("PaymentType");

                if (listPos.size() == 1) {
                    prm.put("paymentType1", "000");
                    prm.put("paymentType2", "zzz");
                } else {
                    for (Map<String, Object> object : listPaymentType) {
                        if (object.containsKey("paymentType1")) {
                            prm.put("paymentType1", object.get("paymentType1"));
                        } else {
                            prm.put("paymentType2", object.get("paymentType2"));
                        }
                    }
                }

                List<Map<String, Object>> listPaymentMethod = (List<Map<String, Object>>) param.get("paymentMethod");

                if (listPos.size() == 1) {
                    prm.put("paymentMethod1", "000");
                    prm.put("paymentMethod2", "zzz");
                } else {
                    for (Map<String, Object> object : listPaymentMethod) {
                        if (object.containsKey("paymentMethod1")) {
                            prm.put("paymentMethod1", object.get("paymentMethod1"));
                        } else {
                            prm.put("paymentMethod2", object.get("paymentMethod2"));
                        }
                    }
                }
            }
            case "pemakaianBySales" -> {
                query = "SELECT COUNT(*) FROM t_pos_bill_item WHERE OUTLET_CODE = :outletCode AND TRANS_DATE BETWEEN :fromDate"
                        + " AND :toDate";
                prm.put("fromDate", param.get("fromDate"));
                prm.put("outletCode", param.get("outletCode"));
                prm.put("toDate", param.get("toDate"));
            }
            case "produksiAktual" -> {
                query = "SELECT COUNT(*) FROM T_MPCS_HIST WHERE MPCS_DATE = :mpcsDate AND OUTLET_CODE = :outletCode AND "
                        + "TIME_UPD BETWEEN :startTime AND :endTime";
                prm.put("mpcsDate", param.get("date"));
                prm.put("outletCode", param.get("outletCode"));
                prm.put("startTime", param.get("startTime"));
                prm.put("endTime", param.get("endTime"));
            }
            case "inventoryMovement" -> {
                query = "SELECT COUNT(*) FROM T_STOCK_CARD WHERE TRANS_DATE BETWEEN :fromDate AND :toDate AND"
                        + " OUTLET_CODE = :outletCode";
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
                prm.put("outletCode", param.get("outletCode"));
            }
            case "stockOpname" -> {
                query = "SELECT COUNT(*) FROM T_OPNAME_DETAIL WHERE OPNAME_NO = :opnameNo AND OUTLET_CODE = :outletCode";
                prm.put("opnameNo", param.get("opnameNo"));
                prm.put("outletCode", param.get("outletCode"));
            }
            case "orderEntryTransactions" -> {
                query = "SELECT COUNT(*) FROM T_ORDER_DETAIL WHERE ORDER_NO = :orderNo AND OUTLET_CODE = :outletCode";
                prm.put("orderNo", param.get("orderNo"));
                prm.put("outletCode", param.get("outletCode"));
            }
            case "receivingTransactions" -> {
                query = "SELECT COUNT(*) FROM t_recv_detail WHERE RECV_NO = :recvNo AND OUTLET_CODE = :outletCode";
                prm.put("recvNo", param.get("recvNo"));
                prm.put("outletCode", param.get("outletCode"));
            }
            case "wastageTransactions" -> {
                query = "SELECT COUNT(*) FROM T_WASTAGE_DETAIL WHERE WASTAGE_NO = :wastageNo AND OUTLET_CODE = :outletCode";
                prm.put("wastageNo", param.get("wastageNo"));
                prm.put("outletCode", param.get("outletCode"));
            }
            case "returnOrderTransactions" -> {
                query = "SELECT COUNT(*) FROM T_RETURN_DETAIL WHERE RETURN_NO = :returnNo AND OUTLET_CODE = :outletCode";
                prm.put("returnNo", param.get("returnNo"));
                prm.put("outletCode", param.get("outletCode"));
            }
            case "itemSelectedByTime" -> {
                query = "SELECT COUNT(*) FROM t_pos_bill_item WHERE TRANS_DATE BETWEEN :fromDate AND :toDate AND"
                        + " OUTLET_CODE = :outletCode AND MENU_ITEM_CODE = :kodeItem";
                prm.put("outletCode", param.get("outletCode"));
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
                prm.put("kodeItem", param.get("kodeItem"));
            }
            case "deliveryOrderTransactions" -> {
                query = "select COUNT(*) FROM T_DEV_DETAIL WHERE OUTLET_CODE = :outletCode AND REQUEST_NO = :requestNo AND DELIVERY_NO = :deliveryNo";
                prm.put("outletCode", param.get("outletCode"));
                prm.put("requestNo", param.get("requestNo"));
                prm.put("deliveryNo", param.get("deliveryNo"));
            }
            case "daftarMenu" -> {
                query = "SELECT COUNT (*) from"
                        + "(select mmi.menu_group_code,mmi.menu_item_code, mg.description, mp.price, "
                        + "mmi.modifier_group1_Code, mmi.modifier_group2_code, mmi.modifier_group3_code, "
                        + "mmi.modifier_group4_code, mmi.modifier_group5_code, mmi.modifier_group6_code, "
                        + "mmi.modifier_group7_code, mmi.call_group_code "
                        + "from m_menu_item mmi "
                        + "join m_global mg on mmi.menu_item_code = mg.code "
                        + "join m_outlet_price mop on mmi.outlet_code = mop.outlet_code "
                        + "join m_price mp on mmi.menu_item_code = mp.menu_item_code and "
                        + "mop.price_type_code = mp.price_type_code "
                        + "left join m_menu_item_limit mmil on "
                        + "mmi.region_code = mmil.region_code and mmi.outlet_code = mmil.outlet_code and "
                        + "mmi.menu_item_code = mmil.menu_item_code where mmi.outlet_code = :outletCode and "
                        + "mmi.menu_group_code in (select menu_group_code "
                        + "from m_menu_group_limit "
                        + "where order_type = :orderType) and mmi.status = :status and "
                        + "mg.cond = 'ITEM' and mop.order_type = :orderType and (mmil.order_type   is null or "
                        + "mmil.order_type = :orderType) order by mmi.menu_group_code,mmi.seq)a "
                        + "LEFT JOIN M_GLOBAL b ON a.menu_group_code=b.code";
                prm.put("outletCode", param.get("outletCode"));
                prm.put("orderType", param.get("orderType"));
                prm.put("status", param.get("status"));
            }
            case "reportRefund" -> {
                query = "SELECT Count(*) FROM T_POS_BILL tpb WHERE tpb.OUTLET_CODE = :outletCode AND tpb.TRANS_DATE BETWEEN :fromDate AND :toDate "
                        + "AND TOTAL_REFUND  IS NOT NULL AND TOTAL_REFUND <> 0";
                prm.put("outletCode", param.get("outletCode"));
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
            }
            case "productEfficiency" -> {
                query = "SELECT COUNT(*) FROM T_POS_BILL A JOIN T_POS_BILL_ITEM B ON A.REGION_CODE =" +
                        " B.REGION_CODE AND A.OUTLET_CODE = B.OUTLET_CODE AND A.POS_CODE = B.POS_CODE AND A.DAY_SEQ =" +
                        " B.DAY_SEQ AND A.BILL_NO = B.BILL_NO WHERE A.TRANS_DATE BETWEEN :fromDate AND :toDate AND " +
                        "A.DELIVERY_STATUS IN ('CLS', '')";
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
            }
            case "DownPayment" -> {
                query = "SELECT count(*)  " +
                "FROM " +
                "	T_POS_BOOK a " +
                "LEFT JOIN T_POS_BILL h ON  " +
                "	a.BOOK_NO  = h.BOOK_NO  " +
                "LEFT JOIN M_OUTLET i ON " +
                "	a.OUTLET_CODE = i.OUTLET_CODE " +
                "WHERE " +
                "	a.BOOK_DATE >= :startDate " +
                "	AND a.BOOK_DATE <= :endDate " +
                "	AND a.OUTLET_CODE = :outletCode " +
                "	AND a.CUSTOMER_NAME LIKE :customerName " +
                "	AND a.ORDER_TYPE LIKE :orderType " +
                "	AND a.BOOK_STATUS LIKE :bookStatus " ;
                prm.put("startDate", param.get("startDate"));                
                prm.put("endDate", param.get("endDate"));                
                prm.put("outletCode", param.get("outletCode"));                
                prm.put("customerName", param.get("customerName"));                
                prm.put("orderType", param.get("orderType"));                
                prm.put("bookStatus", param.get("bookStatus"));                
            }
            case "selectedItemByDetail" -> {
                query = "SELECT " + 
                        "   COUNT(*) " + 
                        "FROM " +
                        "    t_pos_bill_item a " + 
                        "JOIN " + 
                        "    t_pos_bill_item_detail b " + 
                        "    ON a.bill_no = b.bill_no " + 
                        "    AND a.pos_code = b.pos_code " + 
                        "    AND a.trans_date = b.trans_date " + 
                        "    AND a.outlet_code = b.outlet_code " + 
                        "    AND a.region_code = b.region_code " + 
                        "    AND a.day_seq = b.day_seq " + 
                        "    AND a.item_seq = b.item_seq " + 
                        "    AND b.menu_item_code IN (:menuItemCodes) " + 
                        "    AND b.trans_Date BETWEEN :fromDate AND :toDate " +
                        "    AND b.outlet_code = :outletCode " + 
                        "JOIN " + 
                        "    t_pos_bill c " + 
                        "    ON a.bill_no = c.bill_no " + 
                        "    AND a.pos_code = c.pos_code " + 
                        "    AND a.trans_date = c.trans_date " + 
                        "    AND a.outlet_code = c.outlet_code " + 
                        "    AND a.region_code = c.region_code " + 
                        "    AND a.day_seq = c.day_seq " + 
                        "    AND c.delivery_status = 'CLS' " + 
                        "    AND c.trans_Date BETWEEN :fromDate AND :toDate " + 
                        "    AND c.outlet_code = :outletCode " + 
                        "JOIN " + 
                        "    m_item c " + 
                        "    ON b.menu_item_code = c.item_code " + 
                        "JOIN " + 
                        "    m_item d " + 
                        "    ON a.menu_item_code = d.item_code " + 
                        "JOIN " + 
                        " m_outlet e " + 
                        " ON a.outlet_code = e.outlet_code " + 
                        "WHERE " + 
                        "b.trans_Date BETWEEN :fromDate AND :toDate AND c.trans_Date BETWEEN :fromDate AND :toDate " + 
                        "GROUP BY " + 
                        "    b.menu_item_code, c.item_description, a.menu_item_code, d.item_description, e.outlet_name, e.address_1, e.address_2 " + 
                        "ORDER BY " + 
                        "    b.menu_item_code DESC, a.menu_item_code DESC ";
                prm.put("fromDate", param.get("fromDate"));
                prm.put("toDate", param.get("toDate"));
                prm.put("outletCode", param.get("outletCode"));
                prm.put("menuItemCodes", param.get("menuItemCodes"));     
            }
        }
        assert query != null;
        System.err.println("q :" + query);
        System.err.println("prm :" + prm);
        return Integer.valueOf(Objects.requireNonNull(jdbcTemplate.queryForObject(query, prm, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1) == null ? "0" : rs.getString(1);
            }
        })));
    }

    ///////////////////////////////Add Wastage by KP (31-07-2023)///////////////////////////////
    @Override
    public List<Map<String, Object>> listWastageHeader(Map<String, String> ref) {
        String qry = "select wastage_no, wastage_date, type_trans, "
                + "case when type_trans = 'L' then 'Left Over' else 'Wastage' end as type_proses, remark, "
                + "wastage_id, case when status = '1' then 'CLOSED' else 'OPEN' end as final_status "
                + "from t_wastage_header "
                + "where wastage_date between TO_DATE(:startDate, 'dd-mm-yyyy') and TO_DATE(:endDate, 'dd-mm-yyyy') "
                + "order by wastage_date, wastage_no ";
        Map prm = new HashMap();
        prm.put("startDate", ref.get("startDate"));
        prm.put("endDate", ref.get("endDate"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("wastageNo", rs.getString("wastage_no"));
                rt.put("wastageDate", rs.getString("wastage_date"));
                rt.put("transType", rs.getString("type_trans"));
                rt.put("prosesType", rs.getString("type_proses"));
                rt.put("remark", rs.getString("remark"));
                rt.put("wastageID", rs.getString("wastage_id"));
                rt.put("status", rs.getString("final_status"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listWastageDetail(Map<String, String> ref) {
        String qry = "select w.wastage_no, w.item_code, w.quantity, i.item_description, i.uom_stock, w.item_to, i2.item_description item_description_to, i2.uom_stock uom_stock_to "
                + "from t_wastage_detail w "
                + "left join m_item i on i.item_code = w.item_code "
                + "left join m_item i2 on i2.item_code = w.item_to "
                + "where w.wastage_no = :wastageNo ";
        Map prm = new HashMap();
        prm.put("wastageNo", ref.get("wastageNo"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("wastageNo", rs.getString("wastage_no"));
                rt.put("itemCode", rs.getString("item_code"));
                Double qty = rs.getDouble("quantity");
                rt.put("quantity", String.format("%.4f", qty));
                rt.put("itemDesc", rs.getString("item_description"));
                rt.put("uomStock", rs.getString("uom_stock"));
                rt.put("itemTo", rs.getString("item_to"));
                rt.put("itemDescTo", rs.getString("item_description_to"));
                rt.put("quantityTo", String.format("%.2f", qty));
                rt.put("uomStockTo", rs.getString("uom_stock_to"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listOutletReport(Map<String, String> ref) {
        String query = "SELECT b.OUTLET_CODE, b.OUTLET_NAME FROM T_DEV_HEADER a LEFT JOIN M_OUTLET b ON a.OUTLET_TO = "
                + "b.OUTLET_CODE WHERE a.REMARK LIKE '%FREEMEAL%' AND a.OUTLET_CODE =:outletCode AND a.DELIVERY_DATE "
                + "BETWEEN :fromDate AND :toDate AND b.\"TYPE\" = 'HO' GROUP BY b.OUTLET_CODE, b.OUTLET_NAME";

        Map prm = new HashMap();
        prm.put("outletCode", ref.get("outletCode"));
        prm.put("fromDate", ref.get("fromDate"));
        prm.put("toDate", ref.get("toDate"));
        List<Map<String, Object>> list = jdbcTemplate.query(query, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("outletName", rs.getString("OUTLET_NAME"));
                return rt;
            }
        });
        return list;
    }

    // tambah fungsi untuk validasi nilai param by M Joko - 14 Dec 23
    private boolean isValidParamKey(String value) {
        if (value == null || "".equals(value)) {
            return false;
        } else {
            return !" ".equals(value);
        }
    }

/////////////////////////////List Stock Opname 7 AUG 2023///////////////////////////////////////////    
    @Override
    public List<Map<String, Object>> listStockOpname(Map<String, String> balance) {
        // update menampilkan data sebelum di filter by M Joko - 14 Dec 23
        Map prm = new HashMap();
        String qry = "select * from (select a.*,b.template_name,case when A.cd_template=:cdTemplate then 'TEMPLATE' ELSE 'PER BARANG' END AS type ";
        qry += "from t_opname_header a left join m_opname_templ_header b on a.cd_template=b.cd_template where a.cd_template = :cdTemplate ";
        if (isValidParamKey(balance.get("dateStart")) && isValidParamKey(balance.get("dateEnd"))) {
            qry += " and a.opname_date between to_date(:dateStart, 'dd-mm-yyyy') and to_date(:dateEnd, 'dd-mm-yyyy') ";
            prm.put("dateStart", balance.get("dateStart"));
            prm.put("dateEnd", balance.get("dateEnd"));
        }
        if (isValidParamKey(balance.get("status"))) {
            qry += " and a.status = :status ";
            prm.put("status", balance.get("status"));
        } else {
            qry += " and a.status < 2 ";
            prm.put("status", balance.get("status"));
        }
        if (isValidParamKey(balance.get("outletCode"))) {
            qry += " and a.outlet_code = :outletCode ";
            prm.put("outletCode", balance.get("outletCode"));
        }
        if (isValidParamKey(balance.get("cdTemplate"))) {
            prm.put("cdTemplate", balance.get("cdTemplate"));
        } else {
            prm.put("cdTemplate", "1");
        }
        if (isValidParamKey(balance.get("limit"))) {
            prm.put("limit", balance.get("limit"));
        } else {
            prm.put("limit", "1001");
        }
        qry += " order by a.status asc, a.date_upd desc, a.time_upd desc) where rownum < :limit";

        System.err.println("balance : " + balance);
        System.err.println("status : " + balance.get("status"));
        System.err.println("isValidParamKey : " + isValidParamKey(balance.get("status")));
        System.err.println("p : " + prm);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, (ResultSet rs, int i) -> {
            Map<String, Object> rt = new HashMap<>();
            rt.put("outletCode", rs.getString("OUTLET_CODE"));
            rt.put("cdTemplate", rs.getString("CD_TEMPLATE"));
            rt.put("opnanameNo", rs.getString("OPNAME_NO"));
            rt.put("opnameDate", rs.getString("OPNAME_DATE"));
            rt.put("remark", rs.getString("remark"));
            rt.put("type", rs.getString("TYPE"));
            rt.put("status", rs.getString("STATUS"));
            rt.put("userUpd", rs.getString("USER_UPD"));
            rt.put("dateUpd", rs.getString("DATE_UPD"));
            rt.put("timeUpd", rs.getString("TIME_UPD"));
            rt.put("templateName", rs.getString("TEMPLATE_NAME"));
            return rt;
        });
        return list;
    }

    //Added View MPCS by KP (09-08-2023)
    @Override
    public List<Map<String, Object>> listTemplateMpcs(Map<String, String> ref) {
        String qry = "select outlet_code, seq_mpcs, time_mpcs, date_upd, time_upd "
                + "from template_mpcs "
                //+ "where date_upd = TO_DATE(:dateUpd, 'DD/MM/YYYY') "
                //+ "and outlet_code = :outlet "
                + "where outlet_code = :outlet "
                + "order by seq_mpcs asc ";
        Map prm = new HashMap();
        prm.put("outlet", ref.get("outlet"));
        //prm.put("dateUpd", ref.get("dateUpd"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("outletCode", rs.getString("outlet_code"));
                rt.put("seqMpcs", rs.getString("seq_mpcs"));
                rt.put("timeMpcs", rs.getString("time_mpcs"));
                rt.put("dateUpd", rs.getString("date_upd"));
                rt.put("timeUpd", rs.getString("time_upd"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listProjectMpcs(Map<String, String> ref) {
        String qry = "select seq_mpcs, time_mpcs, 0 as qty_proj from template_mpcs "
                + "where outlet_code = :outletCode "
                + "and time_mpcs not in "
                + "(select time_mpcs from t_summ_mpcs "
                + "where outlet_code = :outletCode "
                + "and mpcs_group = :mpcsGrp "
                + "and date_mpcs = :dateMpcs) "
                + "union all "
                + "select seq_mpcs, time_mpcs, qty_proj from t_summ_mpcs "
                + "where outlet_code = :outletCode "
                + "and mpcs_group = :mpcsGrp "
                + "and date_mpcs = :dateMpcs "
                + "order by seq_mpcs, time_mpcs ";
        System.err.println("q :" + qry);
        Map prm = new HashMap();
        prm.put("outletCode", ref.get("outlet"));
        prm.put("mpcsGrp", ref.get("mpcsGroup"));
        prm.put("dateMpcs", ref.get("dateMpcs"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("seqMpcs", rs.getString("seq_mpcs"));
                rt.put("timeMpcs", rs.getString("time_mpcs"));
                rt.put("qtyProj", rs.getString("qty_proj"));
                return rt;
            }
        });
        return list;
    }
    //End of MPCS

    //////////////////// aditya 12/12/2023 /////////////////////
    /// tambahan menampilkan data return order 30 hari sebelum nya ////////////////////
    @Override
    public List<Map<String, Object>> listReturnOrderHeader(Map<String, String> param) {
        Map<String, Object> sqlParam = new HashMap<>();
        String query = "SELECT a.RETURN_NO, a.RETURN_DATE, a.REMARK, CASE WHEN a.TYPE_RETURN = '0' THEN 'Supplier' ELSE 'Gudang' "
                + "END AS TYPE_RETURN, CONCAT(b.DESCRIPTION, CONCAT(c.OUTLET_NAME, d.SUPPLIER_NAME)) AS return_to,"
                + " a.STATUS FROM T_RETURN_HEADER a LEFT JOIN M_GLOBAL b ON a.RETURN_TO = b.CODE AND b.COND = :city"
                + " LEFT JOIN M_OUTLET c ON a.RETURN_TO  = c.OUTLET_CODE LEFT JOIN M_SUPPLIER d ON a.RETURN_TO = d.CD_SUPPLIER ";
        if (param.containsKey("startDate") && param.get("startDate").length() > 0) {
            query += " WHERE a.RETURN_DATE BETWEEN TO_DATE(:startDate, 'dd-mm-yyyy') AND TO_DATE(:endDate, 'dd-mm-yyyy') ";
            sqlParam.put("startDate", param.get("startDate"));
            sqlParam.put("endDate", param.get("endDate"));
        }
        query += " ORDER BY RETURN_DATE DESC, a.DATE_UPD DESC, a.TIME_UPD DESC ";
        if (param.containsKey("limit") && param.get("limit").length() > 0) {
            query = "SELECT * FROM ( " + query + " ) WHERE rownum <= :limit";
            sqlParam.put("limit", param.get("limit"));
        }
        sqlParam.put("city", "X_" + param.get("city"));
        System.err.println("q :" + query);
        //////////////////// done aditya /////////////////////////////////////

        List<Map<String, Object>> list = jdbcTemplate.query(query, sqlParam, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("returnNo", rs.getString("RETURN_NO"));
                rt.put("returnDate", rs.getString("RETURN_DATE"));
                rt.put("typeReturn", rs.getString("TYPE_RETURN"));
                rt.put("returnTo", rs.getString("return_to"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("remark", rs.getString("REMARK"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listReturnOrderDetail(Map<String, String> param) {
        String query = "SELECT a.ITEM_CODE, b.ITEM_DESCRIPTION, a.QTY_WAREHOUSE, a.UOM_WAREHOUSE, a.QTY_PURCHASE, "
                + "a.UOM_PURCHASE, a.TOTAL_QTY, b.UOM_STOCK FROM T_RETURN_DETAIL a LEFT JOIN M_ITEM b ON a.ITEM_CODE = "
                + "b.ITEM_CODE WHERE RETURN_NO = :returnNo";

        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("returnNo", param.get("returnNo"));

        List<Map<String, Object>> list = jdbcTemplate.query(query, sqlParam, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("qtyWarehouse", rs.getString("QTY_WAREHOUSE"));
                rt.put("uomWarehouse", rs.getString("UOM_WAREHOUSE"));
                rt.put("qtyPurchase", rs.getString("QTY_PURCHASE"));
                rt.put("uomPurchase", rs.getString("UOM_PURCHASE"));
                rt.put("totalQty", rs.getString("TOTAL_QTY"));
                rt.put("totalUom", rs.getString("UOM_STOCK"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listSupplierGudangReturnOrder(Map<String, String> param) {
        String query = null;

        Map<String, Object> sqlParam = new HashMap<>();
        if (param.get("typeReturn").equals("Gudang")) {
            query = "SELECT * FROM M_GLOBAL WHERE COND =:cond AND STATUS = 'A' ORDER BY CODE ASC";
            sqlParam.put("cond", param.get("cond"));

        }
        if (param.get("typeReturn").equals("Supplier")) {
            query = "SELECT * FROM M_SUPPLIER WHERE STATUS = 'A' ORDER BY SUPPLIER_NAME ASC";
        }

        List<Map<String, Object>> list = jdbcTemplate.query(query, sqlParam, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                if (param.get("typeReturn").equals("Gudang")) {
                    rt.put("code", rs.getString("CODE"));
                    rt.put("description", rs.getString("DESCRIPTION"));
                    rt.put("value", rs.getString("VALUE"));
                    rt.put("status", rs.getString("STATUS"));
                } else {
                    rt.put("cdSupplier", rs.getString("CD_SUPPLIER"));
                    rt.put("supplierName", rs.getString("SUPPLIER_NAME"));
                }
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listItemSupplierGudangReturnOrder(Map<String, Object> param) {
        String query = null;
        Map<String, Object> sqlParam = new HashMap<>();
        if (param.containsKey("cdSupplier")) {
            query = "SELECT b.ITEM_DESCRIPTION, b.STATUS, b.CONV_WAREHOUSE, b.CONV_STOCK, b.UOM_WAREHOUSE, b.UOM_PURCHASE, b.UOM_STOCK, b.ITEM_CODE "
                    + "FROM M_ITEM_SUPPLIER a LEFT JOIN M_ITEM b ON a.ITEM_CODE = b.ITEM_CODE WHERE CD_SUPPLIER =:cdSupplier";
            sqlParam.put("cdSupplier", param.get("cdSupplier"));
        }
        if (param.containsKey("cdWarehouse")) {
            query = "SELECT ITEM_DESCRIPTION, STATUS, CONV_WAREHOUSE, CONV_STOCK, UOM_WAREHOUSE, UOM_PURCHASE, UOM_STOCK, ITEM_CODE FROM M_ITEM WHERE "
                    + "CD_WAREHOUSE =:cdWarehouse ORDER BY ITEM_CODE ASC";
            sqlParam.put("cdWarehouse", param.get("cdWarehouse"));
        }
        List<Map<String, Object>> list = jdbcTemplate.query(query, sqlParam, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("convWarehouse", rs.getString("CONV_WAREHOUSE"));
                rt.put("convStock", rs.getString("CONV_STOCK"));
                rt.put("uomWarehouse", rs.getString("UOM_WAREHOUSE"));
                rt.put("uomPurchase", rs.getString("UOM_PURCHASE"));
                rt.put("uomStock", rs.getString("UOM_STOCK"));
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                return rt;
            }
        });
        return list;
    }

    /////////////////////////////List Detail Order by No 9-11-2023///////////////////////////////////////////
    @Override
    public List<Map<String, Object>> listDetailOderbyOrderno(Map<String, String> ref) {
        String qry = "SELECT   A.*,B.item_description, B.UOM_STOCK  "
                + " FROM T_ORDER_DETAIL A  "
                + " left join (select item_code,item_description, uom_stock from m_item ) B  "
                + " on A.ITEM_CODE=b.item_code  "
                + "WHERE ORDER_NO IN   "
                + "(SELECT ORDER_NO   "
                + "FROM T_ORDER_HEADER  "
                + "WHERE OUTLET_CODE =:outletCode   "
                + "AND ORDER_DATE=:orderDate   "
                + "AND ORDER_NO =:orderNo)";
        Map prm = new HashMap();
        prm.put("outletCode", ref.get("outletCode"));
        prm.put("orderDate", ref.get("orderDate"));
        prm.put("orderNo", ref.get("orderNo"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("orderType", rs.getString("ORDER_TYPE"));
                rt.put("orderId", rs.getString("ORDER_ID"));
                rt.put("orderNo", rs.getString("ORDER_NO"));
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("item_description"));
                rt.put("qty1", rs.getString("QTY_1"));
                rt.put("cdUom1", rs.getString("CD_UOM_1"));
                rt.put("qty2", rs.getString("QTY_2"));
                rt.put("cdUom2", rs.getString("CD_UOM_2"));
                rt.put("uomStock", rs.getString("uom_stock"));
                rt.put("totalQtyStock", rs.getString("TOTAL_QTY_STOCK"));
                rt.put("unitPrice", rs.getString("UNIT_PRICE"));
                rt.put("userUpd", rs.getString("USER_UPD"));
                rt.put("dateUpd", rs.getString("DATE_UPD"));
                rt.put("timeUpd", rs.getString("TIME_UPD"));
                return rt;
            }
        });
        return list;
    }

    ////////////New method for query stock card - Fathur 29-Nov-2023////////////
    // Updated query 11-Jan-2024//
    @Override
    public List<Map<String, Object>> listQueryStockCard(Map<String, String> ref) {
        String where = " ";
        if (!ref.get("cdWarehouse").equals("")) {
            where = " AND MITEM.ITEM_CODE IN (SELECT ITEM_CODE FROM M_ITEM WHERE CD_WAREHOUSE LIKE :cdWarehouse) ";
        }
        String query = "SELECT SCARD.TRANS_DATE, SCARD.TIME_UPD, "
                + "SCARD.ITEM_CODE, MITEM.ITEM_DESCRIPTION as ITEM_NAME, "
                + "SCARD.QTY_BEGINNING, SCARD.QTY_IN, SCARD.QTY_OUT, ((QTY_BEGINNING + QTY_IN) - QTY_OUT) as QTY_ENDING, "
                + "MITEM.UOM_STOCK AS UNIT, SCARD.REMARK "
                + "FROM T_STOCK_CARD SCARD "
                + "LEFT JOIN M_ITEM MITEM ON SCARD.ITEM_CODE = MITEM.ITEM_CODE "
                + "WHERE SCARD.TRANS_DATE between :startDate and :endDate "
                + "AND SCARD.QTY_IN > 0 AND SCARD.QTY_OUT > 0 "
                + "AND SCARD.ITEM_CODE LIKE :itemCode "
                + "AND MITEM.FLAG_STOCK = 'Y' " + where
                + "ORDER BY SCARD.ITEM_CODE ASC, SCARD.TRANS_DATE ASC ";
        Map param = new HashMap();
        param.put("startDate", ref.get("startDate"));
        param.put("endDate", ref.get("endDate"));
        param.put("itemCode", "%" + ref.get("itemCode") + "%");
        param.put("cdWarehouse", "%" + ref.get("cdWarehouse") + "%");
       
        return jdbcTemplate.query(query, param, (ResultSet rs, int i) -> {
            Map<String, Object> rt = new HashMap<>();
            rt.put("dateUpd", rs.getString("TRANS_DATE"));
            rt.put("timeUpd", rs.getString("TIME_UPD"));
            rt.put("itemCode", rs.getString("ITEM_CODE"));
            rt.put("itemName", rs.getString("ITEM_NAME"));
            rt.put("qtyBeginning", rs.getString("QTY_BEGINNING"));
            rt.put("qtyIn", rs.getString("QTY_IN"));
            rt.put("qtyOut", rs.getString("QTY_OUT"));
            rt.put("qtyEnding", rs.getString("QTY_ENDING"));
            rt.put("unit", rs.getString("UNIT"));
            rt.put("remark", rs.getString("REMARK"));
            return rt;
        });
    }
    ////////////Done method for query stock card////////////

    ////////////New method for query stock card detail - Fathur 30-Nov-2023////////////
    // added unit column 4-Des-2023
    @Override
    public List<Map<String, Object>> listQueryStockCardDetail(Map<String, String> ref) {
        String query = "SELECT S.OUTLET_CODE, S.TRANS_DATE, S.CD_TRANS, S.ITEM_CODE, M.ITEM_DESCRIPTION, M.UOM_STOCK AS UNIT, S.QUANTITY_IN, S.QUANTITY AS QUANTITY_OUT "
                + "FROM T_STOCK_CARD_DETAIL S "
                + "JOIN M_ITEM M ON M.ITEM_CODE = S.ITEM_CODE "
                + "WHERE S.TRANS_DATE = TO_DATE(:date, 'DD/MM/YYYY') "
                + "AND S.ITEM_CODE LIKE :itemCode ";

        Map param = new HashMap();
        Boolean isTransactionIn = ref.get("transactionType").equalsIgnoreCase("IN");
        if (isTransactionIn) {
            query += "AND S.QUANTITY_IN > 0";
        } else {
            query += "AND S.QUANTITY > 0";
        }
        param.put("date", ref.get("date"));
        param.put("itemCode", "%" + ref.get("itemCode") + "%");

        System.err.print("query: " + query);

        List<Map<String, Object>> stockCardDetail = jdbcTemplate.query(query, param, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("itemCode", rs.getString("ITEM_CODE"));
                rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                rt.put("cdTrans", rs.getString("CD_TRANS"));
                if (isTransactionIn) {
                    rt.put("qtyIn", rs.getString("QUANTITY_IN"));
                } else {
                    rt.put("qtyOut", rs.getString("QUANTITY_OUT"));
                }
                rt.put("unit", rs.getString("UNIT"));
                return rt;
            }
        });
        return stockCardDetail;
    }
    ////////////Done method for query stock card detail////////////

    ///////////////NEW METHOD MENU GROUP ITEM  4-DEC-2023////////////////////////////
    @Override
    public List<Map<String, Object>> listMenuGroupCodeDetail(Map<String, String> balance) {
        String qry = "  select A.PLU,A.SEQ,B.DESCRIPTION AS MENU_ITEM,A.STATUS from m_menu_item A "
                + "  LEFT JOIN (SELECT COND,CODE,DESCRIPTION FROM M_GLOBAL WHERE COND='ITEM' AND STATUS='A') B "
                + "  ON A.MENU_ITEM_CODE=B.CODE  "
                + "  LEFT JOIN (SELECT COND,CODE,DESCRIPTION FROM M_GLOBAL WHERE COND='GROUP' AND STATUS='A') C "
                + "  ON A.MENU_GROUP_CODE=C.CODE "
                + " WHERE  A.MENU_GROUP_CODE=:menuGroupCode";
        Map prm = new HashMap();
        prm.put("menuGroupCode", balance.get("menuGroupCode"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("plu", rs.getString("PLU"));
                rt.put("seq", rs.getString("SEQ"));
                rt.put("menuItem", rs.getString("MENU_ITEM"));
                rt.put("status", rs.getString("STATUS"));
                return rt;
            }
        });
        return list;
    }

    ///////////////////////////DONE////////////////////////    
    ////////////New method for query last EOD - M Joko M 4-Dec-2023////////////
    @Override
    public List<Map<String, Object>> lastEod(Map<String, String> ref) {
        Map param = new HashMap();
        String qry = "select * from (select e.*, o.outlet_name from t_eod_hist e join m_outlet o on o.outlet_code=:outletCode where o.outlet_code = :outletCode order by e.trans_date desc) where rownum = 1";
        param.put("outletCode", ref.get("outletCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, param, (ResultSet rs, int i) -> {
            Map<String, Object> rt = new HashMap<>();
            rt.put("regionCode", rs.getString("REGION_CODE"));
            rt.put("outletCode", rs.getString("OUTLET_CODE"));
            rt.put("transDate", rs.getString("TRANS_DATE"));
            rt.put("userEod", rs.getString("USER_EOD"));
            rt.put("dateEod", rs.getString("DATE_EOD"));
            rt.put("timeEod", rs.getString("TIME_EOD"));
            rt.put("sendFlag", rs.getString("SEND_FLAG"));
            rt.put("dateSend", rs.getString("DATE_SEND"));
            rt.put("outletName", rs.getString("OUTLET_NAME"));
            return rt;
        });
        return list;
    }
    /////////////////done last EOD////////////////

    ////////////New method for query POS yg Open berdasarkan Outlet - M Joko M 4-Dec-2023////////////
    @Override
    public List<Map<String, Object>> listPosOpen(Map<String, String> ref) {
        Map param = new HashMap();
        String qry = "select e.*, p.pos_description from t_eod_hist_dtl e join m_pos p on p.outlet_code=e.outlet_code and p.pos_code=e.pos_code where trans_date=(select trans_date from m_outlet where outlet_code=:outletCode) and process_eod<>'Y'";
        if (ref.containsKey("posCode")) {
            // ambil 1 pos berdasar code
            qry = "select e.*, p.pos_description from t_eod_hist_dtl e join m_pos p on p.outlet_code=e.outlet_code and p.pos_code=e.pos_code where trans_date=(select trans_date from m_outlet where outlet_code=:outletCode) and e.pos_code=:posCode and rownum = 1";
            param.put("posCode", ref.get("posCode"));
        }
        param.put("outletCode", ref.get("outletCode"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, param, (ResultSet rs, int i) -> {
            Map<String, Object> rt = new HashMap<>();
            rt.put("regionCode", rs.getString("REGION_CODE"));
            rt.put("outletCode", rs.getString("OUTLET_CODE"));
            rt.put("transDate", rs.getString("TRANS_DATE"));
            rt.put("posCode", rs.getString("POS_CODE"));
            rt.put("posDescription", rs.getString("POS_DESCRIPTION"));
            rt.put("processEod", rs.getString("PROCESS_EOD"));
            rt.put("notes", rs.getString("NOTES"));
            rt.put("userUpd", rs.getString("USER_UPD"));
            rt.put("dateUpd", rs.getString("DATE_UPD"));
            rt.put("timeUpd", rs.getString("TIME_UPD"));
            return rt;
        });
        return list;
    }
    /////////////////done POS yg Open berdasarkan Outlet////////////////

    //////////// New method for query M POS yg Active berdasarkan Outlet - M Joko M 12-Dec-2023////////////
    @Override
    public List<Map<String, Object>> listMPosActive(Map<String, String> ref) {
        String qry = "select region_code,outlet_code,pos_code,pos_description,ref_no,a.status,pos_type,description FROM M_pos a join m_global b on b.code=a.pos_type  where a.outlet_code= :outletCode and a.status='A' and b.cond='POS_TYPE'";
        Map param = new HashMap();
        param.put("outletCode", ref.get("outletCode"));
        System.err.println("q :" + qry);
        return jdbcTemplate.query(qry, param, (ResultSet rs, int i) -> {
            Map<String, Object> rt = new HashMap<>();
            rt.put("ref", rs.getString("REF_NO"));
            rt.put("status", rs.getString("STATUS"));
            rt.put("posType", rs.getString("POS_TYPE"));
            rt.put("description", rs.getString("DESCRIPTION"));
            rt.put("regionCode", rs.getString("REGION_CODE"));
            rt.put("outletCode", rs.getString("OUTLET_CODE"));
            rt.put("posCode", rs.getString("POS_CODE"));
            rt.put("posDescription", rs.getString("POS_DESCRIPTION"));
            return rt;
        });
    }

    ///////////////NEW METHOD LIST RECEIVING ALL BY DANI 12 DECEMBER 2023////
    @Override
    public List<Map<String, Object>> listReceivingAll(Map<String, String> balance) {
        String getCity = getCity(balance.get("outletCode"));

        String qry = "SELECT H.ROWID, H.OUTLET_CODE, H.STATUS, H.ORDER_NO, H.ORDER_TYPE, H.CD_SUPPLIER, TO_CHAR(H.ORDER_DATE, 'DD-Mon-YY') AS ORDER_DATE, "
                + " CASE WHEN H.ORDER_TO = '3' THEN 'Gudang' WHEN H.ORDER_TO = '2' THEN 'Outlet' WHEN H.ORDER_TO = '1' THEN 'Canvasing' ELSE 'Supplier' END as ORDER_TO, "
                + " case when G.DESCRIPTION is null and  m.outlet_name is null then s.supplier_name  "
                + "                when G.DESCRIPTION is null and s.supplier_name  is null then m.outlet_name else "
                + "               g.description end as NAMA_GUDANG "
                + " FROM T_ORDER_HEADER H "
                + " LEFT JOIN M_GLOBAL G ON G.CODE = H.CD_SUPPLIER AND G.COND = 'X_" + getCity + "' AND G.STATUS = 'A' "
                + " LEFT JOIN M_OUTLET M "
                + "               on H.cd_supplier = m.outlet_code "
                + " LEFT JOIN m_supplier S "
                + "               on H.cd_supplier = S.cd_supplier "
                + " WHERE H.STATUS = '0' "
                + " AND H.OUTLET_CODE = :outletCode  "
                + " ORDER BY ORDER_NO ASC, H.ROWID ASC ";

        Map prm = new HashMap();
        prm.put("outletCode", balance.get("outletCode"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("status", rs.getString("STATUS"));
                rt.put("orderNo", rs.getString("ORDER_NO"));
                rt.put("orderType", rs.getString("ORDER_TYPE"));
                rt.put("orderTo", rs.getString("ORDER_TO"));
                rt.put("cdSupplier", rs.getString("CD_SUPPLIER"));
                rt.put("gudangName", rs.getString("NAMA_GUDANG"));
                rt.put("orderDate", rs.getString("ORDER_DATE"));
                // rt.put("orderId", rs.getString("ORDER_ID"));

                return rt;
            }
        });
        return list;
    }

    /////////////////////////////DONE//////////////////////////////////
    ///////////////NEW METHOD LIST MPCS PLAN BY DONA 12 DECEMBER 2023////
    @Override
    public List<Map<String, Object>> listMpcsPlan(Map<String, String> balance) {
        String getCity = getCity(balance.get("outletCode"));

            String qry = "SELECT  OUTLET_CODE, MPCS_GROUP, DATE_MPCS, TIME_MPCS, QTY_PROJ, QTY_PROJ_CONV,  "
                    + "QTY_ACC_PROJ, QTY_SOLD, QTY_ACC_SOLD, QTY_VARIANCE, QTY_ACC_VARIANCE, USER_UPD,  "
                    + "DATE_UPD, TIME_UPD, SEQ_MPCS FROM t_summ_mpcs "
                    + "WHERE OUTLET_CODE =:outletCode   "
                    + "AND MPCS_GROUP =:mpcsGroup   "
                    + "AND DATE_MPCS =:dateMpcs "
                    + "AND TIME_MPCS >='000000' "
                    + "ORDER BY  "
                    + "OUTLET_CODE  ASC, MPCS_GROUP  ASC, DATE_MPCS  ASC, TIME_MPCS  ASC, SEQ_MPCS   "
                    + "ASC ";

        Map prm = new HashMap();
        prm.put("outletCode", balance.get("outletCode"));
        prm.put("mpcsGroup", balance.get("mpcsGroup"));
        prm.put("dateMpcs", balance.get("dateMpcs"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("outletCode", rs.getString("OUTLET_CODE"));
                rt.put("mpcsGroup", rs.getString("MPCS_GROUP"));
                rt.put("dateMpcs", rs.getString("DATE_MPCS"));
                rt.put("timeMpcs", rs.getString("TIME_MPCS"));
                rt.put("qtyProj", rs.getString("QTY_PROJ"));
                rt.put("qtyProjConv", rs.getString("QTY_PROJ_CONV"));
                rt.put("qtyAccProj", rs.getString("QTY_ACC_PROJ"));
                rt.put("qtySold", rs.getString("QTY_SOLD"));
                rt.put("qtyAccSold", rs.getString("QTY_ACC_SOLD"));
                rt.put("qtyVariance", rs.getString("QTY_VARIANCE"));
                rt.put("qtyAccVariance", rs.getString("QTY_ACC_VARIANCE"));
                rt.put("userUpd", rs.getString("USER_UPD"));
                rt.put("dateUpd", rs.getString("DATE_UPD"));
                rt.put("timeUpd", rs.getString("TIME_UPD"));
                rt.put("seqMpcs", rs.getString("SEQ_MPCS"));

                return rt;
            }
        });
        return list;
    }
    //////////////////////////////////DONE//////////////////////////////////////////////////////////

    // New Method List MPCS Production By Fathur 13 Dec 2023 //
    // Updated code: insert into t_summ_mpcs when mpcs group not available Fathur 26 Jan 2024//
    @Transactional
    @Override
    public List<Map<String, Object>> mpcsProductionList(Map<String, String> balance) {
        Map prm = new HashMap();
        prm.put("mpcsGroup", balance.get("mpcsGroup"));
        prm.put("dateMpcs", balance.get("dateMpcs"));
        prm.put("userUpd", balance.get("userUpd"));
         prm.put("outletCode", balance.get("outletCode"));
        String checkMPCSExist = "SELECT count(*) FROM T_SUMM_MPCS tsm WHERE tsm.MPCS_GROUP = :mpcsGroup AND tsm.DATE_MPCS = :dateMpcs";

        Integer rowCount = jdbcTemplate.queryForObject(checkMPCSExist, prm, Integer.class);
        if (rowCount.equals(0)) {
            String insertMpcsProduction = "INSERT INTO t_summ_mpcs ( "
                    + "	 SELECT sm.OUTLET_CODE,  "
                    + "		 :mpcsGroup AS MPCS_GROUP,  "
                    + "		 :dateMpcs AS DATE_MPCS,  "
                    + "		 sm.SEQ_MPCS,  "
                    + "		 sm.TIME_MPCS,  "
                    + "		 0 AS QTY_PROJ_CONV,  "
                    + "		 sm.UOM_PROJ_CONV,  "
                    + "		 0 AS QTY_PROJ,  "
                    + "		 sm.UOM_PROJ,  "
                    + "		 0 AS QTY_ACC_PROJ,  "
                    + "		 sm.UOM_ACC_PROJ,  "
                    + "		 ' ' AS DESC_PROD,  "
                    + "		 0 AS QTY_PROD,  "
                    + "		 sm.UOM_PROD,  "
                    + "		 0 AS QTY_ACC_PROD,  "
                    + "		 sm.UOM_ACC_PROD,  "
                    + "		 ' ' AS PROD_BY,  "
                    + "		 0 AS QTY_SOLD,  "
                    + "		 sm.UOM_SOLD,  "
                    + "		 0 AS QTY_ACC_SOLD,  "
                    + "		 sm.UOM_ACC_SOLD,  "
                    + "		 0 AS QTY_REJECT,  "
                    + "		 sm.UOM_REJECT,  "
                    + "		 0 AS QTY_ACC_REJECT,  "
                    + "		 sm.UOM_ACC_REJECT,  "
                    + "		 0 AS QTY_WASTAGE,  "
                    + "		 sm.UOM_WASTAGE,  "
                    + "		 0 AS QTY_ACC_WASTAGE,  "
                    + "		 sm.UOM_ACC_WASTAGE,  "
                    + "		 0 AS QTY_ONHAND,  "
                    + "		 sm.UOM_ONHAND,  "
                    + "		 0 AS QTY_ACC_ONHAND,  "
                    + "		 sm.UOM_ACC_ONHAND,  "
                    + "		 0 AS QTY_VARIANCE,  "
                    + "		 sm.UOM_VARIANCE,  "
                    + "		 0 AS QTY_ACC_VARIANCE,  "
                    + "		 sm.UOM_ACC_VARIANCE,  "
                    + "		 :userUpd AS USER_UPD,  "
                    + "		 TO_CHAR(SYSDATE, 'DD Mon YYYY') AS DATE_UPD,  "
                    + "		 TO_CHAR(SYSDATE, 'HHMMII') AS TIME_UPD,  "
                    + "		 0 AS QTY_IN,  "
                    + "		 0 AS QTY_OUT  "
                    + "	 FROM T_SUMM_MPCS sm LEFT JOIN M_OUTLET mo ON mo.OUTLET_CODE = sm.OUTLET_CODE WHERE sm.OUTLET_CODE = :outletCode AND sm.MPCS_GROUP = 'C02' AND sm.DATE_MPCS = mo.TRANS_DATE)";
            jdbcTemplate.update((insertMpcsProduction), prm);
            System.out.print("success insert");
        }

        String qry = "select to_char(to_date(c.TIME_MPCS, 'hh24miss'), 'hh24:mi') as TIME_MPCS, c.QTY_PROD, c.QTY_ACC_PROD, c.QTY_ACC_PROD, NVL(c.DESC_PROD, ' ') AS DESC_PROD, c.PROD_BY, "
                + "(to_char(c.DATE_UPD, 'YYYY-MM-dd') || ' ' || to_char(to_date(c.TIME_MPCS, 'hh24miss'), 'hh24:mi:ss')) AS DATE_UPD "
                + "from t_summ_mpcs c "
                + "where c.date_mpcs = :dateMpcs "
                + "AND c.MPCS_GROUP = :mpcsGroup";

        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, (ResultSet rs, int i) -> {
            Map<String, Object> rt = new HashMap<>();
            rt.put("timeMpcs", rs.getString("TIME_MPCS"));
            rt.put("qtyProd", rs.getString("QTY_PROD"));
            rt.put("qtyAccProd", rs.getString("QTY_ACC_PROD"));
            rt.put("descProd", rs.getString("DESC_PROD"));
            rt.put("prodBy", rs.getString("PROD_BY"));
            rt.put("dateUpd", rs.getString("DATE_UPD"));
            return rt;
        });
        return list;
    }
    // Done Method List MPCS Production //

    // New Method List MPCS Production By Fathur 13 Dec 2023 //
    @Override
    public List<Map<String, Object>> mpcsProductionDetail(Map<String, String> balance) {

        String qry = "SELECT HIST_SEQ, MPCS_GROUP, RECIPE_CODE, SEQ_MPCS, QUANTITY, TIME_UPD, DATE_UPD "
                + "FROM T_MPCS_HIST WHERE MPCS_GROUP = :mpcsGroup AND MPCS_DATE = :dateMpcs "
                + "AND TIME_UPD <= replace(:maxTime, '.' , '')||'00' "
                + "AND TIME_UPD >= replace(:minTime, '.' , '')||'00' "
                + "AND FRYER_TYPE != 'D'";

        Map prm = new HashMap();
        prm.put("mpcsGroup", balance.get("mpcsGroup"));
        prm.put("dateMpcs", balance.get("dateMpcs"));
        prm.put("maxTime", balance.get("maxTime"));
        prm.put("minTime", balance.get("minTime"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("mpcsGroup", rs.getString("MPCS_GROUP"));
                rt.put("recipeCode", rs.getString("RECIPE_CODE"));
                rt.put("seqMpcs", rs.getString("SEQ_MPCS"));
                rt.put("quantity", rs.getString("QUANTITY"));
                rt.put("timeUpd", rs.getString("TIME_UPD"));
                rt.put("dateUpd", rs.getString("DATE_UPD"));
                rt.put("histSeq", rs.getString("HIST_SEQ"));
                return rt;
            }
        });
        return list;
    }
    // Done Method List MPCS Production //

    // New Method MPCS Production Recipe - Fathur 13 Dec 2023 //
    // Update table source 8 Jan 2024
    @Override
    public List<Map<String, Object>> mpcsProductionRecipe(Map<String, String> balance) {
        String qry = "SELECT mpcs.ITEM_CODE, m.item_description, MPCS.QTY_STOCK, MPCS.UOM_STOCK "
                + "FROM M_RECIPE_DETAIL mpcs "
                + "left join m_item m on m.item_code = mpcs.item_code "
                + "WHERE mpcs.recipe_code = (SELECT RECIPE_CODE FROM M_RECIPE_HEADER mrh WHERE MPCS_GROUP = :mpcsGroup) ";
        List<Map<String, Object>> list = jdbcTemplate.query(qry, balance, (ResultSet rs, int i) -> {
            Map<String, Object> rt = new HashMap<>();
            rt.put("itemCode", rs.getString("ITEM_CODE"));
            rt.put("itemDescription", rs.getString("item_description"));
            rt.put("qty1", rs.getString("QTY_STOCK"));
            rt.put("uom1", rs.getString("UOM_STOCK"));
            return rt;
        });
        return list;
    }
    // Done MPCS Production Recipe //

    // New Method MPCS Production Product Result - Fathur 13 Dec 2023 //
    @Override
    public List<Map<String, Object>> mpcsProductionProductResult(Map<String, String> balance) {

        String qry = "select p.PRODUCT_CODE, p.QTY_STOCK, p.UOM_STOCK, m.ITEM_DESCRIPTION "
                + "from m_recipe_product p "
                + "LEFT JOIN M_ITEM m ON m.item_code = p.product_code "
                + "where recipe_code = :recipeCode ";

        Map prm = new HashMap();
        prm.put("recipeCode", balance.get("recipeCode"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, (ResultSet rs, int i) -> {
            Map<String, Object> rt = new HashMap<String, Object>();
            rt.put("productCode", rs.getString("PRODUCT_CODE"));
            rt.put("qtyStock", rs.getString("QTY_STOCK"));
            rt.put("uomStock", rs.getString("UOM_STOCK"));
            rt.put("productRemark", rs.getString("ITEM_DESCRIPTION"));

            return rt;
        });
        return list;
    }
    // Done MPCS Production Product Result //

    ///////////// NEW METHOD get order Detail From Inventory - Dani 19 Des 2023
    public List<Map<String, Object>> getOrderDetailFromInventory(Map<String, String> mapping) {
        String json = "";
        String total = "";
        Gson gson = new Gson();
        Map<String, Object> map1 = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            String url = this.warehouseEndpoint + "/get-delivery-order-from-inv";
            HttpPost post = new HttpPost(url);

            post.setHeader("Accept", "*/*");
            post.setHeader("Content-Type", "application/json");

            json = new Gson().toJson(mapping);
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);
            CloseableHttpResponse response = client.execute(post);
            System.out.println("json" + json);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            (response.getEntity().getContent())));
            StringBuilder content = new StringBuilder();
            String line;
            while (null != (line = br.readLine())) {
                content.append(line);
            }
            String result = content.toString();
            System.out.println("trans =" + result);

            map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
            }.getType());

            JsonObject job = gson.fromJson(result, JsonObject.class);
            JsonArray elem = job.getAsJsonArray("item");

            list = gson.fromJson(elem, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    ///////// NEW METHOD get Delivery Order outlet to outlet - Dani 22 Dec 2023
    public Map<String, Object> getDeliveryOrderOutletToOutlet(Map<String, String> mapping) {
        String json = "";
        String total = "";
        Gson gson = new Gson();
        Map<String, Object> map1 = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            String url = this.warehouseEndpoint + "/get-order-outlet-to-outlet";
            HttpPost post = new HttpPost(url);

            post.setHeader("Accept", "*/*");
            post.setHeader("Content-Type", "application/json");

            json = new Gson().toJson(mapping);
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);
            CloseableHttpResponse response = client.execute(post);
            System.out.println("json" + json);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            (response.getEntity().getContent())));
            StringBuilder content = new StringBuilder();
            String line;
            while (null != (line = br.readLine())) {
                content.append(line);
            }
            String result = content.toString();
            System.out.println("trans =" + result);

            map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
            }.getType());
            JsonObject job = gson.fromJson(result, JsonObject.class);
            JsonArray elem = job.getAsJsonArray("item");
            map1 = gson.fromJson(elem.get(0), new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map1;
    }

    ///////// NEW METHOD get Delivery Order Header List - Dani 20 Des 2023
    public List<Map<String, Object>> listDeliveryOrderHdr(Map<String, String> mapping) {
        mapping.put("status", "%" + (mapping.get("status") != null ? mapping.get("status") : "") + "%");
        String qry = "SELECT hdr.OUTLET_TO, mot.OUTLET_NAME as OUTLET_TO_NAME, hdr.OUTLET_CODE , hdr.REMARK, hdr.REQUEST_NO, hdr.DELIVERY_NO, "
                + " to_char(hdr.DELIVERY_DATE, 'DD-Mon-YYYY') as DELIVERY_DATE, CASE WHEN hdr.STATUS = '0' THEN 'Open' WHEN hdr.STATUS = '1' THEN 'Close' WHEN hdr.STATUS = '2' THEN 'Cancel' END as STATUS "
                + " FROM T_DEV_HEADER hdr LEFT JOIN M_OUTLET mot ON hdr.OUTLET_TO = mot.OUTLET_CODE "
                + " WHERE hdr.DELIVERY_DATE >=:dateStart AND hdr.DELIVERY_DATE <=:dateEnd"
                + " AND hdr.OUTLET_CODE = :outletCode "
                + " AND hdr.STATUS LIKE :status ORDER BY hdr.STATUS ASC, hdr.DELIVERY_DATE DESC, hdr.DATE_UPD DESC, hdr.TIME_UPD DESC";
        return jdbcTemplate.query(qry, mapping, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> row = new HashMap<>();
                row.put("outletToName", rs.getString("OUTLET_TO_NAME"));
                row.put("outletTo", rs.getString("OUTLET_TO"));
                row.put("outletCode", rs.getString("OUTLET_CODE"));
                row.put("deliveryNo", rs.getString("DELIVERY_NO"));
                row.put("deliveryDate", rs.getString("DELIVERY_DATE"));
                row.put("status", rs.getString("STATUS"));
                row.put("remark", rs.getString("REMARK"));
                row.put("requestNo", rs.getString("REQUEST_NO"));
                return row;
            }
        });
    }

    ///////// NEW METHOD get HO Outlet List - Dani 22 Des 2023
    public List<Map<String, Object>> listOutletHo(Map<String, String> mapping) {
        String qry = "SELECT a.region_code, a.outlet_code, a.area_code, a.initial_outlet, a.outlet_name, a.type, a.status "
                + "FROM M_OUTLET a "
                + "where a.type = 'HO' and a.INITIAL_OUTLET != 'HO' and a.status='A' ORDER BY a.OUTLET_NAME ASC";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("region", rs.getString("region_code"));
                rt.put("area", rs.getString("area_code"));
                rt.put("outlet", rs.getString("outlet_code"));
                rt.put("Initial", rs.getString("initial_outlet"));
                rt.put("Name", rs.getString("outlet_name"));
                rt.put("Type", rs.getString("type"));
                rt.put("Status", rs.getString("status"));
                return rt;
            }
        });
        return list;
    }

    // NEW METHOD To get delivery order by Dani 27 Dec 2023
    public Map<String, Object> getDeliveryOrder(Map<String, String> mapping) {

        String queryHeader = "SELECT hdr.OUTLET_TO, mot.OUTLET_NAME as OUTLET_TO_NAME, hdr.OUTLET_CODE , hdr.REMARK, hdr.REQUEST_NO, hdr.DELIVERY_NO, "
                + " to_char(hdr.DELIVERY_DATE, 'DD-Mon-YYYY') as DELIVERY_DATE, CASE WHEN hdr.STATUS = '0' THEN 'Open' WHEN hdr.STATUS = '1' THEN 'Close' WHEN hdr.STATUS = '2' THEN 'Cancel' END as STATUS "
                + " FROM T_DEV_HEADER hdr LEFT JOIN M_OUTLET mot ON hdr.OUTLET_TO = mot.OUTLET_CODE "
                + " WHERE hdr.OUTLET_CODE = :outletCode "
                + " AND hdr.OUTLET_TO = :outletTo "
                + " AND hdr.DELIVERY_NO = :deliveryNo "
                + " AND hdr.REQUEST_NO = :requestNo ";
        Map<String, Object> dlvr = jdbcTemplate.queryForObject(queryHeader, mapping, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> row = new HashMap<>();
                row.put("outletToName", rs.getString("OUTLET_TO_NAME"));
                row.put("outletTo", rs.getString("OUTLET_TO"));
                row.put("outletCode", rs.getString("OUTLET_CODE"));
                row.put("deliveryNo", rs.getString("DELIVERY_NO"));
                row.put("deliveryDate", rs.getString("DELIVERY_DATE"));
                row.put("status", rs.getString("STATUS"));
                row.put("remark", rs.getString("REMARK"));
                row.put("requestNo", rs.getString("REQUEST_NO"));
                return row;
            }
        });

        String queryDtls = "SELECT B.ITEM_DESCRIPTION, B.CONV_WAREHOUSE, C.OUTLET_NAME AS OUTLET_TO_NAME, A.OUTLET_CODE, A.OUTLET_TO, A.REQUEST_NO, "
                + " A.DELIVERY_NO, A.ITEM_CODE, A.QTY_PURCH, A.UOM_PURCH, A.QTY_STOCK, A.UOM_STOCK, A.TOTAL_QTY, A.USER_UPD, TO_CHAR(A.DATE_UPD, 'DD-MON-YYYY') AS DATE_UPD, A.TIME_UPD "
                + " FROM T_DEV_DETAIL A LEFT JOIN M_ITEM B ON A.ITEM_CODE = B.ITEM_CODE LEFT JOIN M_OUTLET C ON A.OUTLET_TO = C.OUTLET_CODE "
                + " WHERE A.REQUEST_NO =:requestNo AND A.DELIVERY_NO =:deliveryNo AND A.OUTLET_TO = :outletTo ";
        List<Map<String, Object>> dtls = jdbcTemplate.query(queryDtls, dlvr, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> map = new HashMap<>();
                map.put("convWarehouse", rs.getString("CONV_WAREHOUSE"));
                map.put("outletToName", rs.getString("OUTLET_TO_NAME"));
                map.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                map.put("outletCode", rs.getString("OUTLET_CODE"));
                map.put("outletTo", rs.getString("OUTLET_TO"));
                map.put("requestNo", rs.getString("REQUEST_NO"));
                map.put("deliveryNo", rs.getString("DELIVERY_NO"));
                map.put("itemCode", rs.getString("ITEM_CODE"));
                map.put("qtyPurch", rs.getBigDecimal("QTY_PURCH"));
                map.put("uomPurch", rs.getString("UOM_PURCH"));
                map.put("qtyStock", rs.getBigDecimal("QTY_STOCK"));
                map.put("uomStock", rs.getString("UOM_STOCK"));
                map.put("totalQty", rs.getBigDecimal("TOTAL_QTY"));
                map.put("userUpd", rs.getString("USER_UPD"));
                map.put("dateUpd", rs.getString("DATE_UPD"));
                map.put("timeUpd", rs.getString("TIME_UPD"));
                return map;
            }
        });
        dlvr.put("details", dtls);
        return dlvr;
    }

    ///// NEW METHOD TO GET LIST ORDER OUTLET TO OUTLET FROM WAREHOUSE BY DANI 28 DEC 2023
    public List<Map<String, Object>> getListOrderOutletHeaderWarehouse(Map<String, String> mapping) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        String json = "";
        String total = "";
        Gson gson = new Gson();
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            String url = this.warehouseEndpoint + "/get-order-outlet-to-outlet-header";
            HttpPost post = new HttpPost(url);

            post.setHeader("Accept", "*/*");
            post.setHeader("Content-Type", "application/json");

            json = new Gson().toJson(mapping);
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);
            CloseableHttpResponse response = client.execute(post);
            System.out.println("json" + json);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            (response.getEntity().getContent())));
            StringBuilder content = new StringBuilder();
            String line;
            while (null != (line = br.readLine())) {
                content.append(line);
            }
            String result = content.toString();
            System.out.println("trans =" + result);

            map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
            }.getType());

            JsonObject job = gson.fromJson(result, JsonObject.class);
            JsonArray elem = job.getAsJsonArray("item");

            list = gson.fromJson(elem, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //////// NEW METHOD to get detail outlet to outlet from warehouse  BY DANI 28 DEC 2023
    public Map<String, Object> getOrderOutletWarehouse(Map<String, String> mapping) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        String json = "";
        String total = "";
        Gson gson = new Gson();
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            String url = this.warehouseEndpoint + "/get-order-outlet-to-outlet";
            HttpPost post = new HttpPost(url);

            post.setHeader("Accept", "*/*");
            post.setHeader("Content-Type", "application/json");

            json = new Gson().toJson(mapping);
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);
            CloseableHttpResponse response = client.execute(post);
            System.out.println("json" + json);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            (response.getEntity().getContent())));
            StringBuilder content = new StringBuilder();
            String line;
            while (null != (line = br.readLine())) {
                content.append(line);
            }
            String result = content.toString();
            System.out.println("trans =" + result);

            map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
            }.getType());

            JsonObject job = gson.fromJson(result, JsonObject.class);
            JsonArray element = job.getAsJsonArray("item");
            JsonObject elem = element.get(0).getAsJsonObject();
            JsonArray details = elem.getAsJsonArray("details");
            details.forEach(dtl -> {
                String itemCode = dtl.getAsJsonObject().getAsJsonPrimitive("itemCode").getAsString();
                String strq = "SELECT CONV_STOCK, ITEM_DESCRIPTION FROM M_ITEM WHERE ITEM_CODE = :itemCode";
                Map<String, String> map = new HashMap<>();
                map.put("itemCode", itemCode);
                Map<String, Object> a = jdbcTemplate.queryForObject(strq, map, new DynamicRowMapper());
                dtl.getAsJsonObject().addProperty("itemDescription", (String) a.get("itemDescription"));
                dtl.getAsJsonObject().addProperty("convWarehouse", (BigDecimal) a.get("convStock"));
            });
            list = gson.fromJson(element, new TypeToken<List<Map<String, Object>>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.get(0);
    }

    // Get Order Entry status from inv by Fathur 29 Dec 2023 // 
    @Override
    public Map<String, Object> getOrderEntryStatusFromInv(Map<String, String> mapping) {
        Map<String, Object> map1 = new HashMap<String, Object>();
        Gson gson = new Gson();
        String json = "";
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            String url = this.warehouseEndpoint + "/get-order-entry-status";
            HttpPost post = new HttpPost(url);
            post.setHeader("Accept", "*/*");
            post.setHeader("Content-Type", "application/json");

            json = new Gson().toJson(mapping);
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);
            CloseableHttpResponse response = client.execute(post);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            (response.getEntity().getContent())));
            StringBuilder content = new StringBuilder();
            String line;
            while (null != (line = br.readLine())) {
                content.append(line);
            }
            String result = content.toString();

            map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map1;
    }
    // Done get Order Entry status from inv // 

    //////// NEW METHOD to get list daftar menu by Rafi 29 DEC 2023
    @Override
    public List<Map<String, Object>> getListDaftarMenuReport() {
        String query = "SELECT DISTINCT (mmgl.ORDER_TYPE) AS ORDER_TYPE, mg.DESCRIPTION  FROM M_MENU_GROUP_LIMIT mmgl "
                + "LEFT JOIN M_GLOBAL mg ON mmgl.ORDER_TYPE = mg.CODE AND mg.COND = 'ORDER_TYPE' "
                + "ORDER BY ORDER_TYPE ASC ";

        return jdbcTemplate.query(query, new HashMap(), new DynamicRowMapper());
    }

    //////// NEW METHOD Digunakan untuk ambil data outlet di halaman login by M Joko - 4 Jan 2024
    @Override
    public List<Map<String, Object>> outletInfo(String outletCode) {
        String envBe = appUtil.getOrDefault("app.env", "production");
        String qry = """
                    SELECT 
                        region_code, 
                        outlet_code, 
                        outlet_name, 
                        type, 
                        address_1, 
                        address_2, 
                        city, 
                        post_code, 
                        phone, 
                        fax, 
                        TO_CHAR(TRANS_DATE, 'DD-MM-YYYY') AS TRANS_DATE,
                        area_code,
                        mg.description as area_description,
                        initial_outlet,
                        rsc_code,
                        tax_charge,
                        outlet_24_hour,
                        CASE 
                            WHEN outlet_name LIKE '%TACOBELL%' THEN 'TACOBELL'
                            ELSE 'KFC'
                        END AS brand,
                        :envBe AS env_be
                     
                    FROM M_OUTLET mo
                    JOIN M_GLOBAL mg ON mo.area_code = mg.code AND mg.cond = 'AREACODE'
                    WHERE outlet_code = :outletcode
                     """;
        Map prm = new HashMap();
        prm.put("outletcode", outletCode);
        prm.put("envBe", envBe);
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new DynamicRowMapper());
        return list;
    }

    /////// NEW METHOD to get list mpcs group by Dani 4 Januari 2024
    @Override
    public List<Map<String, Object>> listMpcsGroup(Map<String, String> mapping) {
        String query = "SELECT MMH.MPCS_GROUP , MMH.DESCRIPTION , MMH.QTY_CONV, MMH.STATUS FROM M_MPCS_HEADER mmh "
                + " WHERE MMH.status = 'A' AND OUTLET_CODE = :outletCode ORDER BY MMH.MPCS_GROUP ASC";
        return jdbcTemplate.query(query, mapping, new DynamicRowMapper());
    }

    /////// NEW METHOD to get mpcs query result by Dani 4 Januari 2024
    @Override
    public Map<String, Object> listMpcsQueryResult(Map<String, String> mapping) {
        Map<String, Object> response = new HashMap<>();
        String query = "SELECT * FROM T_SUMM_MPCS WHERE OUTLET_CODE = :outletCode AND DATE_MPCS = :dateMpcs AND MPCS_GROUP = :mpcsGroup ORDER BY SEQ_MPCS ASC";
        List<Map<String, Object>> mpcsQuery = jdbcTemplate.query(query, mapping, new RowMapper() {
            @Override
            @Nullable
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> map = new HashMap<>();
                map.put("outletCode", rs.getString("OUTLET_CODE"));
                map.put("mpcsGroup", rs.getString("MPCS_GROUP"));
                map.put("dateMpcs", rs.getObject("DATE_MPCS", LocalDateTime.class));
                map.put("seqMpcs", rs.getInt("SEQ_MPCS"));
                map.put("timeMpcs", rs.getString("TIME_MPCS"));
                map.put("qtyProjConv", rs.getBigDecimal("QTY_PROJ_CONV").setScale(4));
                map.put("uomProjConv", rs.getString("UOM_PROJ_CONV"));
                map.put("qtyProj", rs.getBigDecimal("QTY_PROJ").setScale(4));
                map.put("uomProj", rs.getString("UOM_PROJ"));
                map.put("qtyAccProj", rs.getBigDecimal("QTY_ACC_PROJ").setScale(4));
                map.put("uomAccProj", rs.getString("UOM_ACC_PROJ"));
                map.put("descProd", rs.getString("DESC_PROD"));
                map.put("qtyProd", rs.getBigDecimal("QTY_PROD").setScale(4));
                map.put("uomProd", rs.getString("UOM_PROD"));
                map.put("qtyAccProd", rs.getBigDecimal("QTY_ACC_PROD").setScale(4));
                map.put("uomAccProd", rs.getString("UOM_ACC_PROD"));
                map.put("prodBy", rs.getString("PROD_BY"));
                map.put("qtySold", rs.getBigDecimal("QTY_SOLD").setScale(4));
                map.put("uomSold", rs.getString("UOM_SOLD"));
                map.put("qtyAccSold", rs.getBigDecimal("QTY_ACC_SOLD").setScale(4));
                map.put("uomAccSold", rs.getString("UOM_ACC_SOLD"));
                map.put("qtyReject", rs.getBigDecimal("QTY_REJECT").setScale(4));
                map.put("uomReject", rs.getString("UOM_REJECT"));
                map.put("qtyAccReject", rs.getBigDecimal("QTY_ACC_REJECT").setScale(4));
                map.put("uomAccReject", rs.getString("UOM_ACC_REJECT"));
                map.put("qtyWastage", rs.getBigDecimal("QTY_WASTAGE").setScale(4));
                map.put("uomWastage", rs.getString("UOM_WASTAGE"));
                map.put("qtyAccWastage", rs.getBigDecimal("QTY_ACC_WASTAGE").setScale(4));
                map.put("uomAccWastage", rs.getString("UOM_ACC_WASTAGE"));
                map.put("qtyOnhand", rs.getBigDecimal("QTY_ONHAND").setScale(4));
                map.put("uomOnhand", rs.getString("UOM_ONHAND"));
                map.put("qtyAccOnhand", rs.getBigDecimal("QTY_ACC_ONHAND").setScale(4));
                map.put("uomAccOnhand", rs.getString("UOM_ACC_ONHAND"));
                map.put("qtyVariance", rs.getBigDecimal("QTY_VARIANCE").setScale(4));
                map.put("uomVariance", rs.getString("UOM_VARIANCE"));
                map.put("qtyAccVariance", rs.getBigDecimal("QTY_ACC_VARIANCE").setScale(4));
                map.put("uomAccVariance", rs.getString("UOM_ACC_VARIANCE"));
                map.put("userUpd", rs.getString("USER_UPD"));
                map.put("dateUpd", rs.getObject("DATE_UPD", LocalDateTime.class));
                map.put("timeUpd", rs.getString("TIME_UPD"));
                map.put("qtyIn", rs.getBigDecimal("QTY_IN").setScale(4));
                map.put("qtyOut", rs.getBigDecimal("QTY_OUT").setScale(4));
                return map;
            }
        });

        response.put("mpcsQueryResult", mpcsQuery);
        if (mpcsQuery.size() > 1) {
            Map<String, Object> summaryDay = new HashMap();
            Map<String, Object> lastData = mpcsQuery.get(mpcsQuery.size() - 1);
            Map<String, Object> summaryDel = jdbcTemplate.queryForObject("SELECT count(1) AS COUNT_DELETE, COALESCE(sum(tmh.QUANTITY), 0) AS QTY_DELETE FROM T_MPCS_HIST tmh WHERE MPCS_GROUP =:mpcsGroup AND MPCS_DATE =:dateMpcs", mapping, new DynamicRowMapper());
            summaryDay.put("summaryProjection", ((BigDecimal) lastData.get("qtyAccProj")).setScale(2));
            summaryDay.put("summaryOnHand", ((BigDecimal) lastData.get("qtyOnhand")).setScale(2));
            summaryDay.put("summaryVariance", ((BigDecimal) lastData.get("qtyAccVariance")).setScale(2));
            summaryDay.put("summaryCooked", ((BigDecimal) lastData.get("qtyAccProd")).setScale(2));
            summaryDay.put("summarySold", ((BigDecimal) lastData.get("qtyAccSold")).setScale(2));
            summaryDay.put("summaryReject", ((BigDecimal) lastData.get("qtyAccReject")).setScale(2));
            summaryDay.put("qtyDelete", ((BigDecimal) summaryDel.get("qtyDelete")).setScale(2));
            summaryDay.put("countDelete", ((BigDecimal) summaryDel.get("countDelete")));
            response.put("mpcsSummary", summaryDay);
        } else {
            Map<String, Object> summaryDay = new HashMap();
            summaryDay.put("summaryProjection", BigDecimal.ZERO.setScale(2));
            summaryDay.put("summaryOnHand", BigDecimal.ZERO.setScale(2));
            summaryDay.put("summaryVariance", BigDecimal.ZERO.setScale(2));
            summaryDay.put("summaryCooked", BigDecimal.ZERO.setScale(2));
            summaryDay.put("summarySold", BigDecimal.ZERO.setScale(2));
            summaryDay.put("summaryReject", BigDecimal.ZERO.setScale(2));
            summaryDay.put("qtyDelete", BigDecimal.ZERO.setScale(2));
            summaryDay.put("countDelete", BigDecimal.ZERO);
            response.put("mpcsSummary", summaryDay);
        }
        return response;
    }

    /////// NEW METHOD to get list Menu Aplikasi by M Joko 8 Januari 2024
    @Override
    public List<Map<String, Object>> listMenuApplication(Map<String, String> mapping) {
        String query = """
            SELECT
                mg.APPL_ID,
                mg.MENU_ID,
                mg.UPLINE_ID,
                mg.GROUP_ID,
                REGEXP_REPLACE(m.DESCRIPTION, 'Master ', '', 1, 0, 'i') AS DESCRIPTION,
                m.LEVEL_MENU,
                mg.ACCESSR,
                mg.ACCESSW 
            FROM
                M_MENUGRP mg
                LEFT JOIN M_MENU m ON mg.APPL_ID = m.APPL_ID AND mg.MENU_ID = m.MENU_ID
            WHERE
            mg.APPL_ID IN ('MA','PS','IV')
                AND mg.TYPE_MENU = 'MENU'
                AND mg.GROUP_ID = 'SUPERVISOR'
                AND m.DESCRIPTION IS NOT NULL
                AND mg.LEVEL_MENU >= 0
                AND (UPPER(m.DESCRIPTION) IN ('OFFICE','MASTER', 'MASTER USER', 'MASTER OUTLET', 'MASTER MENU', 'MASTER PRICE', 'MASTER FRYER TYPE', 'MASTER GROUP ITEM', 'MASTER SUPPLIER', 'MASTER ITEM', 'MASTER RECIPE', 'MPCS', 'MASTER ITEM COST', 'MASTER SALES RECIPE', 'MASTER GLOBAL', 'KIRIM DAN TERIMA DATA')
                OR UPPER(m.DESCRIPTION) IN ('TRANSAKSI', 'ORDER ENTRY', 'STOCK OPNAME', 'RECEIVING', 'DELIVERY ORDER OUTLET', 'RETURN ORDER', 'WASTAGE', 'MPCS', 'QUERY STOCK CARD', 'MPCS QUERY')
                OR UPPER(m.DESCRIPTION) IN ('POINT OF SALES', 'POS', 'EOD', 'QUERY BILL', 'QUERY SALES')
                )
            ORDER BY
                MENU_ID ASC
                       """;
        if (mapping.get("outletBrand").equalsIgnoreCase("TACOBELL")) {
            // todo: query tacobell
            query = """
                    SELECT 
                        CASE 
                            WHEN DESCRIPTION = 'Report Menu dan Detail Modifier' THEN 'Menu and Detail'
                            ELSE Description
                        END AS description,
                        TYPE_MENU AS CODE
                    FROM 
                        M_MENUDTL
                    WHERE 
                        DESCRIPTION IN (
                            'Sales by Date',
                            'Sales by Time',
                            'Sales by Item',
                            'Report Menu dan Detail Modifier',
                            'Summary Sales by Item Code',
                            'Report Stock Card'
                        )
                        AND TYPE_MENU = 'REPORT'
                        AND STATUS = 'A'
                        AND APLIKASI = 'POS'
                    ORDER BY 
                        description DESC;
                    """;
        }
        return jdbcTemplate.query(query, mapping, new DynamicRowMapper());
    }

    /////// NEW METHOD to get list Customer Name report DP by Dani 9 Januari 2024
    @Override
    public List<Map<String, Object>> listCustomerNameReportDp() {
        String query = "SELECT CUSTOMER_NAME, MAX(BOOK_DATE) AS MX FROM T_POS_BOOK GROUP BY CUSTOMER_NAME ORDER BY MX ASC";
        return jdbcTemplate.query(query, new HashMap(), new DynamicRowMapper());
    }

    /////// NEW METHOD to get list order type report DP by Dani 9 Januari 2024
    @Override
    public List<Map<String, Object>> listOrderTypeReportDp() {
        String query = "SELECT DISTINCT (TPB.ORDER_TYPE) , MG.DESCRIPTION  FROM T_POS_BOOK tpb LEFT JOIN M_GLOBAL mg ON MG.COND  = 'ORDER_TYPE' AND MG.CODE = TPB.ORDER_TYPE ";
        return jdbcTemplate.query(query, new HashMap(), new DynamicRowMapper());
    }

    //////// NEW METHOD to get list daftar menu by Rafi 9 Jan 2024
    @Override
    public List<Map<String, Object>> getListItemDetailReport() {
        String query = "SELECT mg.CODE, mg.DESCRIPTION FROM M_GLOBAL mg WHERE mg.COND = 'ITEM' ORDER BY mg.CODE ASC ";

        return jdbcTemplate.query(query, new HashMap(), new DynamicRowMapper());
    }

    /////// NEW METHOD to mengambil data user absensi by id by M Joko 16 Jan 2024
    @Override
    public List<Map<String, Object>> getIdAbsensi(Map<String, String> mapping) {
        String query = """
            SELECT 
             to_char(SYSDATE,'DD-FMMONTH-YYYY') AS DATE_NOW, to_char(SYSDATE,'HH24MISS') AS TIME_NOW,
             s.REGION_CODE, s.OUTLET_CODE, s.STAFF_CODE, s.STAFF_NAME, s.STAFF_FULL_NAME, s.ID_CARD, 
             s.SEX, TO_CHAR(s.DATE_OF_BIRTH,'YYYY-MM-DD') AS DATE_OF_BIRTH, s.POSITION, s.GROUP_ID, s.STATUS, s.RIDER_FLAG,
             NVL(m.DAY_SEQ,0) AS DAY_SEQ, NVL(m.SEQ_NO,0) AS SEQ_NO, NVL(m.TIME_ABSEN,0) AS TIME_ABSEN
            FROM M_STAFF s
            LEFT JOIN T_ABSENSI m ON s.OUTLET_CODE = m.OUTLET_CODE AND s.STAFF_CODE = m.STAFF_ID AND m.DATE_ABSEN = to_char(SYSDATE,'DD-MON-YYYY')
            WHERE STAFF_CODE = :staffCode
            ORDER BY m.DAY_SEQ, m.SEQ_NO
                       """;
        return jdbcTemplate.query(query, mapping, new DynamicRowMapper());
    }
}
