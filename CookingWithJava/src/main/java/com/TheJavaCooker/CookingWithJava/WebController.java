package com.TheJavaCooker.CookingWithJava;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    //todo session para almacenar usuario

    @GetMapping(value={"/busqueda","/buscarReceta","buscar-receta"})
    public String buscar(Model model) {
        //todo recibir parametros del formularios y gestionarlos por POST
        return "busqueda";
    }

    @GetMapping(value={"/crearReceta","/crear-receta","/subir-receta","/subirReceta"})
    public String crearReceta(Model model) {
        return "crearReceta";
    }

    @GetMapping(value={"/inicio","/index","/"})
    public String index(Model model) {
        //todo recibir parametros del formularios y gestionarlos por POST
        return "index";
    }

    @GetMapping(value={"/login","/register"})
    public String login(Model model) {
        //todo recibir parametros del formularios y gestionarlos por POST
        //todo almacenar sesion actual
        return "login";
    }

    @GetMapping(value={"/usuario","/perfil"})
    public String perfil(Model model) {
        //todo obtener perfil a mostrar por id obtenido por GET si no especifica mostar actual
        model.addAttribute("test","Juan");
        return "perfil";
    }


    @GetMapping(value={"/receta","/receta-completa","/recetaCompleta"})
    public String mostrarReceta(Model model) {
        //todo identificar receta usando parametro id (pasar por GET)
        return "receta-completa";
    }

    @GetMapping(value={"/recetas"})
    public String mostrarRecetas(Model model) {
        //todo recetas-favoritas muestra las recetas favoritas de un usuario pasado por GET
        //todo misRecetas-favoritas muestra las recetas favoritas del usuario en la sesion actual
        //todo analogo para creadas
        //todo recibir por get parametros de busqueda ejecutar la busqueda
        //todo si no recibe parametros mostrar las ultimas recetas.
        //todo hacer un metodo auxiliar para gestionar las opciones
        return "recetas";
    }
}
