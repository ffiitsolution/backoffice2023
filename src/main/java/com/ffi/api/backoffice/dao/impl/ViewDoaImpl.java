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
public class ViewDoaImpl implements ViewDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ViewDoaImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> loginJson(ParameterLogin ref) {
        String qry = "select OUTLET_CODE,STAFF_CODE,STAFF_NAME,STAFF_FULL_NAME,ID_CARD,POSITION,GROUP_ID from M_STAFF \n"
                + "where STAFF_CODE = :staffCode and PASSWORD = :pass and OUTLET_CODE = :outletCode and STATUS = 'A'";
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
    ///////////////new method from dona 28-02-2023////////////////////////////

    @Override
    public List<Map<String, Object>> listSupplier(Map<String, String> balance) {
        String qry = "SELECT  CD_SUPPLIER, SUPPLIER_NAME, CP_NAME, FLAG_CANVASING, STATUS, ADDRESS_1, "
                + "ADDRESS_2, CITY, ZIP_CODE, PHONE, FAX, HOMEPAGE, CP_TITLE, CP_MOBILE, CP_PHONE, "
                + "CP_PHONE_EXT, CP_EMAIL, USER_UPD, DATE_UPD, TIME_UPD FROM m_supplier WHERE STATUS LIKE :status and FLAG_CANVASING like :flagCanvasing AND CITY LIKE :city "
                + "ORDER BY CD_SUPPLIER  ASC";
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
                rt.put("Status", rs.getString("STATUS"));
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

        String qry = "select  a.CD_SUPPLIER,a.item_code,item_description,"
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
//        String qry = "SELECT A.ITEM_CODE,A.ITEM_DESCRIPTION,B.ITEM_COST,A.STATUS FROM M_ITEM A LEFT JOIN M_ITEM_COST B ON A.ITEM_CODE=B.ITEM_CODE "
//                + "WHERE A.ITEM_CODE IS NOT NULL AND A.ITEM_CODE <>' ' "
//                + "and a.flag_material like :flagMaterial AND a.FLAG_HALF_FINISH LIKE :flagHalfFinish "
//                + "AND a.FLAG_FINISHED_GOOD LIKE :flagFinishGood AND A.STATUS='A' ORDER  BY A.ITEM_CODE ASC";
////
//        Map prm = new HashMap();
//        prm.put("flagMaterial", "%" + balance.get("flagMaterial") + "%");
//        prm.put("flagFinishGood", "%" + balance.get("flagFinishGood") + "%");
//        prm.put("flagHalfFinish", "%" + balance.get("flagHalfFinish") + "%");
        String qry = "SELECT A.ITEM_CODE,A.ITEM_DESCRIPTION,B.ITEM_COST,A.STATUS FROM M_ITEM A LEFT JOIN M_ITEM_COST B ON A.ITEM_CODE=B.ITEM_CODE "
                + "WHERE A.ITEM_CODE IS NOT NULL AND A.ITEM_CODE <>' ' "
                + "and a.flag_material =:flagMaterial  AND A.STATUS='A' ORDER  BY A.ITEM_CODE ASC ";

//          String qry ="SELECT * FROM M_ITEM WHERE FLAG_MATERIAL= :TES ORDER BY ITEM_CODE ASC";
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
                + "    NVL2(MMI.MODIFIER_GROUP1_CODE, 'Y', 'N') AS MODIFIER_STATUS \n"
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

    ///////////////new method from asep 16-mar-2023 //////////////      
    @Override
    public List<Map<String, Object>> listOutlet(Map<String, String> Logan) {
        String qry = "SELECT region_code,outlet_code, initial_outlet, outlet_name, type, status FROM M_OUTLET where type <>'HO' and status='A'";
        Map prm = new HashMap();
        System.err.println("q :" + qry);
        List<Map<String, Object>> list = jdbcTemplate.query(qry, prm, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("region", rs.getString("region_code"));
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
}
