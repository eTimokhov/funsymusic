package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByOwnerIdOrderByCreateDateDesc(Long userId);

    List<Playlist> findTop10ByOwnerIdInOrderByCreateDateDesc(List<Long> userIds);

    Page<Playlist> findAllByOrderByCreateDateDesc(Pageable pageable);

    Page<Playlist> findAllByOwnerIdOrderByCreateDateDesc(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = "tracks")
    Optional<Playlist> findOneWithTracksById(Long playlistId);

    @Query("select p from PlaylistLike pl join pl.playlist p join pl.user u where u.id = :userId")
    List<Playlist> findByUserLikes(Long userId);
}
