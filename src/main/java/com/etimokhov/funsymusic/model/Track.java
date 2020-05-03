package com.etimokhov.funsymusic.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Track {
    @Id
    @GeneratedValue
    private Long id;
    private String artist;
    private String name;
    private String mediaFile;
    private Integer length;

    private Date uploadDate;

    @ManyToOne
    private User uploader;

    public Track() {
    }

    public Track(String artist, String name, String mediaFile, Integer length, User uploader) {
        this.artist = artist;
        this.name = name;
        this.mediaFile = mediaFile;
        this.length = length;
        this.uploader = uploader;
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

    public Integer getLength() {
        return length;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploadedBy) {
        this.uploader = uploadedBy;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
