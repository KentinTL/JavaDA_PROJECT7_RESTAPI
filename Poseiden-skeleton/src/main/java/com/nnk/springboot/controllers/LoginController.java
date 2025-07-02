package com.nnk.springboot.controllers;

import com.nnk.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Contrôleur gérant les routes liées à l'authentification utilisateur.
 * Utilisé pour afficher la page de login et les erreurs d'accès.
 */
@Controller
@RequestMapping("app")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * Affiche la page de login.
     *
     * @return la vue "login"
     */
    @GetMapping("login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    /**
     * Affiche la liste des utilisateurs (accessible après connexion).
     *
     * @return la vue "user/list" avec les utilisateurs
     */
    @GetMapping("secure/article-details")
    public ModelAndView getAllUserArticles() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userService.findAll());
        mav.setViewName("user/list");
        return mav;
    }

    /**
     * Affiche une page d'erreur 403 personnalisée en cas d'accès non autorisé.
     *
     * @return la vue "403"
     */
    @GetMapping("/403")
    public ModelAndView error() {
        ModelAndView mav = new ModelAndView();
        String errorMessage= "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }
}
