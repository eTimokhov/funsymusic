package com.etimokhov.funsymusic.dto;

public class TrackCommentDto {
    private Long trackId;

    private String text;

    private Integer trackTimestamp;

    private UserDto user;

    private String commentDateRel;

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTrackTimestamp() {
        return trackTimestamp;
    }

    public void setTrackTimestamp(Integer trackTimestamp) {
        this.trackTimestamp = trackTimestamp;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getCommentDateRel() {
        return commentDateRel;
    }

    public void setCommentDateRel(String commentDateRel) {
        this.commentDateRel = commentDateRel;
    }
}
