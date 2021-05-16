package com.etimokhov.funsymusic.controller.rest;

import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.security.UserDetailsImpl;
import com.etimokhov.funsymusic.service.UserEventService;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EventRestController {

    private final UserEventService userEventService;
    private final UserService userService;

    public EventRestController(UserEventService userEventService, UserService userService) {
        this.userEventService = userEventService;
        this.userService = userService;
    }

    @GetMapping("/api/events/{userId}")
    public ResponseEntity<Map<String, Object>> getEvents(@PathVariable Long userId) {
        return new ResponseEntity<>(Map.of(
                "events", userEventService.getLastEvents(userId)
        ), HttpStatus.OK);
    }

    @GetMapping("/api/events/subs")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> getEvents(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        return new ResponseEntity<>(Map.of(
                "events", userEventService.getLastEventsOfSubscriptions(user.getId())
        ), HttpStatus.OK);
    }

}
