package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.*;
import com.TheJavaCooker.CookingWithJava.DataBase.NivelDeDificultad;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.QReceta;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.*;
import com.TheJavaCooker.CookingWithJava.DataBase.TipoDeImagen;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import com.querydsl.core.types.dsl.BooleanExpression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@CacheConfig(cacheNames="recetasCache")
public class RecetaService {
    @Autowired
    private RecetaRepository recetaRepository;
    @Autowired
    private IngredienteService ingredienteService;
    @Autowired
    private PasoService pasoService;
    @Autowired
    private UtensilioService utensilioService;
    @Autowired
    private ImagenService imagenService;

    @CacheEvict(allEntries = true)
    public Pair<DatabaseService.Errores, Receta> crearReceta(String nombreReceta_,
                                                             String tipoDePlato,
                                                             String nivelDeDificultad,
                                                             byte[] imagenDeReceta_,
                                                             List<Pair<String, String>> listaDeIngredientes_,
                                                             List<Pair<String, String>> listaDeUtensilios_,
                                                             List<Pair<Integer, String>> listaDePasos_,
                                                             Usuario usuario_) {
        return crearRecetaConFecha(nombreReceta_,
                tipoDePlato,
                nivelDeDificultad,
                LocalDate.now(),
                imagenDeReceta_,
                listaDeIngredientes_,
                listaDeUtensilios_,
                listaDePasos_,
                usuario_);
    }
    @CacheEvict(allEntries = true)
    public Pair<DatabaseService.Errores, Receta> crearRecetaConFecha(String nombreReceta_,
                                                              String tipoDePlato,
                                                              String nivelDeDificultad,
                                                              LocalDate localDate_,
                                                              byte[] imagenDeReceta_,
                                                              List<Pair<String, String>> listaDeIngredientes_,
                                                              List<Pair<String, String>> listaDeUtensilios_,
                                                              List<Pair<Integer, String>> listaDePasos_,
                                                              Usuario usuario_) {

        if (listaDeIngredientes_.isEmpty()) {
            PersonalDebug.imprimir("WARNING: Creando una receta sin ingredientes.");
        } else {
            DatabaseService.Errores errorIngredientes = IngredienteService.comprobarIngredientes(listaDeIngredientes_);
            if (errorIngredientes != DatabaseService.Errores.SIN_ERRORES) {
                PersonalDebug.imprimir("Fallo en los ingredientes de la receta.");
                return Pair.of(errorIngredientes, new Receta());
            }
        }
        if (listaDeUtensilios_.isEmpty()) {
            PersonalDebug.imprimir("WARNING: Creando una receta sin utensilios.");
        } else {
            DatabaseService.Errores errorUtensilios = UtensilioService.comprobarUtensilios(listaDeUtensilios_);
            if (errorUtensilios != DatabaseService.Errores.SIN_ERRORES) {
                PersonalDebug.imprimir("Fallo en los utensilios de la receta.");
                return Pair.of(errorUtensilios, new Receta());
            }
        }
        if (listaDePasos_.isEmpty()) {
            PersonalDebug.imprimir("WARNING: Creando una receta sin pasos.");
        } else {
            DatabaseService.Errores errorPasos = PasoService.comprobarPasos(listaDePasos_);
            if (errorPasos != DatabaseService.Errores.SIN_ERRORES) {
                PersonalDebug.imprimir("Fallo en los pasos de la receta.");
                return Pair.of(errorPasos, new Receta());
            }
        }
        Pair<DatabaseService.Errores, Imagendb> pairReceta = imagenService.crearImagenDB(imagenDeReceta_, TipoDeImagen.RECETA);
        if (pairReceta.getFirst() != DatabaseService.Errores.SIN_ERRORES) {
            return Pair.of(pairReceta.getFirst(), new Receta());
        }
        Receta nuevaReceta = new Receta(
                nombreReceta_,
                tipoDePlato,
                NivelDeDificultad.fromString(nivelDeDificultad),
                localDate_,
                pairReceta.getSecond(),
                usuario_);
        Pair<DatabaseService.Errores, Receta> nuevoPairReceta = actualizarReceta(nuevaReceta);
        if (nuevoPairReceta.getFirst() == DatabaseService.Errores.SIN_ERRORES) {
            for (Pair<String, String> ingredienteString : listaDeIngredientes_) {
                DatabaseService.Errores errorIngredientes =
                        ingredienteService.crearIngrediente(ingredienteString.getFirst(), ingredienteString.getSecond(), nuevaReceta).getFirst();
                if (errorIngredientes != DatabaseService.Errores.SIN_ERRORES) {
                    PersonalDebug.imprimir("Fallo en los ingredientes de la receta.");
                    return Pair.of(errorIngredientes, nuevaReceta);
                }
            }
            for (Pair<String, String> utensilioString : listaDeUtensilios_) {
                DatabaseService.Errores errorUtensilios =
                        utensilioService.crearUtensilio(utensilioString.getFirst(), utensilioString.getSecond(), nuevaReceta).getFirst();
                if (errorUtensilios != DatabaseService.Errores.SIN_ERRORES) {
                    PersonalDebug.imprimir("Fallo en los utensilios de la receta.");
                    return Pair.of(errorUtensilios, nuevaReceta);
                }
            }
            for (int i = 0, l = listaDePasos_.size(); i < l; ) {
                Pair<Integer, String> pasoPair = listaDePasos_.get(i);
                DatabaseService.Errores errorPasosos =
                        pasoService.crearPaso(++i, pasoPair.getFirst(), pasoPair.getSecond(), nuevaReceta).getFirst();
                if (errorPasosos != DatabaseService.Errores.SIN_ERRORES) {
                    PersonalDebug.imprimir("Fallo en los pasos de la receta.");
                    return Pair.of(errorPasosos, nuevaReceta);
                }
            }
            nuevaReceta.recalcNumPasos();
            recetaRepository.save(nuevaReceta);
        }
        return nuevoPairReceta;

    }
    @CacheEvict(allEntries = true)
    public Pair<DatabaseService.Errores, Receta> actualizarReceta(Receta receta_) {
        if (receta_.getNombreReceta().isEmpty()) {
            PersonalDebug.imprimir("Nombre de receta nulo: " + receta_.getNombreReceta());
            return Pair.of(DatabaseService.Errores.NOMBRE_RECETA_NULO, receta_);
        } else if (receta_.getTipoPlato().isEmpty()) {
            PersonalDebug.imprimir("Tipo de plato nulo: " + receta_.getTipoPlato());
            return Pair.of(DatabaseService.Errores.TIPO_PLATO_RECETA_NULO, receta_);
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
                    return Pair.of(DatabaseService.Errores.NOMBRE_RECETA_REPETIDO, receta_);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, receta_);
                }
            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, receta_);
            }
            /*Set<Receta> recetasCreador = receta_.getCreadorDeLaReceta().getRecetasCreadas();
            if (!recetasCreador.contains(receta_)) {
                recetasCreador.add(receta_);
            }*/
            return Pair.of(DatabaseService.Errores.SIN_ERRORES, receta_);
        }
    }
    @Cacheable
    public Receta buscarPorId(long id) {
        return recetaRepository.findById(id).orElse(null);
    }

    @Cacheable
    public List<Receta> buscarReceta(int indicePagina_,
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
        if (duracionMaxima_ == null) {
            duracionMaxima_ = Integer.MAX_VALUE;
        }
        if (duracionMinima_ == null) {
            duracionMinima_ = 0;
        }
        if (duracionMaxima_ < duracionMinima_) {
            return new ArrayList<>();
        }
        QReceta recetaDsl = QReceta.receta;
        BooleanExpression predicate =
                recetaDsl.duracion_total.between(duracionMinima_, duracionMaxima_);
        if (numPasosMin_ != null && numPasosMax_ != null && numPasosMin_ > numPasosMax_) {
            return new ArrayList<>();
        }
        if (numPasosMin_ != null && numPasosMin_ > 0) {
            predicate = predicate.and(recetaDsl.numero_pasos.goe(numPasosMin_));
        }
        if (numPasosMax_ != null) {
            predicate = predicate.and(recetaDsl.numero_pasos.loe(numPasosMax_));
        }
        if (numFavoritosMin_ != null && numFavoritosMin_ > 0) {
            predicate = predicate.and(recetaDsl.numero_favoritos.goe(numFavoritosMin_));
        }
        if (numComentariosMin_ != null && numComentariosMin_ > 0) {
            predicate = predicate.and(recetaDsl.numero_comentarios.goe(numComentariosMin_));
        }
        if (tipoDePlato_ != null && !tipoDePlato_.isEmpty()) {
            predicate = predicate.and(recetaDsl.tipo_plato.like(tipoDePlato_.toLowerCase()));
        }
        if (nombreDeReceta_ != null && !nombreDeReceta_.isEmpty()) {
            predicate = predicate.and(recetaDsl.nombre_receta.likeIgnoreCase("%" + nombreDeReceta_ + "%"));
        }
        if (nivelDeDificultad_ != null) {
            predicate = predicate.and(recetaDsl.nivel_de_dificultad.eq(nivelDeDificultad_));
        }

        List<Receta> recetas = new ArrayList<>();
        Iterable<Receta> it = recetaRepository.findAll(predicate, PageRequest.of(indicePagina_, elementosPagina_));
        it.forEach((e) -> recetas.add(e));
        return recetas;
    }
    @CacheEvict(allEntries = true)
    public void eliminarTodos() {
        recetaRepository.deleteAll();
        ingredienteService.eliminarTodos();
        pasoService.eliminarTodos();
        utensilioService.eliminarTodos();
    }
    @Cacheable
    public List<Receta> todasLasRecetas() {
        return recetaRepository.findAll();
    }

    @Cacheable
    public long getNumRecetas() {
        return recetaRepository.count();
    }
}
