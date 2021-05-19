package com.etimokhov.funsymusic;

import com.etimokhov.funsymusic.repository.PlaylistRepository;
import com.etimokhov.funsymusic.repository.TrackRepository;
import com.etimokhov.funsymusic.service.PlaylistService;
import com.etimokhov.funsymusic.service.UserService;
import com.etimokhov.funsymusic.util.MediaFileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    @Autowired
    private MediaFileUtil mediaFileUtil;

    @Test
    void contextLoads() {
    }

    @Test
    void testFindByPlaylist() throws IOException {
        mediaFileUtil.saveHls("0zZot3oWPG.mp3");
    }

}
