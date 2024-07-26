package com.dtbid.dropthebid.search.repository;

import com.dtbid.dropthebid.search.model.AuctionDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SearchRepository {

  List<AuctionDto> searchAuctions(@Param("query") String query, @Param("offset") int offset);

  int getTotalCount(@Param("query") String query);
}
