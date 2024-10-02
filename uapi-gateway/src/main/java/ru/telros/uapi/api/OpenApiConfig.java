package ru.telros.uapi.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info(
                title = "User API",
                description = "API спецификация веб-приложения проекта User API",
                version = "1.0.0"
        ),
        tags = {
                @Tag(name = "PUBLIC: Пользователи",
                        description = "Публичный API для работы с пользователями"),
                @Tag(name = "USER: Пользователи",
                        description = "Приватный API для аутентифицированных пользователей c уровнем доступа USER"),
                @Tag(name = "ADMIN: Пользователи",
                        description = "API c уровнем доступа ADMIN для работы с пользователями"),
                @Tag(name = "PUBLIC: Аутентификация",
                        description = "API для аутентификации зарегистрированных пользователей"),
                @Tag(name = "ADMIN: Роли",
                        description = "API c уровнем доступа ADMIN для работы с ролями")
        },
        servers = {
                @Server(url = "http://localhost:8080", description = "Локальный сервер")
        })
public class OpenApiConfig {
}