package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;

import com.TheJavaCooker.CookingWithJava.DataBase.Services.FavoritoService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class FavoritosController {
    @Autowired
    private UsuariosController usuariosController;
    @Autowired
    private RecetaService recetaService;
    @Autowired
    private FavoritoService favoritoService;
    @Autowired
    private WebController webController;


    @PostMapping(value = {"/formulario-favorito"})
    public String formularioFavorito(Model model,
                                     @RequestParam boolean favoritoMarcar,
                                     @RequestParam long favoritoRecetaId,
                                     Principal principal,
                                     HttpServletRequest request) {
        Usuario usuario = usuariosController.usuarioActivo(principal);
        if (usuario == null) {
            return webController.mostrarMensaje(model,principal,request, "ERROR:", "Marcando Favorito.", "El Usuario actual: no se ha encontrado.");
        }
        Receta receta = recetaService.buscarPorId(favoritoRecetaId);
        if (receta == null) {
            return webController.mostrarMensaje(model,principal, request, "ERROR:", "Marcando Favorito.", "La receta actual: no se ha encontrado.");
        }
        if (favoritoMarcar) {
            if (!favoritoService.marcarFavorito(usuario, receta)){
                webController.mostrarMensaje(model,principal,request,"Error:","Marcando Favorito.","El usuario actual ya la ha marcado como favorita");
            }
        } else {
            if (!favoritoService.quitarFavorito(usuario, receta)){
                webController.mostrarMensaje(model,principal,request,"Error:","Quitando Favorito.","El usuario actual no la ha marcado como favorita");
            }
        }
        return "redirect:/receta-" + favoritoRecetaId;
    }
}
