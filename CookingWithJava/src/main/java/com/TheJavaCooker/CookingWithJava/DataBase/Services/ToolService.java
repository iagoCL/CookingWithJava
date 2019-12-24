package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.DifficultyLevel;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Tool;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.ToolRepository;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ToolService {
    @Autowired
    private ToolRepository toolRepository;

    static DatabaseService.Errors checkTools(List<Pair<String, String>> toolsList) {
        List<String> tools = new ArrayList<>();
        for (Pair<String, String> tool : toolsList) {
            String ingredientName = tool.getFirst().toLowerCase();
            if (ingredientName.isEmpty()) {
                PersonalDebug.printMsg("Null tool name: " + ingredientName);
                return DatabaseService.Errors.NULL_TOOL_NAME;
            } else if (tools.contains(ingredientName)) {
                PersonalDebug.printMsg("Repeated tool name: " + ingredientName);
                return DatabaseService.Errors.REPEATED_TOOL_NAME;
            } else {
                tools.add(ingredientName);
            }
        }
        return DatabaseService.Errors.WITHOUT_ERRORS;
    }

    Pair<DatabaseService.Errors, Tool> createTool(String nameTool_, String difficultyLevel_, Recipe recipe_) {
        Tool tool = new Tool(nameTool_, DifficultyLevel.fromString(difficultyLevel_), recipe_);
        if (nameTool_.isEmpty()) {
            PersonalDebug.printMsg("Null tool name: " + nameTool_);
            return Pair.of(DatabaseService.Errors.NULL_TOOL_NAME, tool);
        } else if (recipe_ == null) {
            PersonalDebug.printMsg("Null Recipe");
            return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, tool);
        } else {
            try {
                toolRepository.save(tool);
            } catch (org.springframework.dao.DataIntegrityViolationException exception) {
                if (exception.toString().contains(Tool.constraintToolName)) {
                    PersonalDebug.printMsg("Repeated tool name: " + exception.toString());
                    return Pair.of(DatabaseService.Errors.REPEATED_TOOL_NAME, tool);
                } else {
                    PersonalDebug.printMsg(UserService.unknownConstraint + exception.toString());
                    return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, tool);
                }

            } catch (Exception exception) {
                PersonalDebug.printMsg(UserService.unknownException + exception.toString());
                return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, tool);
            }
            recipe_.getTools().add(tool);
            return Pair.of(DatabaseService.Errors.WITHOUT_ERRORS, tool);
        }
    }

    void deleteAll() {
        toolRepository.deleteAll();
    }
}