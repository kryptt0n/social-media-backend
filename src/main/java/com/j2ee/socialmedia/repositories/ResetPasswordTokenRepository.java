package com.j2ee.socialmedia.repositories;

import com.j2ee.socialmedia.entities.ResetPasswordToken;
import com.j2ee.socialmedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer> {
    Optional<ResetPasswordToken> findByUser(User user);
    Optional<ResetPasswordToken> findByResetPasswordToken(String resetPasswordToken);
}
