package com.ffi.api.backoffice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author USER
 */

public class TableAlias {
    @JsonProperty("data")
    private String data;
    
    @JsonProperty("table")
    private String table;
    
    @JsonProperty("alias")
    private String alias;
    
    @JsonProperty("dateColumn")
    private String dateColumn;
    
    @JsonProperty("hasOutletCode")
    private boolean hasOutletCode;
    
    @JsonProperty("emptyFirst")
    private boolean emptyFirst;
    
    @JsonProperty("active")
    private boolean active;
    
    @JsonProperty("process")
    private boolean process;
    
    @JsonProperty("dateUpd")
    private String dateUpd;
    
    @JsonProperty("timeUpd")
    private String timeUpd;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(String dateColumn) {
        this.dateColumn = dateColumn;
    }

    public boolean isHasOutletCode() {
        return hasOutletCode;
    }

    public void setHasOutletCode(boolean hasOutletCode) {
        this.hasOutletCode = hasOutletCode;
    }

    public boolean isEmptyFirst() {
        return emptyFirst;
    }

    public void setEmptyFirst(boolean emptyFirst) {
        this.emptyFirst = emptyFirst;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isProcess() {
        return process;
    }

    public void setProcess(boolean process) {
        this.process = process;
    }

    public String getDateUpd() {
        return dateUpd;
    }

    public void setDateUpd(String dateUpd) {
        this.dateUpd = dateUpd;
    }

    public String getTimeUpd() {
        return timeUpd;
    }

    public void setTimeUpd(String timeUpd) {
        this.timeUpd = timeUpd;
    }
}
