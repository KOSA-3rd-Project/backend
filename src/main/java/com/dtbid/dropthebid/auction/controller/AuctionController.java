package com.dtbid.dropthebid.auction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.dtbid.dropthebid.auction.model.AuctionDto;
import com.dtbid.dropthebid.auction.model.Image;
import com.dtbid.dropthebid.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auctions")
public class AuctionController {
  private final AuctionService auctionService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Transactional
  public ResponseEntity<?> registerAuction(@RequestPart("newAuction") String newAuctionJson,
      @RequestPart("images") List<MultipartFile> images, @RequestPart("mainImageIndex") String mainImageIndex) {
    log.info("json " + newAuctionJson);

    if (images.isEmpty())
      log.info("이미지 안넘어옴");
    else
      log.info("이미지 넘어옴");
    
    log.info("메인이미지" + mainImageIndex);

    try {
      auctionService.insertAuction(newAuctionJson, images, mainImageIndex);

      return new ResponseEntity<>("auction register success", HttpStatus.CREATED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> getAuction(@PathVariable("id") int auctionId) {
      try {
          AuctionDto auction = auctionService.getAuction(auctionId);
          List<Image> auctionImages = auctionService.getAuctionImages(auctionId);
          int mainImageIndex = -1;
          List<String> auctionImagesUrl = new ArrayList<>();
          
          for (int i = 0; i < auctionImages.size(); i++) {
              Image image = auctionImages.get(i);
              if ("Y".equals(image.getMainImage())) {
                  mainImageIndex = i;
              }
              if (image.getUrl() != null) {
                  auctionImagesUrl.add(image.getUrl());
              }
          }
          
          Map<String, Object> response = new HashMap<>();
          response.put("auctionData", auction);
          response.put("images", auctionImagesUrl);
          response.put("mainImageIndex", mainImageIndex);
          
          return ResponseEntity.ok(response);
      } catch (Exception e) {
          e.printStackTrace();
          Map<String, Object> errorResponse = new HashMap<>();
          errorResponse.put("error", e.getMessage());
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
      }
  }
  
  @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<?> modifyAuction(@PathVariable("id") int auctionId,
      @RequestPart(value="modifiedAuction", required=false) String modifiedAuctionJson,
      @RequestPart(value="newImages", required=false) List<MultipartFile> newImages,
      @RequestPart(value="deletedImages", required=false) String deletedImagesJson,
      @RequestPart(value="mainImageIndex", required=false) String mainImageIndex) {
    log.info("json " + modifiedAuctionJson);
    log.info("mainImageIndex " + mainImageIndex);
    
    try {
      auctionService.updateAuction(auctionId, modifiedAuctionJson, newImages, deletedImagesJson, mainImageIndex);
      
      return new ResponseEntity<>("auction modify success", HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
