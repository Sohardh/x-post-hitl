package com.sohardh.x_post_hitl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {

  private final PostSessionService postSessionService;

  public TelegramBotService(@Lazy PostSessionService postSessionService) {
    this.postSessionService = postSessionService;
  }

  @Value("${telegram.bot.name}")
  private String botName;

  @Value("${telegram.bot.token}")
  private String botToken;

  @Value("${telegram.bot.chat-id}")
  private String chatId;

  @Override
  public String getBotUsername() {
    return botName;
  }

  @Override
  public String getBotToken() {
    return botToken;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String messageText = update.getMessage().getText().trim().toLowerCase();
      long updateChatId = update.getMessage().getChatId();
      log.info("Received message: '{}' from chatId: {}", messageText, updateChatId);

      if (chatId != null && !chatId.isEmpty() && !String.valueOf(updateChatId).equals(chatId)) {
        log.warn("Ignoring message from unauthorized chatId: {}", updateChatId);
        return;
      }

      String response;
      if (messageText.equals("more")) {
        response = postSessionService.addMoreDrafts(2);
      } else if (messageText.matches("^[0-9,\\s]+$")) {
        response = postSessionService.approvePosts(messageText);
      } else {
        response = "Unknown command. Use 'more' for more drafts or reply with CSV numbers (e.g., 1,3) to approve.";
      }

      sendMessage(response);
    }
  }

  public void sendMessage(String text) {
    if (chatId == null || chatId.isEmpty()) {
      log.warn("Telegram Chat ID not configured, cannot send message");
      return;
    }
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText(text);
    try {
      execute(message);
    } catch (TelegramApiException e) {
      log.error("Failed to send Telegram message", e);
    }
  }
}
