package com.TheJavaCooker.CookingWithJava.DataBase.Repository;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.correo_electronico = ?1")
    Usuario buscarPorCorreoElectronico(String correo_electronico_);

    @Query("select u from Usuario u where u.nombre_usuario = ?1")
    Usuario buscarPorNombreUsuario(String nombreUsuario_);

    long count();//numero de usuarios
}


