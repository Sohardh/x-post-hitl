package com.sohardh.x_post_hitl.repository;

import com.sohardh.x_post_hitl.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByIsActiveTrue();
}
