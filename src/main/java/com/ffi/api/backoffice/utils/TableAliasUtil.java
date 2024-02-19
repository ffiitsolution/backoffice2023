/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffi.api.backoffice.model.TableAlias;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author USER
 */

@Component
public class TableAliasUtil {
    
    public static final String TABLE_ALIAS_M = "tableAliasM";
    public static final String TABLE_ALIAS_T = "tableAliasT";
    private Map<String, List<TableAlias>> dataMap;

    @PostConstruct
    public void init() {
        dataMap = new HashMap<>();
        loadDataFromFile(TABLE_ALIAS_M);
        loadDataFromFile(TABLE_ALIAS_T);
    }

    public void loadDataFromFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonPath = "json/" + fileName + ".json";
            ClassPathResource resource = new ClassPathResource(jsonPath);
            TableAlias[] data = mapper.readValue(resource.getInputStream(), TableAlias[].class);
            dataMap.put(fileName, Arrays.asList(data));
        } catch (IOException e) {
            System.err.println("Error loading data from file " + fileName + ": " + e.getMessage());
            dataMap.put(fileName, Collections.emptyList());
        }
    }

    public List<TableAlias> getDataList(String fileName) {
        return dataMap.getOrDefault(fileName, Collections.emptyList());
    }

    public List<TableAlias> searchByColumn(String fileName, String columnName, String columnValue) {
        return dataMap.getOrDefault(fileName, Collections.emptyList()).stream()
                .filter(tableAlias -> matches(tableAlias, columnName, columnValue))
                .collect(Collectors.toList());
    }

    public Optional<TableAlias> firstByColumn(String fileName, String columnName, String columnValue) {
        return dataMap.getOrDefault(fileName, Collections.emptyList()).stream()
                .filter(tableAlias -> matches(tableAlias, columnName, columnValue))
                .findFirst();
    }

    private boolean matches(TableAlias tableAlias, String columnName, String columnValue) {
        return switch (columnName) {
            case "data" -> tableAlias.getData().equals(columnValue);
            case "table" -> tableAlias.getTable().equals(columnValue);
            case "alias" -> tableAlias.getAlias().equals(columnValue);
            case "dateColumn" -> tableAlias.getDateColumn().equals(columnValue);
            case "dateUpd" -> tableAlias.getDateUpd().equals(columnValue);
            case "timeUpd" -> tableAlias.getTimeUpd().equals(columnValue);
            default -> false;
        };
    }
}
