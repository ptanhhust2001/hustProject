package com.document.Documentweb.repository;

import com.document.Documentweb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}