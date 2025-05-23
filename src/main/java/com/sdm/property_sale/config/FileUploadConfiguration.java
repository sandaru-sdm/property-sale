package com.sdm.property_sale.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import jakarta.servlet.MultipartConfigElement;

@Configuration
public class FileUploadConfiguration {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // Set max file size (10MB per file)
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        // Set max request size (60MB total - 6 images * 10MB each)
        factory.setMaxRequestSize(DataSize.ofMegabytes(60));
        return factory.createMultipartConfig();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}