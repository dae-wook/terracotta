package com.daesoo.terracotta.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto<T> {

//    private int statusCode;
    private T result;

//    public ResponseDto (int statusCode, T data) {
//        this.statusCode = statusCode;
//        this.result = data;
//    }

    public ResponseDto (T data) {
//        this.statusCode = statusCode;
        this.result = data;
    }

    public static <T> ResponseDto<T> success(T dto) {
        return new ResponseDto<>(dto);
    }

    public static <T> ResponseDto<T> fail(T result) {
        return new ResponseDto<>(result);
    }

}
