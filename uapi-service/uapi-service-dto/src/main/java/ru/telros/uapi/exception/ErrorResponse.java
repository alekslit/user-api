package ru.telros.uapi.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@Schema(description = "Описание ошибки")
public final class ErrorResponse {
    @Schema(description = "Код ошибки и статус", example = "404 NOT_FOUND")
    private final String status;

    @Schema(description = "Причина ошибки", example = "Объект не найден")
    private final String reason;

    @Schema(description = "Подробное описание ошибки",
            example = "Пользователя с таким id не существует. userId = 99")
    private final String message;

    // формат (yyyy-MM-dd HH:mm:ss):
    @Schema(description = "Дата и время, когда произошла ошибка",
            example = "2024-07-27 06:27:23")
    private final String timestamp;
}