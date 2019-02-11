package com.TheJavaCooker.CookingWithJava.DataBase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

@org.springframework.stereotype.Repository
public interface RecetaRepository extends JpaRepository<Receta, Long>
        , Repository<Receta, Long>
        , QuerydslPredicateExecutor<Receta>{

    @Query("select r from Receta r where r.nombreReceta = ?1")
    Receta buscarPorNombreReceta(String buscarPorNombreReceta);

    @Query("select r from Receta r where r.tipoPlato = ?1")
    List<Receta> buscarPorTipoDePlato(String tipoPlatoEnMiniscula);

    @Query("select distinct r.tipoPlato from Receta r")
    List<String> tiposDePlato();

    @Query("select r from Receta r order by r.fechaCreacion desc")
    Page<Receta> ultimasRecentas(PageRequest pageable);

    long count();//numero de usuarios
}