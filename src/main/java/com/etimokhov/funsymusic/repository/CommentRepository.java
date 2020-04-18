package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
