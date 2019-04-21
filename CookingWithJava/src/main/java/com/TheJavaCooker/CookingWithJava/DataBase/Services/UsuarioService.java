package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.*;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.*;
import com.TheJavaCooker.CookingWithJava.DataBase.TipoDeImagen;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@CacheConfig(cacheNames = "usuariosCache")
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ImagenService imagenService;

    @CacheEvict(value = "usuariosCache", allEntries = true)
    public Pair<DatabaseService.Errores, Usuario> crearUsuario(String nombreUsuario_,
                                                               String contrasena_,
                                                               String correo_,
                                                               String nombreApellidos_,
                                                               byte[] imgUsuario_
    ) {
        Pair<DatabaseService.Errores, Imagendb> p = imagenService.crearImagenDB(imgUsuario_, TipoDeImagen.USUARIO);
        if (p.getFirst() == DatabaseService.Errores.SIN_ERRORES) {
            return actualizarUsuario(new Usuario(nombreUsuario_, contrasena_, correo_, nombreApellidos_, p.getSecond()));
        } else {
            return Pair.of(p.getFirst(), new Usuario());
        }
    }

    @CacheEvict(value = "usuariosCache", allEntries = true)
    public Pair<DatabaseService.Errores, Usuario> actualizarUsuario(Usuario usuario_) {
        if (usuario_.getNombreUsuario().isEmpty()) {
            PersonalDebug.imprimir("Nombre de usuario nulo: " + usuario_.getNombreUsuario());
            return Pair.of(DatabaseService.Errores.NOMBRE_USUARIO_NULO, usuario_);
        } else if (usuario_.getContrasena().isEmpty()) {
            PersonalDebug.imprimir("Contraseña nula: " + usuario_.getContrasena());
            return Pair.of(DatabaseService.Errores.CONTRASENA_NULA, usuario_);
        } else if (usuario_.getNombreApellidos().isEmpty()) {
            PersonalDebug.imprimir("Nombre y apellidos nulos: " + usuario_.getNombreApellidos());
            return Pair.of(DatabaseService.Errores.NOMBRE_APELLIDOS_NULOS, usuario_);
        } else if (usuario_.getCorreoElectronico().isEmpty()) {
            PersonalDebug.imprimir("Correo electronico nulo: " + usuario_.getCorreoElectronico());
            return Pair.of(DatabaseService.Errores.CORREO_ELECTRONICO_NULO, usuario_);
        } else {
            if (usuario_.getFechaCreacion() == null) {
                PersonalDebug.imprimir("WARNING: fecha de usuario nula: puesta la actual");
                usuario_.resetFechaCreacion();
            }
            try {
                usuarioRepository.save(usuario_);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Usuario.constraintNombreUsuario)) {
                    PersonalDebug.imprimir("Nombre Usuario Repetido: " + e.toString());
                    return Pair.of(DatabaseService.Errores.NOMBRE_USUARIO_REPETIDO, usuario_);
                } else if (e.toString().contains(Usuario.constraintCorreoElectronico)) {
                    PersonalDebug.imprimir("Correo electronico repetido: " + e.toString());
                    return Pair.of(DatabaseService.Errores.CORREO_ELECTRONICO_REPETIDO, usuario_);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, usuario_);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(DatabaseService.Errores.ERRROR_DESCONOCIDO, usuario_);
            }
            return Pair.of(DatabaseService.Errores.SIN_ERRORES, usuario_);
        }
    }

    @Cacheable(value = "usuariosCache")
    public Usuario buscarPorId(long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "usuariosCache")
    public List<Usuario> todosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @CacheEvict(allEntries = true)
    public void eliminarTodos() {
        usuarioRepository.deleteAll();
    }

    @Cacheable(value = "usuariosCache")
    public long getNumUsuarios() {
        return usuarioRepository.count();
    }
}
