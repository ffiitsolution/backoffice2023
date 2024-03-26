package com.ffi.api.backoffice;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.api.backoffice.services.EmailService;
import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import net.sf.jasperreports.engine.JRException;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/order-entry-send-email")
    @ApiOperation(value = "Mengirim email order entry ke supplier", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public ResponseMessage sendEmail(@RequestBody String param) throws MessagingException, JRException, SQLException, IOException {
        Gson gsn = new Gson();
        Map<String, Object> balance = gsn.fromJson(param, new TypeToken<Map<String, String>>() {
        }.getType());
        
        return emailService.sendEmail(balance);
    }
}
