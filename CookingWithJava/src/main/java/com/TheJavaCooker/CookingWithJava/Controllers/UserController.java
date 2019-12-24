package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.CookingWithJavaApplication;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.UserRepository;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecipeService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.UserService;
import com.TheJavaCooker.CookingWithJava.UserRepositoryAuthenticationProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserRepositoryAuthenticationProvider userAuthentication;
    @Autowired
    private WebController webController;

    protected static final String textUserNotFound = "Actual user not found.";
    protected static final String textSearchingUser = "Searching user.";

    @GetMapping(value = { "/login", "/register" })
    public String login(Model model, Principal principal, HttpServletRequest request) {
        webController.addHeader(principal, request, model);
        return "login";

    }

    @GetMapping(value = { "/login-error", "/loginError" })
    public String loginInvalid(Model model, Principal principal, HttpServletRequest request) {
        return webController.showMessage(model, principal, request, RecipeController.textError, "Login User.",
                "Incorrect login.");
    }

    @GetMapping(value = { "/logout-success", "/logoutSuccess" })
    public String logoutSuccessful(Model model, Principal principal, HttpServletRequest request) {
        return webController.showMessage(model, principal, request, "message:", "The user has been disconnected.",
                "Correct Logout.");
    }

    @PostMapping(value = { "/form-sign" })
    public String formSignIn(Model model, Principal principal, HttpServletRequest request,
            @RequestParam String nickSignIn, @RequestParam String passSignIn, @RequestParam String passBSignIn,
            @RequestParam String mailSignIn, @RequestParam String nameSignIn,
            @RequestParam("imageSignIn") MultipartFile imageSignIn) {
        if (!passBSignIn.equals(passSignIn)) {
            return webController.showMessage(model, principal, request, RecipeController.textError, "Signing User.",
                    "Different passwords.");
        }
        Pair<DatabaseService.Errors, User> pair = null;
        try {
            pair = userService.createUser(nickSignIn, passSignIn, mailSignIn, nameSignIn, imageSignIn.getBytes());
        } catch (IOException exception) {
            return webController.showMessage(model, principal, request, RecipeController.textError,
                    "Creating Image User.", exception.toString());
        }
        if (pair.getFirst() != DatabaseService.Errors.WITHOUT_ERRORS) {
            return webController.showMessage(model, principal, request, RecipeController.textError, "Creating User.",
                    pair.getFirst().name());

        }
        User user = userRepository.searchByUserName(nickSignIn);
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user.getUserName(),
                passSignIn);
        Authentication auth = userAuthentication.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
        return "redirect:" + CookingWithJavaApplication.getAppURL() + "/user-" + user.getId();
    }

    @GetMapping(value = { "/user-{userId}", "/profile-{userId}" })
    public String profileId(Model model, HttpServletRequest request, Principal principal, @PathVariable long userId) {
        User user = userService.searchById(userId);
        return returnProfile(model, principal, request, user);
    }

    @GetMapping(value = { "/user", "/profile", "/myUser", "/myProfile" })
    public String myProfile(Model model, HttpServletRequest request, Principal principal) {
        User user = activeUser(principal);
        if (user == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError, textSearchingUser,
                    textUserNotFound);
        }
        return returnProfile(model, principal, request, user);
    }

    public String returnProfile(Model model, Principal principal, HttpServletRequest request, User user) {
        if (user == null) {
            return webController.showMessage(model, principal, request, RecipeController.textError, textSearchingUser,
                    textUserNotFound);
        }
        model.addAttribute("num_upload_recipes", recipeService.createdRecipes(user.getId()).size());
        model.addAttribute("num_favorite_recipes", recipeService.favoriteRecipes(user).size());
        model.addAttribute("user", user);
        webController.addHeader(principal, request, model);
        return "profile";
    }

    public User activeUser(Principal principal) {
        if (principal == null) {
            return null;
        }
        return userRepository.searchByUserName(principal.getName());
    }
}