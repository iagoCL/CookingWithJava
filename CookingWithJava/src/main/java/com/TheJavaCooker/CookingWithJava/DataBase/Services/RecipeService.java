package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.DifficultyLevel;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Imagedb;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.QRecipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.RecipeRepository;
import com.TheJavaCooker.CookingWithJava.DataBase.ImageType;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import com.querydsl.core.types.dsl.BooleanExpression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@CacheConfig(cacheNames = "recipesCache")
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private StepService stepService;
    @Autowired
    private ToolService toolService;
    @Autowired
    private ImageService imageService;

    @CacheEvict(value = { "usersCache", "recipesCache" }, allEntries = true)
    public Pair<DatabaseService.Errors, Recipe> createRecipe(String nameRecipe_, String foodType,
            String difficultyLevel_, byte[] recipeImage, List<Pair<String, String>> ingredientsList_,
            List<Pair<String, String>> toolsLists_, List<Pair<Integer, String>> stepsList_, User user_) {
        return createRecipeWithDate(nameRecipe_, foodType, difficultyLevel_, LocalDate.now(), recipeImage,
                ingredientsList_, toolsLists_, stepsList_, user_);
    }

    @CacheEvict(value = { "usersCache", "recipesCache" }, allEntries = true)
    public Pair<DatabaseService.Errors, Recipe> createRecipeWithDate(String nameRecipe_, String foodType,
            String difficultyLevel_, LocalDate localDate_, byte[] recipeImage,
            List<Pair<String, String>> ingredientsList_, List<Pair<String, String>> toolsLists_,
            List<Pair<Integer, String>> stepsList_, User user_) {

        if (ingredientsList_.isEmpty()) {
            PersonalDebug.printMsg("WARNING: Creating a recipe without ingredients.");
        } else {
            DatabaseService.Errors errorIngredients = IngredientService.checkIngredients(ingredientsList_);
            if (errorIngredients != DatabaseService.Errors.WITHOUT_ERRORS) {
                PersonalDebug.printMsg("Error in the recipe ingredients.");
                return Pair.of(errorIngredients, new Recipe());
            }
        }
        if (toolsLists_.isEmpty()) {
            PersonalDebug.printMsg("WARNING: Creating a recipe without tools.");
        } else {
            DatabaseService.Errors errorTools = ToolService.checkTools(toolsLists_);
            if (errorTools != DatabaseService.Errors.WITHOUT_ERRORS) {
                PersonalDebug.printMsg("Error in the recipe tools.");
                return Pair.of(errorTools, new Recipe());
            }
        }
        if (stepsList_.isEmpty()) {
            PersonalDebug.printMsg("WARNING: Creating a recipe without steps.");
        } else {
            DatabaseService.Errors errorSteps = StepService.checkSteps(stepsList_);
            if (errorSteps != DatabaseService.Errors.WITHOUT_ERRORS) {
                PersonalDebug.printMsg("Error in the recipe steps.");
                return Pair.of(errorSteps, new Recipe());
            }
        }
        Pair<DatabaseService.Errors, Imagedb> pairRecipe = imageService.createImagedb(recipeImage, ImageType.RECIPE);
        if (pairRecipe.getFirst() != DatabaseService.Errors.WITHOUT_ERRORS) {
            return Pair.of(pairRecipe.getFirst(), new Recipe());
        }
        Recipe newRecipe = new Recipe(nameRecipe_, foodType, DifficultyLevel.fromString(difficultyLevel_), localDate_,
                pairRecipe.getSecond(), user_);
        Pair<DatabaseService.Errors, Recipe> newRecipePair = updateRecipe(newRecipe);
        if (newRecipePair.getFirst() == DatabaseService.Errors.WITHOUT_ERRORS) {
            for (Pair<String, String> ingredientString : ingredientsList_) {
                DatabaseService.Errors errorIngredients = ingredientService
                        .createIngredient(ingredientString.getFirst(), ingredientString.getSecond(), newRecipe)
                        .getFirst();
                if (errorIngredients != DatabaseService.Errors.WITHOUT_ERRORS) {
                    PersonalDebug.printMsg("Error in the recipe ingredients.");
                    return Pair.of(errorIngredients, newRecipe);
                }
            }
            for (Pair<String, String> toolString : toolsLists_) {
                DatabaseService.Errors errorTools = toolService
                        .createTool(toolString.getFirst(), toolString.getSecond(), newRecipe).getFirst();
                if (errorTools != DatabaseService.Errors.WITHOUT_ERRORS) {
                    PersonalDebug.printMsg("Error in the recipe tools.");
                    return Pair.of(errorTools, newRecipe);
                }
            }
            for (int i = 0, l = stepsList_.size(); i < l;) {
                Pair<Integer, String> stepPair = stepsList_.get(i);
                DatabaseService.Errors errorSteps = stepService
                        .createStep(++i, stepPair.getFirst(), stepPair.getSecond(), newRecipe).getFirst();
                if (errorSteps != DatabaseService.Errors.WITHOUT_ERRORS) {
                    PersonalDebug.printMsg("Error in the recipe steps.");
                    return Pair.of(errorSteps, newRecipe);
                }
            }
            newRecipe.updateNumSteps();
            recipeRepository.save(newRecipe);
        }
        return newRecipePair;

    }

    @CacheEvict(value = "recipesCache", allEntries = false)
    public Pair<DatabaseService.Errors, Recipe> updateRecipe(Recipe recipe_) {
        if (recipe_.getRecipeName().isEmpty()) {
            PersonalDebug.printMsg("Null recipe name: " + recipe_.getRecipeName());
            return Pair.of(DatabaseService.Errors.NULL_RECIPE_NAME, recipe_);
        } else if (recipe_.getFoodType().isEmpty()) {
            PersonalDebug.printMsg("Null food type: " + recipe_.getFoodType());
            return Pair.of(DatabaseService.Errors.NULL_FOOD_TYPE, recipe_);
        } else {
            if (recipe_.getCreationDate() == null) {
                PersonalDebug.printMsg("WARNING: Null recipe date, using today date.");
                recipe_.setCreationDate();
            }
            if (recipe_.getDifficultLevel() == null) {
                PersonalDebug.printMsg("WARNING: Null difficulty level, set to undefined");
                recipe_.setDifficultLevel(DifficultyLevel.UNDEFINED);
            }
            try {
                recipeRepository.save(recipe_);
            } catch (org.springframework.dao.DataIntegrityViolationException exception) {
                if (exception.toString().contains(Recipe.constraintRecipeName)) {
                    PersonalDebug.printMsg("Repeated recipe name: " + exception.toString());
                    return Pair.of(DatabaseService.Errors.REPEATED_RECIPE_NAME, recipe_);
                } else {
                    PersonalDebug.printMsg(UserService.unknownConstraint + exception.toString());
                    return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, recipe_);
                }
            } catch (Exception exception) {
                PersonalDebug.printMsg(UserService.unknownException + exception.toString());
                return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, recipe_);
            }
            return Pair.of(DatabaseService.Errors.WITHOUT_ERRORS, recipe_);
        }
    }

    public Recipe searchById(long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "recipesCache")
    public List<Recipe> searchRecipe(int pageIndex, int pageElements, Integer maxDuration, Integer minDuration,
            Integer minFavorites, Integer minComments, Integer maxNumSteps, Integer minNumSteps, String foodType_,
            String nameDeRecipe_, DifficultyLevel difficultyLevel_, List<String> ingredients_, List<String> tools_) {
        if (maxDuration == null) {
            maxDuration = Integer.MAX_VALUE;
        }
        if (minDuration == null) {
            minDuration = 0;
        }
        if (maxDuration < minDuration) {
            return new ArrayList<>();
        }
        QRecipe recipeDsl = QRecipe.recipe;
        BooleanExpression predicate = recipeDsl.total_duration.between(minDuration, maxDuration);
        if (minNumSteps != null && maxNumSteps != null && minNumSteps > maxNumSteps) {
            return new ArrayList<>();
        }
        if (minNumSteps != null && minNumSteps > 0) {
            predicate = predicate.and(recipeDsl.steps_number.goe(minNumSteps));
        }
        if (maxNumSteps != null) {
            predicate = predicate.and(recipeDsl.steps_number.loe(maxNumSteps));
        }
        if (minFavorites != null && minFavorites > 0) {
            predicate = predicate.and(recipeDsl.number_favorites.goe(minFavorites));
        }
        if (minComments != null && minComments > 0) {
            predicate = predicate.and(recipeDsl.number_comments.goe(minComments));
        }
        if (foodType_ != null && !foodType_.isEmpty()) {
            predicate = predicate.and(recipeDsl.food_type.like(foodType_.toLowerCase()));
        }
        if (nameDeRecipe_ != null && !nameDeRecipe_.isEmpty()) {
            predicate = predicate.and(recipeDsl.recipe_name.likeIgnoreCase("%" + nameDeRecipe_ + "%"));
        }
        if (difficultyLevel_ != null) {
            predicate = predicate.and(recipeDsl.difficulty_level.eq(difficultyLevel_));
        }

        List<Recipe> recipes = new ArrayList<>();
        Iterable<Recipe> it = recipeRepository.findAll(predicate, PageRequest.of(pageIndex, pageElements));
        it.forEach((e) -> recipes.add(e));
        return recipes;
    }

    @Cacheable(value = "recipesCache")
    public List<Recipe> createdRecipes(long recipeCreatorId) {
        QRecipe recipeDsl = QRecipe.recipe;
        BooleanExpression predicate = recipeDsl.recipe_creator.id.eq(recipeCreatorId);
        List<Recipe> recipes = new ArrayList<>();
        Iterable<Recipe> it = recipeRepository.findAll(predicate, PageRequest.of(0, 15));
        it.forEach((e) -> recipes.add(e));
        return recipes;
    }

    @Cacheable(value = "recipesCache")
    public List<Recipe> favoriteRecipes(User user) {
        QRecipe recipeDsl = QRecipe.recipe;
        BooleanExpression predicate = recipeDsl.favorites.contains(user);
        List<Recipe> recipes = new ArrayList<>();
        Iterable<Recipe> it = recipeRepository.findAll(predicate, PageRequest.of(0, 15));
        it.forEach((e) -> recipes.add(e));
        return recipes;
    }

    @CacheEvict(value = { "usersCache", "recipesCache" }, allEntries = true)
    public void deleteAll() {
        recipeRepository.deleteAll();
        ingredientService.deleteAll();
        stepService.deleteAll();
        toolService.deleteAll();
    }

    @Cacheable(value = "recipesCache")
    public List<Recipe> allRecipes() {
        return recipeRepository.findAll();
    }

    @Cacheable(value = "recipesCache")
    public long getNumRecipes() {
        return recipeRepository.count();
    }
}