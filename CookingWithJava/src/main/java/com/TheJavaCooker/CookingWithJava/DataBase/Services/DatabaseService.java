package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseService {
    @Autowired
    private UserService userService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private CommentService CommentService;
    @Autowired
    private FavoriteService FavoriteService;
    @Autowired
    private ImageService imageService;

    public enum Errors {
        REPEATED_COMMENT, REPEATED_INGREDIENT_NAME, REPEATED_STEP_NUMBER, REPEATED_TOOL_NAME, REPEATED_USER_NAME,
        REPEATED_USER_MAIL, REPEATED_RECIPE_NAME, NULL_COMMENT_DESCRIPTION, NULL_COMMENT_TITLE, NULL_FOOD_TYPE,
        NULL_INGREDIENT_AMOUNT, NULL_INGREDIENT_NAME, NULL_MAIL, NULL_NAME_SURNAME, NULL_PASSWORD,
        NULL_STEP_DESCRIPTION, NULL_RECIPE_NAME, NULL_TOOL_NAME, NULL_USER_NAME, INCORRECT_STEP_TIME,
        INCORRECT_STEP_NUMBER, INCORRECT_IMAGE, WITHOUT_ERRORS, UNKNOWN_ERROR
    }

    public UserService getUserService() {
        return userService;
    }

    public RecipeService getRecipeService() {
        return recipeService;
    }

    public CommentService getCommentService() {
        return CommentService;
    }

    public FavoriteService getFavoriteService() {
        return FavoriteService;
    }

    public ImageService getImageService() {
        return imageService;
    }

    public void deleteAll() {
        CommentService.deleteAll();
        recipeService.deleteAll();
        userService.deleteAll();
        imageService.deleteAll();
    }
}