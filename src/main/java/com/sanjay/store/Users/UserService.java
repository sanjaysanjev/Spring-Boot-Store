package com.sanjay.store.Users;

import lombok.AllArgsConstructor;

//import lombok.var;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService{

    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        var user=userRepository.findByemail(email).orElseThrow(()->new UsernameNotFoundException("User not Found"));
        return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
