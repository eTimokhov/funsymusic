package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.IsTrackInPlaylistDto;
import com.etimokhov.funsymusic.dto.form.PlaylistForm;
import com.etimokhov.funsymusic.exception.NotFoundException;
import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.repository.PlaylistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final Logger LOG = LoggerFactory.getLogger(PlaylistServiceImpl.class);

    private final PlaylistRepository playlistRepository;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Override
    public Playlist getPlaylist(Long id) {
        return playlistRepository.findById(id).orElseThrow(() -> new NotFoundException("Playlist #" + id + " not found"));
    }

    @Override
    public Playlist createPlaylist(PlaylistForm playlistForm, User owner) {
        Playlist playlist = new Playlist();
        playlist.setName(playlistForm.getName());
        playlist.setOwner(owner);
        playlist = playlistRepository.save(playlist);
        LOG.info("Playlist #{}: {} was successfully created", playlist.getId(), playlist.getName());
        return playlistRepository.save(playlist);
    }

    @Override
    public List<Playlist> findAllByOwner(Long userId) {
        return playlistRepository.findByOwnerId(userId);
    }

    @Override
    public boolean isPlaylistOwner(Playlist playlist, User user) {
        return playlist.getOwner().equals(user);
    }

    @Override
    public void addToPlaylist(Playlist playlist, Track track) throws IllegalStateException {
        if (playlistContainsTrack(playlist, track)) {
            throw new IllegalStateException("Playlist" + playlist.getId() + "already contains track " + track.getId());
        }
        playlist.getTracks().add(track);
        playlistRepository.save(playlist);
        LOG.info("Track {} added to playlist {}", track.getId(), playlist.getId());
    }

    @Override
    public void removeFromPlaylist(Playlist playlist, Track track) throws IllegalStateException {
        if (!playlistContainsTrack(playlist, track)) {
            throw new IllegalStateException("Playlist" + playlist.getId() + " doesn't contain track " + track.getId());
        }
        playlist.getTracks().removeIf(t -> t.getId().equals(track.getId()));
        playlistRepository.save(playlist);
        LOG.info("Track {} removed from playlist {}", track.getId(), playlist.getId());
    }

    @Override
    public List<IsTrackInPlaylistDto> checkTrackPresenceInUserPlaylists(Track track, User user) {
        return findAllByOwner(user.getId()).stream().map(
                        pl -> new IsTrackInPlaylistDto(pl.getId(), pl.getName(), playlistContainsTrack(pl, track))
                )
                .collect(Collectors.toList());
    }

    private boolean playlistContainsTrack(Playlist playlist, Track track) {
        return playlist.getTracks().stream().anyMatch(t -> t.getId().equals(track.getId()));
    }
}
