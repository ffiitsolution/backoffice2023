package com.ffi.api.backoffice;

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
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
//        configurer.setLocation(new FileSystemResource(PROPERTIES_PATH));
//        return configurer;
//    }
//
//    @Bean
//    public PropertySourcesLogger propertySourcesLogger() {
//        return new PropertySourcesLogger();
//    }
//
//    private static class PropertySourcesLogger implements ApplicationListener<ContextRefreshedEvent> {
//        @Autowired
//        private Environment environment;
//
//        @Override
//        public void onApplicationEvent(ContextRefreshedEvent event) {
//            System.err.println("Applied Property Sources:");
////            for (PropertySource<?> propertySource : environment.getPropertySources()) {
////                System.err.println("- " + propertySource.getName());
////            }
//        }
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//        dataSource.setUrl(get("spring.datasource.url"));
//        dataSource.setUsername(get("spring.datasource.username"));
//        dataSource.setPassword(get("spring.datasource.password"));
//        System.err.println(dataSource);
//        return dataSource;
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate() {
//        return new JdbcTemplate(dataSource());
//    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource());
//        em.setPackagesToScan("com.ffi.api.backoffice.model");
//        em.setJpaProperties(additionalProperties());
//        return em;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(emf);
//        return transactionManager;
//    }

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
