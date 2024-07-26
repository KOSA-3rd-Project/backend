package com.dtbid.dropthebid.auction.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.dtbid.dropthebid.auction.model.Auction;
import com.dtbid.dropthebid.auction.model.AuctionForm;
import com.dtbid.dropthebid.auction.model.Image;

@Mapper
@Repository
public interface AuctionRepository {
  void insertAuction(AuctionForm newAuction);

  void updateAuction(Auction acution);

  void deleteAuction(int auctionId);

  void insertAuctionImage(Image image);
}
