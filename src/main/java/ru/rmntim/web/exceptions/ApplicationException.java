package ru.rmntim.web.exceptions;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}

