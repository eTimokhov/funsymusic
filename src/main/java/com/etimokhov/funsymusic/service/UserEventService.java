package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.UserEventDto;
import com.etimokhov.funsymusic.model.User;

import java.util.List;

public interface UserEventService {
    List<UserEventDto> getLastEvents(Long userId);
    List<UserEventDto> getLastEventsOfSubscriptions(Long userId);
}
