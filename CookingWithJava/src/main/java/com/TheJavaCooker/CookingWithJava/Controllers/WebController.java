package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class WebController {
    private final static String usuarioNoLogueado =
            "<div class=\"user-panel\">\n" +
                    "    <a href=\"login\"><i class=\"fa fa-lock\"></i> Acceso</a>\n" +
                    "</div>";
    private final static String usuarioLogueado1 =
            "<div class=\"user-panel\">\n" +
                    "    <a href=\"usuario\"><i class=\"fa fa-user\"></i> Usuario: ";
    private final static String usuarioLogueado2 = " </a>\n" +
            "    <a href=\"crearReceta\"><i class=\"fa fa-upload\"></i> Subir receta</a>\n" +
            "    <form action=\"logout\" method=\"post\" style=\" display: inline;\">\n" +
            "        <input type=\"hidden\" name=\"_csrf\" value=\"";
    private final static String usuarioLogueado3 = "\"/>\n" +
            "        <button class=\"logout-button\" type=\"submit\"><i class=\"fa fa-unlock\"></i> Logout</button>\n" +
            "    </form>\n" +
            "</div>";

    @Autowired
    private UsuariosController usuariosController;

    @GetMapping(value = {"/inicio", "/index", "/", "/error"})
    public String index(Model model,
                        Principal principal,
                        HttpServletRequest request) {
        anadirUsuarioActual(principal, request, model);
        return "index";
    }

    public String mostrarMensaje(Model model,
                                 Principal principal,
                                 HttpServletRequest request,
                                 String mensaje1,
                                 String mensaje2,
                                 String mensaje3) {
        anadirUsuarioActual(principal, request, model);
        model.addAttribute("mensaje1", mensaje1);
        model.addAttribute("mensaje2", mensaje2);
        model.addAttribute("mensaje3", mensaje3);
        return "mensaje";
    }

    public void anadirUsuarioActual(Principal principal, HttpServletRequest request, Model model) {
        Usuario usuario = usuariosController.usuarioActivo(principal);
        anadirUsuarioActual(usuario, request, model);
    }

    public void anadirUsuarioActual(Usuario usuario, HttpServletRequest request, Model model) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        String tokenString = token.getToken();
        model.addAttribute("token", tokenString);
        if (usuario != null) {
            model.addAttribute("usuarioActivo", usuarioLogueado1 + usuario.getNombreUsuario() + usuarioLogueado2 + tokenString+usuarioLogueado3);
        } else {
            model.addAttribute("usuarioActivo", usuarioNoLogueado);
        }
    }
}
