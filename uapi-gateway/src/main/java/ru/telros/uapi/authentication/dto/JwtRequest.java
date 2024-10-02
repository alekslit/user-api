package ru.telros.uapi.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Schema(description = "Запрос на вход от пользователя")
public final class JwtRequest {
    // электронная почта:
    @NotBlank(message = "Адрес электронной почты не может быть пустым")
    @Size(min = 6, max = 254, message = "Длинна email должна быть от {min} до {max} символов.")
    @Email(regexp = "([A-Za-z0-9]{1,}[\\\\-]{0,1}[A-Za-z0-9]{1,}[\\\\.]{0,1}[A-Za-z0-9]{1,})+@"
            + "([A-Za-z0-9]{1,}[\\\\-]{0,1}[A-Za-z0-9]{1,}[\\\\.]{0,1}[A-Za-z0-9]{1,})+[\\\\.]{1}[a-z]{2,10}",
            message = "Некорректный адрес электронной почты: ${validatedValue}")
    @Schema(description = "Электронная почта", example = "ivan@mail.ru")
    private final String email;

    // пароль от аккаунта пользователя:
    @NotBlank(message = "Пароль пользователя не может быть пустым")
    @Size(min = 4, max = 50, message = "Пароль должен содержать от {min} до {max} символов.")
    @Schema(description = "Пароль от аккаунта пользователя", example = "g32jhg4jh23")
    private final String password;
}