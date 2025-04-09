package com.example.msssmediaexchange.repository;

import com.example.msssmediaexchange.dto.Provider;
import com.example.msssmediaexchange.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {
    Optional<Media> findBySourceIdAndProvider(String sourceId, Provider provider);
}
