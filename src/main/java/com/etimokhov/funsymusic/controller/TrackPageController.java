package com.etimokhov.funsymusic.controller;

import com.etimokhov.funsymusic.service.TrackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TrackPageController {

    private final TrackService trackService;

    public TrackPageController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping("/track/{trackId}")
    public String getTrack(@PathVariable Long trackId, Model model) {
        model.addAttribute("track", trackService.getTrack(trackId));
        return "track";
    }

}
