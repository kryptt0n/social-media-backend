package com.example.mainservice.repositories;

import com.example.mainservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
  
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isAccountNonLocked = false WHERE u.username = :username")
    void deactivateUser(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isAccountNonLocked = true WHERE u.username = :username")
    void recoverUser(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isPublic = true WHERE u.username = :username")
    void setPublic(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isPublic = false WHERE u.username = :username")
    void setPrivate(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.username = :username")
    void deleteByUsername(@Param("username") String username);

    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    Optional<Integer> getIdByUsername(@Param("username") String username);

    long countByIsPublicTrue();
    long countByIsPublicFalse();

}
