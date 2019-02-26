package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Repository.ImagendbRepository;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.RecetaRepository;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.UsuarioRepository;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    @Autowired
    private DatabaseService database;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RecetaRepository recetaRepository;
    @Autowired
    private ImagendbRepository imagenRepository;

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
