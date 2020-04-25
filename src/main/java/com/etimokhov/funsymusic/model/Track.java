package com.etimokhov.funsymusic.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

    @ManyToOne
    private User uploadedBy;

    public Track() {
    }

    public Track(String artist, String name, String mediaFile, String imageFile, Integer length, User uploadedBy) {
        this.artist = artist;
        this.name = name;
        this.mediaFile = mediaFile;
        this.imageFile = imageFile;
        this.length = length;
        this.uploadedBy = uploadedBy;
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

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
