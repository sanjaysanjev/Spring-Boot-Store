package com.sanjay.store.auth;

import com.sanjay.store.Users.User;
import com.sanjay.store.Users.UserRepository;
import lombok.AllArgsConstructor;

import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    public User getCurrentUser()
    {
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        var userId=(Long)authentication.getPrincipal();
        return userRepository.findById(userId).orElse(null);
    }
}
