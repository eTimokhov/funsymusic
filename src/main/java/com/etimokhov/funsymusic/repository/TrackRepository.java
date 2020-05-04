package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findByUploaderId(Long userId);
    List<Track> findTop10ByUploaderIdInOrderByUploadDateDesc(List<Long> userIds);
}
