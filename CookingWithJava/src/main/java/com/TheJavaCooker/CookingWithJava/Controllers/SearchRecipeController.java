package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.DifficultyLevel;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecipeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class SearchRecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private WebController webController;

    @GetMapping(value = { "/search", "/searchRecipe", "search-recipe" })
    public String search(Model model, HttpServletRequest request, Principal principal) {
        webController.addHeader(principal, request, model);
        return "search";
    }

    @RequestMapping(value = { "/form-search-recipe" }, method = RequestMethod.GET)
    public String formSearchRecipe(Model model, HttpServletRequest request, Principal principal,
            @RequestParam String recipeName, @RequestParam String foodType, @RequestParam String difficultyLevel,
            @RequestParam Integer minRecipeDuration, @RequestParam Integer maxRecipeDuration,
            @RequestParam Integer numStepsMax, @RequestParam Integer numStepsMin, @RequestParam Integer numFavorites,
            @RequestParam Integer numComments) {
        DifficultyLevel difficulty_level = DifficultyLevel.fromString(difficultyLevel);
        List<Recipe> searchedRecipes = recipeService.searchRecipe(0, 15, maxRecipeDuration, minRecipeDuration,
                numFavorites, numComments, numStepsMax, numStepsMin, foodType, recipeName, difficulty_level, null,
                null);
        model.addAttribute("recipes", searchedRecipes);
        webController.addHeader(principal, request, model);
        return "recipes";
    }
}