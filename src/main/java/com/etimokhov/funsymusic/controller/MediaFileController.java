package com.etimokhov.funsymusic.controller;

import com.etimokhov.funsymusic.service.TrackService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class MediaFileController {

    private final TrackService trackService;

    public MediaFileController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping(value = "/media/audio/{trackId}", produces = "audio/mp3")
    public ResponseEntity<byte[]> getMp3(@PathVariable Long trackId) throws IOException {
        String mediaFileFullPath = trackService.getMediaFileFullPath(trackId);
        byte[] mediaData = Files.readAllBytes(Paths.get(mediaFileFullPath));

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        return new ResponseEntity<>(mediaData, headers, HttpStatus.OK);
    }
}
