package com.TheJavaCooker.CookingWithJava.DataBase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    @Query("select distinct i.nombreIngrediente from Ingrediente i")
    List<String> todosLosIngredientes();
}