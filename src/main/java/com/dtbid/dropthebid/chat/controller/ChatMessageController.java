package com.dtbid.dropthebid.chat.controller;

import java.sql.Timestamp;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.dtbid.dropthebid.chat.model.ChatMessage;

@Controller
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/receive")
    public void handleChatMessage(ChatMessage chatMessage) {
        Long chatRoomId = chatMessage.getChatRoomId();
        Long memberId = chatMessage.getMemberId();
        String message = chatMessage.getMessage();
        Timestamp createdAt = chatMessage.getCreatedAt();

        ChatMessage result = new ChatMessage(chatRoomId, memberId, message, createdAt);

        String destination = "/topic/" + chatRoomId;
        messagingTemplate.convertAndSend(destination, result);
    }
}
