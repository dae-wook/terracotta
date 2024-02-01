package com.daesoo.terracotta.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.daesoo.terracotta.common.dto.ErrorResponseDto;
import com.daesoo.terracotta.common.dto.ErrorType;
import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.common.exception.DuplicationException;
import com.daesoo.terracotta.common.exception.UnauthorizedException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<ErrorResponseDto> handleException(Exception ex) {
    	log.error(" occurred: {}", ex.getMessage());
    	ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorType.EXCEPTION, ex.getMessage());
        return ResponseDto.fail(HttpStatus.INTERNAL_SERVER_ERROR, errorResponseDto);
    }
	
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<ErrorResponseDto> handle(Exception ex) {
    	log.error(" occurred: {}", ex.getMessage());
    	ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorType.ILLEGAL_ARGUMENT_EXCEPTION, ex.getMessage());
        return ResponseDto.fail(HttpStatus.BAD_REQUEST, errorResponseDto);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    	log.error(" occurred: {}", ex.getMessage());
    	ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorType.VALIDATION_EXCEPTION, ex.getAllErrors().get(0).getDefaultMessage());
        return ResponseDto.fail(HttpStatus.BAD_REQUEST, errorResponseDto);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDto<ErrorResponseDto> handleBadCredentialsException(BadCredentialsException ex) {
    	log.error(" occurred: {}", ex.getMessage());
    	ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorType.BAD_CREDENTIALS_EXCEPTION, ex.getMessage());
        return ResponseDto.fail(HttpStatus.UNAUTHORIZED, errorResponseDto);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException ex) {
    	log.error(" occurred: {}", ex.getMessage());
    	ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorType.ENTITY_NOT_FOUND_EXCEPTION, ex.getMessage());
        return ResponseDto.fail(HttpStatus.BAD_REQUEST, errorResponseDto);
    }
    
    @ExceptionHandler(DuplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<ErrorResponseDto> handleDuplicationException(DuplicationException ex) {
    	log.error(" occurred: {}", ex.getMessage());
    	ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorType.DUPLICATION_EXCEPTION, ex.getMessage());
        return ResponseDto.fail(HttpStatus.BAD_REQUEST, errorResponseDto);
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<ErrorResponseDto> handleUnauthorizedException(UnauthorizedException ex) {
    	log.error(" occurred: {}", ex.getMessage());
    	ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorType.UNATHORIZED_EXCEPTION, ex.getMessage());
        return ResponseDto.fail(HttpStatus.UNAUTHORIZED, errorResponseDto);
    }
}
