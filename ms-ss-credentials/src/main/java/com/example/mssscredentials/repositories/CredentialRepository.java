package com.example.mssscredentials.repositories;

import com.example.mssscredentials.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Integer> {
    Optional<Credential> findByUsername(String username);
    Optional<Credential> findByUserId(Integer userId);

    void deleteByUsername(String username);
}
