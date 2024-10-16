package com.document.Documentweb.controller;

import com.document.Documentweb.dto.ResponseDTO;
import com.document.Documentweb.dto.ResponsePageDTO;
import com.document.Documentweb.dto.comment.CommentReqDTO;
import com.document.Documentweb.dto.comment.CommentResDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.document.Documentweb.service.comment.ICommentService;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    ICommentService service;

    @GetMapping
    public ResponseEntity<ResponsePageDTO<List<CommentResDTO>>> getAll(@RequestParam(required = false) String advanceSearch,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "" + Integer.MAX_VALUE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.findAll(advanceSearch, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CommentResDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<CommentResDTO>> create(@RequestBody CommentReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<CommentResDTO>> update(@PathVariable Long id,@RequestBody CommentReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.update(id, dto)));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam List<Long> ids) {
        service.deleteAllById(ids);
        return ResponseEntity.ok().build();
    }

}