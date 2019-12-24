package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.CookingWithJavaApplication;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;

import com.TheJavaCooker.CookingWithJava.DataBase.Services.FavoriteService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class FavoriteController {
    @Autowired
    private UserController userController;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private WebController webController;

    private static final String textMarkingAsFavorite = "Marking as Favorite.";

    @PostMapping(value = { "/form-favorite" })
    public String formFavorite(Model model, @RequestParam boolean markAsFavorite, @RequestParam long favoriteRecipeId,
            Principal principal, HttpServletRequest request) {
        User user = userController.activeUser(principal);
        if (user == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError,
                    textMarkingAsFavorite, UserController.textUserNotFound);
        }
        Recipe recipe = recipeService.searchById(favoriteRecipeId);
        if (recipe == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError,
                    textMarkingAsFavorite, "Selected recipe not found.");
        }
        if (markAsFavorite) {
            if (!favoriteService.markAsFavorite(user, recipe)) {
                webController.showMessage(model, principal, request, RecipeController.textError, textMarkingAsFavorite,
                        "Already mark as favorite by the actual user.");
            }
        } else {
            if (!favoriteService.removeFavorite(user, recipe)) {
                webController.showMessage(model, principal, request, RecipeController.textError, "Removing Favorite.",
                        "Not marked as favorite by the actual user");
            }
        }
        return "redirect:" + CookingWithJavaApplication.getAppURL() + "/recipe-" + favoriteRecipeId;
    }
}