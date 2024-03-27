package com.ffi.api.backoffice.services;

import com.ffi.paging.ResponseMessage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private ReportServices reportService;
    
    @Value("${mail.sender}")
    private String emailSender;
    @Value("${spring.datasource.url}")
    private String getOracleUrl;
    @Value("${spring.datasource.username}")
    private String getOracleUsername;
    @Value("${spring.datasource.password}")
    private String getOraclePass;

    public ResponseMessage sendEmail(Map<String, Object> balance) throws MessagingException, JRException, SQLException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        Connection conn = DriverManager.getConnection(getOracleUrl, getOracleUsername, getOraclePass);
        
        ResponseMessage rm = new ResponseMessage();
        JasperPrint jasperPrint = reportService.jasperReportOrderEntryTransactions(balance, conn);
        conn.close();
        
        byte[] content = JasperExportManager.exportReportToPdf(jasperPrint);
        ByteArrayResource resource = new ByteArrayResource(content);
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailSender);
            helper.setTo((String)balance.get("recipient"));
            helper.setText((String)balance.get("body"));
            helper.setSubject((String)balance.get("subject"));
            helper.addAttachment("Permintaan " + balance.get("supplierName")+ " - "+ balance.get("orderNo") + ".pdf", resource);
            mailSender.send(message);
            
            rm.setMessage("Sent Success");
            rm.setSuccess(true);
        } catch (MailException e) {
            e.printStackTrace();
            rm.setMessage(e.getMessage());
            rm.setSuccess(false);
        }
        return rm;
    }
}
