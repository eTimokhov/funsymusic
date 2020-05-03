package com.etimokhov.funsymusic.util;

import com.etimokhov.funsymusic.config.MediaFilesConfigProperties;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
public class MediaFileUtil {
    private static final int IMAGE_SIZE_PX = 250;
    private static final int SMALL_IMAGE_SIZE_PX = 50;

    private final MediaFilesConfigProperties mediaFilesConfig;

    private final Logger LOG = LoggerFactory.getLogger(MediaFileUtil.class);

    public MediaFileUtil(MediaFilesConfigProperties mediaFilesConfig) {
        this.mediaFilesConfig = mediaFilesConfig;
    }

    public String saveMp3(byte[] data) throws IOException {
        String fileName = generateRandomFilename("mp3");
        String path = getMp3FileFullPath(fileName);
        FileUtils.writeByteArrayToFile(new File(path), data);
        LOG.info("File {} was successfully saved", path);
        return fileName;
    }

    public String getMp3FileFullPath(String fileName) {
        String path = FilenameUtils.concat(mediaFilesConfig.getMediaStorageDirectory(), mediaFilesConfig.getMp3Directory());
        return FilenameUtils.concat(path, fileName);
    }

    public String getImageFileFullPath(String fileName) {
        String path = FilenameUtils.concat(mediaFilesConfig.getMediaStorageDirectory(), mediaFilesConfig.getImagesDirectory());
        return FilenameUtils.concat(path, fileName);
    }

    public String generateRandomFilenameNoExt() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateRandomFilename(String ext) {
        return String.format("%s.%s", RandomStringUtils.randomAlphanumeric(10), ext);
    }

    public void saveJpegThumbnails(byte[] imageBytes, String imageName) throws IOException {
        InputStream imageInputStream = new ByteArrayInputStream(imageBytes);
        Thumbnails.of(imageInputStream)
                .crop(Positions.CENTER)
                .size(IMAGE_SIZE_PX, IMAGE_SIZE_PX)
                .outputQuality(0.9)
                .toFile(getImageFileFullPath(imageName + ".jpg"));
        imageInputStream.reset();
        Thumbnails.of(imageInputStream)
                .crop(Positions.CENTER)
                .size(SMALL_IMAGE_SIZE_PX, SMALL_IMAGE_SIZE_PX)
                .outputQuality(0.9)
                .toFile(getImageFileFullPath(imageName + "_small.jpg"));
    }
}
