package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.UsuarioRepository;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.UsuarioService;
import com.TheJavaCooker.CookingWithJava.UserRepositoryAuthenticationProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

@Controller
public class UsuariosController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UserRepositoryAuthenticationProvider userAuthentication;
    @Autowired
    private WebController webController;

    @GetMapping(value = {"/login", "/register"})
    public String login(Model model, Principal principal, HttpServletRequest request) {
        webController.anadirUsuarioActual(principal, request, model);
        return "login";

    }

    @GetMapping(value = {"/login-error", "/loginError"})
    public String loginErroneo(Model model,
                               Principal principal,
                               HttpServletRequest request) {
        return webController.mostrarMensaje(model, principal, request, "ERROR:", "Creando Usuario.", "Login: no valido");
    }

    @GetMapping(value = {"/logout-succes", "/logoutSucces"})
    public String logoutSucces(Model model,
                               Principal principal,
                               HttpServletRequest request) {
        return webController.mostrarMensaje(model, principal, request, "Mensaje:", "El usuario se ha desconectado.", "Logout correcto");
    }

    @PostMapping(value = {"/formulario-registro"})
    public String formularioRegistro(Model model,
                                     Principal principal,
                                     HttpServletRequest request,
                                     @RequestParam String nickRegistro,
                                     @RequestParam String contrasenaRegistro,
                                     @RequestParam String contrasena2Registro,
                                     @RequestParam String correoRegistro,
                                     @RequestParam String nombreRegistro,
                                     @RequestParam("imagenRegistro") MultipartFile imagenRegistro) {
        //todo comprobar si las dos contraseñas son iguales
        if (!contrasena2Registro.equals(contrasenaRegistro)) {
            return webController.mostrarMensaje(model, principal, request, "ERROR:", "Registrando Usuario.", "Las contraseñas no coinciden.");
        }
        Pair<DatabaseService.Errores, Usuario> pair = null;
        try {
            pair = usuarioService.crearUsuario(nickRegistro, contrasenaRegistro, correoRegistro, nombreRegistro, imagenRegistro.getBytes());
        } catch (IOException e) {
            return webController.mostrarMensaje(model, principal, request, "ERROR:", "Creando Imagen Usuario.", e.toString());
        }
        if (pair.getFirst() != DatabaseService.Errores.SIN_ERRORES) {
            return webController.mostrarMensaje(model, principal, request, "ERROR:", "Creando Usuario.", pair.getFirst().name());

        }
        Usuario usuario = usuarioRepository.buscarPorNombreUsuario(nickRegistro);
        UsernamePasswordAuthenticationToken authReq =
                new UsernamePasswordAuthenticationToken(usuario.getNombreUsuario(), contrasenaRegistro);
        Authentication auth = userAuthentication.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
        return "redirect:https://127.0.0.1:8443/usuario-" + usuario.getId();
    }

    @GetMapping(value = {"/usuario-{usuarioId}", "/perfil-{usuarioId}"})
    public String perfilId(Model model,
                           HttpServletRequest request,
                           Principal principal,
                           @PathVariable long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        return returnPerfil(model, principal, request, usuario);
    }

    @GetMapping(value = {"/usuario", "/perfil", "/miusuario", "/miperfil", "/miUsuario", "/miPerfil"})
    public String miPerfil(Model model,
                           HttpServletRequest request,
                           Principal principal) {
        Usuario usuario = usuarioActivo(principal);
        if (usuario == null) {
            return webController.mostrarMensaje(model, principal, request, "ERROR:", "Buscando Usuario.", "El Usuario actual: no se ha encontrado.");
        }
        return returnPerfil(model, principal, request, usuario);
    }

    public String returnPerfil(Model model,
                               Principal principal,
                               HttpServletRequest request,
                               Usuario usuario) {
        if (usuario == null) {
            return webController.mostrarMensaje(model, principal, request, "ERROR:", "Buscando Usuario.", "El Usuario: no se ha encontrado.");
        }
        model.addAttribute("num_recetas_subidas", usuario.getNumRecetasCreadas());
        model.addAttribute("num_recetas_favoritas", usuario.getNumRecetasFavoritas());
        model.addAttribute("usuario", usuario);
        model.addAttribute("recetasCreadas", usuario.getRecetasCreadas());
        model.addAttribute("recetasFavoritas", usuario.getRecetasFavoritas());
        webController.anadirUsuarioActual(principal, request, model);
        return "perfil";
    }

    public Usuario usuarioActivo(Principal principal) {
        if (principal == null) {
            return null;
        }
        return usuarioRepository.buscarPorNombreUsuario(principal.getName());
    }
}
