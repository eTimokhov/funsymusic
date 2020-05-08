package com.etimokhov.funsymusic;

import com.etimokhov.funsymusic.repository.PlaylistRepository;
import com.etimokhov.funsymusic.repository.TrackRepository;
import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FunsymusicApplicationTests {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistService playlistService;

    @Test
    void contextLoads() {
    }

    @Test
    void testFindByPlaylist() {
    }

}
