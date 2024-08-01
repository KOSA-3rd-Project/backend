package com.dtbid.dropthebid.auction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dtbid.dropthebid.auction.model.AuctionDto;
import com.dtbid.dropthebid.auction.model.BiddingDto;
import com.dtbid.dropthebid.auction.model.Image;
import com.dtbid.dropthebid.auction.model.Payment;
import com.dtbid.dropthebid.auction.service.AuctionService;
import com.dtbid.dropthebid.auction.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    
    @Autowired
    private AuctionService auctionService;
  
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody Payment payment) {
        System.out.println("Received payment: " + payment);

        try {
            int auctionId = payment.getAuctionId();
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

            Optional<BiddingDto> biddings = auctionService.getHighestBidding(auctionId);

            System.out.println("==================" + biddings);
            
            Map<String, Object> response = new HashMap<>();
            response.put("biddingData", biddings); 
            response.put("auctionData", auction);
            response.put("images", auctionImagesUrl);
            response.put("mainImageIndex", mainImageIndex);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
