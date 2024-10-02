package ru.telros.uapi.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    private final String responseMessage;

    public static final String AUTHENTICATION_EXCEPTION_REASON = "Ошибка аутентификации";
    public static final String USER_EMAIL_NOT_FOUND_MESSAGE = "Пользователя с таким email не существует. email = ";
    public static final String USER_PASSWORD_INCORRECT_MESSAGE = "Неверный пароль";

    public AuthenticationException(String reason, String responseMessage) {
        super(reason);
        this.responseMessage = responseMessage;
    }
}