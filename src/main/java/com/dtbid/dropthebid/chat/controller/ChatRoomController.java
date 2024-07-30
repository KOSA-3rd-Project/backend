package com.dtbid.dropthebid.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dtbid.dropthebid.chat.model.ChatRoom;
import com.dtbid.dropthebid.chat.service.ChatRoomService;

@RestController
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping("/chat")
    public ResponseEntity<Integer> createChatRoom(
            @RequestParam(name = "auctionId") Integer auctionId,
            @RequestParam(name = "memberId") Integer memberId) {

        System.out.println("auctionId : " + auctionId);
        System.out.println("memberId : " + memberId);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setAuctionId(auctionId);
        chatRoom.setMemberId(memberId);

        Integer chatRoomId = chatRoomService.createChatRoom(chatRoom);
        System.out.println("chatRoomId : " + chatRoomId);
        
        return ResponseEntity.ok(chatRoomId);
    }

    @GetMapping("/find")
    public Integer getChatRoomId(@RequestParam Integer auctionId, @RequestParam Integer memberId) {
        return chatRoomService.findChatRoomId(auctionId, memberId);
    }
}
