package com.etimokhov.funsymusic.controller.rest;

import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.service.TrackService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(defaultValue = "10") int size) {

        Page<Track> pageTracks = trackService.findLastUploaded(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("tracks", pageTracks.getContent().stream().map(trackService::mapToDto).collect(Collectors.toList()));
        response.put("currentPage", pageTracks.getNumber());
        response.put("totalItems", pageTracks.getTotalElements());
        response.put("totalPages", pageTracks.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
