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

    @Autowired
    private FileLoggerUtil fileLoggerUtil;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // sesuaikan kondisi endpoint dan param, simpan ke file logger
    private void logActivity(String ep, Map<String, Object> param) {
        if (checkValidParam(param.get("actUser")) && checkValidParam(param.get("actName")) && checkValidParam(param.get("actUrl"))) {
            String staffCode = param.getOrDefault("actUser", "").toString();
            String staffName = param.getOrDefault("actName", "").toString();
            String outletCode = param.getOrDefault("actOutlet", "").toString();
            String url = param.getOrDefault("actUrl", "").toString();
            String query = "SELECT * FROM M_LEVEL_AKSES_DETAIL WHERE URL = :actUrl";
            String module = "-";
            Map<String, Object> obj;
            List<Map<String, Object>> list = jdbcTemplate.query(query, param, new DynamicRowMapper());
            if (!list.isEmpty()) {
                obj = list.get(0);
                module = obj.getOrDefault("description", "").toString();
            }

            switch (ep) {
                case "option1" -> {
                }
                default ->
                    fileLoggerUtil.logActivity(url, module, VIEW, staffCode, staffName, "", true, ep, param);
            }

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
