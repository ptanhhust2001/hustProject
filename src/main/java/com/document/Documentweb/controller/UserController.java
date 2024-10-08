package com.document.Documentweb.controller;

import com.document.Documentweb.dto.ResponseDTO;
import com.document.Documentweb.dto.User.UserReqDTO;
import com.document.Documentweb.dto.User.UserResDTO;
import com.document.Documentweb.dto.User.UserUpdateDTO;
import com.document.Documentweb.service.user.UserServicesImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/users")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class UserController {
    UserServicesImpl services;

    @GetMapping
    public ResponseDTO<List<UserResDTO>> getAllUsers() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName() + " " + authentication.getAuthorities().stream().toString());
        return ResponseDTO.success(services.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseDTO<UserResDTO> getUserById(@PathVariable Long id) {
        return ResponseDTO.success(services.getUserById(id));
    }

    @GetMapping("/current")
    public ResponseDTO<UserResDTO> getCurrentUser() {
        return ResponseDTO.success(services.getCurrentUser());
    }

    @PostMapping
    public ResponseDTO<UserResDTO> createUser(@RequestBody @Valid UserReqDTO user) {
        return ResponseDTO.success(services.createUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseDTO<Void> deleteUser(@PathVariable Long id) {
        services.deleteUser(id);
        return ResponseDTO.success();
    }

    @PutMapping("/{id}")
    public ResponseDTO<UserResDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO dto) {
        return ResponseDTO.success(services.updateUser(id, dto));
    }
}
