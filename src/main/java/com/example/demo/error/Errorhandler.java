package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class Errorhandler {

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorEntity<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorEntity<>(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<ErrorEntity<String>> handleNoSuchElementException(NoSuchElementException e){
        return new ResponseEntity<>(new ErrorEntity<>(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        ) ,HttpStatus.NOT_FOUND);
    }
}
