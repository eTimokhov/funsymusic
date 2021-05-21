package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.UserDto;
import com.etimokhov.funsymusic.exception.InvalidImageException;
import com.etimokhov.funsymusic.exception.NotAuthenticatedException;
import com.etimokhov.funsymusic.exception.NotFoundException;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.repository.RoleRepository;
import com.etimokhov.funsymusic.repository.UserRepository;
import com.etimokhov.funsymusic.util.MediaFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

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
    public UserDto mapToDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getImage(), user.getRegistrationDate());
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

    @Override
    public void subscribeTo(User currentUserWithSubscriptions, User subscribeTo) {
        currentUserWithSubscriptions.getSubscriptions().add(subscribeTo);
        userRepository.save(currentUserWithSubscriptions);
        LOG.info("User {} subscribed to user {}", currentUserWithSubscriptions.getUsername(), subscribeTo.getUsername());
    }

    @Override
    public void unsubscribeFrom(User currentUserWithSubscriptions, User unsubscribeFrom) {
        currentUserWithSubscriptions.getSubscriptions().removeIf(s -> s.getId().equals(unsubscribeFrom.getId()));
        userRepository.save(currentUserWithSubscriptions);
        LOG.info("User {} unsubscribed from user {}", currentUserWithSubscriptions.getUsername(), unsubscribeFrom.getUsername());
    }

    @Override
    public User getByUsernameWithSubscriptions(String username) {
        return userRepository.findWithSubscriptionsByUsername(username).orElseThrow(() -> new NotFoundException("User " + username + " not found"));
    }

    @Override
    public User getByIdWithSubscriptions(Long userId) {
        return userRepository.findWithSubscriptionsById(userId).orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
    }

    @Override
    public boolean isSubscribed(User currentUser, User targetUser) {
        return currentUser.getSubscriptions().stream().
                anyMatch(sub -> sub.getId().equals(targetUser.getId()));
    }

    @Override
    public Page<User> findLastRegistered(Integer page, Integer count) {
        return userRepository.findAllByOrderByRegistrationDateDesc(PageRequest.of(page, count));
    }
}
