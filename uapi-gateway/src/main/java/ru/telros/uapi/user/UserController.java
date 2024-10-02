package ru.telros.uapi.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.telros.uapi.authentication.AuthenticationService;
import ru.telros.uapi.exception.ErrorResponse;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static ru.telros.uapi.api.ApiExceptionMessageValue.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "users", produces = APPLICATION_JSON_VALUE)
@Validated
public class UserController {
    private final UserClient userClient;
    private final AuthenticationService authenticationService;

    /*--------------------Методы с доступом PUBLIC--------------------*/
    // создание пользователя:
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            tags = {"PUBLIC: Пользователи"},
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = NOT_VALID_EMAIL_400)})),
            @ApiResponse(responseCode = "409",
                    description = "Conflict",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = EMAIL_ALREADY_EXIST_409)})),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = API_EXCEPTION_MESSAGE_500)}))
    })
    public NewUserResponse createUser(
            @Valid
            @RequestBody
            @Parameter(required = true) NewUserRequest userRequest
    ) {
        return userClient.createUser(userRequest);
    }

    // получить список пользователей:
    @GetMapping()
    @Operation(
            tags = {"PUBLIC: Пользователи"},
            summary = "Получение списка пользователей",
            description = "Позволяет получить список пользователей. " +
                    "Есть возможность фильтрации по параметрам"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = GET_ALL_USER_400)})),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = API_EXCEPTION_MESSAGE_500)}))
    })
    public List<FullUserDto> getAllUsers(
            @RequestParam(required = false)
            @Parameter(description = "Фильтр по имени") String firstName,
            @RequestParam(required = false)
            @Parameter(description = "Фильтр по фамилии") String lastName,
            @RequestParam(required = false)
            @Parameter(description = "Фильтр по отчеству") String patronymic,
            @RequestParam(required = false)
            @Positive(message = "Параметр запроса year, должен быть " +
                    "положительным числом.")
            @Parameter(description = "Фильтр по году рождения") Integer yearOfBirth,
            @RequestParam(defaultValue = "0")
            @PositiveOrZero(message = "Параметр запроса from, должен быть " +
                    "положительным числом или нулём.")
            @Parameter(description = "Количество элементов, которые нужно " +
                    "пропустить для формирования текущего набора", required = true) Integer from,
            @RequestParam(defaultValue = "10")
            @Positive(message = "Параметр запроса size, должен быть " +
                    "положительным числом.")
            @Parameter(description = "Количество элементов в наборе", required = true) Integer size
    ) {
        return userClient.getAllUsers(firstName, lastName, patronymic, yearOfBirth, from, size);
    }

    // получить пользователя по id:
    @GetMapping("/profile/{userId}")
    @Operation(
            tags = {"PUBLIC: Пользователи"},
            summary = "Получение профиля пользователя",
            description = "Позволяет получить данные одного из пользователей"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = NOT_VALID_USER_ID_400)})),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = USER_NOT_FOUND_404)})),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = API_EXCEPTION_MESSAGE_500)}))
    })
    public FullUserDto getUserById(
            @PathVariable
            @Positive(message = "Параметр запроса userId, должен быть положительным числом.")
            @Parameter(description = "id пользователя", required = true) Long userId
    ) {
        return userClient.getUserById(userId);
    }

    /*--------------------Методы с доступом USER--------------------*/
    // обновить данные пользователя:
    @PatchMapping("/profile/update")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(
            tags = {"USER: Пользователи"},
            summary = "Обновление данных пользователя",
            description = "Позволяет обновить данные пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = NOT_VALID_EMAIL_400)})),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = NOT_VALID_TOKEN_403)})),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = USER_NOT_FOUND_404)})),
            @ApiResponse(responseCode = "409",
                    description = "Conflict",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = EMAIL_ALREADY_EXIST_409)})),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = API_EXCEPTION_MESSAGE_500)}))
    })
    public FullUserDto updateUser(
            @Valid @RequestBody
            @Parameter(required = true) UpdateUserRequest request
    ) {
        return userClient.updateUser(request, authenticationService.getAuthenticationInfo().getId());
    }

    /*--------------------Методы с доступом ADMIN--------------------*/
    // удалить пользователя:
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            tags = {"ADMIN: Пользователи"},
            summary = "Удаление пользователя",
            description = "Позволяет удалить пользователя из БД"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = NOT_VALID_USER_ID_400)})),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = NOT_VALID_TOKEN_403)})),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = USER_NOT_FOUND_404)})),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = API_EXCEPTION_MESSAGE_500)}))
    })
    public void deleteUser(
            @PathVariable
            @Positive(message = "Параметр запроса userId, должен быть положительным числом.")
            @Parameter(description = "id пользователя", required = true) Long userId
    ) {
        userClient.deleteUser(userId);
    }
}