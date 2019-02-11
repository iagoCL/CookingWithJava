package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.*;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseRandomData {
    private DatabaseManager database;
    private UsuarioRepository usuarioRepository;
    private RecetaRepository recetaRepository;

    private final static String[] nombresUtensiliosAleatorios = {
            "centrifugadora",
            "cuchillo",
            "horno",
            "batidora",
            "cuchara",
            "olla",
            "rayo laser",
            "lanzallamas"
    };
    private final static String[] nombresIngredientesAleatorios = {
            "chocolate",
            "harina",
            "lubina",
            "especias",
            "azucar",
            "muchas cosas bonitas",
            "sustancia x",
            "ternera",
            "patatas",
            "zanahoria",
            "cebolla",
            "ajos",
            "cordero",
            "pollo",
            "manzana",
            "sangre",
            "ojos",
            "araña",
            "uranio",
            "madera"
    };

    private final static String[] tiposDePlatoAleatorios = {
            "pescado",
            "carne",
            "postre",
            "plato principal",
            "sopa",
            "aperitivo"
    };

    private static Random random = new Random();
    private static Lorem lorem = LoremIpsum.getInstance();

    public DatabaseRandomData(DatabaseManager database_) {
        this.database = database_;
        usuarioRepository = database.getUsuarioRepository();
        recetaRepository = database.getRecetaRepository();
    }

    public Receta getRecetaAletoria() {
        List<Receta> recetas = recetaRepository.findAll();
        return recetas.get(random.nextInt(recetas.size()));
    }

    public Usuario getUsuarioAletorio() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.get(random.nextInt(usuarios.size()));
    }

    public static LocalDateTime getRandomDateTime(){
        return LocalDateTime.of(
                2014+random.nextInt(5),
                1+random.nextInt(11),
                1+random.nextInt(26),
                5+random.nextInt(18),
                5+random.nextInt(48),
                5+random.nextInt(48),
                50+random.nextInt(488));
    }

    public static String getNivelDeDificultadAleatorio() {
        NivelDeDificultad[] niveles = NivelDeDificultad.values();
        return (niveles[random.nextInt(niveles.length)]).toString();
    }

    public static String getCantidadIngredienteAleatoria() {
        switch (random.nextInt(8)) {
            case 0:
                return random.nextInt(400) + "." + random.nextInt(85) + " mg";
            case 1:
                return random.nextInt(400) + "." + random.nextInt(85) + " g";
            case 2:
                return random.nextInt(15) + "." + random.nextInt(85) + " kg";
            case 3:
                return random.nextInt(700) + "." + random.nextInt(85) + " mL";
            case 4:
                return random.nextInt(25) + "." + random.nextInt(85) + " L";
            case 5:
                return "un poco";
            case 6:
                return "medio vaso";
            case 7:
                return "una pizca";
            default:
                return "undefined";
        }
    }

    public static String getNombreIngredienteAleatorio() {
        return getNombreIngredienteAleatorio(random.nextInt(nombresIngredientesAleatorios.length));
    }

    public static String getNombreIngredienteAleatorio(int index) {
        try {
            return nombresIngredientesAleatorios[index];
        } catch (Exception e) {
            return "undefined";
        }
    }

    public static String getNombreUtensilioAleatorio() {
        return getNombreUtensilioAleatorio(random.nextInt(nombresUtensiliosAleatorios.length));
    }

    public static String getNombreUtensilioAleatorio(int index) {
        try {
            return nombresUtensiliosAleatorios[index];
        } catch (Exception e) {
            return "undefined";
        }
    }

    public static String getDescripcionAleatoria() {
        return lorem.getWords(15, 40);
    }

    public static List<Pair<String, String>> getListaDeUtensiliosAleatorios(){
        List<Pair<String, String>> listaDeUtensiliosAleatorios;
        int[] randomInts = random.ints(0,nombresUtensiliosAleatorios.length).distinct()
                .limit(random.nextInt(nombresUtensiliosAleatorios.length)).toArray();
        if (randomInts.length == 0) {
            listaDeUtensiliosAleatorios = new ArrayList<Pair<String, String>>(1);
            listaDeUtensiliosAleatorios.add(Pair.of(getNombreUtensilioAleatorio(), getNivelDeDificultadAleatorio()));
        } else {
            listaDeUtensiliosAleatorios = new ArrayList<Pair<String, String>>(randomInts.length);
            for (int randomIndex : randomInts) {
                listaDeUtensiliosAleatorios.add(Pair.of(getNombreUtensilioAleatorio(randomIndex), getNivelDeDificultadAleatorio()));
            }
        }
        return listaDeUtensiliosAleatorios;
    }

    public static List<Pair<Integer,String>> getListaDePasosAleatorios(){
        int numPasos = random.nextInt(12);
        List<Pair<Integer,String>> listaPasos = new ArrayList<>(numPasos);
        for(int i = 0;i<numPasos;++i)
        {
            listaPasos.add(Pair.of(1+random.nextInt(240), getDescripcionAleatoria()));
        }
        return listaPasos;
    }

    public static List<Pair<String, String>> getListaDeIngredientesAleatorios() {
        List<Pair<String, String>> listaDeIngredientesAleatorios;
        int[] randomInts = random.ints(0,nombresIngredientesAleatorios.length).distinct()
                .limit(random.nextInt(nombresIngredientesAleatorios.length)).toArray();
        if (randomInts.length == 0) {
            listaDeIngredientesAleatorios = new ArrayList<Pair<String, String>>(1);
            listaDeIngredientesAleatorios.add(Pair.of(getNombreIngredienteAleatorio(), getCantidadIngredienteAleatoria()));
        } else {
            listaDeIngredientesAleatorios = new ArrayList<Pair<String, String>>(randomInts.length);
            for (int randomIndex : randomInts) {
                listaDeIngredientesAleatorios.add(Pair.of(getNombreIngredienteAleatorio(randomIndex), getCantidadIngredienteAleatoria()));
            }
        }
        return listaDeIngredientesAleatorios;
    }

    public static String getTipoDePlatoAleatorio() {
        return getTipoDePlatoAleatorio(random.nextInt(tiposDePlatoAleatorios.length));
    }

    public static String getTipoDePlatoAleatorio(int index) {
        try {
            return tiposDePlatoAleatorios[index];
        } catch (Exception e) {
            return "undefined";
        }
    }

    public void crearFavoritosAleatorios(int numFavoritos_) {
        for (int i = 0; i < numFavoritos_; ++i) {
            database.marcarFavorito(getUsuarioAletorio(), getRecetaAletoria());
        }
    }

    public void crearUsuariosEjemplo(int numUsuarios_) {
        for (int i = 1; i < numUsuarios_; ++i) {
            database.crearUsuario(lorem.getFirstName() + i,
                    "contasena" + i,
                    "correo" + i + "@example.com",
                    lorem.getFirstName() + " "+lorem.getLastName()+" "+lorem.getLastName());
        }
    }

    public void crearRecetasEjemplo(int numRecetas) {
        for (int i = 1; i < numRecetas; ++i) {
            Pair<DatabaseManager.Errores, Receta> p = database.crearReceta("Nombre Receta " + i,
                    getTipoDePlatoAleatorio(),
                    getNivelDeDificultadAleatorio(),
                    getListaDeIngredientesAleatorios(),
                    getListaDeUtensiliosAleatorios(),
                    getListaDePasosAleatorios(),
                    getUsuarioAletorio());
        }
    }

    public void crearComentariosEjemplo(int numComentarios) {
        for (int i = 1; i < numComentarios; ++i) {
            Pair<DatabaseManager.Errores, Comentario> p = database.crearComentarioConFecha(getDescripcionAleatoria(),
                    "Titulo de comentario "+i,
                    getRandomDateTime(),
                    getRecetaAletoria(),
                    getUsuarioAletorio());
        }
    }
}
