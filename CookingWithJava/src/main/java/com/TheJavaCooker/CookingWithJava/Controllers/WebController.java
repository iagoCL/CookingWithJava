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
    private final static String header1 =
                    "<header class=\"header-section\">\n" +
                    "    <div class=\"header-top\">\n" +
                    "        <div class=\"container\">\n" +
                    "            <div class=\"user-panel\">\n";
    private final static String header2 =
                    "            </div>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "    <div class=\"header-bottom\">\n" +
                    "        <div class=\"container\">\n" +
                    "            <a href=\"index\" class=\"site-logo\">\n" +
                    "                <img src=\"img/logo.png\" alt=\"\">\n" +
                    "            </a>\n" +
                    "            <ul class=\"main-menu\">\n" +
                    "                <li><a href=\"buscarReceta\"><i class=\"fa fa-search\"></i> Buscar</a></li>\n" +
                    "                <li><a href=\"index\"><i class=\"fa fa-home\"></i> Inicio</a></li>\n" +
                    "                <li><a href=\"recetas\"><i class=\"fa fa-book\"></i> Recetas</a></li>\n" +
                    "                <li><a href=\"";
    private final static String header3 =
            "\"><i class=\"fa fa-user\"></i> Tu perfil</a></li>\n" +
            "            </ul>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</header>\n";
    private final static String usuarioNoLogueado =
            "<a href=\"login\"><i class=\"fa fa-lock\"></i> Acceso</a>\n";
    private final static String usuarioLogueado1 =
            "    <a href=\"usuario\"><i class=\"fa fa-user\"></i> Usuario: ";
    private final static String usuarioLogueado2 = " </a>\n" +
            "    <a href=\"crearReceta\"><i class=\"fa fa-upload\"></i> Subir receta</a>\n" +
            "    <form action=\"logout\" method=\"post\" style=\" display: inline;\">\n" +
            "        <input type=\"hidden\" name=\"_csrf\" value=\"";
    private final static String usuarioLogueado3 = "\"/>\n" +
            "        <button class=\"logout-button\" type=\"submit\"><i class=\"fa fa-unlock\"></i> Logout</button>\n" +
            "    </form>\n";

    @Autowired
    private UsuariosController usuariosController;

    @GetMapping(value = {"/inicio", "/index", "/", "/error"})
    public String index(Model model,
                        Principal principal,
                        HttpServletRequest request) {
        anadirHeader(principal, request, model);
        return "index";
    }

    public String mostrarMensaje(Model model,
                                 Principal principal,
                                 HttpServletRequest request,
                                 String mensaje1,
                                 String mensaje2,
                                 String mensaje3) {
        anadirHeader(principal, request, model);
        model.addAttribute("mensaje1", mensaje1);
        model.addAttribute("mensaje2", mensaje2);
        model.addAttribute("mensaje3", mensaje3);
        return "mensaje";
    }

    public void anadirHeader(Principal principal, HttpServletRequest request, Model model) {
        Usuario usuario = usuariosController.usuarioActivo(principal);
        anadirHeader(usuario, request, model);
    }

    public void anadirHeader(Usuario usuario, HttpServletRequest request, Model model) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        String tokenString = token.getToken();
        model.addAttribute("token", tokenString);
        if (usuario != null) {
            model.addAttribute("headerHTML",
                    header1 + usuarioLogueado1 + usuario.getNombreUsuario() +
                            usuarioLogueado2 + tokenString + usuarioLogueado3 +
                            header2 + "usuario-" + usuario.getId() + header3);
        } else {
            model.addAttribute("headerHTML",
                    header1 + usuarioNoLogueado +
                            header2 + "login" + header3);
        }
    }
}
