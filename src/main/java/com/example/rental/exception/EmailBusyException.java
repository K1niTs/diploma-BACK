package com.example.rental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Бросаем, когда e-mail уже зарегистрирован. */
@ResponseStatus(HttpStatus.CONFLICT)          // ← 409 вместо 500
public class EmailBusyException extends RuntimeException {
    public EmailBusyException() { super("Email busy"); }
}
