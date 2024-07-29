package com.dtbid.dropthebid.auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dtbid.dropthebid.auction.model.Payment;
import com.dtbid.dropthebid.auction.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody Payment payment) {
        System.out.println("Received payment: " + payment);
        
        try {
            paymentService.savePayment(payment);
            return ResponseEntity.ok("Payment saved successfully");
        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace for debugging
            return ResponseEntity.status(500).body("Failed to save payment");
        }
    }
}
