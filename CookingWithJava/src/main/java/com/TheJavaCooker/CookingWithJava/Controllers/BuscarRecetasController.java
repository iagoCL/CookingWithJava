package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;
import com.TheJavaCooker.CookingWithJava.DataBase.NivelDeDificultad;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecetaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class BuscarRecetasController {
    @Autowired
    private RecetaService recetaService;
    @Autowired
    private WebController webController;

    @GetMapping(value = {"/busqueda", "/buscarReceta", "buscar-receta"})
    public String buscar(Model model, HttpServletRequest request, Principal principal) {
        webController.anadirUsuarioActual(principal, request, model);
        return "busqueda";
    }

    @RequestMapping(value = {"/formulario-buscar-receta"}, method = RequestMethod.GET)
    public String formularioBuscarReceta(Model model,
                                         HttpServletRequest request,
                                         Principal principal,
                                         @RequestParam String nombreDeLaReceta,
                                         @RequestParam String tipoDePlato,
                                         @RequestParam String nivelDificultadReceta,
                                         @RequestParam Integer duracionRecetaMin,
                                         @RequestParam Integer duracionRecetaMax,
                                         @RequestParam Integer numPasosMax,
                                         @RequestParam Integer numPasosMin,
                                         @RequestParam Integer numFav,
                                         @RequestParam Integer numComentarios
    ) {
        NivelDeDificultad nivel_de_dificultad = NivelDeDificultad.fromString(nivelDificultadReceta);
        List<Receta> recetasBuscadas = recetaService.buscarReceta(
                0,
                15,
                duracionRecetaMax,
                duracionRecetaMin,
                numFav,
                numComentarios,
                numPasosMax,
                numPasosMin,
                tipoDePlato,
                nombreDeLaReceta,
                nivel_de_dificultad,
                null,
                null);
        model.addAttribute("recetas", recetasBuscadas);
        webController.anadirUsuarioActual(principal, request, model);
        return "recetas";
    }
}
