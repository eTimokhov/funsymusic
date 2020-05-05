package com.etimokhov.funsymusic.controller.rest;

import com.etimokhov.funsymusic.dto.LikeStatusDto;
import com.etimokhov.funsymusic.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LikeRestController {

    private final LikeService likeService;

    public LikeRestController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/like/track")
    public LikeStatusDto getTrackLikeStatus(Long itemId, Principal principal) {
        return likeService.getTrackLikeStatus(itemId, principal);
    }

    @PostMapping("/like/track/perform")
    public ResponseEntity<String> performTrackLikeAction(@RequestParam Long itemId, @RequestParam Boolean setLike, Principal principal) {
        if (setLike) {
            likeService.likeTrack(itemId, principal);
        } else {
            likeService.unlikeTrack(itemId, principal);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/like/playlist")
    public LikeStatusDto getPlaylistLikeStatus(Long itemId, Principal principal) {
        return likeService.getPlaylistLikeStatus(itemId, principal);
    }

    @PostMapping("/like/playlist/perform")
    public ResponseEntity<String> performPlaylistLikeAction(@RequestParam Long itemId, @RequestParam Boolean setLike, Principal principal) {
        if (setLike) {
            likeService.likePlaylist(itemId, principal);
        } else {
            likeService.unlikePlaylist(itemId, principal);
        }
        return ResponseEntity.ok().build();
    }
}
