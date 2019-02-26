package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;

import com.TheJavaCooker.CookingWithJava.DataBase.Services.FavoritoService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecetaService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FavoritosController {
    @Autowired
    private DatabaseService database;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RecetaService recetaService;
    @Autowired
    private FavoritoService favoritoService;


    @PostMapping(value = {"/formulario-favorito"})
    public String formularioFavorito(Model model,
                                     @RequestParam boolean favoritoMarcar,
                                     @RequestParam long favoritoRecetaId) {
        Usuario usuario = usuarioService.buscarPorId(UsuariosController.usuarioActivoId);
        if (usuario == null) {
            return WebController.mostrarError(model, "ERROR:", "Buscando Usuario.", "El Usuario actual: no se ha encontrado.");
        }
        Receta receta = recetaService.buscarPorId(favoritoRecetaId);
        if (receta == null) {
            return WebController.mostrarError(model, "ERROR:", "Buscando Receta.", "La receta actual: no se ha encontrado.");
        }
        if (favoritoMarcar) {
            favoritoService.marcarFavorito(usuario, receta);
        } else {
            favoritoService.quitarFavorito(usuario, receta);
        }
        return "redirect:/receta-" + favoritoRecetaId;
    }
}
