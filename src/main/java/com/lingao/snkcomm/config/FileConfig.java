package com.lingao.snkcomm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lingao.
 * @description
 * @date 2022/5/10 - 9:48
 */
@Configuration
public class FileConfig implements WebMvcConfigurer {
    @Value("${file.uploadurl}")
    private String  fileUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry register){
        register.addResourceHandler("/upload/**").addResourceLocations("file:"+fileUrl);
    }
}
