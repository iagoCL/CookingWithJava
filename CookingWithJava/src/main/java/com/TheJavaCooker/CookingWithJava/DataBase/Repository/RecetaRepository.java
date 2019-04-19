package com.TheJavaCooker.CookingWithJava.DataBase.Repository;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;

import org.springframework.cache.annotation.CacheConfig;
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
        , QuerydslPredicateExecutor<Receta> {

    @Query("select r from Receta r where r.nombre_receta = ?1")
    Receta buscarPorNombreReceta(String buscarPorNombreReceta);

    @Query("select r from Receta r where r.tipo_plato = ?1")
    List<Receta> buscarPorTipoDePlato(String tipo_platoEnMiniscula);

    @Query("select distinct r.tipo_plato from Receta r")
    List<String> tiposDePlato();

    @Query("select r from Receta r order by r.fecha_creacion desc")
    Page<Receta> ultimasRecetas(PageRequest pageable);

    long count();//numero de recetas
}
