package com.dtbid.dropthebid.search.service;

import com.dtbid.dropthebid.search.model.SearchDto;
import com.dtbid.dropthebid.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 작성자: 이주윤
 */
@RequiredArgsConstructor
@Service
public class SearchService {

  private final SearchRepository searchRepository;

  public List<SearchDto> searchAuctions(String query, int page, String category, String condition,
      Integer minPrice, Integer maxPrice, String sort) {
    int offset = (page - 1) * 9;
    return searchRepository.searchAuctions(query, offset, category, condition, minPrice, maxPrice, sort);
  }

  public int getTotalCount(String query, String category, String condition,
      Integer minPrice, Integer maxPrice) {
    return searchRepository.getTotalCount(query, category, condition, minPrice, maxPrice);
  }
}
