package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseRandomData;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CookingWithJavaApplication {
    @Autowired
    private DatabaseService databaseService;
    private static boolean activarDebug = false;
    private static boolean clearDatabase = false;
    private static int crearUsuarios = 0;
    private static int crearComentarios = 0;
    private static int crearRececetas = 0;
    private static int crearFavoritos = 0;

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-d") || args[i].equals("--debug")) {
                activarDebug = true;
            } else if (args[i].equals("-c") || args[i].equals("--clear")) {
                clearDatabase = true;
            } else if (args[i].equals("-r") || args[i].equals("--randomData")) {
                activarDebug = true;
                clearDatabase = true;
                crearUsuarios += 3;
                crearRececetas += 10;
                crearComentarios += 8;
                crearFavoritos += 8;
            } else if (args[i].startsWith("--usuariosRandom")) {
                crearUsuarios += Integer.parseInt(args[i].substring("--usuariosRandom".length()));
            } else if (args[i].startsWith("--recetasRandom")) {
                crearRececetas += Integer.parseInt(args[i].substring("--recetasRandom".length()));
            } else if (args[i].startsWith("--favoritosRandom")) {
                crearFavoritos += Integer.parseInt(args[i].substring("--favoritosRandom".length()));
            } else if (args[i].startsWith("--comentariosRandom")) {
                crearComentarios += Integer.parseInt(args[i].substring("--comentariosRandom".length()));
            } else {
                System.out.println("ERROR: [-d][--debug]" +
                        " [-t][--test] [-r][--randomData]" +
                        " [-c][--clear] [--usuariosRandomXX]" +
                        "[--recetasRandomXX] [--favoritosRandomXX]" +
                        " [-comentariosRandomXX]");
                //System.exit(-1);
            }
        }
        SpringApplication.run(CookingWithJavaApplication.class, args);
    }

    @PostConstruct
    public void init() {
        PersonalDebug.setDebug(activarDebug);
        PersonalDebug.imprimir("Ejecución con:\nDebug: " + activarDebug
                + "\nclearDatabase: " + clearDatabase
                + "\ncrearUsuarios: " + crearUsuarios
                + "\ncrearRecetas: " + crearRececetas
                + "\ncrearFavoritos: " + crearFavoritos
                + "\ncrearComentarios: " + crearComentarios);
        if (clearDatabase) {
            databaseService.eliminarTodos();
        }
        DatabaseRandomData databaseRandomData = new DatabaseRandomData(databaseService);
        if (crearUsuarios > 0) {
            databaseRandomData.crearUsuariosEjemplo(crearUsuarios);
            PersonalDebug.imprimir("NUEVOS USUARIOS CREADOS");
        }
        if (crearRececetas > 0) {
            databaseRandomData.crearRecetasEjemplo(crearRececetas);
            PersonalDebug.imprimir("NUEVAS RECETAS CREADAS");
        }
        if (crearComentarios > 0) {
            databaseRandomData.crearComentariosEjemplo(crearComentarios);
            PersonalDebug.imprimir("NUEVOS COMENTARIOS CREADOS");
        }
        if (crearFavoritos > 0) {
            databaseRandomData.crearFavoritosAleatorios(crearFavoritos);
            PersonalDebug.imprimir("NUEVOS FAVORITOS CREADOS");
        }
        PersonalDebug.imprimir("APLICACION INICIADA.");
    }
}

