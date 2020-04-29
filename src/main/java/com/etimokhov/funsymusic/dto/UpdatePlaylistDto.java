package com.etimokhov.funsymusic.dto;

import java.util.List;

public class UpdatePlaylistDto {
    private Long playlistId;
    private List<Long> trackIds;

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public List<Long> getTrackIds() {
        return trackIds;
    }

    public void setTrackIds(List<Long> trackIds) {
        this.trackIds = trackIds;
    }
}
