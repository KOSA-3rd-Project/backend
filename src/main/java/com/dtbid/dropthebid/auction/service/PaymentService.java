package com.dtbid.dropthebid.auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dtbid.dropthebid.auction.model.Payment;
import com.dtbid.dropthebid.auction.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public void savePayment(Payment payment) {
        paymentRepository.insertPayment(payment);
    }
}
