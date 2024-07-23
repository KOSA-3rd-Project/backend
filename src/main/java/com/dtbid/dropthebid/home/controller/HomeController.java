package com.dtbid.dropthebid.home.controller;

import com.dtbid.dropthebid.home.model.AuctionSummaryDto;
import com.dtbid.dropthebid.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
public class HomeController {

  private final HomeService homeService;

  @GetMapping("/month")
  public ResponseEntity<AuctionSummaryDto> getAuctionOfTheMonth() {

    AuctionSummaryDto auctionSummaryDto = homeService.getPopularAuctionList().get(0);
    return ResponseEntity.ok(auctionSummaryDto);
  }

  @GetMapping("/popular")
  public ResponseEntity<List<AuctionSummaryDto>> getPopularAuctions() {

    List<AuctionSummaryDto> auctionSummaryDtoList = homeService.getPopularAuctionList();
    List<AuctionSummaryDto> slicedAuctionSummaryDtoList = auctionSummaryDtoList.subList(1, auctionSummaryDtoList.size() - 1);

    return ResponseEntity.ok(slicedAuctionSummaryDtoList);
  }
}
