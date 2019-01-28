package com.TheJavaCooker.CookingWithJava.DataBase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    @Query("select u from Usuario u where u.correoElectronico = ?1")
    Usuario buscarPorCorreoElectronico(String correoElectronico_);
}
