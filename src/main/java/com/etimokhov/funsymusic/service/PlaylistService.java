package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.IsTrackInPlaylistDto;
import com.etimokhov.funsymusic.dto.PlaylistDto;
import com.etimokhov.funsymusic.dto.form.PlaylistForm;
import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PlaylistService {
    Playlist getPlaylist(Long id);

    Playlist getPlaylistWithTracks(Long id);

    Playlist createPlaylist(PlaylistForm playlistForm, User owner);
    Playlist createPlaylist(PlaylistForm playlistForm, String ownerUsername);

    List<Playlist> findAllByOwner(Long userId);

    boolean isPlaylistOwner(Playlist playlist, User user);

    void addToPlaylist(Playlist playlist, Track track);

    void removeFromPlaylist(Playlist playlist, Track track);

    List<IsTrackInPlaylistDto> checkTrackPresenceInUserPlaylists(Track track, User user);

    List<Track> getTracks(Long playlistId);

    void updatePlaylist(Playlist playlist, List<Long> trackIds);

    Page<Playlist> findLastUploaded(Integer page, Integer count);

    Page<Playlist> findLastUploadedByOwner(Long userId, Integer page, Integer count);

    PlaylistDto mapToDto(Playlist playlist);
}
