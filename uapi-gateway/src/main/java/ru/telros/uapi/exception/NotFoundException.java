package ru.telros.uapi.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String responseMessage;

    public static final String NOT_FOUND_EXCEPTION_REASON = "Объект не найден";
    public static final String USER_NOT_FOUND_MESSAGE = "Пользователя с таким id не существует. userId = ";
    public static final String DIRECTION_NAME_NOT_FOUND_MESSAGE = "Направления с таким именем не существует. " +
            "name = ";
    public static final String ROLE_NOT_FOUND_MESSAGE = "Роль с таким id не существует. roleId = ";
    public static final String DIRECTION_ID_NOT_FOUND_MESSAGE = "Направления с таким id не существует. dirId = ";

    public NotFoundException(String reason, String responseMessage) {
        super(reason);
        this.responseMessage = responseMessage;
    }
}