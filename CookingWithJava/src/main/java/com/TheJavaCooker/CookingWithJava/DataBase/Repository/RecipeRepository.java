package com.TheJavaCooker.CookingWithJava.DataBase.Repository;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

@org.springframework.stereotype.Repository

public interface RecipeRepository
        extends JpaRepository<Recipe, Long>, Repository<Recipe, Long>, QuerydslPredicateExecutor<Recipe> {

    @Query("select r from Recipe r where r.recipe_name = ?1")
    Recipe searchByRecipeName(String searchByRecipeName);

    @Query("select r from Recipe r where r.food_type = ?1")
    List<Recipe> searchByFoodType(String food_type_in_lowercase);

    @Query("select distinct r.food_type from Recipe r")
    List<String> foodTypes();

    @Query("select r from Recipe r order by r.creation_date desc")
    Page<Recipe> lastRecipes(PageRequest pageable);

    long count();// number of recipes
}