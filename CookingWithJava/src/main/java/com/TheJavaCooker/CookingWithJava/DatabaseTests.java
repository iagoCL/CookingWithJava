package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DatabaseTests {
    private DatabaseManager database;
    private DatabaseRandomData databaseRandomData;
    private UsuarioRepository usuarioRepository;
    private RecetaRepository recetaRepository;
    private IngredienteRepository ingredienteRepository;
    private UtensilioRepository utensilioRepository;
    private ComentarioRepository comentarioRepository;
    private PasoRepository pasoRepository;

    private static final String nombreUsuario = "nombreUsuarioEjemplo";
    private static final String correoElectronico = "correoDeEjemplo@example.com";
    private static final String contrasena = "contraseñaDeEjemplo";
    private static final String nombreApellidos = "NombreDeEjemplo ApellidosDeEjemplo";

    private static final String nuevoNombreUsuario = "nuevoNombreUsuarioEjemplo";
    private static final String nuevoCorreoElectronico = "nuevoCorreoDeEjemplo@example.com";
    private static final String nuevaContrasena = "nuevaContraseñaDeEjemplo";
    private static final String nuevosNombreApellidos = "NuevoNombreDeEjemplo nuevosApellidosDeEjemplo";

    private static final String tituloDeComentario = "titulo de Comentario de Ejemplo";
    private static final String descripcionDeComentario = "Descripcion de comentario de ejemplo.";

    private static final String nombreReceta = "nombre de Receta de Ejemplo";
    private String tipoDePlato;
    private String nivelDeDificultad;

    private static final String nuevoNombreReceta = "nuevo Nombre de Receta de Ejemplo";
    private String nuevoTipoDePlato;
    private String nuevoNivelDeDificultad;

    private Usuario usuarioEjemploA;
    private Usuario usuarioEjemploB;

    private Receta recetaEjemploA;
    private Receta recetaEjemploB;

    private List<Pair<String, String>> listaIngredientes;
    private List<Pair<String, String>> listaUtensilios;
    private List<Pair<Integer, String>> listaPasos;

    public DatabaseTests(DatabaseManager database_) {
        this.database = database_;
        usuarioRepository = database.getUsuarioRepository();
        recetaRepository = database.getRecetaRepository();
        ingredienteRepository = database.getIngredienteRepository();
        comentarioRepository = database.getComentarioRepository();
        pasoRepository = database.getPasoRepository();
        utensilioRepository = database.getUtensilioRepository();
        databaseRandomData = new DatabaseRandomData(database);
        listaIngredientes = DatabaseRandomData.getListaDeIngredientesAleatorios();
        listaUtensilios = DatabaseRandomData.getListaDeUtensiliosAleatorios();
        listaPasos = DatabaseRandomData.getListaDePasosAleatorios();
    }

    public boolean testCompleto() {

        nuevoTipoDePlato = DatabaseRandomData.getTipoDePlatoAleatorio();
        tipoDePlato = DatabaseRandomData.getTipoDePlatoAleatorio();
        nivelDeDificultad = DatabaseRandomData.getNivelDeDificultadAleatorio();
        nuevoNivelDeDificultad = DatabaseRandomData.getNivelDeDificultadAleatorio();
        databaseRandomData.crearUsuariosEjemplo(15);
        usuarioEjemploA = databaseRandomData.getUsuarioAletorio();
        databaseRandomData.crearRecetasEjemplo(75);
        recetaEjemploA = databaseRandomData.getRecetaAletoria();

        if (!testUsuarios()) {
            return false;
        } else if (!testIngredientes()) {
            return false;
        } else if (!testUtensilios()) {
            return false;
        } else if (!testDePasos()) {
            return false;
        } else if (!testRecetas()) {
            return false;
        } else if (!testFavoritos()) {
            return false;
        } else if (!testComentarios()) {
            return false;
        } else {
            PersonalDebug.imprimir(recetaEjemploB.mostrarMultilinea());
            Iterable<Receta> it = database.buscarReceta(1,
                    2,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "nombre r",
                    null,
                    null,
                    null);
            PersonalDebug.imprimir("PRIMERA QUERY");
            it.forEach((r) -> {
                PersonalDebug.imprimir(r.toString());
            });
            it = database.buscarReceta(1,
                    4,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "PesCado",
                    null,
                    null,
                    null,
                    null);
            PersonalDebug.imprimir("SEGUNDA QUERY");
            it.forEach((r) -> {
                PersonalDebug.imprimir(r.toString());
            });
            PersonalDebug.imprimir("ULTIMAS RECETAS");
            recetaRepository.ultimasRecentas(PageRequest.of(1, 10)).forEach((r) -> {
                PersonalDebug.imprimir(r.toString());
            });
            PersonalDebug.imprimir("TODOS LOS TEST SUPERADOS.");
            return true;
        }
    }

    private boolean testDePasos() {
        List<Pair<Integer, String>> listaPasosErronea = new ArrayList<>(1);
        listaPasosErronea.add(Pair.of(0, "XXX"));
        if (database.crearReceta("XXX", "XXX", "XXX", listaIngredientes, listaUtensilios, listaPasosErronea, usuarioEjemploB).getFirst() != DatabaseManager.Errores.TIEMPO_DE_PASO_INCORRECTO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA LA DURACION DE PASO NULA");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado duracion de paso nula");
        }
        listaPasosErronea.clear();
        listaPasosErronea.add(Pair.of(77, ""));
        if (database.crearReceta("XXX", "XXX", "XXX", listaIngredientes, listaUtensilios, listaPasosErronea, usuarioEjemploB).getFirst() != DatabaseManager.Errores.DESCRIPCION_DE_PASO_NULA) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA LA DESCRIPCION DEL PASO NULA");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado descripcion de paso nulo");
        }

        PersonalDebug.imprimir("PRUEBA DE PASOS SUPERADA");
        return true;
    }

    private boolean testIngredientes() {
        List<Pair<String, String>> listaIngredientesErronea = new ArrayList<>(1);
        listaIngredientesErronea.add(Pair.of("", "XXX"));
        if (database.crearReceta("XXX", "XXX", "XXX", listaIngredientesErronea, listaUtensilios, listaPasos, usuarioEjemploB).getFirst() != DatabaseManager.Errores.NOMBRE_DE_INGREDIENTE_NULO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE DE INGREDIENTE NULO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado nombre de ingrediente nulo");
        }
        listaIngredientesErronea.clear();
        listaIngredientesErronea.add(Pair.of("XXX", ""));
        if (database.crearReceta("XXX", "XXX", "XXX", listaIngredientesErronea, listaUtensilios, listaPasos, usuarioEjemploB).getFirst() != DatabaseManager.Errores.CANTIDAD_DE_INGREDIENTE_NULA) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA LA CANTIDAD DE INGREDIENTE NULA");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado cantidad de ingrediente nula");
        }
        listaIngredientesErronea.clear();
        listaIngredientesErronea.add(Pair.of("XXX", "XXX"));
        listaIngredientesErronea.add(Pair.of("XXX", "XXX"));
        if (database.crearReceta("XXX", "XXX", "XXX", listaIngredientesErronea, listaUtensilios, listaPasos, usuarioEjemploB).getFirst() != DatabaseManager.Errores.NOMBRE_DE_INGREDIENTE_REPETIDO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE DE INGREDIENTE REPETIDO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado nombre de ingrediente repetido");
        }
        List<String> ingredientesUsados = ingredienteRepository.todosLosIngredientes();
        if (ingredientesUsados.isEmpty() && !ingredientesUsados.isEmpty()) {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER AL MENOS UN INGREDIENTE");
            return false;
        } else {
            PersonalDebug.imprimir("tipos de ingrediente usados: " + ingredientesUsados);
        }

        PersonalDebug.imprimir("PRUEBA DE INGREDIENTES SUPERADA");
        return true;
    }

    private boolean testUtensilios() {
        List<Pair<String, String>> listaUtensiliosErronea = new ArrayList<>(1);
        listaUtensiliosErronea.add(Pair.of("", "XXX"));
        if (database.crearReceta("XXX", "XXX", "XXX", listaIngredientes, listaUtensiliosErronea, listaPasos, usuarioEjemploB).getFirst() != DatabaseManager.Errores.NOMBRE_DE_UTENSILIO_NULO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE DE UTENSILO NULO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado nombre de utensilio nulo");
        }
        listaUtensiliosErronea.clear();
        listaUtensiliosErronea.add(Pair.of("XXX", "XXX"));
        listaUtensiliosErronea.add(Pair.of("XXX", "XXX"));
        if (database.crearReceta("XXX", "XXX", "XXX", listaIngredientes, listaUtensiliosErronea, listaPasos, usuarioEjemploB).getFirst() != DatabaseManager.Errores.NOMBRE_DE_UTENSILIO_REPETIDO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE DE utensilios REPETIDO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado nombre de utensilio repetido");
        }
        List<String> untensiliosUsados = utensilioRepository.todosLosUtensilios();
        if (untensiliosUsados.isEmpty() && !untensiliosUsados.isEmpty()) {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER AL MENOS UN UTENSILIO");
            return false;
        } else {
            PersonalDebug.imprimir("tipos de utensilios usados: " + untensiliosUsados);
        }

        PersonalDebug.imprimir("PRUEBA DE UTENSILIOS SUPERADA");
        return true;
    }

    private boolean testRecetas() {
        //Comprobando Recetas
        Pair<DatabaseManager.Errores, Receta> p = database.crearReceta(nombreReceta, tipoDePlato, nivelDeDificultad, listaIngredientes, listaUtensilios, listaPasos, usuarioEjemploB);
        if (p.getFirst() != DatabaseManager.Errores.SIN_ERRORES) {
            PersonalDebug.imprimir("ERROR: SE DEBERIA PODER INSERTAR");
            return false;
        }
        Receta recetaDeEjemplo = p.getSecond();
        PersonalDebug.imprimir("Creada la receta:\n " + recetaDeEjemplo.mostrarMultilinea());
        if (database.crearReceta(nombreReceta, "XXX", "XXX", listaIngredientes, listaUtensilios, listaPasos, usuarioEjemploB).getFirst() != DatabaseManager.Errores.NOMBRE_RECETA_REPETIDO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE REPETIDO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado nombre de receta repetido");
        }
        if (database.crearReceta("", "XXX", "XXX", listaIngredientes, listaUtensilios, listaPasos, usuarioEjemploB).getFirst() != DatabaseManager.Errores.NOMBRE_RECETA_NULO) {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE DE RECETA NULO");
            return false;
        } else {
            PersonalDebug.imprimir("Detectado nombre de receta nulo");
        }
        if (database.crearReceta("XXX", "", "XXX", listaIngredientes, listaUtensilios, listaPasos, usuarioEjemploB).getFirst() != DatabaseManager.Errores.TIPO_PLATO_RECETA_NULO) {
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
        recetaEjemploB.setNivelDificultad(NivelDeDificultad.fromString(nuevoNivelDeDificultad));
        p = database.actualizarReceta(recetaEjemploB);
        if (p.getFirst() != DatabaseManager.Errores.SIN_ERRORES) {
            PersonalDebug.imprimir("ERROR: SE DEBERÍA PODER ACTUALIZAR");
            return false;
        }
        if (numRecetas != recetaRepository.count()) {
            PersonalDebug.imprimir("ERROR: SE HA INSERTADO y NO ACTUALIZADO");
            return false;
        } else {
            PersonalDebug.imprimir("Actualización correcta.");
            PersonalDebug.imprimir("Creada la receta:\n " + recetaDeEjemplo.mostrarMultilinea());
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
        Set<Receta> setDeRecetas = usuarioEjemploB.getRecetasCreadas();
        for (Receta receta : setDeRecetas) {
            PersonalDebug.imprimir("El usuario: " + usuarioEjemploB.getNombreUsuario() + " ha creado: " + receta.toString());
        }

        List<Receta> listaDeRecetas = recetaRepository.buscarPorTipoDePlato(nuevoTipoDePlato);
        if (listaDeRecetas.isEmpty()) {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER AL MENOS UNA RECETA DE ESTE TIPO");
            return false;
        } else {
            for (Receta receta : listaDeRecetas) {
                PersonalDebug.imprimir("Recetas por tipo: " + receta.toString());
            }
        }
        List<String> tiposDePlato = recetaRepository.tiposDePlato();
        if (tiposDePlato.isEmpty()) {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER AL MENOS UN TIPO DE PLATO");
            return false;
        } else {
            PersonalDebug.imprimir("tipos de receta: " + tiposDePlato);
        }

        PersonalDebug.imprimir("TEST DE RECETAS SUPERADO");
        return true;

    }

    private boolean testFavoritos() {
        long numRecetas = recetaRepository.count();
        if (database.marcarFavorito(usuarioEjemploB, recetaEjemploA)) {
            PersonalDebug.imprimir("Receta A marcada favorita por usuario B.");
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA PODER MARCARSE COMO FAVORITA.");
            return false;
        }
        if (database.marcarFavorito(usuarioEjemploA, recetaEjemploA)) {
            PersonalDebug.imprimir("Receta A marcada favorita por usuario A.");
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA PODER MARCARSE COMO FAVORITO.");
            return false;
        }
        if (database.marcarFavorito(usuarioEjemploA, recetaEjemploA)) {
            PersonalDebug.imprimir("ERROR: RECETA A  NO DETECTADA COMO FAVORITA POR USUARIO B.");
            return false;
        } else {
            PersonalDebug.imprimir("Receta A ya marcada favorita por usuario A.");
        }
        if (database.marcarFavorito(usuarioEjemploA, recetaEjemploB)) {
            PersonalDebug.imprimir("Receta B marcada favorita por usuario A.");
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA PODER MARCARSE COMO FAVORITA.");
            return false;
        }
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

        databaseRandomData.crearFavoritosAleatorios(15);
        PersonalDebug.imprimir("TEST DE FAVORITOS CORRECTO");
        return true;
    }

    private boolean testComentarios() {
        LocalDateTime randomTime = DatabaseRandomData.getRandomDateTime();
        LocalDateTime randomTime2 = randomTime.plusDays(5);
        if (database.crearComentarioConFecha("", "XXX", randomTime, recetaEjemploB, usuarioEjemploB).getFirst() == DatabaseManager.Errores.DESCRIPCION_DE_COMENTARIO_NULA) {
            PersonalDebug.imprimir("Detectada descripción de comentario nula.");
        } else {
            PersonalDebug.imprimir("ERROR: DESCRIPCION DE COMENTARIO NULA NO DETECTADA.");
            return false;
        }
        if (database.crearComentarioConFecha("XXX", "", randomTime, recetaEjemploB, usuarioEjemploB).getFirst() == DatabaseManager.Errores.TITULO_DE_COMENTARIO_NULO) {
            PersonalDebug.imprimir("Detectada titulo de comentario nulo.");
        } else {
            PersonalDebug.imprimir("ERROR: TITULO DE COMENTARIO NULO NO DETECTADA.");
            return false;
        }
        if (database.crearComentarioConFecha(descripcionDeComentario + 1, tituloDeComentario + 1, randomTime, recetaEjemploB, usuarioEjemploB).getFirst() == DatabaseManager.Errores.SIN_ERRORES) {
            PersonalDebug.imprimir("Creado nuevo comentario.");
        } else {
            PersonalDebug.imprimir("ERROR: SE DEBERIA PODER COMENTAR.");
            return false;
        }
        if (database.crearComentarioConFecha(descripcionDeComentario + 2, tituloDeComentario + 2, randomTime, recetaEjemploB, usuarioEjemploB).getFirst() == DatabaseManager.Errores.COMENTARIO_REPETIDO) {
            PersonalDebug.imprimir("Detectado comentario repetido.");
        } else {
            PersonalDebug.imprimir("ERROR: SE DEBERIA DETECTAR COMENTARIO REPETIDO.");
            return false;
        }
        if (database.crearComentarioConFecha(descripcionDeComentario + 3, tituloDeComentario + 3, randomTime, recetaEjemploB, usuarioEjemploA).getFirst() == DatabaseManager.Errores.SIN_ERRORES) {
            PersonalDebug.imprimir("Creado nuevo comentario.");
        } else {
            PersonalDebug.imprimir("ERROR: SE DEBERIA PODER COMENTAR.");
            return false;
        }
        if (database.crearComentarioConFecha(descripcionDeComentario + 4, tituloDeComentario + 4, randomTime2, recetaEjemploB, usuarioEjemploA).getFirst() == DatabaseManager.Errores.SIN_ERRORES) {
            PersonalDebug.imprimir("Creado nuevo comentario.");
        } else {
            PersonalDebug.imprimir("ERROR: SE DEBERIA PODER COMENTAR.");
            return false;
        }
        if (database.crearComentarioConFecha(descripcionDeComentario + 5, tituloDeComentario + 5, randomTime, recetaEjemploA, usuarioEjemploB).getFirst() == DatabaseManager.Errores.SIN_ERRORES) {
            PersonalDebug.imprimir("Creado nuevo comentario.");
        } else {
            PersonalDebug.imprimir("ERROR: SE DEBERIA PODER COMENTAR.");
            return false;
        }
        if (recetaEjemploA.getComentarios().size() == 1) {
            PersonalDebug.imprimir("Correcto numero de favoritos.");
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER 1 COMENTARIO.");
            return false;
        }
        if (recetaEjemploB.getComentarios().size() == 3) {
            PersonalDebug.imprimir("Correcto numero de favoritos.");
        } else {
            PersonalDebug.imprimir("ERROR: DEBERÍA HABER 2 FAVORITO.");
            return false;
        }

        databaseRandomData.crearComentariosEjemplo(25);
        Set<Comentario> comentarios = recetaEjemploB.getComentarios();
        for (Comentario comentario : comentarios) {
            PersonalDebug.imprimir("comentario: " + comentario);
        }
        PersonalDebug.imprimir("TEST DE COMENTARIOS CORRECTO");
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
        p = database.actualizarUsuario(usuarioEjemploB);
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
