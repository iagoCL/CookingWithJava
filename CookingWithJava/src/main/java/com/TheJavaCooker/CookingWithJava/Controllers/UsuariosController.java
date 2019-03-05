package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.UsuarioRepository;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class UsuariosController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;

    //todo session para almacenar usuario
    public static long usuarioActivoId = -1;

    @GetMapping(value = {"/login", "/register"})
    public String login(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        return "login";

    }

    @PostMapping(value = {"/formulario-login"})
    public String formularioLogin(Model model,
                                  @RequestParam String nickLogin,
                                  @RequestParam String contrasenaLogin) {
        Usuario usuario = usuarioRepository.buscarPorNombreUsuario(nickLogin);
        if (usuario == null) {
            return loginErroneo(model);
        } else {
            if (usuario.getContrasena().equals(contrasenaLogin)) {
                usuarioActivoId = usuario.getId();
                return "redirect:/perfil-" + usuario.getId();
            } else {
                return loginErroneo(model);
            }
        }
    }

    @GetMapping(value = {"/login-error", "/loginError"})
    public String loginErroneo(Model model) {
        return WebController.mostrarError(model, "ERROR:", "Creando Usuario.", "Login: no valido");
    }

    @PostMapping(value = {"/formulario-registro"})
    public String formularioRegistro(Model model,
                                     @RequestParam String nickRegistro,
                                     @RequestParam String contrasenaRegistro,
                                     @RequestParam String contrasena2Registro,
                                     @RequestParam String correoRegistro,
                                     @RequestParam String nombreRegistro,
                                     @RequestParam("imagenRegistro") MultipartFile imagenRegistro) {
        //todo comprobar si las dos contraseñas son iguales
        if (!contrasena2Registro.equals(contrasenaRegistro)) {
            return WebController.mostrarError(model, "ERROR:", "Registrando Usuario.", "Las contraseñas no coinciden.");
        }
        Pair<DatabaseService.Errores, Usuario> pair = null;
        try {
            pair = usuarioService.crearUsuario(nickRegistro, contrasenaRegistro, correoRegistro, nombreRegistro, imagenRegistro.getBytes());
        } catch (IOException e) {
            return WebController.mostrarError(model, "ERROR:", "Creando Imagen Usuario.", e.toString());
        }
        if (pair.getFirst() != DatabaseService.Errores.SIN_ERRORES) {
            return WebController.mostrarError(model, "ERROR:", "Creando Usuario.", pair.getFirst().name());

        }
        Usuario usuario = usuarioRepository.buscarPorNombreUsuario(nickRegistro);
        usuarioActivoId = usuario.getId();
        return "redirect:/usuario-" + usuarioActivoId;
    }

    @GetMapping(value = {"/usuario-{usuarioId}", "/perfil-{usuarioId}"})
    public String perfilId(Model model, @PathVariable long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        return returnPerfil(model, usuario);
    }

    @GetMapping(value = {"/usuario", "/perfil", "/miusuario", "/miperfil", "/miUsuario", "/miPerfil"})
    public String miPerfil(Model model) {
        Usuario usuario = usuarioService.buscarPorId(usuarioActivoId);
        if (usuario == null) {
            return WebController.mostrarError(model, "ERROR:", "Buscando Usuario.", "El Usuario actual: no se ha encontrado.");
        }
        return returnPerfil(model, usuario);
    }

    public String returnPerfil(Model model, Usuario usuario) {
        if (usuario == null) {
            return WebController.mostrarError(model, "ERROR:", "Buscando Usuario.", "El Usuario: no se ha encontrado.");
        }
        model.addAttribute("num_recetas_subidas", usuario.getNumRecetasCreadas());
        model.addAttribute("num_recetas_favoritas", usuario.getNumRecetasFavoritas());
        model.addAttribute("usuario", usuario);
        model.addAttribute("recetasCreadas", usuario.getRecetasCreadas());
        model.addAttribute("recetasFavoritas", usuario.getRecetasFavoritas());
        return "perfil";
    }
}
