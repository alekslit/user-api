package ru.telros.uapi.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.telros.uapi.role.RoleName;

import java.util.Set;

@AllArgsConstructor
@Getter
@ToString
@Builder
public final class GenerateUserTokenDto {
    private final Long id;
    private final String email;
    private final String password;
    private final Set<RoleName> roles;
}