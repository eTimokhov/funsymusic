package com.etimokhov.funsymusic.repository.like;

import com.etimokhov.funsymusic.model.like.PlaylistLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistLikeRepository extends JpaRepository<PlaylistLike, Long> {
    boolean existsByPlaylistIdAndUserId(Long playlistId, Long userId);

    Optional<PlaylistLike> getByPlaylistIdAndUserId(Long playlistId, Long userId);

    List<PlaylistLike> findTop10ByUserIdInOrderByLikeDateDesc(List<Long> userIds);

    Long countByPlaylistId(Long playlistId);
}
