package com.TheJavaCooker.CookingWithJava.DataBase.Repository;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Imagendb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagendbRepository extends JpaRepository<Imagendb, Long> {
}
