package com.nnk.springboot.services;

import com.nnk.springboot.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service chargé de récupérer les détails de l'utilisateur pour Spring Security.
 * Implémente {@link org.springframework.security.core.userdetails.UserDetailsService}.
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur par son nom d'utilisateur (email dans ce cas).
     *
     * @param username le nom d'utilisateur (email)
     * @return les détails de l'utilisateur pour Spring Security
     * @throws UsernameNotFoundException si aucun utilisateur correspondant n'est trouvé
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Tentative de chargement de l'utilisateur avec l'email: {}", username);

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Échec du chargement de l'utilisateur : Aucun utilisateur trouvé avec l'email {}", username);
                    return new UsernameNotFoundException("User not found with email: " + username);
                });

        logger.info("Utilisateur chargé avec succès : {}", user.getUsername());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}