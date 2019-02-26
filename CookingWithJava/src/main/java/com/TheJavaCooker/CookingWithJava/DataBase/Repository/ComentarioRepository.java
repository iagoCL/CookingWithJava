package com.TheJavaCooker.CookingWithJava.DataBase.Repository;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Comentario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}
