package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.RecetaRepository;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RecetasVariasController {
    @Autowired
    private RecetaRepository recetaRepository;
    @Autowired
    private UsuarioService usuarioService;


    @GetMapping(value = {"/recetas"})
    public String mostrarRecetas(Model model) {
        //todo si no recibe parametros mostrar las ultimas recetas.
        //todo hacer un metodo auxiliar para gestionar las opciones
        model.addAttribute("recetas", recetaRepository.findAll());
        return "recetas";
    }

    @GetMapping(value = {"/mis-recetas-favoritas"})
    public String mostrarRecetasFavoritas(Model model) {
        Usuario usuario = usuarioService.buscarPorId(UsuariosController.usuarioActivoId);
        model.addAttribute("recetas", usuario.getRecetasFavoritas());
        return "recetas";
    }

    @GetMapping(value = {"/recetas-favoritas-{usuarioId}"})
    public String mostrarRecetasFavoritas(Model model, @PathVariable long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        model.addAttribute("recetas", usuario.getRecetasFavoritas());
        return "recetas";
    }

    @GetMapping(value = {"/mis-recetas-creadas"})
    public String mostrarRecetasCreadas(Model model) {
        Usuario usuario = usuarioService.buscarPorId(UsuariosController.usuarioActivoId);
        model.addAttribute("recetas", usuario.getRecetasCreadas());
        return "recetas";
    }

    @GetMapping(value = {"/recetas-creadas-{usuarioId}"})
    public String mostrarRecetasCreadas(Model model, @PathVariable long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        model.addAttribute("recetas", usuario.getRecetasCreadas());
        return "recetas";
    }


}
