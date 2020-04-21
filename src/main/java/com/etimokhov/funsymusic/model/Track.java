package com.etimokhov.funsymusic.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Track {
    @Id
    @GeneratedValue
    private Long id;
    private String artist;
    private String name;
    private String mediaFile;
    private String imageFile;
    private Integer length;

    public Track() {
    }

    public Track(String artist, String name, String mediaFile, String imageFile, Integer length) {
        this.artist = artist;
        this.name = name;
        this.mediaFile = mediaFile;
        this.imageFile = imageFile;
        this.length = length;
    }

    public Long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public String getMediaFile() {
        return mediaFile;
    }

    public String getImageFile() {
        return imageFile;
    }

    public Integer getLength() {
        return length;
    }
}
