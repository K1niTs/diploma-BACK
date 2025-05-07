package com.example.rental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailBusyException extends RuntimeException {
    public EmailBusyException() { super("Email busy"); }
}
