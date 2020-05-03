package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.UserDto;
import com.etimokhov.funsymusic.dto.form.UserForm;
import com.etimokhov.funsymusic.exception.InvalidImageException;
import com.etimokhov.funsymusic.exception.NotAuthenticatedException;
import com.etimokhov.funsymusic.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface UserService {
    User save(UserForm user);

    User findByUsername(String username);
    User getByUsername(String username);

    User getCurrentUser(Principal principal) throws NotAuthenticatedException;

    User findCurrentUser(Principal principal);

    UserDto mapToDto(User user);

    User getById(Long id);

    void uploadImage(User user, MultipartFile imageFile) throws InvalidImageException;
}
