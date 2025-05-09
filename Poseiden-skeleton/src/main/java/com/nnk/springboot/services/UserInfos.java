package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserInfos {
    @Autowired
    private UserRepository userRepository;

    public User getUserInfos(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var username = auth.getName();

        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not Found with email : " + username));
    }
}
