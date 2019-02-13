package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.DatabaseManager;
import com.TheJavaCooker.CookingWithJava.DataBase.Receta;
import com.TheJavaCooker.CookingWithJava.DataBase.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {
    @Autowired
    private DatabaseManager database;

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

    @RequestMapping(value = {"/userImage/{imageId}","/userImage/{imageId}.jpg"})
    @ResponseBody
    public byte[] getImageUsuario(@PathVariable long imageId)  {
        Usuario u = database.getUsuarioRepository().findById(imageId).orElse(null);
        if(u != null){
            return u.getImagenUsuario();
        }
        else return DatabaseRandomData.getRandomUserImage();
    }

    @RequestMapping(value = {"/recetaImage/{imageId}","/recetaImage/{imageId}.jpg"})
    @ResponseBody
    public byte[] getImageReceta(@PathVariable long imageId)  {
        Receta r = database.getRecetaRepository().findById(imageId).orElse(null);
        if(r != null){
            return r.getImagenReceta();
        }
        else return DatabaseRandomData.getRandomRecipeImage();
    }
}
