package com.etimokhov.funsymusic.controller;

import com.etimokhov.funsymusic.dto.form.ChangeSubscriptionForm;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
public class UserPageController {

    private final Logger LOG = LoggerFactory.getLogger(UserPageController.class);

    private final UserService userService;
    private final TrackService trackService;
    private final PlaylistService playlistService;
    private final UserEventService userEventService;

    public UserPageController(UserService userService, TrackService trackService, PlaylistService playlistService, UserEventService userEventService) {
        this.userService = userService;
        this.trackService = trackService;
        this.playlistService = playlistService;
        this.userEventService = userEventService;
    }

    @GetMapping("/user/{username}")
    public String getUserInfo(@PathVariable String username, Principal principal, Model model) {
        User requestedUser = userService.getByUsername(username);
        if (isActiveUser(principal, requestedUser)) {
            return "redirect:/user/me";
        }
        if (principal != null) {
            User currentUser = userService.getCurrentUserWithSubscriptions(principal);
            model.addAttribute("isSubscribed", userService.isSubscribed(currentUser, requestedUser));
        }


        model.addAttribute("user", requestedUser);
        model.addAttribute("uploadedTracks", trackService.findAllByUploader(requestedUser.getId()));
        model.addAttribute("createdPlaylists", playlistService.findAllByOwner(requestedUser.getId()));
        model.addAttribute("lastEvents", userEventService.getLastEvents(requestedUser));

        return "userInfo";
    }

    @GetMapping("/user/me")
    public String getActiveUserInfo(Principal principal, Model model) {
        User currentUser = userService.getCurrentUserWithSubscriptions(principal);

        model.addAttribute("user", currentUser);
        model.addAttribute("uploadedTracks", trackService.findAllByUploader(currentUser.getId()));
        model.addAttribute("createdPlaylists", playlistService.findAllByOwner(currentUser.getId()));

        return "activeUserInfo";
    }

    @PostMapping(value = "/user/changeSubscription")
    public String changeSubscription(@RequestParam String username, @RequestParam ChangeSubscriptionForm.Action action, Principal principal) {
        User currentUser = userService.getCurrentUserWithSubscriptions(principal);
        User targetUser = userService.getByUsername(username);

        if (action == ChangeSubscriptionForm.Action.SUBSCRIBE) {
            userService.subscribeTo(currentUser, targetUser);
        } else if (action == ChangeSubscriptionForm.Action.UNSUBSCRIBE) {
            userService.unsubscribeFrom(currentUser, targetUser);
        }

        return "redirect:/user/" + targetUser.getUsername();
    }

    @PostMapping("/user/uploadImage")
    public String uploadImage(@RequestParam MultipartFile imageFile, Principal principal) {
        User user = userService.getCurrentUser(principal);
        LOG.info("Upload image request: file, {}, {} bytes", imageFile.getName(), imageFile.getSize());
        userService.uploadImage(user, imageFile);
        return "redirect:/user/" + user.getUsername();
    }

    private boolean isActiveUser(Principal principal, User requestedUser) {
        return principal != null && principal.getName().equals(requestedUser.getUsername());
    }
}
