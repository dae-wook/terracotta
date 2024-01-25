package com.daesoo.terracotta.common.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResponseDto<T> {

    private T result;
    private int statusCode;

    public ResponseDto (T data) {
        this.result = data;
    }

    public static <T> ResponseDto<T> success(HttpStatus httpStatus, T dto) {
        return ResponseDto.<T> builder()
        		.result(dto)
        		.statusCode(httpStatus.value())
        		.build();
    }

    public static ResponseDto<String> fail(HttpStatus httpStatus, String message) {
    	return ResponseDto.<String> builder()
        		.result(message)
        		.statusCode(httpStatus.value())
        		.build();
    }

}
