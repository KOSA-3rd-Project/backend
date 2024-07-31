package com.dtbid.dropthebid.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dtbid.dropthebid.chat.model.ChatRoom;
import com.dtbid.dropthebid.chat.repository.ChatRoomRepository;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public Long createChatRoom(ChatRoom chatRoom) {
        try {
            chatRoomRepository.insertChatRoom(chatRoom);
            return chatRoom.getChatRoomId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create chat room", e);
        }
    }

}