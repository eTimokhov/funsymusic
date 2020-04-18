package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.Playlist;
import com.etimokhov.funsymusic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
