package com.TheJavaCooker.CookingWithJavaInternalService.Controllers;

import com.TheJavaCooker.CookingWithJavaInternalService.PersonalDebug;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/createTXT")
public class CreateTXTController {

    @PostMapping(name = "/", consumes = "application/json")
    @ResponseBody
    public String createTXT(@RequestBody String recipeJSON) {
        JsonNode rootNode;
        int numSteps;
        String recipeAsTxt;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.readTree(recipeJSON);
        } catch (final Exception exception) {
            PersonalDebug.printMsg(
                    "TXT Error reading message: Message: " + recipeJSON + " Exception: " + exception.toString());
            return "";
        }
        try {
            JsonNode creatorNode = rootNode.get("creator");
            numSteps = rootNode.get("steps_number").asInt();
            recipeAsTxt = "Recipe: " + "\nRecipe Name: " + rootNode.get("recipe_name").asText() + "\nFood Type: "
                    + rootNode.get("food_type").asText() + "\nDifficulty Level: "
                    + rootNode.get("difficulty_level").asText() + "\nNumber of comments: "
                    + rootNode.get("number_comments").asText() + "\nNumber of favorites: "
                    + rootNode.get("number_favorites").asText() + "\nTotal Duration: "
                    + rootNode.get("total_duration").asText() + "\n\nCreator: " + "\n\tUser Name: "
                    + creatorNode.get("user_name").asText() + "\n\tComplete Name: "
                    + creatorNode.get("complete_name").asText() + "\n\tmail: " + creatorNode.get("mail").asText()
                    + "\n\n Number of Steps: " + numSteps + ":";
        } catch (final Exception exception) {
            PersonalDebug.printMsg(
                    "TXT Error parsing root message: Message: " + recipeJSON + " Exception: " + exception.toString());
            return "";
        }
        try {
            JsonNode stepsNode = rootNode.get("steps");
            for (int i = 1; i <= numSteps; ++i) {
                JsonNode paso = stepsNode.get("step-" + i);
                recipeAsTxt += "\nStep - " + i + ":" + "\n\tDuration: " + paso.get("duration").asText()
                        + "\n\tDescription: " + paso.get("description").asText();
            }
        } catch (final Exception exception) {
            PersonalDebug.printMsg(
                    "TXT Error parsing steps: Message: " + recipeJSON + " Exception: " + exception.toString());
            return "";
        }
        try {
            int numIngredients = rootNode.get("ingredients_number").asInt();
            recipeAsTxt += "\n\nnumIngredients: " + numIngredients + ":";
            JsonNode ingredientsNode = rootNode.get("ingredients");
            for (int i = 1; i <= numIngredients; ++i) {
                JsonNode ingredient = ingredientsNode.get("ingredient-" + i);
                recipeAsTxt += "\nIngredient-" + i + ":" + "\n\tIngredient Name: "
                        + ingredient.get("ingredient_name").asText() + "\n\tIngredient Amount: "
                        + ingredient.get("ingredient_amount").asText();
            }
        } catch (final Exception exception) {
            PersonalDebug.printMsg(
                    "TXT Error parsing ingredients: Message: " + recipeJSON + " Exception: " + exception.toString());
            return "";
        }
        try {
            int numTools = rootNode.get("tool_number").asInt();
            recipeAsTxt += "\n\nnum Tools: " + numSteps + ":";
            JsonNode toolsNode = rootNode.get("tools");
            for (int i = 1; i <= numTools; ++i) {
                JsonNode tool = toolsNode.get("tool-" + i);
                recipeAsTxt += "\nTool-" + i + ":" + "\n\tTool Name: " + tool.get("tool_name").asText()
                        + "\n\tDifficulty Level: " + tool.get("difficulty_level").asText();

            }
            return recipeAsTxt;
        } catch (final Exception exception) {
            PersonalDebug.printMsg(
                    "TXT Error parsing tools: Message: " + recipeJSON + " Exception: " + exception.toString());
            return "";
        }
    }
}
