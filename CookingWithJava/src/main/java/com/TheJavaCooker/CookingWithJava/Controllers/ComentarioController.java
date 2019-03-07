package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Comentario;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.ComentarioService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecetaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class ComentarioController {
    @Autowired
    private RecetaService recetaService;
    @Autowired
    private UsuariosController usuariosController;
    @Autowired
    private ComentarioService comentarioService;

    @RequestMapping(value = {"/formulario-comentario"}, method = RequestMethod.POST)
    public String login(Model model,
                        @RequestParam String commentSubject,
                        @RequestParam String commentMessage,
                        @RequestParam long commentRecetaId,
                        Principal principal) {
        Usuario usuario = usuariosController.usuarioActivo(principal);
        if (usuario == null) {
            return WebController.mostrarError(model, "ERROR:", "Al poner comentario.", "No esta logueado.");
        }
        Receta receta = recetaService.buscarPorId(commentRecetaId);
        if (receta == null) {
            return WebController.mostrarError(model, "ERROR:", "Al poner comentario.", "No exite la receta enviada.");
        }
        Pair<DatabaseService.Errores, Comentario> pair = comentarioService.crearComentario(commentMessage, commentSubject, receta, usuario);
        if (pair.getFirst() != DatabaseService.Errores.SIN_ERRORES) {
            return WebController.mostrarError(model, "ERROR:", "Al poner comentario.", pair.getFirst().name());
        }
        return "redirect:/receta-" + commentRecetaId;
    }

}
