package com.nnk.springboot.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité représentant un utilisateur du système.
 * Elle contient les champs nécessaires à l'authentification et à l'autorisation.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    /** Nom d'utilisateur */
    @NotBlank(message = "Username is mandatory")
    private String username;

    /**
     * Mot de passe sécurisé, soumis à une validation forte.
     * Doit contenir au moins 8 caractères, une majuscule, un chiffre et un symbole.
     */
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*\\W).{8,}$", message = "the password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter, 1 number and a special character")
    @NotBlank(message = "Password is mandatory")
    private String password;

    /** Nom complet de l'utilisateur. */
    @NotBlank(message = "FullName is mandatory")
    private String fullname;

    /** Rôle de l'utilisateur dans l'application (ex: USER, ADMIN). */
    @NotBlank(message = "Role is mandatory")
    private String role;
}
