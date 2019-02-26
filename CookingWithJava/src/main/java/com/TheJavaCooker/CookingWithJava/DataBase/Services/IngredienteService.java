package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.*;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.*;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IngredienteService {
    @Autowired
    private IngredienteRepository ingredienteRepository;

    static DatabaseService.Errores comprobarIngredientes(List<Pair<String, String>> listaIngredientes_) {
        List<String> ingredientesActuales = new ArrayList<>();
        for (Pair<String, String> ingrediente : listaIngredientes_) {
            String nombreIngrediente = ingrediente.getFirst().toLowerCase();
            if (nombreIngrediente.isEmpty()) {
                PersonalDebug.imprimir("Nombre de ingrediente nulo: " + nombreIngrediente);
                return DatabaseService.Errores.NOMBRE_DE_INGREDIENTE_NULO;
            } else if (ingrediente.getSecond().isEmpty()) {
                PersonalDebug.imprimir("Cantidad nula: " + ingrediente.getSecond());
                return DatabaseService.Errores.CANTIDAD_DE_INGREDIENTE_NULA;
            } else if (ingredientesActuales.contains(nombreIngrediente)) {
                PersonalDebug.imprimir("Nombre de ingrediente repetido: " + nombreIngrediente);
                return DatabaseService.Errores.NOMBRE_DE_INGREDIENTE_REPETIDO;
            } else {
                ingredientesActuales.add(nombreIngrediente);
            }
        }
        return DatabaseService.Errores.SIN_ERRORES;
    }

    Pair<DatabaseService.Errores, Ingrediente> crearIngrediente(String nombreIngrediente_,
                                                                String cantidadIngrediente_,
                                                                Receta receta_) {
        Ingrediente ingrediente = new Ingrediente(nombreIngrediente_, cantidadIngrediente_, receta_);
        if (nombreIngrediente_.isEmpty()) {
            PersonalDebug.imprimir("Nombre de ingrediente nulo: " + nombreIngrediente_);
            return Pair.of(DatabaseService.Errores.NOMBRE_DE_INGREDIENTE_NULO, ingrediente);
        } else if (cantidadIngrediente_.isEmpty()) {
            PersonalDebug.imprimir("Cantidad de ingrediente nula: " + cantidadIngrediente_);
            return Pair.of(DatabaseService.Errores.CANTIDAD_DE_INGREDIENTE_NULA, ingrediente);
        } else if (receta_ == null) {
            PersonalDebug.imprimir("Receta nula");
            return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, ingrediente);
        } else {
            try {
                ingredienteRepository.save(ingrediente);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Ingrediente.constraintNombreIngrediente)) {
                    PersonalDebug.imprimir("Nombre de ingrediente Repetido: " + e.toString());
                    return Pair.of(DatabaseService.Errores.NOMBRE_DE_INGREDIENTE_REPETIDO, ingrediente);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, ingrediente);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, ingrediente);
            }
            receta_.getIngredientes().add(ingrediente);
            return Pair.of(DatabaseService.Errores.SIN_ERRORES, ingrediente);
        }
    }

    void eliminarTodos() {
        ingredienteRepository.deleteAll();
    }
}
