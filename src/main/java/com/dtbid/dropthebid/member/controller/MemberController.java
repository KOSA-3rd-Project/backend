package com.dtbid.dropthebid.member.controller;

import com.dtbid.dropthebid.auction.service.AuctionService;
import com.dtbid.dropthebid.member.model.dto.MemberAuctionDto;
import com.dtbid.dropthebid.member.model.dto.MemberDto;
import com.dtbid.dropthebid.member.model.form.SignInForm;
import com.dtbid.dropthebid.member.model.form.SignUpForm;
import com.dtbid.dropthebid.member.service.MemberService;
import com.dtbid.dropthebid.security.model.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;
  private final AuctionService auctionService;

  // 오동건 - 회원 정보 조회
  @GetMapping("/info")
  public ResponseEntity<MemberDto> memberInformation(
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    return new ResponseEntity<>(
        memberService.memberInformation(userDetails.getId()), HttpStatus.CREATED);
  }

  // 오동건 - 나의 경매 물품
  @GetMapping("/info/auctions/items")
  public ResponseEntity<Map<String, Object>> memberAuctionItem(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestParam(value = "page", defaultValue = "1") int page) {

    return new ResponseEntity<>(
        auctionService.memberAuctionItem(customUserDetails.getId(), page), HttpStatus.CREATED);
  }

  // 오동건 - 나의 입찰 내역
  @GetMapping("/info/auctions/bidding")
  public ResponseEntity<Map<String, Object>> memberBidding(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestParam(value = "page", defaultValue = "1") int page) {

    return new ResponseEntity<>(
        auctionService.memberBidding(customUserDetails.getId(), page) , HttpStatus.CREATED);
  }

  // 오동건 - 회원가입
  @PostMapping("/signup")
  public ResponseEntity<String> memberSignUp(@RequestBody @Valid SignUpForm signUpForm) {

    return new ResponseEntity<>(memberService.memberSignUp(signUpForm), HttpStatus.CREATED);
  }

  // 오동건 - 로그인
  @PostMapping("/signin")
  public ResponseEntity<String> memberSignIn(@RequestBody @Valid SignInForm signInForm,
      HttpServletResponse response) {

    return new ResponseEntity<>(
        memberService.memberSignIn(signInForm, response), HttpStatus.CREATED);
  }

  // 오동건 - refresh token 확인
  @PostMapping("/checks/refresh-token")
  public ResponseEntity<String> checkRefreshToken(
      @CookieValue(value = "refreshToken", required = false) String refreshToken) {

    return new ResponseEntity<>(memberService.checkRefreshToken(refreshToken), HttpStatus.CREATED);
  }

  // 오동건 - 회원 정보 수정
  @PutMapping("/info")
  public ResponseEntity<MemberDto> memberInfoChange(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody SignUpForm signUpForm) {

    return new ResponseEntity<>(
        memberService.memberInfoChange(customUserDetails.getId(), signUpForm), HttpStatus.CREATED);
  }

  // 오동건 - 로그아웃
  @PutMapping("/logout")
  public ResponseEntity<String> memberLogout(HttpServletResponse response,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    return new ResponseEntity<>(
        memberService.memberLogout(response, customUserDetails.getId()), HttpStatus.CREATED);
  }

  // 오동건 - 회원 탈퇴
  @PutMapping("/info/withdrawal")
  public ResponseEntity<String> memberInfoWithdrawal(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    return new ResponseEntity<>(
        memberService.memberInfoWithdrawal(customUserDetails.getId()), HttpStatus.CREATED);
  }
}
