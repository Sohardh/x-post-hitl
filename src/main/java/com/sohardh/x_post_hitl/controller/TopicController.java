package com.sohardh.x_post_hitl.controller;

import com.sohardh.x_post_hitl.model.Topic;
import com.sohardh.x_post_hitl.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

  private final TopicService topicService;

  @GetMapping
  public List<Topic> getAllTopics() {
    return topicService.getAllTopics();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Topic> getTopicById(@PathVariable Long id) {
    return ResponseEntity.ok(topicService.getTopicById(id));
  }

  @PostMapping
  public Topic createTopic(@RequestBody Topic topic) {
    return topicService.createTopic(topic);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @RequestBody Topic topicDetails) {
    return ResponseEntity.ok(topicService.updateTopic(id, topicDetails));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
    topicService.deleteTopic(id);
    return ResponseEntity.noContent().build();
  }
}
