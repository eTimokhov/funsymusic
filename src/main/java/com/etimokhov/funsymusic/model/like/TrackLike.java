package com.etimokhov.funsymusic.model.like;

import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class TrackLike {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Track track;

    @OneToOne
    private User user;

    private Date likeDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(Date likeDate) {
        this.likeDate = likeDate;
    }
}
