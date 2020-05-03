package com.etimokhov.funsymusic.controller.rest;

import com.etimokhov.funsymusic.dto.AddTrackCommentDto;
import com.etimokhov.funsymusic.dto.TrackCommentDto;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.TrackComment;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.CommentService;
import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.TrackService;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommentRestController {

    private final PlaylistService playlistService;
    private final TrackService trackService;
    private final UserService userService;
    private final CommentService commentService;

    public CommentRestController(PlaylistService playlistService, TrackService trackService, UserService userService, CommentService commentService) {
        this.playlistService = playlistService;
        this.trackService = trackService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @PostMapping("/track/addComment")
    public ResponseEntity<String> addComment(@Valid @RequestBody AddTrackCommentDto commentDto, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        commentService.addTrackComment(commentDto, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/track/comments")
    public List<TrackCommentDto> getTrackComments(@RequestParam Long trackId) {
        Track track = trackService.getTrack(trackId);
        List<TrackComment> trackComments = commentService.findTrackComments(track);
        return trackComments.stream()
                .map(commentService::mapToDto)
                .collect(Collectors.toList());
    }
}
