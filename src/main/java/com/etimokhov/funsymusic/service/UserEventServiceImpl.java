package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.UserEventDto;
import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.TrackComment;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.repository.PlaylistRepository;
import com.etimokhov.funsymusic.repository.TrackCommentRepository;
import com.etimokhov.funsymusic.repository.TrackRepository;
import com.etimokhov.funsymusic.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final UserService userService;


    public UserEventServiceImpl(TrackCommentRepository trackCommentRepository, TrackRepository trackRepository, PlaylistRepository playlistRepository, UserRepository userRepository, UserService userService) {
        this.trackCommentRepository = trackCommentRepository;
        this.trackRepository = trackRepository;
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    private List<UserEventDto> getLastEvents(List<Long> userIds) {
        List<TrackComment> trackComments = trackCommentRepository.findTop10ByUserIdInOrderByCommentDateDesc(userIds);
        List<Track> tracks = trackRepository.findTop10ByUploaderIdInOrderByUploadDateDesc(userIds);
        List<Playlist> playlists = playlistRepository.findTop10ByOwnerIdInOrderByCreateDateDesc(userIds);

        return Stream.of(
                trackComments.stream().map(this::mapTrackCommentToEventDto),
                tracks.stream().map(this::mapTrackToEventDto),
                playlists.stream().map(this::mapPlaylistToEventDto)
        )
                .flatMap(Function.identity())
                .sorted(Comparator.comparing(UserEventDto::getEventDate, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());

    }

    @Override
    public List<UserEventDto> getLastEvents(User user) {
        return getLastEvents(List.of(user.getId()));
    }

    @Override
    public List<UserEventDto> getLastEventsOfSubscriptions(User userWithSubscriptions) {
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
}
