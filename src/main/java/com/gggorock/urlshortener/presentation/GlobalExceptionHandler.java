package com.gggorock.urlshortener.presentation;

import com.gggorock.urlshortener.domain.NotExistingUrlException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotExistingUrlException.class)
    public ResponseEntity<String> notExistingUrlException() {
        return new ResponseEntity<>("존재하지 않는 URL입니다.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> bindException() {
        return new ResponseEntity<>("올바르지 않은 형식입니다.", HttpStatus.BAD_REQUEST);
    }
}
