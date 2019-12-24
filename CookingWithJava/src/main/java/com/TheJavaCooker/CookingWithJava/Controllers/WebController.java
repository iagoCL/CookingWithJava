package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class WebController {
        private final static String header1 = "<header class=\"header-section\">\n" + "    <div class=\"header-top\">\n"
                        + "        <div class=\"container\">\n" + "            <div class=\"user-panel\">\n";
        private final static String header2 = "            </div>\n" + "        </div>\n" + "    </div>\n"
                        + "    <div class=\"header-bottom\">\n" + "        <div class=\"container\">\n"
                        + "            <a href=\"index\" class=\"site-logo\">\n"
                        + "                <img src=\"img/logo.png\" alt=\"\">\n" + "            </a>\n"
                        + "            <ul class=\"main-menu\">\n"
                        + "                <li><a href=\"searchRecipe\"><i class=\"fa fa-search\"></i> Search</a></li>\n"
                        + "                <li><a href=\"index\"><i class=\"fa fa-home\"></i> Start</a></li>\n"
                        + "                <li><a href=\"recipes\"><i class=\"fa fa-book\"></i> Recipes</a></li>\n"
                        + "                <li><a href=\"";
        private final static String header3 = "\"><i class=\"fa fa-user\"></i> Profile</a></li>\n"
                        + "            </ul>\n" + "        </div>\n" + "    </div>\n" + "</header>\n";
        private final static String userNotLogged = "<a href=\"login\"><i class=\"fa fa-lock\"></i> Access</a>\n";
        private final static String userLogged1 = "    <a href=\"user\"><i class=\"fa fa-user\"></i> User: ";
        private final static String userLogged2 = " </a>\n"
                        + "    <a href=\"createRecipe\"><i class=\"fa fa-upload\"></i> Upload recipe</a>\n"
                        + "    <form action=\"logout\" method=\"post\" style=\" display: inline;\">\n"
                        + "        <input type=\"hidden\" name=\"_csrf\" value=\"";
        private final static String userLogged3 = "\"/>\n"
                        + "        <button class=\"logout-button\" type=\"submit\"><i class=\"fa fa-unlock\"></i> Logout</button>\n"
                        + "    </form>\n";

        @Autowired
        private UserController userController;

        @GetMapping(value = { "/Start", "/index", "/", "/error" })
        public String index(Model model, Principal principal, HttpServletRequest request) {
                addHeader(principal, request, model);
                return "index";
        }

        public String showMessage(Model model, Principal principal, HttpServletRequest request, String message1,
                        String message2, String message3) {
                addHeader(principal, request, model);
                model.addAttribute("message1", message1);
                model.addAttribute("message2", message2);
                model.addAttribute("message3", message3);
                return "message";
        }

        public void addHeader(Principal principal, HttpServletRequest request, Model model) {
                User user = userController.activeUser(principal);
                addHeader(user, request, model);
        }

        public void addHeader(User user, HttpServletRequest request, Model model) {
                CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
                String tokenString = token.getToken();
                model.addAttribute("token", tokenString);
                if (user != null) {
                        model.addAttribute("headerHTML", header1 + userLogged1 + user.getUserName() + userLogged2
                                        + tokenString + userLogged3 + header2 + "user-" + user.getId() + header3);
                } else {
                        model.addAttribute("headerHTML", header1 + userNotLogged + header2 + "login" + header3);
                }
        }
}