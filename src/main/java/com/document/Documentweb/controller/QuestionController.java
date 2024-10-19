package com.document.Documentweb.controller;

import com.document.Documentweb.dto.ResponseDTO;
import com.document.Documentweb.dto.question.QuestionReqDTO;
import com.document.Documentweb.dto.question.QuestionResDTO;
import com.document.Documentweb.dto.question.QuestionUpdateDTO;
import com.document.Documentweb.service.question.IQuestionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    IQuestionService service;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<QuestionResDTO>>> getAll(@RequestParam(required = false) String advanceSearch) {
        return ResponseEntity.ok(ResponseDTO.success(service.findAll(advanceSearch)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<QuestionResDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<QuestionResDTO>> create(@RequestBody @Valid QuestionReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<QuestionResDTO>> update(@RequestParam Long id, @RequestBody @Valid QuestionUpdateDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.update(id, dto)));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Void>> deleteAllById(@RequestParam List<Long> ids) {
        service.deleteAllById(ids);
        return ResponseEntity.ok(ResponseDTO.success());
    }
}
