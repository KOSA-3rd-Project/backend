package com.dtbid.dropthebid.auction.repository;

import org.apache.ibatis.annotations.Mapper;
import com.dtbid.dropthebid.auction.model.Payment;

@Mapper
public interface PaymentRepository {

  void insertPayment(Payment payment);
  
}