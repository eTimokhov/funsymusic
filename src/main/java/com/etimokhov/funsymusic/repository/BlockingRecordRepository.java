package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.BlockingRecord;
import com.etimokhov.funsymusic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockingRecordRepository extends JpaRepository<BlockingRecord, Long> {
    Optional<BlockingRecord> findByUserOrderByBlockedUntilDesc(User user);
}
