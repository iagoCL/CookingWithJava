package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecetaService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class RecetaController {
    @Autowired
    private RecetaService recetaService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = {"/crearReceta", "/crear-receta", "/subir-receta", "/subirReceta"})
    public String crearReceta(Model model, HttpServletRequest request ) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        return "crearReceta";
    }

    @PostMapping(value = {"/formulario-crear-receta"})
    public String formularioCrearReceta(Model model,
                                        @RequestParam Map<String, String> allRequestParams,
                                        @RequestParam("imagenReceta") MultipartFile imagenReceta) {
        String nombreDeLaReceta = allRequestParams.get("nombreDeLaReceta");
        String tipoDePlato = allRequestParams.get("tipoDePlato");
        String nivelDificultadReceta = allRequestParams.get("nivelDificultadReceta");
        Integer numPasos = Integer.parseInt(allRequestParams.get("numPasos"));
        Integer numIngredientes = Integer.parseInt(allRequestParams.get("numIngredientes"));
        Integer numUtensilios = Integer.parseInt(allRequestParams.get("numUtensilios"));
        List<Pair<String, String>> listaDeIngredientes = new ArrayList<>(numIngredientes);
        List<Pair<String, String>> listaDeUtensilios = new ArrayList<>(numUtensilios);
        List<Pair<Integer, String>> listaDePasos = new ArrayList<>(numPasos);
        for (int i = 1; i <= numPasos; ++i) {
            Integer duracion = Integer.parseInt(allRequestParams.get("paso-" + i + "Duracion"));
            String descripcion = allRequestParams.get("paso-" + i + "Descripcion");
            listaDePasos.add(Pair.of(duracion, descripcion));
        }
        for (int i = 1; i <= numIngredientes; ++i) {
            String cantidad = allRequestParams.get("ingrediente-" + i + "Cantidad");
            String nombre = allRequestParams.get("ingrediente-" + i + "Name");
            listaDeIngredientes.add(Pair.of(nombre, cantidad));
        }
        for (int i = 1; i <= numUtensilios; ++i) {
            String dificultad = allRequestParams.get("utensilio-" + i + "Nivel");
            String nombre = allRequestParams.get("utensilio-" + i + "Name");
            listaDeUtensilios.add(Pair.of(nombre, dificultad));
        }


        Usuario usuario = usuarioService.buscarPorId(UsuariosController.usuarioActivoId);
        if (usuario == null) {
            return WebController.mostrarError(model, "ERROR:", "Creando Receta.", "No esta logueado.");
        }
        Pair<DatabaseService.Errores, Receta> pair = null;
        try {
            pair = recetaService.crearReceta(nombreDeLaReceta, tipoDePlato, nivelDificultadReceta, imagenReceta.getBytes(), listaDeIngredientes, listaDeUtensilios, listaDePasos, usuario);
        } catch (IOException e) {
            return WebController.mostrarError(model, "ERROR:", "Cargando Imagen Receta.", e.toString());
        }
        if (pair.getFirst() != DatabaseService.Errores.SIN_ERRORES) {
            return WebController.mostrarError(model, "ERROR:", "Creando Receta.", pair.getFirst().name());
        }
        return "redirect:/receta-" + pair.getSecond().getId();
    }


    @GetMapping(value = {"/receta-{recetaId}", "/receta-completa-{recetaId}", "/recetaCompleta-{recetaId}"})
    public String mostrarReceta(Model model, @PathVariable long recetaId, HttpServletRequest request) {
        Receta receta = recetaService.buscarPorId(recetaId);
        if (receta == null) {
            return WebController.mostrarError(model, "ERROR:", "Buscando Receta.", "La receta: " + recetaId + " no se ha encontrado.");
        }
        model.addAttribute("receta", receta);
        model.addAttribute("tipoDePlato", receta.getTipoPlato());
        model.addAttribute("favoritos", receta.getNumFavoritos());
        model.addAttribute("fechaDeCreacion", receta.getStringFechaCreacion());
        model.addAttribute("duracionTotal", receta.getStringDuracionTotal());
        Usuario creadorReceta = receta.getCreadorDeLaReceta();
        model.addAttribute("creadorRecetaNombre", creadorReceta.getNombreUsuario());
        model.addAttribute("pasos", receta.getPasos());
        model.addAttribute("comentarios", receta.getComentarios());
        model.addAttribute("utensilios", receta.getUtensilios());
        model.addAttribute("ingredientes", receta.getIngredientes());
        boolean marcadaFavorita = false;
        Usuario usuario = usuarioService.buscarPorId(UsuariosController.usuarioActivoId);
        if (usuario != null) {
            marcadaFavorita = receta.marcadaComoFavorita(usuario);
        }
        model.addAttribute("esFavorita", marcadaFavorita);
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());

        return "receta-completa";
    }
}