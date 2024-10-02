package ru.telros.uapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.telros.uapi.util.DateMapper;

import java.time.LocalDateTime;

@RestControllerAdvice("ru.telros.uapi")
@Slf4j
public class ErrorHandler {
    /*------Обработчики для статуса 401 (Unauthorized)------*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleConstraintAuthenticationException(final AuthenticationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.toString())
                .reason(e.getMessage())
                .message(e.getResponseMessage())
                .timestamp(DateMapper.localDateTimeToString(LocalDateTime.now()))
                .build();
        log.debug("{}: {}", e.getClass().getSimpleName(), e.getResponseMessage());

        return errorResponse;
    }

    /*------Обработчики для статуса 404 (Not found)------*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .reason(e.getMessage())
                .message(e.getResponseMessage())
                .timestamp(DateMapper.localDateTimeToString(LocalDateTime.now()))
                .build();
        log.debug("{}: {}", e.getClass().getSimpleName(), e.getMessage());

        return errorResponse;
    }

    /*------Обработчики для статуса 409 (Conflict)------*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistException(final AlreadyExistException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.toString())
                .reason(e.getMessage())
                .message(e.getResponseMessage())
                .timestamp(DateMapper.localDateTimeToString(LocalDateTime.now()))
                .build();
        log.debug("{}: {}", e.getClass().getSimpleName(), e.getResponseMessage());

        return errorResponse;
    }

    /*------Обработчики для статуса 500 (Internal server error)------*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .reason("Произошла непредвиденная ошибка")
                .message("Пожалуйста, обратитесь в службу технической поддержки")
                .timestamp(DateMapper.localDateTimeToString(LocalDateTime.now()))
                .build();
        log.debug("500 {}: {}", e.getClass().getSimpleName(), e.getMessage(), e);

        return errorResponse;
    }
}