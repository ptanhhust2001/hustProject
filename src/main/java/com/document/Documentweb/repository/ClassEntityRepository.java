package com.document.Documentweb.repository;

import com.document.Documentweb.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassEntityRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findByName(String name);
}