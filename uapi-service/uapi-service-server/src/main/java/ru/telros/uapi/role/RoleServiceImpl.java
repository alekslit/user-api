package ru.telros.uapi.role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.telros.uapi.exception.NotFoundException;

import java.util.List;

import static ru.telros.uapi.exception.NotFoundException.NOT_FOUND_EXCEPTION_REASON;
import static ru.telros.uapi.exception.NotFoundException.ROLE_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    /*--------------------Основные методы--------------------*/
    @Override
    public List<Role> getAllRoles() {
        log.debug("{}.", "Попытка получить все объекты Role");
        return roleRepository.findAll();
    }

    @Override
    public void deleteRole(Integer roleId) {
        log.debug("{} = {}.", "Попытка удалить объект Role по id", roleId);
        // проверим, существует ли такая роль:
        getRoleById(roleId);
        roleRepository.deleteById(roleId);
    }

    /*--------------------Вспомогательные методы--------------------*/
    @Override
    public void getRoleById(Integer roleId) {
        roleRepository.findById(roleId).orElseThrow(() -> {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(),
                    ROLE_NOT_FOUND_MESSAGE, roleId);
            return new NotFoundException(NOT_FOUND_EXCEPTION_REASON,
                    ROLE_NOT_FOUND_MESSAGE + roleId);
        });
    }
}