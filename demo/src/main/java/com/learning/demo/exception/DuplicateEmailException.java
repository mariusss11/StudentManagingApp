package com.learning.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class DuplicateEmailException extends RuntimeException{

    public DuplicateEmailException(String msg) {
        super(msg);
    }

    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}

