package com.example.msosidentity.services;

import com.example.msosidentity.dto.UserMeDTO;
import com.example.msosidentity.exceptions.InvalidCredentialsException;
import com.example.msosidentity.exceptions.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ControllersUtil {

    public UserMeDTO extractUser(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("Authentication required");
        }

        if (!(authentication.getPrincipal() instanceof UserMeDTO)) {
            throw new InvalidCredentialsException("Invalid authentication");
        }

        return (UserMeDTO) authentication.getPrincipal();
    }
}
