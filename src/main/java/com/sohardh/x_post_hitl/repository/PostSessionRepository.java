package com.sohardh.x_post_hitl.repository;

import com.sohardh.x_post_hitl.model.PostSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostSessionRepository extends JpaRepository<PostSession, Long> {
    Optional<PostSession> findFirstByIsClosedFalseOrderByCreatedAtDesc();
}
