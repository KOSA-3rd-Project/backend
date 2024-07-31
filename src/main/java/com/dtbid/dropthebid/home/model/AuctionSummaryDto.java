package com.dtbid.dropthebid.home.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionSummaryDto {
  private Long auctionId;
  private String itemName;
  private Long startPrice;
  private Long highestBid;
  private LocalDateTime dueDate;
  private String url;
}
