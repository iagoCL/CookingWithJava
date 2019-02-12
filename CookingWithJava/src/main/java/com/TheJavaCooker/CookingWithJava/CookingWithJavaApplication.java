package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CookingWithJavaApplication {
    @Autowired
    private DatabaseManager databaseManager;
    private static boolean activarDebug = false;
    private static boolean clearDatabase = false;
    private static boolean realizarTests = false;
    private static int crearUsuarios = 0;
    private static int crearComentarios = 0;
    private static int crearRececetas = 0;
    private static int crearFavoritos = 0;

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-d") || args[i].equals("--debug")) {
                activarDebug = true;
            } else if (args[i].equals( "-t") || args[i].equals( "--test")) {
                realizarTests = true;
            } else if (args[i].equals( "-c") || args[i].equals( "--clear")) {
                clearDatabase = true;
            } else if (args[i].equals( "-r") || args[i].equals( "--randomData")) {
                activarDebug = true;
                clearDatabase = true;
                crearUsuarios += 10;
                crearRececetas += 20;
                crearComentarios += 25;
                crearFavoritos += 30;
            } else if (args[i].startsWith("--usuarioRandom")) {
                crearUsuarios += Integer.parseInt(args[i].substring("--usuarioRandom".length()));
            }else if (args[i].startsWith("--recetaRandom")) {
                crearRececetas += Integer.parseInt(args[i].substring("--recetaRandom".length()));
            }else if (args[i].startsWith("--favoritosRandom")) {
                crearFavoritos += Integer.parseInt(args[i].substring("--favoritosRandom".length()));
            }else if (args[i].startsWith("--comentariosRandom")) {
                crearComentarios += Integer.parseInt(args[i].substring("--comentariosRandom".length()));
            } else{
                System.out.println("ERROR: [-d][--debug]" +
                        " [-t][--test] [-r][--randomData]" +
                        " [-c][--clear] [--usuarioRandomXX]" +
                        "[--recetaRandomXX] [-favoritoRandomXX]" +
                        " [-comentariosRandomXX]");
                System.exit(-1);
            }
        }
        SpringApplication.run(CookingWithJavaApplication.class, args);
    }

    @PostConstruct
    public void init() {
        PersonalDebug.setDebug(activarDebug);
        PersonalDebug.imprimir("Ejecución con:\nDebug: "+activarDebug
                +"\ntesteo: "+realizarTests
                +"\nclearDatabase: "+clearDatabase
                +"\ncrearUsuarios: "+crearUsuarios
                +"\ncrearRecetas: "+crearRececetas
                +"\ncrearFavoritos: "+crearFavoritos
                +"\ncrearComentarios: "+crearComentarios);
        if(clearDatabase){
            databaseManager.clear();
        }
        if(realizarTests) {
            DatabaseTests databaseTests = new DatabaseTests(databaseManager);
            databaseTests.testCompleto();
        }
        DatabaseRandomData databaseRandomData = new DatabaseRandomData(databaseManager);
        if(crearUsuarios>0)
        {
            databaseRandomData.crearUsuariosEjemplo(crearUsuarios);
        }
        if(crearRececetas>0)
        {
            databaseRandomData.crearRecetasEjemplo(crearRececetas);
        }
        if(crearComentarios>0)
        {
            databaseRandomData.crearComentariosEjemplo(crearComentarios);
        }
        if(crearFavoritos>0)
        {
            databaseRandomData.crearFavoritosAleatorios(crearFavoritos);
        }
        PersonalDebug.imprimir("APLICACION INICIADA");
        //*/
    }

}

