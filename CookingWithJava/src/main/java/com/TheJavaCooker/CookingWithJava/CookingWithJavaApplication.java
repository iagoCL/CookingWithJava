package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseRandomData;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.DatabaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import javax.annotation.PostConstruct;

@EnableCaching
@SpringBootApplication
public class CookingWithJavaApplication {
    @Autowired
    private DatabaseService databaseService;
    private static boolean enableDebug = false;
    private static boolean clearDatabase = false;
    private static int createUsers = 0;
    private static int createComments = 0;
    private static int createRecipes = 0;
    private static int createFavorites = 0;
    private static String internalService = "http://127.0.0.1:7000";
    private static String appURL = "https://127.0.0.1:8443";

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-d") || args[i].equals("--debug")) {
                enableDebug = true;
            } else if (args[i].equals("-c") || args[i].equals("--clear")) {
                clearDatabase = true;
            } else if (args[i].startsWith("-urlInternalService")) {
                internalService = args[i].substring("-urlInternalService".length());
            } else if (args[i].startsWith("-urlMainApp")) {
                appURL = args[i].substring("-urlMainApp".length());
            } else if (args[i].startsWith("-urlHazelCast")) {
                HazleCastConfiguration.addNode(args[i].substring("-urlHazelCast".length()));
            } else if (args[i].equals("-r") || args[i].equals("--randomData")) {
                enableDebug = true;
                clearDatabase = true;
                createUsers += 3;
                createRecipes += 10;
                createComments += 8;
                createFavorites += 8;
            } else if (args[i].startsWith("--usersRandom")) {
                createUsers += Integer.parseInt(args[i].substring("--usersRandom".length()));
            } else if (args[i].startsWith("--recipesRandom")) {
                createRecipes += Integer.parseInt(args[i].substring("--recipesRandom".length()));
            } else if (args[i].startsWith("--favoritesRandom")) {
                createFavorites += Integer.parseInt(args[i].substring("--favoritesRandom".length()));
            } else if (args[i].startsWith("--commentsRandom")) {
                createComments += Integer.parseInt(args[i].substring("--commentsRandom".length()));
            } else {
                System.out.println("ERROR: [-d][--debug]" + " [-t][--test] [-r][--randomData]"
                        + " [-c][--clear] [--usersRandomXX]" + "[--recipesRandomXX] [--favoritesRandomXX]"
                        + " [-commentsRandomXX] [-urlInternalService]" + " [-urlHazelCast]");
            }
        }
        InternalServiceClient.setURL(internalService);
        SpringApplication.run(CookingWithJavaApplication.class, args);
    }

    public static String getAppURL() {
        return appURL;
    }

    @PostConstruct
    public void init() {
        PersonalDebug.setDebug(enableDebug);
        PersonalDebug.printMsg("Execution with:\nDebug: " + enableDebug + "\nclearDatabase: " + clearDatabase
                + "\ncreateUsers: " + createUsers + "\ncreateRecipes: " + createRecipes + "\ncreateFavorites: "
                + createFavorites + "\ncreateComments: " + createComments);
        if (clearDatabase) {
            databaseService.deleteAll();
        }
        DatabaseRandomData databaseRandomData = new DatabaseRandomData(databaseService);
        if (createUsers > 0) {
            databaseRandomData.createExampleUsers(createUsers);
            PersonalDebug.printMsg("NEW USERS CREATED");
        }
        if (createRecipes > 0) {
            databaseRandomData.createExampleRecipes(createRecipes);
            PersonalDebug.printMsg("NEW RECIPES CREATED");
        }
        if (createComments > 0) {
            databaseRandomData.createExampleComments(createComments);
            PersonalDebug.printMsg("NEW COMMENTS CREATED");
        }
        if (createFavorites > 0) {
            databaseRandomData.createExampleFavorites(createFavorites);
            PersonalDebug.printMsg("NEW FAVORITES CREATED");
        }
        PersonalDebug.printMsg("APP STARTED");
    }
}