package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {
    @Autowired
    private DatabaseManager database;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RecetaRepository recetaRepository;
    @Autowired
    private ImagendbRepository imagenRepository;

    //todo session para almacenar usuario
    private long usuarioActivoId = -1;
    private List<Receta> recetasBuscadas;

    @GetMapping(value = {"/busqueda", "/buscarReceta", "buscar-receta"})
    public String buscar(Model model) {
        return "busqueda";
    }

    @RequestMapping(value = {"/formulario-buscar-receta"}, method = RequestMethod.GET)
    public String formularioBuscarReceta(Model model,
                                         @RequestParam String nombreDeLaReceta,
                                         @RequestParam String tipoDePlato,
                                         @RequestParam String nivelDificultadReceta,
                                         @RequestParam Integer duracionRecetaMin,
                                         @RequestParam Integer duracionRecetaMax,
                                         @RequestParam Integer numPasosMax,
                                         @RequestParam Integer numPasosMin,
                                         @RequestParam Integer numFav,
                                         @RequestParam Integer numComentarios
    ) {
        model.addAttribute("nombreDeLaReceta", nombreDeLaReceta);
        model.addAttribute("tipoDePlato", tipoDePlato);
        model.addAttribute("nivelDificultadReceta", nivelDificultadReceta);
        NivelDeDificultad nivel_de_dificultad = NivelDeDificultad.fromString(nivelDificultadReceta);
        recetasBuscadas = database.buscarReceta(0, 15, duracionRecetaMax, duracionRecetaMin, numFav, numComentarios, numPasosMax, numPasosMin, tipoDePlato, nombreDeLaReceta, nivel_de_dificultad, null, null);
        return "/recetas-buscadas";
    }

    @GetMapping(value = {"/crearReceta", "/crear-receta", "/subir-receta", "/subirReceta"})
    public String crearReceta(Model model) {
        return "crearReceta";
    }

    @PostMapping(value = {"/formulario-crear-receta"})
    public String formularioCrearReceta(Model model,
                                        @RequestParam Map<String, String> allRequestParams,
                                        @RequestParam("imagenReceta") MultipartFile imagenReceta) {
        String nombreDeLaReceta = allRequestParams.get("nombreDeLaReceta");
        String tipoDePlato = allRequestParams.get("tipoDePlato");
        String nivelDificultadReceta = allRequestParams.get("nivelDificultadReceta");
        List<Pair<String, String>> listaDeIngredientes = new ArrayList<>();
        List<Pair<String, String>> listaDeUtensilios = new ArrayList<>();
        List<Pair<Integer, String>> listaDePasos = new ArrayList<>();
        Usuario usuario = usuarioRepository.findById(usuarioActivoId).orElse(null);
        if (usuario == null) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Creando Receta.");
            model.addAttribute("mensaje3", "No esta logueado.");
            return "mensaje";
        }
        Pair<DatabaseManager.Errores, Receta> pair = null;
        try {
            pair = database.crearReceta(nombreDeLaReceta, tipoDePlato, nivelDificultadReceta, imagenReceta.getBytes(), listaDeIngredientes, listaDeUtensilios, listaDePasos, usuario);
        } catch (IOException e) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Cargando Imagen Receta.:");
            model.addAttribute("mensaje3", e.toString());
            return "mensaje";
        }
        if (pair.getFirst() != DatabaseManager.Errores.SIN_ERRORES) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Creando Receta.");
            model.addAttribute("mensaje3", pair.getFirst().name());
            return "mensaje";
        }
        return "redirect:/receta-" + pair.getSecond().getId();
    }

    @GetMapping(value = {"/inicio", "/index", "/"})
    public String index(Model model) {
        return "index";
    }

    @GetMapping(value = {"/login", "/register"})
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value = {"/formulario-comentario"}, method = RequestMethod.POST)
    public String login(Model model, @RequestParam String commentSubject, @RequestParam String commentMessage, @RequestParam long commentRecetaId) {
        model.addAttribute("commentSubject", commentSubject);
        model.addAttribute("commentMessage", commentMessage);
        model.addAttribute("commentRecetaId", commentRecetaId);
        Usuario usuario = usuarioRepository.findById(usuarioActivoId).orElse(null);
        if (usuario == null) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Al poner comentario.");
            model.addAttribute("mensaje3", "No esta logueado.");
            return "mensaje";
        }
        Receta receta = recetaRepository.findById(commentRecetaId).orElse(null);
        if (receta == null) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Al poner comentario.");
            model.addAttribute("mensaje3", "No exite la receta enviada.");
            return "mensaje";
        }
        Pair<DatabaseManager.Errores, Comentario> pair = database.crearComentario(commentMessage, commentSubject, receta, usuario);
        if (pair.getFirst() != DatabaseManager.Errores.SIN_ERRORES) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Creando Comentario.");
            model.addAttribute("mensaje3", pair.getFirst().name());
            return "mensaje";
        }
        return "redirect:/receta-" + commentRecetaId;
    }

    @PostMapping(value = {"/formulario-login"})
    public String formularioLogin(Model model, @RequestParam String nickLogin, @RequestParam String contrasenaLogin) {
        //todo cambiar a POST
        model.addAttribute("nickLogin", nickLogin);
        model.addAttribute("contrasenaLogin", contrasenaLogin);
        Usuario usuario = usuarioRepository.loginValido(nickLogin, contrasenaLogin);
        if (usuario == null) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Creando Usuario.:");
            model.addAttribute("mensaje3", "Login: no valido");
            return "mensaje";
        } else {
            usuarioActivoId = usuario.getId();
            return "redirect:/perfil-" + usuario.getId();
        }
    }

    @PostMapping(value = {"/formulario-registro"})
    public String formularioRegistro(Model model,
                                     @RequestParam String nickRegistro,
                                     @RequestParam String contrasenaRegistro,
                                     @RequestParam String correoRegistro,
                                     @RequestParam String nombreRegistro,
                                     @RequestParam("imagenRegistro") MultipartFile imagenRegistro) {
        //todo comprobar si las dos contraseñas son iguales
        model.addAttribute("nickRegistro", nickRegistro);
        model.addAttribute("contrasenaRegistro", contrasenaRegistro);
        model.addAttribute("correoRegistro", correoRegistro);
        model.addAttribute("nombreRegistro", nombreRegistro);
        Pair<DatabaseManager.Errores, Usuario> pair = null;
        try {
            pair = database.crearUsuario(nickRegistro, contrasenaRegistro, correoRegistro, nombreRegistro, imagenRegistro.getBytes());
        } catch (IOException e) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Cargando Imagen Usuario.:");
            model.addAttribute("mensaje3", e.toString());
            return "mensaje";
        }
        if (pair.getFirst() != DatabaseManager.Errores.SIN_ERRORES) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Creando Usuario.:");
            model.addAttribute("mensaje3", pair.getFirst().name());
            return "mensaje";
        }
        Usuario usuario = usuarioRepository.buscarPorNombreUsuario(nickRegistro);
        usuarioActivoId = usuario.getId();
        return "redirect:/usuario-" + usuarioActivoId;
    }

    @GetMapping(value = {"/error"})
    public String error(Model model) {
        return "index";
    }

    @GetMapping(value = {"/usuario-{usuarioId}", "/perfil-{usuarioId}"})
    public String perfilId(Model model, @PathVariable long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        return returnPerfil(model, usuario);
    }

    @GetMapping(value = {"/usuario", "/perfil", "/miusuario", "/miperfil", "/miUsuario", "/miPerfil"})
    public String miPerfil(Model model) {
        Usuario usuario = usuarioRepository.findById(usuarioActivoId).orElse(null);
        if (usuario == null) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Buscando Usuario.:");
            model.addAttribute("mensaje3", "El Usuario actual: no se ha encontrado.");
            return "mensaje";
        }
        return returnPerfil(model, usuario);
    }

    public String returnPerfil(Model model, Usuario usuario) {
        if (usuario == null) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Buscando Usuario.:");
            model.addAttribute("mensaje3", "El Usuario: no se ha encontrado.");
            return "mensaje";
        }
        model.addAttribute("num_recetas_subidas", usuario.getNumRecetasCreadas());
        model.addAttribute("num_recetas_favoritas", usuario.getNumRecetasFavoritas());
        model.addAttribute("usuario", usuario);
        model.addAttribute("recetasCreadas", usuario.getRecetasCreadas());
        model.addAttribute("recetasFavoritas", usuario.getRecetasFavoritas());
        model.addAttribute("recetas", new ArrayList<Receta>());
        return "perfil";
    }

    @GetMapping(value = {"/receta-{recetaId}", "/receta-completa-{recetaId}", "/recetaCompleta-{recetaId}"})
    public String mostrarReceta(Model model, @PathVariable long recetaId) {
        Receta receta = recetaRepository.findById(recetaId).orElse(null);
        if (receta == null) {
            model.addAttribute("mensaje1", "ERROR:");
            model.addAttribute("mensaje2", "Buscando Receta.:");
            model.addAttribute("mensaje3", "La receta: " + recetaId + " no se ha encontrado.");
            return "mensaje";
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
        return "receta-completa";
    }

    @GetMapping(value = {"/recetas"})
    public String mostrarRecetas(Model model) {
        //todo si no recibe parametros mostrar las ultimas recetas.
        //todo hacer un metodo auxiliar para gestionar las opciones
        model.addAttribute("recetas", recetaRepository.findAll());
        return "recetas";
    }

    @GetMapping(value = {"/mis-recetas-favoritas"})
    public String mostrarRecetasFavoritas(Model model) {
        Usuario usuario = usuarioRepository.findById(usuarioActivoId).orElse(null);
        model.addAttribute("recetas", usuario.getRecetasFavoritas());
        return "recetas";
    }

    @GetMapping(value = {"/recetas-favoritas-{usuarioId}"})
    public String mostrarRecetasFavoritas(Model model, @PathVariable long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        model.addAttribute("recetas", usuario.getRecetasFavoritas());
        return "recetas";
    }

    @GetMapping(value = {"/mis-recetas-creadas"})
    public String mostrarRecetasCreadas(Model model) {
        Usuario usuario = usuarioRepository.findById(usuarioActivoId).orElse(null);
        model.addAttribute("recetas", usuario.getRecetasCreadas());
        return "recetas";
    }

    @GetMapping(value = {"/recetas-creadas-{usuarioId}"})
    public String mostrarRecetasCreadas(Model model, @PathVariable long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        model.addAttribute("recetas", usuario.getRecetasCreadas());
        return "recetas";
    }

    @GetMapping(value = {"/recetas-buscadas"})
    public String mostrarRecetasBuscadas(Model model) {
        model.addAttribute("recetas", recetasBuscadas);
        return "recetas";
    }

    @RequestMapping(value = {"/image/{imageId}", "/image/{imageId}.jpg"})
    @ResponseBody
    public byte[] getImageUsuario(@PathVariable long imageId) {
        Imagendb u = imagenRepository.findById(imageId).orElse(null);
        if (u != null) {
            return u.getImagen();
        } else
            return DatabaseRandomData.getRandomRecipeImage();
    }
}
