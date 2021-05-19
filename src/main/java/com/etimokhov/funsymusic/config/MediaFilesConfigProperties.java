package com.etimokhov.funsymusic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "funsymusic")
public class MediaFilesConfigProperties {
    private String mediaStorageDirectory;
    private String imagesDirectory;
    private String mp3Directory;
    private String hlsDirectory;
    private String ffmpegLocation;

    public String getMediaStorageDirectory() {
        return mediaStorageDirectory;
    }

    public void setMediaStorageDirectory(String mediaStorageDirectory) {
        this.mediaStorageDirectory = mediaStorageDirectory;
    }

    public String getImagesDirectory() {
        return imagesDirectory;
    }

    public void setImagesDirectory(String imagesDirectory) {
        this.imagesDirectory = imagesDirectory;
    }

    public String getMp3Directory() {
        return mp3Directory;
    }

    public void setMp3Directory(String mp3Directory) {
        this.mp3Directory = mp3Directory;
    }

    public String getHlsDirectory() {
        return hlsDirectory;
    }

    public void setHlsDirectory(String hlsDirectory) {
        this.hlsDirectory = hlsDirectory;
    }

    public String getFfmpegLocation() {
        return ffmpegLocation;
    }

    public void setFfmpegLocation(String ffmpegLocation) {
        this.ffmpegLocation = ffmpegLocation;
    }
}
