package com.ohgiraffers.unicorn.error.exception;

import com.ohgiraffers.unicorn.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 유효성 검사 실패, 잘못된 파라미터 요청
@Getter
public class Exception400 extends RuntimeException {

    public Exception400(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.BAD_REQUEST);
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}