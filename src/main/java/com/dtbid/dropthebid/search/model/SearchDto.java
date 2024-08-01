package com.dtbid.dropthebid.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 작성자: 이주윤
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {
  private Long auctionId;
  private String itemName;
  private Long startPrice;
  private Long highestBid;
  private String url;
}

