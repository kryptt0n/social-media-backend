package com.example.mssscredentials.repositories;

import com.example.mssscredentials.entity.TemporaryIdentityCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemporaryIdentityCredentialsRepository extends JpaRepository<TemporaryIdentityCredentials, Long> {
    Optional<TemporaryIdentityCredentials> findByCode(String code);
}
