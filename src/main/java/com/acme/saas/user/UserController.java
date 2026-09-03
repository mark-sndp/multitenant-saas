package com.acme.saas.user;

import com.acme.saas.security.Roles;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('" + Roles.TENANT_ADMIN + "', '" + Roles.PLATFORM_ADMIN + "')")
    public List<UserResponse> listUsers() {
        return userService.listUsersForCurrentTenant().stream().map(UserResponse::from).toList();
    }
}
