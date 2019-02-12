package com.TheJavaCooker.CookingWithJava.DataBase;

import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DatabaseManager {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RecetaRepository recetaRepository;
    @Autowired
    private IngredienteRepository ingredienteRepository;
    @Autowired
    private PasoRepository pasoRepository;
    @Autowired
    private UtensilioRepository utensilioRepository;
    @Autowired
    private ComentarioRepository comentarioRepository;
    private static final Sort sortOrder = new Sort(Sort.Direction.ASC, "numFavoritos");


    public enum Errores {
        SIN_ERRORES,
        NOMBRE_USUARIO_REPETIDO,
        CORREO_ELECTRONICO_REPETIDO,
        NOMBRE_USUARIO_NULO,
        CONTRASENA_NULA,
        CORREO_ELECTRONICO_NULO,
        NOMBRE_APELLIDOS_NULOS,
        NOMBRE_RECETA_NULO,
        NOMBRE_RECETA_REPETIDO,
        TIPO_PLATO_RECETA_NULO,
        NOMBRE_DE_INGREDIENTE_REPETIDO,
        NOMBRE_DE_INGREDIENTE_NULO,
        CANTIDAD_DE_INGREDIENTE_NULA,
        NOMBRE_DE_UTENSILIO_NULO,
        NOMBRE_DE_UTENSILIO_REPETIDO,
        DESCRIPCION_DE_PASO_NULA,
        TIEMPO_DE_PASO_INCORRECTO,
        DESCRIPCION_DE_COMENTARIO_NULA,
        TITULO_DE_COMENTARIO_NULO,
        COMENTARIO_REPETIDO,
        NUMERO_DE_PASO_INCORRECTO,
        NUMERO_DE_PASO_REPETIDO,
        ERRROR_DESCONOCIDO
    }

    public void clear(){
        //todo
    }

    public boolean marcarFavorito(Usuario usuario_, Receta receta_) {
        if (receta_.marcarFavorito(usuario_)) {
            receta_.recalcNumFavoritos();
            recetaRepository.save(receta_);
            return true;
        } else {
            return false;
        }
    }

    public boolean quitarFavorito(Usuario usuario_, Receta receta_) {
        if (receta_.quitarFavorito(usuario_)) {
            receta_.recalcNumFavoritos();
            recetaRepository.save(receta_);
            return true;
        } else {
            return false;
        }
    }

    private Errores comprobarIngredientes(List<Pair<String, String>> listaIngredientes_) {
        List<String> ingredientesActuales = new ArrayList<>();
        for (Pair<String, String> ingrediente : listaIngredientes_) {
            String nombreIngrediente = ingrediente.getFirst().toLowerCase();
            if (nombreIngrediente.isEmpty()) {
                PersonalDebug.imprimir("Nombre de ingrediente nulo: " + nombreIngrediente);
                return Errores.NOMBRE_DE_INGREDIENTE_NULO;
            } else if (ingrediente.getSecond().isEmpty()) {
                PersonalDebug.imprimir("Cantidad nula: " + ingrediente.getSecond());
                return Errores.CANTIDAD_DE_INGREDIENTE_NULA;
            } else if (ingredientesActuales.contains(nombreIngrediente)) {
                PersonalDebug.imprimir("Nombre de ingrediente repetido: " + nombreIngrediente);
                return Errores.NOMBRE_DE_INGREDIENTE_REPETIDO;
            } else {
                ingredientesActuales.add(nombreIngrediente);
            }
        }
        return Errores.SIN_ERRORES;
    }

    private Errores comprobarUtensilios(List<Pair<String, String>> listaUtensilios_) {
        List<String> utensiliosActuales = new ArrayList<>();
        for (Pair<String, String> utensilio : listaUtensilios_) {
            String nombreIngrediente = utensilio.getFirst().toLowerCase();
            if (nombreIngrediente.isEmpty()) {
                PersonalDebug.imprimir("Nombre de utensilio nulo: " + nombreIngrediente);
                return Errores.NOMBRE_DE_UTENSILIO_NULO;
            } else if (utensiliosActuales.contains(nombreIngrediente)) {
                PersonalDebug.imprimir("Nombre de utensilio repetido: " + nombreIngrediente);
                return Errores.NOMBRE_DE_UTENSILIO_REPETIDO;
            } else {
                utensiliosActuales.add(nombreIngrediente);
            }
        }
        return Errores.SIN_ERRORES;
    }

    private Errores comprobarPasos(List<Pair<Integer, String>> listaPasos_) {
        for (int i = 0, l = listaPasos_.size(); i < l; ++i) {
            Pair<Integer, String> paso = listaPasos_.get(i);
            if (paso.getSecond().isEmpty()) {
                PersonalDebug.imprimir("Descripcion de paso nula en :" + i);
                return Errores.DESCRIPCION_DE_PASO_NULA;
            } else if (paso.getFirst() < 0) {
                PersonalDebug.imprimir("Tiempo de paso nulo o negativo: " + paso.getFirst());
                return Errores.TIEMPO_DE_PASO_INCORRECTO;
            }
        }
        return Errores.SIN_ERRORES;
    }

    private Pair<Errores, Ingrediente> crearIngrediente(String nombreIngrediente_,
                                                        String cantidadIngrediente_,
                                                        Receta receta_) {
        Ingrediente ingrediente = new Ingrediente(nombreIngrediente_, cantidadIngrediente_, receta_);
        if (nombreIngrediente_.isEmpty()) {
            PersonalDebug.imprimir("Nombre de ingrediente nulo: " + nombreIngrediente_);
            return Pair.of(Errores.NOMBRE_DE_INGREDIENTE_NULO, ingrediente);
        } else if (cantidadIngrediente_.isEmpty()) {
            PersonalDebug.imprimir("Cantidad de ingrediente nula: " + cantidadIngrediente_);
            return Pair.of(Errores.CANTIDAD_DE_INGREDIENTE_NULA, ingrediente);
        } else if (receta_ == null) {
            PersonalDebug.imprimir("Receta nula");
            return Pair.of(Errores.ERRROR_DESCONOCIDO, ingrediente);
        } else {
            try {
                ingredienteRepository.save(ingrediente);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Ingrediente.constraintNombreIngrediente)) {
                    PersonalDebug.imprimir("Nombre de ingrediente Repetido: " + e.toString());
                    return Pair.of(Errores.NOMBRE_DE_INGREDIENTE_REPETIDO, ingrediente);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, ingrediente);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, ingrediente);
            }
            receta_.getIngredientes().add(ingrediente);
            return Pair.of(Errores.SIN_ERRORES, ingrediente);
        }
    }

    private Pair<Errores, Utensilio> crearUtensilio(String nombreUtensilio_,
                                                    String nivelDeDificultad_,
                                                    Receta receta_) {
        Utensilio utensilio = new Utensilio(nombreUtensilio_, NivelDeDificultad.fromString(nivelDeDificultad_), receta_);
        if (nombreUtensilio_.isEmpty()) {
            PersonalDebug.imprimir("Nombre de utensilio nulo: " + nombreUtensilio_);
            return Pair.of(Errores.NOMBRE_DE_UTENSILIO_NULO, utensilio);
        } else if (receta_ == null) {
            PersonalDebug.imprimir("Receta nula");
            return Pair.of(Errores.ERRROR_DESCONOCIDO, utensilio);
        } else {
            try {
                utensilioRepository.save(utensilio);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Utensilio.constraintNombreUtensilio)) {
                    PersonalDebug.imprimir("Nombre de utensilio Repetido: " + e.toString());
                    return Pair.of(Errores.NOMBRE_DE_UTENSILIO_REPETIDO, utensilio);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, utensilio);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, utensilio);
            }
            receta_.getUtensilios().add(utensilio);
            return Pair.of(Errores.SIN_ERRORES, utensilio);
        }
    }

    private Pair<Errores, Paso> crearPaso(int numeroPaso_,
                                          int tiempoEnMinutos_,
                                          String descripcionPaso_,
                                          Receta receta_) {
        Paso paso = new Paso(numeroPaso_, tiempoEnMinutos_, descripcionPaso_, receta_);
        if (numeroPaso_ < 1) {
            PersonalDebug.imprimir("Numero de paso nulo o negativo: " + numeroPaso_);
            return Pair.of(Errores.NUMERO_DE_PASO_INCORRECTO, paso);
        } else if (tiempoEnMinutos_ < 1) {
            PersonalDebug.imprimir("tiempo de paso nulo o negativo: " + tiempoEnMinutos_);
            return Pair.of(Errores.TIEMPO_DE_PASO_INCORRECTO, paso);
        } else if (descripcionPaso_.isEmpty()) {
            PersonalDebug.imprimir("Descripcion de paso nula: " + descripcionPaso_);
            return Pair.of(Errores.DESCRIPCION_DE_PASO_NULA, paso);
        } else if (receta_ == null) {
            PersonalDebug.imprimir("Receta nula");
            return Pair.of(Errores.ERRROR_DESCONOCIDO, paso);
        } else {
            try {
                pasoRepository.save(paso);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Paso.constraintNombrePaso)) {
                    PersonalDebug.imprimir("Numero de paso Repetido: " + e.toString());
                    return Pair.of(Errores.NUMERO_DE_PASO_REPETIDO, paso);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, paso);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, paso);
            }
            receta_.getPasos().add(paso);
            return Pair.of(Errores.SIN_ERRORES, paso);
        }
    }

    public Pair<Errores, Comentario> crearComentario(String descripcionComentario_,
                                                     String tituloComentario_,
                                                     Receta recetaId_,
                                                     Usuario usuarioId_) {
        return crearComentarioConFecha(descripcionComentario_, tituloComentario_,
                LocalDateTime.now(), recetaId_, usuarioId_);
    }

    public Pair<Errores, Comentario> crearComentarioConFecha(String descripcionComentario_,
                                                             String tituloComentario_,
                                                             LocalDateTime fechaComentario_,
                                                             Receta recetaId_,
                                                             Usuario usuarioId_) {
        Comentario comentario = new Comentario(descripcionComentario_,
                tituloComentario_, fechaComentario_, recetaId_, usuarioId_);
        if (descripcionComentario_.isEmpty()) {
            PersonalDebug.imprimir("Descripcion de comentario nula: " + descripcionComentario_);
            return Pair.of(Errores.DESCRIPCION_DE_COMENTARIO_NULA, comentario);
        } else if (tituloComentario_.isEmpty()) {
            PersonalDebug.imprimir("Titulo de comentario nulo: " + descripcionComentario_);
            return Pair.of(Errores.TITULO_DE_COMENTARIO_NULO, comentario);
        } else if (recetaId_ == null) {
            PersonalDebug.imprimir("Receta nula");
            return Pair.of(Errores.ERRROR_DESCONOCIDO, comentario);
        } else if (usuarioId_ == null) {
            PersonalDebug.imprimir("Usuario nulo");
            return Pair.of(Errores.ERRROR_DESCONOCIDO, comentario);
        } else {
            if (fechaComentario_ == null) {
                PersonalDebug.imprimir("WARNING: Fecha nula, poniendo la actual");
                comentario.resetFechaComentario();
            }
            try {
                comentarioRepository.save(comentario);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Comentario.constraintComentarioUnico)) {
                    PersonalDebug.imprimir("Comentario Repetido: " + e.toString());
                    return Pair.of(Errores.COMENTARIO_REPETIDO, comentario);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, comentario);
                }
            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, comentario);
            }
            recetaId_.getComentarios().add(comentario);
            recetaId_.recalcNumComentarios();
            recetaRepository.save(recetaId_);
            return Pair.of(Errores.SIN_ERRORES, comentario);
        }
    }


    public Pair<Errores, Usuario> crearUsuario(String nombreUsuario_,
                                               String contrasena_,
                                               String correo_,
                                               String nombreApellidos_) {
        return actualizarUsuario(new Usuario(nombreUsuario_, contrasena_, correo_, nombreApellidos_));
    }

    public Pair<Errores, Usuario> actualizarUsuario(Usuario usuario_) {
        if (usuario_.getNombreUsuario().isEmpty()) {
            PersonalDebug.imprimir("Nombre de usuario nulo: " + usuario_.getNombreUsuario());
            return Pair.of(Errores.NOMBRE_USUARIO_NULO, usuario_);
        } else if (usuario_.getContrasena().isEmpty()) {
            PersonalDebug.imprimir("Contraseña nula: " + usuario_.getContrasena());
            return Pair.of(Errores.CONTRASENA_NULA, usuario_);
        } else if (usuario_.getNombreApellidos().isEmpty()) {
            PersonalDebug.imprimir("Nombre y apellidos nulos: " + usuario_.getNombreApellidos());
            return Pair.of(Errores.NOMBRE_APELLIDOS_NULOS, usuario_);
        } else if (usuario_.getCorreoElectronico().isEmpty()) {
            PersonalDebug.imprimir("Correo electronico nulo: " + usuario_.getCorreoElectronico());
            return Pair.of(Errores.CORREO_ELECTRONICO_NULO, usuario_);
        } else {
            if (usuario_.getFechaCreacion() == null) {
                PersonalDebug.imprimir("WARNING: fecha de usuario nula: puesta la actual");
                usuario_.resetFechaCreacion();
            }
            try {
                usuarioRepository.save(usuario_);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Usuario.constraintNombreUsuario)) {
                    PersonalDebug.imprimir("Nombre Usuario Repetido: " + e.toString());
                    return Pair.of(Errores.NOMBRE_USUARIO_REPETIDO, usuario_);
                } else if (e.toString().contains(Usuario.constraintCorreoElectronico)) {
                    PersonalDebug.imprimir("Correo electronico repetido: " + e.toString());
                    return Pair.of(Errores.CORREO_ELECTRONICO_REPETIDO, usuario_);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, usuario_);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, usuario_);
            }
            return Pair.of(Errores.SIN_ERRORES, usuario_);
        }
    }

    public Pair<Errores, Receta> crearReceta(String nombreReceta_,
                                             String tipoDePlato,
                                             String nivelDeDificultad,
                                             List<Pair<String, String>> listaDeIngredientes_,
                                             List<Pair<String, String>> listaDeUtensilios_,
                                             List<Pair<Integer, String>> listaDePasos_,
                                             Usuario usuario_) {
        Receta nuevaReceta = new Receta(
                nombreReceta_,
                tipoDePlato,
                NivelDeDificultad.fromString(nivelDeDificultad),
                usuario_);
        if (listaDeIngredientes_.isEmpty()) {
            PersonalDebug.imprimir("WARNING: Creando una receta sin ingredientes.");
        } else {
            Errores errorIngredientes = comprobarIngredientes(listaDeIngredientes_);
            if (errorIngredientes != Errores.SIN_ERRORES) {
                PersonalDebug.imprimir("Fallo en los ingredientes de la receta.");
                return Pair.of(errorIngredientes, nuevaReceta);
            }
        }
        if ( listaDeUtensilios_.isEmpty()) {
            PersonalDebug.imprimir("WARNING: Creando una receta sin utensilios.");
        } else {
            Errores errorUtensilios = comprobarUtensilios(listaDeUtensilios_);
            if (errorUtensilios != Errores.SIN_ERRORES) {
                PersonalDebug.imprimir("Fallo en los utensilios de la receta.");
                return Pair.of(errorUtensilios, nuevaReceta);
            }
        }
        if ( listaDePasos_.isEmpty()) {
            PersonalDebug.imprimir("WARNING: Creando una receta sin pasos.");
        } else {
            Errores errorPasos = comprobarPasos(listaDePasos_);
            if (errorPasos != Errores.SIN_ERRORES) {
                PersonalDebug.imprimir("Fallo en los pasos de la receta.");
                return Pair.of(errorPasos, nuevaReceta);
            }
        }

        Pair<Errores, Receta> nuevoPairReceta = actualizarReceta(nuevaReceta);
        if (nuevoPairReceta.getFirst() == Errores.SIN_ERRORES) {
            for (Pair<String, String> ingredienteString : listaDeIngredientes_) {
                Errores errorIngredientes =
                        crearIngrediente(ingredienteString.getFirst(), ingredienteString.getSecond(), nuevaReceta).getFirst();
                if (errorIngredientes != Errores.SIN_ERRORES) {
                    PersonalDebug.imprimir("Fallo en los ingredientes de la receta.");
                    return Pair.of(errorIngredientes, nuevaReceta);
                }
            }
            for (Pair<String, String> utensilioString : listaDeUtensilios_) {
                Errores errorUtensilios =
                        crearUtensilio(utensilioString.getFirst(), utensilioString.getSecond(), nuevaReceta).getFirst();
                if (errorUtensilios != Errores.SIN_ERRORES) {
                    PersonalDebug.imprimir("Fallo en los utensilios de la receta.");
                    return Pair.of(errorUtensilios, nuevaReceta);
                }
            }
            for (int i = 0, l = listaDePasos_.size(); i < l; ) {
                Pair<Integer, String> pasoPair = listaDePasos_.get(i);
                Errores errorPasosos =
                        crearPaso(++i, pasoPair.getFirst(), pasoPair.getSecond(), nuevaReceta).getFirst();
                if (errorPasosos != Errores.SIN_ERRORES) {
                    PersonalDebug.imprimir("Fallo en los pasos de la receta.");
                    return Pair.of(errorPasosos, nuevaReceta);
                }
            }
            nuevaReceta.recalcNumPasos();
            recetaRepository.save(nuevaReceta);
        }
        return nuevoPairReceta;

    }

    public Pair<Errores, Receta> actualizarReceta(Receta receta_) {
        if (receta_.getNombreReceta().isEmpty()) {
            PersonalDebug.imprimir("Nombre de receta nulo: " + receta_.getNombreReceta());
            return Pair.of(Errores.NOMBRE_RECETA_NULO, receta_);
        } else if (receta_.getTipoPlato().isEmpty()) {
            PersonalDebug.imprimir("Tipo de plato nulo: " + receta_.getTipoPlato());
            return Pair.of(Errores.TIPO_PLATO_RECETA_NULO, receta_);
        } else {
            if (receta_.getFechaCreacion() == null) {
                PersonalDebug.imprimir("WARNING: fecha de receta nula: puesta la actual");
                receta_.resetFechaCreacion();
            }
            if (receta_.getNivelDificultad() == null) {
                PersonalDebug.imprimir("WARNING: nivel de dificultad nulo puesto a indefinido");
                receta_.setNivelDificultad(NivelDeDificultad.INDEFINIDO);
            }
            try {
                recetaRepository.save(receta_);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Receta.constraintNombreReceta)) {
                    PersonalDebug.imprimir("Nombre Receta Repetido: " + e.toString());
                    return Pair.of(Errores.NOMBRE_RECETA_REPETIDO, receta_);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, receta_);
                }
            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, receta_);
            }
            Set<Receta> recetasCreador = receta_.getCreadorDeLaReceta().getRecetasCreadas();
            if (!recetasCreador.contains(receta_)) {
                recetasCreador.add(receta_);
            }
            return Pair.of(Errores.SIN_ERRORES, receta_);
        }
    }

    public Iterable<Receta> buscarReceta(int indicePagina_,
                                         int elementosPagina_,
                                         Integer duracionMaxima_,
                                         Integer duracionMinima_,
                                         Integer numFavoritosMin_,
                                         Integer numComentariosMin_,
                                         Integer numPasosMax_,
                                         Integer numPasosMin_,
                                         String tipoDePlato_,
                                         String nombreDeReceta_,
                                         NivelDeDificultad nivelDeDificultad_,
                                         List<String> ingredientes_,
                                         List<String> utensilios_) {
        if(duracionMaxima_ == null) {
            duracionMaxima_ = Integer.MAX_VALUE;
        }
        if(duracionMinima_ == null) {
            duracionMinima_ = 0;
        }
        if(duracionMaxima_<duracionMinima_){
            return new ArrayList<>();
        }
        QReceta recetaDsl = QReceta.receta;
        BooleanExpression predicate =
                recetaDsl.duracionTotal.between(duracionMinima_,duracionMaxima_);
        if(numPasosMin_ != null && numPasosMax_ != null &&  numPasosMin_>numPasosMax_)
        {
            return new ArrayList<>();
        }
        if(numPasosMin_ != null && numPasosMin_>0) {
             predicate=predicate.and(recetaDsl.numPasos.goe(numPasosMin_));
        }
        if(numPasosMax_ != null) {
            predicate=predicate.and(recetaDsl.numPasos.loe(numPasosMax_));
        }
        if(numFavoritosMin_ != null && numFavoritosMin_>0) {
            predicate= predicate.and(recetaDsl.numFavoritos.goe(numFavoritosMin_));
        }
        if(numComentariosMin_ != null && numComentariosMin_>0) {
            predicate =predicate.and(recetaDsl.numComentarios.goe(numComentariosMin_));
        }
        if(tipoDePlato_ != null && !tipoDePlato_.isEmpty()) {
            predicate =predicate.and(recetaDsl.tipoPlato.like(tipoDePlato_.toLowerCase()));
        }
        if(nombreDeReceta_ != null && !nombreDeReceta_.isEmpty()) {
            predicate =predicate.and(recetaDsl.nombreReceta.likeIgnoreCase("%"+nombreDeReceta_+"%"));
        }
        if(nivelDeDificultad_ != null) {
            predicate =predicate.and(recetaDsl.nivelDificultad.eq(nivelDeDificultad_));
        }

        //todo busqueda por ingrediente y utensilio
        //todo fotos

        //todo paso a mysql

        return recetaRepository.findAll(predicate,PageRequest.of(indicePagina_, elementosPagina_,sortOrder));
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }

    public RecetaRepository getRecetaRepository() {
        return recetaRepository;
    }

    public IngredienteRepository getIngredienteRepository() {
        return ingredienteRepository;
    }

    public PasoRepository getPasoRepository() {
        return pasoRepository;
    }

    public UtensilioRepository getUtensilioRepository() {
        return utensilioRepository;
    }

    public ComentarioRepository getComentarioRepository() {
        return comentarioRepository;
    }
}
