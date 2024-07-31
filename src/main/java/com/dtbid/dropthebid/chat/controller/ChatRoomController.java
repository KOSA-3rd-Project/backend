package com.dtbid.dropthebid.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dtbid.dropthebid.chat.model.ChatRoom;
import com.dtbid.dropthebid.chat.service.ChatRoomService;
import com.dtbid.dropthebid.security.model.CustomUserDetails;

@RestController
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping("/chat")
    public ResponseEntity<Long> createChatRoom(@RequestBody ChatRoom chatRoom,
                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        
        Long memberId = customUserDetails.getId();
        chatRoom.setMemberId(memberId);

        Long chatRoomId = chatRoomService.createChatRoom(chatRoom);
        
        return ResponseEntity.ok(chatRoomId);
    }
}