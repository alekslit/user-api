package ru.telros.uapi.role;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.telros.uapi.exception.NotFoundException;

import java.util.List;

import static ru.telros.uapi.exception.NotFoundException.NOT_FOUND_EXCEPTION_REASON;
import static ru.telros.uapi.exception.NotFoundException.ROLE_NOT_FOUND_MESSAGE;

@Service
@Slf4j
public class RoleClient {
    private final RestTemplate rest;

    @Autowired
    public RoleClient(@Value("${uapi-service.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .build();
    }

    public List<RoleDto> getAllRoles() {
        log.debug("Отправляем запрос на получение списка всех ролей");
        final String path = "/roles";

        return rest.exchange(path,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RoleDto>>() {
                }).getBody();
    }

    public void deleteRole(Integer roleId) {
        log.debug("{} = {}", "Отправляем запрос на удаление роли c id", roleId);
        final String path = "/roles/" + roleId;
        try {
            rest.delete(path);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(),
                        ROLE_NOT_FOUND_MESSAGE, roleId);
                throw new NotFoundException(NOT_FOUND_EXCEPTION_REASON,
                        ROLE_NOT_FOUND_MESSAGE + roleId);
            }
        }
    }
}