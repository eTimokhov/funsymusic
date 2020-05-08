package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.AddTrackCommentDto;
import com.etimokhov.funsymusic.dto.TrackCommentDto;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.TrackComment;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.repository.TrackCommentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final TrackCommentRepository trackCommentRepository;
    private final TrackService trackService;
    private final UserService userService;

    public CommentServiceImpl(TrackCommentRepository trackCommentRepository, TrackService trackService, UserService userService) {
        this.trackCommentRepository = trackCommentRepository;
        this.trackService = trackService;
        this.userService = userService;
    }

    @Override
    public TrackComment addTrackComment(AddTrackCommentDto commentDto, User user) {
        TrackComment trackComment = new TrackComment();
        trackComment.setTrack(trackService.getTrack(commentDto.getTrackId()));
        trackComment.setText(commentDto.getText());
        trackComment.setTrackTimestamp(commentDto.getTrackTimestamp());
        trackComment.setCommentDate(new Date());
        trackComment.setUser(user);
        return trackCommentRepository.save(trackComment);
    }

    @Override
    public List<TrackComment> findTrackComments(Track track) {
        return trackCommentRepository.findAllByTrackOrderByCommentDateDesc(track);
    }

    @Override
    public TrackCommentDto mapToDto(TrackComment trackComment) {
        TrackCommentDto trackCommentDto = new TrackCommentDto();
        trackCommentDto.setText(trackComment.getText());
        trackCommentDto.setTrackId(trackComment.getTrack().getId());
        trackCommentDto.setTrackTimestamp(trackComment.getTrackTimestamp());
        trackCommentDto.setCommentDate(trackComment.getCommentDate());
        trackCommentDto.setUser(userService.mapToDto(trackComment.getUser()));
        return trackCommentDto;
    }

    @Override
    public List<TrackComment> findLastTrackComments(User user) {
        return trackCommentRepository.findTop10ByUserIdInOrderByCommentDateDesc(List.of(user.getId()));
    }

}
