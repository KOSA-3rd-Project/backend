package com.dtbid.dropthebid.auction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.dtbid.dropthebid.auction.model.AuctionDto;
import com.dtbid.dropthebid.auction.model.BiddingDto;
import com.dtbid.dropthebid.auction.model.Image;
import com.dtbid.dropthebid.auction.service.AuctionService;
import com.dtbid.dropthebid.exception.GlobalException;
import com.dtbid.dropthebid.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auctions")
public class AuctionController { // 이윤빈 - 경매 등록, 조회, 취소, 입찰, 입찰 내역 조회
  private final AuctionService auctionService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Transactional
  public ResponseEntity<?> registerAuction(@RequestPart("newAuction") String newAuctionJson,
      @RequestPart("images") List<MultipartFile> images, @RequestPart("mainImageIndex") String mainImageIndex, 
      @AuthenticationPrincipal CustomUserDetails customUserDetails) { // 경매 상품 등록 
    log.info("json " + newAuctionJson);

    if (images.isEmpty())
      log.info("이미지 안넘어옴");
    else
      log.info("이미지 넘어옴");
    
    log.info("메인이미지" + mainImageIndex);

    try {

      int auctionId = auctionService.insertAuction(newAuctionJson, images, mainImageIndex, customUserDetails.getId());

      return new ResponseEntity<>(auctionId, HttpStatus.CREATED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @GetMapping(value = "/all/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> getAuction(@PathVariable("id") int auctionId) { // 경매 정보 가져오기
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
  public ResponseEntity<String> modifyAuction(@PathVariable("id") int auctionId,
      @RequestPart(value="modifiedAuction", required=false) String modifiedAuctionJson,
      @RequestPart(value="newImages", required=false) List<MultipartFile> newImages,
      @RequestPart(value="deletedImages", required=false) String deletedImagesJson,
      @RequestPart(value="mainImageIndex", required=false) String mainImageIndex) { // 경매 상품 수정
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

   @PostMapping("/{id}/bids")
   public ResponseEntity<String> biddingAuction(
       @PathVariable("id") int auctionId, @RequestParam("price") int price, 
       @AuthenticationPrincipal CustomUserDetails customUserDetails){ // 입찰하기
     try {
       System.out.println("????");
       System.out.println(customUserDetails.getId());
       auctionService.insertBidding(auctionId, price, customUserDetails.getUsername());
       
       return new ResponseEntity<>("bidding success", HttpStatus.OK);
     } catch (GlobalException e) {
       return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
     } catch (Exception e) {
       e.printStackTrace();
       return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
     }
   }
   
   @GetMapping("/all/{id}/bids")
   public ResponseEntity<Map<String, Object>> getBiddings(@PathVariable("id") int auctionId){
     try {
       List<BiddingDto> biddings = auctionService.getBiddings(auctionId);
       
       Map<String, Object> response = new HashMap<>();
       response.put("biddingData", biddings);
       
       return ResponseEntity.ok(response);
     } catch (Exception e){
       e.printStackTrace();
       Map<String, Object> errorResponse = new HashMap<>();
       log.info("here {}", e);
       errorResponse.put("error", e.getMessage());
       
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
     }
   }
   
   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteAuction(@PathVariable("id") int auctionId) {
     try {
       if (auctionService.getAuction(auctionId).getAuctionStatusId() == 1) {
         auctionService.updateAuctionStatus(auctionId);
         return new ResponseEntity<>("auction cancle success", HttpStatus.OK);
       } else {
         return new ResponseEntity<>("경매 시작 전인 상품만 취소할 수 있습니다.", HttpStatus.BAD_REQUEST);
       }
      
     } catch (Exception e) {
       e.printStackTrace();
       return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
     }
   }
   
}
