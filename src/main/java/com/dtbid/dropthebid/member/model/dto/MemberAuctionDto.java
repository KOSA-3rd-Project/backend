package com.dtbid.dropthebid.member.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberAuctionDto {

    private Long memberId;
    private Long auctionId;
    private String itemName;
    private Long startPrice;
    private Long auctionStatusId;
    private String auctionStatusName;
    private String url;
    private Long maxPrice;
}
