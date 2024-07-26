package com.dtbid.dropthebid.member.model.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpForm {

    @NotBlank(message = "이메일: 필수 정보입니다.")
    private String email;
    @NotBlank(message = "비밀번호: 필수 정보입니다.")
    private String password;
    private String passwordCheck;
    @NotBlank(message = "닉네임: 필수 정보입니다.")
    private String nickname;
    @NotBlank(message = "이름: 필수 정보입니다.")
    private String name;
    @NotBlank(message = "성별: 필수 정보입니다.")
    private String sex;
    @NotBlank(message = "생년월일: 필수 정보입니다.")
    private String birthday;
    @NotBlank(message = "주소: 필수 정보입니다.")
    private String address;
}
