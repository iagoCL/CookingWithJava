package com.TheJavaCooker.CookingWithJava.DataBase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {

    @Query("select r from Receta r where r.nombreReceta = ?1")
    Receta buscarPorNombreReceta(String buscarPorNombreReceta);

    @Query("select r from Receta r where r.tipoPlato = ?1")
    List<Receta> buscarPorTipoDePlato(String tipoPlatoEnMiniscula);

    @Query("select distinct r.tipoPlato from Receta r")
    List<String> tiposDePlato();

    long count();//numero de usuarios
}