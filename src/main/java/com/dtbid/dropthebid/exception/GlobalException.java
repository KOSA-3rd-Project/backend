package com.dtbid.dropthebid.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final String errorCode;


    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMESSAGE());
        this.errorCode = errorCode.toString();
    }
}
