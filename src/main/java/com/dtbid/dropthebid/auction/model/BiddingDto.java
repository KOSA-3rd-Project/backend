package com.dtbid.dropthebid.auction.model;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BiddingDto {
  private int bidId;
  private int memberId;
  private String memberEmail;
  private int auctionId;
  private int price;
  Timestamp time;
}
