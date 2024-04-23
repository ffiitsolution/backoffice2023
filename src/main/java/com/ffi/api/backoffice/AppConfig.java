package com.ffi.api.backoffice;

import java.awt.Toolkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

@Configuration
public class AppConfig {
    private static final String PROPERTIES_PATH = "C:/API_CONFIG/boffi.properties";

    @Autowired
    private Environment environment;
    
    public void doBeepMpcs() {
        // Custom pattern: 3 short beeps with pauses in between
        beep(100);
        pause(500);
        beep(100);
        pause(500);
        beep(100);
    }
    
    public void beep(int duration) {
        Toolkit.getDefaultToolkit().beep();
        pause(duration);
    }

    public void pause(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(get("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(get("spring.mail.port")));
        mailSender.setUsername(get("spring.mail.username"));
        mailSender.setPassword(get("spring.mail.password"));
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", get("spring.mail.properties.mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", get("spring.mail.properties.mail.smtp.starttls.enable"));
        
        return mailSender;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("spring.jpa.database", get("spring.jpa.database"));
        properties.setProperty("spring.jpa.show-sql", get("spring.jpa.show-sql"));
        return properties;
    }

    public String get(String key) {
        String value = environment.getProperty(key);
//        System.err.println("AppConfig " + key + ": " + value);
        return value == null ? "" : value;
    }

    public String get(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    public String getOutletCode() {
        return get("app.outletCode");
    }

    public String getEndpoint(String ep) {
        return get("endpoint." + ep);
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> servletContainer() {
        return server -> {
            server.setContextPath(get("server.servlet.context-path", "/boffi"));
            server.setPort(Integer.parseInt(get("server.port", "14022")));
        };
    }
}
