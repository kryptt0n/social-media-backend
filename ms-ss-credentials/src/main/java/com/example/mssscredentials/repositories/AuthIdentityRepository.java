package com.example.mssscredentials.repositories;

import com.example.mssscredentials.dto.AuthMethodType;
import com.example.mssscredentials.entity.AuthIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthIdentityRepository extends JpaRepository<AuthIdentity, Long> {
    List<AuthIdentity> findAllByTypeAndIdentifier(AuthMethodType type, String value);
    List<AuthIdentity> findAllByUserId(Integer userId);
}
