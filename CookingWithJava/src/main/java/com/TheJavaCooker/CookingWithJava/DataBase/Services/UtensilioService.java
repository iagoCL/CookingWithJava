package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.*;
import com.TheJavaCooker.CookingWithJava.DataBase.NivelDeDificultad;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.*;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UtensilioService {
    @Autowired
    private UtensilioRepository utensilioRepository;

    static DatabaseService.Errores comprobarUtensilios(List<Pair<String, String>> listaUtensilios_) {
        List<String> utensiliosActuales = new ArrayList<>();
        for (Pair<String, String> utensilio : listaUtensilios_) {
            String nombreIngrediente = utensilio.getFirst().toLowerCase();
            if (nombreIngrediente.isEmpty()) {
                PersonalDebug.imprimir("Nombre de utensilio nulo: " + nombreIngrediente);
                return DatabaseService.Errores.NOMBRE_DE_UTENSILIO_NULO;
            } else if (utensiliosActuales.contains(nombreIngrediente)) {
                PersonalDebug.imprimir("Nombre de utensilio repetido: " + nombreIngrediente);
                return DatabaseService.Errores.NOMBRE_DE_UTENSILIO_REPETIDO;
            } else {
                utensiliosActuales.add(nombreIngrediente);
            }
        }
        return DatabaseService.Errores.SIN_ERRORES;
    }


    Pair<DatabaseService.Errores, Utensilio> crearUtensilio(String nombreUtensilio_,
                                                            String nivelDeDificultad_,
                                                            Receta receta_) {
        Utensilio utensilio = new Utensilio(nombreUtensilio_, NivelDeDificultad.fromString(nivelDeDificultad_), receta_);
        if (nombreUtensilio_.isEmpty()) {
            PersonalDebug.imprimir("Nombre de utensilio nulo: " + nombreUtensilio_);
            return Pair.of(DatabaseService.Errores.NOMBRE_DE_UTENSILIO_NULO, utensilio);
        } else if (receta_ == null) {
            PersonalDebug.imprimir("Receta nula");
            return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, utensilio);
        } else {
            try {
                utensilioRepository.save(utensilio);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Utensilio.constraintNombreUtensilio)) {
                    PersonalDebug.imprimir("Nombre de utensilio Repetido: " + e.toString());
                    return Pair.of(DatabaseService.Errores.NOMBRE_DE_UTENSILIO_REPETIDO, utensilio);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, utensilio);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, utensilio);
            }
            receta_.getUtensilios().add(utensilio);
            return Pair.of(DatabaseService.Errores.SIN_ERRORES, utensilio);
        }
    }

    void eliminarTodos() {
        utensilioRepository.deleteAll();
    }
}
