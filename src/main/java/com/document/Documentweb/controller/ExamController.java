package com.document.Documentweb.controller;

import com.document.Documentweb.dto.ResponseDTO;
import com.document.Documentweb.dto.exam.ExamReqDTO;
import com.document.Documentweb.dto.exam.ExamResDTO;
import com.document.Documentweb.dto.exam.ExamUpdateDTO;
import com.document.Documentweb.service.exam.IExamService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {
    IExamService service;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ExamResDTO>>> getAll(@RequestParam(required = false) String advanceSearch) {
        return ResponseEntity.ok(ResponseDTO.success(service.findAll(advanceSearch)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ExamResDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ExamResDTO>> create(@RequestBody @Valid ExamReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<ExamResDTO>> update(@RequestParam Long id, @RequestBody @Valid ExamUpdateDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.update(id, dto)));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Void>> deleteAllById(@RequestParam List<Long> ids) {
        service.deleteAllById(ids);
        return ResponseEntity.ok(ResponseDTO.success());
    }

}
