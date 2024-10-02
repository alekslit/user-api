package ru.telros.uapi.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "Роль пользователя / уровень доступа")
public final class RoleDto {
    // идентификатор:
    @Schema(description = "id роли", example = "1")
    private final Integer id;

    // название роли:
    @Schema(description = "Название роли", example = "USER")
    private final String name;
}