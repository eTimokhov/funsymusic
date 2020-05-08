package com.etimokhov.funsymusic.controller;

import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.TrackService;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Collectors;

@Controller
public class BrowsePageController {

    public static final Integer TRACKS_PER_PAGE = 20;
    public static final Integer PLAYLISTS_PER_PAGE = 20;
    public static final Integer USERS_PER_PAGE = 20;


    private final UserService userService;
    private final TrackService trackService;
    private final PlaylistService playlistService;

    public BrowsePageController(UserService userService, TrackService trackService, PlaylistService playlistService) {
        this.userService = userService;
        this.trackService = trackService;
        this.playlistService = playlistService;
    }

    @GetMapping("/browse/tracks")
    public String browseTracks(Model model, @RequestParam(required = false) Integer page) {
        if (page == null) page = 1;
        Page<Track> trackPage = trackService.findLastUploaded(page - 1, TRACKS_PER_PAGE);
        model.addAttribute("tracks", trackPage.toList());
        model.addAttribute("currentPage", trackPage.getNumber() + 1);
        model.addAttribute("hasNextPage", trackPage.hasNext());
        model.addAttribute("hasPreviousPage", trackPage.hasPrevious());
        return "tracks";
    }

    @GetMapping("/browse/playlists")
    public String browsePlaylists(Model model, @RequestParam(required = false) Integer page) {
        if (page == null) page = 1;
        Page<Playlist> playlistPage = playlistService.findLastUploaded(page - 1, PLAYLISTS_PER_PAGE);
        model.addAttribute("playlists", playlistPage.toList());
        model.addAttribute("currentPage", playlistPage.getNumber() + 1);
        model.addAttribute("hasNextPage", playlistPage.hasNext());
        model.addAttribute("hasPreviousPage", playlistPage.hasPrevious());
        return "playlists";
    }

    @GetMapping("/browse/users")
    public String browseUsers(Model model, @RequestParam(required = false) Integer page) {
        if (page == null) page = 1;
        Page<User> userPage = userService.findLastRegistered(page - 1, USERS_PER_PAGE);
        model.addAttribute("users", userPage.toList().stream().map(userService::mapToDto).collect(Collectors.toList()));
        model.addAttribute("currentPage", userPage.getNumber() + 1);
        model.addAttribute("hasNextPage", userPage.hasNext());
        model.addAttribute("hasPreviousPage", userPage.hasPrevious());
        return "users";
    }
}
