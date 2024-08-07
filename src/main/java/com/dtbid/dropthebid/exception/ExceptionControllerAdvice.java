package com.dtbid.dropthebid.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ExceptionControllerAdvice {

    // 오동건 - Runtime애 대한 예외처리
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ExceptionResponse> globalRequestException(
        final GlobalException globalException) {

        return ResponseEntity.badRequest().body(
            new ExceptionResponse(globalException.getMessage(), globalException.getErrorCode()));
    }


    // 오동건 - Valid에 대한 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validRequestException(
        final MethodArgumentNotValidException methodArgumentNotValidException) {

        ObjectError objectError = methodArgumentNotValidException.getBindingResult()
            .getAllErrors().get(0);

        return ResponseEntity.badRequest().body(
            new ExceptionResponse(objectError.getDefaultMessage(),
                objectError.getObjectName().toUpperCase() + "_" +
                    Objects.requireNonNull(objectError.getCode()).toUpperCase()));
    }


    @Getter
    @AllArgsConstructor
    public static class ExceptionResponse {

        private String message;
        private String errorCode;
    }
}
