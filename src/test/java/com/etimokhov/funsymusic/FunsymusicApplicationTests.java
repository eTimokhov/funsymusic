package com.etimokhov.funsymusic;

import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.repository.PlaylistRepository;
import com.etimokhov.funsymusic.repository.TrackRepository;
import com.etimokhov.funsymusic.service.PlaylistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.AssertionErrors;

@SpringBootTest
class FunsymusicApplicationTests {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistService playlistService;

    @Test
    void contextLoads() {
    }

    @Test
    void testFindByPlaylist() {
        Playlist playlist = playlistRepository.findOneWithTracksById(10L).orElseThrow();
        AssertionErrors.assertEquals("wrong query", playlist.getTracks().size(), 4);
    }

}
