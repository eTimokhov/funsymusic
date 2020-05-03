package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.form.UserForm;
import com.etimokhov.funsymusic.exception.InvalidImageException;
import com.etimokhov.funsymusic.exception.NotAuthenticatedException;
import com.etimokhov.funsymusic.exception.NotFoundException;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.repository.RoleRepository;
import com.etimokhov.funsymusic.repository.UserRepository;
import com.etimokhov.funsymusic.util.MediaFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    private final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MediaFileUtil mediaFileUtil;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MediaFileUtil mediaFileUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mediaFileUtil = mediaFileUtil;
    }

    @Override
    public User save(UserForm userForm) {
        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
        user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByName("USER"))));
        user.setImage("default");
        userRepository.save(user);
        LOG.info("New user #{}, {}:{} saved.", user.getId(), user.getUsername(), user.getPassword());
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User " + username + " not found"));
    }

    @Override
    public User getCurrentUser(Principal principal) throws NotAuthenticatedException {
        if (principal == null) {
            throw new NotAuthenticatedException();
        }
        return getByUsername(principal.getName());
    }

    @Override
    public User findCurrentUser(Principal principal) {
        if (principal == null) {
            return null;
        }
        return findByUsername(principal.getName());
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User id:" + id + " not found"));
    }

    @Override
    public void uploadImage(User user, MultipartFile imageFile) throws InvalidImageException {
        String imageName = mediaFileUtil.generateRandomFilenameNoExt();
        try {
            mediaFileUtil.saveJpegThumbnails(imageFile.getBytes(), imageName);
        } catch (IOException e) {
            throw new InvalidImageException(e);
        }
        user.setImage(imageName);
        userRepository.save(user);
    }
}
