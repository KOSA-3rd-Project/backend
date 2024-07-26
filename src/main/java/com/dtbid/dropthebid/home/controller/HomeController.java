package com.dtbid.dropthebid.home.controller;

import com.dtbid.dropthebid.home.model.AuctionSummaryDto;
import com.dtbid.dropthebid.home.service.HomeService;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auctions")
public class HomeController {

  private final HomeService homeService;

  @GetMapping("/month")
  public ResponseEntity<Map<String, Object>> getAuctionOfTheMonth() {

    AuctionSummaryDto auctionSummaryDto = homeService.getPopularAuctionList().get(0);

    Map<String, Object> response = new HashMap<>();
    response.put("auction", auctionSummaryDto);
    response.put("message", "이 달의 경매 조회 성공");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return new ResponseEntity<>(response, headers, HttpStatus.OK);
  }

  @GetMapping("/popular")
  public ResponseEntity<Map<String, Object>> getPopularAuctions() {

    List<AuctionSummaryDto> auctionSummaryDtoList = homeService.getPopularAuctionList();
    List<AuctionSummaryDto> slicedAuctionSummaryDtoList = null;

    if (auctionSummaryDtoList != null) {
      slicedAuctionSummaryDtoList = auctionSummaryDtoList.subList(1,
          auctionSummaryDtoList.size());
    }

    Map<String, Object> response = new HashMap<>();
    response.put("auctions", slicedAuctionSummaryDtoList);
    response.put("message", "인기 경매 목록 조회 성공");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return new ResponseEntity<>(response, headers, HttpStatus.OK);
  }

  @GetMapping("/new")
  public ResponseEntity<Map<String, Object>> getNewAuctionList() {

    List<AuctionSummaryDto> auctionSummaryDtoList = homeService.getNewAuctionList();

    Map<String, Object> response = new HashMap<>();
    response.put("auctions", auctionSummaryDtoList);
    response.put("message", "신규 경매 목록 조회 성공");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return new ResponseEntity<>(response, headers, HttpStatus.OK);
  }
}
