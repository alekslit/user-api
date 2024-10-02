package ru.telros.uapi.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@Schema(description = "Вывод всей информации о пользователе")
public final class FullUserDto {
    // идентификатор пользователя:
    @Schema(description = "id пользователя", example = "7")
    private final Long id;

    // имя пользователя:
    @Schema(description = "Имя пользователя", example = "Иван")
    private final String firstName;

    // фамилия пользователя:
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private final String lastName;

    // отчество пользователя:
    @Schema(description = "Отчество пользователя", example = "Иванович")
    private final String patronymic;

    // дата рождения пользователя (в формате: yyyy-MM-dd):
    @Schema(description = "Дата рождения пользователя", example = "1995.03.27")
    private final String birthday;

    // электронная почта:
    @Schema(description = "Электронная почта", example = "ivan@mail.ru")
    private final String email;

    // номер телефона пользователя (формат: 8 800 555 35 35):
    @Schema(description = "Номер телефона пользователя", example = "8 800 555 55 35")
    private final String phone;
}