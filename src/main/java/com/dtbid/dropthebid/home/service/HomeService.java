package com.dtbid.dropthebid.home.service;

import com.dtbid.dropthebid.home.model.AuctionSummaryDto;
import com.dtbid.dropthebid.home.repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

  private final HomeRepository homeRepository;

  public List<AuctionSummaryDto> getPopularAuctionList() {
    return homeRepository.getPopularAuctionList();
  }

  public List<AuctionSummaryDto> getNewAuctionList() {
    return homeRepository.getNewAuctionList();
  }
}
