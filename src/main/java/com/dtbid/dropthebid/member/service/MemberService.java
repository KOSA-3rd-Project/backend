package com.dtbid.dropthebid.member.service;

import com.dtbid.dropthebid.exception.GlobalException;
import com.dtbid.dropthebid.member.model.dto.MemberDto;
import com.dtbid.dropthebid.member.model.form.SignInForm;
import com.dtbid.dropthebid.member.model.form.SignUpForm;
import com.dtbid.dropthebid.member.repository.MemberRepository;
import com.dtbid.dropthebid.security.component.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dtbid.dropthebid.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Value("${REFRESH_EXPIRE_TIME}")
  private int REFRESH_EXPIRE_TIME;


  // 오동건 - 사용자아이디 기준 정보 조히
  public MemberDto memberInformation(Long memberId) {

    return memberRepository.findByMemberId(memberId);
  }

  // 오동건 - 회원가입
  public String memberSignUp(SignUpForm signUpForm) {

    // 이메일 중복 확인
    if (memberRepository.countByMemberEmail(signUpForm.getEmail()) > 0) {
      throw new GlobalException(ALREADY_MEMBER_EMAIL);
    }

    // 닉네임 중복 확인
    if (memberRepository.countByNicName(signUpForm.getNickname()) > 0) {
      throw new GlobalException(ALREADY_NICKNAME);

    }

    signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));

    memberRepository.insertMember(signUpForm);

    return "회원가입이 되었습니다.";
  }

  // 오동건 - 로그인
  public String memberSignIn(SignInForm signInForm, HttpServletResponse response) {

    // 아이디 확인
    MemberDto memberDto = memberRepository.findByMemberEmail(signInForm.getEmail());
    if (memberDto == null) {
      throw new GlobalException(UNABLE_LOGIN);
    }

    // 비밀번호 확인
    if (!passwordEncoder.matches(signInForm.getPassword(), memberDto.getPassword())) {
      throw new GlobalException(UNABLE_LOGIN);
    }

    // refresh token 생성
    String refreshToken = jwtTokenProvider.createRefreshToken(
        memberDto.getMemberId(), memberDto.getEmail(), memberDto.getAuthority());

    // refresh token 수정 (재등록)
    memberRepository.updateMemberToken(memberDto.getMemberId(), refreshToken);

    // refresh token 쿠키에 저장
    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
    refreshTokenCookie.setHttpOnly(true);   // 스크립트에서 접근 불가
    refreshTokenCookie.setSecure(false);    // HTTPS를 사용하는 경우에 사용
    refreshTokenCookie.setPath("/");        // 쿠키 경로 설정
    refreshTokenCookie.setMaxAge(REFRESH_EXPIRE_TIME / 1000); // 시간 설정
    response.addCookie(refreshTokenCookie);

    return jwtTokenProvider.createAccessToken(
        memberDto.getMemberId(), memberDto.getEmail(), memberDto.getAuthority());
  }

  // 오동건 - 토큰 사용 이메일 조회
  public String checkRefreshToken(String token) {

    // refresh Token 으로 이메일 찾기
    String email = jwtTokenProvider.getTokenEmail(token, false);

    MemberDto memberDto = memberRepository.findByMemberEmail(email);

    // 토큰 비교
    if (token.equals(memberDto.getRefreshToken())) {

      return jwtTokenProvider.createAccessToken(memberDto.getMemberId(), memberDto.getEmail(),
          memberDto.getAuthority());
    } else {
      throw new GlobalException(NOT_FIND_TOKEN);
    }
  }

  // 오동건 - 회원 정보 수정
  @Transactional
  public MemberDto memberInfoChange(Long memberId, SignUpForm signUpForm) {

    signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));

    memberRepository.updateMemberInfo(memberId, signUpForm);

    return memberRepository.findByMemberId(memberId);
  }
}
