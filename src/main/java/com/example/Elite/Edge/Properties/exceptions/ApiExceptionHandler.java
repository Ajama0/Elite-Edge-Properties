package com.example.Elite.Edge.Properties.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<?> HandleRequest(RuntimeException runtimeException) {
        ExceptionPayload exceptionPayload = new ExceptionPayload(
                runtimeException.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()

        );

        return new ResponseEntity<>(exceptionPayload, HttpStatus.BAD_REQUEST);

    }



}

