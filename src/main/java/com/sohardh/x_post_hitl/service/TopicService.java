package com.sohardh.x_post_hitl.service;

import com.sohardh.x_post_hitl.model.Topic;
import com.sohardh.x_post_hitl.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

  private final TopicRepository topicRepository;

  public TopicService(TopicRepository topicRepository) {
    this.topicRepository = topicRepository;
  }

  public List<Topic> getAllTopics() {
    return topicRepository.findAll();
  }

  public List<Topic> getActiveTopics() {
    return topicRepository.findByIsActiveTrue();
  }

  public Topic getTopicById(Long id) {
    return topicRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Topic not found with id: " + id));
  }

  public Topic createTopic(Topic topic) {
    return topicRepository.save(topic);
  }

  public Topic updateTopic(Long id, Topic topicDetails) {
    Topic topic = getTopicById(id);
    topic.setName(topicDetails.getName());
    topic.setActive(topicDetails.isActive());
    return topicRepository.save(topic);
  }

  public void deleteTopic(Long id) {
    topicRepository.deleteById(id);
  }
}
