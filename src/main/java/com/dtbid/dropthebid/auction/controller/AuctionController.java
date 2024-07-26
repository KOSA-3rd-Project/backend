package com.dtbid.dropthebid.auction.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.dtbid.dropthebid.auction.model.AuctionForm;
import com.dtbid.dropthebid.auction.service.AuctionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuctionController {
  private final AuctionService auctionService;
  private final ObjectMapper objectMapper;

  @PostMapping(value = "/auctions", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Transactional
  public ResponseEntity<?> registerAuction(@RequestPart("newAuction") String newAuctionJson,
      @RequestPart("images") List<MultipartFile> images) {
    log.info("json " + newAuctionJson);

    if (images.isEmpty())
      log.info("이미지 안넘어옴");
    else
      log.info("이미지 넘어옴");

    try {

      AuctionForm newAuction = objectMapper.readValue(newAuctionJson, AuctionForm.class);
      newAuction.setMemberId(2); // 로그인이랑 합치면 수정
      
      Timestamp curruentTime = new Timestamp(System.currentTimeMillis());
      
      //if (newAuction.getStartDate() >= )
      newAuction.setAuctionStatusId(1);

      auctionService.insertAuction(newAuction);

      int auctionId = newAuction.getAuctionId();

      boolean isFirstImage = true;
      for (MultipartFile image : images) {
        auctionService.insertAuctionImage(image, auctionId, isFirstImage);
        isFirstImage = false;
      }

      return new ResponseEntity<>("Success", HttpStatus.CREATED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
