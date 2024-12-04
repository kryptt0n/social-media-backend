package com.j2ee.socialmedia.services;

import com.j2ee.socialmedia.dto.UserDTO;
import com.j2ee.socialmedia.entities.User;
import com.j2ee.socialmedia.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DtoMapperService dtoMapperService;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, DtoMapperService dtoMapperService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.dtoMapperService = dtoMapperService;
    }

    public void registerUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles("USER");
        }
        userRepository.save(user);
    }

    public Optional<UserDTO> getUserByUsername(String usernameToFind, String currentUsername) {
        Optional<User> user = userRepository.findByUsername(usernameToFind);

        if (user.isPresent()) {
            return Optional.of(dtoMapperService.userToUserDTO(currentUsername).apply(user.get()));
        }
        return Optional.empty();
    }
}
