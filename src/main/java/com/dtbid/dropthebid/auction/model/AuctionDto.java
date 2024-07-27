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
public class AuctionDto {
  private int auctionId;
  private int memberId;
  private int categoryId;
  private int auctionStatusId;
  private int auctionProductStatusId;
  private String itemName;
  private String description;
  private String location;
  private int startPrice;
  private Timestamp startDate;
  private Timestamp dueDate;
  private Timestamp createdAt;
  private Timestamp updatedAt;
}
