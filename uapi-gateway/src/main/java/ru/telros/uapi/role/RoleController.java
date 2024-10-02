package ru.telros.uapi.role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.telros.uapi.exception.ErrorResponse;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static ru.telros.uapi.api.ApiExceptionMessageValue.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "roles", produces = APPLICATION_JSON_VALUE)
@Validated
public class RoleController {
    private final RoleClient roleClient;

    // получить список ролей:
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            tags = {"ADMIN: Роли"},
            summary = "Получить список ролей",
            description = "Позволяет получить список ролей"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = NOT_USER_BY_EMAIL_401)})),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = ACCESS_DENIED_403)})),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = API_EXCEPTION_MESSAGE_500)}))
    })
    public List<RoleDto> getAllRoles() {
        return roleClient.getAllRoles();
    }

    // удалить роль:
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            tags = {"ADMIN: Роли"},
            summary = "Удалить роль",
            description = "Позволяет удалить роль"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = NOT_USER_BY_EMAIL_401)})),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = ACCESS_DENIED_403)})),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = ROLE_NOT_FOUND_404)})),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(value = API_EXCEPTION_MESSAGE_500)}))
    })
    public void deleteRole(
            @PathVariable
            @Positive(message = "Параметр запроса roleId, должен быть положительным числом.")
            @Parameter(description = "id роли", required = true) Integer roleId
    ) {
        roleClient.deleteRole(roleId);
    }
}