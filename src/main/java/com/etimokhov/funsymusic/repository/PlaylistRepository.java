package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByOwnerId(Long userId);
}
