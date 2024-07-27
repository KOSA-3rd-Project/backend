package com.dtbid.dropthebid.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    
    private Long chatRoomId;
    private Long auctionId;
    private Long memberId;
}
