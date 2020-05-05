package com.etimokhov.funsymusic.dto;

import java.util.Date;

public class UserEventDto {



    public enum Action {
        TRACK_COMMENT, NEW_TRACK, NEW_PLAYLIST, TRACK_LIKE, PLAYLIST_LIKE;
    }
    private UserDto user;

    private Action action;

    private String targetId;

    private String targetName;

    private Date eventDate;

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
}
