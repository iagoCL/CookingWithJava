package com.TheJavaCooker.CookingWithJava.DataBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseManager {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void crearUsuariosEjemplo(int numUsuarios_){
        for( int i = 1; i<numUsuarios_; ++i) {
            crearUsuario("Usuario" + i, "contasena" + i, "correo" + i + "@example.com");
        }
    }

    public Usuario crearUsuario(String nombreUsuario_, String contrasena_, String correo_){
        Usuario nuevoUsuario = new Usuario(nombreUsuario_, contrasena_, correo_);
        usuarioRepository.save(nuevoUsuario);
        return  nuevoUsuario;
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }
}
