package com.etimokhov.funsymusic.controller.rest;

import com.etimokhov.funsymusic.dto.IsTrackInPlaylistDto;
import com.etimokhov.funsymusic.dto.PlaylistDto;
import com.etimokhov.funsymusic.dto.TrackDto;
import com.etimokhov.funsymusic.dto.TrackInPlaylistDto;
import com.etimokhov.funsymusic.dto.UpdatePlaylistDto;
import com.etimokhov.funsymusic.dto.form.PlaylistForm;
import com.etimokhov.funsymusic.exception.NotAuthenticatedException;
import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.LikeService;
import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.TrackService;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class PlaylistRestController {

    private final PlaylistService playlistService;
    private final TrackService trackService;
    private final UserService userService;
    private final LikeService likeService;

    public PlaylistRestController(PlaylistService playlistService, TrackService trackService, UserService userService, LikeService likeService) {
        this.playlistService = playlistService;
        this.trackService = trackService;
        this.userService = userService;
        this.likeService = likeService;
    }

    //TODO: map to dto
    @GetMapping("/api/playlists")
    public ResponseEntity<Map<String, Object>> getPlaylists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId) {

        Page<Playlist> pagePlaylists;
        if (userId != null) {
            pagePlaylists = playlistService.findLastUploadedByOwner(userId, page, size);
        } else {
            pagePlaylists = playlistService.findLastUploaded(page, size);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("playlists", pagePlaylists.getContent().stream().map(playlistService::mapToDto).collect(Collectors.toList()));
        response.put("currentPage", pagePlaylists.getNumber());
        response.put("totalItems", pagePlaylists.getTotalElements());
        response.put("totalPages", pagePlaylists.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/playlists")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> newPlaylist(@Valid @RequestBody PlaylistForm playlistForm, Principal principal) {
        Playlist playlist = playlistService.createPlaylist(playlistForm, principal.getName());
        return new ResponseEntity<>(Map.of(
                "status", "success",
                "playlist", playlistService.mapToDto(playlist)
        ), HttpStatus.OK);
    }

    @GetMapping("api/playlists/{playlistId}")
    public ResponseEntity<Map<String, Object>> getPlaylist(@PathVariable Long playlistId) {
        PlaylistDto playlist = playlistService.mapToDto(playlistService.getPlaylist(playlistId));
        return new ResponseEntity<>(Map.of(
                "playlist", playlist
        ), HttpStatus.OK);
    }

    @GetMapping("/api/playlists/liked")
    public ResponseEntity<Map<String, Object>> getLikedPlaylists(@RequestParam Long userId) {
        User requestedUser = userService.getById(userId);
        List<PlaylistDto> likedPlaylists = likeService.getLikedPlaylists(requestedUser)
                .stream()
                .map(playlistService::mapToDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(Map.of(
                "playlists", likedPlaylists
        ), HttpStatus.OK);
    }

    @PostMapping("/api/playlists/addTrack")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> addTrackToPlaylist(@Valid @RequestBody TrackInPlaylistDto trackInPlaylistDto, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        Playlist playlist = playlistService.getPlaylist(trackInPlaylistDto.getPlaylistId());
        if (!playlistService.isPlaylistOwner(playlist, currentUser)) {
            throw new NotAuthenticatedException();
        }
        Track track = trackService.getTrack(trackInPlaylistDto.getTrackId());
        playlistService.addToPlaylist(playlist, track);
        return new ResponseEntity<>(Map.of(
                "status", "success"
        ), HttpStatus.OK);
    }

    @PostMapping("/api/playlists/removeTrack")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> removeTrackFromPlaylist(@Valid @RequestBody TrackInPlaylistDto trackInPlaylistDto, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        Playlist playlist = playlistService.getPlaylist(trackInPlaylistDto.getPlaylistId());
        if (!playlistService.isPlaylistOwner(playlist, currentUser)) {
            throw new NotAuthenticatedException();
        }
        Track track = trackService.getTrack(trackInPlaylistDto.getTrackId());
        playlistService.removeFromPlaylist(playlist, track);
        return new ResponseEntity<>(Map.of(
                "status", "success"
        ), HttpStatus.OK);
    }

    @GetMapping("/api/playlists/trackInPlaylists")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> getPlaylists(@RequestParam Long trackId, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        Track track = trackService.getTrack(trackId);
        return new ResponseEntity<>(Map.of(
                "itip", playlistService.checkTrackPresenceInUserPlaylists(track, currentUser)
        ), HttpStatus.OK);
    }

    @GetMapping("/api/playlists/getTracks")
    public ResponseEntity<Map<String, Object>> getTracks(@RequestParam Long playlistId) {
        Playlist playlist = playlistService.getPlaylistWithTracks(playlistId);
        List<TrackDto> tracks = playlist.getTracks().stream()
                .map(trackService::mapToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(Map.of(
                "tracks", tracks
        ), HttpStatus.OK);
    }

    @PostMapping("/api/playlists/savePlaylist")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> savePlaylist(@RequestBody UpdatePlaylistDto updatePlaylistDto, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        Playlist playlist = playlistService.getPlaylist(updatePlaylistDto.getPlaylistId());
        if (!playlistService.isPlaylistOwner(playlist, currentUser)) {
            throw new NotAuthenticatedException();
        }
        playlistService.updatePlaylist(playlist, updatePlaylistDto.getTrackIds());
        return new ResponseEntity<>(Map.of(
                "status", "success"
        ), HttpStatus.OK);
    }
}
