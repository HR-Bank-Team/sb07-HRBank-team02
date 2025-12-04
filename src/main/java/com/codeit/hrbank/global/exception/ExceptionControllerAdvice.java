package com.codeit.hrbank.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse illegalArgumentException(IllegalArgumentException e){
        return new ErrorResponse(LocalDateTime.now(),400,"잘못된 요청입니다.",e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponse illegalStateException(IllegalStateException e){
        return new ErrorResponse(LocalDateTime.now(),400,"잘못된 요청입니다.",e.getMessage());
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse noSuchElementException(NoSuchElementException e){
        return new ErrorResponse(LocalDateTime.now(),400,"잘못된 요청입니다.",e.getMessage());
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ErrorResponse IOException(IOException e){
        return new ErrorResponse(LocalDateTime.now(),500,"파일 저장 실패",null);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse bindException(BindException e){
        StringBuilder sb = new StringBuilder();
        e.getFieldErrors().forEach(error -> {
            sb.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        });
        return new ErrorResponse(LocalDateTime.now(),400,"잘못된 요청입니다.",sb.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse exception(Exception e){
        return new ErrorResponse(LocalDateTime.now(),400,"잘못된 요청입니다.",e.getMessage());
    }

}


