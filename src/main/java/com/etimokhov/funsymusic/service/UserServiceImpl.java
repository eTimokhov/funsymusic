package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.UserDto;
import com.etimokhov.funsymusic.dto.form.UserForm;
import com.etimokhov.funsymusic.exception.InvalidImageException;
import com.etimokhov.funsymusic.exception.NotAuthenticatedException;
import com.etimokhov.funsymusic.exception.NotFoundException;
import com.etimokhov.funsymusic.model.BlockingRecord;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.repository.BlockingRecordRepository;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BlockingRecordRepository blockingRecordRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MediaFileUtil mediaFileUtil;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BlockingRecordRepository blockingRecordRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MediaFileUtil mediaFileUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.blockingRecordRepository = blockingRecordRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mediaFileUtil = mediaFileUtil;
    }

    @Override
    public User save(UserForm userForm) {
        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
        user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByName("USER"))));
        user.setRegistrationDate(new Date());
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
    public UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setId(user.getId());
        userDto.setImage(user.getImage());
        return userDto;
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
    public User getCurrentUserWithSubscriptions(Principal principal) throws NotAuthenticatedException {
        if (principal == null) {
            throw new NotAuthenticatedException();
        }
        return getByUsernameWithSubscriptions(principal.getName());
    }

    @Override
    public Set<User> getSubscribers(User user) {
        return userRepository.findAllBySubscriptions(user);
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

    @Override
    public void blockUser(String username, Date blockUntil, String reason) {
        User user = findByUsername(username);
        if (user == null) {
            LOG.error("Trying to block unknown user {}", username);
            return;
        }
        BlockingRecord blockingRecord = new BlockingRecord();
        blockingRecord.setUser(user);
        blockingRecord.setReason(reason);
        blockingRecord.setBlockedDate(new Date());
        blockingRecord.setBlockedUntil(blockUntil);
        blockingRecord = blockingRecordRepository.save(blockingRecord);
        LOG.info("Added new blocking record, username: {}, blocked until: {}", blockingRecord.getUser().getUsername(), blockingRecord.getBlockedUntil());
    }

    @Override
    public BlockingRecord findRelevantBlockingRecord(String username) {
        User user = findByUsername(username);
        if (user == null) {
            LOG.error("Trying to find blocking record for unknown user {}", username);
            return null;
        }
        return blockingRecordRepository.findByUserOrderByBlockedUntilDesc(user).orElse(null);
    }

    @Override
    public boolean isBlockingRecordRelevant(BlockingRecord blockingRecord) {
        if (blockingRecord == null) {
            return false;
        }
        return blockingRecord.getBlockedUntil().before(new Date());
    }


}
