package com.nnk.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configure la chaîne de filtres de sécurité pour l'application.
     * Autorise l'accès public aux pages de login et d'inscription,
     * exige une authentification pour toute autre requête.
     *
     * @param http configuration HTTP
     * @return la chaîne de filtres configurée
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/style/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .usernameParameter("username")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/admin/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/app-logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Fournit un gestionnaire d'authentification basé sur un fournisseur DAO.
     * Utilise un encodage BCrypt et un UserDetailsService personnalisé.
     *
     * @param userDetailsService service pour charger les utilisateurs
     * @param passwordEncoder encodeur de mots de passe
     * @return le gestionnaire d'authentification
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authProvider);
    }

    /**
     * Fournit un encodeur de mot de passe utilisant l'algorithme BCrypt.
     *
     * @return un encodeur BCrypt
     */
    @Bean
    PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
    }
}
