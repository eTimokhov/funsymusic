package com.etimokhov.funsymusic.controller;

import com.etimokhov.funsymusic.dto.form.BlockUserForm;
import com.etimokhov.funsymusic.model.BlockingRecord;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.CommentService;
import com.etimokhov.funsymusic.service.LikeService;
import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.TrackService;
import com.etimokhov.funsymusic.service.UserEventService;
import com.etimokhov.funsymusic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
public class AdminPanelController {

    private final Logger LOG = LoggerFactory.getLogger(UserPageController.class);

    private final UserService userService;
    private final TrackService trackService;
    private final PlaylistService playlistService;
    private final UserEventService userEventService;
    private final LikeService likeService;
    private final CommentService commentService;

    public AdminPanelController(UserService userService, TrackService trackService, PlaylistService playlistService, UserEventService userEventService, LikeService likeService, CommentService commentService) {
        this.userService = userService;
        this.trackService = trackService;
        this.playlistService = playlistService;
        this.userEventService = userEventService;
        this.likeService = likeService;
        this.commentService = commentService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        Page<User> userPage = userService.findLastRegistered(0, Integer.MAX_VALUE);
        model.addAttribute("users", userPage.toList().stream().map(userService::mapToDto).collect(Collectors.toList()));
        return "admin";
    }

    @GetMapping("/admin/user/{username}")
    public String getUserInfoForAdmin(@PathVariable String username, Principal principal, Model model) {
        User requestedUser = userService.getByUsername(username);
        if (principal != null) {
            User currentUser = userService.getCurrentUserWithSubscriptions(principal);
            model.addAttribute("isSubscribed", userService.isSubscribed(currentUser, requestedUser));
        }

        BlockingRecord blockingRecord = userService.findRelevantBlockingRecord(username);

        model.addAttribute("user", requestedUser);
        model.addAttribute("blockingRecord", blockingRecord);
        model.addAttribute("isBlocked", userService.isBlockingRecordRelevant(blockingRecord));
        model.addAttribute("uploadedTracks", trackService.findAllByUploader(requestedUser.getId()));
        model.addAttribute("createdPlaylists", playlistService.findAllByOwner(requestedUser.getId()));
        model.addAttribute("likedTracks", likeService.getLikedTracks(requestedUser));
        model.addAttribute("likedPlaylists", likeService.getLikedPlaylists(requestedUser));
        model.addAttribute("lastComments", commentService.findLastTrackComments(requestedUser));
        model.addAttribute("lastEvents", userEventService.getLastEvents(requestedUser));
        model.addAttribute("blockUserForm", new BlockUserForm());

        return "userInfoForAdmin";
    }

    @PostMapping("/admin/blockUser")
    public String newPlaylist(@ModelAttribute BlockUserForm blockUserForm, Principal principal, Model model) {
        userService.blockUser(blockUserForm.getUsername(), blockUserForm.getBlockUntil(), blockUserForm.getReason());
        return "redirect:/admin/user/" + blockUserForm.getUsername();
    }
}
