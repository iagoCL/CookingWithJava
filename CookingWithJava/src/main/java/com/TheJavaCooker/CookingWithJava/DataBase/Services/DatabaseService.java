package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseService {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RecetaService recetaService;
    @Autowired
    private ComentarioService comentarioService;
    @Autowired
    ImagenService imagenService;


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
        DESCRIPCION_DE_COMENTARIO_NULA,
        TITULO_DE_COMENTARIO_NULO,
        COMENTARIO_REPETIDO,
        NUMERO_DE_PASO_INCORRECTO,
        NUMERO_DE_PASO_REPETIDO,
        IMAGEN_ERRONEA,
        ERRROR_DESCONOCIDO
    }

    public void eliminarTodos() {
        comentarioService.eliminarTodos();
        recetaService.eliminarTodos();
        usuarioService.eliminarTodos();
        imagenService.eliminarTodos();
    }
}
