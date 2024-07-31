package com.dtbid.dropthebid.chat.repository;

import org.apache.ibatis.annotations.Mapper;
import com.dtbid.dropthebid.chat.model.ChatRoom;

@Mapper
public interface ChatRoomRepository {

    void insertChatRoom(ChatRoom chatRoom);
    
}