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
    private String mediaUrl;
    private String imageUrl;
    private Integer length;

    public Track() {
    }

    public Track(String artist, String name, String mediaUrl, String imageUrl, Integer length) {
        this.artist = artist;
        this.name = name;
        this.mediaUrl = mediaUrl;
        this.imageUrl = imageUrl;
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

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getLength() {
        return length;
    }
}
