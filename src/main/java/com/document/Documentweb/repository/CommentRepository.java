package com.document.Documentweb.repository;

import com.document.Documentweb.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAll(Specification<Object> spec);
    Page<Comment> findAll(Specification<Object> spec, Pageable pageable);
}