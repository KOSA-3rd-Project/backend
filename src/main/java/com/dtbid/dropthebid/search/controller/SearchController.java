package com.dtbid.dropthebid.search.controller;

import com.dtbid.dropthebid.search.model.SearchDto;
import com.dtbid.dropthebid.search.service.SearchService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 작성자: 이주윤
 */
@RequiredArgsConstructor
@RestController
public class SearchController {

  private final SearchService searchService;

  @GetMapping("/search")
  public ResponseEntity<Map<String, Object>> searchAuctions(
      @RequestParam(value = "q", defaultValue = "") String query,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "category", required = false) String category,
      @RequestParam(value = "condition", required = false) String condition,
      @RequestParam(value = "minPrice", required = false) Integer minPrice,
      @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
      @RequestParam(value = "sort", defaultValue = "latest") String sort) {

    List<SearchDto> auctions = searchService.searchAuctions(query, page, category, condition,
        minPrice, maxPrice, sort);
    int totalCount = searchService.getTotalCount(query, category, condition, minPrice, maxPrice);

    Map<String, Object> response = new HashMap<>();
    response.put("auctions", auctions);
    response.put("currentPage", page);
    response.put("totalPages", (int) Math.ceil((double) totalCount / 9));
    response.put("totalCount", totalCount);
    response.put("message", "검색 성공");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return new ResponseEntity<>(response, headers, HttpStatus.OK);
  }
}
