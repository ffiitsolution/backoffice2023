/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.utils;

/**
 *
 * @author USER
 * 
 * digunakan untuk POST data ke API lain
 * 
 */
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class RestApiUtil {

    public ResponseEntity<String> post(String apiUrl, List<Map<String, Object>> jsonParams, HttpHeaders additionalHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (additionalHeaders != null) {
            headers.addAll(additionalHeaders);
        }
        HttpEntity<List<Map<String, Object>>> requestEntity = new HttpEntity<>(jsonParams, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(apiUrl, requestEntity, String.class);
    }
}
