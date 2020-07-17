package com.wuwhy.whytest.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MyConfiguration implements WebMvcConfigurer {
    @Value("${upload.path:#{systemProperties['user.home']}}")
    private String baseImagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        baseImagePath = baseImagePath.replaceAll("\\\\", "/");
        String path = "file:"+baseImagePath+"/image/";
        registry.addResourceHandler("/image/**").addResourceLocations(path);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }
}