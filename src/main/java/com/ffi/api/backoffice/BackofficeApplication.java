package com.ffi.api.backoffice;

import com.ffi.api.backoffice.utils.OutputRedirectToFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackofficeApplication {

    public static void main(String[] args) throws Exception {
        
        try {
            // Menyalin output console ke file untuk mempermudah debugging
            OutputRedirectToFile.redirectOutputToFile("logs/temp.log");
            
            SpringApplication.run(BackofficeApplication.class, args);
        } finally {
            OutputRedirectToFile.restoreOutput();
        }
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerImpl();
    }

    private static class WebMvcConfigurerImpl implements WebMvcConfigurer {

        public WebMvcConfigurerImpl() {
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    .allowCredentials(false);
        }
    }
}
