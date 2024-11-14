package ru.rmntim.web.exceptions;

public class ServerException extends Exception {
    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

