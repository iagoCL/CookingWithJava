package com.TheJavaCooker.CookingWithJava.DataBase.Repository;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Ingrediente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    @Query("select distinct i.nombre_ingrediente from Ingrediente i")
    List<String> todosLosIngredientes();
}
