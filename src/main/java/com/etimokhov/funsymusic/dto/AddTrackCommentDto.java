package com.etimokhov.funsymusic.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddTrackCommentDto {
    @NotNull
    private Long trackId;

    @NotBlank
    private String text;

    private Integer trackTimestamp;

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
}
