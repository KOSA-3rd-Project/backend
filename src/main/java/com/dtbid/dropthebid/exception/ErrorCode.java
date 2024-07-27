package com.dtbid.dropthebid.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 오동건 - 에러코드
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 회원
    ALREADY_MEMBER_EMAIL("이미 가입된 이메일 입니다."),
    UNABLE_LOGIN("아이디 또는 비밀번호가 잘못 되었습니다."),
    
    // 이윤빈 - 경매
    NOT_EXIST_AUCTION("존재하지 않는 상품입니다."),
    NOT_EXIST_IMAGES("이미지가 존재하지 않습니다.")
    ;
  
  

    private final String MESSAGE;
}
