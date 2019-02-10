package com.TheJavaCooker.CookingWithJava.DataBase;

import com.TheJavaCooker.CookingWithJava.PersonalDebug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DatabaseManager {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RecetaRepository recetaRepository;
    @Autowired
    private IngredienteRepository ingredienteRepository;
    @Autowired
    private PasoRepository pasoRepository;
    @Autowired
    private UtensilioRepository utensilioRepository;
    @Autowired
    private ComentarioRepository comentarioRepository;


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
        NOMBRE_DE_UTENSILIO_NULO,
        NOMBRE_DE_UTENSILIO_REPETIDO,
        DESCRIPCION_DE_PASO_NULA,
        TIEMPO_DE_PASO_INCORRECTO,
        NUMERO_DE_PASO_INCORRECTO,
        NUMERO_DE_PASO_REPETIDO,
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
            String nombreIngrediente = ingrediente.getFirst().toLowerCase();
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

    private Errores comprobarUtensilios(List<Pair<String, String>> listaUtensilios_) {
        List<String> utensiliosActuales = new ArrayList<>();
        for (Pair<String, String> utensilio : listaUtensilios_) {
            String nombreIngrediente = utensilio.getFirst().toLowerCase();
            if (nombreIngrediente.isEmpty()) {
                PersonalDebug.imprimir("Nombre de utensilio nulo: " + nombreIngrediente);
                return Errores.NOMBRE_DE_UTENSILIO_NULO;
            } else if (utensiliosActuales.contains(nombreIngrediente)) {
                PersonalDebug.imprimir("Nombre de utensilio repetido: " + nombreIngrediente);
                return Errores.NOMBRE_DE_UTENSILIO_REPETIDO;
            } else {
                utensiliosActuales.add(nombreIngrediente);
            }
        }
        return Errores.SIN_ERRORES;
    }

    private Errores comprobarPasos(List<Pair<Integer, String>> listaPasos_) {
        for(int i = 0, l = listaPasos_.size(); i<l;++i){
            Pair<Integer, String> paso = listaPasos_.get(i);
            if (paso.getSecond().isEmpty()) {
                PersonalDebug.imprimir("Descripcion de paso nula en :"+i);
                return Errores.DESCRIPCION_DE_PASO_NULA;
            } else if (paso.getFirst() < 0) {
                PersonalDebug.imprimir("Tiempo de paso nulo o negativo: " + paso.getFirst());
                return Errores.TIEMPO_DE_PASO_INCORRECTO;
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
            receta_.getIngredientes().add(ingrediente);
            return Pair.of(Errores.SIN_ERRORES, ingrediente);
        }
    }

    private Pair<Errores, Utensilio> crearUtensilio(String nombreUtensilio_,
                                                        String nivelDeDificultad_,
                                                        Receta receta_) {
        Utensilio utensilio = new Utensilio(nombreUtensilio_, NivelDeDificultad.fromString(nivelDeDificultad_), receta_);
        if (nombreUtensilio_.isEmpty()) {
            PersonalDebug.imprimir("Nombre de utensilio nulo: " + nombreUtensilio_);
            return Pair.of(Errores.NOMBRE_DE_UTENSILIO_NULO, utensilio);
        } else if (receta_ == null) {
            PersonalDebug.imprimir("Receta nula");
            return Pair.of(Errores.ERRROR_DESCONOCIDO, utensilio);
        } else {
            try {
                utensilioRepository.save(utensilio);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Utensilio.constraintNombreUtensilio)) {
                    PersonalDebug.imprimir("Nombre de utensilio Repetido: " + e.toString());
                    return Pair.of(Errores.NOMBRE_DE_UTENSILIO_REPETIDO, utensilio);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, utensilio);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, utensilio);
            }
            receta_.getUtensilios().add(utensilio);
            return Pair.of(Errores.SIN_ERRORES, utensilio);
        }
    }

    private Pair<Errores, Paso> crearPaso(int numeroPaso_,
                                                 int tiempoEnMinutos_,
                                                 String descripcionPaso_,
                                                Receta receta_) {
        Paso paso = new Paso(numeroPaso_, tiempoEnMinutos_,descripcionPaso_, receta_);
        if (numeroPaso_<1) {
            PersonalDebug.imprimir("Numero de paso nulo o negativo: " + numeroPaso_);
            return Pair.of(Errores.NUMERO_DE_PASO_INCORRECTO, paso);
        } else if (tiempoEnMinutos_<1) {
            PersonalDebug.imprimir("tiempo de paso nulo o negativo: " + tiempoEnMinutos_);
            return Pair.of(Errores.TIEMPO_DE_PASO_INCORRECTO, paso);
        } else if (descripcionPaso_.isEmpty()) {
            PersonalDebug.imprimir("Descripcion de paso nula: " + descripcionPaso_);
            return Pair.of(Errores.DESCRIPCION_DE_PASO_NULA, paso);
        } else if (receta_ == null) {
            PersonalDebug.imprimir("Receta nula");
            return Pair.of(Errores.ERRROR_DESCONOCIDO, paso);
        } else {
            try {
                pasoRepository.save(paso);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Paso.constraintNombrePaso)) {
                    PersonalDebug.imprimir("Numero de paso Repetido: " + e.toString());
                    return Pair.of(Errores.NUMERO_DE_PASO_REPETIDO, paso);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, paso);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, paso);
            }
            receta_.getPasos().add(paso);
            return Pair.of(Errores.SIN_ERRORES, paso);
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
                                             List<Pair<Integer, String>> listaDePasos_,
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
        if (listaDeUtensilios_.isEmpty()) {
            PersonalDebug.imprimir("WARNING: Creando una receta sin utensilios.");
        } else {
            Errores errorUtensilios = comprobarUtensilios(listaDeUtensilios_);
            if (errorUtensilios != Errores.SIN_ERRORES) {
                PersonalDebug.imprimir("Fallo en los utensilios de la receta.");
                return Pair.of(errorUtensilios, nuevaReceta);
            }
        }
        if (listaDePasos_.isEmpty()) {
            PersonalDebug.imprimir("WARNING: Creando una receta sin pasos.");
        } else {
            Errores errorPasos = comprobarPasos(listaDePasos_);
            if (errorPasos != Errores.SIN_ERRORES) {
                PersonalDebug.imprimir("Fallo en los pasos de la receta.");
                return Pair.of(errorPasos, nuevaReceta);
            }
        }

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
            for (Pair<String, String> utensilioString : listaDeUtensilios_) {
                Errores errorUtensilios =
                        crearUtensilio(utensilioString.getFirst(), utensilioString.getSecond(), nuevaReceta).getFirst();
                if (errorUtensilios != Errores.SIN_ERRORES) {
                    PersonalDebug.imprimir("Fallo en los utensilios de la receta.");
                    return Pair.of(errorUtensilios, nuevaReceta);
                }
            }
            for(int i = 0, l = listaDePasos_.size(); i<l;){
                Pair<Integer, String> pasoPair = listaDePasos_.get(i);
                Errores errorPasosos =
                        crearPaso(++i,pasoPair.getFirst(), pasoPair.getSecond(), nuevaReceta).getFirst();
                if (errorPasosos != Errores.SIN_ERRORES) {
                    PersonalDebug.imprimir("Fallo en los pasos de la receta.");
                    return Pair.of(errorPasosos, nuevaReceta);
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
            Set<Receta> recetasCreador = receta_.getCreadorDeLaReceta().getRecetasCreadas();
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

    public PasoRepository getPasoRepository() {
        return pasoRepository;
    }

    public UtensilioRepository getUtensilioRepository() {
        return utensilioRepository;
    }

    public ComentarioRepository getComentarioRepository() {
        return comentarioRepository;
    }
}
