package com.dtbid.dropthebid.auction.repository;

import java.util.ArrayList;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.dtbid.dropthebid.auction.model.AuctionDto;
import com.dtbid.dropthebid.auction.model.AuctionForm;
import com.dtbid.dropthebid.auction.model.Image;

@Mapper
@Repository
public interface AuctionRepository {
  void insertAuction(AuctionForm newAuction);

  void updateAuction(AuctionForm newAuction);

  void updateAuction(int auctionId);
  
  Optional<AuctionDto> getAuction(int auctionId);

  void insertAuctionImage(Image image);
  
  void deleteAuctionImage(String url);
  
  ArrayList<Image> getAuctionImages(int auctionId);

}
