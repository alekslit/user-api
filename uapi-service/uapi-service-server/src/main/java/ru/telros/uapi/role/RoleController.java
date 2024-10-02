package ru.telros.uapi.role;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "roles", produces = APPLICATION_JSON_VALUE)
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public List<RoleDto> getAllRoles() {
        return RoleMapper.roleToRoleDto(roleService.getAllRoles());
    }

    @DeleteMapping("/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Integer roleId) {
        roleService.deleteRole(roleId);
    }
}