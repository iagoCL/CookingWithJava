package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.CookingWithJavaApplication;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;
import com.TheJavaCooker.CookingWithJava.DataBase.DifficultyLevel;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Controller
public class EditRecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserController userController;
    @Autowired
    private WebController webController;

    private final static String textEditingRecipe = "Editing the recipe.";

    @PostMapping(value = { "/form-edit-recipe" })
    public String formEditRecipe(Model model, @RequestParam Map<String, String> allRequestParams, Principal principal,
            HttpServletRequest request) {

        User user = userController.activeUser(principal);
        if (user == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError, textEditingRecipe,
                    RecipeController.textNotLogged);
        } else if (user.getId() != user.getId()) {
            return webController.showMessage(model, principal, request, RecipeController.textError, textEditingRecipe,
                    "The active user is not the creator.");
        }

        String recipeName = allRequestParams.get("recipeName");
        String foodType = allRequestParams.get("foodType");
        String difficultyLevel = allRequestParams.get("difficultyLevel");
        int recipeId = Integer.parseInt(allRequestParams.get("recipeId"));
        Recipe recipe = recipeService.searchById(recipeId);
        recipe.setDifficultLevel(DifficultyLevel.fromString(difficultyLevel));
        recipe.setFoodType(foodType);
        recipe.setRecipeName(recipeName);
        Pair<DatabaseService.Errors, Recipe> pair = null;
        try {
            pair = recipeService.updateRecipe(recipe);
        } catch (Exception exception) {
            return webController.showMessage(model, principal, request, RecipeController.textError,
                    "Loading Recipe Image.", exception.toString());
        }
        if (pair.getFirst() != DatabaseService.Errors.WITHOUT_ERRORS) {
            return webController.showMessage(model, principal, request, RecipeController.textError,
                    "Editing the Recipe.", pair.getFirst().name());
        }
        return "redirect:" + CookingWithJavaApplication.getAppURL() + "/recipe-" + pair.getSecond().getId();
    }

    @GetMapping(value = { "/editRecipe-{recipeId}" })
    public String searchRecipe(Model model, @PathVariable long recipeId, HttpServletRequest request,
            Principal principal) {

        Recipe recipe = recipeService.searchById(recipeId);
        if (recipe == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError,
                    "Searching the Recipe.", "Recipe: " + recipeId + " cannot be found.");
        }
        model.addAttribute("recipe", recipe);
        model.addAttribute("foodType", recipe.getFoodType());
        webController.addHeader(principal, request, model);
        return "editRecipe";
    }
}