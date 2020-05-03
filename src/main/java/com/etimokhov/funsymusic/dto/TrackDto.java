package com.etimokhov.funsymusic.dto;

public class TrackDto {

    private Long id;

    private String name;

    private String artist;

    private Integer length;

    private String mediaFileName;

    public TrackDto() {
    }

    public TrackDto(Long id, String name, String artist, Integer length, String mediaFileName) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.length = length;
        this.mediaFileName = mediaFileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", length=" + length +
                ", mediaFileName='" + mediaFileName + '\'' +
                '}';
    }
}
