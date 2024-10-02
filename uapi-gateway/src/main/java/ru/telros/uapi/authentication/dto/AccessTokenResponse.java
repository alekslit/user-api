package ru.telros.uapi.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "Новый Access токен")
public final class AccessTokenResponse {
    @Schema(description = "Тип токена", example = "Bearer")
    private final String type = "Bearer";

    @Schema(description = "Access токен", example = "(многочисленный набор символов)")
    private final String accessToken;
}