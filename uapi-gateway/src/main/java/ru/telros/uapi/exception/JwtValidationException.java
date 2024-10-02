package ru.telros.uapi.exception;

import lombok.Getter;

@Getter
public class JwtValidationException extends RuntimeException {
    private final String responseMessage;

    public static final String JWT_VALIDATION_EXCEPTION_REASON = "Ошибка валидации токена";
    public static final String EXPIRED_JWT_MESSAGE = "Срок действия токена истёк";
    public static final String MALFORMED_JWT_MESSAGE = "Неверный формат токена";
    public static final String SIGNATURE_EXCEPTION_MESSAGE = "Подпись токена недействительна";

    public JwtValidationException(String reason, String responseMessage) {
        super(reason);
        this.responseMessage = responseMessage;
    }
}