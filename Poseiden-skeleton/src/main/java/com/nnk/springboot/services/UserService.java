package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsable de la gestion des utilisateurs.
 * Fournit les opérations de création et d'encodage des mots de passe.
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Crée un nouvel utilisateur après avoir vérifié son unicité et encodé son mot de passe.
     *
     * @param user L'utilisateur à créer.
     * @return L'utilisateur sauvegardé.
     * @throws RuntimeException si l'utilisateur existe déjà.
     */
    public User create(User user){
        if (userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("L'utilisateur existe déjà");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
