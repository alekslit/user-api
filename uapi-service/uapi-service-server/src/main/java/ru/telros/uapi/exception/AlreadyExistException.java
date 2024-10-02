package ru.telros.uapi.exception;

import lombok.Getter;

@Getter
public class AlreadyExistException extends RuntimeException {
    private final String responseMessage;

    public static final String ALREADY_EXIST_EXCEPTION_REASON = "Объект уже существует";
    public static final String DUPLICATE_USER_EMAIL_MESSAGE = "Пользователь с таким email уже существует. email = ";

    public AlreadyExistException(String reason, String responseMessage) {
        super(reason);
        this.responseMessage = responseMessage;
    }
}