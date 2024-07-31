package com.dtbid.dropthebid.member.repository;

import com.dtbid.dropthebid.member.model.dto.MemberDto;
import com.dtbid.dropthebid.member.model.form.SignUpForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberRepository {

  // 오동건 - 가입된 이메일 확인
  Long countByMemberEmail(String email);

  // 오동건 - 가입된 닉네임 확인
  Long countByNicName(String nickName);

  // 오동건 - 사용자아이디 기준 정보 조히
  MemberDto findByMemberId(Long memberId);

  // 오동건 - 이메일기준 사용자 검색
  MemberDto findByMemberEmail(String email);

  // 오동건 - 회원가입
  void insertMember(SignUpForm signUpForm);

  // 오동건 - refresh token 수정
  void updateMemberToken(
      @Param("memberId") Long memberId, @Param("refreshToken") String refreshToken);

  // 오동건 - 회원 정보 수정
  void updateMemberInfo(
      @Param("memberId") Long memberId, @Param("signUpForm") SignUpForm signUpForm);
}
