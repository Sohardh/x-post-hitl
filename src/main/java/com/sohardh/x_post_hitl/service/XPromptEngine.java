package com.sohardh.x_post_hitl.service;

import com.sohardh.x_post_hitl.model.Topic;
import com.sohardh.x_post_hitl.repository.TopicRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class XPromptEngine {

    private final ChatClient chatClient;
    private final TopicRepository topicRepository;

    @Value("classpath:prompts/x-post.st")
    private Resource xPostTemplate;

    public XPromptEngine(ChatClient.Builder chatClientBuilder, TopicRepository topicRepository) {
        this.chatClient = chatClientBuilder.build();
        this.topicRepository = topicRepository;
    }

    public List<String> generatePosts(int postCount) {
        List<Topic> activeTopics = topicRepository.findByIsActiveTrue();
        String topicsString = activeTopics.stream()
                .map(Topic::getName)
                .collect(Collectors.joining(", "));

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(xPostTemplate);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of(
                "postCount", postCount,
                "topics", topicsString
        ));

        String response = chatClient.prompt(new Prompt(systemMessage)).call().content();

        if (response == null || response.isBlank()) {
            return List.of();
        }

        return response.lines()
                .filter(line -> !line.isBlank())
                .collect(Collectors.toList());
    }
}
