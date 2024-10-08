package com.document.Documentweb.repository;

import com.document.Documentweb.entity.InValidateToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InValidateTokenRepository extends JpaRepository<InValidateToken, String> {
}