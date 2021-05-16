package com.etimokhov.funsymusic.controller.rest;

import com.etimokhov.funsymusic.dto.AddTrackCommentDto;
import com.etimokhov.funsymusic.dto.PlaylistDto;
import com.etimokhov.funsymusic.dto.TrackCommentDto;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.TrackComment;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.CommentService;
import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.TrackService;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @PostMapping("/api/comments")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> addComment(@Valid @RequestBody AddTrackCommentDto commentDto, Principal principal) {
        TrackComment comment = commentService.addTrackComment(commentDto, principal.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("comment", commentService.mapToDto(comment));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/comments")
    public List<TrackCommentDto> getTrackComments(@RequestParam Long trackId) {
        Track track = trackService.getTrack(trackId);
        List<TrackComment> trackComments = commentService.findTrackComments(track);
        return trackComments.stream()
                .map(commentService::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/comments/by")
    public ResponseEntity<Map<String, Object>> getComments(@RequestParam Long userId) {
        User requestedUser = userService.getById(userId);
        List<TrackCommentDto> comments = commentService.findLastTrackComments(requestedUser)
                .stream()
                .map(commentService::mapToDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(Map.of(
                "comments", comments
        ), HttpStatus.OK);
    }
}
