package com.etimokhov.funsymusic.dto.form;

import javax.validation.constraints.NotBlank;

public class TrackForm {
    @NotBlank
    private String name;

    @NotBlank
    private String artist;

    private Integer length;

    @NotBlank
    private String mediaFileName;

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
                '}';
    }
}
