package com.dtbid.dropthebid.exception;

import lombok.Getter;

// 오동건 - 예외처리
@Getter
public class GlobalException extends RuntimeException {

    private final String errorCode;


    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMESSAGE());
        this.errorCode = errorCode.toString();
    }
}
