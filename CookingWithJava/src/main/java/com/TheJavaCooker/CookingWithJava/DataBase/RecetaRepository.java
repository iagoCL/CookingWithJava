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
        , QuerydslPredicateExecutor<Receta> {

    @Query("select r from Receta r")
    List<Receta> buscarTodas();

    @Query("select r from Receta r where r.nombre_receta = ?1")
    Receta buscarPorNombreReceta(String buscarPorNombreReceta);

    @Query("select r from Receta r where r.tipo_plato = ?1")
    List<Receta> buscarPorTipoDePlato(String tipo_platoEnMiniscula);

    @Query("select distinct r.tipo_plato from Receta r")
    List<String> tiposDePlato();

    @Query("select r from Receta r order by r.fecha_creacion desc")
    Page<Receta> ultimasRecetas(PageRequest pageable);

    @Query("select r from Receta r where r.nombre_receta = ?1 and r.tipo_plato = ?2 and r.nivel_de_dificultad = ?3")
    List<Receta> buscarReceta(String nombre_receta, String tipo_plato, NivelDeDificultad nivel_de_dificultad);

    long count();//numero de recetas
}
