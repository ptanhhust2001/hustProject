package com.document.Documentweb.controller;

import com.document.Documentweb.dto.ResponseDTO;
import com.document.Documentweb.dto.authentication.role.RoleReqDTO;
import com.document.Documentweb.dto.authentication.role.RoleResDTO;
import com.document.Documentweb.service.RoleServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/roles")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class RoleController {
    RoleServiceImpl service;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<RoleResDTO>>> getAllRoles() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<RoleResDTO>> createRole(@RequestBody @Valid RoleReqDTO role) {
        return ResponseEntity.ok(service.create(role));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Void>> deleteRoles(@RequestParam String name) {
        service.delete(name);
        return ResponseEntity.ok(ResponseDTO.success());
    }
}
