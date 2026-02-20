package com.sohardh.x_post_hitl.repository;

import com.sohardh.x_post_hitl.model.PostSession;
import com.sohardh.x_post_hitl.model.ScheduledPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledPostRepository extends JpaRepository<ScheduledPost, Long> {

  List<ScheduledPost> findBySession(PostSession session);

  List<ScheduledPost> findBySessionAndStatus(PostSession session, ScheduledPost.PostStatus status);

  List<ScheduledPost> findByStatus(ScheduledPost.PostStatus status);
}
