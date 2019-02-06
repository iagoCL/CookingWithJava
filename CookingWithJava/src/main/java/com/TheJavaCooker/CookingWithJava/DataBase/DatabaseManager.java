package com.TheJavaCooker.CookingWithJava.DataBase;

import com.TheJavaCooker.CookingWithJava.PersonalDebug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class DatabaseManager {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RecetaRepository recetaRepository;
    private Random random = new Random();

    public enum Errores {
        SIN_ERRORES,
        NOMBRE_USUARIO_REPETIDO,
        CORREO_ELECTRONICO_REPETIDO,
        NOMBRE_USUARIO_NULO,
        CONTRASENA_NULA,
        CORREO_ELECTRONICO_NULO,
        NOMBRE_APELLIDOS_NULOS,
        NOMBRE_RECETA_NULO,
        NOMBRE_RECETA_REPETIDO,
        TIPO_PLATO_RECETA_NULO,
        ERRROR_DESCONOCIDO
    }

    public void crearFavoritosAleatorios(int numFavoritos_) {
        for (int i = 0; i < numFavoritos_; ++i) {
            Receta recetaAleatoria = getRecetaAletoria();
            recetaAleatoria.marcarFavorito(getUsuarioAletorio());
            recetaRepository.save(recetaAleatoria);
        }
    }

    public Receta getRecetaAletoria() {
        List<Receta> recetas = recetaRepository.findAll();
        return recetas.get(random.nextInt(recetas.size()));
    }

    public Usuario getUsuarioAletorio() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.get(random.nextInt(usuarios.size()));
    }

    public void crearUsuariosEjemplo(int numUsuarios_) {
        for (int i = 1; i < numUsuarios_; ++i) {
            crearUsuario("Usuario" + i,
                    "contasena" + i,
                    "correo" + i + "@example.com",
                    "Nombre" + i + " Apellidos" + i);
        }
    }

    public Pair<Errores, Usuario> crearUsuario(String nombreUsuario_, String contrasena_, String correo_, String nombreApellidos_) {
        return actualizarOCrearUsuario(new Usuario(nombreUsuario_, contrasena_, correo_, nombreApellidos_));
    }

    public Pair<Errores, Usuario> actualizarOCrearUsuario(Usuario usuario_) {
        if (usuario_.getNombreUsuario().isEmpty()) {
            PersonalDebug.imprimir("Nombre de usuario nulo: " + usuario_.getNombreUsuario());
            return Pair.of(Errores.NOMBRE_USUARIO_NULO, usuario_);
        } else if (usuario_.getContrasena().isEmpty()) {
            PersonalDebug.imprimir("Contraseña nula: " + usuario_.getContrasena());
            return Pair.of(Errores.CONTRASENA_NULA, usuario_);
        } else if (usuario_.getNombreApellidos().isEmpty()) {
            PersonalDebug.imprimir("Nombre y apellidos nulos: " + usuario_.getNombreApellidos());
            return Pair.of(Errores.NOMBRE_APELLIDOS_NULOS, usuario_);
        } else if (usuario_.getCorreoElectronico().isEmpty()) {
            PersonalDebug.imprimir("Correo electronico nulo: " + usuario_.getCorreoElectronico());
            return Pair.of(Errores.CORREO_ELECTRONICO_NULO, usuario_);
        } else {
            try {
                usuarioRepository.save(usuario_);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Usuario.constraintNombreUsuario)) {
                    PersonalDebug.imprimir("Nombre Usuario Repetido: " + e.toString());
                    return Pair.of(Errores.NOMBRE_USUARIO_REPETIDO, usuario_);
                } else if (e.toString().contains(Usuario.constraintCorreoElectronico)) {
                    PersonalDebug.imprimir("Correo electronico repetido: " + e.toString());
                    return Pair.of(Errores.CORREO_ELECTRONICO_REPETIDO, usuario_);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, usuario_);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, usuario_);
            }
            return Pair.of(Errores.SIN_ERRORES, usuario_);
        }
    }

    public Pair<Errores, Receta> crearReceta(String nombreReceta_, String tipoDePlato, Usuario usuario_) {
        return actualizarOCrearReceta(new Receta(nombreReceta_, tipoDePlato, usuario_));
    }

    public Pair<Errores, Receta> actualizarOCrearReceta(Receta receta_) {
        if (receta_.getNombreReceta().isEmpty()) {
            PersonalDebug.imprimir("Nombre de receta nulo: " + receta_.getNombreReceta());
            return Pair.of(Errores.NOMBRE_RECETA_NULO, receta_);
        } else if (receta_.getTipoPlato().isEmpty()) {
            PersonalDebug.imprimir("Tipo de plato nulo: " + receta_.getTipoPlato());
            return Pair.of(Errores.TIPO_PLATO_RECETA_NULO, receta_);
        } else {
            try {
                recetaRepository.save(receta_);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Receta.constraintNombreReceta)) {
                    PersonalDebug.imprimir("Nombre Receta Repetido: " + e.toString());
                    return Pair.of(Errores.NOMBRE_RECETA_REPETIDO, receta_);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, receta_);
                }
            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, receta_);
            }
            List<Receta> recetasCreador = receta_.getCreadorDeLaReceta().getRecetasCreadas();
            if (!recetasCreador.contains(receta_)) {
                recetasCreador.add(receta_);
            }
            return Pair.of(Errores.SIN_ERRORES, receta_);
        }
    }

    public String getTipoDePlatoAleatorio() {
        switch (random.nextInt(5)) {
            case 0:
                return "pescado";
            case 1:
                return "carne";
            case 2:
                return "postre";
            case 3:
                return "plato principal";
            case 4:
                return "sopa";
            default:
                return "undefined";
        }
    }

    public void crearRecetasEjemplo(int numRecetas) {
        for (int i = 1; i < numRecetas; ++i) {
            crearReceta("Nombre Receta " + i, getTipoDePlatoAleatorio(), getUsuarioAletorio());
        }
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }

    public RecetaRepository getRecetaRepository() {
        return recetaRepository;
    }


}
