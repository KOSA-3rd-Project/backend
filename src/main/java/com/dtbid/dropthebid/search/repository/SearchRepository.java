package com.dtbid.dropthebid.search.repository;

import com.dtbid.dropthebid.search.model.SearchDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SearchRepository {

  List<SearchDto> searchAuctions(@Param("query") String query, @Param("offset") int offset,
      @Param("category") String category, @Param("condition") String condition,
      @Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice,
      @Param("sort") String sort);

  int getTotalCount(@Param("query") String query, @Param("category") String category,
      @Param("condition") String condition,
      @Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice);
}
