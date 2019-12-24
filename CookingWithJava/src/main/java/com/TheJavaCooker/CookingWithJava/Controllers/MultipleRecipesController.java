package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecipeService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MultipleRecipesController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserController userController;
    @Autowired
    private UserService userService;
    @Autowired
    private WebController webController;

    @GetMapping(value = { "/recipes" })
    public String showRecipes(Model model, Principal principal, HttpServletRequest request) {
        model.addAttribute("recipes", recipeService.allRecipes());
        webController.addHeader(principal, request, model);
        return "recipes";
    }

    @GetMapping(value = { "/my-favorite-recipes" })
    public String showFavoriteRecipes(Model model, Principal principal, HttpServletRequest request) {
        User user = userController.activeUser(principal);
        if (user == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError,
                    "Showing favorite recipes.", UserController.textUserNotFound);
        }
        model.addAttribute("recipes", recipeService.favoriteRecipes(user));
        webController.addHeader(user, request, model);
        return "recipes";
    }

    @GetMapping(value = { "/favorite-recipes-{userId}" })
    public String showFavoriteRecipes(Model model, Principal principal, HttpServletRequest request,
            @PathVariable long userId) {
        User user = userService.searchById(userId);
        model.addAttribute("recipes", recipeService.favoriteRecipes(user));
        webController.addHeader(principal, request, model);
        return "recipes";
    }

    @GetMapping(value = { "/my-created-recipes" })
    public String showCreatedRecipes(Model model, Principal principal, HttpServletRequest request) {
        User user = userController.activeUser(principal);
        if (user == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError,
                    "Showing created recipes.", UserController.textUserNotFound);
        }
        model.addAttribute("recipes", recipeService.createdRecipes(user.getId()));
        webController.addHeader(user, request, model);
        return "recipes";
    }

    @GetMapping(value = { "/created-recipes-{userId}" })
    public String showCreatedRecipes(Model model, Principal principal, HttpServletRequest request,
            @PathVariable long userId) {
        model.addAttribute("recipes", recipeService.createdRecipes(userId));
        webController.addHeader(principal, request, model);
        return "recipes";
    }
}