package com.etimokhov.funsymusic.util;

import com.etimokhov.funsymusic.config.MediaFilesConfigProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class MediaFileUtil {
    private final MediaFilesConfigProperties mediaFilesConfig;

    private final Logger LOG = LoggerFactory.getLogger(MediaFileUtil.class);

    public MediaFileUtil(MediaFilesConfigProperties mediaFilesConfig) {
        this.mediaFilesConfig = mediaFilesConfig;
    }

    public String saveMp3(byte[] data) throws IOException {
        String fileName = generateRandomFilename("mp3");
        String path = getMediaFileFullPath(fileName);
        FileUtils.writeByteArrayToFile(new File(path), data);
        LOG.info("File {} was successfully saved", path);
        return fileName;
    }

    public String savePng(byte[] data) throws IOException {
        String fileName = generateRandomFilename("png");
        String path = getMediaFileFullPath(fileName);
        FileUtils.writeByteArrayToFile(new File(path), data);
        LOG.info("File {} was successfully saved", path);
        return fileName;
    }

    public String getMediaFileFullPath(String fileName) {
        String path = FilenameUtils.concat(mediaFilesConfig.getMediaStorageDirectory(), mediaFilesConfig.getMp3Directory());
        return FilenameUtils.concat(path, fileName);
    }

    private String generateRandomFilename(String ext) {
        return String.format("%s.%s", RandomStringUtils.randomAlphanumeric(10), ext);
    }
}
