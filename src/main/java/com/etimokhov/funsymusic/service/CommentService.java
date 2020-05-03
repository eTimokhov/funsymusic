package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.AddTrackCommentDto;
import com.etimokhov.funsymusic.dto.TrackCommentDto;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.TrackComment;
import com.etimokhov.funsymusic.model.User;

import java.util.List;

public interface CommentService {
    TrackComment addTrackComment(AddTrackCommentDto commentDto, User user);

    List<TrackComment> findTrackComments(Track track);

    TrackCommentDto mapToDto(TrackComment trackComment);
}