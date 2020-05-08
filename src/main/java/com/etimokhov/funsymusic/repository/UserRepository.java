package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = "subscriptions")
    Optional<User> findWithSubscriptionsByUsername(String username);

    Set<User> findAllBySubscriptions(User subscription);

    Page<User> findAllByOrderByRegistrationDateDesc(Pageable pageable);
}
