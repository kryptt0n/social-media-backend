package com.example.msssuserprofilecrud.services;


import com.example.msssuserprofilecrud.dto.UpdateUserDTO;
import com.example.msssuserprofilecrud.dto.UserProfileDTO;
import com.example.msssuserprofilecrud.dto.UserEmailDTO;
import com.example.msssuserprofilecrud.dto.UserStatsResponse;
import com.example.msssuserprofilecrud.entities.User;
import com.example.msssuserprofilecrud.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserCrudService {

    private final UserRepository userRepository;


    public UserCrudService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserProfileDTO> getUserByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserProfileDTO result = new UserProfileDTO(userId, user.getBio(), user.getEmail(), user.isAccountNonLocked(), user.isPublic());
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public User registerUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles("USER");
        }

        return userRepository.save(user);
    }

    public List<UserProfileDTO> getAllUserProfiles() {
        return userRepository.findAll().stream()
                .map(user -> new UserProfileDTO(
                        user.getId(),
                        user.getBio(),
                        user.getEmail(),
                        user.isAccountNonLocked(),
                        user.isPublic()
                ))
                .toList();
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

    public Optional<User> updateUser(UpdateUserDTO updateUserDTO, Integer id) {
        return userRepository.findById(id).map(user -> {
            if (updateUserDTO.getBio() != null)
                user.setBio(updateUserDTO.getBio());
            return userRepository.save(user);
        });
    }

    public UserEmailDTO getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        UserEmailDTO result;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            result = new UserEmailDTO(true, user.getEmail(), user.getId());
        } else {
            result = new UserEmailDTO(false, null, 0);
        }

        return result;
    }

    public UserStatsResponse getUserStats() {
        long total = userRepository.count();
        long publicCount = userRepository.countByIsPublicTrue();
        long privateCount = userRepository.countByIsPublicFalse();

        return new UserStatsResponse(total, publicCount, privateCount);
    }
}
