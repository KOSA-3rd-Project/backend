package com.dtbid.dropthebid.auction.model;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class AuctionForm {
  private int auctionId;
  private int memberId;
  private int categoryId;
  private int auctionStatusId;
  private int auctionProductStatusId;
  private String itemName;
  private String description;
  private String location;
  private int startPrice;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
  private Timestamp startDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
  private Timestamp dueDate;
}
