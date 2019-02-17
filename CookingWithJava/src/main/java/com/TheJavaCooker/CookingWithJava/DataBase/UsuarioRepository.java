package com.TheJavaCooker.CookingWithJava.DataBase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.correo_electronico = ?1")
    Usuario buscarPorCorreoElectronico(String correo_electronico_);

    @Query("select u from Usuario u where u.nombre_usuario = ?1")
    Usuario buscarPorNombreUsuario(String nombreUsuario_);

    @Query("select u from Usuario u where u.nombre_usuario = ?1")
    Usuario loginValido(String nombre_usuario, String contrasena);

    long count();//numero de usuarios
}


