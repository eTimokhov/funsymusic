package com.etimokhov.funsymusic.controller.ajax;

import com.etimokhov.funsymusic.dto.IsTrackInPlaylistDto;
import com.etimokhov.funsymusic.dto.TrackInPlaylistDto;
import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.TrackService;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class AjaxPlaylistController {

    private final PlaylistService playlistService;
    private final TrackService trackService;
    private final UserService userService;

    public AjaxPlaylistController(PlaylistService playlistService, TrackService trackService, UserService userService) {
        this.playlistService = playlistService;
        this.trackService = trackService;
        this.userService = userService;
    }

    @PostMapping("/playlist/addTrack")
    public ResponseEntity<String> addTrackToPlaylist(@RequestBody TrackInPlaylistDto trackInPlaylistDto, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        Playlist playlist = playlistService.getPlaylist(trackInPlaylistDto.getPlaylistId());
        if (!playlistService.isPlaylistOwner(playlist, currentUser)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not permitted to modify this playlist");
        }
        Track track = trackService.getTrack(trackInPlaylistDto.getTrackId());
        playlistService.addToPlaylist(playlist, track);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/playlist/removeTrack")
    public ResponseEntity<String> removeTrackFromPlaylist(@RequestBody TrackInPlaylistDto trackInPlaylistDto, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        Playlist playlist = playlistService.getPlaylist(trackInPlaylistDto.getPlaylistId());
        if (!playlistService.isPlaylistOwner(playlist, currentUser)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not permitted to modify this playlist");
        }
        Track track = trackService.getTrack(trackInPlaylistDto.getTrackId());
        playlistService.removeFromPlaylist(playlist, track);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/playlist/trackInPlaylists")
    public List<IsTrackInPlaylistDto> getPlaylists(@RequestParam Long trackId, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        Track track = trackService.getTrack(trackId);
        return playlistService.checkTrackPresenceInUserPlaylists(track, currentUser);
    }
}
