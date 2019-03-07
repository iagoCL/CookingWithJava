package com.TheJavaCooker.CookingWithJava.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    @GetMapping(value = {"/inicio", "/index", "/", "/error"})
    public String index(Model model) {
        return "index";
    }

    static String mostrarError(Model model, String error1, String error2, String error3) {
        model.addAttribute("mensaje1", error1);
        model.addAttribute("mensaje2", error2);
        model.addAttribute("mensaje3", error3);
        return "mensaje";
    }
}
