package ru.telros.uapi.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@ToString
@Schema(description = "Обновление информации о пользователе")
public final class UpdateUserRequest {
    // имя пользователя:
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 50, message = "Длинна имени должна быть от {min} до {max} символов.")
    @Schema(description = "Имя пользователя", example = "Иван")
    private final String firstName;

    // фамилия пользователя:
    @Size(min = 2, max = 50, message = "Длинна фамилии должна быть от {min} до {max} символов.")
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private final String lastName;

    // отчество пользователя:
    @Size(min = 2, max = 50, message = "Длинна отчества должна быть от {min} до {max} символов.")
    @Schema(description = "Отчество пользователя", example = "Иванович")
    private final String patronymic;

    // дата рождения пользователя (принимаем в формате: yyyy-MM-dd):
    @PastOrPresent(message = "Дата рождения пользователя не может быть позже текущей даты")
    @Schema(description = "Дата рождения пользователя", example = "1995-03-27")
    private final LocalDate birthday;

    // электронная почта:
    @NotBlank(message = "Адрес электронной почты не может быть пустым")
    @Size(min = 6, max = 254, message = "Длинна email должна быть от {min} до {max} символов.")
    @Email(regexp = "([A-Za-z0-9]{1,}[\\\\-]{0,1}[A-Za-z0-9]{1,}[\\\\.]{0,1}[A-Za-z0-9]{1,})+@"
            + "([A-Za-z0-9]{1,}[\\\\-]{0,1}[A-Za-z0-9]{1,}[\\\\.]{0,1}[A-Za-z0-9]{1,})+[\\\\.]{1}[a-z]{2,10}",
            message = "Некорректный адрес электронной почты: ${validatedValue}")
    @Schema(description = "Электронная почта", example = "ivan@mail.ru")
    private final String email;

    // номер телефона пользователя (формат: 88005553535):
    @Size(min = 11, max = 13, message = "Длинна номера телефона должна быть от {min} до {max} символов.")
    @Schema(description = "Номер телефона пользователя", example = "88005555535")
    private final String phone;
}