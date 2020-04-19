package com.etimokhov.funsymusic;

import com.etimokhov.funsymusic.config.MediaFilesConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MediaFilesConfigProperties.class)
public class FunsymusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(FunsymusicApplication.class, args);
    }

}
