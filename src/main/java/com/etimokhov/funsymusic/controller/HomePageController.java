package com.etimokhov.funsymusic.controller;

import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.TrackService;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
public class HomePageController {

    private final UserService userService;
    private final TrackService trackService;
    private final PlaylistService playlistService;

    public HomePageController(UserService userService, TrackService trackService, PlaylistService playlistService) {
        this.userService = userService;
        this.trackService = trackService;
        this.playlistService = playlistService;
    }


    @GetMapping(value = {"/", "/home"})
    public String home(Model model) {
        model.addAttribute("lastTracks", trackService.findLastUploaded(0, 10).toList());
        model.addAttribute("lastPlaylists", playlistService.findLastUploaded(0, 10).toList());
        model.addAttribute("lastUsers", userService.findLastRegistered(0, 10).toList().stream()
                .map(userService::mapToDto).collect(Collectors.toList())
        );
        return "home";
    }
}
