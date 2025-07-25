package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * Contrôleur Spring MVC pour la gestion des utilisateurs.
 * Fournit les opérations CRUD : création, lecture, mise à jour et suppression.
 */
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Affiche la liste des utilisateurs.
     *
     * @param model Le modèle pour transmettre les données à la vue.
     * @return La vue "user/list".
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    /**
     * Affiche le formulaire d'ajout d'utilisateur.
     *
     * @param user L'objet utilisateur vide pour le binding.
     * @return La vue "user/add".
     */
    @GetMapping("/user/add")
    public String addUser(User user) {
        return "user/add";
    }

    /**
     * Valide et enregistre un nouvel utilisateur.
     *
     * @param user   L'utilisateur soumis.
     * @param result Résultat de la validation.
     * @param model  Le modèle utilisé pour la vue.
     * @return Redirection vers la liste ou vue "add" en cas d'erreur.
     */
    @PostMapping("/user/validate")
    public String validate(@Validated User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/add";
        }
        try {
            userService.create(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        } catch (RuntimeException e) {
            model.addAttribute("message", e.getMessage());
            return "user/add";
        }

    }

    /**
     * Affiche le formulaire de mise à jour pour un utilisateur existant.
     *
     * @param id    L'identifiant de l'utilisateur à modifier.
     * @param model Le modèle avec l'utilisateur trouvé.
     * @return La vue "user/update".
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.findById(id).orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user Id: " + id));
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * Met à jour un utilisateur existant après validation.
     *
     * @param id     L'identifiant de l'utilisateur.
     * @param user   Les nouvelles données de l'utilisateur.
     * @param result Résultat de la validation.
     * @param model  Le modèle pour la vue.
     * @return Redirection vers la liste des utilisateurs.
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        userService.update(id, user);
        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }

    /**
     * Supprime un utilisateur par son identifiant.
     *
     * @param id    L'identifiant de l'utilisateur à supprimer.
     * @param model Le modèle utilisé pour la vue.
     * @return Redirection vers la liste des utilisateurs.
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.delete(id);
        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }
}
