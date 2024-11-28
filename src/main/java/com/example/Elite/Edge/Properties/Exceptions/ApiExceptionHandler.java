package com.example.Elite.Edge.Properties.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(value = {PropertyException.class})
    public ResponseEntity<?>HandleRequest(PropertyException propertyException){
        ExceptionPayload exceptionPayload = new ExceptionPayload(
                propertyException.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()

        );

        return new ResponseEntity<>(exceptionPayload, HttpStatus.BAD_REQUEST);
    }


}
