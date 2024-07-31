package com.dtbid.dropthebid.chat.controller;

import java.sql.Timestamp;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import com.dtbid.dropthebid.chat.model.ChatMessage;
import com.dtbid.dropthebid.security.model.CustomUserDetails;

@Controller
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/receive")
    public void handleChatMessage(@Payload ChatMessage chatMessage,
                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails != null) {
            Long memberId = customUserDetails.getId();
            chatMessage.setMemberId(memberId);

            Long chatRoomId = chatMessage.getChatRoomId();
            String message = chatMessage.getMessage();
            Timestamp createdAt = chatMessage.getCreatedAt();

            // 유효성 검사 및 필요한 처리 추가
            if (chatRoomId != null && message != null && createdAt != null) {
                ChatMessage result = new ChatMessage(chatRoomId, memberId, message, createdAt);

                String destination = "/topic/" + chatRoomId;
                messagingTemplate.convertAndSend(destination, result);
            } else {
                // 유효하지 않은 데이터 처리 (예: 로그 기록, 에러 응답 등)
                System.err.println("Invalid chat message received: " + chatMessage);
            }
        } else {
            System.err.println("User not authenticated");
        }
    }
}
