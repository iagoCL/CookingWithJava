package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.*;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecetaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class RecetaController {
    @Autowired
    private RecetaService recetaService;
    @Autowired
    private UsuariosController usuariosController;
    @Autowired
    private WebController webController;

    @GetMapping(value = {"/crearReceta", "/crear-receta", "/subir-receta", "/subirReceta"})
    public String crearReceta(Model model, Principal principal, HttpServletRequest request ) {
        webController.anadirHeader(principal, request, model);
        return "crearReceta";
    }

    @PostMapping(value = {"/formulario-crear-receta"})
    public String formularioCrearReceta(Model model,
                                        @RequestParam Map<String, String> allRequestParams,
                                        @RequestParam("imagenReceta") MultipartFile imagenReceta,
                                        Principal principal,
                                        HttpServletRequest request) {

        Usuario usuario = usuariosController.usuarioActivo(principal);
        if (usuario == null) {
            return webController.mostrarMensaje(model,principal,request, "ERROR:", "Creando Receta.", "No esta logueado.");
        }

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
        Pair<DatabaseService.Errores, Receta> pair = null;
        try {
            pair = recetaService.crearReceta(nombreDeLaReceta, tipoDePlato, nivelDificultadReceta, imagenReceta.getBytes(), listaDeIngredientes, listaDeUtensilios, listaDePasos, usuario);
        } catch (IOException e) {
            return webController.mostrarMensaje(model,principal,request, "ERROR:", "Cargando Imagen Receta.", e.toString());
        }
        if (pair.getFirst() != DatabaseService.Errores.SIN_ERRORES) {
            return webController.mostrarMensaje(model,principal,request, "ERROR:", "Creando Receta.", pair.getFirst().name());
        }
        return "redirect:https://127.0.0.1:8443/receta-" + pair.getSecond().getId();
    }


    @GetMapping(value = {"/receta-{recetaId}", "/receta-completa-{recetaId}", "/recetaCompleta-{recetaId}"})
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
        boolean recetaCreada = false;
        Usuario usuario = usuariosController.usuarioActivo(principal);
        if (usuario != null) {
            marcadaFavorita = receta.marcadaComoFavorita(usuario);
            recetaCreada = creadorReceta.getId() == usuario.getId();
        }
        model.addAttribute("esFavorita", marcadaFavorita);
        model.addAttribute("esCreador", recetaCreada);
        webController.anadirHeader(principal, request, model);
        return "receta-completa";
    }
}
