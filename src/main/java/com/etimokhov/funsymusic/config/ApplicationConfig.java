package com.etimokhov.funsymusic.config;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {
    @Value("${funsymusic.mediaStorageDirectory}")
    private String mediaStorageDirectory;

    @Value("${funsymusic.imagesDirectory}")
    private String imagesDirectory;

    @Value("${funsymusic.hlsDirectory}")
    private String hlsDirectory;

    @Value("${funsymusic.mp3Directory}")
    private String mp3Directory;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/" + FilenameUtils.concat(mediaStorageDirectory, imagesDirectory) + "/");
        registry.addResourceHandler("/hls/**")
                .addResourceLocations("file:/" + FilenameUtils.concat(mediaStorageDirectory, hlsDirectory) + "/");
        registry.addResourceHandler("/music/**")
                .addResourceLocations("file:/" + FilenameUtils.concat(mediaStorageDirectory, mp3Directory) + "/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}
