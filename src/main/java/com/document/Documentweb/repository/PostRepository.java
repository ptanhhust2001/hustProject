package com.document.Documentweb.repository;

import com.document.Documentweb.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAll(Specification<Object> spec);
    Page<Post> findAll(Specification<Object> spec, Pageable pageable);
}