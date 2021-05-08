package com.etimokhov.funsymusic.controller.rest;

import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserRestController {

    private final UserService userService;

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

}
