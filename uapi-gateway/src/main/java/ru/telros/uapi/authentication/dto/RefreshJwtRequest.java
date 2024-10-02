package ru.telros.uapi.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Schema(description = "Запрос на обновление Refresh и Access токенов")
public final class RefreshJwtRequest {
    @Schema(description = "Refresh токен", example = "(многочисленный набор символов)")
    private String refreshToken;
}