package com.example.itmo.extended.controllers;

import com.example.itmo.extended.model.dto.request.UserInfoRequest;
import com.example.itmo.extended.model.dto.response.UserInfoResponse;
import com.example.itmo.extended.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", name = "Authorization")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id")
    public UserInfoResponse getUser(@RequestHeader("api-key") String apiKey, @PathVariable Long id) {

        return userService.getUser(apiKey, id);
    }

    @PostMapping
    @Operation(summary = "Создать пользователя", security = @SecurityRequirement(name = AUTHORIZATION))
    public UserInfoResponse addUser(@RequestBody @Valid UserInfoRequest request) {
        return userService.addUser(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные пользователя по id", security = @SecurityRequirement(name = AUTHORIZATION))
    public UserInfoResponse updateUser(@PathVariable Long id, @RequestBody UserInfoRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя по id", security = @SecurityRequirement(name = AUTHORIZATION))
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список пользователей")
    public Page<UserInfoResponse> getAllUsers(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer perPage,
                                              @RequestParam(defaultValue = "lastName") String sort,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                              @RequestParam(required = false) String filter) {
        return userService.getAllUsers(page, perPage, sort, order, filter);
    }

    @GetMapping("/ya/{id}")
    @Operation(summary = "Получить пользователя по id")
    public UserInfoResponse getYaUser(@PathVariable Long id, @RequestHeader("api-key") String apiKey) {
        return userService.getYaUser(id, apiKey);
    }
}
