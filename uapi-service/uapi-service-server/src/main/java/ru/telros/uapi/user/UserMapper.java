package ru.telros.uapi.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.telros.uapi.role.Role;
import ru.telros.uapi.role.RoleMapper;
import ru.telros.uapi.role.RoleName;
import ru.telros.uapi.util.DateMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    /*--------------------Основные методы--------------------*/
    public static User newUserRequestToUser(NewUserRequest userRequest) {
        return User.builder()
                .firstName(userRequest.getFirstName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .registrationDate(LocalDateTime.now())
                .roles(Collections.singleton(Role.builder()
                        .id(1)
                        .name(RoleName.USER)
                        .build()))
                .build();
    }

    public static NewUserResponse userToNewUserResponse(User user) {
        return NewUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .registrationDate(DateMapper.localDateTimeToString(user.getRegistrationDate()))
                .build();
    }

    public static FullUserDto userToFullUserDto(User user) {
        return FullUserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .patronymic(user.getPatronymic())
                .birthday(DateMapper.localDateToString(user.getBirthday()))
                .email(user.getEmail())
                .phone(getUserPhone(user.getPhone()))
                .build();
    }

    public static List<FullUserDto> userToFullUserDto(List<User> users) {
        return users.stream()
                .map(UserMapper::userToFullUserDto)
                .collect(Collectors.toList());
    }

    public static GenerateUserTokenDto userToGenerateUserDto(User user) {
        return GenerateUserTokenDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(RoleMapper.roleToRoleName(user.getRoles()))
                .build();
    }

    /*--------------------Вспомогательные методы--------------------*/
    // возвращает телефон в формате: 8 800 555 35 35:
    private static String getUserPhone(String phone) {
        if (phone == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(phone);
        builder.insert(phone.length() - 10, " ");
        builder.insert(phone.length() - 6, " ");
        builder.insert(phone.length() - 2, " ");
        builder.insert(phone.length() + 1, " ");

        return builder.toString();
    }
}