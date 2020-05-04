package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.Playlist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByOwnerId(Long userId);

    List<Playlist> findTop10ByOwnerIdInOrderByCreateDateDesc(List<Long> userIds);

    @EntityGraph(attributePaths = "tracks")
    Optional<Playlist> findOneWithTracksById(Long playlistId);
}
