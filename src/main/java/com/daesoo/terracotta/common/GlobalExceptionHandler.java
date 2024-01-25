package com.daesoo.terracotta.common;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.daesoo.terracotta.common.dto.ResponseDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<?> handleException(Exception ex) {
    	ex.printStackTrace();
        return ResponseDto.fail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
	
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<?> handle(Exception ex) {
        return ResponseDto.fail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseDto.fail(HttpStatus.BAD_REQUEST, ex.getAllErrors().get(0).getDefaultMessage());
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDto<?> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseDto.fail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
}
