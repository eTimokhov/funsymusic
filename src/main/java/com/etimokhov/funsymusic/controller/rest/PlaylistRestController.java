package com.etimokhov.funsymusic.controller.rest;

import com.etimokhov.funsymusic.dto.IsTrackInPlaylistDto;
import com.etimokhov.funsymusic.dto.TrackDto;
import com.etimokhov.funsymusic.dto.TrackInPlaylistDto;
import com.etimokhov.funsymusic.dto.UpdatePlaylistDto;
import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.TrackService;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PlaylistRestController {

    private final PlaylistService playlistService;
    private final TrackService trackService;
    private final UserService userService;

    public PlaylistRestController(PlaylistService playlistService, TrackService trackService, UserService userService) {
        this.playlistService = playlistService;
        this.trackService = trackService;
        this.userService = userService;
    }

    @GetMapping("/api/playlists")
    public ResponseEntity<Map<String, Object>> getTracks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Playlist> pagePlaylists = playlistService.findLastUploaded(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("playlists", pagePlaylists.getContent());
        response.put("currentPage", pagePlaylists.getNumber());
        response.put("totalItems", pagePlaylists.getTotalElements());
        response.put("totalPages", pagePlaylists.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
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

    @GetMapping("/playlist/getTracks")
    public List<TrackDto> getTracks(@RequestParam Long playlistId) {
        Playlist playlist = playlistService.getPlaylistWithTracks(playlistId);
        return playlist.getTracks().stream()
                .map(trackService::mapToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/playlist/savePlaylist")
    public ResponseEntity<String> savePlaylist(@RequestBody UpdatePlaylistDto updatePlaylistDto, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        Playlist playlist = playlistService.getPlaylist(updatePlaylistDto.getPlaylistId());
        if (!playlistService.isPlaylistOwner(playlist, currentUser)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not permitted to modify this playlist");
        }
        playlistService.updatePlaylist(playlist, updatePlaylistDto.getTrackIds());
        return ResponseEntity.ok().build();
    }
}
