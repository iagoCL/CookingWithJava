package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.*;
import org.springframework.data.util.Pair;

import java.util.Iterator;
import java.util.List;

public class DatabaseTests {
    private DatabaseManager database;
    private UsuarioRepository usuarioRepository;
    private RecetaRepository recetaRepository;

    private static final String nombreUsuario = "nombreUsuarioEjemplo";
    private static final String correoElectronico = "correoDeEjemplo@example.com";
    private static final String contrasena = "contraseñaDeEjemplo";
    private static final String nombreApellidos = "NombreDeEjemplo ApellidosDeEjemplo";

    private static final String nuevoNombreUsuario = "nuevoNombreUsuarioEjemplo";
    private static final String nuevoCorreoElectronico = "nuevoCorreoDeEjemplo@example.com";
    private static final String nuevaContrasena = "nuevaContraseñaDeEjemplo";
    private static final String nuevosNombreApellidos = "NuevoNombreDeEjemplo nuevosApellidosDeEjemplo";

    private static final String nombreReceta = "nombre de Receta de Ejemplo";
    private String tipoDePlato;

    private static final String nuevoNombreReceta = "nuevo Nombre de Receta de Ejemplo";
    private String nuevoTipoDePlato;

    private Usuario usuarioEjemploA;
    private Usuario usuarioEjemploB;

    private Receta recetaEjemploA;
    private Receta recetaEjemploB;

    public DatabaseTests(DatabaseManager database_) {
        this.database = database_;
        usuarioRepository = database.getUsuarioRepository();
        recetaRepository = database.getRecetaRepository();
    }

    public boolean testCompleto() {
        nuevoTipoDePlato = database.getTipoDePlatoAleatorio();
        tipoDePlato = database.getTipoDePlatoAleatorio();
        database.crearUsuariosEjemplo(15);
        usuarioEjemploA = database.getUsuarioAletorio();
        database.crearRecetasEjemplo(25);
        recetaEjemploA = database.getRecetaAletoria();

        if (!testUsuarios()) {
            return false;
        } else if (!testRecetas()) {
            return false;
        } else if (!testFavoritos()) {
            return false;
        } else {
            PersonalDebug.imprimir("TODOS LOS TEST SUPERADOS.");
            return true;
        }

    }

    private boolean testRecetas() {
        Pair<DatabaseManager.Errores, Receta> p = database.crearReceta(nombreReceta, tipoDePlato, usuarioEjemploB);
        if (p.getFirst() != DatabaseManager.Errores.SIN_ERRORES) {
            PersonalDebug.imprimir("ERROR: SE DEBERIA PODER INSERTAR");
            return false;
        }
        Receta recetaDeEjemplo = p.getSecond();
        if (database.crearReceta(nombreReceta, "XXX", usuarioEjemploB).getFirst() != DatabaseManager.Errores.NOMBRE_RECETA_REPETIDO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE REPETIDO");
            return true;
        } else {
            PersonalDebug.imprimir("Detectado nombre de receta repetido");
        }
        if (database.crearReceta("", "XXX", usuarioEjemploB).getFirst() != DatabaseManager.Errores.NOMBRE_RECETA_NULO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE DE RECETA NULO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado nombre de receta nulo");
        }
        if (database.crearReceta("XXX", "", usuarioEjemploB).getFirst() != DatabaseManager.Errores.TIPO_PLATO_RECETA_NULO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL TIPO DE PLATO DE LA RECETA NULO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado tipo de plato de la receta nulo");
        }

        recetaEjemploB = recetaRepository.buscarPorNombreReceta(nombreReceta);
        if (recetaDeEjemplo.completeEquals(recetaEjemploB)) {
            PersonalDebug.imprimir("Busqueda por nombre receta correcta");
        } else {
            PersonalDebug.imprimir("ERROR: BUSCANDO POR NOMBRE RECETA, DEBERIAN SER IGUALES");
            return false;
        }

        long numRecetas = recetaRepository.count();
        recetaEjemploB.setNombreReceta(nuevoNombreReceta);
        recetaEjemploB.setTipoPlato(nuevoTipoDePlato);
        p = database.actualizarOCrearReceta(recetaEjemploB);
        if (p.getFirst() != DatabaseManager.Errores.SIN_ERRORES) {
            PersonalDebug.imprimir("ERROR: SE DEBERÍA PODER ACTUALIZAR");
            return false;
        }
        if (numRecetas != recetaRepository.count()) {
            PersonalDebug.imprimir("ERROR: SE HA INSERTADO y NO ACTUALIZADO");
            return false;
        } else {
            PersonalDebug.imprimir("Actualización correcta");
        }

        if (recetaEjemploB.getCreadorDeLaReceta().completeEquals(usuarioEjemploB)) {
            PersonalDebug.imprimir("El usuario ha creado la receta");
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER CREADO LA RECETA");
            return false;
        }
        numRecetas = usuarioEjemploB.getNumRecetasCreadas();
        if (numRecetas > 0) {
            PersonalDebug.imprimir("El usuario: " + usuarioEjemploB.getNombreUsuario() + " ha creado un total de: " + numRecetas);
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER AL MENOS UNA RECETA CREADA");
            return false;
        }
        List<Receta> listaDeRecetas = usuarioEjemploB.getRecetasCreadas();
        for (Receta receta : listaDeRecetas) {
            PersonalDebug.imprimir("El usuario: " + usuarioEjemploB.getNombreUsuario() + " ha creado: " + receta.toString());
        }

        listaDeRecetas = recetaRepository.buscarPorTipoDePlato(nuevoTipoDePlato);
        if (listaDeRecetas.isEmpty()) {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER AL MENOS UNA RECETA DE ESTE TIPO");
            return false;
        } else {
            for (Receta receta : listaDeRecetas) {
                PersonalDebug.imprimir("Recetas por tipo: " + receta.toString());
            }
        }
        PersonalDebug.imprimir("TEST DE RECETAS SUPERADO");
        return true;

    }

    private boolean testFavoritos() {
        if (recetaEjemploA.marcarFavorito(usuarioEjemploA)) {
            PersonalDebug.imprimir("Receta A marcada favorita por usuario A.");
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA PODER MARCARSE COMO FAVORITO.");
            return false;
        }
        if (recetaEjemploA.marcarFavorito(usuarioEjemploA)) {
            PersonalDebug.imprimir("ERROR: RECETA A  NO DETECTADA COMO FAVORITA POR USUARIO B.");
            return false;
        } else {
            PersonalDebug.imprimir("Receta A ya marcada favorita por usuario A.");
        }
        if (recetaEjemploA.marcarFavorito(usuarioEjemploB)) {
            PersonalDebug.imprimir("Receta A marcada favorita por usuario B.");
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA PODER MARCARSE COMO FAVORITA.");
            return false;
        }
        if (recetaEjemploB.marcarFavorito(usuarioEjemploA)) {
            PersonalDebug.imprimir("Receta B marcada favorita por usuario A.");
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA PODER MARCARSE COMO FAVORITA.");
            return false;
        }

        long numRecetas = recetaRepository.count();
        recetaRepository.save(recetaEjemploA);
        recetaRepository.save(recetaEjemploB);
        if (numRecetas == recetaRepository.count()) {
            PersonalDebug.imprimir("Se han actualizado las recetas.");
        } else {
            PersonalDebug.imprimir("ERROR: SE HAN CREADO NUEVAS RECETAS.");
            return false;
        }

        if (usuarioEjemploA.getNumRecetasFavoritas() == 2) {
            PersonalDebug.imprimir("Correcto: el usuario: " + usuarioEjemploA.getNombreUsuario() + " tiene 2 recetas favoritas:");
            for (Receta recetaFavorita : usuarioEjemploA.getRecetasFavoritas()) {
                PersonalDebug.imprimir("Receta favorita del usuario : " + usuarioEjemploA.getNombreUsuario() + ": " + recetaFavorita.toString());
            }
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER 2 RECETAS FAVORITAS.");
            return false;
        }
        if (usuarioEjemploB.getNumRecetasFavoritas() == 1) {
            PersonalDebug.imprimir("Correcto: el usuario B tiene 1 receta favorita:");
            for (Receta recetaFavorita : usuarioEjemploB.getRecetasFavoritas()) {
                PersonalDebug.imprimir("Receta favorita del usuario: " + usuarioEjemploB.getNombreUsuario() + ": " + recetaFavorita.toString());
            }
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER 2 RECETAS FAVORITAS.");
            return false;
        }
        if (recetaEjemploA.getNumFavoritos() == 2) {
            PersonalDebug.imprimir("Correcto numero de favoritos.");
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER 2 RECETAS FAVORITAS.");
            return false;
        }
        if (recetaEjemploB.getNumFavoritos() == 1) {
            PersonalDebug.imprimir("Correcto numero de favoritos.");
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER 1 FAVORITO.");
            return false;
        }


        database.crearFavoritosAleatorios(25);
        PersonalDebug.imprimir("TEST DE FAVORITOS CORRECTO");
        return true;
    }

    private boolean testUsuarios() {
        Pair<DatabaseManager.Errores, Usuario> p = database.crearUsuario(nombreUsuario, contrasena, correoElectronico, nombreApellidos);
        if (p.getFirst() != DatabaseManager.Errores.SIN_ERRORES) {
            PersonalDebug.imprimir("ERROR: SE DEBERIA PODER INSERTAR");
            return false;
        }
        Usuario usuarioDeEjemplo = p.getSecond();
        if (database.crearUsuario(nombreUsuario, "XXX", "XXX", "XXX").getFirst() != DatabaseManager.Errores.NOMBRE_USUARIO_REPETIDO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE REPETIDO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado nombre de usuario repetido");
        }
        if (database.crearUsuario("XXX", "XXX", correoElectronico, "XXX").getFirst() != DatabaseManager.Errores.CORREO_ELECTRONICO_REPETIDO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL CORREO ELECTRONICO REPETIDO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado correo electronico repetido");
        }
        if (database.crearUsuario("", "XXX", "XXX", "XXX").getFirst() != DatabaseManager.Errores.NOMBRE_USUARIO_NULO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE DE USUARIO NULO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado nombre de usuario nulo");
        }
        if (database.crearUsuario("XXX", "", "XXX", "XXX").getFirst() != DatabaseManager.Errores.CONTRASENA_NULA) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA LA CONTRASEÑA NULA");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado contraseña  nula");
        }
        if (database.crearUsuario("XXX", "XXX", "", "XXX").getFirst() != DatabaseManager.Errores.CORREO_ELECTRONICO_NULO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL CORREO ELECTRONICO NULO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado correo electronico nulo");
        }
        if (database.crearUsuario("XXX", "XXX", "XXX", "").getFirst() != DatabaseManager.Errores.NOMBRE_APELLIDOS_NULOS) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA LOS NOMBRES Y APELLIDOS NULOS");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado correo electronico nulo");
        }
        usuarioEjemploB = usuarioRepository.buscarPorCorreoElectronico(correoElectronico);
        if (usuarioDeEjemplo.completeEquals(usuarioEjemploB)) {
            PersonalDebug.imprimir("Busqueda por correo correcta");
        } else {
            PersonalDebug.imprimir("ERROR: BUSCANDO POR CORREO ELECTRONICO, DEBERIAN SER IGUALES");
            return false;
        }
        usuarioEjemploB = usuarioRepository.buscarPorNombreUsuario(nombreUsuario);
        if (usuarioDeEjemplo.completeEquals(usuarioEjemploB)) {
            PersonalDebug.imprimir("Busqueda por nombre correcta");
        } else {
            PersonalDebug.imprimir("ERROR: BUSCANDO POR NOMBRE, DEBERIAN SER IGUALES");
            return false;
        }


        long numUsuarios = usuarioRepository.count();
        usuarioEjemploB.setContrasena(nuevaContrasena);
        usuarioEjemploB.setCorreoElectronico(nuevoCorreoElectronico);
        usuarioEjemploB.setNombreUsuario(nuevoNombreUsuario);
        usuarioEjemploB.setNombreApellidos(nuevosNombreApellidos);
        p = database.actualizarOCrearUsuario(usuarioEjemploB);
        if (p.getFirst() != DatabaseManager.Errores.SIN_ERRORES) {
            PersonalDebug.imprimir("ERROR: SE DEBERÍA PODER INSERTAR EL USUARIO");
            return false;
        }
        if (numUsuarios != usuarioRepository.count()) {
            PersonalDebug.imprimir("ERROR: SE HA INSERTADO y NO ACTUALIZADO");
            return false;
        } else {
            PersonalDebug.imprimir("Actualización correcta");
        }
        PersonalDebug.imprimir("TEST DE USUARIOS SUPERADO");
        return true;
    }
}
