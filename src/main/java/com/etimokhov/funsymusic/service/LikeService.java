package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.LikeStatusDto;
import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;

import java.security.Principal;
import java.util.List;

public interface LikeService {
    LikeStatusDto getTrackLikeStatus(Long trackId, Principal principal);
    void likeTrack(Long trackId, Principal principal);
    void unlikeTrack(Long trackId, Principal principal);

    List<Track> getLikedTracks(User user);

    LikeStatusDto getPlaylistLikeStatus(Long playlistId, Principal principal);
    void likePlaylist(Long playlistId, Principal principal);
    void unlikePlaylist(Long playlistId, Principal principal);

    List<Playlist> getLikedPlaylists(User user);
}
