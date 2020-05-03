package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.TrackComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackCommentRepository extends JpaRepository<TrackComment, Long> {
    List<TrackComment> findAllByTrackOrderByCommentDateDesc(Track track);
}
