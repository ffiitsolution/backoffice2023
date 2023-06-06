/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.dao.impl;

import com.ffi.api.backoffice.dao.ViewDao;
import com.ffi.api.backoffice.model.ParameterLogin;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dwi Prasetyo
 */
@Repository
public class ViewDoaImpl implements ViewDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ViewDoaImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> loginJson(ParameterLogin ref) {
        String qry = "select S.REGION_CODE,G.DESCRIPTION REG_NAME,S.OUTLET_CODE,O.OUTLET_NAME,S.STAFF_CODE,S.STAFF_NAME,S.STAFF_FULL_NAME,S.ID_CARD,S.POSITION,S.GROUP_ID,O.CITY \n"
                + "from M_STAFF S\n"
                + "JOIN M_OUTLET O ON O.OUTLET_CODE = S.OUTLET_CODE\n"
                + "JOIN M_GLOBAL G ON G.CODE = S.REGION_CODE AND G.COND = 'REG_OUTLET' \n"
                + "where S.STAFF_CODE = :staffCode and S.PASSWORD = :pass and S.OUTLET_CODE = :outletCode and S.STATUS = 'A'";
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
                + " order by cd_Supplier asc";
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
        String qry = "SELECT  a.CD_SUPPLIER, a.ITEM_CODE,b.item_description,a.STATUS, a.USER_UPD, a.DATE_UPD,a.TIME_UPD FROM M_ITEM_SUPPLIER A \n"
                + "left join m_item B \n"
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

    @Override
    public List<Map<String, Object>> listMasterItem(Map<String, String> balance) {
        String qry = "select ITEM_CODE||' -'||ITEM_DESCRIPTION as name,item_description, item_code from m_item where Status='A' and Flag_Finished_Good='N'";
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

    @Override
    public List<Map<String, Object>> listItemSupplier(Map<String, String> balance) {

        String qry = "select  a.CD_SUPPLIER,a.item_code,b.item_description,"
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
        String qry = "SELECT  A.OUTLET_CODE,B.OUTLET_NAME, A.FRYER_TYPE, A.FRYER_TYPE_SEQ, A.STATUS, A.FRYER_TYPE_RESET,\n"
                + "A.FRYER_TYPE_SEQ_CNT, A.FRYER_TYPE_CNT, A.USER_UPD, A.DATE_UPD, A.TIME_UPD  FROM\n"
                + "M_MPCS_DETAIL A LEFT JOIN M_OUTLET B\n"
                + "ON A.OUTLET_CODE=B.OUTLET_CODE WHERE A.OUTLET_CODE =:outletCode  AND A.FRYER_TYPE = :fryerType ORDER BY\n"
                + "A.OUTLET_CODE  ASC, A.FRYER_TYPE  ASC,A. FRYER_TYPE_SEQ  ASC ";
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
    @Override
    public List<Map<String, Object>> listMenuGroup(Map<String, String> ref) {
        String qry = "SELECT  \n"
                + "    M.MENU_GROUP_CODE,\n"
                + "    G.DESCRIPTION AS MENU_GROUP\n"
                + "FROM M_MENU_GROUP M \n"
                + "JOIN M_GLOBAL G \n"
                + "ON M.MENU_GROUP_CODE = G.CODE \n"
                + "WHERE G.COND = 'GROUP' AND M.OUTLET_CODE LIKE :outlet_code \n"
                + "ORDER BY MENU_GROUP_CODE";
        Map prm = new HashMap();
        prm.put("outlet_code", "%" + ref.get("outlet_code") + "%");
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("menugroupcode", rs.getString("menu_group_code"));
                rt.put("menugroup", rs.getString("menu_group"));
                return rt;
            }
        });
        return list;

    }

    @Override
    public List<Map<String, Object>> listPrice(Map<String, String> ref) {
        String qry = "SELECT \n"
                + "    G1.CODE AS ITEM_CODE,\n"
                + "    G1.DESCRIPTION AS ITEM_NAME,\n"
                + "    MP.PRICE AS PRICE,\n"
                + "    MP.PRICE_TYPE_CODE AS PRICE_TYPE_CODE,\n"
                + "    G2.DESCRIPTION AS ORDER_DESCRIPTION\n"
                + "FROM M_GLOBAL G1\n"
                + "LEFT JOIN M_PRICE MP\n"
                + "ON G1.CODE = MP.MENU_ITEM_CODE\n"
                + "LEFT JOIN M_OUTLET_PRICE OP\n"
                + "ON OP.PRICE_TYPE_CODE = MP.PRICE_TYPE_CODE AND OP.OUTLET_CODE LIKE '%%'\n"
                + "LEFT JOIN M_GLOBAL G2\n"
                + "ON OP.ORDER_TYPE = G2.CODE AND G2.COND = 'ORDER_TYPE'\n"
                + "WHERE G1.COND = 'ITEM' AND G1.STATUS = 'A' AND MP.PRICE_TYPE_CODE IN (SELECT PRICE_TYPE_CODE FROM M_OUTLET_PRICE)\n"
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
        String qry = "SELECT\n"
                + "    MMI.MENU_ITEM_CODE,\n"
                + "    MI.ITEM_DESCRIPTION,\n"
                + "    -- MMI.MENU_GROUP_CODE,\n"
                + "    MG.DESCRIPTION AS MENU_GROUP_NAME,\n"
                + "    MP.PRICE,\n"
                + "    MP.PRICE_TYPE_CODE AS PRICE_TYPE_CODE,\n"
                + "    MMI.TAXABLE\n"
                + "    -- MG.DESCRIPTION AS ORDER_DESCRIPTION\n"
                + "FROM M_MENU_ITEM MMI\n"
                + "LEFT JOIN M_ITEM MI\n"
                + "ON MMI.MENU_ITEM_CODE = MI.ITEM_CODE \n"
                + "LEFT JOIN M_PRICE MP\n"
                + "ON MMI.MENU_ITEM_CODE = MP.MENU_ITEM_CODE\n"
                + "LEFT JOIN M_OUTLET_PRICE MOP\n"
                + "ON MOP.PRICE_TYPE_CODE = MP.PRICE_TYPE_CODE \n"
                + "LEFT JOIN M_GLOBAL MG\n"
                + "ON MMI.MENU_GROUP_CODE = MG.CODE AND MG.COND = 'GROUP'\n"
                + "WHERE MMI.MENU_GROUP_CODE LIKE :Menu_Group_Code \n"
                + "AND MMI.MENU_ITEM_CODE LIKE '%%'\n"
                + "AND MMI.STATUS = 'A' \n"
                + "AND MI.STATUS = 'A' \n"
                + "AND MMI.OUTLET_CODE LIKE :Outlet_Code \n"
                + "AND MOP.OUTLET_CODE LIKE :Outlet_Code \n"
                + "AND MOP.ORDER_TYPE = 'ETA'\n"
                + "ORDER BY MMI.MENU_ITEM_CODE";
        Map prm = new HashMap();
        prm.put("Outlet_Code", "%" + ref.get("outlet_code") + "%");
        prm.put("Menu_Group_Code", "%" + ref.get("menu_group_code") + "%");
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("menuitemcode", rs.getString("menu_item_code"));
                rt.put("itemdescription", rs.getString("item_description"));
                rt.put("menugroupname", rs.getString("menu_group_name"));
                rt.put("price", rs.getString("price"));
                rt.put("pricetypecode", rs.getString("price_type_code"));
                rt.put("taxable", rs.getString("taxable"));
                return rt;
            }
        });
        return list;

    }

    @Override
    public List<Map<String, Object>> listItemDetail(Map<String, String> ref) {
        String qry = "SELECT \n"
                + "    MMI.MENU_ITEM_CODE, \n"
                + "    MI.ITEM_DESCRIPTION, \n"
                + "    -- MMI.MENU_GROUP_CODE, \n"
                + "    MG2.DESCRIPTION AS MENU_GROUP_NAME, \n"
                + "    MP.PRICE, \n"
                + "    MP.PRICE_TYPE_CODE AS PRICE_TYPE_CODE, \n"
                + "    MG.DESCRIPTION AS ORDER_DESCRIPTION, \n"
                + "    CASE WHEN MENU_SET = 'N' AND MODIFIER_GROUP1_CODE = ' ' AND MODIFIER_GROUP2_CODE = ' ' AND MODIFIER_GROUP3_CODE = ' ' AND MODIFIER_GROUP4_CODE = ' ' THEN 'N' \n"
                + "     WHEN MENU_SET = 'Y' AND MODIFIER_GROUP1_CODE = ' ' AND MODIFIER_GROUP2_CODE = ' ' AND MODIFIER_GROUP3_CODE = ' ' AND MODIFIER_GROUP4_CODE = ' '  THEN 'N' \n"
                + "     ELSE 'Y' \n"
                + "    END AS MODIFIER_STATUS \n"
                + "FROM M_MENU_ITEM MMI \n"
                + "LEFT JOIN M_ITEM MI \n"
                + "ON MMI.MENU_ITEM_CODE = MI.ITEM_CODE \n"
                + "LEFT JOIN M_PRICE MP \n"
                + "ON MMI.MENU_ITEM_CODE = MP.MENU_ITEM_CODE \n"
                + "LEFT JOIN M_OUTLET_PRICE MOP \n"
                + "ON MOP.PRICE_TYPE_CODE = MP.PRICE_TYPE_CODE \n"
                + "LEFT JOIN M_GLOBAL MG \n"
                + "ON MOP.ORDER_TYPE = MG.CODE AND MG.COND = 'ORDER_TYPE' \n"
                + "LEFT JOIN M_GLOBAL MG2 \n"
                + "ON MMI.MENU_GROUP_CODE = MG2.CODE AND MG2.COND = 'GROUP' \n"
                + "WHERE MMI.MENU_GROUP_CODE LIKE :Menu_Group_Code \n"
                + "AND MMI.MENU_ITEM_CODE = :Menu_Item_Code \n"
                + "AND MMI.STATUS = 'A' \n"
                + "AND MI.STATUS = 'A' \n"
                + "AND MMI.OUTLET_CODE LIKE :Outlet_Code \n"
                + "AND MOP.OUTLET_CODE LIKE :Outlet_Code \n"
                + "ORDER BY MMI.MENU_ITEM_CODE";
        Map prm = new HashMap();
        prm.put("Outlet_Code", "%" + ref.get("outlet_code") + "%");
        prm.put("Menu_Group_Code", "%" + ref.get("menu_group_code") + "%");
        prm.put("Menu_Item_Code", ref.get("menu_item_code"));

        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("menuitemcode", rs.getString("menu_item_code"));
                rt.put("itemdescription", rs.getString("item_description"));
                rt.put("menugroupname", rs.getString("menu_group_name"));
                rt.put("price", rs.getString("price"));
                rt.put("pricetypecode", rs.getString("price_type_code"));
                rt.put("orderdescription", rs.getString("order_description"));
                rt.put("modifierstatus", rs.getString("modifier_status"));
                return rt;
            }
        });
        return list;

    }

    @Override
    public List<Map<String, Object>> listModifier(Map<String, String> ref) {
        String qry = "SELECT \n"
                + "    MMI.MENU_ITEM_CODE, \n"
                + "    MI.ITEM_DESCRIPTION, \n"
                + "    --- MMI.MENU_GROUP_CODE, \n"
                + "    --- MMI.MODIFIER_GROUP1_CODE AS MODIFIER_GROUP_CODE, \n"
                + "    MOD.MODIFIER_ITEM_CODE, \n"
                + "    MI2.ITEM_DESCRIPTION AS MODIFIER_ITEM_NAME \n"
                + "FROM M_MENU_ITEM MMI \n"
                + "LEFT JOIN M_ITEM MI \n"
                + "ON MMI.MENU_ITEM_CODE = MI.ITEM_CODE \n"
                + "LEFT JOIN M_MODIFIER_ITEM MOD \n"
                + "ON MMI.MODIFIER_GROUP1_CODE = MOD.MODIFIER_GROUP_CODE \n"
                + "LEFT JOIN M_ITEM MI2 \n"
                + "ON MOD.MODIFIER_ITEM_CODE = MI2.ITEM_CODE \n"
                + "WHERE MMI.MENU_GROUP_CODE LIKE :Menu_Group_Code \n"
                + "AND MMI.MENU_ITEM_CODE = :Menu_Item_Code \n"
                + "AND MMI.STATUS = 'A' \n"
                + "AND MI.STATUS = 'A' \n"
                + "AND MMI.OUTLET_CODE LIKE :Outlet_Code \n"
                + "ORDER BY MMI.MENU_ITEM_CODE";
        Map prm = new HashMap();
        prm.put("Outlet_Code", "%" + ref.get("outlet_code") + "%");
        prm.put("Menu_Group_Code", "%" + ref.get("menu_group_code") + "%");
        prm.put("Menu_Item_Code", ref.get("menu_item_code"));
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("menuitemcode", rs.getString("menu_item_code"));
                rt.put("itemdescription", rs.getString("item_description"));
                rt.put("modifieritemcode", rs.getString("modifier_item_code"));
                rt.put("modifieritemname", rs.getString("modifier_item_name"));

                return rt;
            }
        });
        return list;

    }

    @Override
    public List<Map<String, Object>> listSpecialPrice(Map<String, String> ref) {
        String qry = "SELECT  \n"
                + "    MSP.MENU_ITEM_CODE, \n"
                + "    MG.DESCRIPTION, \n"
                + "    MSP.DATE_START, \n"
                + "    MSP.DATE_END, \n"
                + "    MSP.TIME_START, \n"
                + "    MSP.TIME_END, \n"
                + "    MSP.OUTLET_CODE \n"
                + "FROM M_OUTLET_SPECIAL_PRICE MSP \n"
                + "JOIN M_GLOBAL MG \n"
                + "ON MSP.MENU_ITEM_CODE = MG.CODE AND MG.COND = 'ITEM' \n"
                + "WHERE DATE_START BETWEEN TRUNC(SYSDATE, 'MM') AND LAST_DAY(SYSDATE) \n"
                + "AND DATE_END BETWEEN TRUNC(SYSDATE, 'MM') AND LAST_DAY(SYSDATE) \n"
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
    public List<Map<String, Object>> listMpcsHeader(Map<String, String> logan) {
        String qry = "select * from m_mpcs_header WHERE OUTLET_CODE='5244'";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
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
        String qry = "select rp.recipe_code, rp.product_code, rp.product_remark, rp.qty_stock, rp.uom_stock "
                + "from m_recipe_product rp "
                + "where rp.recipe_code = :reCode ";
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
        String qry = "SELECT DISTINCT VS.REGIONAL_DESC,VS.OUTLET_NAME,VS.AREA_DESC,VS.AREA_CODE,G.DESCRIPTION CITY_STAFF,\n"
                + "G1.DESCRIPTION POSITION_NAME,G2.DESCRIPTION ACCESS_NAME,\n"
                + "S.*,PS.STAFF_POS_CODE,PS.PASSWORD AS PASS_POS_CODE,s.access_level ACCESS_LEVEL FROM M_STAFF S\n"
                + "JOIN V_STRUCTURE_STORE VS ON VS.OUTLET_CODE = S.OUTLET_CODE \n"
                + "LEFT JOIN M_POS_STAFF PS ON PS.STAFF_CODE = S.STAFF_CODE \n"
                + "LEFT JOIN M_GLOBAL G ON G.CODE = S.CITY AND G.COND = 'CITY'\n"
                + "LEFT JOIN M_GLOBAL G1 ON G1.CODE = S.POSITION AND G1.COND = 'POSITION'\n"
                + "LEFT JOIN M_GLOBAL G2 ON G2.CODE = S.ACCESS_LEVEL AND G2.COND = 'ACCESS'\n"
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
                rt.put("accesssName", rs.getString("ACCESS_NAME"));
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
        String qry = "SELECT A.PLU_CODE,d.description,A.ITEM_CODE,B.ITEM_DESCRIPTION,A.QTY_EI,A.QTY_TA,A.UOM_STOCK, A.user_upd, A.date_upd, A.time_upd \n"
                + "FROM M_SALES_RECIPE A\n"
                + "JOIN M_ITEM B ON a.item_code=b.item_code\n"
                + "join m_menu_item c on a.plu_code=c.menu_Item_code\n"
                + "join m_global d on a.plu_code=d.code\n"
                + "WHERE c.status='A' and d.cond='ITEM' AND d.status='A' and c.outlet_code=:outletCode and A.PLU_CODE = :pluCode \n"
                + "GROUP BY c.menu_group_code,A.PLU_CODE,d.description,A.ITEM_CODE,B.ITEM_DESCRIPTION,A.QTY_EI,A.QTY_TA,A.UOM_STOCK, A.user_upd, A.date_upd, A.time_upd\n"
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
        String qry = "SELECT A.PLU_CODE,d.description\n"
                + "FROM M_SALES_RECIPE A\n"
                + "JOIN M_ITEM B ON a.item_code=b.item_code\n"
                + "join m_menu_item c on a.plu_code=c.menu_Item_code\n"
                + "join m_global d on a.plu_code=d.code\n"
                + "WHERE c.status='A' and d.cond='ITEM' AND d.status='A' and c.outlet_code= :outletCode \n"
                + "GROUP BY c.menu_group_code,A.PLU_CODE,d.description\n"
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
        String qry = "select distinct i.menu_item_code, g.description, i.status "
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
        String qry = "select distinct area_code,area_desc,region_code,regional_desc from V_STRUCTURE_STORE where region_code like :region_code order by region_code,area_code asc";
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
        String qry = "select distinct type,type_store from V_STRUCTURE_STORE";
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
        String qry = "select CODE,DESCRIPTION from M_GLOBAL WHERE COND  LIKE :cond AND STATUS LIKE :status ";
        Map prm = new HashMap();
        prm.put("cond", "%" + balance.get("cond") + "%");
        prm.put("status", "%" + balance.get("status") + "%");
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
        String qry = "select COND,CODE,DESCRIPTION,VALUE,STATUS from m_global WHERE COND = :cond ";
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
                rt.put("cdSupplier", rs.getString("CD_SUPPLIER"));
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
    @Override
    public List<Map<String, Object>> listOrderHeaderAll(Map<String, String> balance) {
        String getCity = getCity(balance.get("outletCode"));
        String where = "";
        if (!balance.get("orderDate").equals("")) {
            where = "AND ORDER_DATE =:orderDate";
        } else {
            where = "and ORDER_DATE between TO_CHAR(CURRENT_DATE-7,'dd-MON-yy') and TO_CHAR(CURRENT_DATE,'dd-MON-yy')";
        }
        String qry = "SELECT H.*,G.DESCRIPTION NAMA_GUDANG FROM T_ORDER_HEADER H\n"
                + "LEFT JOIN M_GLOBAL G ON G.CODE = H.CD_SUPPLIER AND G.COND = 'X_" + getCity + "'\n"
                + "WHERE H.STATUS LIKE :status \n"
                + "AND H.ORDER_TYPE LIKE :orderType \n"
                + "AND H.OUTLET_CODE = :outletCode \n"
                + "AND G.STATUS = 'A' " + where + "";
        Map prm = new HashMap();
        prm.put("status", "%" + balance.get("status") + "%");
        prm.put("orderType", "%" + balance.get("orderType") + "%");
        prm.put("outletCode", balance.get("outletCode"));
        prm.put("orderDate", balance.get("orderDate"));

//        prm.put("orderDate", balance.get("orderDate"));
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

    ///////////////////done
    ///////////////NEW METHOD LIST ORDER HEADER BY DONA 27 APRIL 2023////
    @Override
    public List<Map<String, Object>> listOrderDetail(Map<String, String> balance) {
        String qry = "SELECT \n"
                + "    ITEM_CODE,\n"
                + "    ITEM_DESCRIPTION,\n"
                + "    JUMLAH_SATUAN_BESAR,\n"
                + "    SATUAN_BESAR,\n"
                + "    JUMLAH_SATUAN_KECIL,\n"
                + "    UOM_PURCHASE,\n"
                + "    (CONV_WAREHOUSE * CONV_STOCK) CONV_WAREHOUSE,\n"
                + "    (JUMLAH_SATUAN_BESAR * CONV_WAREHOUSE) + JUMLAH_SATUAN_KECIL TOTAL_JUMLAH,\n"
                + "    UOM_STOCK AS TOTAL\n"
                + "FROM (\n"
                + "SELECT \n"
                + "    ITEM_CODE,\n"
                + "    ITEM_DESCRIPTION,\n"
                + "    0 AS JUMLAH_SATUAN_BESAR,\n"
                + "    UOM_WAREHOUSE AS SATUAN_BESAR,\n"
                + "    0 AS JUMLAH_SATUAN_KECIL,\n"
                + "    UOM_PURCHASE,UOM_STOCK,\n"
                + "    CONV_WAREHOUSE,CONV_STOCK,\n"
                + "    0 TOTAL_JUMLAH,\n"
                + "    UOM_PURCHASE AS TOTAL\n"
                + "FROM M_ITEM WHERE CD_WAREHOUSE = :cdWarehouse)";
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

        String qry = "SELECT ORDER_ID||COUNTNO ORDER_ID FROM (\n"
                + "SELECT A.OUTLET_CODE||:month||A.YEAR AS ORDER_ID,A.COUNTER_NO+1 COUNTNO FROM M_COUNTER A\n"
                + "LEFT JOIN M_OUTLET B\n"
                + "ON B.OUTLET_CODE=A.OUTLET_CODE\n"
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
        String qry = "select oh.OUTLET_CODE,oh.ORDER_NO,od.ITEM_CODE,i.ITEM_DESCRIPTION,\n"
                + "od.QTY_1 jumlah_besar,od.CD_UOM_1 satuan_besar,\n"
                + "od.QTY_2 jumlah_kecil,od.CD_UOM_2 satuan_kecil,od.TOTAL_QTY_STOCK total_qty,i.UOM_STOCK,\n"
                + "(i.CONV_WAREHOUSE*i.CONV_STOCK) uom_conv\n"
                + "from T_ORDER_HEADER oh\n"
                + "left join T_ORDER_DETAIL od on od.ORDER_NO = oh.ORDER_NO and od.ORDER_ID = oh.ORDER_ID\n"
                + "left join M_ITEM i on i.ITEM_CODE = od.ITEM_CODE\n"
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
                rt.put("uomConv", rs.getString("uom_conv"));
                return rt;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listItemDetailOpname(Map<String, String> balance) {
        String qry = "select ITEM_CODE,ITEM_DESCRIPTION,QTY_WAREHOUSE,CONV_WAREHOUSE,UOM_WAREHOUSE,\n"
                + "QTY_PURCHASE,CONV_STOCK,UOM_PURCHASE,0 TOTAL_STOCK,UOM_STOCK\n"
                + "from (\n"
                + "select i.ITEM_CODE,i.ITEM_DESCRIPTION, 0 QTY_WAREHOUSE, i.UOM_WAREHOUSE,\n"
                + "0 QTY_PURCHASE,i.CONV_WAREHOUSE,i.UOM_PURCHASE,i.CONV_STOCK,i.UOM_STOCK\n"
                + "from m_item i\n"
                + "where i.status = 'A' AND FLAG_MATERIAL = 'Y' AND FLAG_STOCK = 'Y'\n"
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
        String qry = "SELECT ITEM_CODE,ITEM_DESCRIPTION,\n"
                + "SUM(QTY_WAREHOUSE) QTY_WAREHOUSE, MAX(UOM_WAREHOUSE) UOM_WAREHOUSE ,\n"
                + "SUM(QTY_PURCHASE) QTY_PURCHASE, MAX(CONV_WAREHOUSE) CONV_WAREHOUSE,\n"
                + "MAX(UOM_PURCHASE) UOM_PURCHASE, SUM(TOTAL_QTY) TOTAL_QTY,\n"
                + "MAX(CONV_STOCK) CONV_STOCK,MAX(UOM_STOCK) UOM_STOCK\n"
                + "FROM (\n"
                + "select i.ITEM_CODE,i.ITEM_DESCRIPTION, 0 QTY_WAREHOUSE, i.UOM_WAREHOUSE,\n"
                + "0 QTY_PURCHASE,i.CONV_WAREHOUSE,i.UOM_PURCHASE,0 TOTAL_QTY,i.CONV_STOCK,i.UOM_STOCK\n"
                + "from m_item i\n"
                + "where i.status = 'A' AND i.FLAG_MATERIAL = 'Y' AND i.FLAG_STOCK = 'Y'\n"
                + "UNION ALL\n"
                + "select OD.ITEM_CODE,I.ITEM_DESCRIPTION,\n"
                + "(OD.QTY_PURCH/I.CONV_WAREHOUSE) QTY_WAREHOUSE,I.UOM_WAREHOUSE,\n"
                + "(OD.QTY_STOCK/I.CONV_STOCK) QTY_PURCHASE,i.CONV_WAREHOUSE,I.UOM_PURCHASE,\n"
                + "OD.TOTAL_QTY,i.CONV_STOCK,i.UOM_STOCK\n"
                + "from T_OPNAME_DETAIL OD\n"
                + "LEFT JOIN M_ITEM I ON I.ITEM_CODE = OD.ITEM_CODE\n"
                + "WHERE OD.OPNAME_NO = :opnameNo AND OD.OUTLET_CODE = :outletCode)\n"
                + "GROUP BY ITEM_CODE,ITEM_DESCRIPTION\n"
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
        String qry = "SELECT H.OPNAME_NO,H.OPNAME_DATE,\n"
                + "CASE WHEN H.CD_TEMPLATE = '1' THEN TH.TEMPLATE_NAME\n"
                + "     WHEN H.CD_TEMPLATE = '0' THEN 'STOCK OPNAME BARANG '|| H.OPNAME_DATE \n"
                + "END OPNAME_NAME,\n"
                + "case when H.CD_TEMPLATE = '1' THEN 'TEMPLATE' \n"
                + "     WHEN H.CD_TEMPLATE = '0' THEN 'PER BARANG' \n"
                + "END TYPE,H.STATUS\n"
                + "FROM T_OPNAME_HEADER H \n"
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
    public String cekOpname(String outletCode, String month) {
        String qry = "SELECT COUNT(*) count FROM T_OPNAME_HEADER WHERE TO_CHAR(OPNAME_DATE,'MON') = :month AND OUTLET_CODE = :outletCode";
        Map prm = new HashMap();
        prm.put("outletCode", outletCode);
        prm.put("month", month);
        return jdbcTemplate.queryForObject(qry, prm, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString(1) == null ? "0" : rs.getString(1);

            }
        }).toString();
    }

    @Override
    public String cekItem() {
        String qry = "SELECT COUNT(*) FROM M_ITEM \n"
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
        String qry = "select " +
                    "rc.recv_no, " +
                    "rc.recv_date, " +
                    "rc.order_no, " +
                    "'Pembelian ' || (" +
                    "    case when length(ord.cd_supplier) = 4 then 'Outlet ' || mo.outlet_name" +
                    "    when length(ord.cd_supplier) = 5 then mg.description" +
                    "    else 'Supplier ' || sp.supplier_name end" +
                    ") remark," +
                    "case when hk.status_kirim = 'S' and hk.status_terima = 'R' then 'Sudah' else ' ' end as upd_online, " +
                    "rc.no_of_print, " +
                    "case when rc.status = '1' then 'CLOSE' when rc.status = '0' then 'OPEN' else 'UNKNOWN' end as status " +
                    "from t_recv_header rc " +
                    "left join t_order_header ord on ord.order_no = rc.order_no " +
                    "left join m_supplier sp on sp.cd_supplier = ord.cd_supplier " +
                    "left join m_global mg on mg.cond = 'X_JKT' and mg.code = ord.cd_supplier " +
                    "left join m_outlet mo on mo.outlet_code = ord.cd_supplier " +
                    "left join hist_kirim hk on hk.no_order = ord.order_no " +
                    "where rc.recv_date between to_date(:dateStart, 'dd-mm-yyyy') and to_date(:dateEnd, 'dd-mm-yyyy') ";
                    //"and length(ord.cd_supplier) = 5 " +
                    //"order by rc.recv_date ";
        Map prm = new HashMap();
        prm.put("dateStart", ref.get("dateStart"));
        prm.put("dateEnd", ref.get("dateEnd"));
        String filter = ref.get("filter");
        if(filter.equalsIgnoreCase("1")) { //Outlet
            qry += "and length(ord.cd_supplier) = 4 ";
        } else if(filter.equalsIgnoreCase("2")){ //Gudang
            qry += "and length(ord.cd_supplier) = 5 ";
        } else if(filter.equalsIgnoreCase("3")){ //Supplier
            qry += "and length(ord.cd_supplier) = 10 ";
        }
        qry += "order by rc.recv_date ";
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("noTerima", rs.getString("recv_no"));
                rt.put("tanggal", rs.getString("recv_date"));
                rt.put("noOrder", rs.getString("order_no"));
                rt.put("tipeOrder", rs.getString("remark"));
                rt.put("updOnline", rs.getString("upd_online"));
                rt.put("print", rs.getString("no_of_print"));
                rt.put("status", rs.getString("status"));
                return rt;
            }
        });
        return list;
    }
}
