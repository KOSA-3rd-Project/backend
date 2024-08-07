package com.dtbid.dropthebid.auction.model;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
  
  // private Integer paymentId;
  private Integer auctionId;
  private Integer biddingSuccessId;
  private String name;
  private String phoneNumber;
  private String address;
  private String message;
  private Timestamp createdAt;
  
}
