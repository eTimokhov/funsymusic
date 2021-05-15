package com.etimokhov.funsymusic.controller.rest;

import com.etimokhov.funsymusic.dto.TrackDto;
import com.etimokhov.funsymusic.dto.form.TrackForm;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.TrackService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TrackRestController {

    private final TrackService trackService;

    public TrackRestController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping("/api/tracks")
    public ResponseEntity<Map<String, Object>> getTracks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId) {

        Page<Track> pageTracks;

        if (userId != null) {
            pageTracks = trackService.findLastUploadedByUploader(userId, page, size);
        } else {
            pageTracks = trackService.findLastUploaded(page, size);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("tracks", pageTracks.getContent().stream().map(trackService::mapToDto).collect(Collectors.toList()));
        response.put("currentPage", pageTracks.getNumber());
        response.put("totalItems", pageTracks.getTotalElements());
        response.put("totalPages", pageTracks.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/tracks/{id}")
    public ResponseEntity<Map<String, Object>> getTrack(@PathVariable Long id) {
        return new ResponseEntity<>(Map.of(
                "track", trackService.mapToDto(trackService.getTrack(id))
        ), HttpStatus.OK);
    }

    @PostMapping("/api/tracks/uploadFile")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> uploadTrack(@RequestParam MultipartFile file) {
        TrackForm trackForm = trackService.processTrackFileUploading(file);
        return new ResponseEntity<>(Map.of(
                "trackInfo", trackForm
        ), HttpStatus.OK);
    }

    @PostMapping("/api/tracks")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> saveTrack(@Valid @RequestBody TrackForm trackForm, Principal principal) {
        Track track = trackService.saveTrack(trackForm, principal.getName());
        return new ResponseEntity<>(Map.of(
                "status", "success",
                "track", trackService.mapToDto(track)
        ), HttpStatus.OK);
    }

}
