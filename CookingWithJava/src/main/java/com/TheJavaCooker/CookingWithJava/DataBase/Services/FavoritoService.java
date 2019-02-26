package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavoritoService {
    @Autowired
    private RecetaRepository recetaRepository;

    public boolean marcarFavorito(Usuario usuario_, Receta receta_) {
        if (receta_.marcarFavorito(usuario_)) {
            recetaRepository.save(receta_);
            return true;
        } else {
            return false;
        }
    }

    public boolean quitarFavorito(Usuario usuario_, Receta receta_) {
        if (receta_.quitarFavorito(usuario_)) {
            recetaRepository.save(receta_);
            return true;
        } else {
            return false;
        }
    }
}
