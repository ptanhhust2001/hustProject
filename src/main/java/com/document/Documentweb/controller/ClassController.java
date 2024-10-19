package com.document.Documentweb.controller;

import com.document.Documentweb.dto.ResponseDTO;
import com.document.Documentweb.dto.classenity.ClassReqDTO;
import com.document.Documentweb.dto.classenity.ClassResDTO;
import com.document.Documentweb.service.classentity.IClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/class")
@AllArgsConstructor
public class ClassController {
    IClassService service;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ClassResDTO>>> getAll() {
        return ResponseEntity.ok(ResponseDTO.success(service.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ClassResDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ClassResDTO>> create(@RequestBody ClassReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<ClassResDTO>> update(@PathVariable Long id,@RequestBody ClassReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.update(id ,dto)));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllById(@RequestParam List<Long> ids) {
        service.deleteAllByIds(ids);
        return ResponseEntity.ok().build();
    }
}