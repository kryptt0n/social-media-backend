package com.example.mainservice.configurations;

import com.example.mainservice.entities.User;
import com.example.mainservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String[] roles = user.getRoles() != null && !user.getRoles().isEmpty()
                ? user.getRoles().split(",")
                : new String[] {"ROLE_USER"};

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(roles)
                .disabled(false)
                .accountLocked(user.isAccountNonLocked())
                .build();
    }
}
