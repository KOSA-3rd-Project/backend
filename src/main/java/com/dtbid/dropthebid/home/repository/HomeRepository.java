package com.dtbid.dropthebid.home.repository;

import com.dtbid.dropthebid.home.model.AuctionSummaryDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface HomeRepository {

  List<AuctionSummaryDto> getPopularAuctionList();

  List<AuctionSummaryDto> getNewAuctionList();
}
