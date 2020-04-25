package com.etimokhov.funsymusic.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class Playlist {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String imageUrl;

    @ManyToOne
    private User owner;

    @ManyToMany
    private List<Track> tracks;
}
