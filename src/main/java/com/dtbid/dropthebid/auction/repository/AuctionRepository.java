package com.dtbid.dropthebid.auction.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.dtbid.dropthebid.auction.model.AuctionDto;
import com.dtbid.dropthebid.auction.model.AuctionForm;
import com.dtbid.dropthebid.auction.model.BiddingDto;
import com.dtbid.dropthebid.auction.model.Image;

@Mapper
@Repository
public interface AuctionRepository {
  void insertAuction(AuctionForm newAuction);

  void updateAuction(AuctionForm newAuction);

  void updateAuctionStatus(@Param("auctionId") int auctionId, @Param("auctionStatusId") int auctionStatusId);
  
  List<AuctionDto> getAll();
  
  Optional<AuctionDto> getAuction(int auctionId);

  void insertAuctionImage(Image image);
  
  void deleteAuctionImage(String url);
  
  ArrayList<Image> getAuctionImages(int auctionId);

  void insertBidding(@Param("auctionId") int auctionId, @Param("price") int price, @Param("memberEmail") String memberEmail);

  Optional<BiddingDto> getHighestBidding(int auctionId);
  
  List<BiddingDto> getBiddings(int auctionId);

  void insertBidingSuccess(int bidId);


}
