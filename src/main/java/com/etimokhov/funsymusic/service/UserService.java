package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
    User findById(Long id);
}
