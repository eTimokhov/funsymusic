package com.etimokhov.funsymusic.startup;

import com.etimokhov.funsymusic.config.ConfigProperties;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class MediaStorageInitializer {
    private final Logger LOG = LoggerFactory.getLogger(MediaStorageInitializer.class);

    private final ConfigProperties configProperties;

    public MediaStorageInitializer(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public void initMediaFilesStorageDirectory() {
        LOG.info("Media file storage configuration: {}", configProperties.getMediaStorageDirectory());
        try {
            createDirectoryIfNotExists(configProperties.getMediaStorageDirectory());
            createDirectoryIfNotExists(configProperties.getMediaStorageDirectory() + "/audio");
            createDirectoryIfNotExists(configProperties.getMediaStorageDirectory() + "/images");
            LOG.info("Media file storage is ready");
        } catch (IOException e) {
            LOG.error("Exception occurred during media file storage directory preparing.", e);
            LOG.warn("Media file storage is NOT ready");
        }
    }

    private void createDirectoryIfNotExists(String path) throws IOException {
        File directory = new File(path);
        if (!directory.exists()) {
            FileUtils.forceMkdir(directory);
            LOG.info("Directory created: {}", directory);
        }
    }
}
