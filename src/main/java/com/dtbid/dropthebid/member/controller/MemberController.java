package com.dtbid.dropthebid.member.controller;

import com.dtbid.dropthebid.member.model.form.SignInForm;
import com.dtbid.dropthebid.member.model.form.SignUpForm;
import com.dtbid.dropthebid.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;

  // 오동건 - 회원가입
  @PostMapping("/signup")
  public ResponseEntity<String> memberSignUp(@RequestBody @Valid SignUpForm signUpForm) {

    String signUp = memberService.memberSignUp(signUpForm);

    return new ResponseEntity<>(signUp, HttpStatus.CREATED);
  }

  // 오동건 - 로그인
  @PostMapping("/signin")
  public ResponseEntity<String> memberSignIn(@RequestBody @Valid SignInForm signInForm)
      throws InterruptedException {

    return new ResponseEntity<>(memberService.memberSignIn(signInForm), HttpStatus.CREATED);
  }
}
