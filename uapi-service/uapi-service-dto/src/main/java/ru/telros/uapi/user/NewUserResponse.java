package ru.telros.uapi.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@Schema(description = "Ответ на создание нового пользователя")
public final class NewUserResponse {
    // идентификатор пользователя:
    @Schema(description = "id пользователя", example = "7")
    private final Long id;

    // имя пользователя:
    @Schema(description = "Имя пользователя", example = "Иван")
    private final String firstName;

    // электронная почта:
    @Schema(description = "Электронная почта", example = "ivan@mail.ru")
    private final String email;

    // дата и время регистрации пользователя (в формате: yyyy-MM-dd HH:mm:ss):
    @Schema(description = "Дата регистрации пользователя", example = "1995-03-27 22:13:36")
    private final String registrationDate;
}