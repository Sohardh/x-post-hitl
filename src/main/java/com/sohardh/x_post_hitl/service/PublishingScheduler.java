package com.sohardh.x_post_hitl.service;

import com.sohardh.x_post_hitl.model.ScheduledPost;
import com.sohardh.x_post_hitl.repository.ScheduledPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishingScheduler {

  private final ThreadPoolTaskScheduler taskScheduler;
  private final ScheduledPostRepository scheduledPostRepository;
  private final TelegramBotService telegramBotService;
  private final Random random = new Random();

  /**
   * Schedule all APPROVED posts for a given session at random offsets of 1–8 hours from now.
   */
  @Transactional
  public void scheduleForApprovedPostsInSession(Long sessionId) {
    // Fetch APPROVED posts for the session
    List<ScheduledPost> approved = scheduledPostRepository.findAll().stream()
        .filter(p -> p.getSession() != null && p.getSession().getId().equals(sessionId))
        .filter(p -> p.getStatus() == ScheduledPost.PostStatus.APPROVED)
        .toList();

    if (approved.isEmpty()) {
      log.info("No approved posts to schedule for session {}", sessionId);
      return;
    }

    Instant now = Instant.now();
    for (ScheduledPost post : approved) {
      long hours = 1 + random.nextInt(12); // 1..8
      long jitterMinutes = random.nextInt(60); // additional randomness within the hour
      Instant scheduledTime = now.plus(Duration.ofHours(hours))
          .plus(Duration.ofMinutes(jitterMinutes));

      post.setScheduledTime(scheduledTime);
      scheduledPostRepository.save(post);

      Runnable task = () -> publishPostSafely(post.getId());
      taskScheduler.schedule(task, Date.from(scheduledTime));
      log.info("Scheduled post {} at {} (in ~{}h {}m)", post.getId(), scheduledTime, hours,
          jitterMinutes);
    }
  }

  private void publishPostSafely(Long postId) {
    try {
      publishPost(postId);
    } catch (Exception e) {
      log.error("Failed to publish post {}: {}", postId, e.getMessage(), e);
    }
  }

  @Transactional
  public void publishPost(Long postId) throws Exception {
    ScheduledPost post = scheduledPostRepository.findById(postId)
        .orElse(null);
    if (post == null) {
      log.warn("Scheduled post {} not found when attempting to publish", postId);
      return;
    }
    if (post.getStatus() != ScheduledPost.PostStatus.APPROVED) {
      log.info("Skipping post {}: status is {} (expected APPROVED)", postId, post.getStatus());
      return;
    }

    String text = post.getContent();
    String message = "📢 [SCHEDULED POST]\n\nPlease publish the following post manually on X:\n\n" + text;
    telegramBotService.sendMessage(message);
    post.setStatus(ScheduledPost.PostStatus.PUBLISHED);
    scheduledPostRepository.save(post);
    log.info("Sent post {} to Telegram for manual publishing", postId);
  }
}
