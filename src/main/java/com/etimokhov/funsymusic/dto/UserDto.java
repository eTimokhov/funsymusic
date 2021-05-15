package com.etimokhov.funsymusic.dto;

import java.util.Date;

public class UserDto {

    private Long id;

    private String username;

    private String image;

    private Date registrationDate;

    public UserDto(Long id, String username, String image, Date registrationDate) {
        this.id = id;
        this.username = username;
        this.image = image;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
