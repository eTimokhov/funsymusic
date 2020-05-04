package com.etimokhov.funsymusic.controller;

import com.etimokhov.funsymusic.dto.UserEventDto;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.TrackService;
import com.etimokhov.funsymusic.service.UserEventService;
import com.etimokhov.funsymusic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class FeedController {

    private final Logger LOG = LoggerFactory.getLogger(FeedController.class);

    private final UserService userService;
    private final UserEventService userEventService;
    private final TrackService trackService;
    private final PlaylistService playlistService;

    public FeedController(UserService userService, UserEventService userEventService, TrackService trackService, PlaylistService playlistService) {
        this.userService = userService;
        this.userEventService = userEventService;
        this.trackService = trackService;
        this.playlistService = playlistService;
    }

    @GetMapping("/feed")
    public String getFeed(Principal principal, Model model) {
        User user = userService.getCurrentUserWithSubscriptions(principal);
        List<UserEventDto> eventDtos = userEventService.getLastEventsOfSubscriptions(user);
        model.addAttribute("lastEvents", eventDtos);
        return "feed";
    }

}
