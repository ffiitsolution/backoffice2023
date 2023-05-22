/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dwi Prasetyo
 */
public class DetailOpname {
    
    String outletCode;
    String opnameNo;
    String outletNo;
    String itemCode;
    int qtyFreeze = 0;
    int costFreeze = 0;
    String qtyPurch;
    String uomPurch;
    String qtyStock;
    String uomStock;
    String totalQty;
    String userUpd;

    List<DetailOpname> details = new ArrayList<DetailOpname>();

    public String getOpnameNo() {
        return opnameNo;
    }

    public void setOpnameNo(String opnameNo) {
        this.opnameNo = opnameNo;
    }
    
    
    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getOutletNo() {
        return outletNo;
    }

    public void setOutletNo(String outletNo) {
        this.outletNo = outletNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQtyFreeze() {
        return qtyFreeze;
    }

    public void setQtyFreeze(int qtyFreeze) {
        this.qtyFreeze = qtyFreeze;
    }

    public int getCostFreeze() {
        return costFreeze;
    }

    public void setCostFreeze(int costFreeze) {
        this.costFreeze = costFreeze;
    }

    public String getQtyPurch() {
        return qtyPurch;
    }

    public void setQtyPurch(String qtyPurch) {
        this.qtyPurch = qtyPurch;
    }

    public String getUomPurch() {
        return uomPurch;
    }

    public void setUomPurch(String uomPurch) {
        this.uomPurch = uomPurch;
    }

    public String getQtyStock() {
        return qtyStock;
    }

    public void setQtyStock(String qtyStock) {
        this.qtyStock = qtyStock;
    }

    public String getUomStock() {
        return uomStock;
    }

    public void setUomStock(String uomStock) {
        this.uomStock = uomStock;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getUserUpd() {
        return userUpd;
    }

    public void setUserUpd(String userUpd) {
        this.userUpd = userUpd;
    }

    public List<DetailOpname> getDetails() {
        return details;
    }

    public void setDetails(List<DetailOpname> details) {
        this.details = details;
    }
    
    
    
}
