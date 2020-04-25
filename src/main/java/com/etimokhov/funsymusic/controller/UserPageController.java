package com.etimokhov.funsymusic.controller;

import com.etimokhov.funsymusic.service.TrackService;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserPageController {

    private final UserService userService;
    private final TrackService trackService;

    public UserPageController(UserService userService, TrackService trackService) {
        this.userService = userService;
        this.trackService = trackService;
    }

    @GetMapping("/user/{id}")
    public String getUserInfo(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("uploadedTracks", trackService.findAllByUploader(id));
        return "userInfo";
    }
}
