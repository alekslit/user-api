package ru.telros.uapi.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "users", produces = APPLICATION_JSON_VALUE)
@Validated
public class UserController {
    private final UserService userService;

    /*--------------------Методы с доступом PUBLIC--------------------*/
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserResponse createUser(@RequestBody NewUserRequest userRequest) {
        return UserMapper.userToNewUserResponse(userService.saveUser(userRequest));
    }

    // поиск пользователей с фильтрами:
    @GetMapping
    public List<FullUserDto> getAllUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String patronymic,
            @RequestParam(required = false) Integer yearOfBirth,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return UserMapper.userToFullUserDto(userService
                .getAllUsers(firstName, lastName, patronymic, yearOfBirth, from, size));
    }

    @GetMapping("/profile/{userId}")
    public FullUserDto getUserById(@PathVariable Long userId) {
        return UserMapper.userToFullUserDto(userService.getUserById(userId));
    }

    /*--------------------Методы с доступом USER--------------------*/
    @PatchMapping("/profile/update/{userId}")
    public FullUserDto updateUser(
            @RequestBody UpdateUserRequest request,
            @PathVariable Long userId
    ) {
        return UserMapper.userToFullUserDto(userService.updateUser(request, userId));
    }

    /*--------------------Методы с доступом ADMIN--------------------*/
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    /*--------------------Вспомогательные методы--------------------*/
    // метод для запроса данных пользователя, которые участвуют в формировании токенов:
    @GetMapping("/token")
    public GenerateUserTokenDto getUserByEmail(@RequestParam String email) {
        return UserMapper.userToGenerateUserDto(userService.findUserByEmail(email));
    }
}