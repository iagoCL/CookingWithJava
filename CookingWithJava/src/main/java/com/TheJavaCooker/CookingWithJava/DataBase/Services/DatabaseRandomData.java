package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.CookingWithJavaApplication;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;
import com.TheJavaCooker.CookingWithJava.DataBase.DifficultyLevel;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import org.springframework.data.util.Pair;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseRandomData {
    private UserService userService;
    private RecipeService recipeService;
    private CommentService CommentService;
    private FavoriteService FavoriteService;

    private final static String[] randomRecipeImgPath = { "/randomRecipes/1.jpg", "/randomRecipes/2.jpg",
            "/randomRecipes/3.jpg", "/randomRecipes/4.jpg", "/randomRecipes/5.jpg", "/randomRecipes/6.jpg",
            "/randomRecipes/7.jpg", "/randomRecipes/8.jpg", "/randomRecipes/9.jpg", "/randomRecipes/10.jpg",
            "/randomRecipes/11.jpg", "/randomRecipes/12.jpg" };
    private final static String[] randomUserImgPath = { "/randomUsers/1.jpg", "/randomUsers/2.jpg",
            "/randomUsers/3.jpg", "/randomUsers/4.jpg", "/randomUsers/5.jpg" };

    private final static String[] randomToolName = { "fork", "plate", "oven", "knife", "spoon", "cooker", "ray laser",
            "flame thrower" };
    private final static String[] randomIngredientName = { "chocolate", "flour", "salmon", "pepper", "sugar",
            "pretty things", "xXx XxX xXx", "pork", "potatoes", "carrots", "onion", "garlic", "lamb", "chicken",
            "apple", "blood", "love", "spider", "dragon", "eggs" };

    private final static String[] randomFoodTypeName = { "fish", "meat", "dessert", "Main dish", "soup", "start" };

    private static Random random = new Random();
    private static Lorem lorem = LoremIpsum.getInstance();

    public DatabaseRandomData(DatabaseService databaseService) {
        this.userService = databaseService.getUserService();
        this.recipeService = databaseService.getRecipeService();
        this.CommentService = databaseService.getCommentService();
        this.FavoriteService = databaseService.getFavoriteService();
    }

    public byte[] getRandomUserImage() {

        String pathToFile = randomUserImgPath[random.nextInt(randomUserImgPath.length)];
        return getImageFromPath(pathToFile);
    }

    public byte[] getRandomRecipeImage() {
        String pathToFile = randomRecipeImgPath[random.nextInt(randomRecipeImgPath.length)];
        return getImageFromPath(pathToFile);
    }

    private byte[] getImageFromPath(String pathToFile) {
        try {
            /* Java <=8 */
            URL resource = CookingWithJavaApplication.class.getResource(pathToFile);
            URI uri = resource.toURI();
            Path path = Paths.get(uri);
            return Files.readAllBytes(path);

             /* Java >=9 *
             return getClass().getClassLoader().getResourceAsStream(pathToFile).readAllBytes();//
             //*/
        } catch (Exception exception) {
            PersonalDebug.printMsg("ERROR OPENING IMAGE: " + exception.toString());
            return null;
        }
    }

    public Recipe getRandomRecipe() {
        List<Recipe> recipes = recipeService.allRecipes();
        return recipes.get(random.nextInt(recipes.size()));
    }

    public User getRandomUser() {
        List<User> users = userService.allUsers();
        return users.get(random.nextInt(users.size()));
    }

    public static LocalDate getRandomDate() {
        return LocalDate.of(2014 + random.nextInt(5), 1 + random.nextInt(11), 1 + random.nextInt(26));
    }

    public static LocalDateTime getRandomDateTime() {
        return LocalDateTime.of(2014 + random.nextInt(5), 1 + random.nextInt(11), 1 + random.nextInt(26),
                5 + random.nextInt(18), 5 + random.nextInt(48), 5 + random.nextInt(48), 50 + random.nextInt(488));
    }

    public static String getRandomDifficultyLevel() {
        DifficultyLevel[] levels = DifficultyLevel.values();
        return (levels[random.nextInt(levels.length)]).toString();
    }

    public static String getRandomIngredientAmount() {
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
            return "a bit";
        case 6:
            return "half cup";
        case 7:
            return "the entire package";
        default:
            return "undefined";
        }
    }

    public static String getRandomIngredientName() {
        return getRandomIngredientName(random.nextInt(randomIngredientName.length));
    }

    public static String getRandomIngredientName(int index) {
        try {
            return randomIngredientName[index];
        } catch (Exception exception) {
            return "undefined";
        }
    }

    public static String getRandomToolName() {
        return getRandomToolName(random.nextInt(randomToolName.length));
    }

    public static String getRandomToolName(int index) {
        try {
            return randomToolName[index];
        } catch (Exception exception) {
            return "undefined";
        }
    }

    public static String getRandomDescription() {
        return lorem.getWords(15, 40);
    }

    public static List<Pair<String, String>> getRandomToolList() {
        List<Pair<String, String>> toolsList;
        int[] randomInts = random.ints(0, randomToolName.length).distinct().limit(random.nextInt(randomToolName.length))
                .toArray();
        if (randomInts.length == 0) {
            toolsList = new ArrayList<Pair<String, String>>(1);
            toolsList.add(Pair.of(getRandomToolName(), getRandomDifficultyLevel()));
        } else {
            toolsList = new ArrayList<Pair<String, String>>(randomInts.length);
            for (int randomIndex : randomInts) {
                toolsList.add(Pair.of(getRandomToolName(randomIndex), getRandomDifficultyLevel()));
            }
        }
        return toolsList;
    }

    public static List<Pair<Integer, String>> getRandomStepsList() {
        int steps_number = random.nextInt(12);
        List<Pair<Integer, String>> stepsList = new ArrayList<>(steps_number);
        for (int i = 0; i < steps_number; ++i) {
            stepsList.add(Pair.of(1 + random.nextInt(240), getRandomDescription()));
        }
        return stepsList;
    }

    public static List<Pair<String, String>> getRandomIngredientsList() {
        List<Pair<String, String>> ingredientsList;
        int[] randomInts = random.ints(0, randomIngredientName.length).distinct()
                .limit(random.nextInt(randomIngredientName.length)).toArray();
        if (randomInts.length == 0) {
            ingredientsList = new ArrayList<Pair<String, String>>(1);
            ingredientsList.add(Pair.of(getRandomIngredientName(), getRandomIngredientAmount()));
        } else {
            ingredientsList = new ArrayList<Pair<String, String>>(randomInts.length);
            for (int randomIndex : randomInts) {
                ingredientsList.add(Pair.of(getRandomIngredientName(randomIndex), getRandomIngredientAmount()));
            }
        }
        return ingredientsList;
    }

    public static String getRandomFoodType() {
        return getRandomFoodType(random.nextInt(randomFoodTypeName.length));
    }

    public static String getRandomFoodType(int index) {
        try {
            return randomFoodTypeName[index];
        } catch (Exception exception) {
            return "undefined";
        }
    }

    public void createExampleFavorites(int numFavorites_) {
        for (int i = 0; i < numFavorites_; ++i) {
            FavoriteService.markAsFavorite(getRandomUser(), getRandomRecipe());
        }
    }

    public void createExampleUsers(int numUsers_) {
        for (long i = 1 + userService.getNumUsers(), l = i + numUsers_; i < l; ++i) {
            userService.createUser(lorem.getFirstName() + i, "password" + i, "mail" + i + "@example.com",
                    lorem.getFirstName() + " " + lorem.getLastName() + " " + lorem.getLastName(), getRandomUserImage());
        }
    }

    public void createExampleRecipes(int numRecipes) {
        for (long i = 1 + recipeService.getNumRecipes(), l = i + numRecipes; i < l; ++i) {
            recipeService.createRecipeWithDate("Recipe Name" + i, getRandomFoodType(), getRandomDifficultyLevel(),
                    getRandomDate(), getRandomRecipeImage(), getRandomIngredientsList(), getRandomToolList(),
                    getRandomStepsList(), getRandomUser());
        }
    }

    public void createExampleComments(int numComments) {
        for (int i = 1; i < numComments; ++i) {
            CommentService.createCommentWithDate(getRandomDescription(), "Comment title " + i, getRandomDateTime(),
                    getRandomRecipe(), getRandomUser());
        }
    }
}