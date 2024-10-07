package com.learning.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public class StudentException {
    private final String message;
    private final Throwable cause;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public StudentException(String message, Throwable cause, HttpStatus httpStatus) {
        this.message = message;
        this.cause = cause;
        this.httpStatus = httpStatus;
        this.timestamp = ZonedDateTime.now(ZoneId.of("Z")); // Use UTC for timestamps
    }

}
