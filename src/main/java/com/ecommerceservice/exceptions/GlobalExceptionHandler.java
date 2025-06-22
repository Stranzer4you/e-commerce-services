package com.ecommerceservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse handleBadRequestException(Exception ex){
        return new ErrorResponse(400,ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MethodArgumentNotValidExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        MethodArgumentNotValidExceptionResponse dto = new MethodArgumentNotValidExceptionResponse();
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(s-> s.getField() + " : " + s.getDefaultMessage()).toList();
        dto.setStatus(400);
        dto.setMessages(errors);
        return  dto;
    }
}
