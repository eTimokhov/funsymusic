package com.etimokhov.funsymusic.controller;

import com.etimokhov.funsymusic.dto.form.PlaylistForm;
import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class PlaylistPageController {

    private final PlaylistService playlistService;
    private final UserService userService;

    public PlaylistPageController(PlaylistService playlistService, UserService userService) {
        this.playlistService = playlistService;
        this.userService = userService;
    }

    @GetMapping("/playlist/{playlistId}")
    public String getPlaylist(@PathVariable Long playlistId, Model model) {
        model.addAttribute("playlist", playlistService.getPlaylist(playlistId));
        return "playlist";
    }

    @GetMapping("/playlist/new")
    public String newPlaylist(Model model) {
        model.addAttribute("playlistForm", new PlaylistForm());
        return "newPlaylist";
    }

    @PostMapping("/playlist/new")
    public String newPlaylist(@ModelAttribute @Valid PlaylistForm playlistForm, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            return "newPlaylist";
        }
        User currentUser = userService.getCurrentUser(principal);
        Playlist playlist = playlistService.createPlaylist(playlistForm, currentUser);
        return "redirect:/playlist/" + playlist.getId();
    }
}
