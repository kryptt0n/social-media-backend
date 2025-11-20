package com.example.msosidentity.repositories;

import com.example.msosidentity.entities.CacheOauth;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheOauthRepository extends CrudRepository<CacheOauth, String> {
}
