package com.etimokhov.funsymusic.model.like;

import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class PlaylistLike {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Playlist playlist;

    @OneToOne
    private User user;

    private Date likeDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
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
