package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.CookingWithJavaApplication;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import com.TheJavaCooker.CookingWithJava.DataBase.NivelDeDificultad;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Controller
public class editarRecetaController {
    @Autowired
    private RecetaService recetaService;
    @Autowired
    private UsuariosController usuariosController;
    @Autowired
    private WebController webController;

    @PostMapping(value = {"/formulario-editar-receta"})
    public String formularioEditarReceta(Model model,
                                        @RequestParam Map<String, String> allRequestParams,
                                        Principal principal,
                                        HttpServletRequest request) {

        Usuario usuario = usuariosController.usuarioActivo(principal);
        if (usuario == null) {
            return webController.mostrarMensaje(model,principal,request, "ERROR:", "Editando la Receta.", "No esta logueado.");
        }
        else if(usuario.getId() != usuario.getId())
        {
            return webController.mostrarMensaje(model,principal,request, "ERROR:", "Editando la Receta.", "No es el creador.");
        }

        String nombreDeLaReceta = allRequestParams.get("nombreDeLaReceta");
        String tipoDePlato = allRequestParams.get("tipoDePlato");
        String nivelDificultadReceta = allRequestParams.get("nivelDificultadReceta");
        int recetaId = Integer.parseInt(allRequestParams.get("recetaId"));
        Receta receta = recetaService.buscarPorId(recetaId);
        receta.setNivelDificultad(NivelDeDificultad.fromString(nivelDificultadReceta));
        receta.setTipoPlato(tipoDePlato);
        receta.setNombreReceta(nombreDeLaReceta);
        Pair<DatabaseService.Errores, Receta> pair = null;
        try {
            pair = recetaService.actualizarReceta(receta);
        } catch (Exception e) {
            return webController.mostrarMensaje(model,principal,request, "ERROR:", "Cargando Imagen Receta.", e.toString());
        }
        if (pair.getFirst() != DatabaseService.Errores.SIN_ERRORES) {
            return webController.mostrarMensaje(model,principal,request, "ERROR:", "Editando Receta.", pair.getFirst().name());
        }
        return "redirect:"+ CookingWithJavaApplication.getAppURL()+"/receta-" + pair.getSecond().getId();
    }


    @GetMapping(value = {"/editarReceta-{recetaId}"})
    public String mostrarReceta(Model model,
                                @PathVariable long recetaId,
                                HttpServletRequest request,
                                Principal principal) {

        Receta receta = recetaService.buscarPorId(recetaId);
        if (receta == null) {
            return webController.mostrarMensaje(model,principal,request, "ERROR:", "Buscando Receta.", "La receta: " + recetaId + " no se ha encontrado.");
        }
        model.addAttribute("receta", receta);
        model.addAttribute("tipoDePlato", receta.getTipoPlato());
        boolean marcadaFavorita = false;
        Usuario usuario = usuariosController.usuarioActivo(principal);
        webController.anadirHeader(principal, request, model);
        return "editarReceta";
    }
}
