package com.etimokhov.funsymusic.controller.advice;

import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.service.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class UserControllerAdvice {

    private final UserService userService;

    public UserControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public User addCurrentUser(Principal principal) {
        return userService.findCurrentUser(principal);
    }
}
