package com.dtbid.dropthebid.auction.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.dtbid.dropthebid.auction.model.AuctionDto;
import com.dtbid.dropthebid.auction.model.BiddingDto;
import com.dtbid.dropthebid.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class SchedulerServie {
    
  private final AuctionRepository auctionRepository;
  
  @Scheduled(fixedRate = 60000)
  public void run() {
     
    List<AuctionDto> auctions = auctionRepository.getAll();
    
    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    log.info("현재 시간 " + now);
    
    auctions.forEach(auction -> {
      // 경매 상태가 시작 전인 경우 -> 진행 중으로 업데이트
      if (auction.getAuctionStatusId() == 1 &&
          (auction.getStartDate().before(now) || auction.getStartDate().equals(now)) &&
          auction.getDueDate().after(now)) {

          auctionRepository.updateAuctionStatus(auction.getAuctionId(), 2);
          log.info("Auction {} has been started.", auction.getAuctionId());
      }

      // 경매 상태가 진행 중이고 종료 시간이 지난 경우
      if (auction.getAuctionStatusId() == 2 && auction.getDueDate().before(now)) {
        Optional<BiddingDto> bidding = auctionRepository.getHighestBidding(auction.getAuctionId());
        
        if (bidding.isPresent()) { // 입찰이 존재하면 낙찰로 업데이트
          auctionRepository.updateAuctionStatus(auction.getAuctionId(), 3);
          auctionRepository.insertBidingSuccess(bidding.get().getBidId());
          log.info("Auction {} has been successfully closed.", auction.getAuctionId());
        } else { // 입찰이 존재하지 않으면 유찰로 업데이트
          auctionRepository.updateAuctionStatus(auction.getAuctionId(), 4);
          log.info("Auction {} has been failed.", auction.getAuctionId());
        }
      }
    });
  }
  
}
