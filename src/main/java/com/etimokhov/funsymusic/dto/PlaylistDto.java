package com.etimokhov.funsymusic.dto;

import java.util.Date;

public class PlaylistDto {

    private Long id;

    private String name;

    private Long ownerId;

    private Date createDate;

    public PlaylistDto(Long id, String name, Long ownerId, Date createDate) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.createDate = createDate;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
