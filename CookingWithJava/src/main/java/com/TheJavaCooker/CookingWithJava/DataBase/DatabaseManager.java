package com.TheJavaCooker.CookingWithJava.DataBase;

import com.TheJavaCooker.CookingWithJava.PersonalDebug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseManager {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RecetaRepository recetaRepository;
    @Autowired
    private IngredienteRepository ingredienteRepository;

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
        NOMBRE_DE_INGREDIENTE_REPETIDO,
        NOMBRE_DE_INGREDIENTE_NULO,
        CANTIDAD_DE_INGREDIENTE_NULA,
        ERRROR_DESCONOCIDO
    }

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

    private Errores comprobarIngredientes(List<Pair<String, String>> listaIngredientes_) {
        List<String> ingredientesActuales = new ArrayList<>();
        for (Pair<String, String> ingrediente : listaIngredientes_) {
            String nombreIngrediente = ingrediente.getFirst();
            if (nombreIngrediente.isEmpty()) {
                PersonalDebug.imprimir("Nombre de ingrediente nulo: " + nombreIngrediente);
                return Errores.NOMBRE_DE_INGREDIENTE_NULO;
            } else if (ingrediente.getSecond().isEmpty()) {
                PersonalDebug.imprimir("Cantidad nula: " + ingrediente.getSecond());
                return Errores.CANTIDAD_DE_INGREDIENTE_NULA;
            } else if (ingredientesActuales.contains(nombreIngrediente)) {
                PersonalDebug.imprimir("Nombre de ingrediente repetido: " + nombreIngrediente);
                return Errores.NOMBRE_DE_INGREDIENTE_REPETIDO;
            } else {
                ingredientesActuales.add(nombreIngrediente);
            }
        }
        return Errores.SIN_ERRORES;
    }

    private Pair<Errores, Ingrediente> crearIngrediente(String nombreIngrediente_,
                                                        String cantidadIngrediente_,
                                                        Receta receta_) {
        Ingrediente ingrediente = new Ingrediente(nombreIngrediente_, cantidadIngrediente_, receta_);
        if (nombreIngrediente_.isEmpty()) {
            PersonalDebug.imprimir("Nombre de ingrediente nulo: " + nombreIngrediente_);
            return Pair.of(Errores.NOMBRE_DE_INGREDIENTE_NULO, ingrediente);
        } else if (cantidadIngrediente_.isEmpty()) {
            PersonalDebug.imprimir("Cantidad de ingrediente nula: " + cantidadIngrediente_);
            return Pair.of(Errores.CANTIDAD_DE_INGREDIENTE_NULA, ingrediente);
        } else if (receta_ == null) {
            PersonalDebug.imprimir("Receta nula");
            return Pair.of(Errores.ERRROR_DESCONOCIDO, ingrediente);
        } else {
            try {
                ingredienteRepository.save(ingrediente);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Ingrediente.constraintNombreIngrediente)) {
                    PersonalDebug.imprimir("Nombre de ingrediente Repetido: " + e.toString());
                    return Pair.of(Errores.NOMBRE_DE_INGREDIENTE_REPETIDO, ingrediente);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, ingrediente);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, ingrediente);
            }
            return Pair.of(Errores.SIN_ERRORES, ingrediente);
        }
    }

    public Pair<Errores, Usuario> crearUsuario(String nombreUsuario_,
                                               String contrasena_,
                                               String correo_,
                                               String nombreApellidos_) {
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
            if (usuario_.getFechaCreacion() == null) {
                PersonalDebug.imprimir("WARNING: fecha de usuario nula: puesta la actual");
                usuario_.resetFechaCreacion();
            }
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

    public Pair<Errores, Receta> crearReceta(String nombreReceta_,
                                             String tipoDePlato,
                                             String nivelDeDificultad,
                                             List<Pair<String, String>> listaDeIngredientes_,
                                             List<Pair<String, String>> listaDeUtensilios_,
                                             List<Pair<Integer, String>> listaPasos_,
                                             Usuario usuario_) {
        Receta nuevaReceta = new Receta(
                nombreReceta_,
                tipoDePlato,
                NivelDeDificultad.fromString(nivelDeDificultad),
                usuario_);
        if (listaDeIngredientes_.isEmpty()) {
            PersonalDebug.imprimir("WARNING: Creando una receta sin ingredientes.");
        } else {
            Errores errorIngredientes = comprobarIngredientes(listaDeIngredientes_);
            if (errorIngredientes != Errores.SIN_ERRORES) {
                PersonalDebug.imprimir("Fallo en los ingredientes de la receta.");
                return Pair.of(errorIngredientes, nuevaReceta);
            }
        }
        //todo Aqui ira la comprobacion de los utensios y pasos
        Pair<Errores, Receta> nuevoPairReceta = actualizarOCrearReceta(nuevaReceta);
        if (nuevoPairReceta.getFirst() == Errores.SIN_ERRORES) {
            for (Pair<String, String> ingredienteString : listaDeIngredientes_) {
                Errores errorIngredientes =
                        crearIngrediente(ingredienteString.getFirst(), ingredienteString.getSecond(), nuevaReceta).getFirst();
                if (errorIngredientes != Errores.SIN_ERRORES) {
                    PersonalDebug.imprimir("Fallo en los ingredientes de la receta.");
                    return Pair.of(errorIngredientes, nuevaReceta);
                }
            }
            //todo Aqui ira lo de los utensilios y pasos
        }
        return nuevoPairReceta;

    }

    public Pair<Errores, Receta> actualizarOCrearReceta(Receta receta_) {
        if (receta_.getNombreReceta().isEmpty()) {
            PersonalDebug.imprimir("Nombre de receta nulo: " + receta_.getNombreReceta());
            return Pair.of(Errores.NOMBRE_RECETA_NULO, receta_);
        } else if (receta_.getTipoPlato().isEmpty()) {
            PersonalDebug.imprimir("Tipo de plato nulo: " + receta_.getTipoPlato());
            return Pair.of(Errores.TIPO_PLATO_RECETA_NULO, receta_);
        } else {
            if (receta_.getFechaCreacion() == null) {
                PersonalDebug.imprimir("WARNING: fecha de receta nula: puesta la actual");
                receta_.resetFechaCreacion();
            }
            if (receta_.getNivelDificultad() == null) {
                PersonalDebug.imprimir("WARNING: nivel de dificultad nulo puesto a indefinido");
                receta_.setNivelDificultad(NivelDeDificultad.INDEFINIDO);
            }
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

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }

    public RecetaRepository getRecetaRepository() {
        return recetaRepository;
    }

    public IngredienteRepository getIngredienteRepository() {
        return ingredienteRepository;
    }
}
