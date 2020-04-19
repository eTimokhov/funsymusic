package com.etimokhov.funsymusic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "funsymusic")
public class ConfigProperties {
    private String mediaStorageDirectory;

    public String getMediaStorageDirectory() {
        return mediaStorageDirectory;
    }

    public void setMediaStorageDirectory(String mediaStorageDirectory) {
        this.mediaStorageDirectory = mediaStorageDirectory;
    }
}
