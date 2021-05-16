package com.etimokhov.funsymusic.controller.rest;

import com.etimokhov.funsymusic.dto.form.ChangeSubscriptionForm;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserRestController {

    private final UserService userService;

    private final Logger LOG = LoggerFactory.getLogger(UserRestController.class);

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<User> pageUsers = userService.findLastRegistered(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("users", pageUsers.getContent().stream().map(userService::mapToDto).collect(Collectors.toList()));
        response.put("currentPage", pageUsers.getNumber());
        response.put("totalItems", pageUsers.getTotalElements());
        response.put("totalPages", pageUsers.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable Long id, Principal principal) {
        User requestedUser = userService.getById(id);
        return new ResponseEntity<>(Map.of(
                "user", userService.mapToDto(requestedUser)
        ), HttpStatus.OK);

    }

    @GetMapping("/api/users/{id}/sub")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> getSubscription(@PathVariable Long id, Principal principal) {
        User requestedUser = userService.getById(id);
        User currentUser = userService.getByUsernameWithSubscriptions(principal.getName());
        return new ResponseEntity<>(Map.of(
                "isSubscribed", userService.isSubscribed(currentUser, requestedUser)
        ), HttpStatus.OK);
    }

    @PostMapping("/api/users/{id}/sub")
    public ResponseEntity<Map<String, Object>> changeSubscription(@PathVariable Long id, @Valid @RequestBody ChangeSubscriptionForm subscriptionForm, Principal principal) {
        User currentUser = userService.getByUsernameWithSubscriptions(principal.getName());
        User targetUser = userService.getById(id);

        if (subscriptionForm.getAction() == ChangeSubscriptionForm.Action.SUBSCRIBE) {
            userService.subscribeTo(currentUser, targetUser);
        } else if (subscriptionForm.getAction() == ChangeSubscriptionForm.Action.UNSUBSCRIBE) {
            userService.unsubscribeFrom(currentUser, targetUser);
        } else throw new IllegalArgumentException("Action is wrong.");

        return new ResponseEntity<>(Map.of(
                "status", "success"
        ), HttpStatus.OK);
    }

    @PostMapping("/api/user/image")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam MultipartFile file, Principal principal) {
        User user = userService.getCurrentUser(principal);
        LOG.info("Upload image request: file, {}, {} bytes", file.getName(), file.getSize());
        userService.uploadImage(user, file);
        return new ResponseEntity<>(Map.of(
                "status", "success"
        ), HttpStatus.OK);
    }

}
