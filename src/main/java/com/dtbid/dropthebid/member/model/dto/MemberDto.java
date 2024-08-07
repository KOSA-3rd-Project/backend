package com.dtbid.dropthebid.member.model.dto;

import lombok.Getter;
import lombok.Setter;

// 오동건
@Getter
@Setter
public class MemberDto {

    private Long memberId;
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String sex;
    private String birthday;
    private String address;
    private String authority;
    private String refreshToken;
    private String deleteFlag;
}
