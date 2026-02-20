package com.sohardh.x_post_hitl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerationScheduler {

  private final PostSessionService postSessionService;
  private final TelegramBotService telegramBotService;

  @Scheduled(cron = "0 0 8 * * ?", zone = "Asia/Calcutta")
  public void generateDailyPosts() {
    log.info("Triggering daily post generation at 8 AM IST");
    String message = postSessionService.startNewSession(3);
    telegramBotService.sendMessage(message);
    log.info("Daily generation complete");
  }
}
