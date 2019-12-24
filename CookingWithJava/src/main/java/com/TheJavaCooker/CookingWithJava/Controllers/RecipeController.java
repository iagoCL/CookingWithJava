package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.CookingWithJavaApplication;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class RecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserController userController;
    @Autowired
    private WebController webController;

    private static final String textCreatingRecipe = "Creating a new Recipe.";
    protected static final String textNotLogged = "User not logged.";
    protected final static String textError = "ERROR:";

    @GetMapping(value = { "/createRecipe", "/create-recipe", "/upload-recipe", "/uploadRecipe" })
    public String createRecipe(Model model, Principal principal, HttpServletRequest request) {
        webController.addHeader(principal, request, model);
        return "createRecipe";
    }

    @PostMapping(value = { "/form-create-recipe" })
    public String formCreateRecipe(Model model, @RequestParam Map<String, String> allRequestParams,
            @RequestParam("recipeImage") MultipartFile recipeImage, Principal principal, HttpServletRequest request) {

        User user = userController.activeUser(principal);
        if (user == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError, textCreatingRecipe,
                    textNotLogged);
        }

        String recipeName = allRequestParams.get("recipeName");
        String foodType = allRequestParams.get("foodType");
        String difficultyLevel = allRequestParams.get("difficultyLevel");
        Integer numSteps = Integer.parseInt(allRequestParams.get("numSteps"));
        Integer numIngredients = Integer.parseInt(allRequestParams.get("numIngredients"));
        Integer numTools = Integer.parseInt(allRequestParams.get("numTools"));
        List<Pair<String, String>> ingredientsList = new ArrayList<>(numIngredients);
        List<Pair<String, String>> toolsLists = new ArrayList<>(numTools);
        List<Pair<Integer, String>> stepsList = new ArrayList<>(numSteps);
        for (int i = 1; i <= numSteps; ++i) {
            Integer duration = Integer.parseInt(allRequestParams.get("step-" + i + "Duration"));
            String description = allRequestParams.get("step-" + i + "Description");
            stepsList.add(Pair.of(duration, description));
        }
        for (int i = 1; i <= numIngredients; ++i) {
            String amount = allRequestParams.get("ingredient-" + i + "Amount");
            String name = allRequestParams.get("ingredient-" + i + "Name");
            ingredientsList.add(Pair.of(name, amount));
        }
        for (int i = 1; i <= numTools; ++i) {
            String difficulty = allRequestParams.get("tool-" + i + "Level");
            String name = allRequestParams.get("tool-" + i + "Name");
            toolsLists.add(Pair.of(name, difficulty));
        }
        Pair<DatabaseService.Errors, Recipe> pair = null;
        try {
            pair = recipeService.createRecipe(recipeName, foodType, difficultyLevel, recipeImage.getBytes(),
                    ingredientsList, toolsLists, stepsList, user);
        } catch (IOException exception) {
            return webController.showMessage(model, principal, request, RecipeController.textError,
                    "Loading Image Recipe.", exception.toString());
        }
        if (pair.getFirst() != DatabaseService.Errors.WITHOUT_ERRORS) {
            return webController.showMessage(model, principal, request, RecipeController.textError, textCreatingRecipe,
                    pair.getFirst().name());
        }
        return "redirect:" + CookingWithJavaApplication.getAppURL() + "/recipe-" + pair.getSecond().getId();
    }

    @GetMapping(value = { "/recipe-{recipeId}", "/complete-recipe-{recipeId}", "/completeRecipe-{recipeId}" })
    public String showRecipe(Model model, @PathVariable long recipeId, HttpServletRequest request,
            Principal principal) {

        Recipe recipe = recipeService.searchById(recipeId);
        if (recipe == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError, "Searching Recipe.",
                    "Recipe: " + recipeId + " not found.");
        }
        model.addAttribute("recipe", recipe);
        model.addAttribute("foodType", recipe.getFoodType());
        model.addAttribute("favorites", recipe.getNumFavorites());
        model.addAttribute("creationDate", recipe.getStringCreationDate());
        model.addAttribute("totalDuration", recipe.getStringTotalDuration());
        User recipeCreator = recipe.getRecipeCreator();
        model.addAttribute("recipeNameCreator", recipeCreator.getUserName());
        model.addAttribute("recipeIdCreator", recipeCreator.getId());
        model.addAttribute("steps", recipe.getSteps());
        model.addAttribute("comments", recipe.getComments());
        model.addAttribute("tools", recipe.getTools());
        model.addAttribute("ingredients", recipe.getIngredients());
        boolean isFavorite = false;
        boolean isCreator = false;
        User user = userController.activeUser(principal);
        if (user != null) {
            isFavorite = recipe.isFavorite(user);
            isCreator = recipeCreator.getId() == user.getId();
        }
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("isCreator", isCreator);
        webController.addHeader(principal, request, model);
        return "complete-recipe";
    }
}