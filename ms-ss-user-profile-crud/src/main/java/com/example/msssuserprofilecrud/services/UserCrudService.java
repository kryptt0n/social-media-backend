package com.example.msssuserprofilecrud.services;


import com.example.msssuserprofilecrud.dto.CredentialsDto;
import com.example.msssuserprofilecrud.dto.UpdateUserDTO;
import com.example.msssuserprofilecrud.dto.UserDTO;
import com.example.msssuserprofilecrud.entities.User;
import com.example.msssuserprofilecrud.feign.IdentityClient;
import com.example.msssuserprofilecrud.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserCrudService {

    private final UserRepository userRepository;
    private final IdentityClient identityClient;


    public UserCrudService(UserRepository userRepository, IdentityClient identityClient) {
        this.userRepository = userRepository;
        this.identityClient = identityClient;


    }

    public Optional<UserDTO> getUserByUsername(String usernameToFind, String currentUsername) {
        return userRepository.findByUsername(usernameToFind).map(user ->
                new UserDTO(
                        user.getUsername(),
                        user.getImageUrl(),
                        user.getBio(),
                        false,
                        0,
                        0,
                        user.isAccountNonLocked(),
                        user.isPublic()
                )
        );
    }

    public void registerUser(User user) {
        // Set created timestamp
        user.setCreatedAt(LocalDateTime.now());
        String rawPassword = user.getPassword();
        // Encode password
        user.setPassword(user.getPassword());

        // Assign default role if not set
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles("USER");
        }

        // Save to DB
        userRepository.save(user);
        CredentialsDto credentialsDto = new CredentialsDto(user.getUsername(), rawPassword);
        identityClient.register(credentialsDto);
    }

    @Transactional
    public void deactivateUser(String username) {
        userRepository.deactivateUser(username);
    }

    @Transactional
    public void recoverUser(String username) {
        userRepository.recoverUser(username);
    }

    @Transactional
    public void setPublic(String username) {
        userRepository.setPublic(username);
    }

    @Transactional
    public void setPrivate(String username) {
        userRepository.setPrivate(username);
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public Optional<User> updateUser(UpdateUserDTO updateUserDTO, Integer id) {
        return userRepository.findById(id).map(user -> {
            if (updateUserDTO.getImageUrl() != null)
                user.setImageUrl(updateUserDTO.getImageUrl());
            if (updateUserDTO.getBio() != null)
                user.setBio(updateUserDTO.getBio());
            return userRepository.save(user);
        });
    }

    public Optional<Integer> getIdByUsername(String username) {
        return userRepository.getIdByUsername(username);
    }
}
