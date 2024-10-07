package com.document.Documentweb.controller;

import com.document.Documentweb.dto.ResponseDTO;
import com.document.Documentweb.dto.authentication.permission.PermissionReqDTO;
import com.document.Documentweb.dto.authentication.permission.PermissionResDTO;
import com.document.Documentweb.service.PermissionServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionServiceImpl service;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<PermissionResDTO>>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<PermissionResDTO>> create(@RequestBody @Valid PermissionReqDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Void>> delete(@RequestParam List<String> name) {
        service.delete(name);
        return ResponseEntity.ok(ResponseDTO.success());
    }
}
