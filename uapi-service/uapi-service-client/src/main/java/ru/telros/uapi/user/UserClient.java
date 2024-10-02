package ru.telros.uapi.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.telros.uapi.exception.AlreadyExistException;
import ru.telros.uapi.exception.AuthenticationException;
import ru.telros.uapi.exception.NotFoundException;

import java.util.List;

import static ru.telros.uapi.exception.AlreadyExistException.ALREADY_EXIST_EXCEPTION_REASON;
import static ru.telros.uapi.exception.AlreadyExistException.DUPLICATE_USER_EMAIL_MESSAGE;
import static ru.telros.uapi.exception.AuthenticationException.AUTHENTICATION_EXCEPTION_REASON;
import static ru.telros.uapi.exception.AuthenticationException.USER_EMAIL_NOT_FOUND_MESSAGE;
import static ru.telros.uapi.exception.NotFoundException.NOT_FOUND_EXCEPTION_REASON;
import static ru.telros.uapi.exception.NotFoundException.USER_NOT_FOUND_MESSAGE;

@Service
@Slf4j
public class UserClient {
    private final RestTemplate restTemplate;
    private final RestClient restClient;

    @Autowired
    public UserClient(@Value("${uapi-service.url}") String serverUrl, RestTemplateBuilder builder) {
        this.restTemplate = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .build();
        this.restClient = RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory())
                .baseUrl(serverUrl)
                .build();
    }

    /*--------------------Основные методы--------------------*/
    public GenerateUserTokenDto getUserByEmail(String email) {
        log.debug("{} = {}", "Отправляем запрос на получение данных о пользователе с email", email);
        final String path = "/users/token?email={email}";
        ResponseEntity<GenerateUserTokenDto> response =
                new ResponseEntity<>(HttpStatusCode.valueOf(200));
        try {
            response = restTemplate.exchange(path,
                    HttpMethod.GET,
                    null,
                    GenerateUserTokenDto.class,
                    email);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                log.debug("{}: {}{}.", AuthenticationException.class.getSimpleName(),
                        USER_EMAIL_NOT_FOUND_MESSAGE, email);
                throw new AuthenticationException(AUTHENTICATION_EXCEPTION_REASON,
                        USER_EMAIL_NOT_FOUND_MESSAGE + email);
            }
        }

        return response.getBody();
    }

    public NewUserResponse createUser(NewUserRequest request) {
        log.debug("{} = {}", "Отправляем запрос на создание пользователя с email", request.getEmail());
        final String path = "/users";
        ResponseEntity<NewUserResponse> response =
                new ResponseEntity<>(HttpStatusCode.valueOf(200));
        try {
            response = restTemplate.postForEntity(path, request, NewUserResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 409) {
                log.debug("{}: {}{}.", AlreadyExistException.class.getSimpleName(),
                        DUPLICATE_USER_EMAIL_MESSAGE, request.getEmail());
                throw new AlreadyExistException(ALREADY_EXIST_EXCEPTION_REASON,
                        DUPLICATE_USER_EMAIL_MESSAGE + request.getEmail());
            }
        }

        return response.getBody();
    }

    public List<FullUserDto> getAllUsers(String firstName,
                                         String lastName,
                                         String patronymic,
                                         Integer yearOfBirth,
                                         Integer from,
                                         Integer size) {
        log.debug("Отправляем запрос на получение пользователей с фильтрами");
        final String path = "/users?firstName={firstName}&lastName={lastName}" +
                "&patronymic={patronymic}&yearOfBirth={yearOfBirth}&from={from}&size={size}";

        return restTemplate.exchange(
                path,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FullUserDto>>() {
                },
                firstName,
                lastName,
                patronymic,
                yearOfBirth,
                from,
                size).getBody();
    }

    public FullUserDto getUserById(Long userId) {
        log.debug("{} = {}", "Отправляем запрос на получение данных о пользователе c id", userId);
        final String path = "/users/profile/" + userId;
        ResponseEntity<FullUserDto> response =
                new ResponseEntity<>(HttpStatusCode.valueOf(200));
        try {
            response = restTemplate.getForEntity(path, FullUserDto.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(),
                        USER_NOT_FOUND_MESSAGE, userId);
                throw new NotFoundException(NOT_FOUND_EXCEPTION_REASON,
                        USER_NOT_FOUND_MESSAGE + userId);
            }
        }

        return response.getBody();
    }

    public FullUserDto updateUser(UpdateUserRequest request, Long userId) {
        log.debug("{} = {}", "Отправляем запрос на изменение данных пользователя c id", userId);
        final String path = "/users/profile/update/" + userId;
        try {
            return restClient.patch()
                    .uri(path)
                    .body(request)
                    .retrieve()
                    .body(FullUserDto.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(),
                        USER_NOT_FOUND_MESSAGE, userId);
                throw new NotFoundException(NOT_FOUND_EXCEPTION_REASON,
                        USER_NOT_FOUND_MESSAGE + userId);
            }
        }

        return null;
    }

    public void deleteUser(Long userId) {
        log.debug("{} = {}", "Отправляем запрос на удаление пользователя c id", userId);
        final String path = "/users/" + userId;
        try {
            restTemplate.delete(path);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(),
                        USER_NOT_FOUND_MESSAGE, userId);
                throw new NotFoundException(NOT_FOUND_EXCEPTION_REASON,
                        USER_NOT_FOUND_MESSAGE + userId);
            }
        }
    }
}