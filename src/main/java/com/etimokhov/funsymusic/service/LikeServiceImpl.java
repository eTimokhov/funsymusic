package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.LikeStatusDto;
import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.model.like.PlaylistLike;
import com.etimokhov.funsymusic.model.like.TrackLike;
import com.etimokhov.funsymusic.repository.PlaylistRepository;
import com.etimokhov.funsymusic.repository.TrackRepository;
import com.etimokhov.funsymusic.repository.like.PlaylistLikeRepository;
import com.etimokhov.funsymusic.repository.like.TrackLikeRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    private final TrackLikeRepository trackLikeRepository;
    private final PlaylistLikeRepository playlistLikeRepository;

    private final TrackService trackService;
    private final PlaylistService playlistService;

    private final TrackRepository trackRepository;
    private final PlaylistRepository playlistRepository;


    private final UserService userService;

    public LikeServiceImpl(TrackLikeRepository trackLikeRepository, PlaylistLikeRepository playlistLikeRepository, TrackService trackService, PlaylistService playlistService, TrackRepository trackRepository, PlaylistRepository playlistRepository, UserService userService) {
        this.trackLikeRepository = trackLikeRepository;
        this.playlistLikeRepository = playlistLikeRepository;
        this.trackService = trackService;
        this.playlistService = playlistService;
        this.trackRepository = trackRepository;
        this.playlistRepository = playlistRepository;
        this.userService = userService;
    }

    @Override
    public LikeStatusDto getTrackLikeStatus(Long trackId, Principal principal) {
        trackService.getTrack(trackId);
        LikeStatusDto likeStatusDto = new LikeStatusDto();
        likeStatusDto.setTotalCount(trackLikeRepository.countByTrackId(trackId));
        if (principal != null) {
            User currentUser = userService.getCurrentUser(principal);
            likeStatusDto.setLiked(trackLikeRepository.existsByTrackIdAndUserId(trackId, currentUser.getId()));
            likeStatusDto.setAuth(true);
        } else {
            likeStatusDto.setLiked(false);
            likeStatusDto.setAuth(false);
        }
        return likeStatusDto;
    }

    @Override
    public void likeTrack(Long trackId, Principal principal) {
        Track track = trackService.getTrack(trackId);
        User currentUser = userService.getCurrentUser(principal);
        boolean liked = trackLikeRepository.existsByTrackIdAndUserId(trackId, currentUser.getId());
        if (!liked) {
            TrackLike trackLike = new TrackLike();
            trackLike.setTrack(track);
            trackLike.setUser(currentUser);
            trackLike.setLikeDate(new Date());
            trackLikeRepository.save(trackLike);
        }
    }

    @Override
    public void unlikeTrack(Long trackId, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        trackLikeRepository.getByTrackIdAndUserId(trackId, currentUser.getId())
                .ifPresent(trackLikeRepository::delete);
    }

    @Override
    public List<Track> getLikedTracks(User user) {
        return trackRepository.findByUserLikes(user.getId());
    }

    @Override
    public LikeStatusDto getPlaylistLikeStatus(Long playlistId, Principal principal) {
        playlistService.getPlaylist(playlistId);
        LikeStatusDto likeStatusDto = new LikeStatusDto();
        likeStatusDto.setTotalCount(playlistLikeRepository.countByPlaylistId(playlistId));
        if (principal != null) {
            User currentUser = userService.getCurrentUser(principal);
            likeStatusDto.setLiked(playlistLikeRepository.existsByPlaylistIdAndUserId(playlistId, currentUser.getId()));
            likeStatusDto.setAuth(true);
        } else {
            likeStatusDto.setLiked(false);
            likeStatusDto.setAuth(false);
        }
        return likeStatusDto;
    }

    @Override
    public void likePlaylist(Long playlistId, Principal principal) {
        Playlist playlist = playlistService.getPlaylist(playlistId);
        User currentUser = userService.getCurrentUser(principal);
        boolean liked = playlistLikeRepository.existsByPlaylistIdAndUserId(playlistId, currentUser.getId());
        if (!liked) {
            PlaylistLike playlistLike = new PlaylistLike();
            playlistLike.setPlaylist(playlist);
            playlistLike.setUser(currentUser);
            playlistLike.setLikeDate(new Date());
            playlistLikeRepository.save(playlistLike);
        }
    }

    @Override
    public void unlikePlaylist(Long playlistId, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        playlistLikeRepository.getByPlaylistIdAndUserId(playlistId, currentUser.getId())
                .ifPresent(playlistLikeRepository::delete);
    }

    @Override
    public List<Playlist> getLikedPlaylists(User user) {
        return playlistRepository.findByUserLikes(user.getId());
    }
}
