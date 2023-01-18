package com.hipravin.devcompanion.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, ConstraintViolationException.class})
    protected ResponseEntity<Object> handleBadArguments(RuntimeException ex, WebRequest request) {

        return handleExceptionInternal(ex, null,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}