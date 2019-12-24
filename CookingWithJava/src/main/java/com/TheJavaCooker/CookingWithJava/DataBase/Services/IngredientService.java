package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.PersonalDebug;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Ingredient;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.IngredientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;
    private static final String nullIngredientName = "Null ingredient name: ";
    private static final String nullIngredientAmount = "Null ingredient amount: ";
    private static final String repeatIngredientName = "Repeat ingredient name: ";

    static DatabaseService.Errors checkIngredients(List<Pair<String, String>> ingredientsList) {
        List<String> ingredients = new ArrayList<>();
        for (Pair<String, String> ingredient : ingredientsList) {
            String ingredientName = ingredient.getFirst().toLowerCase();
            if (ingredientName.isEmpty()) {
                PersonalDebug.printMsg(nullIngredientName + ingredientName);
                return DatabaseService.Errors.NULL_INGREDIENT_NAME;
            } else if (ingredient.getSecond().isEmpty()) {
                PersonalDebug.printMsg(nullIngredientAmount + ingredient.getSecond());
                return DatabaseService.Errors.NULL_INGREDIENT_AMOUNT;
            } else if (ingredients.contains(ingredientName)) {
                PersonalDebug.printMsg(repeatIngredientName + ingredientName);
                return DatabaseService.Errors.REPEATED_INGREDIENT_NAME;
            } else {
                ingredients.add(ingredientName);
            }
        }
        return DatabaseService.Errors.WITHOUT_ERRORS;
    }

    Pair<DatabaseService.Errors, Ingredient> createIngredient(String ingredientName, String ingredientAmount,
            Recipe recipe_) {
        Ingredient ingredient = new Ingredient(ingredientName, ingredientAmount, recipe_);
        if (ingredientName.isEmpty()) {
            PersonalDebug.printMsg(nullIngredientName + ingredientName);
            return Pair.of(DatabaseService.Errors.NULL_INGREDIENT_NAME, ingredient);
        } else if (ingredientAmount.isEmpty()) {
            PersonalDebug.printMsg(nullIngredientAmount + ingredientAmount);
            return Pair.of(DatabaseService.Errors.NULL_INGREDIENT_AMOUNT, ingredient);
        } else if (recipe_ == null) {
            PersonalDebug.printMsg("Null Recipe");
            return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, ingredient);
        } else {
            try {
                ingredientRepository.save(ingredient);
            } catch (org.springframework.dao.DataIntegrityViolationException exception) {
                if (exception.toString().contains(Ingredient.constraintUniqueIngredientName)) {
                    PersonalDebug.printMsg(repeatIngredientName + exception.toString());
                    return Pair.of(DatabaseService.Errors.REPEATED_INGREDIENT_NAME, ingredient);
                } else {
                    PersonalDebug.printMsg(UserService.unknownConstraint + exception.toString());
                    return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, ingredient);
                }

            } catch (Exception exception) {
                PersonalDebug.printMsg(UserService.unknownException + exception.toString());
                return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, ingredient);
            }
            recipe_.getIngredients().add(ingredient);
            return Pair.of(DatabaseService.Errors.WITHOUT_ERRORS, ingredient);
        }
    }

    void deleteAll() {
        ingredientRepository.deleteAll();
    }
}