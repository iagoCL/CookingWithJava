package com.TheJavaCooker.CookingWithJavaInternalService.Controllers;

import com.TheJavaCooker.CookingWithJavaInternalService.PDFCreator;
import com.TheJavaCooker.CookingWithJavaInternalService.PersonalDebug;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/createPDF")
public class CreatePDFController {
    @PostMapping(name = "/", consumes = "application/json")
    @ResponseBody
    public byte[] createPDF(@RequestBody final String recipeJSON) {
        JsonNode rootNode;
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.readTree(recipeJSON);
        } catch (final Exception exception) {
            PersonalDebug.printMsg(
                    "PDF Error reading message: Message: " + recipeJSON + " Exception: " + exception.toString());
            return new byte[0];
        }
        String recipeName, foodType, totalDuration, creatorName;
        try {
            final JsonNode creatorNode = rootNode.get("creator");
            recipeName = rootNode.get("recipe_name").asText();
            foodType = rootNode.get("food_type").asText();
            totalDuration = rootNode.get("total_duration").asText();
            creatorName = creatorNode.get("user_name").asText();
        } catch (final Exception exception) {
            PersonalDebug
                    .printMsg("PDF Error parsing root: Message: " + recipeJSON + " Exception: " + exception.toString());
            return new byte[0];
        }
        List<String> ingredientsName;
        try {
            final int numIngredients = rootNode.get("ingredients_number").asInt();
            ingredientsName = new ArrayList<>(numIngredients);
            final JsonNode ingredientsNode = rootNode.get("ingredients");
            for (int i = 1; i <= numIngredients; ++i) {
                final JsonNode ingredient = ingredientsNode.get("ingredient-" + i);
                ingredientsName.add(ingredient.get("ingredient_name").asText());
            }
        } catch (final Exception exception) {
            PersonalDebug.printMsg(
                    "PDF Error parsing ingredients: Message: " + recipeJSON + " Exception: " + exception.toString());
            return new byte[0];
        }
        List<String> toolsName;
        try {
            final int numTools = rootNode.get("tool_number").asInt();
            toolsName = new ArrayList<>(numTools);
            final JsonNode toolsNode = rootNode.get("tools");
            for (int i = 1; i <= numTools; ++i) {
                final JsonNode tool = toolsNode.get("tool-" + i);
                toolsName.add(tool.get("tool_name").asText());
            }
        } catch (final Exception exception) {
            PersonalDebug.printMsg(
                    "PDF Error parsing tools: Message: " + recipeJSON + " Exception: " + exception.toString());
            return new byte[0];
        }
        List<String> stepsDescriptions;
        try {
            final int numSteps = rootNode.get("steps_number").asInt();
            stepsDescriptions = new ArrayList<>(numSteps);
            final JsonNode stepsNode = rootNode.get("steps");
            for (int i = 1; i <= numSteps; ++i) {
                final JsonNode paso = stepsNode.get("step-" + i);
                stepsDescriptions.add(paso.get("description").asText());
            }
        } catch (final Exception exception) {
            PersonalDebug.printMsg(
                    "PDF Error parsing steps: Message: " + recipeJSON + " Exception: " + exception.toString());
            return new byte[0];
        }
        try {
            return PDFCreator.createPDF(recipeName, foodType, totalDuration, creatorName, ingredientsName, toolsName,
                    stepsDescriptions);
        } catch (final Exception exception) {
            PersonalDebug.printMsg(
                    "PDF Error generating the file: Message: " + recipeJSON + " Exception: " + exception.toString());
            return new byte[0];
        }
    }

    public static void showReceivedMessage(final JsonNode rootNode) {
        PersonalDebug.printMsg("Original Message: " + rootNode.toString() + "\n\nrecipe:\n\tfood_type: "
                + rootNode.get("food_type").asText() + "\n\trecipe_name: " + rootNode.get("recipe_name").asText()
                + "\n\tdifficulty_level: " + rootNode.get("difficulty_level").asText() + "\n\tnumber_comments: "
                + rootNode.get("number_comments").asText() + "\n\tnumber_favorites: "
                + rootNode.get("number_favorites").asText() + "\n\ttotal_duration: "
                + rootNode.get("total_duration").asText());

        final JsonNode creatorNode = rootNode.get("creator");

        PersonalDebug.printMsg(
                "\nCreator: " + "\n\tuser_name: " + creatorNode.get("user_name").asText() + "\n\tcomplete_name: "
                        + creatorNode.get("complete_name").asText() + "\n\tmail: " + creatorNode.get("mail").asText());

        final int numSteps = rootNode.get("steps_number").asInt();
        PersonalDebug.printMsg("\tsteps_number: " + numSteps);
        final JsonNode stepsNode = rootNode.get("steps");
        for (int i = 1; i <= numSteps; ++i) {
            final JsonNode paso = stepsNode.get("paso-" + i);
            PersonalDebug.printMsg("\nPaso: " + i + "\n\tduration: " + paso.get("duration").asText()
                    + "\n\tdescription: " + paso.get("description").asText());
        }

        final int numIngredients = rootNode.get("ingredients_number").asInt();
        PersonalDebug.printMsg("\ningredients_number: " + numIngredients);
        final JsonNode ingredientsNode = rootNode.get("ingredients");
        for (int i = 1; i <= numIngredients; ++i) {
            final JsonNode ingredient = ingredientsNode.get("ingredient-" + i);
            PersonalDebug.printMsg(
                    "\nIngredient: " + i + "\n\tingredient_name: " + ingredient.get("ingredient_name").asText()
                            + "\n\tingredient_amount: " + ingredient.get("ingredient_amount").asText());
        }

        final int numTools = rootNode.get("tool_number").asInt();
        PersonalDebug.printMsg("\ntool_number: " + numSteps);
        final JsonNode toolsNode = rootNode.get("tools");
        for (int i = 1; i <= numTools; ++i) {
            final JsonNode tool = toolsNode.get("tool-" + i);
            PersonalDebug.printMsg("\nTool: " + i + "\n\ttool_name: " + tool.get("tool_name").asText()
                    + "\n\tdifficulty_level: " + tool.get("difficulty_level").asText());
        }
    }
}
