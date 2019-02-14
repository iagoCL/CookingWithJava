package com.TheJavaCooker.CookingWithJava.DataBase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtensilioRepository extends JpaRepository<Utensilio, Long> {

    @Query("select distinct u.nombre_utensilio from Utensilio u")
    List<String> todosLosUtensilios();
}
