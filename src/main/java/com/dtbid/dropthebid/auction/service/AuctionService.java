package com.dtbid.dropthebid.auction.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.dtbid.dropthebid.auction.model.AuctionDto;
import com.dtbid.dropthebid.auction.model.AuctionForm;
import com.dtbid.dropthebid.auction.model.BiddingDto;
import com.dtbid.dropthebid.auction.model.Image;
import com.dtbid.dropthebid.auction.repository.AuctionRepository;
import com.dtbid.dropthebid.exception.ErrorCode;
import com.dtbid.dropthebid.exception.GlobalException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {
  
  private final AuctionRepository auctionRepository;
  private final S3Service s3Service;
  
  private final ObjectMapper objectMapper;
  
  @Transactional
  public int insertAuction(String newAuctionJson, List<MultipartFile> images, String mainImageIndex, Long memberId) {
    try {
      AuctionForm newAuction = objectMapper.readValue(newAuctionJson, AuctionForm.class);
      
      newAuction.setMemberId(memberId); 
      
      Timestamp currentTime = new Timestamp(System.currentTimeMillis());
      
      int comparison = currentTime.compareTo(newAuction.getStartDate());
      
      if (comparison > 0 || comparison == 0) {
        newAuction.setAuctionStatusId(2); // 진행 중 
      } else {
        newAuction.setAuctionStatusId(1); // 시작 전 
      }

      auctionRepository.insertAuction(newAuction);

      int auctionId = newAuction.getAuctionId();
      
      int mainImageIdx = Integer.parseInt(mainImageIndex);
      boolean isMainImage;
      
      for (int i = 0; i < images.size(); i++) {
        MultipartFile image = images.get(i);
        isMainImage = (i == mainImageIdx);
        insertAuctionImage(image, auctionId, isMainImage);
      }
      return auctionId;
    } catch(Exception e) {
      e.printStackTrace();
      return -1;
    }
    
  }
  
  public void insertAuctionImage(MultipartFile file, int auctionId, Boolean isMainImage) throws IOException {
    
    String url = s3Service.uploadImage(file);
    
    Image image = Image.builder()
        .auctionId(auctionId)
        .url(url)
        .extension(file.getContentType())
        .mainImage(isMainImage ? "Y" : "N")
        .imageSize(file.getSize())
        .build();
    auctionRepository.insertAuctionImage(image);
  }
  
  @Transactional
  public void updateAuction(int auctionId, String modifiedAuctionJson, List<MultipartFile> newImages, String deletedImagesJson, String mainImageIndex) {
    try {
      boolean isMainImage;
      int mainImageIdx;
      
      if (mainImageIndex != null) {
        mainImageIdx = Integer.parseInt(mainImageIndex);
      } else {
        mainImageIdx = -1;
      }
      
      if (modifiedAuctionJson != null) {
        AuctionForm modifiedAuction = objectMapper.readValue(modifiedAuctionJson, AuctionForm.class);
        
        auctionRepository.updateAuction(modifiedAuction);
      }
      
      if (newImages != null) {
        for (int i = 0; i < newImages.size(); i++) {
          MultipartFile newImage = newImages.get(i);
          isMainImage = (i == mainImageIdx);
          insertAuctionImage(newImage, auctionId, isMainImage);
        }
      }
      
      // 삭제된 사진 db, s3에서 삭제
      if(deletedImagesJson != null) {
        List<String> deletedImages = objectMapper.readValue(deletedImagesJson, new TypeReference<List<String>>() {});
        
        deletedImages.stream()
          .forEach(imageUrl -> {
          s3Service.deleteImage(imageUrl);
          auctionRepository.deleteAuctionImage(imageUrl);
        });
      }
    } catch(Exception e) {
     e.printStackTrace();
    }

  }

  public AuctionDto getAuction(int auctionId) {
    Optional<AuctionDto> _auction = auctionRepository.getAuction(auctionId);
    
    if(_auction.isPresent()) {
      return _auction.get();
    } else {
      throw new GlobalException(ErrorCode.NOT_EXIST_AUCTION);
    }
  }

  public List<Image> getAuctionImages(int auctionId) {
    List<Image> images = auctionRepository.getAuctionImages(auctionId);
    
    if(images.isEmpty())
      throw new GlobalException(ErrorCode.NOT_EXIST_IMAGES);
      
    return images;
  }

  @Transactional
  public void insertBidding(int auctionId, int price, String memberEmail) {
    try {
        System.out.println("Method insertBidding called");
        
        Optional<BiddingDto> highestBid = auctionRepository.getHighestBidding(auctionId);
        System.out.println("Highest Bid: " + highestBid);
        
        int highestBidPrice = 0;
        
        if (highestBid.isPresent()) {
          highestBidPrice = highestBid.get().getPrice();
        }
        
        if (price > highestBidPrice || highestBidPrice == 0) {
            auctionRepository.insertBidding(auctionId, price, memberEmail);
        } else {
            throw new GlobalException(ErrorCode.NOT_LOWER_BID_PRICE);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
  }


  public List<BiddingDto> getBiddings(int auctionId) {
    List<BiddingDto> biddings = new ArrayList<>();
    
    try { 
      biddings = auctionRepository.getBiddings(auctionId);
     
      log.info("실행됨");
    } catch (Exception e) { 
      e.printStackTrace(); 
    }

    return biddings;
  }

  public void updateAuctionStatus(int auctionId) {
    try {
      auctionRepository.updateAuctionStatus(auctionId, 5);
    } catch(Exception e) {
      e.printStackTrace(); 
    }
    
  }

}
