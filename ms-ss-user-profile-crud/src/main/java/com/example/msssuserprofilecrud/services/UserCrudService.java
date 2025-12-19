package com.example.msssuserprofilecrud.services;


import com.example.msssuserprofilecrud.controllers.UserCrudController;
import com.example.msssuserprofilecrud.dto.UpdateUserDTO;
import com.example.msssuserprofilecrud.dto.UserProfileDTO;
import com.example.msssuserprofilecrud.dto.UserShortDTO;
import com.example.msssuserprofilecrud.dto.UserStatsResponse;
import com.example.msssuserprofilecrud.entities.User;
import com.example.msssuserprofilecrud.exceptions.UserAlreadyExistsException;
import com.example.msssuserprofilecrud.exceptions.UserNotFoundException;
import com.example.msssuserprofilecrud.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserCrudService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserCrudService.class);


    public UserCrudService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserProfileDTO> getUserByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserProfileDTO result = new UserProfileDTO(userId, user.getBio(), user.getEmail(), user.getUsername(), user.isAccountNonLocked(), user.isPublic());
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with this value already exists");
        }

        user.setCreatedAt(LocalDateTime.now());
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles("USER");
        }

        return userRepository.save(user);
    }

    public List<UserProfileDTO> getAllUserProfiles() {
        return userRepository.findAll().stream()
                .map(this::convertUserToUserProfile)
                .toList();
    }

    public Optional<UserProfileDTO> getUserProfileByUsername(String username) {
        log.warn("Found in repository: {}", userRepository.findByUsername(username));
        return userRepository.findByUsername(username).map(this::convertUserToUserProfile);
    }


    @Transactional
    public void deactivateUser(Integer id) {
        userRepository.deactivateUser(id);
    }

    @Transactional
    public void recoverUser(Integer id) {
        userRepository.recoverUser(id);
    }

    @Transactional
    public void setPublic(Integer id) {
        userRepository.setPublic(id);
    }

    @Transactional
    public void setPrivate(Integer id) {
        userRepository.setPrivate(id);
    }

    @Transactional
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteUserWithEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    public Optional<User> updateUser(UpdateUserDTO updateUserDTO, Integer id) {
        return userRepository.findById(id).map(user -> {
            if (updateUserDTO.getBio() != null)
                user.setBio(updateUserDTO.getBio());
            return userRepository.save(user);
        });
    }

    public UserShortDTO getUserByEmail(String email) {
        return extractUserInfo(userRepository.findByEmail(email));
    }

    public UserShortDTO getUserByUsername(String username) {
        return extractUserInfo(userRepository.findByUsername(username));
    }

    public UserStatsResponse getUserStats() {
        long total = userRepository.count();
        long publicCount = userRepository.countByIsPublicTrue();
        long privateCount = userRepository.countByIsPublicFalse();

        return new UserStatsResponse(total, publicCount, privateCount);
    }

    private UserShortDTO extractUserInfo(Optional<User> userOptional) {
        UserShortDTO result;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            result = new UserShortDTO(true, user.getEmail(), user.getUsername(), user.getId());
        } else {
           throw new UserNotFoundException("User not found");
        }

        return result;
    }

    private UserProfileDTO convertUserToUserProfile(User user) {
        return new UserProfileDTO(user.getId(),
                user.getBio(),
                user.getEmail(),
                user.getUsername(),
                user.isAccountNonLocked(),
                user.isPublic());
    }
}
