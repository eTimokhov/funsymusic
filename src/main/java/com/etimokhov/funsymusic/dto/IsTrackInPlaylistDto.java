package com.etimokhov.funsymusic.dto;

public class IsTrackInPlaylistDto {
    private Long playlistId;
    private String playlistName;
    private Boolean isTrackInPlaylist;

    public IsTrackInPlaylistDto() {
    }

    public IsTrackInPlaylistDto(Long playlistId, String playlistName, Boolean isTrackInPlaylist) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.isTrackInPlaylist = isTrackInPlaylist;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Boolean getTrackInPlaylist() {
        return isTrackInPlaylist;
    }

    public void setTrackInPlaylist(Boolean trackInPlaylist) {
        isTrackInPlaylist = trackInPlaylist;
    }
}
