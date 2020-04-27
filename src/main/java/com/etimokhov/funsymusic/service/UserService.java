package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.exception.NotAuthenticatedException;
import com.etimokhov.funsymusic.model.User;

import java.security.Principal;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
    User getByUsername(String username);

    User getCurrentUser(Principal principal) throws NotAuthenticatedException;

    User findCurrentUser(Principal principal);

    User getById(Long id);
}
