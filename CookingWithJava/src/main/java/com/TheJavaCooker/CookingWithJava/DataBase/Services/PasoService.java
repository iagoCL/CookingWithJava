package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.*;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.*;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasoService {
    @Autowired
    private PasoRepository pasoRepository;

    static DatabaseService.Errores comprobarPasos(List<Pair<Integer, String>> listaPasos_) {
        for (int i = 0, l = listaPasos_.size(); i < l; ++i) {
            Pair<Integer, String> paso = listaPasos_.get(i);
            if (paso.getSecond().isEmpty()) {
                PersonalDebug.imprimir("Descripcion de paso nula en :" + i);
                return DatabaseService.Errores.DESCRIPCION_DE_PASO_NULA;
            } else if (paso.getFirst() < 0) {
                PersonalDebug.imprimir("Tiempo de paso nulo o negativo: " + paso.getFirst());
                return DatabaseService.Errores.TIEMPO_DE_PASO_INCORRECTO;
            }
        }
        return DatabaseService.Errores.SIN_ERRORES;
    }

    Pair<DatabaseService.Errores, Paso> crearPaso(int numeroPaso_,
                                                  int tiempoEnMinutos_,
                                                  String descripcionPaso_,
                                                  Receta receta_) {
        Paso paso = new Paso(numeroPaso_, tiempoEnMinutos_, descripcionPaso_, receta_);
        if (numeroPaso_ < 1) {
            PersonalDebug.imprimir("Numero de paso nulo o negativo: " + numeroPaso_);
            return Pair.of(DatabaseService.Errores.NUMERO_DE_PASO_INCORRECTO, paso);
        } else if (tiempoEnMinutos_ < 1) {
            PersonalDebug.imprimir("tiempo de paso nulo o negativo: " + tiempoEnMinutos_);
            return Pair.of(DatabaseService.Errores.TIEMPO_DE_PASO_INCORRECTO, paso);
        } else if (descripcionPaso_.isEmpty()) {
            PersonalDebug.imprimir("Descripcion de paso nula: " + descripcionPaso_);
            return Pair.of(DatabaseService.Errores.DESCRIPCION_DE_PASO_NULA, paso);
        } else if (receta_ == null) {
            PersonalDebug.imprimir("Receta nula");
            return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, paso);
        } else {
            try {
                pasoRepository.save(paso);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Paso.constraintNombrePaso)) {
                    PersonalDebug.imprimir("Numero de paso Repetido: " + e.toString());
                    return Pair.of(DatabaseService.Errores.NUMERO_DE_PASO_REPETIDO, paso);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, paso);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, paso);
            }
            receta_.getPasos().add(paso);
            return Pair.of(DatabaseService.Errores.SIN_ERRORES, paso);
        }
    }

    void eliminarTodos() {
        pasoRepository.deleteAll();
    }
}
