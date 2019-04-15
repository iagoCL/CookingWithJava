package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.RecetaRepository;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class RecetasVariasController {
    @Autowired
    private RecetaRepository recetaRepository;
    @Autowired
    private UsuariosController usuariosController;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private WebController webController;


    @GetMapping(value = {"/recetas"})
    public String mostrarRecetas(Model model,
                                 Principal principal,
                                 HttpServletRequest request) {
        model.addAttribute("recetas", recetaRepository.findAll());
        webController.anadirHeader(principal, request, model);
        return "recetas";
    }

    @GetMapping(value = {"/mis-recetas-favoritas"})
    public String mostrarRecetasFavoritas(Model model,
                                          Principal principal,
                                          HttpServletRequest request
    ) {
        Usuario usuario = usuariosController.usuarioActivo(principal);
        if (usuario == null) {
            return webController.mostrarMensaje(model, principal, request, "ERROR:", "Mostrando recetas favoritas.", "El Usuario actual: no se ha encontrado.");
        }
        model.addAttribute("recetas", usuario.getRecetasFavoritas());
        webController.anadirHeader(usuario, request, model);
        return "recetas";
    }

    @GetMapping(value = {"/recetas-favoritas-{usuarioId}"})
    public String mostrarRecetasFavoritas(Model model,
                                          Principal principal,
                                          HttpServletRequest request,
                                          @PathVariable long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        model.addAttribute("recetas", usuario.getRecetasFavoritas());
        webController.anadirHeader(principal, request, model);
        return "recetas";
    }

    @GetMapping(value = {"/mis-recetas-creadas"})
    public String mostrarRecetasCreadas(Model model,
                                        Principal principal,
                                        HttpServletRequest request) {
        Usuario usuario = usuariosController.usuarioActivo(principal);
        if (usuario == null) {
            return webController.mostrarMensaje(model, principal, request, "ERROR:", "Mostrando recetas creadas.", "El Usuario actual: no se ha encontrado.");
        }
        model.addAttribute("recetas", usuario.getRecetasCreadas());
        webController.anadirHeader(usuario, request, model);
        return "recetas";
    }

    @GetMapping(value = {"/recetas-creadas-{usuarioId}"})
    public String mostrarRecetasCreadas(Model model,
                                        Principal principal,
                                        HttpServletRequest request,
                                        @PathVariable long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        model.addAttribute("recetas", usuario.getRecetasCreadas());
        webController.anadirHeader(principal, request, model);
        return "recetas";
    }


}
