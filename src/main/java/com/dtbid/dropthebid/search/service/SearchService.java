package com.dtbid.dropthebid.search.service;

import com.dtbid.dropthebid.search.model.SearchDto;
import com.dtbid.dropthebid.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {

  private final SearchRepository searchRepository;

  public List<SearchDto> searchAuctions(String query, int page) {
    int offset = (page - 1) * 9;
    return searchRepository.searchAuctions(query, offset);
  }

  public int getTotalCount(String query) {
    return searchRepository.getTotalCount(query);
  }
}
