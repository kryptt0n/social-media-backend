package com.example.mainservice.services;

import com.example.mainservice.dto.UpdateUserDTO;
import com.example.mainservice.dto.UserDTO;
import com.example.mainservice.entities.User;
import com.example.mainservice.repositories.UserRepository;
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

    public Optional<User> authenticateUser(User user) {
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser.isPresent() && passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
            return foundUser;
        }
        return Optional.empty();
    }

    public void deactivateUser(String username) {
        userRepository.deactivateUser(username);
    }

    public void recoverUser(String username) {
        userRepository.recoverUser(username);
    }

    public void setPublic(String username) {
        userRepository.setPublic(username);
    }

    public void setPrivate(String username) {
        userRepository.setPrivate(username);
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public Optional<User> updateUser(UpdateUserDTO updateUserDTO, Integer id) {
        return userRepository.findById(id).map(user -> {
            if (updateUserDTO.getImageUrl() != null) {
                user.setImageUrl(updateUserDTO.getImageUrl());
            }
            if (updateUserDTO.getBio() != null) {
                user.setBio(updateUserDTO.getBio());
            }
            return userRepository.save(user);
        });
    }

    public Optional<Integer> getIdByUsername(String username) {
        return userRepository.getIdByUsername(username);
    }
}
