package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public void delete(Integer id) {
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Invalid user Id:" + id));
        userRepository.deleteById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void update(Integer id, User user) {
        var userFound = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Invalid user Id:" + id));
        userFound.setPassword(passwordEncoder.encode(user.getPassword()));
        userFound.setFullname(user.getFullname());
        userFound.setUsername(user.getUsername());
        userFound.setRole(user.getRole());

        userRepository.saveAndFlush(userFound);
    }
}
