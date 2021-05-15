package com.etimokhov.funsymusic.controller.rest;

import com.etimokhov.funsymusic.dto.LikeStatusDto;
import com.etimokhov.funsymusic.dto.form.LikeForm;
import com.etimokhov.funsymusic.service.LikeService;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LikeRestController {

    private final LikeService likeService;
    private final UserService userService;

    public LikeRestController(LikeService likeService, UserService userService) {
        this.likeService = likeService;
        this.userService = userService;
    }

    @GetMapping("/api/track/{id}/like")
    public ResponseEntity<Map<String, Object>> getTrackLikeStatus(@PathVariable Long id, Principal principal) {
        return new ResponseEntity<>(Map.of(
                "likeInfo", likeService.getTrackLikeStatus(id, principal)
        ), HttpStatus.OK);
    }

    @PostMapping("/api/track/{id}/like")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> performTrackLikeAction(@PathVariable Long id, @RequestBody @Valid LikeForm likeForm, Principal principal) {
        if (likeForm.getAction() == LikeForm.Action.SET) {
            likeService.likeTrack(id, principal);
        } else if (likeForm.getAction() == LikeForm.Action.UNSET){
            likeService.unlikeTrack(id, principal);
        } else throw new IllegalArgumentException("Action is wrong");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/playlist/{id}/like")
    public ResponseEntity<Map<String, Object>> getPlaylistLikeStatus(@PathVariable Long id, Principal principal) {
        return new ResponseEntity<>(Map.of(
                "likeInfo", likeService.getPlaylistLikeStatus(id, principal)
        ), HttpStatus.OK);
    }

    @PostMapping("/api/playlist/{id}/like")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> performPlaylistLikeAction(@PathVariable Long id, @RequestBody @Valid LikeForm likeForm, Principal principal) {
        if (likeForm.getAction() == LikeForm.Action.SET) {
            likeService.likePlaylist(id, principal);
        } else if (likeForm.getAction() == LikeForm.Action.UNSET){
            likeService.unlikePlaylist(id, principal);
        } else throw new IllegalArgumentException("Action is wrong");
        return ResponseEntity.ok().build();
    }
}
