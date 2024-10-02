package ru.telros.uapi.user;

import java.util.List;

public interface UserService {
    User saveUser(NewUserRequest userRequest);

    User findUserByEmail(String email);

    User getUserById(Long userId);

    User updateUser(UpdateUserRequest request, Long userId);

    List<User> getAllUsers(String firstName,
                           String lastName,
                           String patronymic,
                           Integer yearOfBirth,
                           Integer from,
                           Integer size);

    void deleteUser(Long userId);
}