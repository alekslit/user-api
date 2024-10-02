package ru.telros.uapi.role;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoleMapper {
    public static RoleName roleToRoleName(Role role) {
        return role.getName();
    }

    public static Set<RoleName> roleToRoleName(Set<Role> roles) {
        return roles.stream()
                .map(RoleMapper::roleToRoleName)
                .collect(Collectors.toSet());
    }

    public static RoleDto roleToRoleDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName().toString())
                .build();
    }

    public static List<RoleDto> roleToRoleDto(List<Role> roles) {
        return roles.stream()
                .map(RoleMapper::roleToRoleDto)
                .collect(Collectors.toList());
    }
}