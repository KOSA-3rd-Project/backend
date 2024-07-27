package com.dtbid.dropthebid.chat.controller;

import java.sql.Timestamp;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.dtbid.dropthebid.chat.model.ChatMessage;

@Controller
public class ChatController {

    @MessageMapping("/receive")
    @SendTo("/send")
    public ChatMessage SocketHandler(
                                      // @AuthenticationPrincipal UserDetails userDetails,
                                      ChatMessage chatMessage) {
        // String membername = userDetails.getUsername();
        // System.out.println("membername: " + membername);
        
        Long chatRoomId = chatMessage.getChatRoomId();
        Long memberId = 2L;
        String message = chatMessage.getMessage();
        Timestamp createdAt = chatMessage.getCreatedAt();
        
        ChatMessage result = new ChatMessage(chatRoomId, memberId, message, createdAt);

        return result;
    }
}
