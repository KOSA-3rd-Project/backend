package com.dtbid.dropthebid.search.controller;

import com.dtbid.dropthebid.search.model.AuctionDto;
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

@RequiredArgsConstructor
@RestController
public class SearchController {

  private final SearchService searchService;

  @GetMapping("/search")
  public ResponseEntity<Map<String, Object>> searchAuctions(
      @RequestParam(value = "q", defaultValue = "") String query,
      @RequestParam(value = "page", defaultValue = "1") int page) {

    List<AuctionDto> auctions = searchService.searchAuctions(query, page);
    int totalCount = searchService.getTotalCount(query);

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
