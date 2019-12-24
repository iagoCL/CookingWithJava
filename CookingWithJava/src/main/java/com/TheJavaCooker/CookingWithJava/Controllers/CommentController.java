package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.CookingWithJavaApplication;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Comment;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.CommentService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecipeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class CommentController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserController userController;
    @Autowired
    private CommentService commentService;
    @Autowired
    private WebController webController;

    private static final String textPutComment = "Posting the comment.";

    @RequestMapping(value = { "/form-comment" }, method = RequestMethod.POST)
    public String login(Model model, @RequestParam String commentSubject, @RequestParam String commentMessage,
            @RequestParam long commentRecipeId, Principal principal, HttpServletRequest request) {
        User user = userController.activeUser(principal);
        if (user == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError, textPutComment,
                    RecipeController.textNotLogged);
        }
        Recipe recipe = recipeService.searchById(commentRecipeId);
        if (recipe == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError, textPutComment,
                    "The recipe: " + commentRecipeId + " does not exist.");
        }
        Pair<DatabaseService.Errors, Comment> pair = commentService.createComment(commentMessage, commentSubject,
                recipe, user);
        if (pair.getFirst() != DatabaseService.Errors.WITHOUT_ERRORS) {
            return webController.showMessage(model, principal, request, RecipeController.textError, textPutComment,
                    pair.getFirst().name());
        }
        return "redirect:" + CookingWithJavaApplication.getAppURL() + "/recipe-" + commentRecipeId;
    }
}