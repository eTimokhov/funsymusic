package com.etimokhov.funsymusic.repository.like;

import com.etimokhov.funsymusic.model.like.TrackLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackLikeRepository extends JpaRepository<TrackLike, Long> {
    boolean existsByTrackIdAndUserId(Long trackId, Long userId);

    Optional<TrackLike> getByTrackIdAndUserId(Long trackId, Long userId);

    List<TrackLike> findTop10ByUserIdInOrderByLikeDateDesc(List<Long> userIds);

    Long countByTrackId(Long trackId);
}
