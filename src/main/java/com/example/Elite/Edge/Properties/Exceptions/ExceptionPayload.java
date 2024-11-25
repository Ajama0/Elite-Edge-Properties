package com.example.Elite.Edge.Properties.Exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ExceptionPayload {
    private String message;
    private HttpStatus httpStatus;
    private ZonedDateTime zonedDateTime;

    public ExceptionPayload(String message, HttpStatus httpStatus, ZonedDateTime zonedDateTime) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.zonedDateTime = zonedDateTime;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }
}
