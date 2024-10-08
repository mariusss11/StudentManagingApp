package com.learning.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String msg) {
        super(msg);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}



