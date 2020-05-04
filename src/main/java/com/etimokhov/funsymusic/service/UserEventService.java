package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.UserEventDto;
import com.etimokhov.funsymusic.model.User;

import java.util.List;

public interface UserEventService {
    List<UserEventDto> getLastEvents(User user);
    List<UserEventDto> getLastEventsOfSubscriptions(User userWithSubscriptions);
}
