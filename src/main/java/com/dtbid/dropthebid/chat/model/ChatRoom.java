package com.dtbid.dropthebid.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    
    private Integer chatRoomId; // 데이터베이스에서 자동 생성된 값이 매핑됨
    private Integer auctionId;
    private Integer memberId;
}
