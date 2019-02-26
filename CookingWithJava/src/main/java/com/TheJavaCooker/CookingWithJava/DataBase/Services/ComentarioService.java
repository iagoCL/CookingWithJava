package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.*;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.*;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class ComentarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RecetaRepository recetaRepository;
    @Autowired
    private ComentarioRepository comentarioRepository;

    @Transactional
    public Pair<DatabaseService.Errores, Comentario> crearComentario(String descripcionComentario_,
                                                                     String tituloComentario_,
                                                                     Receta recetaId_,
                                                                     Usuario usuarioId_) {
        return crearComentarioConFecha(descripcionComentario_, tituloComentario_,
                LocalDateTime.now(), recetaId_, usuarioId_);
    }

    @Transactional
    Pair<DatabaseService.Errores, Comentario> crearComentarioConFecha(String descripcionComentario_,
                                                                      String tituloComentario_,
                                                                      LocalDateTime fechaComentario_,
                                                                      Receta recetaId_,
                                                                      Usuario usuarioId_) {
        Comentario comentario = new Comentario(descripcionComentario_,
                tituloComentario_, fechaComentario_, recetaId_, usuarioId_);
        if (descripcionComentario_.isEmpty()) {
            PersonalDebug.imprimir("Descripcion de comentario nula: " + descripcionComentario_);
            return Pair.of(DatabaseService.Errores.DESCRIPCION_DE_COMENTARIO_NULA, comentario);
        } else if (tituloComentario_.isEmpty()) {
            PersonalDebug.imprimir("Titulo de comentario nulo: " + descripcionComentario_);
            return Pair.of(DatabaseService.Errores.TITULO_DE_COMENTARIO_NULO, comentario);
        } else if (recetaId_ == null) {
            PersonalDebug.imprimir("Receta nula");
            return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, comentario);
        } else if (usuarioId_ == null) {
            PersonalDebug.imprimir("Usuario nulo");
            return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, comentario);
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
                    return Pair.of(DatabaseService.Errores.COMENTARIO_REPETIDO, comentario);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, comentario);
                }
            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, comentario);
            }
            recetaId_.nuevoComentario();
            usuarioId_.nuevoComentario();
            recetaRepository.save(recetaId_);
            usuarioRepository.save(usuarioId_);
            return Pair.of(DatabaseService.Errores.SIN_ERRORES, comentario);
        }
    }

    void eliminarTodos() {
        comentarioRepository.deleteAll();
    }

}
