package com.learning.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class SameEmailUpdateException extends RuntimeException{
    public SameEmailUpdateException(String msg) {
        super(msg);
    }

    public SameEmailUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}

