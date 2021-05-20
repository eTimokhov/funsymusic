package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.UserEventDto;
import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.TrackComment;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.model.like.PlaylistLike;
import com.etimokhov.funsymusic.model.like.TrackLike;
import com.etimokhov.funsymusic.repository.PlaylistRepository;
import com.etimokhov.funsymusic.repository.TrackCommentRepository;
import com.etimokhov.funsymusic.repository.TrackRepository;
import com.etimokhov.funsymusic.repository.UserRepository;
import com.etimokhov.funsymusic.repository.like.PlaylistLikeRepository;
import com.etimokhov.funsymusic.repository.like.TrackLikeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserEventServiceImpl implements UserEventService {
    private final TrackCommentRepository trackCommentRepository;
    private final TrackRepository trackRepository;
    private final PlaylistRepository playlistRepository;
    private final TrackLikeRepository trackLikeRepository;
    private final PlaylistLikeRepository playlistLikeRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    public UserEventServiceImpl(TrackCommentRepository trackCommentRepository, TrackRepository trackRepository, PlaylistRepository playlistRepository, TrackLikeRepository trackLikeRepository, PlaylistLikeRepository playlistLikeRepository, UserRepository userRepository, UserService userService) {
        this.trackCommentRepository = trackCommentRepository;
        this.trackRepository = trackRepository;
        this.playlistRepository = playlistRepository;
        this.trackLikeRepository = trackLikeRepository;
        this.playlistLikeRepository = playlistLikeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    private List<UserEventDto> getLastEvents(List<Long> userIds) {
        List<TrackComment> trackComments = trackCommentRepository.findTop10ByUserIdInOrderByCommentDateDesc(userIds);
        List<Track> tracks = trackRepository.findTop10ByUploaderIdInOrderByUploadDateDesc(userIds);
        List<Playlist> playlists = playlistRepository.findTop10ByOwnerIdInOrderByCreateDateDesc(userIds);
        List<TrackLike> trackLikes = trackLikeRepository.findTop10ByUserIdInOrderByLikeDateDesc(userIds);
        List<PlaylistLike> playlistLikes = playlistLikeRepository.findTop10ByUserIdInOrderByLikeDateDesc(userIds);

        return Stream.of(
                trackComments.stream().map(this::mapTrackCommentToEventDto),
                tracks.stream().map(this::mapTrackToEventDto),
                playlists.stream().map(this::mapPlaylistToEventDto),
                trackLikes.stream().map(this::mapTrackLikeToEventDto),
                playlistLikes.stream().map(this::mapPlaylistLikeToEventDto)
        )
                .flatMap(Function.identity())
                .sorted(Comparator.comparing(UserEventDto::getEventDate, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());

    }

    @Override
    public List<UserEventDto> getLastEvents(Long userId) {
        return getLastEvents(List.of(userId));
    }

    @Override
    public List<UserEventDto> getLastEventsOfSubscriptions(Long userId) {
        User userWithSubscriptions = userService.getByIdWithSubscriptions(userId);
        List<Long> subscriptionsId = userWithSubscriptions.getSubscriptions().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        return getLastEvents(subscriptionsId);
    }

    private UserEventDto mapTrackCommentToEventDto(TrackComment trackComment) {
        UserEventDto userEventDto = new UserEventDto();
        userEventDto.setAction(UserEventDto.Action.TRACK_COMMENT);
        userEventDto.setTargetId(String.valueOf(trackComment.getTrack().getId()));
        userEventDto.setTargetName(trackComment.getTrack().getName());
        userEventDto.setEventDate(trackComment.getCommentDate());
        userEventDto.setUser(userService.mapToDto(trackComment.getUser()));
        userEventDto.setTargetText(trackComment.getText());
        return userEventDto;
    }

    private UserEventDto mapTrackToEventDto(Track track) {
        UserEventDto userEventDto = new UserEventDto();
        userEventDto.setAction(UserEventDto.Action.NEW_TRACK);
        userEventDto.setTargetId(String.valueOf(track.getId()));
        userEventDto.setTargetName(track.getName());
        userEventDto.setEventDate(track.getUploadDate());
        userEventDto.setUser(userService.mapToDto(track.getUploader()));
        return userEventDto;
    }

    private UserEventDto mapPlaylistToEventDto(Playlist playlist) {
        UserEventDto userEventDto = new UserEventDto();
        userEventDto.setAction(UserEventDto.Action.NEW_PLAYLIST);
        userEventDto.setTargetId(String.valueOf(playlist.getId()));
        userEventDto.setTargetName(playlist.getName());
        userEventDto.setEventDate(playlist.getCreateDate());
        userEventDto.setUser(userService.mapToDto(playlist.getOwner()));
        return userEventDto;
    }

    private UserEventDto mapTrackLikeToEventDto(TrackLike trackLike) {
        UserEventDto userEventDto = new UserEventDto();
        userEventDto.setAction(UserEventDto.Action.TRACK_LIKE);
        userEventDto.setTargetId(String.valueOf(trackLike.getTrack().getId()));
        userEventDto.setTargetName(trackLike.getTrack().getName());
        userEventDto.setEventDate(trackLike.getLikeDate());
        userEventDto.setUser(userService.mapToDto(trackLike.getUser()));
        return userEventDto;
    }

    private UserEventDto mapPlaylistLikeToEventDto(PlaylistLike playlistLike) {
        UserEventDto userEventDto = new UserEventDto();
        userEventDto.setAction(UserEventDto.Action.PLAYLIST_LIKE);
        userEventDto.setTargetId(String.valueOf(playlistLike.getPlaylist().getId()));
        userEventDto.setTargetName(playlistLike.getPlaylist().getName());
        userEventDto.setEventDate(playlistLike.getLikeDate());
        userEventDto.setUser(userService.mapToDto(playlistLike.getUser()));
        return userEventDto;
    }
}
