package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.RecipeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = { "recipesCache", "usersCache" })
public class FavoriteService {
    @Autowired
    private RecipeRepository recipeRepository;

    @CacheEvict(allEntries = true)
    public boolean markAsFavorite(User user_, Recipe recipe_) {
        if (recipe_.markAsFavorite(user_)) {
            recipeRepository.save(recipe_);
            return true;
        } else {
            return false;
        }
    }

    @CacheEvict(allEntries = true)
    public boolean removeFavorite(User user_, Recipe recipe_) {
        if (recipe_.removeFavorite(user_)) {
            recipeRepository.save(recipe_);
            return true;
        } else {
            return false;
        }
    }
}