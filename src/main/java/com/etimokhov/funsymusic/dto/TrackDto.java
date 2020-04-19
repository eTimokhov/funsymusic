package com.etimokhov.funsymusic.dto;

import javax.validation.constraints.NotBlank;

public class TrackDto {
    @NotBlank
    private String name;

    private String artist;

    private Integer length;

    @NotBlank
    private String mediaFileName;

    private String imageFileName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getMediaFileName() {
        return mediaFileName;
    }

    public void setMediaFileName(String mediaFileName) {
        this.mediaFileName = mediaFileName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "TrackDto{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", length=" + length +
                ", mediaFileName='" + mediaFileName + '\'' +
                ", imageFileName='" + imageFileName + '\'' +
                '}';
    }
}
