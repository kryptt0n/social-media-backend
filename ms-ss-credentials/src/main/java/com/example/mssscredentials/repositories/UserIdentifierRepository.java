package com.example.mssscredentials.repositories;

import com.example.mssscredentials.entity.IdentifierType;
import com.example.mssscredentials.entity.UserIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserIdentifierRepository extends JpaRepository<UserIdentifier, Long> {
    Optional<UserIdentifier> findByIdentifier(String value);
    boolean existsByIdentifierAndType(String value, IdentifierType type);
    Optional<UserIdentifier> findByUserIdAndType(Integer userId, IdentifierType type);
    List<UserIdentifier> findAllByUserId(Integer userId);
}
