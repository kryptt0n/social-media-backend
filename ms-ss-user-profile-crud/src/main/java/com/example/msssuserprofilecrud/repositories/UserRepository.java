package com.example.msssuserprofilecrud.repositories;

import com.example.msssuserprofilecrud.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isAccountNonLocked = false WHERE u.id = :id")
    void deactivateUser(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isAccountNonLocked = true WHERE u.id = :id")
    void recoverUser(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isPublic = true WHERE u.id = :id")
    void setPublic(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isPublic = false WHERE u.id = :id")
    void setPrivate(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id = :id")
    void deleteByUsername(@Param("id") Integer id);

    @Query("SELECT u.id FROM User u WHERE u.id = :id")
    Optional<Integer> getIdByUsername(@Param("id") Integer id);

    long countByIsPublicTrue();
    long countByIsPublicFalse();

}
