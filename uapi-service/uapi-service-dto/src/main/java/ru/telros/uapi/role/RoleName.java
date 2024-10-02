package ru.telros.uapi.role;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum RoleName implements GrantedAuthority {
    USER("USER"),
    ADMIN("ADMIN"),
    // роль для авто-тестов (проверка удаления):
    DELETE("DELETE");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}