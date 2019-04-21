package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = {"recetasCache","usuariosCache"})
public class FavoritoService {
    @Autowired
    private RecetaRepository recetaRepository;

    @CacheEvict(allEntries = true)
    public boolean marcarFavorito(Usuario usuario_, Receta receta_) {
        if (receta_.marcarFavorito(usuario_)) {
            recetaRepository.save(receta_);
            return true;
        } else {
            return false;
        }
    }

    @CacheEvict(allEntries = true)
    public boolean quitarFavorito(Usuario usuario_, Receta receta_) {
        if (receta_.quitarFavorito(usuario_)) {
            recetaRepository.save(receta_);
            return true;
        } else {
            return false;
        }
    }
}
